package org.kuali.ole.docstore.process;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 1/11/12
 * Time: 6:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class BulkLoadHandler {
    private static Logger logger = LoggerFactory.getLogger(BulkLoadHandler.class);

    private BulkIngestNIndexRouteBuilder bulkRoute = null;
    private static BulkLoadHandler loadHandler = new BulkLoadHandler();

    private BulkLoadHandler() {
    }

    public static BulkLoadHandler getInstance() {
        return loadHandler;
    }

    public synchronized void loadBulk(String folder, String user, String action) throws Exception {
        if (bulkRoute == null) {
            bulkRoute = new BulkIngestNIndexRouteBuilder(folder, user, action,
                    DocStoreCamelContext.getInstance().getCamelContext());
            bulkRoute.setErrorHandlerBuilder(DocStoreCamelContext.getInstance().getErrorHandler());
            DocStoreCamelContext.getInstance().getCamelContext().addRoutes(bulkRoute);
        } else {
            logger.warn("Bulk Ingest Process is Already Running @ " + folder);
            throw new Exception("Bulk Ingest Process is Already Running @ " + folder);
        }
    }

    public synchronized void start() {
        try {
            bulkRoute.getFileEndPoint().start();
        } catch (Exception e) {
            logger.error("Unable to Stop Bulk Ingest Process : ", e);
        }
    }

    public synchronized void stop() {
        try {
            if (bulkRoute != null && bulkRoute.getFileEndPoint() != null) {
                bulkRoute.getFileEndPoint().stop();
            }
        } catch (Exception e) {
            logger.error("Unable to Stop Bulk Ingest Process : ", e);
        }
    }

    public synchronized void remove() {
        try {
            bulkRoute.getFileEndPoint().shutdown();
            bulkRoute = null;
        } catch (Exception e) {
            logger.error("Unable to Stop Bulk Ingest Process : ", e);
        }
    }

    public String getStatus() {
        if (bulkRoute != null) {
            return bulkRoute.getFileEndPoint().getStatus().name();
        } else {
            return null;
        }
    }

    public BulkIngestNIndexRouteBuilder getBulkRoute() {
        return bulkRoute;
    }

    public void setBulkRoute(BulkIngestNIndexRouteBuilder bulkRoute) {
        this.bulkRoute = bulkRoute;
    }
}
