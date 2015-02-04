package org.kuali.ole.deliver.api;

import org.kuali.rice.kim.api.identity.address.EntityAddressContract;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/10/12
 * Time: 9:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleEntityAddressContract {

    public EntityAddressContract getEntityAddressBo();

    public OleAddressContract getOleAddressBo();
}
