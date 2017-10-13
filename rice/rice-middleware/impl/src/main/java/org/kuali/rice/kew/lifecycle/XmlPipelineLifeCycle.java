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
package org.kuali.rice.kew.lifecycle;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.kew.batch.XmlPollerService;
import org.kuali.rice.kew.service.KEWServiceLocator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class XmlPipelineLifeCycle extends BaseLifecycle {

	protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(XmlPipelineLifeCycle.class);

	private ScheduledExecutorService scheduledExecutor;
	private ScheduledFuture future;

	public void start() throws Exception {
		LOG.info("Configuring XML ingestion pipeline...");
		scheduledExecutor = Executors.newScheduledThreadPool(1);
		final XmlPollerService xmlPoller = KEWServiceLocator.getXmlPollerService();
		LOG.info("Starting XML data loader.  Polling at " + xmlPoller.getPollIntervalSecs() + "-second intervals");
		if (!ConfigContext.getCurrentContextConfig().getDevMode()) {
			future = scheduledExecutor.scheduleWithFixedDelay(xmlPoller, xmlPoller.getInitialDelaySecs(), xmlPoller.getPollIntervalSecs(), TimeUnit.SECONDS);
			super.start();
		}
	}

	public void stop() throws Exception {
		if (isStarted()) {
			LOG.warn("Shutting down XML file polling timer");
			try {
				if (future != null) {
					if (!future.cancel(false)) {
						LOG.warn("Failed to cancel the XML Poller service.");
					}
					future = null;
				}
				if (scheduledExecutor != null) {
					scheduledExecutor.shutdownNow();
					scheduledExecutor = null;
				}
			} finally {
				super.stop();
			}
		}
	}

}
