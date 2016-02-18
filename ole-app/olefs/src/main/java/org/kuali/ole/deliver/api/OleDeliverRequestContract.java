package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/11/12
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleDeliverRequestContract extends Versioned, Identifiable {

    public String getItemId();

    public String getRequestId();

    public String getItemStatus();

    public String getItemType();

    public Integer getBorrowerQueuePosition();

    public String getTitle();

    public String getAuthor();

    public String getShelvingLocation();

    public String getCallNumber();

    public String getCopyNumber();

    public String getPatronName();

    public Timestamp getCreateDate();

    public String getVolumeNumber();

    public OleDeliverRequestTypeContract getOleDeliverRequestType();
}
