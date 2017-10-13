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
package org.kuali.rice.krms.impl.repository;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.lookup.LookupableImpl;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.UrlFactory;
import org.kuali.rice.krad.web.form.LookupForm;
import org.kuali.rice.krms.api.KrmsConstants;
import org.kuali.rice.krms.impl.ui.AgendaEditor;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AgendaLookupableHelperServiceImpl extends LookupableImpl {

    @Override
    public boolean allowsMaintenanceNewOrCopyAction() {
        // The context is unknown on create so we need to let the user in
        // TODO: maybe restrict it so only user that have rights to some contexts are allowed to create agendas.
        return true;
    }

    @Override
    public boolean allowsMaintenanceEditAction(Object dataObject) {
        boolean allowsEdit = false;

        AgendaBo agenda = (AgendaBo) dataObject;
        allowsEdit = KrmsRepositoryServiceLocator.getAgendaAuthorizationService().isAuthorized(KrmsConstants.MAINTAIN_KRMS_AGENDA, agenda.getContextId());

        return allowsEdit;
    }

    @Override
    public boolean allowsMaintenanceDeleteAction(Object dataObject) {
        boolean allowsMaintain = false;
        boolean allowsDelete = false;

        AgendaBo agenda = (AgendaBo) dataObject;
        allowsMaintain = KrmsRepositoryServiceLocator.getAgendaAuthorizationService().isAuthorized(KrmsConstants.MAINTAIN_KRMS_AGENDA, agenda.getContextId());

        return allowsDelete && allowsMaintain;
    }

    @Override
    protected String getActionUrlHref(LookupForm lookupForm, Object dataObject, String methodToCall, List<String> pkNames) {
        Properties props = new Properties();
        props.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, methodToCall);

        Map<String, String> primaryKeyValues = KRADUtils.getPropertyKeyValuesFromDataObject(pkNames, dataObject);
        for (String primaryKey : primaryKeyValues.keySet()) {
            String primaryKeyValue = primaryKeyValues.get(primaryKey);

            props.put(primaryKey, primaryKeyValue);
            props.put(KRADConstants.OVERRIDE_KEYS, primaryKey);
        }

        if (StringUtils.isNotBlank(lookupForm.getReturnLocation())) {
            props.put(KRADConstants.RETURN_LOCATION_PARAMETER, lookupForm.getReturnLocation());
        }

        props.put(UifParameters.DATA_OBJECT_CLASS_NAME, AgendaEditor.class.getName());
        props.put(UifParameters.VIEW_TYPE_NAME, UifConstants.ViewType.MAINTENANCE.name());

        return UrlFactory.parameterizeUrl(org.kuali.rice.krms.impl.util.KrmsImplConstants.WebPaths.AGENDA_EDITOR_PATH, props);
    }
}
