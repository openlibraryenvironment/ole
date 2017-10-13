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
package org.kuali.rice.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

/**
 * This is a description of what this class does - g don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class ImmutableProperties extends Properties {
        
        public ImmutableProperties(Properties properties){
                super();
                for(Object o: properties.keySet()){
                        super.put(o, properties.get(o));
                }               
        }

        @Override
        public synchronized void load(InputStream inStream) throws IOException {
                 throw new UnsupportedOperationException("This class is immutable");
        }

        @Override
        public synchronized void load(Reader reader) throws IOException {
                throw new UnsupportedOperationException("This class is immutable");
        }

        @Override
        public synchronized void loadFromXML(InputStream in) throws IOException,
                        InvalidPropertiesFormatException {
                throw new UnsupportedOperationException("This class is immutable");
        }

    @Deprecated
        @Override
        public synchronized void save(OutputStream out, String comments) {
                throw new UnsupportedOperationException("This class is immutable");
        }

        @Override
        public synchronized Object setProperty(String key, String value) {
                throw new UnsupportedOperationException("This class is immutable");
        }

        @Override
        public synchronized Object put(Object key, Object value) {
                throw new UnsupportedOperationException("This class is immutable");
        }

        @Override
        public synchronized void putAll(Map<? extends Object, ? extends Object> t) {
                throw new UnsupportedOperationException("This class is immutable");
        }

        @Override
        public synchronized Object remove(Object key) {
                throw new UnsupportedOperationException("This class is immutable");
        }

}