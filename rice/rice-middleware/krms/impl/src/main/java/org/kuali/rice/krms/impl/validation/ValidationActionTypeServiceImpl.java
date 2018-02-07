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
package org.kuali.rice.krms.impl.validation;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableRadioButtonGroup;
import org.kuali.rice.core.api.uif.RemotableTextInput;
import org.kuali.rice.core.api.uif.RemotableTextarea;
import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.framework.type.ValidationActionService;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.framework.type.ActionTypeService;
import org.kuali.rice.krms.framework.type.ValidationActionType;
import org.kuali.rice.krms.framework.type.ValidationActionTypeService;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.type.KrmsTypeServiceBase;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * {@link ValidationActionTypeService} implementation
 *
 * @author Kuali Rice Team (rice.collab@kuali.org).
 */
public class ValidationActionTypeServiceImpl extends KrmsTypeServiceBase implements ValidationActionTypeService {

    private ValidationActionService validationService;

    private ValidationActionTypeServiceImpl() {}

    /**
     * Factory method for getting a {@link ActionTypeService}
     * @return a {@link ActionTypeService}
     */
    public static ActionTypeService getInstance() {
        return new ValidationActionTypeServiceImpl();
    }

    @Override
    public Action loadAction(ActionDefinition validationActionDefinition) {

        if (validationActionDefinition == null) { throw new RiceIllegalArgumentException("validationActionDefinition must not be null"); }
        if (validationActionDefinition.getAttributes() == null) { throw new RiceIllegalArgumentException("validationActionDefinition must not be null");}

        // TypeCode
        if (!validationActionDefinition.getAttributes().containsKey(ValidationActionTypeService.VALIDATIONS_ACTION_TYPE_CODE_ATTRIBUTE)) {
            throw new RiceIllegalArgumentException("validationActionDefinition does not contain an " +
                    ValidationActionTypeService.VALIDATIONS_ACTION_TYPE_CODE_ATTRIBUTE + " attribute");
        }
        String validationActionTypeCode = validationActionDefinition.getAttributes().get(ValidationActionTypeService.VALIDATIONS_ACTION_TYPE_CODE_ATTRIBUTE);
        if (StringUtils.isBlank(validationActionTypeCode)) {
            throw new RiceIllegalArgumentException(ValidationActionTypeService.VALIDATIONS_ACTION_TYPE_CODE_ATTRIBUTE + " attribute must not be null or blank");
        }

        // Message
        if (!validationActionDefinition.getAttributes().containsKey(VALIDATIONS_ACTION_MESSAGE_ATTRIBUTE)) {
            throw new RiceIllegalArgumentException("validationActionDefinition does not contain an " +
                    VALIDATIONS_ACTION_MESSAGE_ATTRIBUTE + " attribute");
        }
        String validationMessage = validationActionDefinition.getAttributes().get(VALIDATIONS_ACTION_MESSAGE_ATTRIBUTE);
        if (StringUtils.isBlank(validationMessage)) {
            throw new RiceIllegalArgumentException(VALIDATIONS_ACTION_MESSAGE_ATTRIBUTE + " attribute must not be null or blank");
        }

        if (ValidationActionType.WARNING.getCode().equals(validationActionTypeCode)) {
            return new ValidationAction(ValidationActionType.WARNING, validationMessage);
        }
        if (ValidationActionType.ERROR.getCode().equals(validationActionTypeCode)) {
            return new ValidationAction(ValidationActionType.ERROR, validationMessage);
        }
        return null;
    }

    /**
     *
     * @return List<RemotableAttributeField> for Validation Actions
     * @throws RiceIllegalArgumentException if krmsType is null (krmsTypeId lookup returns null)
     */
    @Override
    public List<RemotableAttributeField> getAttributeFields(@WebParam(name = "krmsTypeId") String krmsTypeId) throws RiceIllegalArgumentException {

        List<RemotableAttributeField> results = new ArrayList<RemotableAttributeField>();

        KrmsTypeDefinition krmsType =
                KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService().getTypeById(krmsTypeId);

        if (krmsType == null) {
            throw new RiceIllegalArgumentException("krmsTypeId must be a valid id of a KRMS type");
        } else {
            List<KrmsTypeAttribute> typeAttributes = krmsType.getAttributes();
            Map<String, Integer> attribDefIdSequenceNumbers = new TreeMap<String, Integer>();
            Map<String, String> unsortedIdLables = new TreeMap<String, String>();
            if (!CollectionUtils.isEmpty(typeAttributes)) {
                // translate the attribute and store the sort code in our map
                Map<String, String> keyLabels = new TreeMap<String, String>();
                keyLabels.put(ValidationActionType.WARNING.getCode(), "Warning Action");
                keyLabels.put(ValidationActionType.ERROR.getCode(), "Error Action");

                KrmsTypeRepositoryService typeRepositoryService = KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();
                KrmsAttributeDefinition attributeDefinition = null;
                RadioButtonTypeServiceUtil util = new RadioButtonTypeServiceUtil();

                for (KrmsTypeAttribute typeAttribute : typeAttributes) {

                    attributeDefinition = typeRepositoryService.getAttributeDefinitionById(typeAttribute.getAttributeDefinitionId());

                    if (VALIDATIONS_ACTION_TYPE_CODE_ATTRIBUTE.equals(attributeDefinition.getName())) {
                        RemotableAttributeField attributeField = util.translateTypeAttribute(attributeDefinition, keyLabels);
                        results.add(attributeField);
                    }

                    if (VALIDATIONS_ACTION_MESSAGE_ATTRIBUTE.equals(attributeDefinition.getName())) {
                        RemotableAttributeField attributeField = createMessageField(attributeDefinition);
                        results.add(attributeField);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Create the Message RemotableAttributeField
     * @param krmsAttributeDefinition of the Message field
     * @return RemotableAttributeField for the Message field
     */
    private RemotableAttributeField createMessageField(KrmsAttributeDefinition krmsAttributeDefinition) {

        RemotableTextarea.Builder controlBuilder = RemotableTextarea.Builder.create();
        controlBuilder = RemotableTextarea.Builder.create();
        controlBuilder.setRows(Integer.valueOf(2));
        controlBuilder.setCols(Integer.valueOf(30));
        controlBuilder.setWatermark("Enter a Validation Action Message");

        RemotableAttributeField.Builder builder = RemotableAttributeField.Builder.create(krmsAttributeDefinition.getName());
        builder.setRequired(true);
        builder.setDataType(DataType.STRING);
        builder.setControl(controlBuilder);
        builder.setLongLabel(krmsAttributeDefinition.getLabel());
        builder.setShortLabel("Message");
        builder.setMinLength(Integer.valueOf(1));
        builder.setMaxLength(Integer.valueOf(400));

        return builder.build();
    }


    @Override
    public void setValidationService(ValidationActionService mockValidationService) {
        if (mockValidationService == null) {
            throw new RiceIllegalArgumentException("validationService must not be null");
        }
        this.validationService = mockValidationService;
    }
}
