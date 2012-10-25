package org.knowhowlab.osgi.workshop2012.firealarm.it;

import org.junit.Before;
import org.knowhowlab.osgi.testing.assertions.OSGiAssert;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.EagerSingleStagedReactorFactory;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;

import static org.ops4j.pax.exam.CoreOptions.*;

/**
 * @author dpishchukhin
 */
@ExamReactorStrategy(EagerSingleStagedReactorFactory.class)
public class BaseTest {
    @Inject
    protected BundleContext bundleContext;

    @Before
    public void initAssertions() {
        OSGiAssert.init(bundleContext);
    }

    public static Option[] baseConfiguration() {
        return options(
                workingDirectory("target/paxexam/"),
                cleanCaches(true),
                junitBundles(),

                customFramework("felix", "file:" + System.getProperty("root.basedir") + "/platform-firealarm-felix-it.xml", "felix 4.0.3"),
                customFramework("equinox", "file:" + System.getProperty("root.basedir") + "/platform-firealarm-equinox-it.xml", "equinox 3.8.1"),

                mavenBundle("org.knowhowlab.osgi", "org.knowhowlab.osgi.testing.utils").versionAsInProject(),
                mavenBundle("org.knowhowlab.osgi", "org.knowhowlab.osgi.testing.assertions").versionAsInProject(),

                mavenBundle("org.knowhowlab.osgi.workshop2012", "firealarm.api").version(System.getProperty("project.version")),
                mavenBundle("org.knowhowlab.osgi.workshop2012", "firealarm.room").version(System.getProperty("project.version")),
                mavenBundle("org.knowhowlab.osgi.workshop2012", "firealarm.lamp").version(System.getProperty("project.version")),
                mavenBundle("org.knowhowlab.osgi.workshop2012", "firealarm.sprinkler").version(System.getProperty("project.version")),
                mavenBundle("org.knowhowlab.osgi.workshop2012", "firealarm.smoke.sensor").version(System.getProperty("project.version")),
                mavenBundle("org.knowhowlab.osgi.workshop2012", "firealarm.temperature.sensor").version(System.getProperty("project.version")),
                mavenBundle("org.knowhowlab.osgi.workshop2012", "firealarm.core").version(System.getProperty("project.version"))
        );
    }

}
