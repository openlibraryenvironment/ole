package org.kuali.ole.alert.document;

import org.kuali.ole.alert.bo.AlertBo;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 12/18/14.
 */
public class OlePersistableBusinessObjectBase extends PersistableBusinessObjectBase {
    @Transient
    public List<AlertBo> alertBoList = new ArrayList();

    public List<AlertBo> getAlertBoList() {
        return alertBoList;
    }

    public void setAlertBoList(List<AlertBo> alertBoList) {
        this.alertBoList = alertBoList;
    }

    public OlePersistableBusinessObjectBase() {
        super();
        alertBoList=new ArrayList<>();
    }
}
