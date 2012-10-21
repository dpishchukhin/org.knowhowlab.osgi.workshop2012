package org.knowhowlab.osgi.workshop2012.firealarm.core;

import java.util.Set;

/**
 * @author dpishchukhin
 */
public class AlarmSystemFeedback {
    private boolean activateGlobal;
    private Set<String> activeAlarmRooms;
    private Set<String> inactiveAlarmRooms;

    public AlarmSystemFeedback(boolean activateGlobal, Set<String> activeAlarmRooms, Set<String> inactiveAlarmRooms) {
        this.activateGlobal = activateGlobal;
        this.activeAlarmRooms = activeAlarmRooms;
        this.inactiveAlarmRooms = inactiveAlarmRooms;
    }

    public boolean isActivateGlobal() {
        return activateGlobal;
    }

    public Set<String> getActiveAlarmRooms() {
        return activeAlarmRooms;
    }

    public Set<String> getInactiveAlarmRooms() {
        return inactiveAlarmRooms;
    }
}
