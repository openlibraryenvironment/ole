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
public class ExistingTermResolver extends AbstractProfileTermResolver{
    /**
     *  This method returns the profile terms need to be resolved
     * @param profileTerm
     * @return  profileTerm
     */
    @Override
    protected Object resolveFromProfileTerm(ProfileTerm profileTerm) {
        return profileTerm.getExistingField();
    }

    /**
     *     Gets the existing field constant from oleConstants
     * @return  existing field.
     */
    @Override
    public String getOutput() {
        return OLEConstants.EXISTING_FIELD;
    }
}
