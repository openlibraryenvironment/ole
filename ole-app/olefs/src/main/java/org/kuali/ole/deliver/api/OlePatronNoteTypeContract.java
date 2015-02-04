package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/28/12
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OlePatronNoteTypeContract extends Identifiable {

    public String getPatronNoteTypeId();

    public String getPatronNoteTypeCode();

    public String getPatronNoteTypeName();

    public boolean isActive();

}
