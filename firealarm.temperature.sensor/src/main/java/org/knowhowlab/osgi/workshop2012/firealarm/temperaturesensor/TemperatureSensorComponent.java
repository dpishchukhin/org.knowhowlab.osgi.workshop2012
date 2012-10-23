package org.knowhowlab.osgi.workshop2012.firealarm.temperaturesensor;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Property;
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
@Component(specVersion = "1.1", name = "firealarm.temperature.sensor", label = "Temperature Sensor", description = "Temperature Sensor",
        metatype = true, configurationFactory = true, policy = ConfigurationPolicy.REQUIRE, immediate = true)
@Service(FireAppliance.class)
@Property(name = TemperatureSensorComponent.TEMPERATURE_LIMIT_PROP,
        label = "Temperature Alarm Limit", description = "Temperature Alarm Limit", value = "70")
public class TemperatureSensorComponent extends AbstractSensorAppliance implements FireAppliance {
    public static final String TEMPERATURE_LIMIT_PROP = "temperature.limit";
    private ScheduledExecutorService executorService;
    protected Float temperatureLimit;

    @Override
    protected void activate(ComponentContext ctx) {
        super.activate(ctx);
        temperatureLimit = Float.valueOf((String) ctx.getProperties().get(TEMPERATURE_LIMIT_PROP));
        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(new CheckEnvironmentStatusRunnable(), 1, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void deactivate(ComponentContext ctx) {
        executorService.shutdown();
        executorService.shutdownNow();

        super.deactivate(ctx);
    }

    @Override
    protected void modified(ComponentContext ctx) {
        super.modified(ctx);
        temperatureLimit = Float.valueOf((String) ctx.getProperties().get(TEMPERATURE_LIMIT_PROP));
    }

    private class CheckEnvironmentStatusRunnable implements Runnable {
        @Override
        public void run() {
            try {
                float currentTemperature = getRoom().getCurrentTemperature();
                boolean limitCrossed = (currentTemperature >= temperatureLimit);
                if (limitCrossed && !activated) {
                    activateAlarm(String.format("Temp: %sC", currentTemperature));
                } else if (activated && !limitCrossed) {
                    deactivateAlarm();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
