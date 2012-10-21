package org.knowhowlab.osgi.workshop2012.firealarm.room.internal;

import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironment;

/**
 * @author dpishchukhin
 */
public interface RoomEnvironmentManipulator extends RoomEnvironment {
    void activateFog(boolean activate);

    void setActualTemperature(float temperature);
}
