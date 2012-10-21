package org.knowhowlab.osgi.workshop2012.firealarm.api;

/**
 * @author dpishchukhin
 */
public interface FireAppliance {
    boolean isEnabled();

    void enable();

    void disable();
}
