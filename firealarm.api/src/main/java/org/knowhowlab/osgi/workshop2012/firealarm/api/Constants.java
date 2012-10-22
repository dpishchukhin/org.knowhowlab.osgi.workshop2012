package org.knowhowlab.osgi.workshop2012.firealarm.api;

/**
 * @author dpishchukhin
 */
public class Constants {
    /**
     * Room ID (as registration property, config property, event alarm property)
     */
    public static final String ROOM_ID_PROP = "room.id";
    /**
     * Description of an appliance or environment
     */
    public static final String DESCRIPTION_PROP = "description";
    /**
     * Service Registration property for ActionAppliance - should be activated on alarm in any room, or only where this application is placed
     */
    public static final String GLOBAL_PROP = "global";
    /**
     * Events topic
     */
    public static final String TOPIC = "firealarm";
    /**
     * Event alarm state property: <code>true</code> or <code>false</code>
     */
    public static final String ACTIVE_ALARM = "alarm";
    /**
     * Event alarm details property
     */
    public static final String DETAILS_PROP = "details";
    /**
     * Event alarm Sensor ID
     */
    public static final String SENSOR_ID_PROP = "sensor.id";
}
