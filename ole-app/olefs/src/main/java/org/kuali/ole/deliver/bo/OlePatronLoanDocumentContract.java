package org.kuali.ole.deliver.bo;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 10/25/12
 * Time: 3:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OlePatronLoanDocumentContract extends Versioned, Identifiable {

    public String getItemBarcode();

    public String getTitle();

    public String getAuthor();

    public Date getDueDate();

    public String getLocation();

    public String getCallNumber();

    public String getMessageInfo();

}
