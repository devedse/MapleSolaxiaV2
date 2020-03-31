
package server.events.gm.MapleBossBattle;


import net.server.Server;
import server.maps.MapleMap;
import server.events.gm.core.EventStep;
import tools.MaplePacketCreator;

public class EventDescriptionStep extends EventStep {
    public static final int MAP_EFFECT = 5120016;
    public static final int PREP_TIME = 2;
    public static final int BATTLE_TIME = 30;
    public static final String[] DESC = {
            "Welcome to Boss Battle!",
            "Today you'll be battling Zakum!",
            "You'll have " + PREP_TIME + " minutes to prepare your parties and " + BATTLE_TIME + " minutes to kill Zakum.",
            "Good luck out there!"
    };

    MapleMap map;

    public EventDescriptionStep(MapleMap map) {
        this.map = map;
    }

    // Impl abstract method
    protected void executeStep()  throws InterruptedException {
        for(String desc : DESC) {
            map.startMapEffect(desc, MAP_EFFECT, 7 * 1000);
            Thread.sleep(8 * 1000);
        }
        map.broadcastMessage(MaplePacketCreator.getClock(PREP_TIME * 60));
        Thread.sleep(PREP_TIME * 60 * 1000);
    }
}

