/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krad.demo.travel.fiscalofficer;

import java.util.Map;

import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.util.KRADConstants;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FiscalOfficerInfoMaintainableImpl extends MaintainableImpl {
    
    private transient FiscalOfficerService fiscalOfficerService;

    
    @Override
    public void saveDataObject() {
        if(getMaintenanceAction().equals(KRADConstants.MAINTENANCE_NEW_ACTION) ||
                getMaintenanceAction().equals(KRADConstants.MAINTENANCE_COPY_ACTION)) {
            getFiscalOfficerService().createFiscalOfficer((FiscalOfficerInfo)getDataObject());
        }
        else {
            getFiscalOfficerService().updateFiscalOfficer((FiscalOfficerInfo)getDataObject());
        }
    }

    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {
        return getFiscalOfficerService().retrieveFiscalOfficer(new Long(dataObjectKeys.get("id")));
    }

    protected FiscalOfficerService getFiscalOfficerService() {
        if(fiscalOfficerService == null) {
            fiscalOfficerService = GlobalResourceLoader.getService("fiscalOfficerService");
        }
        return this.fiscalOfficerService;
    }

    public void setFiscalOfficerService(FiscalOfficerService fiscalOfficerService) {
        this.fiscalOfficerService = fiscalOfficerService;
    }

}
