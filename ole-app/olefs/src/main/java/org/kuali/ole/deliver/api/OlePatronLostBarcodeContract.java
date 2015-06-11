package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/26/12
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OlePatronLostBarcodeContract extends Versioned, Identifiable {

    public String getOlePatronLostBarcodeId();

    public String getOlePatronId();

    public Timestamp getInvalidOrLostBarcodeEffDate();

    public String getInvalidOrLostBarcodeNumber();

    public String getObjectId();


}
