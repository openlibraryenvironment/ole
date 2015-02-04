package org.kuali.ole.deliver.calendar.maintenance;

import org.kuali.ole.deliver.calendar.bo.*;
import org.kuali.ole.deliver.calendar.service.DateUtil;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.maintenance.MaintenanceLock;
import org.kuali.rice.krad.service.KRADServiceLocator;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: arjuns
 * Date: 7/30/13
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarMaintenanceImpl extends MaintainableImpl {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleCalendarMaintenanceImpl.class);

    @Override
    public List<MaintenanceLock> generateMaintenanceLocks() {
        return Collections.emptyList();
    }
    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {
        LOG.debug("Inside retrieveObjectForEditOrCopy()");
        Object dataObject = null;
        OleCalendar oleCalendar = new OleCalendar();
        try {
            dataObject = getLookupService().findObjectBySearch(getDataObjectClass(), dataObjectKeys);
            oleCalendar = (OleCalendar) dataObject;
            DateUtil dateUtil = new DateUtil();
            // oleCalendar = (OleCalendar) document.getDocumentDataObject();//changed for edit purpose
            for (OleCalendarWeek oldCalendarWeek : oleCalendar.getOleCalendarWeekList()) {

                if (oldCalendarWeek.getOpenTime().equals("") || oldCalendarWeek.getCloseTime().equals("")) {
                    break;
                }
                String convertedOpenTime = "";
                String convertCloseTime = "";
                String oleOpenTime = oldCalendarWeek.getOpenTime();
                String oleCloseTime = oldCalendarWeek.getCloseTime();
                try {
                    convertedOpenTime = dateUtil.convertTo12HoursFormat(oleOpenTime);
                    convertCloseTime = dateUtil.convertTo12HoursFormat(oleCloseTime);
                } catch (java.text.ParseException e) {
                    LOG.error("Exception while converting to 12Hours format", e); //To change body of catch statement use File | Settings | File Templates.
                }

                if (convertedOpenTime != null) {
                    oldCalendarWeek.setOpenTime(convertedOpenTime.substring(0, convertedOpenTime.length() - 2));
                }
                if (convertCloseTime != null) {
                    oldCalendarWeek.setCloseTime(convertCloseTime.substring(0, convertCloseTime.length() - 2));
                }
            }

            oleCalendar.sortCalendarWeek(oleCalendar); //added for OLE-5381
            for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendar.getOleCalendarExceptionPeriodList()) {
                for (OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek : oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList()) {
                    String convOpenTimeExcptPrdWk = "";
                    String convCloseTimeExcptPrdWk = "";
                    String oleOpenTimeExcptPrdWk = oleCalendarExceptionPeriodWeek.getOpenTime();
                    String oleCloseTimeExcptPrdWk = oleCalendarExceptionPeriodWeek.getCloseTime();
                    try {
                        convOpenTimeExcptPrdWk = dateUtil.convertTo12HoursFormat(oleOpenTimeExcptPrdWk);
                        convCloseTimeExcptPrdWk = dateUtil.convertTo12HoursFormat(oleCloseTimeExcptPrdWk);
                    } catch (java.text.ParseException e) {
                        LOG.error("Exception while converting to 12Hours format", e);  //To change body of catch statement use File | Settings | File Templates.
                    }

                    if (convOpenTimeExcptPrdWk != null) {
                        oleCalendarExceptionPeriodWeek.setOpenTime(convOpenTimeExcptPrdWk.substring(0, convOpenTimeExcptPrdWk.length() - 2));
                    }
                    if (convCloseTimeExcptPrdWk != null) {
                        oleCalendarExceptionPeriodWeek.setCloseTime(convCloseTimeExcptPrdWk.substring(0, convCloseTimeExcptPrdWk.length() - 2));
                    }
                }
            }


            for (OleCalendarExceptionDate oleCalendarExceptionDate : oleCalendar.getOleCalendarExceptionDateList()) {
                String convertedOpenTimeExcptDate = "";
                String convertedCloseTimeExcptDate = "";
                String oleOpenTimeExcptDate = oleCalendarExceptionDate.getOpenTime();
                String oleCloseTimeExcptDate = oleCalendarExceptionDate.getCloseTime();
                if ((!oleCalendarExceptionDate.getOpenTime().equals("")) && (!oleCalendarExceptionDate.getCloseTime().equals(""))) { //changed
                    try {
                        convertedOpenTimeExcptDate = dateUtil.convertTo12HoursFormat(oleOpenTimeExcptDate);
                        convertedCloseTimeExcptDate = dateUtil.convertTo12HoursFormat(oleCloseTimeExcptDate);
                    } catch (java.text.ParseException e) {
                        LOG.error("Exception while converting to 12Hours format", e);  //To change body of catch statement use File | Settings | File Templates.
                    }

                    if (convertedOpenTimeExcptDate != null) {
                        oleCalendarExceptionDate.setOpenTime(convertedOpenTimeExcptDate.substring(0, convertedOpenTimeExcptDate.length() - 2));
                    }
                    if (convertedCloseTimeExcptDate != null) {
                        oleCalendarExceptionDate.setCloseTime(convertedCloseTimeExcptDate.substring(0, convertedCloseTimeExcptDate.length() - 2));
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception while converting to 12Hours format", e);
        }
        return oleCalendar;
    }

    @Override
    public void processAfterNew(MaintenanceDocument document,
                                Map<String, String[]> requestParameters) {
        LOG.debug("Inside processAfterNew()");
        super.processAfterNew(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription("New Calendar Document");

    }

    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        LOG.debug("Inside processAfterCopy()");
        super.processAfterCopy(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription("Copied Calendar Document");
    }

    @Override
    public void processAfterEdit(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        LOG.debug("Inside processAfterEdit()");
        super.processAfterEdit(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription("Edited Calendar Document");

    }

    @Override
    public void processAfterRetrieve() {


        try {
            OleCalendar oleCalendar = (OleCalendar) this.getDataObject();
            Map<String, Object> oleCalendarMap = new HashMap<String, Object>();
            oleCalendarMap.put("beginDate", oleCalendar.getBeginDate());
            List<OleCalendar> oleCalendarList = (List<OleCalendar>) KRADServiceLocator.getBusinessObjectService().findMatching(OleCalendar.class, oleCalendarMap);
            if (oleCalendarList.size() > 0) {
                oleCalendar = oleCalendarList.get(0);
            }
            if (oleCalendar.getCalendarId() != null) {
                DateUtil dateUtil = new DateUtil();
                // oleCalendar = (OleCalendar) document.getDocumentDataObject();//changed for edit purpose
                List<OleCalendarWeek> oleCalendarWeekList = new ArrayList<OleCalendarWeek>();
                for (OleCalendarWeek oldCalendarWeek : oleCalendar.getOleCalendarWeekList()) {

                    if (oldCalendarWeek.getOpenTime().equals("") || oldCalendarWeek.getCloseTime().equals("")) {
                        break;
                    }
                    String convertedOpenTime = "";
                    String convertCloseTime = "";
                    String oleOpenTime = oldCalendarWeek.getOpenTime();
                    String oleCloseTime = oldCalendarWeek.getCloseTime();
                    try {
                        convertedOpenTime = dateUtil.convertTo12HoursFormat(oleOpenTime);
                        convertCloseTime = dateUtil.convertTo12HoursFormat(oleCloseTime);
                    } catch (java.text.ParseException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                    if (convertedOpenTime != null) {
                        oldCalendarWeek.setOpenTime(convertedOpenTime.substring(0, convertedOpenTime.length() - 2));
                    }
                    if (convertCloseTime != null) {
                        oldCalendarWeek.setCloseTime(convertCloseTime.substring(0, convertCloseTime.length() - 2));
                    }
                    oleCalendarWeekList.add(oldCalendarWeek);
                }
                oleCalendar.setOleCalendarWeekList(oleCalendarWeekList);


                for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendar.getOleCalendarExceptionPeriodList()) {
                    for (OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek : oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList()) {
                        String convOpenTimeExcptPrdWk = "";
                        String convCloseTimeExcptPrdWk = "";
                        String oleOpenTimeExcptPrdWk = oleCalendarExceptionPeriodWeek.getOpenTime();
                        String oleCloseTimeExcptPrdWk = oleCalendarExceptionPeriodWeek.getCloseTime();
                        try {
                            convOpenTimeExcptPrdWk = dateUtil.convertTo12HoursFormat(oleOpenTimeExcptPrdWk);
                            convCloseTimeExcptPrdWk = dateUtil.convertTo12HoursFormat(oleCloseTimeExcptPrdWk);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                        if (convOpenTimeExcptPrdWk != null) {
                            oleCalendarExceptionPeriodWeek.setOpenTime(convOpenTimeExcptPrdWk.substring(0, convOpenTimeExcptPrdWk.length() - 2));
                        }
                        if (convCloseTimeExcptPrdWk != null) {
                            oleCalendarExceptionPeriodWeek.setCloseTime(convCloseTimeExcptPrdWk.substring(0, convCloseTimeExcptPrdWk.length() - 2));
                        }
                    }
                }


                for (OleCalendarExceptionDate oleCalendarExceptionDate : oleCalendar.getOleCalendarExceptionDateList()) {
                    String convertedOpenTimeExcptDate = "";
                    String convertedCloseTimeExcptDate = "";
                    String oleOpenTimeExcptDate = oleCalendarExceptionDate.getOpenTime();
                    String oleCloseTimeExcptDate = oleCalendarExceptionDate.getCloseTime();
                    if ((!oleCalendarExceptionDate.getOpenTime().equals("")) && (!oleCalendarExceptionDate.getCloseTime().equals(""))) { //changed
                        try {
                            convertedOpenTimeExcptDate = dateUtil.convertTo12HoursFormat(oleOpenTimeExcptDate);
                            convertedCloseTimeExcptDate = dateUtil.convertTo12HoursFormat(oleCloseTimeExcptDate);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }

                        if (convertedOpenTimeExcptDate != null) {
                            oleCalendarExceptionDate.setOpenTime(convertedOpenTimeExcptDate.substring(0, convertedOpenTimeExcptDate.length() - 2));
                        }
                        if (convertedCloseTimeExcptDate != null) {
                            oleCalendarExceptionDate.setCloseTime(convertedCloseTimeExcptDate.substring(0, convertedCloseTimeExcptDate.length() - 2));
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception", e);
        }

    }


}
