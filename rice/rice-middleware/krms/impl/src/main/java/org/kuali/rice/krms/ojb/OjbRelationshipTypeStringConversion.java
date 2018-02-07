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
package org.kuali.rice.krms.ojb;

import org.apache.ojb.broker.accesslayer.conversions.ConversionException;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.kuali.rice.krms.api.repository.typerelation.RelationshipType;

/**
 * This class performs an OJB conversion between a String and the code value in the RelationType enum.
 *
 * @author Kuali Student Team
 */
public class OjbRelationshipTypeStringConversion implements FieldConversion {

    @Override
    public Object javaToSql(Object source) throws ConversionException {
        if (source instanceof RelationshipType) {
            if (source != null) {
                RelationshipType relationshipType = (RelationshipType) source;
                return relationshipType.getCode();
            }
        }
        return null;
    }

    @Override
    public Object sqlToJava(Object source) throws ConversionException {
        if (source instanceof String) {
            if (source != null) {
                String s = (String) source;
                for (RelationshipType relType : RelationshipType.values()) {
                    if (relType.getCode().equals(s)) {
                        return relType;
                    }
                }
            }
        }
        return null;
    }
}
