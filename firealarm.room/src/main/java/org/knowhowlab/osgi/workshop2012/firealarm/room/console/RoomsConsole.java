package org.knowhowlab.osgi.workshop2012.firealarm.room.console;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.EnvironmentStatus;
import org.knowhowlab.osgi.workshop2012.firealarm.room.internal.EnvironmentManipulator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dpishchukhin
 */
@Component(specVersion = "1.1", name = "firealarm.rooms.console", label = "Rooms Console", description = "Rooms Console", immediate = true)
@Service(RoomsConsole.class)
@Properties({
        @Property(name = CommandProcessor.COMMAND_SCOPE, value = "firealarm"),
        @Property(name = CommandProcessor.COMMAND_FUNCTION, value = {"lr", "chroom"})
})
public class RoomsConsole {
    @Reference(referenceInterface = EnvironmentStatus.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC,
            bind = "bindRoom", unbind = "unbindRoom")
    private Map<String, EnvironmentStatus> rooms = new HashMap<String, EnvironmentStatus>();

    @Descriptor("list all rooms")
    public void lr() {
        for (String roomId : rooms.keySet()) {
            EnvironmentStatus env = rooms.get(roomId);
            System.out.println(String.format("Room %s - fog: %s, temp: %sC", roomId, env.hasFog(), env.getCurrentTemperature()));
        }
    }

    @Descriptor("change room environment")
    public void chroom(
            @Descriptor("room id") @Parameter(names = {"-r", "--room"}, absentValue = Parameter.UNSPECIFIED) String roomId,
            @Descriptor("activate fog") @Parameter(names = {"-f", "--fog"}, presentValue = "true", absentValue = Parameter.UNSPECIFIED) boolean fog,
            @Descriptor("activate fog") @Parameter(names = {"-t", "--temp"}, absentValue = Parameter.UNSPECIFIED) float temp
    ) {
        EnvironmentStatus room = rooms.get(roomId);
        if (room == null) {
            System.out.println("Unknown room ID: " + roomId);
        } else if (!(room instanceof EnvironmentManipulator)) {
            System.out.println("Unable change env for room ID: " + roomId);
        } else {
            EnvironmentManipulator manipulator = (EnvironmentManipulator) room;
            manipulator.activateFog(fog);
            manipulator.setActualTemperature(temp);
            System.out.println("Env changed for room ID: " + roomId);
        }
    }

    protected void bindRoom(EnvironmentStatus env, Map<String, Object> props) {
        rooms.put((String) props.get(Constants.ROOM_ID), env);
    }

    protected void unbindRoom(EnvironmentStatus env, Map<String, Object> props) {
        rooms.remove(props.get(Constants.ROOM_ID));
    }
}
