/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kew.test;


import static org.junit.Assert.fail;

import org.junit.Test;
import org.kuali.rice.core.framework.persistence.ojb.DataAccessUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springmodules.orm.ojb.PersistenceBrokerTemplate;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class OjbBeanTestCase extends KEWTestCase {

    private Object lock = new Object();

    @Test public void testOptimisticLocking() throws Exception {
        if (isOptimisticallyLocked()) {
            getTransactionTemplate().execute(new TransactionCallback() {
                public Object doInTransaction(TransactionStatus status) {
                    try {
                        Object bean = loadBean();
                        modifyBean(bean);
                        synchronized (lock) {
                            modifyConcurrently();
                            try {
                                lock.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        // transaction from other thread should be commited at this point
                        try {
                            getPersistenceBrokerTemplate().store(bean);
                            fail("The bean was modified by a different transaction, OptimisticLockFailureException should have been thrown.");
                        } catch (Exception e) {
                            if (!DataAccessUtils.isOptimisticLockFailure(e)) {
                				throw e;
            				}
                        }
                        return null;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    private void modifyConcurrently() {
        Runnable runnable = new Runnable() {
            public void run() {
                synchronized (lock) {
                try {
                    getTransactionTemplate().execute(new TransactionCallback() {
                        public Object doInTransaction(TransactionStatus nestedStatus) {
                            try {
                                Object bean = loadBean();
                                modifyBean(bean);
                                getPersistenceBrokerTemplate().store(bean);
                                return null;
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                });
                } catch (Throwable t) {
                    t.printStackTrace();
                    fail(t.getMessage());
                } finally {
                    lock.notify();
                }
                }
            }
        };
        new Thread(runnable).start();
    }

    protected abstract Object loadBean() throws Exception;

    protected abstract void modifyBean(Object bean) throws Exception;

    protected abstract boolean isOptimisticallyLocked();

    protected PersistenceBrokerTemplate getPersistenceBrokerTemplate() {
        return new PersistenceBrokerTemplate();
    }

}
