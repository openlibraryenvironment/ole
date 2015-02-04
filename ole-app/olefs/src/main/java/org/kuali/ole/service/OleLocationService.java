package org.kuali.ole.service;

import org.kuali.ole.describe.bo.OleLocation;
import org.kuali.ole.deliver.bo.OlePatronDocument;

/**
 * Created by IntelliJ IDEA.
 * User: Gopalp
 * Date: 5/31/12
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OleLocationService {

    public boolean createLocation(OleLocation oleLocation);

    public boolean updateLocation(OleLocation oleLocation);

    public OleLocation getLocation(String locationCode);


}
