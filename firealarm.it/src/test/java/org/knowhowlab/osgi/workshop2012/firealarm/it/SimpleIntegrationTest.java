package org.knowhowlab.osgi.workshop2012.firealarm.it;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.knowhowlab.osgi.testing.assertions.ServiceAssert;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import java.util.concurrent.TimeUnit;

import static org.knowhowlab.osgi.testing.utils.FilterUtils.*;

/**
 * @author dpishchukhin
 */
@RunWith(JUnit4TestRunner.class)
public class SimpleIntegrationTest extends BaseTest {

    @Configuration
    public static Option[] testConfiguration() {
        return baseConfiguration();
    }

    @Test
    public void testBundleContext() throws InvalidSyntaxException {
        Assert.assertNotNull(bundleContext);
    }

    @Test
    public void testFireSystemEventHandler() throws InvalidSyntaxException {
        ServiceAssert.assertServiceAvailable(EventAdmin.class);
        ServiceAssert.assertServiceAvailable(and(create(EventHandler.class), eq(EventConstants.EVENT_TOPIC, Constants.TOPIC)), 10, TimeUnit.SECONDS);
    }
}
