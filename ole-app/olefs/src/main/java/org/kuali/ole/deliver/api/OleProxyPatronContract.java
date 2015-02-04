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
public interface OleProxyPatronContract extends Versioned, Identifiable {

    public String getOleProxyPatronDocumentId();

    public String getProxyPatronId();

    public String getOlePatronId();

    //public OlePatronDocument getProxyPatron();

    //public OlePatronContract getOlePatronDocument();

    public Date getProxyPatronExpirationDate();

    public Date getProxyPatronActivationDate();

    public boolean isActive();

}
