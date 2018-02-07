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
package org.kuali.rice.krad.uif.util;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.uif.component.Component;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Spring <code>BeanPostProcessor</code> that processes configured <code>Component</code>
 * instances in the dictionary
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ComponentBeanPostProcessor implements BeanPostProcessor {

    public ComponentBeanPostProcessor() {
    }

    /**
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessBeforeInitialization(java.lang.Object,
     *      java.lang.String)
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * Sets the unique Id for a <code>Component</code> if bean name given (not generated) and the id property was
     * not set for the view
     *
     * <p>
     * The ID will only be set here if an id is given for the Spring bean. For inner beans, the ID will be generated
     * during the view lifecycle
     * </p>
     *
     * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object,
     *      java.lang.String)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Component) {
            Component component = (Component) bean;

            if (StringUtils.isBlank(component.getId())) {
                if (!StringUtils.contains(beanName, "$") && !StringUtils.contains(beanName, "#")) {
                    component.setId(beanName);
                    component.setBaseId(beanName);
                }
            }
        }

        return bean;
    }
}
