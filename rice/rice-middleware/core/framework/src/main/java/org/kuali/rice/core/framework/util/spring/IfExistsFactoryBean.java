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
package org.kuali.rice.core.framework.util.spring;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AbstractFactoryBean;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class IfExistsFactoryBean extends AbstractFactoryBean implements BeanFactoryAware {
    private BeanFactory beanFactory;
    private String beanName;
    private Object customReturnValue;

    @Override
    protected Object createInstance() throws Exception {
        if (beanFactory.containsBean(beanName)) {
            if (customReturnValue != null) {
                return customReturnValue;
            } else {
                return beanFactory.getBean(beanName);
            }
        }
        return null;
    }

    /**
     * @see org.springframework.beans.factory.config.AbstractFactoryBean#getObjectType()
     */
    @Override
    public Class getObjectType() {
       return null;
    }

    public BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    public String getBeanName() {
        return this.beanName;
    }

    public Object getCustomReturnValue() {
        return this.customReturnValue;
    }

    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public void setCustomReturnValue(Object customReturnValue) {
        this.customReturnValue = customReturnValue;
    }

}
