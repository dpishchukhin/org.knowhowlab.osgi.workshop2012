package org.knowhowlab.osgi.workshop2012.firealarm.api.common;

import org.apache.felix.scr.annotations.*;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.FireAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironment;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dpishchukhin
 */
@Properties({
        @Property(name = Constants.ROOM_ID_PROP, label = "Room ID", description = "Room ID"),
        @Property(name = Constants.DESCRIPTION_PROP, label = "Appliance Description", description = "Appliance Description")
})
@Reference(name = AbstractFireAppliance.ROOM_REFERENCE_NAME, referenceInterface = RoomEnvironment.class,
        cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC, strategy = ReferenceStrategy.LOOKUP)
public abstract class AbstractFireAppliance implements FireAppliance {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFireAppliance.class);

    public static final String ROOM_REFERENCE_NAME = "room.reference";

    protected boolean activated;
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
    public boolean isActivated() {
        return activated;
    }

    /**
     * Get room there the applicance is placed
     *
     * @return room
     */
    protected RoomEnvironment getRoom() {
        try {
            ServiceReference[] serviceReferences = ctx.getBundleContext().getServiceReferences(RoomEnvironment.class.getName(), String.format("(%s=%s)", Constants.ROOM_ID_PROP, ctx.getProperties().get(Constants.ROOM_ID_PROP)));
            if (serviceReferences != null && serviceReferences.length == 1) {
                // Equinos DS returns null!!!. Felix works fine
                // return (RoomEnvironment) ctx.locateService(ROOM_REFERENCE_NAME, serviceReferences[0]);
                return (RoomEnvironment) ctx.getBundleContext().getService(serviceReferences[0]);
            }
        } catch (InvalidSyntaxException e) {
            LOG.warn("Invalid filter", e);
        }
        return null;
    }
}
