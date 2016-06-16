package org.kuali.ole.spring.batch.processor;

import org.apache.camel.ErrorHandlerFactory;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;

/**
 * Created by SheikS on 4/10/2016.
 */
public class OleNGBatchCamelRoutesDefinition extends RoutesDefinition {
    @Override
    protected RouteDefinition createRoute() {
        RouteDefinition route = new OleNGBatchCamelRouteDefinition();
        ErrorHandlerFactory handler = getErrorHandlerBuilder();
        if (handler != null) {
            route.setErrorHandlerBuilderIfNull(handler);
        }
        return route;
    }


}
