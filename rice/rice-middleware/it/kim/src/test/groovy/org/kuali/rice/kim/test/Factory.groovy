/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kim.test

import org.kuali.rice.core.api.mo.common.Identifiable

/**
 * Helps constructs objects
 */
class Factory {
    def private static factories = []
    def private static long counter = System.currentTimeMillis()

    def Factory() {
        factories.add(this)
    }

    /**
     * returns string ids using a global in memory counter
     * @return
     */
    def synchronized static makeId() {
        counter++;
        Long.toHexString(counter)
    }

    /**
     * Invoke a factory method
     * @param fields object field values
     * @param type name of type/factory method
     * @return constructed object
     */
    def static make(Map fields = [:], type) {
        def short_name = { name ->
            (name =~ /([^\.]*)$/)[0][1]
        }
        def methodName = short_name(type.name)
        def found = factories.findResult {
            def method = it.metaClass.methods.find { it.name == methodName }
            if (method != null) {
                return it."$methodName"(fields)
            }
        }
        if (found != null) {
            return found
        } else {
            throw new RuntimeException("No factory found for " + type)
        }
    }

    /**
     * Determines inserts associatino key in dest map if key is present in source
     * @param key associated object field name
     * @param src object field values
     * @param dest destination for linking property
     * @param id_key optional foreign key field name, if ommitted: key + 'Id'
     * @return new field value map
     */
    def static assignRelationId(key, src, dest, id_key = key + 'Id') {
       Identifiable identifiable = src[key]
       if (identifiable != null) {
           println("Setting $id_key to $identifiable.id")
           dest[id_key] = identifiable.id
       }
       dest
    }

     /**
     * Determines appropriate key for association and inserts it into the map of field values
     * @param key associated object field name
     * @param fields object field values
     * @param defaults default/generated values
     * @param id_key optional foreign key field name, if ommitted: key + 'Id'
     * @return new field value map
     */
    def static mergeAndLink(key, fields, defaults = [:], id_key = key + 'Id') {
       assignRelationId(key, fields, defaults, id_key)
       defaults.putAll(fields)
       defaults.remove(key)
       defaults
    }
}
