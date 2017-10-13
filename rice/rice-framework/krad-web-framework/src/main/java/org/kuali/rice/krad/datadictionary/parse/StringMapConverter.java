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
package org.kuali.rice.krad.datadictionary.parse;

import org.kuali.rice.krad.util.KRADUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Converts a string configured in the dictionary for a map property type to a Map
 * object using the convention of commas to separate map entries and a colon to separate
 * the key value pair
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StringMapConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        Set<ConvertiblePair> pairs = new HashSet<ConvertiblePair>();

        ConvertiblePair stringMapPair = new ConvertiblePair(String.class, Map.class);
        pairs.add(stringMapPair);

        return pairs;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return KRADUtils.convertStringParameterToMap((String) source);
    }
}
