package org.kuali.ole.sys.context;

import org.kuali.ole.OleLocationsXMLPollerServiceImpl;
import org.kuali.ole.OleXmlPollerService;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by pvsubrah on 12/6/13.
 */
public class OlePollingStandaloneLifeCycle extends BaseLifecycle {
    protected final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OlePollingStandaloneLifeCycle.class);

    private ScheduledExecutorService scheduledExecutor;
    private List<OleXmlPollerService> xmlPollingServices;
    private List<ScheduledFuture> futures = new ArrayList<ScheduledFuture>();

    public void start() throws Exception {
        LOG.info("Configuring XML ingestion pipeline...");
        scheduledExecutor = Executors.newScheduledThreadPool(2);
        if (!ConfigContext.getCurrentContextConfig().getDevMode()) {
            for (Iterator<OleXmlPollerService> iterator = xmlPollingServices.iterator(); iterator.hasNext(); ) {
                OleXmlPollerService oleXmlPollerService = iterator.next();
                LOG.info("Starting XML data loader for " + oleXmlPollerService.getClass().getName()  +"Polling at " + oleXmlPollerService.getPollIntervalSecs() + "-second intervals");
                futures.add(scheduledExecutor.scheduleWithFixedDelay(oleXmlPollerService, oleXmlPollerService.getInitialDelaySecs(), oleXmlPollerService.getPollIntervalSecs(), TimeUnit.SECONDS));
            }
            super.start();
        }
    }

    public void stop() throws Exception {
        if (isStarted()) {
            LOG.warn("Shutting down XML file polling timer");
            try {
                if (futures != null) {
                    for (Iterator<ScheduledFuture> iterator = futures.iterator(); iterator.hasNext(); ) {
                        ScheduledFuture future = iterator.next();
                        if (!future.cancel(false)) {
                            LOG.warn("Failed to cancel the XML Poller service.");
                        }
                        future = null;
                    }
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

    public void setXmlPollingServices(List xmlPollingServices) {
        this.xmlPollingServices = xmlPollingServices;
    }

    public List getXmlPollingServices() {
        return xmlPollingServices;
    }
}
