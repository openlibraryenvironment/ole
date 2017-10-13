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
package org.kuali.rice.kew.impl.peopleflow;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.repository.type.KewTypeDefinition;
import org.kuali.rice.kew.framework.peopleflow.PeopleFlowTypeService;
import org.kuali.rice.krad.inquiry.InquirableImpl;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.web.form.InquiryForm;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom view helper for the people flow inquiry view to retrieve the type attribute remotable fields
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PeopleFlowInquirableImpl extends InquirableImpl {
    private static final long serialVersionUID = -8392423307944257532L;

    /**
     * Invokes the {@link org.kuali.rice.kew.api.repository.type.KewTypeRepositoryService} to retrieve the remotable
     * field definitions for the attributes associated with the selected type
     *
     * @param view - view instance
     * @param model - object containing the form data, from which the selected type will be pulled
     * @param container - container that holds the remotable fields
     * @return List<RemotableAttributeField> instances for the type attributes, or empty list if not attributes exist
     */
    public List<RemotableAttributeField> retrieveTypeAttributes(View view, Object model, Container container) {
        List<RemotableAttributeField> remoteFields = new ArrayList<RemotableAttributeField>();

        PeopleFlowBo peopleFlow = (PeopleFlowBo) ((InquiryForm) model).getDataObject();

        // retrieve the type service and invoke to get the remotable field definitions
        if (peopleFlow != null && StringUtils.isNotBlank(peopleFlow.getTypeId())) {
            String typeId = peopleFlow.getTypeId();
            KewTypeDefinition typeDefinition = KewApiServiceLocator.getKewTypeRepositoryService().getTypeById(typeId);
            PeopleFlowTypeService peopleFlowTypeService = GlobalResourceLoader.<PeopleFlowTypeService>getService(
                    QName.valueOf(typeDefinition.getServiceName()));
            remoteFields = peopleFlowTypeService.getAttributeFields(typeId);
        }

        return remoteFields;
    }
}
