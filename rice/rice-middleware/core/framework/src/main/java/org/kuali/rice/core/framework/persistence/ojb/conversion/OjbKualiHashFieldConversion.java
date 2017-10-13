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
package org.kuali.rice.core.framework.persistence.ojb.conversion;

import java.security.GeneralSecurityException;

import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.encryption.EncryptionService;


/**
 * This class calls core service to hash values going to the database.
 * 
 * 
 */

public class OjbKualiHashFieldConversion implements FieldConversion {

    /**
     * @see FieldConversion#javaToSql(Object)
     */
    public Object javaToSql(Object source) {
        Object converted = source;
        if ( converted != null ) {
            // don't convert if already a hashed value
            if ( converted.toString().endsWith( EncryptionService.HASH_POST_PREFIX ) ) {
                converted = StringUtils.stripEnd( converted.toString(), EncryptionService.HASH_POST_PREFIX );
            } else {
                try {
                    converted = CoreApiServiceLocator.getEncryptionService().hash(converted);
                } catch (GeneralSecurityException e) {
                    throw new RuntimeException("Unable to hash value to db: " + e.getMessage());
                }
            }
        }

        return converted;
    }

    /**
     * @see FieldConversion#sqlToJava(Object)
     */
    public Object sqlToJava(Object source) {
        if ( source == null ) {
            return "";
        }
        return source.toString() + EncryptionService.HASH_POST_PREFIX;
    }
}
