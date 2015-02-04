package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/25/12
 * Time: 12:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleAddressContract extends Versioned, Identifiable {

    public String getOleAddressId();

    public boolean isAddressVerified();

    public String getOlePatronId();

    public String getId();

    public Date getAddressValidFrom();

    public Date getAddressValidTo();

    public String getAddressSource();

    public OleAddressSourceContract getAddressSourceBo();

    //public EntityAddressContract getEntityAddress();
}
