package org.knowhowlab.osgi.workshop2012.firealarm.core;

import org.knowhowlab.osgi.workshop2012.firealarm.api.ActionAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.FireAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironment;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.ComponentConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author dpishchukhin
 */
public class EnvironmentManager {
    private static final Logger LOG = LoggerFactory.getLogger(EnvironmentManager.class);

    private BundleContext bc;

    public EnvironmentManager(BundleContext bc) {
        this.bc = bc;
    }

    public String getRoomDescription(String roomId) {
        ServiceReference reference = findReference(RoomEnvironment.class, String.format("(%s=%s)", Constants.ROOM_ID_PROP, roomId));
        if (reference != null) {
            return (String) reference.getProperty(Constants.DESCRIPTION_PROP);
        } else {
            LOG.warn("Unable to find room: " + roomId);
            return null;
        }
    }

    public String getApplianceDescription(String applianceId) {
        ServiceReference reference = findReference(FireAppliance.class, String.format("(%s=%s)", ComponentConstants.COMPONENT_ID, applianceId));
        if (reference != null) {
            return (String) reference.getProperty(Constants.DESCRIPTION_PROP);
        } else {
            LOG.warn("Unable to find appliance: " + applianceId);
            return null;
        }
    }

    public boolean isApplianceActivated(String applianceId) {
        ServiceReference reference = findReference(FireAppliance.class, String.format("(%s=%s)", ComponentConstants.COMPONENT_ID, applianceId));
        if (reference != null) {
            FireAppliance fireAppliance = (FireAppliance) bc.getService(reference);
            try {
                return fireAppliance.isActivated();
            } finally {
                bc.ungetService(reference);
            }
        } else {
            LOG.warn("Unable to find appliance: " + applianceId);
            return false;
        }
    }

    public void activateActionAppliance(String applianceId, boolean activate) {
        ServiceReference reference = findReference(ActionAppliance.class, String.format("(%s=%s)", ComponentConstants.COMPONENT_ID, applianceId));
        if (reference != null) {
            ActionAppliance actionAppliance = (ActionAppliance) bc.getService(reference);
            try {
                if (activate) {
                    actionAppliance.activateAppliance();
                } else {
                    actionAppliance.deactivateAppliance();
                }
            } finally {
                bc.ungetService(reference);
            }
        } else {
            LOG.warn("Unable to find appliance: " + applianceId);
        }
    }

    public Iterable<String> roomIds() {
        return new ReferencesIterable(RoomEnvironment.class, null, Constants.ROOM_ID_PROP);
    }

    public Iterable<String> getAllFireApplianceIdsByRoomId(String roomId) {
        return new ReferencesIterable(FireAppliance.class, createRoomAppliancesFilter(roomId, false), ComponentConstants.COMPONENT_ID);
    }

    public Iterable<? extends String> getGlobalActionApplianceIds() {
        return new ReferencesIterable(ActionAppliance.class, String.format("(%s=%s)", Constants.GLOBAL_PROP, "true"), ComponentConstants.COMPONENT_ID);
    }

    public Iterable<? extends String> getActionApplianceIdsByRoomId(String roomId, boolean onlyLocal) {
        return new ReferencesIterable(ActionAppliance.class, createRoomAppliancesFilter(roomId, onlyLocal), ComponentConstants.COMPONENT_ID);
    }

    private String createRoomAppliancesFilter(String roomId, boolean onlyLocal) {
        if (onlyLocal) {
            return String.format("(&(!(%s=%s))(%s=%s))", Constants.GLOBAL_PROP, "true", Constants.ROOM_ID_PROP, roomId);
        } else {
            return String.format("(%s=%s)", Constants.ROOM_ID_PROP, roomId);
        }
    }

    private ServiceReference findReference(Class referenceClass, String filter) {
        try {
            ServiceReference[] references = bc.getAllServiceReferences(referenceClass.getName(), filter);
            if (references != null) {
                if (references.length == 1) {
                    return references[0];
                } else {
                    LOG.warn("Result is not unique for filter: " + filter);
                }
            } else {
                LOG.warn("No results for filter: " + filter);
            }
        } catch (InvalidSyntaxException e) {
            LOG.warn("Filter is invalid: " + filter, e);
        }
        return null;
    }

    private class ReferencesIterable implements Iterable<String> {
        protected Class referenceClass;
        protected String filter;
        protected String idPropertyName;

        private ReferencesIterable(Class referenceClass, String filter, String idPropertyName) {
            this.referenceClass = referenceClass;
            this.filter = filter;
            this.idPropertyName = idPropertyName;
        }

        @Override
        public Iterator<String> iterator() {
            List<String> ids = new ArrayList<String>();
            try {
                ServiceReference[] references = bc.getAllServiceReferences(referenceClass.getName(), filter);
                if (references != null) {
                    for (ServiceReference reference : references) {
                        ids.add(String.valueOf(reference.getProperty(idPropertyName)));
                    }
                }
            } catch (InvalidSyntaxException e) {
                e.printStackTrace();
            }
            return ids.iterator();
        }
    }
}
