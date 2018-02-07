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
package org.kuali.rice.kns.datadictionary.exporter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Deprecated
public abstract class DataDictionaryMapBase implements Map {

    public int size() {
        throw new UnsupportedOperationException( "This operation not supported on a " + this.getClass().getName() );
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean containsKey(Object key) {
        return get( key ) != null;
    }

    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException( "This operation not supported on a " + this.getClass().getName() );
    }

    public Object put(Object key, Object value) {
        throw new UnsupportedOperationException( "This operation not supported on a " + this.getClass().getName() );
    }

    public Object remove(Object key) {
        throw new UnsupportedOperationException( "This operation not supported on a " + this.getClass().getName() );
    }

    public void putAll(Map map) {
        throw new UnsupportedOperationException( "This operation not supported on a " + this.getClass().getName() );
    }

    public void clear() {
        throw new UnsupportedOperationException( "This operation not supported on a " + this.getClass().getName() );
    }

    public Set keySet() {
        throw new UnsupportedOperationException( "This operation not supported on a " + this.getClass().getName() );
    }

    public Collection values() {
        throw new UnsupportedOperationException( "This operation not supported on a " + this.getClass().getName() );
    }

    public Set entrySet() {
        throw new UnsupportedOperationException( "This operation not supported on a " + this.getClass().getName() );
    }
    
}
