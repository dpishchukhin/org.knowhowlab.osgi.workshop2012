package org.knowhowlab.osgi.workshop2012.firealarm.core;

import junit.framework.Assert;
import org.junit.Test;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.osgi.service.event.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dpishchukhin
 */
public class FireAlarmSystemComponentTest {
    @Test
    public void testHandleEvent() throws Exception {
        // add 2 rooms
        List<String> rooms = new ArrayList<String>();
        rooms.add("1");
        rooms.add("2");
        // add 4 action appliances
        Map<String, Map<String, Object>> actionAppliances = new HashMap<String, Map<String, Object>>();
        // appliance 1 for room 1 (not global)
        actionAppliances.put("1", createAppliance("1", false));
        // appliance 2 for room 1 (global)
        actionAppliances.put("2", createAppliance("1", true));
        // appliance 3 for room 2 (global)
        actionAppliances.put("3", createAppliance("2", true));
        // appliance 4 for room 2 (not global)
        actionAppliances.put("4", createAppliance("2", false));

        EnvironmentManagerMock mock = new EnvironmentManagerMock(rooms, actionAppliances);

        FireAlarmSystemComponent systemComponent = new FireAlarmSystemComponent();
        systemComponent.setEnvironmentManager(mock);

        // fire alarm event for room 1
        Map eventProps = new HashMap();
        eventProps.put(Constants.ROOM_ID_PROP, "1");
        eventProps.put(Constants.SENSOR_ID_PROP, "1");
        eventProps.put(Constants.ACTIVE_ALARM, true);
        systemComponent.handleEvent(new Event(Constants.TOPIC, eventProps));

        // appliance 1 activated
        Assert.assertTrue(mock.isApplianceActivated("1"));
        // appliance 2 activated
        Assert.assertTrue(mock.isApplianceActivated("2"));
        // appliance 3 activated
        Assert.assertTrue(mock.isApplianceActivated("3"));
        // appliance 1 deactivated - another room and not global
        Assert.assertFalse(mock.isApplianceActivated("4"));
    }

    private HashMap<String, Object> createAppliance(String roomId, boolean global) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.ROOM_ID_PROP, roomId);
        map.put(Constants.GLOBAL_PROP, global);
        return map;
    }
}
