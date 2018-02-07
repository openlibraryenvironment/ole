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
package org.kuali.rice.krms.framework.type;

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.util.jaxb.MapStringStringAdapter;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.framework.engine.Rule;

import javax.jws.WebParam;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;
import java.util.Map;

/**
 * {@link ValidationRule} Service
 * 
 * @see ValidationRule
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface ValidationRuleTypeService extends RuleTypeService {
    /**
     * VALIDATIONS_RULE_TYPE_CODE_ATTRIBUTE - Database krms_attr_defn_t NM value
     */
    static public final String VALIDATIONS_RULE_TYPE_CODE_ATTRIBUTE = "ruleTypeCode"; // Database krms_attr_defn_t NM value

    @Override
    public Rule loadRule(RuleDefinition validationRuleDefinition);

    @Override
    public List<RemotableAttributeField> getAttributeFields(@WebParam(name = "krmsTypeId") String krmsTypeId)
            throws RiceIllegalArgumentException;

    @Override
    public List<RemotableAttributeError> validateAttributesAgainstExisting(
            @WebParam(name = "krmsTypeId") String krmsTypeId, @WebParam(name = "newAttributes") @XmlJavaTypeAdapter(
            value = MapStringStringAdapter.class) Map<String, String> newAttributes, @WebParam(name = "oldAttributes") @XmlJavaTypeAdapter(
            value = MapStringStringAdapter.class) Map<String, String> oldAttributes) throws RiceIllegalArgumentException;

    @Override
    public List<RemotableAttributeError> validateAttributes(@WebParam(name = "krmsTypeId") String krmsTypeId, @WebParam(name = "attributes") @XmlJavaTypeAdapter(
            value = MapStringStringAdapter.class) Map<String, String> attributes) throws RiceIllegalArgumentException;
}
