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
package org.kuali.rice.ken.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.ojb.broker.OptimisticLockException;
import org.kuali.rice.ken.service.ProcessingResult;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Base class for jobs that must obtain a set of work items atomically
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class ConcurrentJob<T> {
    /**
     * Oracle's "ORA-00054: resource busy and acquire with NOWAIT specified"
     */
    private static final int ORACLE_00054 = 54;
    /**
     * Oracle's "ORA-00060 deadlock detected while waiting for resource"
     */
    private static final int ORACLE_00060 = 60;
    

    protected final Logger LOG = Logger.getLogger(getClass());

    protected ExecutorService executor;
    protected PlatformTransactionManager txManager;
    
    /**
     * Constructs a ConcurrentJob instance.
     * @param txManager PlatformTransactionManager to use for transactions
     * @param executor the ExecutorService to use to process work items
     */
    public ConcurrentJob(PlatformTransactionManager txManager, ExecutorService executor) {
        this.txManager = txManager;
        this.executor = executor;
    }

    /**
     * Helper method for creating a TransactionTemplate initialized to create
     * a new transaction
     * @return a TransactionTemplate initialized to create a new transaction
     */
    protected TransactionTemplate createNewTransaction() {
        TransactionTemplate tt = new TransactionTemplate(txManager);
        tt.setPropagationBehavior(TransactionTemplate.PROPAGATION_REQUIRES_NEW);
        return tt;
    }

    /**
     * Template method that subclasses should override to obtain a set of available work items
     * and mark them as taken
     * @return a collection of available work items that have been marked as taken
     */
    protected abstract Collection<T> takeAvailableWorkItems();

    /**
     * Template method that subclasses should override to group work items into units of work
     * @param workItems list of work items to break into groups
     * @param result ProcessingResult to modify if there are any failures...this is sort of a hack because previously
     * failure to obtain a deliverer was considered a work item failure, and now this method has been factored out...
     * but the tests still want to see the failure
     * @return a collection of collection of work items
     */
    protected Collection<Collection<T>> groupWorkItems(Collection<T> workItems, ProcessingResult result) {
        Collection<Collection<T>> groupedWorkItems = new ArrayList<Collection<T>>();
        
        if (workItems != null) {
	        for (T workItem: workItems) {
	            Collection<T> c = new ArrayList<T>(1);
	            c.add(workItem);
	            groupedWorkItems.add(c);
	        }
        }
        return groupedWorkItems;
    }

    /**
     * Template method that subclasses should override to process a given work item and mark it
     * as untaken afterwards
     * @param items the work item
     * @return a collection of success messages
     */
    protected abstract Collection<?> processWorkItems(Collection<T> items);

    /**
     * Template method that subclasses should override to unlock a given work item when procesing has failed.
     * @param item the work item to unlock
     */
    protected abstract void unlockWorkItem(T item);

    /**
     * Main processing method which invokes subclass implementations of template methods
     * to obtain available work items, and process them concurrently
     * @return a ProcessingResult object containing the results of processing
     */
    public ProcessingResult run() {
        LOG.debug("[" + new Timestamp(System.currentTimeMillis()).toString() + "] STARTING RUN");

        final ProcessingResult result = new ProcessingResult();

        // retrieve list of available work items in a transaction
        Collection<T> items = null;
        try {
            items = (Collection<T>)
                createNewTransaction().execute(new TransactionCallback() {
                    public Object doInTransaction(TransactionStatus txStatus) {
                        return takeAvailableWorkItems();
                    }
                });
        } catch (DataAccessException dae) {
            // Spring does not detect OJB's org.apache.ojb.broker.OptimisticLockException and turn it into a
            // org.springframework.dao.OptimisticLockingFailureException?
            if (ExceptionUtils.indexOfType(dae, OptimisticLockException.class) != -1) {
                // anticipated in the case that another thread is trying to grab items
                LOG.info("Contention while taking work items");
            } else {
                // in addition to logging a message, should we throw an exception or log a failure here?
                LOG.error("Error taking work items", dae);
                Throwable t = dae.getMostSpecificCause();
                if (t != null && t instanceof SQLException) {
                    SQLException sqle = (SQLException) t;
                    if (sqle.getErrorCode() == ORACLE_00054 && StringUtils.contains(sqle.getMessage(), "resource busy")) {
                        // this is expected and non-fatal given that these jobs will run again
                        LOG.warn("Select for update lock contention encountered");
                    } else if (sqle.getErrorCode() == ORACLE_00060 && StringUtils.contains(sqle.getMessage(), "deadlock detected")) {
                        // this is bad...two parties are waiting forever somewhere...
                        // database is probably wedged now :(
                        LOG.error("Select for update deadlock encountered!");
                    }
                }
            }
            return result;
        } catch (UnexpectedRollbackException ure) {
            // occurs against Mckoi... :(
            LOG.error("UnexpectedRollbackException - possibly due to Mckoi");
            return result;
        } catch (TransactionException te) {
            LOG.error("Error occurred obtaining available work items", te);
            result.addFailure("Error occurred obtaining available work items: " + te);
            return result;
        }

        Collection<Collection<T>> groupedWorkItems = groupWorkItems(items, result);

        // now iterate over all work groups and process each
        Iterator<Collection<T>> i = groupedWorkItems.iterator();
        List<Future> futures = new ArrayList<Future>();
        while(i.hasNext()) {
            final Collection<T> workUnit= i.next();

            LOG.info("Processing work unit: " + workUnit);
            /* performed within transaction */
            /* executor manages threads to run work items... */
            futures.add(executor.submit(new Callable() {
                public Object call() throws Exception {
                    ProcessingResult result = new ProcessingResult();
                    try {
                        Collection<?> successes = (Collection<Object>)
                            createNewTransaction().execute(new TransactionCallback() {
                                public Object doInTransaction(TransactionStatus txStatus) {
                                    return processWorkItems(workUnit);
                                }
                            });
                        result.addAllSuccesses(successes);
                    } catch (Exception e) {
                        LOG.error("Error occurred processing work unit " + workUnit, e);
                        for (final T workItem: workUnit) {
                            LOG.error("Error occurred processing work item " + workItem, e);
                            result.addFailure("Error occurred processing work item " + workItem + ": " + e);
                            unlockWorkItemAtomically(workItem);
                        }
                    }
                    return result;
                }
            }));
        }

        // wait for workers to finish
        for (Future f: futures) {
            try {
                ProcessingResult workResult = (ProcessingResult) f.get();
                result.add(workResult);
            } catch (Exception e) {
                String message = "Error obtaining work result: " + e;
                LOG.error(message, e);
                result.addFailure(message);
            }
        }

        LOG.debug("[" + new Timestamp(System.currentTimeMillis()).toString() + "] FINISHED RUN - " + result);

        return result;
    }
    
    protected void unlockWorkItemAtomically(final T workItem) {
        try {
            createNewTransaction().execute(new TransactionCallback() {
                public Object doInTransaction(TransactionStatus txStatus) {
                    LOG.info("Unlocking failed work item: " + workItem);
                    unlockWorkItem(workItem);
                    return null;
                }
            });
        } catch (Exception e2) {
            LOG.error("Error unlocking failed work item " + workItem, e2);
        }
    }
}
