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
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableRadioButtonGroup;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeAttribute;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import org.kuali.rice.krms.framework.engine.Rule;
import org.kuali.rice.krms.framework.type.ValidationActionType;
import org.kuali.rice.krms.framework.type.ValidationRuleType;
import org.kuali.rice.krms.framework.type.ValidationRuleTypeService;
import org.kuali.rice.krms.impl.provider.repository.RepositoryToEngineTranslator;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.type.KrmsTypeServiceBase;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * {@link ValidationRuleTypeService} implementation
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class ValidationRuleTypeServiceImpl extends KrmsTypeServiceBase implements ValidationRuleTypeService {

    private RepositoryToEngineTranslator translator;

    /**
     * private constructor to enforce use of static factory
     */
    private ValidationRuleTypeServiceImpl(){
        super();
    }

    /**
     * Factory method for getting a {@link ValidationRuleTypeService}
     * @return a {@link ValidationRuleTypeService} corresponding to the given {@link ValidationRuleType}.
     */
    public static ValidationRuleTypeService getInstance() {
        return new ValidationRuleTypeServiceImpl();
    }

    @Override
    public Rule loadRule(RuleDefinition validationRuleDefinition) {
        if (validationRuleDefinition == null) { throw new RiceIllegalArgumentException("validationRuleDefinition must not be null"); }
        if (validationRuleDefinition.getAttributes() == null) { throw new RiceIllegalArgumentException("validationRuleDefinition must not be null");}

        if (!validationRuleDefinition.getAttributes().containsKey(VALIDATIONS_RULE_TYPE_CODE_ATTRIBUTE)) {

            throw new RiceIllegalArgumentException("validationRuleDefinition does not contain an " +
                    VALIDATIONS_RULE_TYPE_CODE_ATTRIBUTE + " attribute");
        }
        String validationRuleTypeCode = validationRuleDefinition.getAttributes().get(VALIDATIONS_RULE_TYPE_CODE_ATTRIBUTE);

        if (StringUtils.isBlank(validationRuleTypeCode)) {
            throw new RiceIllegalArgumentException(VALIDATIONS_RULE_TYPE_CODE_ATTRIBUTE + " attribute must not be null or blank");
        }

        if (ValidationRuleType.VALID.getCode().equals(validationRuleTypeCode)) {
            return new ValidationRule(ValidationRuleType.VALID, validationRuleDefinition.getName(),
                    translator.translatePropositionDefinition(validationRuleDefinition.getProposition()),
                    translator.translateActionDefinitions(validationRuleDefinition.getActions()));
        }
        if (ValidationRuleType.INVALID.getCode().equals(validationRuleTypeCode)) {
            return new ValidationRule(ValidationRuleType.INVALID, validationRuleDefinition.getName(),
                    translator.translatePropositionDefinition(validationRuleDefinition.getProposition()),
                    translator.translateActionDefinitions(validationRuleDefinition.getActions()));
        }
        return null;
    }

    /**
     * @param translator the RepositoryToEngineTranslator to set
     */
    public void setRepositoryToEngineTranslator(RepositoryToEngineTranslator translator) {
        this.translator = translator;
    }

    /**
     *
     * @return List<RemotableAttributeField>
     * @throws RiceIllegalArgumentException
     */
    @Override
    public List<RemotableAttributeField> getAttributeFields(@WebParam(name = "krmsTypeId") String krmsTypeId) throws RiceIllegalArgumentException {
        Map<String, String> keyLabels = new TreeMap<String, String>();
        keyLabels.put(ValidationRuleType.VALID.getCode(), "Valid - action executed when false");
        keyLabels.put(ValidationRuleType.INVALID.getCode(), "Invalid - action executed when true");
        return getAttributeFields(krmsTypeId, keyLabels);
    }

    /**
     *
     * @param krmsTypeId
     * @param keyLabels Map<String, String> where the key is the VallidationRuleType code with the value being the UI Label.
     * @return List<RemotableAttributeField> for Validation Rules
     * @throws RiceIllegalArgumentException if krmsType is null (krmsTypeId lookup returns null)
     */
    private List<RemotableAttributeField> getAttributeFields(@WebParam(name = "krmsTypeId") String krmsTypeId, Map<String, String> keyLabels) throws RiceIllegalArgumentException {
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
                KrmsTypeRepositoryService typeRepositoryService = KrmsRepositoryServiceLocator.getKrmsTypeRepositoryService();
                KrmsAttributeDefinition attributeDefinition = null;
                RadioButtonTypeServiceUtil util = new RadioButtonTypeServiceUtil();

                for (KrmsTypeAttribute typeAttribute : typeAttributes) {


                    attributeDefinition = typeRepositoryService.getAttributeDefinitionById(typeAttribute.getAttributeDefinitionId());

                    if (VALIDATIONS_RULE_TYPE_CODE_ATTRIBUTE.equals(attributeDefinition.getName())) {
                        RemotableAttributeField attributeField = util.translateTypeAttribute(attributeDefinition, keyLabels);
                        results.add(attributeField);
                    }
                }
            }
        }
        return results;
    }
}

