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
package org.kuali.rice.core.impl.util.spring;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.aop.ClassFilter;

/**
 * Matches if the annotation specified during construction is present on the class or any of methods returned by class.getMethods().
 */
public class ClassOrMethodAnnotationFilter implements ClassFilter {
    private final Class<? extends Annotation> annotationType;

    public ClassOrMethodAnnotationFilter(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    public boolean matches(Class<?> clazz) {
        boolean isAnnotationPresent = clazz.isAnnotationPresent(this.annotationType);
        if (!isAnnotationPresent) {
            for (Method method : clazz.getMethods()) {
                if (method.isAnnotationPresent(annotationType)) {
                    isAnnotationPresent = true;
//                    if (Transactional.class.equals(this.annotationType)) {
//                        throw new RuntimeException("The @Transactional annotation should be specified at the class level and overriden at the method level, if need be.");
//                    }
                    break;
                }
            }
        }
        return isAnnotationPresent;
    }
}
