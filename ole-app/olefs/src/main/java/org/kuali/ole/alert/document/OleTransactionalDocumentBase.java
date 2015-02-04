package org.kuali.ole.alert.document;

import org.kuali.ole.alert.bo.AlertBo;
import org.kuali.rice.krad.document.TransactionalDocumentBase;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 11/3/14.
 */
@MappedSuperclass
public class OleTransactionalDocumentBase extends TransactionalDocumentBase {

    @Transient
    public List<AlertBo> alertBoList = new ArrayList();

    public List<AlertBo> getAlertBoList() {
        return alertBoList;
    }

    public void setAlertBoList(List<AlertBo> alertBoList) {
        this.alertBoList = alertBoList;
    }

    public OleTransactionalDocumentBase(){
        super();
        alertBoList = new ArrayList<>();
    }
}
