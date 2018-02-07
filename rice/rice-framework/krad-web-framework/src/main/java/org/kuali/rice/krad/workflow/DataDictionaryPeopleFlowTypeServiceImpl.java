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
package org.kuali.rice.krad.workflow;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableTextInput;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.kew.api.KEWPropertyConstants;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.document.Document;
import org.kuali.rice.kew.api.document.DocumentContent;
import org.kuali.rice.kew.api.repository.type.KewAttributeDefinition;
import org.kuali.rice.kew.api.repository.type.KewTypeAttribute;
import org.kuali.rice.kew.api.repository.type.KewTypeDefinition;
import org.kuali.rice.kew.framework.peopleflow.PeopleFlowTypeService;
import org.kuali.rice.krad.service.DataDictionaryRemoteFieldService;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.util.BeanPropertyComparator;

import javax.jws.WebParam;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class DataDictionaryPeopleFlowTypeServiceImpl implements PeopleFlowTypeService {

    /**
     * @see org.kuali.rice.kew.framework.peopleflow.PeopleFlowTypeService#filterToSelectableRoleIds(java.lang.String,
     *      java.util.List<java.lang.String>)
     */
    public List<String> filterToSelectableRoleIds(@WebParam(name = "kewTypeId") String kewTypeId,
            @WebParam(name = "roleIds") List<String> roleIds) {
        return roleIds;
    }

    /**
     * @see org.kuali.rice.kew.framework.peopleflow.PeopleFlowTypeService#resolveRoleQualifiers(java.lang.String,
     *      java.lang.String, org.kuali.rice.kew.api.document.Document, org.kuali.rice.kew.api.document.DocumentContent)
     */
    public Map<String, String> resolveRoleQualifiers(@WebParam(name = "kewTypeId") String kewTypeId,
            @WebParam(name = "roleId") String roleId, @WebParam(name = "document") Document document,
            @WebParam(name = "documentContent") DocumentContent documentContent) {
        return new HashMap<String, String>();
    }

    /**
     * Allows for more than one set of qualifiers to be returned for the purpose of matching.
     *
     * @param kewTypeId the people flow type identifier.  Must not be null or blank.
     * @param roleId the role that the qualifiers are specific to.  Must not be null or blank.
     * @param document the document that the qualifiers are being resolved against.  Must not be null.
     * @param documentContent the contents for the document that the qualifiers are being resolved against.
     * Must not be null.
     * @return List<Map<String, String>>
     */
    @Override
    public List<Map<String, String>> resolveMultipleRoleQualifiers(
            @WebParam(name = "kewTypeId") String kewTypeId,
            @WebParam(name = "roleId") String roleId,
            @WebParam(name = "document") Document document,
            @WebParam(name = "documentContent") DocumentContent documentContent) {

        return new ArrayList<Map<String, String>>();
    }

    /**
     * @see org.kuali.rice.kew.framework.peopleflow.PeopleFlowTypeService#getAttributeFields(java.lang.String)
     */
    public List<RemotableAttributeField> getAttributeFields(@WebParam(name = "kewTypeId") String kewTypeId) {
        List<RemotableAttributeField> fields = new ArrayList<RemotableAttributeField>();

        KewTypeDefinition typeDefinition = KewApiServiceLocator.getKewTypeRepositoryService().getTypeById(kewTypeId);
        List<KewTypeAttribute> typeAttributes = new ArrayList<KewTypeAttribute>(typeDefinition.getAttributes());

        // sort type attributes by sort code
        List<String> sortAttributes = new ArrayList<String>();
        sortAttributes.add(KEWPropertyConstants.SEQUENCE_NUMBER);
        Collections.sort(typeAttributes, new BeanPropertyComparator(sortAttributes));

        // build a remotable field for each active type attribute
        for (KewTypeAttribute typeAttribute : typeAttributes) {
            if (!typeAttribute.isActive()) {
                continue;
            }

            RemotableAttributeField attributeField = null;
            if (StringUtils.isBlank(typeAttribute.getAttributeDefinition().getComponentName())) {
                attributeField = buildRemotableFieldWithoutDataDictionary(typeAttribute.getAttributeDefinition());
            } else {
                attributeField = getDataDictionaryRemoteFieldService().buildRemotableFieldFromAttributeDefinition(
                        typeAttribute.getAttributeDefinition().getComponentName(),
                        typeAttribute.getAttributeDefinition().getName());
            }

            fields.add(attributeField);
        }

        return fields;
    }

    /**
     * Builds a {@link RemotableAttributeField} instance when there is no component configured (and therefore we can
     * not lookup the data dictionary)
     *
     * <p>
     * Very basic field, should have labels configured and defaults to using text control
     * </p>
     *
     * @param attributeDefinition - KEW attribute definition configured from which the name, label, and description
     * will be pulled
     * @return RemotableAttributeField instance built from the given KEW attribute definition
     */
    protected RemotableAttributeField buildRemotableFieldWithoutDataDictionary(
            KewAttributeDefinition attributeDefinition) {
        RemotableAttributeField.Builder definition = RemotableAttributeField.Builder.create(
                attributeDefinition.getName());

        definition.setLongLabel(attributeDefinition.getLabel());
        definition.setShortLabel(attributeDefinition.getLabel());
        definition.setHelpDescription(attributeDefinition.getDescription());

        // default control to text
        RemotableTextInput.Builder controlBuilder = RemotableTextInput.Builder.create();
        controlBuilder.setSize(30);
        definition.setControl(controlBuilder);

        return definition.build();
    }

    /**
     * @see org.kuali.rice.kew.framework.peopleflow.PeopleFlowTypeService#validateAttributes(java.lang.String,
     *      java.util.Map<java.lang.String,java.lang.String>)
     */
    public List<RemotableAttributeError> validateAttributes(@WebParam(name = "kewTypeId") String kewTypeId,
            @WebParam(name = "attributes") @XmlJavaTypeAdapter(
                    value = MapStringStringAdapter.class) Map<String, String> attributes) throws RiceIllegalArgumentException {
        return null;
    }

    /**
     * @see org.kuali.rice.kew.framework.peopleflow.PeopleFlowTypeService#validateAttributesAgainstExisting(java.lang.String,
     *      java.util.Map<java.lang.String,java.lang.String>, java.util.Map<java.lang.String,java.lang.String>)
     */
    public List<RemotableAttributeError> validateAttributesAgainstExisting(
            @WebParam(name = "kewTypeId") String kewTypeId, @WebParam(name = "newAttributes") @XmlJavaTypeAdapter(
            value = MapStringStringAdapter.class) Map<String, String> newAttributes,
            @WebParam(name = "oldAttributes") @XmlJavaTypeAdapter(
                    value = MapStringStringAdapter.class) Map<String, String> oldAttributes) throws RiceIllegalArgumentException {
        return null;
    }

    public DataDictionaryRemoteFieldService getDataDictionaryRemoteFieldService() {
        return KRADServiceLocatorWeb.getDataDictionaryRemoteFieldService();
    }
}
