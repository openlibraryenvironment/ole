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
package org.kuali.rice.ksb.messaging.threadpool;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.ksb.util.KSBConstants;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class KSBScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor implements KSBScheduledPool {

	private static final Logger LOG = Logger.getLogger(KSBScheduledThreadPoolExecutor.class);

	private boolean started;
	private static final int DEFAULT_SIZE = 2;

	public KSBScheduledThreadPoolExecutor() {
		super(DEFAULT_SIZE, new KSBThreadFactory());
	}

	public boolean isStarted() {
		return started;
	}

	public void start() throws Exception {
		LOG.info("Starting the KSB scheduled thread pool...");
		try {
			Integer size = new Integer(ConfigContext.getCurrentContextConfig().getProperty(KSBConstants.Config.FIXED_POOL_SIZE));
			this.setCorePoolSize(size);
		} catch (NumberFormatException nfe) {
			// ignore this, instead the pool will be set to DEFAULT_SIZE
		}
		LOG.info("...KSB scheduled thread pool successfully started.");
	}

	public void stop() throws Exception {
		LOG.info("Stopping the KSB scheduled thread pool...");
		try {
            int pendingTasks = this.shutdownNow().size();
            LOG.info(pendingTasks + " pending tasks...");
			LOG.info("awaiting termination: " + this.awaitTermination(20, TimeUnit.SECONDS));
			LOG.info("...KSB scheduled thread pool successfully stopped, isShutdown=" + this.isShutdown() + ", isTerminated=" + this.isTerminated());
		} catch (Exception e) {
			LOG.warn("Exception thrown shutting down " + KSBScheduledThreadPoolExecutor.class.getSimpleName(), e);
		}

	}
	
	private static class KSBThreadFactory implements ThreadFactory {
		
		private ThreadFactory defaultThreadFactory = Executors.defaultThreadFactory();
		
		public Thread newThread(Runnable runnable) {
			Thread thread = defaultThreadFactory.newThread(runnable);
			thread.setName("KSB-Scheduled-" + thread.getName());
			return thread;
	    }
	}

}
