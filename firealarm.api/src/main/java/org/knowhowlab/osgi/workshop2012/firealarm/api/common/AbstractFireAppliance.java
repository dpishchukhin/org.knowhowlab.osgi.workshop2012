package org.knowhowlab.osgi.workshop2012.firealarm.api.common;

import org.apache.felix.scr.annotations.*;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.FireAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.EnvironmentStatus;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dpishchukhin
 */
@Properties({
        @Property(name = Constants.ROOM_ID, label = "Room ID", description = "Room ID"),
        @Property(name = Constants.DESCRIPTION, label = "Appliance Description", description = "Appliance Description")
})
@Reference(name = AbstractFireAppliance.ROOM_REFERENCE_NAME, referenceInterface = EnvironmentStatus.class,
        cardinality = ReferenceCardinality.OPTIONAL_MULTIPLE, policy = ReferencePolicy.DYNAMIC, strategy = ReferenceStrategy.LOOKUP)
public abstract class AbstractFireAppliance implements FireAppliance {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFireAppliance.class);

    public static final String ROOM_REFERENCE_NAME = "room.reference";

    private boolean enabled = true;
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
    public boolean isEnabled() {
        return enabled && getRoom() != null;
    }

    protected EnvironmentStatus getRoom() {
        try {
            ServiceReference[] serviceReferences = ctx.getBundleContext().getServiceReferences(EnvironmentStatus.class.getName(), String.format("(%s=%s)", Constants.ROOM_ID, (String) ctx.getProperties().get(Constants.ROOM_ID)));
            if (serviceReferences != null && serviceReferences.length == 1) {
                return (EnvironmentStatus) ctx.locateService(ROOM_REFERENCE_NAME, serviceReferences[0]);
            }
        } catch (InvalidSyntaxException e) {
            LOG.warn("Invalid filter", e);
        }
        return null;
    }

    @Override
    public void enable() {
        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }
}
