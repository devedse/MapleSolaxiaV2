/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation version 3 as published by
 the Free Software Foundation. You may not use, modify or distribute
 this program under any other version of the GNU Affero General Public
 License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.server.handlers.login;

import java.util.Calendar;

import java.sql.Connection;
import java.sql.PreparedStatement;

import net.MaplePacketHandler;
import server.TimerManager;
import tools.HexTool;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import java.io.UnsupportedEncodingException;
import client.MapleClient;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.security.NoSuchAlgorithmException;
import tools.DatabaseConnection;

import java.security.MessageDigest;

public final class LoginPasswordHandler implements MaplePacketHandler {

    @Override
    public boolean validateState(MapleClient c) {
        return !c.isLoggedIn();
    }
    
    private static String hashpwSHA512(String pwd) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest digester = MessageDigest.getInstance("SHA-512");
        digester.update(pwd.getBytes("UTF-8"), 0, pwd.length());
        return HexTool.toString(digester.digest()).replace(" ", "").toLowerCase();
    }

    @Override
    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        System.out.println("Inlog man!!!\r\n");

        String login = slea.readMapleAsciiString();
        String pwd = slea.readMapleAsciiString();
        c.setAccountName(login);
        
        int loginok = c.login(login, pwd);

        Connection con = null;
        PreparedStatement ps = null;

        System.out.println("Loginok: " + loginok);

        if (loginok == 5) {
            try {
                con = DatabaseConnection.getConnection();
                ps = con.prepareStatement("INSERT INTO accounts (name, password, salt, birthday, tempban, verified, gm, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS); //Jayd: Added birthday, tempban
                ps.setString(1, login);
                ps.setString(2, hashpwSHA512(pwd + "saltje"));
                ps.setString(3, "saltje");
                ps.setString(4, "2018-06-20"); //Jayd's idea: was added to solve the MySQL 5.7 strict checking (birthday)
                ps.setString(5, "2018-06-20"); //Jayd's idea: was added to solve the MySQL 5.7 strict checking (tempban)
                ps.setBoolean(6, true);
                ps.setByte(7, (byte)6);
                ps.setString(8, login + "jaaahoah@bestaaatttnieeet.com");
                ps.executeUpdate();
                
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                c.setAccID(rs.getInt(1));
                rs.close();
            } catch (SQLException | NoSuchAlgorithmException | UnsupportedEncodingException e) {
                c.setAccID(-1);
                e.printStackTrace();
            } finally {
                disposeSql(con, ps);
                loginok = c.login(login, pwd);
            }
        }

        
        if (c.hasBannedIP() || c.hasBannedMac()) {
            c.announce(MaplePacketCreator.getLoginFailed(3));
            return;
        }
        Calendar tempban = c.getTempBanCalendar();
        if (tempban != null) {
            if (tempban.getTimeInMillis() > System.currentTimeMillis()) {
                c.announce(MaplePacketCreator.getTempBan(tempban.getTimeInMillis(), c.getGReason()));
                return;
            }
        }
        if (loginok == 3) {
            c.announce(MaplePacketCreator.getPermBan(c.getGReason()));//crashes but idc :D
            return;
        } else if (loginok != 0) {
            c.announce(MaplePacketCreator.getLoginFailed(loginok));
            return;
        }
        if (c.finishLogin() == 0) {
            login(c);
        } else {
            c.announce(MaplePacketCreator.getLoginFailed(7));
        }
    }
    
    private static void login(MapleClient c){
        c.announce(MaplePacketCreator.getAuthSuccess(c));//why the fk did I do c.getAccountName()?
        final MapleClient client = c;
        c.setIdleTask(TimerManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                client.disconnect(false, false);
            }
        }, 600000));
    }

    private static void disposeSql(Connection con, PreparedStatement ps) {
        try {
            if (con != null) {
                con.close();
            }

            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
