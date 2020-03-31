package server.events.gm.MapleBossBattle;

import client.MapleCharacter;
import constants.ExpTable;
import net.server.Server;
import server.events.gm.core.EventStep;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.MaplePacketCreator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BossBattleStep extends EventStep {
    public static final int BOSS_BATTLE_TIME = 1; // 30 minutes
    public static final int ZAKUM_ID = 8800000;
    private MapleMap map;

    public BossBattleStep(MapleMap map) {
        this.map = map;
    }

    // Impl abstract method
    protected void executeStep()  throws InterruptedException {
        map.broadcastMessage(MaplePacketCreator.getClock(BOSS_BATTLE_TIME * 60));
        ArrayList<MapleMonster> zakumMonsters = createBossBattleZakum();
        Thread.sleep(BOSS_BATTLE_TIME * 60 * 1000);
        map.broadcastMessage(MaplePacketCreator.removeClock());
        map.killAllMonsters();
        showDamageRanking(zakumMonsters);
        rewardExpBasedOnNumKilled(zakumMonsters);
    }

    private ArrayList<MapleMonster> createBossBattleZakum() throws InterruptedException{
        MapleMonster zakumBody = MapleLifeFactory.getBossBattleMonster(ZAKUM_ID);
        Point zakumSpawnPoint = new Point(43, -217);
        map.spawnMonsterOnGroundBelow(zakumBody, zakumSpawnPoint);

        ArrayList<MapleMonster> zakumMonsters = new ArrayList<MapleMonster>();
        zakumMonsters.add(zakumBody);
        Thread.sleep(5);
        for (int zakumArmId = 8800003; zakumArmId < 8800011; zakumArmId++) {
            MapleMonster zakumArm = MapleLifeFactory.getBossBattleMonster(zakumArmId);
            map.spawnMonsterOnGroundBelow(zakumArm, zakumSpawnPoint);
            zakumMonsters.add(zakumArm);
        }
        return zakumMonsters;
    }

    private void showDamageRanking(ArrayList<MapleMonster> monsters) {

    }

    private void rewardExpBasedOnNumKilled(ArrayList<MapleMonster> monsters) {
        List<MapleCharacter> chars = new ArrayList<>(map.getCharacters());
        for (MapleMonster monster : monsters) {
            if (monster.getHp() == 0) {
                for (MapleCharacter chr : chars) {
                    rewardExp(chr, monsters.size(), monster);
                }
            }
        }
    }

    private void rewardExp(MapleCharacter chr, int numMonsters, MapleMonster monster) {
        int requiredExp = ExpTable.getExpNeededForLevel(chr.getLevel());
        // Modified Sigmoid Exp % curve
        long exp = Math.round(
                requiredExp * (2.5 / (1 + Math.exp(
                        2.5 * ((double)chr.getLevel())/ExpTable.MAX_LEVEL
                )))
        ) / (numMonsters == 0 ? 1 : numMonsters);

        exp = exp == 0 ? 1 : exp;
        chr.gainExpNoModifiers(exp, true, true, true);
        requiredExp = ExpTable.getExpNeededForLevel(chr.getLevel());
        if (monster.getId() == 8800000) {
            chr.gainExpNoModifiers((long) ((long)requiredExp * 0.5), true, true, true);
        }
    }
}
