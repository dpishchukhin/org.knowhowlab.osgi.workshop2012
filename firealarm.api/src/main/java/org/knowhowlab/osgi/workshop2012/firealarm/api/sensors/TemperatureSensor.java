package org.knowhowlab.osgi.workshop2012.firealarm.api.sensors;

/**
 * @author dpishchukhin
 */
public interface TemperatureSensor {
    boolean isActivated();

    float getActualTemperature();

    float getActivationTemperatureLimit();

    void setActivationTemperatureLimit(float limit);

    float getDeactivationTemperatureLimit();

    void setDeactivationTemperatureLimit(float limit);
}
