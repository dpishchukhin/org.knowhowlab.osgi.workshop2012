package org.knowhowlab.osgi.workshop2012.firealarm.api.environment;

/**
 * @author dpishchukhin
 */
public interface RoomEnvironmentManipulator extends RoomEnvironment {
    void activateFog(boolean activate);

    void setActualTemperature(float temperature);
}
