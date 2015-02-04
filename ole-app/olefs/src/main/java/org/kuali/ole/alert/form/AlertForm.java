package org.kuali.ole.alert.form;

import org.kuali.ole.alert.bo.ActionListAlertBo;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 11/4/14.
 */

/**
 * This is the form for the alert view in the action list
 */
public class AlertForm extends UifFormBase {

    public List<ActionListAlertBo> actionListAlertList = new ArrayList<>();

    public List<ActionListAlertBo> getActionListAlertList() {
        return actionListAlertList;
    }

    public void setActionListAlertList(List<ActionListAlertBo> actionListAlertList) {
        this.actionListAlertList = actionListAlertList;
    }
}
