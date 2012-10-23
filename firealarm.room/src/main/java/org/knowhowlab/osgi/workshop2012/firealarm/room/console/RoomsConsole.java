package org.knowhowlab.osgi.workshop2012.firealarm.room.console;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironment;
import org.knowhowlab.osgi.workshop2012.firealarm.room.internal.RoomEnvironmentManipulator;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dpishchukhin
 */
@Component(specVersion = "1.1", name = "firealarm.rooms.console", label = "Rooms Console", description = "Rooms Console", immediate = true)
@Service(RoomsConsole.class)
@Properties({
        @Property(name = CommandProcessor.COMMAND_SCOPE, value = "firealarm"),
        @Property(name = CommandProcessor.COMMAND_FUNCTION, value = {"lr", "fire"})
})
public class RoomsConsole {
    @Reference(referenceInterface = RoomEnvironment.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC,
            bind = "bindRoom", unbind = "unbindRoom")
    private Map<String, RoomEnvironment> rooms = new HashMap<String, RoomEnvironment>();

    @Descriptor("list all rooms")
    public void lr() {
        for (String roomId : rooms.keySet()) {
            RoomEnvironment env = rooms.get(roomId);
            System.out.println(String.format("Room %s - fog: %s, temp: %sC", roomId, env.hasFog(), env.getCurrentTemperature()));
        }
    }

    @Descriptor("emulate fire in a room")
    public void fire(
            @Descriptor("room id") @Parameter(names = {"-r", "--room"}, absentValue = Parameter.UNSPECIFIED) String roomId,
            @Descriptor("activate fog") @Parameter(names = {"-f", "--fog"}, presentValue = "true", absentValue = Parameter.UNSPECIFIED) boolean fog
    ) {
        RoomEnvironment room = rooms.get(roomId);
        if (room == null) {
            System.out.println("Unknown room ID: " + roomId);
        } else if (!(room instanceof RoomEnvironmentManipulator)) {
            System.out.println("Unable change env for room ID: " + roomId);
        } else {
            RoomEnvironmentManipulator manipulator = (RoomEnvironmentManipulator) room;
            manipulator.activateFog(fog);
            manipulator.setActualTemperature(100);
            System.out.println("Env changed for room ID: " + roomId);
        }
    }

    protected void bindRoom(RoomEnvironment env, Map<String, Object> props) {
        rooms.put((String) props.get(Constants.ROOM_ID_PROP), env);
    }

    protected void unbindRoom(RoomEnvironment env, Map<String, Object> props) {
        rooms.remove(props.get(Constants.ROOM_ID_PROP));
    }
}
