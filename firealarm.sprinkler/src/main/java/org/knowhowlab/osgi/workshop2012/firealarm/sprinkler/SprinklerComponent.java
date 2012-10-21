package org.knowhowlab.osgi.workshop2012.firealarm.sprinkler;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Service;
import org.knowhowlab.osgi.workshop2012.firealarm.api.ActionAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.FireAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.common.AbstractFireAppliance;
import org.osgi.service.component.ComponentContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author dpishchukhin
 */
@Component(specVersion = "1.1", name = "firealarm.sprinkler", label = "Sprinkler", description = "Sprinkler",
        metatype = true, configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, immediate = true)
@Service({FireAppliance.class, ActionAppliance.class})
public class SprinklerComponent extends AbstractFireAppliance implements ActionAppliance {
    private ScheduledExecutorService executorService;

    @Override
    protected void activate(ComponentContext ctx) {
        super.activate(ctx);
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(new ExtinguishRunnable(), 1, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void deactivate(ComponentContext ctx) {
        executorService.shutdown();
        executorService.shutdownNow();

        super.deactivate(ctx);
    }

    @Override
    public void activateAppliance() {
        activated = true;
    }

    @Override
    public void deactivateAppliance() {
        activated = false;
    }

    private class ExtinguishRunnable implements Runnable {
        @Override
        public void run() {
            if (activated) {
                getRoom().extinguish();
            }
        }
    }
}
