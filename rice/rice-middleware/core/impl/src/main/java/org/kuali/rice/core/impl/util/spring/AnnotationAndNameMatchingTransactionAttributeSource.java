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

import java.lang.reflect.Method;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionAttribute;

/**
 * Classes are not considered for name matching, if they do not have the specified annotation on the class or method. However, the
 * name matching takes precendence, if they do.
 */
public class AnnotationAndNameMatchingTransactionAttributeSource extends NameMatchTransactionAttributeSource {
    
	private static final long serialVersionUID = 6119879657045541414L;
	
	private AnnotationTransactionAttributeSource annotationTransactionAttributeSource;
    private Integer transactionTimeout;

    /**
     * @see org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource#getTransactionAttribute(java.lang.reflect.Method,
     *      java.lang.Class)
     */
    @Override
    public TransactionAttribute getTransactionAttribute(Method method, Class<?> targetClass) {
        TransactionAttribute transactionAttribute = annotationTransactionAttributeSource.getTransactionAttribute(method, targetClass);
        if (transactionAttribute != null) {
            TransactionAttribute overridingTransactionAttribute = super.getTransactionAttribute(method, targetClass);
            if (overridingTransactionAttribute != null) {
                transactionAttribute = overridingTransactionAttribute;
            }
        }
        setTimeout(transactionAttribute);
        return transactionAttribute;
    }

    /**
     * Sets the annotationTransactionAttributeSource attribute value.
     *
     * @param annotationTransactionAttributeSource The annotationTransactionAttributeSource to set.
     */
    public void setAnnotationTransactionAttributeSource(AnnotationTransactionAttributeSource annotationTransactionAttributeSource) {
        this.annotationTransactionAttributeSource = annotationTransactionAttributeSource;
    }

    public void setTransactionTimeout(Integer transactionTimeout) {
        this.transactionTimeout = transactionTimeout;
    }

    /**
     * If the TransactionAttribute has a setTimeout() method, then this method will invoke it and pass the configured
     * transaction timeout.  This is to allow for proper setting of the transaction timeout on a JTA transaction.
     */
    protected void setTimeout(TransactionAttribute transactionAttribute) {
        try {
            if (transactionTimeout != null) {
                PropertyUtils.setSimpleProperty(transactionAttribute, "timeout", new Integer(transactionTimeout));
            }
        } catch (Exception e) {
            // failed to set the timeout
        }
    }
}
