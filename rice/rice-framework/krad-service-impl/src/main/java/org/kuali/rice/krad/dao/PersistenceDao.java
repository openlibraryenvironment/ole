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
package org.kuali.rice.krad.dao;

public interface PersistenceDao {
    public void clearCache();
    
    public Object resolveProxy(Object o);
    
    public void retrieveAllReferences(Object o);
    
    public void retrieveReference(Object o, String referenceName);
    
    /**
     * Determines if the given object is proxied by the ORM or not
     * 
     * @param object the object to determine if it is a proxy
     * @return true if the object is an ORM proxy; false otherwise
     */
    public abstract boolean isProxied(Object object);
}
