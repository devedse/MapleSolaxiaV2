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
package server.events.gm.MapleOxQuiz;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;

import client.MapleCharacter;

import server.TimerManager;
import server.maps.MapleMap;

import server.events.gm.MapleEvent;
import server.events.gm.MapleBossBattle.EventBeginStep;
import server.events.gm.MapleBossBattle.EventDescriptionStep;
import server.events.gm.MapleBossBattle.BossBattleStep;
//import server.events.gm.MapleBossBattle.EventOverStep;

import tools.Randomizer;
import tools.MaplePacketCreator;
/**
 *
 * @author FloppyDisk
 * @author tzwang1
 */
public final class MapleBossBattle extends MapleEvent {
    public static final int MAPLE_BOSS_BATTLE_MAP_ID = 280030000;
    // private static MapleDataProvider stringData = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/Etc.wz"));

    public MapleBossBattle(MapleMap map, int limit) {
        super(map, limit);
    }

    public void startEvent() {
        // Event Stages
        runner.register(new EventBeginStep(map, this));
        runner.register(new EventDescriptionStep(map));
        runner.register(new BossBattleStep(map));
//        runner.register(new EventOverStep(map));

        // Start Event
        TimerManager.getInstance().schedule(runner, 0);
    }
}
