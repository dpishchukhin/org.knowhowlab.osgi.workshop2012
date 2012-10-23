package org.knowhowlab.osgi.workshop2012.firealarm.core.console;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.knowhowlab.osgi.workshop2012.firealarm.core.internal.EnvironmentManager;
import org.osgi.service.component.ComponentContext;

/**
 * @author dpishchukhin
 */
@Component(specVersion = "1.1", name = "firealarm.system.console", label = "Fire Alarm System Console", description = "Fire Alarm System Console",
        immediate = true)
@Service(FireAlarmSystemConsole.class)
@Properties({
        @Property(name = CommandProcessor.COMMAND_SCOPE, value = "firealarm"),
        @Property(name = CommandProcessor.COMMAND_FUNCTION, value = {"status"})
})
public class FireAlarmSystemConsole {
    private EnvironmentManager environmentManager;

    @Activate
    protected void activate(ComponentContext ctx) {
        setEnvironmentManager(new EnvironmentManager(ctx.getBundleContext()));
    }

    protected void setEnvironmentManager(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    @Descriptor("print fire alarm system status")
    public void status() {
        for (String roomId : environmentManager.roomIds()) {
            String roomDescription = environmentManager.getRoomDescription(roomId);
            System.out.println(roomDescription);

            for (String applianceId : environmentManager.getAllFireApplianceIdsByRoomId(roomId)) {
                System.out.println(String.format("%s - %s", environmentManager.getApplianceDescription(applianceId), environmentManager.isApplianceActivated(applianceId)));
            }
            System.out.println("---\n");
        }
    }
}
