package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/11/12
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleDeliverRequestTypeContract extends Versioned, Identifiable {

    public String getRequestTypeId();

    public String getRequestTypeCode();

    public String getRequestTypeName();

    public String getRequestTypeDescription();

    public boolean isActive();

}
