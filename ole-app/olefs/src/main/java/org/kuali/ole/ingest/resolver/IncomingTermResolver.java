package org.kuali.ole.ingest.resolver;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.pojo.ProfileTerm;

/**
 * Created by IntelliJ IDEA.
 * User: pvsubrah
 * Date: 3/28/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class IncomingTermResolver extends AbstractProfileTermResolver{
    /**
     *  Returns the  incomingFields need to be resolved.
     * @param profileTerm
     * @return incomingFields.
     */
    @Override
    protected Object resolveFromProfileTerm(ProfileTerm profileTerm) {
        return profileTerm.getIncomingField();
    }

    /**
     * Gets the incoming field constant from oleConstant.
     * @return  incoming Field
     */
    @Override
    public String getOutput() {
        return OLEConstants.INCOMING_FIELD;
    }
}
