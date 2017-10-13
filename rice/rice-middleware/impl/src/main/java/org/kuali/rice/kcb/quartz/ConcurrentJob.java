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
package org.kuali.rice.kcb.quartz;

import org.apache.log4j.Logger;
import org.apache.ojb.broker.OptimisticLockException;
import org.kuali.rice.core.api.util.RiceUtilities;
import org.kuali.rice.kcb.quartz.ProcessingResult.Failure;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

/**
 * Base class for jobs that must obtain a set of work items atomically
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class ConcurrentJob<T> {
    protected final Logger LOG = Logger.getLogger(getClass());

    protected ExecutorService executor = Executors.newSingleThreadExecutor(new KCBThreadFactory());
    protected PlatformTransactionManager txManager;

    /**
     * Sets the {@link ExecutorService} to use to process work items.  Default is single-threaded.
     * @param executor the {@link ExecutorService} to use to process work items.  Default is single-threaded.
     */
    public void setExecutorService(ExecutorService executor) {
        this.executor = executor;
    }

    /**
     * Sets the {@link PlatformTransactionManager}
     * @param txManager the {@link PlatformTransactionManager} 
     */
    @Required
    public void setTransactionManager(PlatformTransactionManager txManager) {
        this.txManager = txManager;
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
    protected Collection<Collection<T>> groupWorkItems(Collection<T> workItems, ProcessingResult<T> result) {
        Collection<Collection<T>> groupedWorkItems = new ArrayList<Collection<T>>(workItems.size());
        for (T workItem: workItems) {
            Collection<T> c = new ArrayList<T>(1);
            c.add(workItem);
            groupedWorkItems.add(c);
        }
        return groupedWorkItems;
    }

    /**
     * Template method that subclasses should override to process a given work item and mark it
     * as untaken afterwards
     * @param item the work item
     * @return a collection of success messages
     */
    protected abstract Collection<T> processWorkItems(Collection<T> items);

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
    public ProcessingResult<T> run() {
        LOG.debug("[" + new Timestamp(System.currentTimeMillis()).toString() + "] STARTING RUN");

        final ProcessingResult<T> result = new ProcessingResult<T>();

        // retrieve list of available work items in a transaction
        final Collection<T> items;
        try {
            items = executeInTransaction(new TransactionCallback() {
                public Object doInTransaction(TransactionStatus txStatus) {
                    return takeAvailableWorkItems();
                }
            });
        } catch (DataAccessException dae) {
            // Spring does not detect OJB's org.apache.ojb.broker.OptimisticLockException and turn it into a
            // org.springframework.dao.OptimisticLockingFailureException?
            OptimisticLockException optimisticLockException = RiceUtilities.findExceptionInStack(dae, OptimisticLockException.class);
            if (optimisticLockException != null) {
                // anticipated in the case that another thread is trying to grab items
                LOG.info("Contention while taking work items");
            } else {
                // in addition to logging a message, should we throw an exception or log a failure here?
                LOG.error("Error taking work items", dae);
            }
            return result;
        } catch (UnexpectedRollbackException ure) {
            // occurs against Mckoi... :(
            LOG.error("UnexpectedRollbackException - possibly due to Mckoi");
            return result;
        } catch (TransactionException te) {
            LOG.error("Error occurred obtaining available work items", te);
            result.addFailure(new Failure<T>(te));
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
                    ProcessingResult<T> result = new ProcessingResult<T>();
                    try {
                        Collection<T> successes = executeInTransaction(new TransactionCallback() {
                            public Object doInTransaction(TransactionStatus txStatus) {
                                return processWorkItems(workUnit);
                            }
                        });
                        result.addAllSuccesses(successes);
                    } catch (Exception e) {
                        LOG.error("Error occurred processing work unit " + workUnit, e);
                        for (final T workItem: workUnit) {
                            LOG.error("Error occurred processing work item " + workItem, e);
                            result.addFailure(new Failure<T>(workItem, e));
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
                ProcessingResult<T> workResult = (ProcessingResult<T>) f.get();
                result.add(workResult);
            } catch (Exception e) {
                String message = "Error obtaining work result: " + e;
                LOG.error(message, e);
                result.addFailure(new Failure<T>(e, message));
            }
        }

        finishProcessing(result);

        LOG.debug("[" + new Timestamp(System.currentTimeMillis()).toString() + "] FINISHED RUN - " + result);

        return result;
    }

    /**
     * Template method called after processing of work items has completed
     */
    protected void finishProcessing(ProcessingResult<T> result) {}

    protected void unlockWorkItemAtomically(final T workItem) {
        try {
            executeInTransaction(new TransactionCallback() {
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
    
    protected <X> X executeInTransaction(TransactionCallback callback) {
        // haha just kidding
        //return (X) callback.doInTransaction(null);
        return (X) createNewTransaction().execute(callback);
    }
    
    private static class KCBThreadFactory implements ThreadFactory {
		
		private ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
		
		public Thread newThread(Runnable runnable) {
			Thread thread = defaultThreadFactory.newThread(runnable);
			thread.setName("KCB-job-" + thread.getName());
			return thread;
	    }
	}
}
