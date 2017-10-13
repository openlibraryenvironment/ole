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
package org.kuali.rice.core;

import org.springframework.beans.factory.FactoryBean;

/**
 * The entire purpose of this class is to wrap an otherwise normal bean
 * whose class is dynamically set at runtime through the PropertyPlaceholderConfigurer
 * because the <bean> element attributes themselves are not parameterizable,
 * only the property values. 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BeanHolder implements FactoryBean {
    private Class clazz;
    private Object instance;

    public synchronized Object getObject() throws Exception {
        if (this.instance == null) {
            this.instance = this.clazz.newInstance();
        }
        return this.instance;
    }

    public Class getObjectType() {
        return this.clazz;
    }

    public void setObjectType(Class clazz) {
        this.clazz = clazz;
    }

    public boolean isSingleton() {
        return true;
    }
}
