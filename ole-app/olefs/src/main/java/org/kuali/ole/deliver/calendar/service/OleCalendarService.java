package org.kuali.ole.deliver.calendar.service;

import org.kuali.ole.deliver.calendar.bo.OleCalendar;
import org.kuali.ole.deliver.calendar.bo.OleCalendarGroup;

import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: arjuns
 * Date: 7/27/13
 * Time: 5:40 PM
 * To change this template use File | Settings | File Templates.
 */
public interface OleCalendarService {
    public OleCalendarGroup getCalendarGroup(String deskId);

    public OleCalendar getActiveCalendar(Timestamp date, String groupId);

    public Timestamp calculateDueDate(String deskId, String timePeriod, Timestamp currentDate);

    public Timestamp calculateDueDateHrs(String deskId, String timePeriod, Timestamp currentDate);

    public Float calculateFine(String deskId, Timestamp dueDate, Timestamp currentDate, String fineAmount);

    public void generalInfoValidation(OleCalendar oleCalendar, boolean isNew);

    public void assignEndDate(OleCalendar oleCalendar);

    public void convert12HrsFormat(OleCalendar oldCalendar);

    public void validateCalendarDocument(OleCalendar oleCalendar);

    public void deleteCalendarDocument(OleCalendar oleCalendar);
}
