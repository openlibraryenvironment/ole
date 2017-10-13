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

import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;

/**
 * Wrapper for ClassOrMethodAnnotationFilter and ClassOrMethodAnnotationMatcher that are based on the anotation type specified
 * during construction.
 */
public class ClassOrMethodAnnotationPointcut implements Pointcut {
    private final ClassFilter classFilter;
    private final MethodMatcher methodMatcher;

    public ClassOrMethodAnnotationPointcut(Class<? extends Annotation> annotationType) {
        this.classFilter = new ClassOrMethodAnnotationFilter(annotationType);
        this.methodMatcher = new ClassOrMethodAnnotationMatcher(annotationType);
    }

    /**
     * @see org.springframework.aop.Pointcut#getClassFilter()
     */
    public ClassFilter getClassFilter() {
        return classFilter;
    }

    /**
     * @see org.springframework.aop.Pointcut#getMethodMatcher()
     */
    public MethodMatcher getMethodMatcher() {
        return methodMatcher;
    }

}
