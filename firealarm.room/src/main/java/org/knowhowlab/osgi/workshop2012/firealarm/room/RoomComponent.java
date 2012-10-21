package org.knowhowlab.osgi.workshop2012.firealarm.room;

import org.apache.felix.scr.annotations.*;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.EnvironmentStatus;
import org.knowhowlab.osgi.workshop2012.firealarm.room.internal.EnvironmentManipulator;

/**
 * @author dpishchukhin
 */
@Component(specVersion = "1.1", name = "firealarm.room", label = "Room", description = "Room",
        metatype = true, configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, immediate = true)
@Service(EnvironmentStatus.class)
@Properties({
        @Property(name = Constants.ROOM_ID, label = "Room ID", description = "Room ID")
})
public class RoomComponent implements EnvironmentManipulator {
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
    public void activateFog(boolean activate) {
        this.fog = activate;
    }

    @Override
    public void setActualTemperature(float temperature) {
        this.actualTemperature = temperature;
    }
}
