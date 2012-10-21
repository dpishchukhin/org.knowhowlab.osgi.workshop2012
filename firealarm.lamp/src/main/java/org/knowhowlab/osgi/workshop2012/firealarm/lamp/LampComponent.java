package org.knowhowlab.osgi.workshop2012.firealarm.lamp;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.knowhowlab.osgi.workshop2012.firealarm.api.ActionAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.Constants;
import org.knowhowlab.osgi.workshop2012.firealarm.api.FireAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.common.AbstractFireAppliance;

/**
 * @author dpishchukhin
 */
@Component(specVersion = "1.1", name = "firealarm.lamp", label = "Notification Lamp", description = "Notification Lamp",
        metatype = true, configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, immediate = true)
@Service({FireAppliance.class, ActionAppliance.class})
@Property(name = Constants.GLOBAL_PROP, label = "Global Notification", description = "Global Notification", value = "false")
public class LampComponent extends AbstractFireAppliance implements ActionAppliance {
    @Override
    public void activateAppliance() {
        activated = true;
    }

    @Override
    public void deactivateAppliance() {
        activated = false;
    }
}
