package org.kuali.ole.deliver.api;

import org.kuali.rice.core.api.mo.common.Identifiable;
import org.kuali.rice.core.api.mo.common.Versioned;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 5/28/12
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public interface OlePatronNotesContract extends Versioned, Identifiable {

    public String getPatronNoteId();

    public String getOlePatronId();

    public String getPatronNoteText();

    public OlePatronNoteTypeContract getOlePatronNoteType();

    public boolean isActive();

    public String getObjectId();

    //public OlePatronContract getOlePatron();
}
