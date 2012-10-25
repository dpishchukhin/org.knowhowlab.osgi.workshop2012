package org.knowhowlab.osgi.workshop2012.firealarm.api.environment;

/**
 * Room Environment emulation
 *
 * @author dpishchukhin
 */
public interface RoomEnvironment {
    /**
     * Is there smoke in the room
     * @return <code>true</code> or <code>false</code>
     */
    boolean hasSmoke();

    /**
     * Get current temperature in the room
     * @return in Celsius
     */
    float getCurrentTemperature();

    /**
     * Emulate extinguish action
     */
    void extinguish();
}
