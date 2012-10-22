package org.knowhowlab.osgi.workshop2012.firealarm.api;

/**
 * Action Appliance that could be (de)activated
 *
 * @author dpishchukhin
 */
public interface ActionAppliance extends FireAppliance {
    /**
     * Activate appliance
     */
    void activateAppliance();

    /**
     * Deactivate appliance
     */
    void deactivateAppliance();
}
