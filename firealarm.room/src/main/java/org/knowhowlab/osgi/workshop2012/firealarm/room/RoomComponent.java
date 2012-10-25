package org.knowhowlab.osgi.workshop2012.firealarm.room;

import org.apache.felix.scr.annotations.*;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironment;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironmentManipulator;

/**
 * @author dpishchukhin
 */
@Component(specVersion = "1.1", name = "firealarm.room", label = "Mock Room", description = "Mock Room",
        metatype = true, configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, immediate = true)
@Service({RoomEnvironment.class, RoomEnvironmentManipulator.class})
@Properties({
        @Property(name = Constants.ROOM_ID_PROP, label = "Room ID", description = "Room ID"),
        @Property(name = Constants.DESCRIPTION_PROP, label = "Room Description", description = "Room Description")
})
public class RoomComponent implements RoomEnvironmentManipulator {
    public static final float MINIMUM_TEMP = 20;

    private boolean smoke;
    private float actualTemperature = MINIMUM_TEMP;

    @Override
    public boolean hasSmoke() {
        return smoke;
    }

    @Override
    public float getCurrentTemperature() {
        return actualTemperature;
    }

    @Override
    public void extinguish() {
        if (actualTemperature > MINIMUM_TEMP) {
            actualTemperature--;
        } else if (smoke) {
            smoke = false;
        }
    }

    @Override
    public void activateSmoke(boolean activate) {
        this.smoke = activate;
    }

    @Override
    public void setActualTemperature(float temperature) {
        this.actualTemperature = temperature;
    }
}
