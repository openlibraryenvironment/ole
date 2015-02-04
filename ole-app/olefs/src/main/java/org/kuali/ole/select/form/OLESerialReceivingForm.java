package org.kuali.ole.select.form;

import org.kuali.rice.krad.web.form.TransactionalDocumentFormBase;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 6/26/13
 * Time: 4:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLESerialReceivingForm extends TransactionalDocumentFormBase {
    public OLESerialReceivingForm() {
        super();
    }

    @Override
    protected String getDefaultDocumentTypeName() {
        return "OLE_SER_RECV_REC";
    }
}
