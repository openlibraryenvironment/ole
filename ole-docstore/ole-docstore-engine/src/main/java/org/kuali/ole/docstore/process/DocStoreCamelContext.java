package org.kuali.ole.docstore.process;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.LoggingErrorHandlerBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class to Handle DocStore Processes.
 *
 * @author Rajesh Chowdary K
 * @created Mar 15, 2012
 */
public class DocStoreCamelContext {

    private static Logger log = LoggerFactory.getLogger(BulkLoadHandler.class);
    private static DocStoreCamelContext handlerContext = null;
    private CamelContext context = null;
    private LoggingErrorHandlerBuilder errorHandler = new LoggingErrorHandlerBuilder();

    private DocStoreCamelContext() {
        context = new DefaultCamelContext();
        try {
            context.start();
            log.info("DocStoreCamelContext Initialized");
        } catch (Exception e) {
            log.error("Ingest Handler Service Starup Failed : ", e);
            log.info(e.getMessage());
        }
    }

    /**
     * Method to get Instance of DocStoreCamelContext.
     *
     * @return
     */
    public static DocStoreCamelContext getInstance() {
        if (handlerContext == null) {
            handlerContext = new DocStoreCamelContext();
        }
        return handlerContext;
    }

    public boolean isRunning() {
        return !context.isSuspended();
    }

    public void resume() throws Exception {
        if (context.isSuspended()) {
            context.resume();
        }
    }

    public void suspend() throws Exception {
        if (!context.isSuspended()) {
            context.suspend();
        }
    }

    public void stop() {
        try {
            BulkLoadHandler.getInstance().stop();
            context.stop();
        } catch (Exception e) {
            log.error("Ingest Handler Service Cold Stop Failed : ", e);
        }
    }

    public CamelContext getCamelContext() {
        return context;
    }

    public LoggingErrorHandlerBuilder getErrorHandler() {
        return errorHandler;
    }

}
