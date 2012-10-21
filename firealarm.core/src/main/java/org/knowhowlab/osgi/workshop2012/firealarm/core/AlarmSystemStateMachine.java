package org.knowhowlab.osgi.workshop2012.firealarm.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author dpishchukhin
 */
public class AlarmSystemStateMachine {
    private Map<String, Set<String>> roomsWithAlarm = new HashMap<String, Set<String>>();

    protected synchronized AlarmSystemFeedback handleAlarmEvent(String roomId, String sensorId, boolean activeAlarm) {
        Set<String> deactivateInRooms = new HashSet<String>();

        if (activeAlarm) {
            Set<String> activeSensors = roomsWithAlarm.get(roomId);
            if (activeSensors == null) {
                activeSensors = new HashSet<String>();
                roomsWithAlarm.put(roomId, activeSensors);
            }
            activeSensors.add(sensorId);
        } else {
            Set<String> activeSensors = roomsWithAlarm.get(roomId);
            if (activeSensors != null) {
                activeSensors.remove(sensorId);
                if (activeSensors.isEmpty()) {
                    roomsWithAlarm.remove(roomId);
                    deactivateInRooms.add(roomId);
                }
            }
        }
        return new AlarmSystemFeedback(!roomsWithAlarm.isEmpty(), roomsWithAlarm.keySet(), deactivateInRooms);
    }
}
