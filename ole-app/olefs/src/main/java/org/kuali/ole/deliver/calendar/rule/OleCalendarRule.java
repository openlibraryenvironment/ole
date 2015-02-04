package org.kuali.ole.deliver.calendar.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.calendar.bo.*;
import org.kuali.rice.kim.api.identity.IdentityService;
import org.kuali.rice.kim.service.UiDocumentService;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;
import org.kuali.rice.krad.util.GlobalVariables;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dileepp
 * Date: 8/26/13
 * Time: 4:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendarRule extends MaintenanceDocumentRuleBase {
    protected UiDocumentService uiDocumentService;
    protected IdentityService identityService;

    /**
     * This method validates the patron object and returns boolean value
     *
     * @param document
     * @return isValid
     */

    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        LOG.debug(" Inside processCustomSaveDocumentBusinessRule");
        boolean isValid = true;
        OleCalendar oleCalendar = (OleCalendar) document.getNewMaintainableObject().getDataObject();
        isValid &= validateBeginAndEndDate(oleCalendar);
        isValid &= validateGeneralWeekDaysFromAndTo(oleCalendar.getOleCalendarWeekList());
        isValid &= validateGeneralOpenAndCloseTime(oleCalendar.getOleCalendarWeekList());
        isValid &= validateGeneralWeekDay(oleCalendar.getOleCalendarWeekList());
        isValid &= validateExceptionDaysOpenAndCloseTime(oleCalendar.getOleCalendarExceptionDateList());
        isValid &= validateExceptionDay(oleCalendar, isValid);
        isValid &= validateExceptionDays(oleCalendar.getOleCalendarExceptionDateList());
        isValid &= validateExceptionPeriodListOpenTime(oleCalendar.getOleCalendarExceptionPeriodList());
        isValid &= validateGeneralPeriodDaysFromAndTo(oleCalendar);
        isValid &= validateExceptionPeriodDay(oleCalendar, isValid);
        isValid &= validateExceptionPeriodListDay(oleCalendar.getOleCalendarExceptionPeriodList(), isValid);
       // isValid &= validateExceptionPeriodBeginEndDate(oleCalendar, isValid);


        return isValid;
    }
    private boolean validateBeginAndEndDate(OleCalendar oleCalendar){
        if(oleCalendar.getBeginDate()!=null && oleCalendar.getEndDate()!=null){
            long begin=oleCalendar.getBeginDate().getTime();
            long end =oleCalendar.getEndDate().getTime();
            if(end<begin){
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_GENERAL, OLEConstants.CALENDAR_BEGIN_END_DATE);
                return false;
            }
        }
        return true;
    }
    private boolean validateGeneralWeekDay(List<OleCalendarWeek> oleCalendarWeekList) {
        List<Integer> weekDays = new ArrayList<Integer>();
        for (int i = 0; i < oleCalendarWeekList.size(); i++) {
            if(new Integer(oleCalendarWeekList.get(i).getStartDay()) <= new Integer(oleCalendarWeekList.get(i).getEndDay())){
                for (int x = new Integer(oleCalendarWeekList.get(i).getStartDay()); x <= new Integer(oleCalendarWeekList.get(i).getEndDay()); x++) {
                    if (weekDays.size() > 0) {
                        if (weekDays.contains(x)) {
                            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_GENERAL, OLEConstants.CALENDAR_WEEKDAYS_OVERLAP);
                            return false;
                        } else {
                            weekDays.add(x);
                        }
                    } else {
                        weekDays.add(x);
                    }
                }
            }
            else{
                int end=6;
                int strtDay=new Integer(oleCalendarWeekList.get(i).getStartDay());
                for(int strt=strtDay;strt<=end;strt++ ){
                    if (weekDays.size() > 0) {
                        if (weekDays.contains(strt)) {
                            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_GENERAL, OLEConstants.CALENDAR_WEEKDAYS_OVERLAP);
                            return false;
                        } else {
                            weekDays.add(strt);
                        }
                    } else {
                        weekDays.add(strt);
                    }
                    if(end==strt){
                        for(strt=0;strt<=new Integer(oleCalendarWeekList.get(i).getEndDay());strt++ ){
                            if (weekDays.size() > 0) {
                                if (weekDays.contains(strt)) {
                                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_GENERAL, OLEConstants.CALENDAR_WEEKDAYS_OVERLAP);
                                    return false;
                                } else {
                                    weekDays.add(strt);
                                }
                            } else {
                                weekDays.add(strt);
                            }
                        }
                    }

                }
        }

        }
        return true;
    }
    private boolean validateGeneralWeekDaysFromAndTo(List<OleCalendarWeek> oleCalendarWeekList) {
        boolean isValid = true;
        for(OleCalendarWeek oleCalendarWeek:oleCalendarWeekList){
            if(oleCalendarWeek.getStartDay().equals("9") || oleCalendarWeek.getEndDay().equals("9")){
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_GENERAL, OLEConstants.CALENDAR_GENERAL_ECH_DAY_WEEK);
                isValid &= false;
            }
        }
        return isValid;
    }
    private boolean validateGeneralOpenAndCloseTime(List<OleCalendarWeek> oleCalendarWeekList) {
        boolean isValid = true;
        for (OleCalendarWeek oleCalendarWeek : oleCalendarWeekList) {
            if (oleCalendarWeek.getOpenTime() == null || oleCalendarWeek.getOpenTime().equals("")) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_GENERAL, OLEConstants.CALENDAR_OPEN_TIME);
                isValid &= false;
            }
            if (oleCalendarWeek.getCloseTime() == null || oleCalendarWeek.getCloseTime().equals("")) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_GENERAL, OLEConstants.CALENDAR_CLOSE_TIME);
                isValid &= false;
            }
           /* if (oleCalendarWeek.getStartDay().equals(oleCalendarWeek.getEndDay())) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_GENERAL, OLEConstants.CALENDAR_WEEK_DAYS_SAME);
                isValid &= false;
            }*/
            if (isValid && oleCalendarWeek.isEachDayWeek()) {
                float closeTime = Float.parseFloat(oleCalendarWeek.getCloseTime().split(":")[0] + "." + oleCalendarWeek.getCloseTime().split(":")[1]);
                float openTime = Float.parseFloat(oleCalendarWeek.getOpenTime().split(":")[0] + "." + oleCalendarWeek.getOpenTime().split(":")[1]);
                if (openTime >= closeTime) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_GENERAL, OLEConstants.CALENDAR_OPEN_CLOSE_TIME_CHECK);
                    isValid &= false;
                }
            }

        }
        if (oleCalendarWeekList.size() == 0) {
            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_GENERAL, OLEConstants.CALENDAR_GENERAL_LIST);
            isValid &= false;
        }
        return isValid;
    }

    private boolean validateExceptionDay(OleCalendar oleCalendar, boolean isValid) {
        if (isValid) {
            List<OleCalendarExceptionDate> oleCalendarExceptionDates = oleCalendar.getOleCalendarExceptionDateList();
            long beginDay = oleCalendar.getBeginDate().getTime();
            for (OleCalendarExceptionDate oleCalendarExceptionDate : oleCalendarExceptionDates) {
                long exceptionDay = Timestamp.valueOf(oleCalendarExceptionDate.getDate().toString() + " " + "00:00:00.01").getTime();
                if (!(exceptionDay > beginDay)) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_EXCEPTION, OLEConstants.CALENDAR_EXCEPTION_VALIDATE);
                    return false;
                }
            }
        }
        return true;
    }

    private boolean validateExceptionDays(List<OleCalendarExceptionDate> oleCalendarExceptionDates) {

        List<Date> days = new ArrayList<>();
        for (OleCalendarExceptionDate oleCalendarExceptionDate : oleCalendarExceptionDates) {
            if (days.contains(oleCalendarExceptionDate.getDate())) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_EXCEPTION, OLEConstants.CALENDAR_EXCEPTION_DUPLICATE);
                return false;
            } else {
                days.add(oleCalendarExceptionDate.getDate());
            }
        }
        return true;
    }

    private boolean validateExceptionDaysOpenAndCloseTime(List<OleCalendarExceptionDate> oleCalendarExceptionDates) {
        boolean isValid = true;
        for (OleCalendarExceptionDate oleCalendarExceptionDate : oleCalendarExceptionDates) {
            if (oleCalendarExceptionDate.getDate() == null || oleCalendarExceptionDate.getDate().equals("")) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_EXCEPTION, OLEConstants.CALENDAR_EXCEPTION_DATE_EMPTY);
                isValid &= false;
            }
            if (oleCalendarExceptionDate.getExceptionType().equals("") || oleCalendarExceptionDate.getExceptionType().equalsIgnoreCase("Select")) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_EXCEPTION, OLEConstants.CALENDAR_EXCEPTION_TYPE_EMPTY);
                isValid &= false;
            }
            if (oleCalendarExceptionDate.getExceptionType().equalsIgnoreCase(OLEConstants.CALENDAR_EXCEPTION_TYPE) && isValid) {
                if (!oleCalendarExceptionDate.getOpenTime().equals("")) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_EXCEPTION, OLEConstants.CALENDAR_EXCEPTION_OPEN_TIME_EMPTY);
                    isValid &= false;
                }
                if (!oleCalendarExceptionDate.getCloseTime().equals("")) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_EXCEPTION, OLEConstants.CALENDAR_EXCEPTION_CLOSE_TIME_EMPTY);
                    isValid &= false;
                }
            } else if (isValid) {
                if (oleCalendarExceptionDate.getOpenTime().equals("")) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_EXCEPTION, OLEConstants.CALENDAR_EXCEPTION_OPEN_TIME);
                    isValid &= false;
                }
                if (oleCalendarExceptionDate.getCloseTime().equals("")) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_EXCEPTION, OLEConstants.CALENDAR_EXCEPTION_CLOSE_TIME);
                    isValid &= false;
                }
                if (isValid) {
                    float closeTime = Float.parseFloat(oleCalendarExceptionDate.getCloseTime().split(":")[0] + "." + oleCalendarExceptionDate.getCloseTime().split(":")[1]);
                    float openTime = Float.parseFloat(oleCalendarExceptionDate.getOpenTime().split(":")[0] + "." + oleCalendarExceptionDate.getOpenTime().split(":")[1]);
                    if (openTime >= closeTime) {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_EXCEPTION, OLEConstants.CALENDAR_OPEN_CLOSE_TIME_CHECK);
                        isValid &= false;
                    }
                }
            }

        }
        return isValid;
    }

    private boolean validateExceptionPeriodBeginEndDate(OleCalendar oleCalendar, boolean valid) {
        boolean isValid = true;
        long beginDay = oleCalendar.getBeginDate().getTime();
        if (valid)
            for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendar.getOleCalendarExceptionPeriodList()) {
                long exceptionPeriodBeginDay = oleCalendarExceptionPeriod.getBeginDate().getTime();
                long exceptionPeriodEndDay = oleCalendarExceptionPeriod.getEndDate().getTime();
                if (exceptionPeriodBeginDay < beginDay) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_PERIOD_BEGIN);
                    isValid &= false;
                }
                if (exceptionPeriodEndDay < beginDay) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_PERIOD_END);
                    isValid &= false;
                }
                if (exceptionPeriodEndDay < exceptionPeriodBeginDay && isValid) {
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_PERIOD_BEGIN_END);
                    isValid &= false;
                }

            }
        return isValid;
    }

    private boolean validateExceptionPeriodListDay(List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList, boolean isValid) {
        if (isValid) {
            for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendarExceptionPeriodList) {
                List<Integer> weekDays = new ArrayList<Integer>();
                for (int i = 0; i < oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList().size(); i++) {
                    if(new Integer(oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList().get(i).getStartDay()) <= new Integer(oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList().get(i).getEndDay())){
                        for (int x = new Integer(oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList().get(i).getStartDay()); x <= new Integer(oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList().get(i).getEndDay()); x++) {
                            if (weekDays.size() > 0) {
                                if (weekDays.contains(x)) {
                                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_WEEKDAYS_OVERLAP);
                                    return false;
                                } else {
                                    weekDays.add(x);
                                }
                            } else {
                                weekDays.add(x);
                            }
                        }
                    }
                    else{
                        int end=6;
                        int strtDay=new Integer(oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList().get(i).getStartDay());
                        for(int strt=strtDay;strt<=end;strt++ ){
                            if (weekDays.size() > 0) {
                                if (weekDays.contains(strt)) {
                                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_WEEKDAYS_OVERLAP);
                                    return false;
                                } else {
                                    weekDays.add(strt);
                                }
                            } else {
                                weekDays.add(strt);
                            }
                            if(end==strt){
                                for(strt=0;strt<=new Integer(oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList().get(i).getEndDay());strt++ ){
                                    if (weekDays.size() > 0) {
                                        if (weekDays.contains(strt)) {
                                            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_WEEKDAYS_OVERLAP);
                                            return false;
                                        } else {
                                            weekDays.add(strt);
                                        }
                                    } else {
                                        weekDays.add(strt);
                                    }
                                }
                            }

                        }
                    }

                }
            }
        }
        return true;
    }
    private boolean validateGeneralPeriodDaysFromAndTo(OleCalendar oleCalendar) {
        boolean isValid = true;
        for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendar.getOleCalendarExceptionPeriodList()) {
            for(OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek:oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList()){
                if(oleCalendarExceptionPeriodWeek.getStartDay().equals("9") || oleCalendarExceptionPeriodWeek.getEndDay().equals("9")){
                    GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_GENERAL_ECH_DAY_WEEK);
                    isValid &= false;
                }
            }
        }
        return isValid;
    }
    private boolean validateExceptionPeriodListOpenTime(List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList) {
        boolean isValid = true;
        for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendarExceptionPeriodList) {
            if (oleCalendarExceptionPeriod.getBeginDate() == null || oleCalendarExceptionPeriod.getBeginDate().equals("")) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_PERIOD_BEGIN_DATE_EMPTY);
                isValid &= false;
            }
            if (oleCalendarExceptionPeriod.getEndDate() == null || oleCalendarExceptionPeriod.getEndDate().equals("")) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_PERIOD_END_DATE_EMPTY);
                isValid &= false;
            }
            if(oleCalendarExceptionPeriod.getExceptionPeriodType().equals("") ||  oleCalendarExceptionPeriod.getExceptionPeriodType()==null){
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_PERIOD_EXP_PRD_TYP);
                isValid &= false;
            }
            if (isValid)
                for (OleCalendarExceptionPeriodWeek oleCalendarExceptionPeriodWeek : oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList()) {
                    if (oleCalendarExceptionPeriodWeek.getOpenTime() == null || oleCalendarExceptionPeriodWeek.getOpenTime().equals("")) {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_OPEN_TIME);
                        isValid &= false;
                    }
                    if (oleCalendarExceptionPeriodWeek.getCloseTime() == null || oleCalendarExceptionPeriodWeek.getCloseTime().equals("")) {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_CLOSE_TIME);
                        isValid &= false;
                    }
                 /*   if (oleCalendarExceptionPeriodWeek.getStartDay().equals(oleCalendarExceptionPeriodWeek.getEndDay())) {
                        GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_WEEK_DAYS_SAME);
                        isValid &= false;
                    }*/
                    if (isValid && oleCalendarExceptionPeriodWeek.isEachDayOfExceptionWeek()) {
                        float closeTime = Float.parseFloat(oleCalendarExceptionPeriodWeek.getCloseTime().split(":")[0] + "." + oleCalendarExceptionPeriodWeek.getCloseTime().split(":")[1]);
                        float openTime = Float.parseFloat(oleCalendarExceptionPeriodWeek.getOpenTime().split(":")[0] + "." + oleCalendarExceptionPeriodWeek.getOpenTime().split(":")[1]);
                        if (openTime >= closeTime) {
                            GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_OPEN_CLOSE_TIME_CHECK);
                            isValid &= false;
                        }
                    }

                }
            if (!oleCalendarExceptionPeriod.getExceptionPeriodType().equals("Holiday") && !oleCalendarExceptionPeriod.getExceptionPeriodType().equals("") && oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList().size() == 0 ) {
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_PERIOD_LIST_EMPTY);
                isValid &= false;
            }
        }
        return isValid;
    }
    private boolean validateExceptionPeriodDay(OleCalendar oleCalendar,Boolean valid) {
        boolean isSuccess=false;
        if(valid)
        for (OleCalendarExceptionPeriod oleCalendarExceptionPeriod : oleCalendar.getOleCalendarExceptionPeriodList()) {
            Timestamp beginDateTimestamp=oleCalendarExceptionPeriod.getBeginDate();
            long beginDate=beginDateTimestamp.getTime();
            Timestamp endDateTimestamp=oleCalendarExceptionPeriod.getEndDate();
            long  endDate=endDateTimestamp.getTime();
            if(oleCalendarExceptionPeriod.getCalendarExceptionPeriodId()!=null)
            for (OleCalendarExceptionPeriod calendarExceptionPeriod : oleCalendar.getOleCalendarExceptionPeriodList()) {
                Timestamp calendarBeginDateTimestamp=calendarExceptionPeriod.getBeginDate();
                long calendarBeginDate=calendarBeginDateTimestamp.getTime();
                Timestamp calendarEndDateTimestamp=calendarExceptionPeriod.getEndDate();
                long  calendarEndDate=endDateTimestamp.getTime();
                if(calendarExceptionPeriod.getCalendarExceptionPeriodId()!=null && !oleCalendarExceptionPeriod.getCalendarExceptionPeriodId().equals(calendarExceptionPeriod.getCalendarExceptionPeriodId())){
                    if(beginDate>=calendarBeginDate &&  beginDate <= calendarBeginDate ){
                        isSuccess=true;
                    }
                    if(endDate >= calendarEndDate  &&  endDate <= calendarEndDate ){
                        isSuccess=true;
                    }
                }
            }
            if(isSuccess){
                GlobalVariables.getMessageMap().putErrorForSectionId(OLEConstants.CALENDAR_PERIOD, OLEConstants.CALENDAR_PERIOD_RANGE_OVERLAP_ERROR);
                valid &= false;
            }
        }
        return valid;
    }

}