package org.knowhowlab.osgi.workshop2012.firealarm.room;

import junit.framework.Assert;
import org.junit.Test;

/**
 * @author dpishchukhin
 */
public class RoomComponentTest {
    @Test
    public void testExtinguish() throws Exception {
        RoomComponent component = new RoomComponent();
        component.setActualTemperature(100);
        component.extinguish();
        Assert.assertEquals(99.0, component.getCurrentTemperature(), 0);
    }

    @Test
    public void testExtinguish_fog() throws Exception {
        RoomComponent component = new RoomComponent();
        component.setActualTemperature(RoomComponent.MINIMUM_TEMP);
        component.activateFog(true);
        component.extinguish();
        Assert.assertEquals(RoomComponent.MINIMUM_TEMP, component.getCurrentTemperature(), 0);
        Assert.assertEquals(false, component.hasFog());
    }
}
