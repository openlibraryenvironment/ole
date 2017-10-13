/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.sampleu.demo.course;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CourseSection implements Serializable {

    private String section;
    private String registrationId;
    private String term;
    private Date startDate;
    private Date endDate;
    private boolean customMeetingTime;
    private String standardMeetingSchedule;
    private String standardMeetingTime;
    private String attendanceType;

    private Integer weeklyInClassHours;
    private Integer weeklyOutOfClassHours;
    private Integer termInClassHours;
    private Integer termOutOfClassHours;

    private Integer totalMinEnrollment;
    private Integer totalMaxEnrollment;

    private String building;
    private String room;

    private boolean waitlist;
    private String waitlistType;
    private Integer waitlistMaximum;

    private boolean finalExam;
    private Date finalExamDate;
    private String finalExamStartTime;
    private String finalExamEndTime;
    private String finalExamBuilding;
    private String finalExamRoom;

    private boolean publish;
    private String publishTo;

    private String scheduleComments;
    private String courseUrl;
    private String internetClassProvider;

    private String location;
    private Integer registeredNumber;
    private Integer waitlistNumber;
    private String instructor;

    private Course course;

    private List<CourseInstructor> instructors;

    public CourseSection() {
        instructors = new ArrayList<CourseInstructor>();

        registeredNumber = 0;
        waitlistNumber = 0;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isCustomMeetingTime() {
        return customMeetingTime;
    }

    public void setCustomMeetingTime(boolean customMeetingTime) {
        this.customMeetingTime = customMeetingTime;
    }

    public String getStandardMeetingSchedule() {
        return standardMeetingSchedule;
    }

    public void setStandardMeetingSchedule(String standardMeetingSchedule) {
        this.standardMeetingSchedule = standardMeetingSchedule;
    }

    public String getStandardMeetingTime() {
        return standardMeetingTime;
    }

    public void setStandardMeetingTime(String standardMeetingTime) {
        this.standardMeetingTime = standardMeetingTime;
    }

    public String getAttendanceType() {
        return attendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        this.attendanceType = attendanceType;
    }

    public Integer getWeeklyInClassHours() {
        return weeklyInClassHours;
    }

    public void setWeeklyInClassHours(Integer weeklyInClassHours) {
        this.weeklyInClassHours = weeklyInClassHours;
    }

    public Integer getWeeklyOutOfClassHours() {
        return weeklyOutOfClassHours;
    }

    public void setWeeklyOutOfClassHours(Integer weeklyOutOfClassHours) {
        this.weeklyOutOfClassHours = weeklyOutOfClassHours;
    }

    public Integer getWeeklyTotalHours() {
        Integer total = 0;
        if (weeklyInClassHours != null) {
            total += weeklyInClassHours;
        }
        if (weeklyOutOfClassHours != null) {
            total += weeklyOutOfClassHours;
        }
        return total;
    }

    public Integer getTermInClassHours() {
        return termInClassHours;
    }

    public void setTermInClassHours(Integer termInClassHours) {
        this.termInClassHours = termInClassHours;
    }

    public Integer getTermOutOfClassHours() {
        return termOutOfClassHours;
    }

    public void setTermOutOfClassHours(Integer termOutOfClassHours) {
        this.termOutOfClassHours = termOutOfClassHours;
    }

    public Integer getTermTotalHours() {
        Integer total = 0;
        if (termInClassHours != null) {
            total += termInClassHours;
        }
        if (termOutOfClassHours != null) {
            total += termOutOfClassHours;
        }
        return total;
    }

    public Integer getTotalMinEnrollment() {
        return totalMinEnrollment;
    }

    public void setTotalMinEnrollment(Integer totalMinEnrollment) {
        this.totalMinEnrollment = totalMinEnrollment;
    }

    public Integer getTotalMaxEnrollment() {
        return totalMaxEnrollment;
    }

    public void setTotalMaxEnrollment(Integer totalMaxEnrollment) {
        this.totalMaxEnrollment = totalMaxEnrollment;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public boolean isWaitlist() {
        return waitlist;
    }

    public void setWaitlist(boolean waitlist) {
        this.waitlist = waitlist;
    }

    public String getWaitlistType() {
        return waitlistType;
    }

    public void setWaitlistType(String waitlistType) {
        this.waitlistType = waitlistType;
    }

    public Integer getWaitlistMaximum() {
        return waitlistMaximum;
    }

    public void setWaitlistMaximum(Integer waitlistMaximum) {
        this.waitlistMaximum = waitlistMaximum;
    }

    public boolean isFinalExam() {
        return finalExam;
    }

    public void setFinalExam(boolean finalExam) {
        this.finalExam = finalExam;
    }

    public Date getFinalExamDate() {
        return finalExamDate;
    }

    public void setFinalExamDate(Date finalExamDate) {
        this.finalExamDate = finalExamDate;
    }

    public String getFinalExamStartTime() {
        return finalExamStartTime;
    }

    public void setFinalExamStartTime(String finalExamStartTime) {
        this.finalExamStartTime = finalExamStartTime;
    }

    public String getFinalExamEndTime() {
        return finalExamEndTime;
    }

    public void setFinalExamEndTime(String finalExamEndTime) {
        this.finalExamEndTime = finalExamEndTime;
    }

    public String getFinalExamBuilding() {
        return finalExamBuilding;
    }

    public void setFinalExamBuilding(String finalExamBuilding) {
        this.finalExamBuilding = finalExamBuilding;
    }

    public String getFinalExamRoom() {
        return finalExamRoom;
    }

    public void setFinalExamRoom(String finalExamRoom) {
        this.finalExamRoom = finalExamRoom;
    }

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public String getPublishTo() {
        return publishTo;
    }

    public void setPublishTo(String publishTo) {
        this.publishTo = publishTo;
    }

    public String getScheduleComments() {
        return scheduleComments;
    }

    public void setScheduleComments(String scheduleComments) {
        this.scheduleComments = scheduleComments;
    }

    public String getCourseUrl() {
        return courseUrl;
    }

    public void setCourseUrl(String courseUrl) {
        this.courseUrl = courseUrl;
    }

    public String getInternetClassProvider() {
        return internetClassProvider;
    }

    public void setInternetClassProvider(String internetClassProvider) {
        this.internetClassProvider = internetClassProvider;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getRegisteredNumber() {
        return registeredNumber;
    }

    public void setRegisteredNumber(Integer registeredNumber) {
        this.registeredNumber = registeredNumber;
    }

    public Integer getWaitlistNumber() {
        return waitlistNumber;
    }

    public void setWaitlistNumber(Integer waitlistNumber) {
        this.waitlistNumber = waitlistNumber;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<CourseInstructor> getInstructors() {
        return instructors;
    }

    public void setInstructors(List<CourseInstructor> instructors) {
        this.instructors = instructors;
    }
}
