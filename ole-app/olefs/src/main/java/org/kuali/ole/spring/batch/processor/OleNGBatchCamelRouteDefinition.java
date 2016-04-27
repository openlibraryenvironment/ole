package org.kuali.ole.spring.batch.processor;

import org.apache.camel.builder.ExpressionClause;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.SplitDefinition;

/**
 * Created by SheikS on 4/10/2016.
 */
public class OleNGBatchCamelRouteDefinition extends RouteDefinition {
    @Override
    public ExpressionClause<SplitDefinition> split() {
        SplitDefinition answer = new OleNGBatchCamelSplitDefinition();
        addOutput(answer);
        return ExpressionClause.createAndSetExpression(answer);
    }
}
