package org.knowhowlab.osgi.workshop2012.firealarm.api.common;

import org.apache.felix.scr.annotations.*;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.FireAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironment;
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dpishchukhin
 */
@Properties({
        @Property(name = Constants.ROOM_ID_PROP, label = "Room ID", description = "Room ID"),
        @Property(name = Constants.DESCRIPTION_PROP, label = "Appliance Description", description = "Appliance Description")
})
public abstract class AbstractFireAppliance implements FireAppliance {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractFireAppliance.class);

    public static final String ROOM_REFERENCE_NAME = "room.reference";

    @Reference(name = AbstractFireAppliance.ROOM_REFERENCE_NAME, referenceInterface = RoomEnvironment.class,
            cardinality = ReferenceCardinality.MANDATORY_MULTIPLE, policy = ReferencePolicy.DYNAMIC,
            bind = "bindRoom", unbind = "unbindRoom")
    private Map<String, RoomEnvironment> rooms = new HashMap<String, RoomEnvironment>();

    protected boolean activated;

    protected String description;
    protected String applianceId;
    protected String roomId;

    @Activate
    protected void activate(ComponentContext ctx) {
        readProperties(ctx);
    }

    private void readProperties(ComponentContext ctx) {
        description = (String) ctx.getProperties().get(Constants.DESCRIPTION_PROP);
        applianceId = String.valueOf(ctx.getProperties().get(ComponentConstants.COMPONENT_ID));
        roomId = (String) ctx.getProperties().get(Constants.ROOM_ID_PROP);
    }

    @Deactivate
    protected void deactivate(ComponentContext ctx) {
    }

    @Modified
    protected void modified(ComponentContext ctx) {
        readProperties(ctx);
    }

    @Override
    public boolean isActivated() {
        return activated;
    }

    protected void bindRoom(RoomEnvironment room, Map<String, Object> props) {
        rooms.put((String) props.get(Constants.ROOM_ID_PROP), room);
    }

    protected void unbindRoom(RoomEnvironment room, Map<String, Object> props) {
        rooms.remove(props.get(Constants.ROOM_ID_PROP));
    }

    /**
     * Get room there the applicance is placed
     *
     * @return room
     */
    protected RoomEnvironment getRoom() {
        RoomEnvironment applianceRoom = rooms.get(roomId);
        if (applianceRoom == null) {
            LOG.warn(String.format("Room: %s is not available yet%n", roomId));
        }
        return applianceRoom;
    }
}
