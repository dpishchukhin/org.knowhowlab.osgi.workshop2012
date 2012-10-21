package org.knowhowlab.osgi.workshop2012.firealarm.api.environment;

/**
 * @author dpishchukhin
 */
public interface EnvironmentStatus {
    boolean hasFog();

    float getCurrentTemperature();
}
