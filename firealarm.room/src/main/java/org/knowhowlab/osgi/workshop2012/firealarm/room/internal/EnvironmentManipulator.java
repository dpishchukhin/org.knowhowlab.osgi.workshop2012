package org.knowhowlab.osgi.workshop2012.firealarm.room.internal;

import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.EnvironmentStatus;

/**
 * @author dpishchukhin
 */
public interface EnvironmentManipulator extends EnvironmentStatus {
    void activateFog(boolean activate);

    void setActualTemperature(float temperature);
}
