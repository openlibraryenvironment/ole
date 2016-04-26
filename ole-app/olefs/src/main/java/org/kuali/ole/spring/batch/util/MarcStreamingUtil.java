package org.kuali.ole.spring.batch.util;

import org.kuali.ole.OleCamelContext;
import org.kuali.ole.oleng.batch.process.model.BatchProcessTxObject;
import org.kuali.ole.spring.batch.processor.DynamicMarcStreamRouteBuilder;

/**
 * Created by SheikS on 4/4/2016.
 */
public class MarcStreamingUtil {

    public void addDynamicMarcStreamRoute(OleCamelContext camelContext, String endPointFrom, int chunkSize, BatchProcessTxObject batchProcessTxObject) throws Exception {
        camelContext.addRoutes(new DynamicMarcStreamRouteBuilder(camelContext.getContext(), endPointFrom, chunkSize, batchProcessTxObject));
    }
}
