package org.knowhowlab.osgi.workshop2012.firealarm.api.environment;

/**
 * @author dpishchukhin
 */
public interface RoomEnvironment {
    boolean hasFog();

    float getCurrentTemperature();

    void extinguish();
}
