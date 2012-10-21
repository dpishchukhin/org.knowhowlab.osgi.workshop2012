package org.knowhowlab.osgi.workshop2012.firealarm.core;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.Descriptor;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.FireAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironment;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dpishchukhin
 */
@Component(specVersion = "1.1", name = "firealarm.core", label = "Fire Alarm System", description = "Fire Alarm System",
        metatype = true, immediate = true)
@Service({EventHandler.class, CoreComponent.class})
@Properties({
        @Property(name = EventConstants.EVENT_TOPIC, value = "firealarm"),
        @Property(name = CommandProcessor.COMMAND_SCOPE, value = "firealarm"),
        @Property(name = CommandProcessor.COMMAND_FUNCTION, value = {"status"})
})
public class CoreComponent implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(CoreComponent.class);

    @Reference(referenceInterface = RoomEnvironment.class,
            cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC,
            bind = "bindRoom", unbind = "unbindRoom")
    private Map<String, String> rooms = new HashMap<String, String>();

    protected ComponentContext ctx;

    @Activate
    protected void activate(ComponentContext ctx) {
        this.ctx = ctx;
    }

    @Deactivate
    protected void deactivate(ComponentContext ctx) {
        this.ctx = null;
    }

    @Override
    public void handleEvent(Event event) {
        System.out.println(event);
    }

    @Descriptor("print fire alarm system status")
    public void status() {
        for (String roomId : rooms.keySet()) {
            String roomDescription = rooms.get(roomId);
            System.out.println(roomDescription);
            try {
                ServiceReference[] applianceReferences = ctx.getBundleContext().getServiceReferences(FireAppliance.class.getName(), String.format("(%s=%s)", Constants.ROOM_ID_PROP, roomId));
                if (applianceReferences != null) {
                    for (ServiceReference reference : applianceReferences) {
                        FireAppliance appliance = (FireAppliance) ctx.getBundleContext().getService(reference);
                        try {
                            System.out.println(String.format("%s - %s", reference.getProperty(Constants.DESCRIPTION_PROP), appliance.isActivated()));
                        } finally {
                            ctx.getBundleContext().ungetService(reference);
                        }
                    }
                }
                System.out.println("---\n");
            } catch (InvalidSyntaxException e) {
                LOG.warn("Unable to process", e);
            }
        }
    }

    protected void bindRoom(RoomEnvironment env, Map<String, Object> props) {
        rooms.put((String) props.get(Constants.ROOM_ID_PROP), (String) props.get(Constants.DESCRIPTION_PROP));
    }

    protected void unbindRoom(RoomEnvironment env, Map<String, Object> props) {
        rooms.remove(props.get(Constants.ROOM_ID_PROP));
    }

}
