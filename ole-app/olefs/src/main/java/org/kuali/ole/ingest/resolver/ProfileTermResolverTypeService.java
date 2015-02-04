package org.kuali.ole.ingest.resolver;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.resolver.ExistingTermResolver;
import org.kuali.ole.ingest.resolver.IncomingTermResolver;
import org.kuali.rice.krms.api.engine.TermResolver;
import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;
import org.kuali.rice.krms.framework.type.TermResolverTypeService;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 3/28/12
 * Time: 2:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProfileTermResolverTypeService implements TermResolverTypeService {
    /**
     *    Returns the instance of IncomingTermResolver or ExistingTermResolver based on resolverName  from TermResolverDefinition
     * @param termResolverDefinition
     * @return  TermResolver
     */
    @Override
    public TermResolver<?> loadTermResolver(TermResolverDefinition termResolverDefinition) {
        String resolverName = termResolverDefinition.getName();
        if (resolverName.equals(OLEConstants.INCOMING_FIELD)) {
            return new IncomingTermResolver();
        } else if (resolverName.equals(OLEConstants.EXISTING_FIELD)){
             return new ExistingTermResolver();
        }
        throw new IllegalArgumentException("Failed to load term resolver from the given definition: " +
                termResolverDefinition);
    }
}
