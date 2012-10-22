package org.knowhowlab.osgi.workshop2012.firealarm.it;

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

    public static Option[] baseConfiguration() {
        return options(
                workingDirectory("target/paxexam/"),
                cleanCaches(true),
                junitBundles(),

                customFramework("felix", "file:" + System.getProperty("root.basedir") + "/platform-firealarm-felix.xml", "felix 4.0.3"),
                customFramework("equinox", "file:" + System.getProperty("root.basedir") + "/platform-firealarm-equinox-noconsole.xml", "equinox 3.8.1")
        );
    }

}
