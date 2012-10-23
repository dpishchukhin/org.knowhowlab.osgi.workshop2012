package org.knowhowlab.osgi.workshop2012.firealarm.core;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

/**
 * @author dpishchukhin
 */
@Component(specVersion = "1.1", name = "firealarm.system", label = "Fire Alarm System", description = "Fire Alarm System",
        metatype = true, immediate = true)
@Service({EventHandler.class, FireAlarmSystemComponent.class})
@Properties({
        @Property(name = EventConstants.EVENT_TOPIC, value = "firealarm"),
        @Property(name = CommandProcessor.COMMAND_SCOPE, value = "firealarm"),
        @Property(name = CommandProcessor.COMMAND_FUNCTION, value = {"status"})
})
public class FireAlarmSystemComponent extends AlarmSystemStateMachine implements EventHandler {
    private EnvironmentManager environmentManager;

    @Activate
    protected void activate(ComponentContext ctx) {
        setEnvironmentManager(new EnvironmentManager(ctx.getBundleContext()));
    }

    protected void setEnvironmentManager(EnvironmentManager environmentManager) {
        this.environmentManager = environmentManager;
    }

    @Override
    public void handleEvent(Event event) {
        AlarmSystemFeedback feedback = handleAlarmEvent((String) event.getProperty(Constants.ROOM_ID_PROP),
                (String) event.getProperty(Constants.SENSOR_ID_PROP),
                (Boolean) event.getProperty(Constants.ACTIVE_ALARM));
        processSystemFeedback(feedback);
    }

    private void processSystemFeedback(AlarmSystemFeedback feedback) {
        changeGlobalActionAppliancesState(feedback.isActivateGlobal());
        for (String roomId : feedback.getActiveAlarmRooms()) {
            changeRoomActionAppliancesState(roomId, true);
        }
        for (String roomId : feedback.getInactiveAlarmRooms()) {
            changeRoomActionAppliancesState(roomId, false);
        }
    }

    private void changeRoomActionAppliancesState(String roomId, boolean activate) {
        for (String applianceId : environmentManager.getActionApplianceIdsByRoomId(roomId, true)) {
            environmentManager.activateActionAppliance(applianceId, activate);
        }
    }

    private void changeGlobalActionAppliancesState(boolean activate) {
        for (String applianceId : environmentManager.getGlobalActionApplianceIds()) {
            environmentManager.activateActionAppliance(applianceId, activate);
        }
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
