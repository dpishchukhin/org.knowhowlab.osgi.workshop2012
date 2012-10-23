package org.knowhowlab.osgi.workshop2012.firealarm.it;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowhowlab.osgi.testing.assertions.ServiceAssert;
import org.knowhowlab.osgi.testing.utils.ServiceUtils;
import org.knowhowlab.osgi.workshop2012.firealarm.api.ActionAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.FireAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironment;
import org.knowhowlab.osgi.workshop2012.firealarm.api.environment.RoomEnvironmentManipulator;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;

import java.io.IOException;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;

import static org.knowhowlab.osgi.testing.utils.FilterUtils.*;
import static org.knowhowlab.osgi.workshop2012.firealarm.api.Constants.*;

/**
 * @author dpishchukhin
 */
@RunWith(JUnit4TestRunner.class)
public class ComplexIntegrationTest extends BaseTest {
    private static final String ROOM_ID1 = "1";
    private static final String ROOM_ID2 = "2";

    @Configuration
    public static Option[] testConfiguration() {
        return baseConfiguration();
    }

    @Test
    public void testComplexScenario() throws InvalidSyntaxException, InterruptedException {
        // add room 1
        createRoom(ROOM_ID1, "Room: " + ROOM_ID1);
        ServiceAssert.assertServiceAvailable(and(create(RoomEnvironment.class), eq(ROOM_ID_PROP, ROOM_ID1)), 5, TimeUnit.SECONDS);

        // add room 2
        createRoom(ROOM_ID2, "Room: " + ROOM_ID2);
        ServiceAssert.assertServiceAvailable(and(create(RoomEnvironment.class), eq(ROOM_ID_PROP, ROOM_ID2)), 5, TimeUnit.SECONDS);

        // add fog sensor to room 1
        createFogSensor(ROOM_ID1, "Fog Sensor for Room: " + ROOM_ID1);
        ServiceAssert.assertServiceAvailable(and(create(FireAppliance.class), eq(ROOM_ID_PROP, ROOM_ID1)), 5, TimeUnit.SECONDS);

        // add fog sensor to room 2
        createFogSensor(ROOM_ID2, "Fog Sensor for Room: " + ROOM_ID2);
        ServiceAssert.assertServiceAvailable(and(create(FireAppliance.class), eq(ROOM_ID_PROP, ROOM_ID2)), 5, TimeUnit.SECONDS);

        // add lamp to room 1
        createLamp(ROOM_ID1, true);
        ServiceAssert.assertServiceAvailable(and(create(ActionAppliance.class), eq(ROOM_ID_PROP, ROOM_ID1)), 5, TimeUnit.SECONDS);

        // add lamp to room 2
        createLamp(ROOM_ID2, false);
        ServiceAssert.assertServiceAvailable(and(create(ActionAppliance.class), and(not(eq(GLOBAL_PROP, true)), eq(ROOM_ID_PROP, ROOM_ID2))), 5, TimeUnit.SECONDS);

        // add global lamp to room 2
        createLamp(ROOM_ID2, true);
        ServiceAssert.assertServiceAvailable(and(create(ActionAppliance.class), and(eq(GLOBAL_PROP, true), eq(ROOM_ID_PROP, ROOM_ID2))), 5, TimeUnit.SECONDS);

        // emulate fire in room 1
        RoomEnvironmentManipulator room1 = ServiceUtils.getService(bundleContext, RoomEnvironmentManipulator.class, eq(ROOM_ID_PROP, ROOM_ID1));
        room1.activateFog(true);
        room1.setActualTemperature(100);

        // wait a little
        Thread.sleep(2000);

        // check state of lamps
        ActionAppliance lampInRoom1 = ServiceUtils.getService(bundleContext, ActionAppliance.class, eq(ROOM_ID_PROP, ROOM_ID1));
        Assert.assertTrue(lampInRoom1.isActivated());

        ActionAppliance globalLampInRoom2 = ServiceUtils.getService(bundleContext, ActionAppliance.class,
                and(eq(GLOBAL_PROP, true), eq(ROOM_ID_PROP, ROOM_ID2)));
        Assert.assertTrue(globalLampInRoom2.isActivated());

        ActionAppliance notGlobalLampInRoom2 = ServiceUtils.getService(bundleContext, ActionAppliance.class,
                and(not(eq(GLOBAL_PROP, true)), eq(ROOM_ID_PROP, ROOM_ID2)));
        Assert.assertFalse(notGlobalLampInRoom2.isActivated());
    }

    private void createLamp(String roomId, boolean global) {
        try {
            ServiceReference serviceReference = ServiceUtils.getServiceReference(bundleContext, ConfigurationAdmin.class);
            ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) bundleContext.getService(serviceReference);
            try {
                org.osgi.service.cm.Configuration configuration = configurationAdmin.createFactoryConfiguration("firealarm.lamp", null);
                Hashtable<String, Object> props = new Hashtable<String, Object>();
                props.put(ROOM_ID_PROP, roomId);
                props.put(GLOBAL_PROP, global);
                configuration.update(props);
            } finally {
                bundleContext.ungetService(serviceReference);
            }
        } catch (IOException e) {
            Assert.fail(e.toString());
        }
    }

    private void createFogSensor(String roomId, String description) {
        try {
            ServiceReference serviceReference = ServiceUtils.getServiceReference(bundleContext, ConfigurationAdmin.class);
            ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) bundleContext.getService(serviceReference);
            try {
                org.osgi.service.cm.Configuration configuration = configurationAdmin.createFactoryConfiguration("firealarm.fog.sensor", null);
                Hashtable<String, String> props = new Hashtable<String, String>();
                props.put(ROOM_ID_PROP, roomId);
                props.put(DESCRIPTION_PROP, description);
                configuration.update(props);
            } finally {
                bundleContext.ungetService(serviceReference);
            }
        } catch (IOException e) {
            Assert.fail(e.toString());
        }
    }

    private void createRoom(String roomId, String description) {
        try {
            ServiceReference serviceReference = ServiceUtils.getServiceReference(bundleContext, ConfigurationAdmin.class);
            ConfigurationAdmin configurationAdmin = (ConfigurationAdmin) bundleContext.getService(serviceReference);
            try {
                org.osgi.service.cm.Configuration configuration = configurationAdmin.createFactoryConfiguration("firealarm.room", null);
                Hashtable<String, String> props = new Hashtable<String, String>();
                props.put(ROOM_ID_PROP, roomId);
                props.put(DESCRIPTION_PROP, description);
                configuration.update(props);
            } finally {
                bundleContext.ungetService(serviceReference);
            }
        } catch (IOException e) {
            Assert.fail(e.toString());
        }
    }

}
