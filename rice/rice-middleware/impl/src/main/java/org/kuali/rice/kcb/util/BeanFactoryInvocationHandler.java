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
package org.kuali.rice.kcb.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.springframework.beans.factory.BeanFactory;

/**
 * Maps accessor invocations to beans in a BeanFactory. 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class BeanFactoryInvocationHandler implements InvocationHandler {
     private static final String PREFIX = "get";

     private BeanFactory beanFactory;
     
     public BeanFactoryInvocationHandler(BeanFactory beanFactory) {
         this.beanFactory = beanFactory;
     }

     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
         if (args != null && args.length > 0) {
             throw new RuntimeException("BeanFactoryInvocationHandler only serves getters");
         }
         String methodName = method.getName();
         if (!methodName.startsWith(PREFIX)) {
             throw new RuntimeException("BeanFactoryInvocationHandler only serves getters");
         }
         String beanName = methodName.substring(PREFIX.length());
         if (beanName.length() == 0) {
             throw new RuntimeException("Illegal accessor, no bean name specified: " + methodName);
         }
         char firstChar = beanName.charAt(0);
         if (!Character.isLowerCase(firstChar)) {
             beanName = Character.toLowerCase(firstChar) + beanName.substring(1);
         }
         return beanFactory.getBean(beanName);
     }
}
