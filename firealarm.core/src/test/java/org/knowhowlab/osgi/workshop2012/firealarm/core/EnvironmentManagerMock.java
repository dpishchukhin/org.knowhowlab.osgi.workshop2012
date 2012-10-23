package org.knowhowlab.osgi.workshop2012.firealarm.core;

import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.core.internal.EnvironmentManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author dpishchukhin
 */
public class EnvironmentManagerMock extends EnvironmentManager {
    private final static String ACTIVATED_PROP = "ACTIVATED_PROP";

    private List<String> rooms;
    private Map<String, Map<String, Object>> actionAppliances;

    public EnvironmentManagerMock(List<String> rooms, Map<String, Map<String, Object>> actionAppliances) {
        super(null);
        this.rooms = rooms;
        this.actionAppliances = actionAppliances;
    }

    @Override
    public Iterable<String> roomIds() {
        return rooms;
    }

    @Override
    public Iterable<? extends String> getGlobalActionApplianceIds() {
        List<String> ids = new ArrayList<String>();
        for (String applianceId : actionAppliances.keySet()) {
            Map<String, Object> props = actionAppliances.get(applianceId);
            if ((Boolean)props.get(Constants.GLOBAL_PROP)) {
                ids.add(applianceId);
            }
        }
        return ids;
    }

    @Override
    public Iterable<? extends String> getActionApplianceIdsByRoomId(String roomId, boolean onlyLocal) {
        List<String> ids = new ArrayList<String>();
        for (String applianceId : actionAppliances.keySet()) {
            Map<String, Object> props = actionAppliances.get(applianceId);
            if (roomId.equals(props.get(Constants.ROOM_ID_PROP))) {
                if (onlyLocal && !(Boolean)props.get(Constants.GLOBAL_PROP)) {
                    ids.add(applianceId);
                } else {
                    ids.add(applianceId);
                }
            }
        }
        return ids;
    }

    @Override
    public void activateActionAppliance(String applianceId, boolean activate) {
        actionAppliances.get(applianceId).put(ACTIVATED_PROP, true);
    }

    @Override
    public boolean isApplianceActivated(String applianceId) {
        return actionAppliances.get(applianceId).get(ACTIVATED_PROP) == Boolean.TRUE;

    }
}
