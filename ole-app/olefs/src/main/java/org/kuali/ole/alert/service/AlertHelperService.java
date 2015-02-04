package org.kuali.ole.alert.service;

import org.kuali.ole.alert.bo.ActionListAlertBo;

import java.util.List;

/**
 * Created by maheswarang on 11/4/14.
 */

/**
 * This service is defined for processing the alerts in the action list
 */
public interface AlertHelperService {
    public List<ActionListAlertBo> getActionListAlertsByUserId(String userId);
    public void approveActionListAlert(ActionListAlertBo actionListAlertBo);
}
