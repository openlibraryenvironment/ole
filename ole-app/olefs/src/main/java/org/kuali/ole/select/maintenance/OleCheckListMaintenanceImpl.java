package org.kuali.ole.select.maintenance;

import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.service.AttachmentService;
import org.kuali.rice.krad.service.KRADServiceLocator;

/**
 * OleCheckListMaintenanceImpl is the implementation class for License Request Maintenance Document
 */
public class OleCheckListMaintenanceImpl extends MaintainableImpl {

    private AttachmentService attachmentService;

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCheckListMaintenanceImpl.class);

    /**
     * This method invokes default processAfterRetrieve.
     */
    @Override
    public void processAfterRetrieve() {
        super.processAfterRetrieve();
    }

    /**
     * Gets KualiConfigurationService
     *
     * @return ConfigurationService
     */
    public ConfigurationService getKualiConfigurationService() {
        return GlobalResourceLoader.getService("kualiConfigurationService");
    }

}
