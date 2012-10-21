package org.knowhowlab.osgi.workshop2012.firealarm.api;

/**
 * @author dpishchukhin
 */
public interface ActionAppliance extends FireAppliance {
    boolean isActivated();

    void activate();

    void deactivate();
}
