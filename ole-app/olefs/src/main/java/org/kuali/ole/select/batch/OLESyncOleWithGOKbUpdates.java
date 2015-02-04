/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.batch;

import org.apache.log4j.Logger;
import org.kuali.ole.coa.businessobject.Account;
import org.kuali.ole.select.OleSelectNotificationConstant;
import org.kuali.ole.select.gokb.service.GokbLocalService;
import org.kuali.ole.select.gokb.service.GokbRdbmsService;
import org.kuali.ole.select.gokb.service.impl.GokbLocalServiceImpl;
import org.kuali.ole.select.gokb.service.impl.GokbRdbmsServiceImpl;
import org.kuali.ole.select.gokb.service.impl.GokbThread;
import org.kuali.ole.select.gokb.util.OleGokbXmlUtil;
import org.kuali.ole.select.service.OleAccountService;
import org.kuali.ole.select.service.OleNotifyService;
import org.kuali.ole.select.service.impl.OleNotifyServiceImpl;
import org.kuali.ole.service.OLEEResourceHelperService;
import org.kuali.ole.sys.OLEConstants;
import org.kuali.ole.sys.batch.AbstractStep;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.core.api.datetime.DateTimeService;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.rules.rule.event.RouteDocumentEvent;
import org.kuali.rice.krad.service.DocumentService;
import org.kuali.rice.krad.service.impl.DocumentServiceImpl;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.ObjectUtils;

import java.sql.Timestamp;
import java.util.*;

public class OLESyncOleWithGOKbUpdates {

    private static final Logger LOG = Logger.getLogger(OLESyncOleWithGOKbUpdates.class);

    private OLEEResourceHelperService oleeResourceHelperService = new OLEEResourceHelperService();

    public OLEEResourceHelperService getOleeResourceHelperService() {
        if(oleeResourceHelperService == null) {
            oleeResourceHelperService = new OLEEResourceHelperService();
        }
        return oleeResourceHelperService;
    }

    public void synchronizeOleWithGOKb() {
        LOG.debug("Start of scheduled job to execute synchronizeOleWithGOKb.");
        try {
            getOleeResourceHelperService().retrieveAndApplyGokbChanges();
        } catch (Exception ex) {
            LOG.error("Exception occurred while performing synchronizeOleWithGOKb", ex);
        }

    }

}

