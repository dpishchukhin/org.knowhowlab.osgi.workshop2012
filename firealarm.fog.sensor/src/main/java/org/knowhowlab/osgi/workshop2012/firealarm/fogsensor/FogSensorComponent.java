package org.knowhowlab.osgi.workshop2012.firealarm.fogsensor;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Service;
import org.knowhowlab.osgi.workshop2012.firealarm.api.FireAppliance;
import org.knowhowlab.osgi.workshop2012.firealarm.api.common.AbstractSensorAppliance;
import org.osgi.service.component.ComponentContext;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author dpishchukhin
 */
@Component(specVersion = "1.1", name = "firealarm.fog.sensor", label = "Fog Sensor", description = "Fog Sensor",
        metatype = true, configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, immediate = true)
@Service(FireAppliance.class)
public class FogSensorComponent extends AbstractSensorAppliance implements FireAppliance {
    private ScheduledExecutorService executorService;

    @Override
    protected void activate(ComponentContext ctx) {
        super.activate(ctx);
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(new CheckEnvironmentStatusRunnable(), 1, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void deactivate(ComponentContext ctx) {
        executorService.shutdown();
        executorService.shutdownNow();

        super.deactivate(ctx);
    }

    private class CheckEnvironmentStatusRunnable implements Runnable {
        @Override
        public void run() {
            boolean hasFog = getRoom().hasFog();
            if (hasFog && !activated) {
                activateAlarm(null);
            } else if (activated && !hasFog) {
                deactivateAlarm();
            }
        }
    }
}
