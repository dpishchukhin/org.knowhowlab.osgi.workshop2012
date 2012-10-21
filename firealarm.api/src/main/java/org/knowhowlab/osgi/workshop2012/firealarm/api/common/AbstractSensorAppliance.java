package org.knowhowlab.osgi.workshop2012.firealarm.api.common;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.ReferencePolicy;
import org.apache.felix.scr.annotations.ReferenceStrategy;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dpishchukhin
 */
@Reference(name = AbstractSensorAppliance.EVENT_ADMIN_REFERENCE_NAME, referenceInterface = EventAdmin.class,
        cardinality = ReferenceCardinality.MANDATORY_UNARY, policy = ReferencePolicy.DYNAMIC, strategy = ReferenceStrategy.LOOKUP)
public abstract class AbstractSensorAppliance extends AbstractFireAppliance {
    public static final String EVENT_ADMIN_REFERENCE_NAME = "ea.reference";

    protected void activateAlarm(String details) {
        activated = true;
        EventAdmin eventAdmin = (EventAdmin) ctx.locateService(EVENT_ADMIN_REFERENCE_NAME);
        eventAdmin.postEvent(createAlarmEvent(true, details));
    }

    protected void deactivateAlarm() {
        activated = false;
        EventAdmin eventAdmin = (EventAdmin) ctx.locateService(EVENT_ADMIN_REFERENCE_NAME);
        eventAdmin.postEvent(createAlarmEvent(false, null));
    }

    private Event createAlarmEvent(boolean active, String details) {
        Map props = new HashMap();
        props.put(Constants.ROOM_ID_PROP, ctx.getProperties().get(Constants.ROOM_ID_PROP));
        props.put(Constants.SENSOR_ID_PROP, String.valueOf(ctx.getProperties().get(ComponentConstants.COMPONENT_ID)));
        props.put(Constants.DESCRIPTION_PROP, ctx.getProperties().get(Constants.DESCRIPTION_PROP));
        if (details != null) {
            props.put(Constants.DETAILS_PROP, details);
        }
        props.put(Constants.ACTIVE_ALARM, active);
        return new Event(Constants.TOPIC, props);
    }

}
