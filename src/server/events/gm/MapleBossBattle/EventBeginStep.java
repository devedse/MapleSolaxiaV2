package server.events.gm.MapleBossBattle;

import net.server.Server;
import server.events.gm.MapleEvent;
import server.events.gm.core.EventStep;
import server.maps.MapleMap;
import tools.MaplePacketCreator;

public class EventBeginStep extends EventStep {
    public static final String EVENT_NOTICE = "[EVENT] - Boss Battle will be starting in %d minutes! Please see Paul, Jean, Martin, or Tony to join!!";
    public static final int NOTICE_MINUTES = 1;

    MapleMap map;
    MapleEvent event;

    public EventBeginStep(MapleMap map, MapleEvent event) {
        this.map = map;
        this.event = event;
    }

    // Impl abstract method
    protected void executeStep()  throws InterruptedException {
        event.openEntry();
        for(int i = NOTICE_MINUTES; i > 0; i--) {
            Server.getInstance().broadcastMessage(
                    MaplePacketCreator.serverNotice(6, String.format(EVENT_NOTICE, i)));
            Thread.sleep(60 * 1000);
        }
        event.closeEntry();
    }
}
