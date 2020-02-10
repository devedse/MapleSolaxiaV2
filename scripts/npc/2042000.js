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

/* Edited by: Benjixd
    NPC Name: Spiegelmann
    Description: Monster Carnival Assistant & CP1 host
*/

var status = 0;
var CPQ_MAP = 980000000;
var CPQ2_MAP = 980030000;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if(mode == -1) {
        cm.dispose();
        return;
    }

    if (status >= 2 && mode == 0) {
        cm.dispose();
        return;
    }

    if(mode == 1) {
        status++;
    } else {
        status--;
    }

    if(cm.getPlayer().getMapId() == CPQ_MAP) {
        spiegelmannInOfficeCPQ1(mode, type, selection);
    } else {
        spiegelmannInTown(mode, type, selection);
    }
}

function spiegelmannInTown(mode, type, selection) {
    if(status == 1) {
        cm.sendNext("Haha! I am Spiegelmann, the creator of this Monster Carnival. Would you like to try it out?");
    } else if(status == 2) {
        cm.sendSimple("Which monster carnival would you like to participate in?\r\n#L0##e1.#n#b The Monster Carnival#k#l\r\n#L1##e2.#n#b The 2nd Monster Carnival#k#l");
    } else if (status == 3) {
        cm.getPlayer().saveLocation("MIRROR");
        if(selection == 0) {
            cm.warp(CPQ_MAP);
        } else if(selection == 1) {
            cm.warp(CPQ2_MAP);
        }
        cm.sendNext("Good luck out there!");
        cm.dispose();
    }
}

function spiegelmannInOfficeCPQ1(mode, type, selection) {
    if(status == 1) {
        cm.sendOk("Hello");
        cm.dispose();
    }
}