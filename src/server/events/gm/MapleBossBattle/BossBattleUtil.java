package server.events.gm.MapleBossBattle;

import server.life.MapleLifeFactory;
import server.life.MapleMonster;

public class BossBattleUtil {
    public static MapleMonster createBossBattleMonster(int id) {
        MapleMonster mob = MapleLifeFactory.getMonster(id);
        mob.disableDrops();
        return mob;
    }
}
