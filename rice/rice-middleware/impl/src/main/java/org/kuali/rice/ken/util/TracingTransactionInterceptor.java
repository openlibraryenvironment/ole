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
package org.kuali.rice.ken.util;

import javax.transaction.Status;
import javax.transaction.UserTransaction;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.transaction.jta.JtaTransactionManager;

/**
 * Interceptor implementation that simply logs the JTA transaction status
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TracingTransactionInterceptor implements MethodInterceptor {
    private static final Logger LOG = Logger.getLogger(TracingTransactionInterceptor.class);

    private Level level;
    private JtaTransactionManager txManager;

    /**
     * @param lvl
     */
    public void setLevel(String lvl) {
        level = Level.toLevel(lvl);
    }

    /**
     * @param manager
     */
    public void setJtaTransactionManager(JtaTransactionManager manager) {
        this.txManager = manager;
    }

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        StringBuilder sb = new StringBuilder();
        UserTransaction tx = txManager.getUserTransaction();
        if (tx == null) {
            sb.append("null");
        } else {
            sb.append(toString(tx.getStatus()));
        }
        LOG.log(level, invocation.getMethod() +" UserTransaction: " + sb);
        return invocation.proceed();
    }

    /**
     * @param txStatus
     * @return String
     */
    private static final String toString(int txStatus) {
        switch (txStatus) {
            case Status.STATUS_ACTIVE: return "STATUS_ACTIVE";
            case Status.STATUS_COMMITTED: return "STATUS_COMMITTED";  
            case Status.STATUS_COMMITTING: return "STATUS_COMMITTING";
            case Status.STATUS_MARKED_ROLLBACK: return "STATUS_MARKED_ROLLBACK";
            case Status.STATUS_NO_TRANSACTION: return "STATUS_NO_TRANSACTION";
            case Status.STATUS_PREPARED: return "STATUS_PREPARED";
            case Status.STATUS_PREPARING: return "STATUS_PREPARING";
            case Status.STATUS_ROLLEDBACK: return "STATUS_ROLLEDBACK";
            case Status.STATUS_ROLLING_BACK: return "STATUS_ROLLING_BACK";
            case Status.STATUS_UNKNOWN: return "STATUS_UNKNOWN";
            default: return "unknown status: " + txStatus;
        }
    }
}
