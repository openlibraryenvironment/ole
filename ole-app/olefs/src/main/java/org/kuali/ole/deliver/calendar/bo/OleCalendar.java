package org.kuali.ole.deliver.calendar.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: palanivel
 * Date: 7/20/13
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleCalendar extends PersistableBusinessObjectBase {

    private String calendarId;
    private String calendarDescription;
    private Timestamp beginDate;
    private Timestamp endDate;
    private List<OleCalendarWeek> oleCalendarWeekList = new ArrayList<>();
    private List<OleCalendarExceptionDate> oleCalendarExceptionDateList = new ArrayList<>();
    private List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList = new ArrayList<>();
    private List<OleCalendarWeek> oleCalendarWeekDeleteList = new ArrayList<>();
    private List<OleCalendarExceptionDate> oleCalendarExceptionDateDeleteList = new ArrayList<>();
    private List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodDeleteList = new ArrayList<>();
    private List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodDeleteWeekList = new ArrayList<>();
    private OleCalendarGroup oleCalendarGroup;
    private String calendarGroupId;
    private boolean hideTime = true;

    private String endDateYesMessage;
    private String endDateNoMessage;
    private String message;
    private boolean endDateYesFlag;
    private boolean endDateNoFlag;
    private boolean cancelOperationEndDateFlag;
    private boolean cancelOperationFlag;

    public void sortCalendarWeek(OleCalendar oleCalendar){ //added for OLE-5381

      Collections.sort(oleCalendar.getOleCalendarWeekList(), new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                OleCalendarWeek oleCalendarWeek1 = (OleCalendarWeek) o1;
                OleCalendarWeek oleCalendarWeek2 = (OleCalendarWeek) o2;
                return oleCalendarWeek1.getStartDay().compareTo(oleCalendarWeek2.getStartDay());
            }
      });
      for(OleCalendarExceptionPeriod oleCalendarExceptionPeriod:oleCalendar.getOleCalendarExceptionPeriodList()){
            Collections.sort(oleCalendarExceptionPeriod.getOleCalendarExceptionPeriodWeekList(), new Comparator() {
                @Override
                public int compare(Object o1, Object o2) {
                    OleCalendarExceptionPeriodWeek oleCalendarPeriodWeek1 = (OleCalendarExceptionPeriodWeek) o1;
                    OleCalendarExceptionPeriodWeek oleCalendarPeriodWeek2 = (OleCalendarExceptionPeriodWeek) o2;
                    return oleCalendarPeriodWeek1.getStartDay().compareTo(oleCalendarPeriodWeek2.getStartDay());
                }
            });
      }
    }

    public List<OleCalendarWeek> getOleCalendarWeekDeleteList() {
        return oleCalendarWeekDeleteList;
    }

    public void setOleCalendarWeekDeleteList(List<OleCalendarWeek> oleCalendarWeekDeleteList) {
        this.oleCalendarWeekDeleteList = oleCalendarWeekDeleteList;
    }

    public List<OleCalendarExceptionDate> getOleCalendarExceptionDateDeleteList() {
        return oleCalendarExceptionDateDeleteList;
    }

    public void setOleCalendarExceptionDateDeleteList(List<OleCalendarExceptionDate> oleCalendarExceptionDateDeleteList) {
        this.oleCalendarExceptionDateDeleteList = oleCalendarExceptionDateDeleteList;
    }

    public List<OleCalendarExceptionPeriod> getOleCalendarExceptionPeriodDeleteList() {
        return oleCalendarExceptionPeriodDeleteList;
    }

    public void setOleCalendarExceptionPeriodDeleteList(List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodDeleteList) {
        this.oleCalendarExceptionPeriodDeleteList = oleCalendarExceptionPeriodDeleteList;
    }

    public List<OleCalendarExceptionPeriodWeek> getOleCalendarExceptionPeriodDeleteWeekList() {
        return oleCalendarExceptionPeriodDeleteWeekList;
    }

    public void setOleCalendarExceptionPeriodDeleteWeekList(List<OleCalendarExceptionPeriodWeek> oleCalendarExceptionPeriodDeleteWeekList) {
        this.oleCalendarExceptionPeriodDeleteWeekList = oleCalendarExceptionPeriodDeleteWeekList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEndDateYesMessage() {
        return endDateYesMessage;
    }

    public void setEndDateYesMessage(String endDateYesMessage) {
        this.endDateYesMessage = endDateYesMessage;
    }

    public String getEndDateNoMessage() {
        return endDateNoMessage;
    }

    public void setEndDateNoMessage(String endDateNoMessage) {
        this.endDateNoMessage = endDateNoMessage;
    }

    private String chronologicalSequence;

    public String getChronologicalSequence() {
        return chronologicalSequence;
    }

    public void setChronologicalSequence(String chronologicalSequence) {
        this.chronologicalSequence = chronologicalSequence;
    }

    private String exceptionDayChecking;  //added for holiday

    public String getExceptionDayChecking() {
        return exceptionDayChecking;
    }

    public void setExceptionDayChecking(String exceptionDayChecking) {
        this.exceptionDayChecking = exceptionDayChecking;
    }

    public String getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(String calendarId) {
        this.calendarId = calendarId;
    }

    public String getCalendarDescription() {
        return calendarDescription;
    }

    public void setCalendarDescription(String calendarDescription) {
        this.calendarDescription = calendarDescription;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public List<OleCalendarWeek> getOleCalendarWeekList() {
        return oleCalendarWeekList;
    }

    public void setOleCalendarWeekList(List<OleCalendarWeek> oleCalendarWeekList) {
        this.oleCalendarWeekList = oleCalendarWeekList;
    }

    public List<OleCalendarExceptionDate> getOleCalendarExceptionDateList() {
        return oleCalendarExceptionDateList;
    }

    public void setOleCalendarExceptionDateList(List<OleCalendarExceptionDate> oleCalendarExceptionDateList) {
        this.oleCalendarExceptionDateList = oleCalendarExceptionDateList;
    }

    public List<OleCalendarExceptionPeriod> getOleCalendarExceptionPeriodList() {
        return oleCalendarExceptionPeriodList;
    }

    public void setOleCalendarExceptionPeriodList(List<OleCalendarExceptionPeriod> oleCalendarExceptionPeriodList) {
        this.oleCalendarExceptionPeriodList = oleCalendarExceptionPeriodList;
    }

    public OleCalendarGroup getOleCalendarGroup() {
        return oleCalendarGroup;
    }

    public void setOleCalendarGroup(OleCalendarGroup oleCalendarGroup) {
        this.oleCalendarGroup = oleCalendarGroup;
    }

    public String getCalendarGroupId() {
        return calendarGroupId;
    }

    public void setCalendarGroupId(String calendarGroupId) {
        this.calendarGroupId = calendarGroupId;
    }

    public boolean isHideTime() {
        return hideTime;
    }

    public void setHideTime(boolean hideTime) {
        this.hideTime = hideTime;
    }

    public boolean isEndDateYesFlag() {
        return endDateYesFlag;
    }

    public void setEndDateYesFlag(boolean endDateYesFlag) {
        this.endDateYesFlag = endDateYesFlag;
    }

    public boolean isEndDateNoFlag() {
        return endDateNoFlag;
    }

    public void setEndDateNoFlag(boolean endDateNoFlag) {
        this.endDateNoFlag = endDateNoFlag;
    }

    public boolean isCancelOperationFlag() {
        return cancelOperationFlag;
    }

    public void setCancelOperationFlag(boolean cancelOperationFlag) {
        this.cancelOperationFlag = cancelOperationFlag;
    }

    public boolean isCancelOperationEndDateFlag() {
        return cancelOperationEndDateFlag;
    }

    public void setCancelOperationEndDateFlag(boolean cancelOperationEndDateFlag) {
        this.cancelOperationEndDateFlag = cancelOperationEndDateFlag;
    }
}
