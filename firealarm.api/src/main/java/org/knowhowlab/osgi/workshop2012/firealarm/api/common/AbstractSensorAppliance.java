package org.knowhowlab.osgi.workshop2012.firealarm.api.common;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dpishchukhin
 */
@Reference(name = AbstractSensorAppliance.EVENT_ADMIN_REFERENCE_NAME, referenceInterface = EventAdmin.class,
        cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.STATIC,
        bind = "bindEventAdmin", unbind = "unbindEventAdmin")
public abstract class AbstractSensorAppliance extends AbstractFireAppliance {
    public static final String EVENT_ADMIN_REFERENCE_NAME = "ea.reference";

    private EventAdmin eventAdmin;

    protected void bindEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    protected void unbindEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = null;
    }

    /**
     * Sends alarm activation event
     *
     * @param details alarm details
     */
    protected void activateAlarm(String details) {
        activated = true;
        eventAdmin.postEvent(createAlarmEvent(true, details));
    }

    /**
     * Sends alarm deactivation event
     */
    protected void deactivateAlarm() {
        activated = false;
        eventAdmin.postEvent(createAlarmEvent(false, null));
    }

    private Event createAlarmEvent(boolean active, String details) {
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(Constants.ROOM_ID_PROP, roomId);
        props.put(Constants.SENSOR_ID_PROP, applianceId);
        props.put(Constants.DESCRIPTION_PROP, description);
        if (details != null) {
            props.put(Constants.DETAILS_PROP, details);
        }
        props.put(Constants.ACTIVE_ALARM, active);
        return new Event(Constants.TOPIC, props);
    }
}
