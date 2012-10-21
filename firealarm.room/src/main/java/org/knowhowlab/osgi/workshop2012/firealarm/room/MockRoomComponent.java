package org.knowhowlab.osgi.workshop2012.firealarm.room;

import org.apache.felix.scr.annotations.*;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironment;
import org.knowhowlab.osgi.workshop2012.firealarm.room.internal.RoomEnvironmentManipulator;

/**
 * @author dpishchukhin
 */
@Component(specVersion = "1.1", name = "firealarm.room", label = "Mock Room", description = "Mock Room",
        metatype = true, configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, immediate = true)
@Service(RoomEnvironment.class)
@Properties({
        @Property(name = Constants.ROOM_ID_PROP, label = "Room ID", description = "Room ID"),
        @Property(name = Constants.DESCRIPTION_PROP, label = "Room Description", description = "Room Description")
})
public class MockRoomComponent implements RoomEnvironmentManipulator {
    public static final int MINIMUM_TEMP = 20;

    private boolean fog;
    private float actualTemperature;

    @Override
    public boolean hasFog() {
        return fog;
    }

    @Override
    public float getCurrentTemperature() {
        return actualTemperature;
    }

    @Override
    public void extinguish() {
        if (actualTemperature > MINIMUM_TEMP) {
            actualTemperature--;
        } else if (fog) {
            fog = false;
        }
    }

    @Override
    public void activateFog(boolean activate) {
        this.fog = activate;
    }

    @Override
    public void setActualTemperature(float temperature) {
        this.actualTemperature = temperature;
    }
}
