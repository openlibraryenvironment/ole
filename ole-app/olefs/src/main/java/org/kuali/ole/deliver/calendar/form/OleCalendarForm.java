package org.kuali.ole.deliver.calendar.form;

import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;


/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/20/13
 * Time: 5:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarForm extends TransactionalDocumentFormBase {

    public OleCalendarForm() {
        super();
    }

    @Override
    protected String getDefaultDocumentTypeName() {
        return "OLE_DLVR_CAL_DOC";
    }
}
