package org.kuali.ole.ingest.resolver;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.pojo.ProfileTerm;
import org.kuali.rice.krms.api.engine.TermResolutionException;
import org.kuali.rice.krms.api.engine.TermResolver;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 3/28/12
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractProfileTermResolver implements TermResolver<Object> {
    /**
     *  This method gets the unique prerequisites based on OleConstant value.
     * @return  prerequisite.
     */
    @Override
    public Set<String> getPrerequisites() {
        return Collections.singleton(OLEConstants.ISBN_TERM);
    }

    /**
     * This method gets the empty immutable set of parameterNames
     * @return  parameterNames.
     */
    @Override
    public Set<String> getParameterNames() {
        return Collections.emptySet();
    }

    /**
     * Gets the cost attribute value
     * @return  1.
     */
    @Override
    public int getCost() {
        return 1;
    }

    /**
     *  Returns the resolved prerequisites as profileTerm object
     * @param resolvedPrereqs
     * @param parameters
     * @return   profileTerm
     * @throws org.kuali.rice.krms.api.engine.TermResolutionException
     */
    @Override
    public Object resolve(Map<String, Object> resolvedPrereqs,
                          Map<String, String> parameters) throws TermResolutionException {
        ProfileTerm profileTerm = (ProfileTerm) resolvedPrereqs.get("ISBN Term");
        if (profileTerm == null) {
            throw new TermResolutionException("Failed to locate Patron prereq", this, parameters);
        }
        return resolveFromProfileTerm(profileTerm);
    }

    protected abstract Object resolveFromProfileTerm(ProfileTerm profileTerm);
}
