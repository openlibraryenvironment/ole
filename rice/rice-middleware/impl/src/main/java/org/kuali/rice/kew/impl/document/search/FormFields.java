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
package org.kuali.rice.kew.impl.document.search;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.kns.lookup.LookupableHelperService;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.util.KRADConstants;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
class FormFields {
    private final Collection<Row> rows;

    FormFields(Collection<Row> rows) {
        this.rows = rows;
    }

    /**
     * Preserves Field values, saving single or array property value depending on field type; single property value is
     * converted into String[1]
     * This implementation makes the assumption that a Field can either represent a single property value, or an array
     * of values but not both! (only one is preserved)
     * @return a Map<String, String[]> containing field values depending on field type
     */
    Map<String, String[]> getFieldValues() {
        Map<String, String[]> values = new HashMap<String, String[]>();
        for (Field field : getFields()) {
            String[] value;
            if(!Field.MULTI_VALUE_FIELD_TYPES.contains(field.getFieldType())) {
                value = new String[] { field.getPropertyValue() };
            } else {
                //multi value, set to values
                value = field.getPropertyValues();
            }
            values.put(field.getPropertyName(), value);
        }
        return values;
    }
    
    Field getField(final String name) {
        return Iterables.tryFind(getFields(), new Predicate<Field>() {
            public boolean apply(@Nullable Field input) {
                return StringUtils.equals(name, input.getPropertyName());
            }
        }).orNull();
    }

    void setFieldValue(String name, String value) {
        setFieldValue(name, new String[]{value});
    }

    void setFieldValue(String name, String[] value) {
        setFieldValues(Collections.singletonMap(name, value));
    }

    /**
     * Overrides Row Field values with Map values
     * @param values the fieldvalues
     */
    void setFieldValues(Map<String, String[]> values) {
        for (Field field: getFields()) {
            if (StringUtils.isNotBlank(field.getPropertyName())) {
                String[] value = values.get(field.getPropertyName());
                if (ArrayUtils.isNotEmpty(value)) {
                    setFieldValue(field, value);
                }
            }
        }
    }

    void applyFieldAuthorizations(LookupableHelperService lhs) {
        for (Field f: getFields()) {
            lhs.applyFieldAuthorizationsFromNestedLookups(f);
        }
    }

    List<Field> getFieldList() {
        return Lists.newArrayList(getFields());
    }

    /**
     * Sets a Field value appropriately, depending on whether it is a "multi-value" field type
     */
    void setFieldValue(Field field, String[] values) {
        if(!Field.MULTI_VALUE_FIELD_TYPES.contains(field.getFieldType())) {
            field.setPropertyValue(CollectionUtils.get(values, 0));
        } else {
            //multi value, set to values
            field.setPropertyValues(values);
        }
    }

    Iterable<Field> getFields() {
        return Iterables.concat(Iterables.transform(this.rows, new Function<Row, Iterable<Field>>() {
            @Override
            public Iterable<Field> apply(@Nullable Row row) {
                return row.getFields();
            }
        }));
    }
}
