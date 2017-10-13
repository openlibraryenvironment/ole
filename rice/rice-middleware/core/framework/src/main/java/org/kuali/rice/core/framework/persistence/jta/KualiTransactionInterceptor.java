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
package org.kuali.rice.core.framework.persistence.jta;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * Transaction interceptor which does logging at various levels 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class KualiTransactionInterceptor extends TransactionInterceptor {
	private static final Logger LOG = Logger.getLogger(KualiTransactionInterceptor.class);
	
	/**
     * @see org.springframework.transaction.interceptor.TransactionAspectSupport#createTransactionIfNecessary(java.lang.reflect.Method,
     *      java.lang.Class)
     */
	@Override
    protected TransactionInfo createTransactionIfNecessary(Method method, Class targetClass) {
        TransactionInfo txInfo = super.createTransactionIfNecessary(method, targetClass);

        // using INFO level since DEBUG level turns on the (somewhat misleading) log statements of the superclass
        if (logger.isDebugEnabled()) {
            if (txInfo != null) {
                TransactionStatus txStatus = txInfo.getTransactionStatus();
                if (txStatus != null) {
                    if (txStatus.isNewTransaction()) {
                        LOG.debug("creating explicit transaction for " + txInfo.getJoinpointIdentification());
                    }
                    else {
                        if (txStatus instanceof DefaultTransactionStatus) {
                            DefaultTransactionStatus dtxStatus = (DefaultTransactionStatus) txStatus;

                            if (dtxStatus.isNewSynchronization()) {
                                LOG.debug("creating implicit transaction for " + txInfo.getJoinpointIdentification());
                            }
                        }
                    }
                }
            }
        }

        return txInfo;
    }

    /**
     * @see org.springframework.transaction.interceptor.TransactionAspectSupport#doCloseTransactionAfterThrowing(org.springframework.transaction.interceptor.TransactionAspectSupport.TransactionInfo,
     *      java.lang.Throwable)
     */
	@Override
    protected void completeTransactionAfterThrowing(TransactionInfo txInfo, Throwable ex) {
		LOG.fatal( "Exception caught by Transaction Interceptor, this will cause a rollback at the end of the transaction.", ex );
        // using INFO level since DEBUG level turns on the (somewhat misleading) log statements of the superclass
        if (logger.isDebugEnabled()) {
            if (txInfo != null) {
                TransactionStatus txStatus = txInfo.getTransactionStatus();
                if (txStatus != null) {
                    if (txStatus.isNewTransaction()) {
                        LOG.debug("closing explicit transaction for " + txInfo.getJoinpointIdentification());
                    }
                    else {
                        if (txStatus instanceof DefaultTransactionStatus) {
                            DefaultTransactionStatus dtxStatus = (DefaultTransactionStatus) txStatus;

                            if (dtxStatus.isNewSynchronization()) {
                                LOG.debug("closing implicit transaction for " + txInfo.getJoinpointIdentification());
                            }
                        }
                    }
                }
            }
        }

        super.completeTransactionAfterThrowing(txInfo, ex);
    }
	
	/**
     * @see org.springframework.transaction.interceptor.TransactionAspectSupport#doCommitTransactionAfterReturning(org.springframework.transaction.interceptor.TransactionAspectSupport.TransactionInfo)
     */
    @Override
    protected void commitTransactionAfterReturning(TransactionInfo txInfo) {
        // using INFO level since DEBUG level turns on the (somewhat misleading) log statements of the superclass
        if (logger.isDebugEnabled()) {
            if (txInfo != null) {
                TransactionStatus txStatus = txInfo.getTransactionStatus();
                if (txStatus != null) {
                    if (txStatus.isNewTransaction()) {
                        LOG.debug("committing explicit transaction for " + txInfo.getJoinpointIdentification());
                    }
                    else {
                        if (txStatus instanceof DefaultTransactionStatus) {
                            DefaultTransactionStatus dtxStatus = (DefaultTransactionStatus) txStatus;

                            if (dtxStatus.isNewSynchronization()) {
                                LOG.debug("committing implicit transaction for " + txInfo.getJoinpointIdentification());
                            }
                        }
                    }
                }
            }
        }

        super.commitTransactionAfterReturning(txInfo);
    }
}
