package org.knowhowlab.osgi.workshop2012.firealarm.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author dpishchukhin
 */
public class AlarmSystemStateMachineTest {
    @Test
    public void testHandleAlarmEvent_Activate() throws Exception {
        AlarmSystemStateMachine stateMachine = new AlarmSystemStateMachine();
        AlarmSystemFeedback feedback = stateMachine.handleAlarmEvent("1", "1", true);

        Assert.assertNotNull(feedback);
        Assert.assertEquals(true, feedback.isActivateGlobal());
        Assert.assertEquals(1, feedback.getActiveAlarmRooms().size());
        Assert.assertEquals("1", feedback.getActiveAlarmRooms().iterator().next());
        Assert.assertEquals(0, feedback.getInactiveAlarmRooms().size());
    }

    @Test
    public void testHandleAlarmEvent_Deactivate() throws Exception {
        AlarmSystemStateMachine stateMachine = new AlarmSystemStateMachine();
        stateMachine.handleAlarmEvent("1", "1", true);

        AlarmSystemFeedback feedback = stateMachine.handleAlarmEvent("1", "1", false);

        Assert.assertNotNull(feedback);
        Assert.assertEquals(false, feedback.isActivateGlobal());
        Assert.assertEquals(0, feedback.getActiveAlarmRooms().size());
        Assert.assertEquals(1, feedback.getInactiveAlarmRooms().size());
        Assert.assertEquals("1", feedback.getInactiveAlarmRooms().iterator().next());
    }
}
