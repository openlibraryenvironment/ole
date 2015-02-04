package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/28/12
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleBorrowerTypeContract extends Identifiable, Versioned {

    public String getBorrowerTypeId();

    public String getBorrowerTypeCode();

    public String getBorrowerTypeDescription();

    public String getBorrowerTypeName();

}
