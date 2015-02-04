package org.kuali.ole.deliver.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.context.ContextSelectionCriteria;
import org.kuali.rice.krms.impl.repository.KrmsRepositoryServiceLocator;
import org.kuali.rice.krms.impl.repository.RuleRepositoryServiceImpl;

/**
 * Created by vivekb on 9/5/14.
 */
public class OLERuleRepositoryServiceImpl extends RuleRepositoryServiceImpl {

    private static final Logger LOG = Logger.getLogger(OLERuleRepositoryServiceImpl.class);
    /**
     * This method is overridden to change db call to cache call...
     *
     * @see org.kuali.rice.krms.api.repository.RuleRepositoryService#selectContext(org.kuali.rice.krms.api.repository.context.ContextSelectionCriteria)
     */
    @Override
    public ContextDefinition selectContext(
            ContextSelectionCriteria contextSelectionCriteria) {
        if (contextSelectionCriteria == null){
            throw new IllegalArgumentException("selection criteria is null");
        }
        if (StringUtils.isBlank(contextSelectionCriteria.getNamespaceCode())){
            throw new IllegalArgumentException("selection criteria namespaceCode is null or blank");
        }
        long b1 = System.currentTimeMillis();
        ContextDefinition result = KrmsRepositoryServiceLocator.getContextBoService().getContextByNameAndNamespace(contextSelectionCriteria.getName(),
                contextSelectionCriteria.getNamespaceCode());
        long b2 = System.currentTimeMillis();
        long tot = b2 - b1;
        LOG.info("-----------TimeTaken to complete selectContext -----------"+tot);

        return result;
    }

}
