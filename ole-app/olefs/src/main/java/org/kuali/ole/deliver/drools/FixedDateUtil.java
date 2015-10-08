package org.kuali.ole.deliver.drools;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLEParameterConstants;
import org.kuali.ole.deliver.bo.OleFixedDateTimeSpan;
import org.kuali.ole.deliver.bo.OleFixedDueDate;
import org.kuali.ole.deliver.calendar.service.OleCalendarService;
import org.kuali.ole.deliver.calendar.service.impl.OleCalendarServiceImpl;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterKey;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pvsubrah on 3/19/15.
 */
public class FixedDateUtil {
    public Timestamp getFixedDateByPolicyId(String ruleName){
        Timestamp dueDate = null;
            java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
            List<OleFixedDueDate> oleFixedDueDates =  getFixedDueDateBasedOnPolicySet(ruleName);
            if(oleFixedDueDates!=null && oleFixedDueDates.size()>0){
                for(OleFixedDueDate oleFixedDueDate : oleFixedDueDates){
                    List<OleFixedDateTimeSpan> oleFixedDateTimeSpans = oleFixedDueDate.getOleFixedDateTimeSpanList();
                    for(OleFixedDateTimeSpan oleFixedDateTimeSpan : oleFixedDateTimeSpans){
                        if(dateComparison(date,oleFixedDateTimeSpan.getFromDueDate(),oleFixedDateTimeSpan.getToDueDate())){
                            String defaultCloseTime = getParameter(OLEParameterConstants.DEF_CLOSE_TIME);
                            dueDate = Timestamp.valueOf(new SimpleDateFormat(OLEConstants.CHECK_IN_DATE_TIME_FORMAT).
                                    format(oleFixedDateTimeSpan.getFixedDueDate()).concat(" ").concat(defaultCloseTime));
                            break;
                        }
                    }
                }
            }
        return dueDate;
    }

    private List<OleFixedDueDate> getFixedDueDateBasedOnPolicySet(String ruleName){
        Map<String,String> policySet = new HashMap<>();
        policySet.put("circulationPolicySetId",ruleName);
        List<OleFixedDueDate> oleFixedDueDates =  (List<OleFixedDueDate>) KRADServiceLocator.getBusinessObjectService().findMatching(OleFixedDueDate.class,policySet);
        return oleFixedDueDates;
    }

    private boolean dateComparison(Date date1,Date date2,Date date3){
        if(date1!=null && date2!=null && date3!=null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String dateString1 = dateFormat.format(date1);
            String dateString2 = dateFormat.format(date2);
            String dateString3 = dateFormat.format(date3);
            if((date1.after(date2) && date1.before(date3)) || dateString1.equals(dateString2) || dateString1.equals(dateString3)){
                return true;
            }
        }
        return false;
    }

    public String getParameter(String name) {
        ParameterKey parameterKey = ParameterKey.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
        Parameter parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        if(parameter==null){
            parameterKey = ParameterKey.create(OLEConstants.APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,name);
            parameter = CoreServiceApiServiceLocator.getParameterRepositoryService().getParameter(parameterKey);
        }
        return parameter!=null?parameter.getValue():null;
    }
}

