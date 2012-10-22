package org.knowhowlab.osgi.workshop2012.firealarm.it;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.Configuration;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.osgi.framework.InvalidSyntaxException;

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
}
