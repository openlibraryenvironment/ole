package org.kuali.ole.spring.batch.processor;

import org.apache.camel.CamelContextAware;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinitionHelper;
import org.apache.camel.model.SplitDefinition;
import org.apache.camel.processor.CamelInternalProcessor;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.aggregate.AggregationStrategyBeanAdapter;
import org.apache.camel.spi.RouteContext;
import org.apache.camel.util.CamelContextHelper;

import java.util.concurrent.ExecutorService;

/**
 * Created by SheikS on 4/10/2016.
 */
public class OleNGBatchCamelSplitDefinition extends SplitDefinition {
    private Object objectForCustomProcess;

    @Override
    public Processor createProcessor(RouteContext routeContext) throws Exception {
        Processor childProcessor = this.createChildProcessor(routeContext, true);
         setAggregationStrategy(createAggregationStrategy(routeContext));

        boolean isParallelProcessing = getParallelProcessing() != null && getParallelProcessing();
        boolean isStreaming = getStreaming() != null && getStreaming();
        boolean isShareUnitOfWork = getShareUnitOfWork() != null && getShareUnitOfWork();
        boolean isParallelAggregate = getParallelAggregate() != null && getParallelAggregate();
        boolean shutdownThreadPool = ProcessorDefinitionHelper.willCreateNewThreadPool(routeContext, this, isParallelProcessing);
        ExecutorService threadPool = ProcessorDefinitionHelper.getConfiguredExecutorService(routeContext, "Split", this, isParallelProcessing);

        long timeout = getTimeout() != null ? getTimeout() : 0;
        if (timeout > 0 && !isParallelProcessing) {
            throw new IllegalArgumentException("Timeout is used but ParallelProcessing has not been enabled.");
        }
        if (getOnPrepareRef() != null) {
            setOnPrepare(CamelContextHelper.mandatoryLookup(routeContext.getCamelContext(), getOnPrepareRef(), Processor.class));
        }

        Expression exp = getExpression().createExpression(routeContext);

        OleNGBatchCamelSplitter answer = new OleNGBatchCamelSplitter(getObjectForCustomProcess(), routeContext.getCamelContext(), exp, childProcessor, getAggregationStrategy(),
                isParallelProcessing, threadPool, shutdownThreadPool, isStreaming, isStopOnException(),
                timeout, getOnPrepare(), isShareUnitOfWork, isParallelAggregate);

        answer.setObjectForCustomProcess(getObjectForCustomProcess());
        if (isShareUnitOfWork) {
            // wrap answer in a sub unit of work, since we share the unit of work
            CamelInternalProcessor internalProcessor = new CamelInternalProcessor(answer);
            internalProcessor.addAdvice(new CamelInternalProcessor.SubUnitOfWorkProcessorAdvice());
            return internalProcessor;
        }
        return answer;
    }

    private AggregationStrategy createAggregationStrategy(RouteContext routeContext) {
        AggregationStrategy strategy = getAggregationStrategy();
        if (strategy == null && getStrategyRef() != null) {
            Object aggStrategy = routeContext.lookup(getStrategyRef(), Object.class);
            if (aggStrategy instanceof AggregationStrategy) {
                strategy = (AggregationStrategy) aggStrategy;
            } else if (aggStrategy != null) {
                AggregationStrategyBeanAdapter adapter = new AggregationStrategyBeanAdapter(aggStrategy, getStrategyMethodName());
                if (getStrategyMethodAllowNull() != null) {
                    adapter.setAllowNullNewExchange(getStrategyMethodAllowNull());
                    adapter.setAllowNullOldExchange(getStrategyMethodAllowNull());
                }
                strategy = adapter;
            } else {
                throw new IllegalArgumentException("Cannot find AggregationStrategy in Registry with name: " + getStrategyRef());
            }
        }

        if (strategy != null && strategy instanceof CamelContextAware) {
            ((CamelContextAware) strategy).setCamelContext(routeContext.getCamelContext());
        }

        return strategy;
    }

    public Object getObjectForCustomProcess() {
        return objectForCustomProcess;
    }

    public void setObjectForCustomProcess(Object objectForCustomProcess) {
        this.objectForCustomProcess = objectForCustomProcess;
    }
}
