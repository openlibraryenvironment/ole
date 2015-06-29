package org.kuali.ole.alert.service;

import org.kuali.ole.alert.bo.AlertBo;

import java.util.List;

/**
 * Created by maheswarang on 11/13/14.
 */
public interface AlertService {
    public List<AlertBo> retrieveAlertList(String documentNumber);
    public List<AlertBo> retrieveApprovedAlertList(String documentNumber);
    public void deleteAlerts(String documentNumber);
    public void deleteActionListAlerts(String documentNumber);

}
