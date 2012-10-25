package org.knowhowlab.osgi.workshop2012.firealarm.room.console;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.apache.felix.service.command.Parameter;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironment;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironmentManipulator;

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
    @Reference(referenceInterface = RoomEnvironmentManipulator.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC,
            bind = "bindRoom", unbind = "unbindRoom")
    private Map<String, RoomEnvironmentManipulator> rooms = new HashMap<String, RoomEnvironmentManipulator>();

    @Descriptor("list all rooms")
    public void lr() {
        for (String roomId : rooms.keySet()) {
            RoomEnvironment env = rooms.get(roomId);
            System.out.println(String.format("Room %s - smoke: %s, temp: %sC", roomId, env.hasSmoke(), env.getCurrentTemperature()));
        }
    }

    @Descriptor("emulate fire in a room")
    public void fire(
            @Descriptor("room id") @Parameter(names = {"-r", "--room"}, absentValue = Parameter.UNSPECIFIED) String roomId,
            @Descriptor("activate smoke") @Parameter(names = {"-s", "--smoke"}, presentValue = "true", absentValue = Parameter.UNSPECIFIED) boolean smoke
    ) {
        RoomEnvironmentManipulator room = rooms.get(roomId);
        if (room == null) {
            System.out.println("Unknown room ID: " + roomId);
        } else {
            room.activateSmoke(smoke);
            room.setActualTemperature(100);
            System.out.println("Env changed for room ID: " + roomId);
        }
    }

    protected void bindRoom(RoomEnvironmentManipulator env, Map<String, Object> props) {
        rooms.put((String) props.get(Constants.ROOM_ID_PROP), env);
    }

    protected void unbindRoom(RoomEnvironmentManipulator env, Map<String, Object> props) {
        rooms.remove(props.get(Constants.ROOM_ID_PROP));
    }
}
