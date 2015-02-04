package org.kuali.ole.select.controller;

import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceLock;

import java.util.Collections;
import java.util.List;

/**
 * LicenceRoutingRuleDelegationMaintainable removes the lock on License Request Maintenance Document.
 */
public class LicenceRoutingRuleDelegationMaintainable extends MaintainableImpl {

    /**
     * This method is used to avoid maintenance lock at the route state.
     *
     * @return List
     */
    @Override
    public List<MaintenanceLock> generateMaintenanceLocks() {
        return Collections.emptyList();
    }

}
