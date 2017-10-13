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

import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableRadioButtonGroup;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility for creating RemotableRadioButtonGroups given a {@link KrmsAttributeDefinition} and a Map<String, String> of (Radio Button) Values and Labels
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RadioButtonTypeServiceUtil {

    /**
     * RemotableRadioButtonGroups given a {@link KrmsAttributeDefinition} and a Map<String, String> of (Radio Button) Values and Labels.
     * @param krmsAttributeDefinition
     * @param valueLabels
     * @return RemotableAttributeField
     */
    RemotableAttributeField translateTypeAttribute(KrmsAttributeDefinition krmsAttributeDefinition, Map<String, String> valueLabels) {

        RemotableAttributeField.Builder builder = RemotableAttributeField.Builder.create(krmsAttributeDefinition.getName());

        RemotableRadioButtonGroup.Builder controlBuilder = RemotableRadioButtonGroup.Builder.create(valueLabels);

        builder.setLongLabel(krmsAttributeDefinition.getLabel());
        builder.setName(krmsAttributeDefinition.getName());
        builder.setRequired(true);
        List<String> defaultValue = new ArrayList<String>();
        defaultValue.add((String) valueLabels.keySet().toArray()[0]); // First value
//        defaultValue.add(valueLabels.get(valueLabels.keySet().toArray()[0])); // First label
//        defaultValue.add("1"); // index
        builder.setDefaultValues(defaultValue);
        builder.setControl(controlBuilder);

        return builder.build();
    }
}
