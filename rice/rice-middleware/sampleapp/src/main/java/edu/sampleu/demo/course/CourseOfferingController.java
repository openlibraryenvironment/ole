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

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.kuali.rice.core.api.util.type.KualiDecimal;
import org.kuali.rice.krad.web.controller.UifControllerBase;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Controller
@RequestMapping(value = "/courseOffering")
public class CourseOfferingController extends UifControllerBase {

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected CourseOfferingForm createInitialForm(HttpServletRequest request) {
        return new CourseOfferingForm();
    }

    /**
     * Populate some data for demonstration
     */
    @Override
    @RequestMapping(params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        Course course = new Course();
        course.setSubjectId("CTWR");
        course.setNumber("437");
        course.setTitle("Writing the Situation Comedy Pilot");
        course.setActivityType("Lecture");
        course.setMinCredits(2);
        course.setMaxCredits(4);
        course.setGradingOptions("A-F");
        course.setTranscriptTitle("Filmwriting");
        course.setFee(new KualiDecimal(25));
        course.setOfferingStatus("Active");

        CourseSection section = new CourseSection();
        section.setSection("001");
        section.setRegistrationId("12345");

        CourseInstructor instructor = new CourseInstructor();
        instructor.setAffiliation("I");
        instructor.setName("Dr. Neal");
        section.getInstructors().add(instructor);

        CourseInstructor instructor2 = new CourseInstructor();
        instructor2.setAffiliation("I");
        instructor2.setName("Dr. Smith");
        section.getInstructors().add(instructor2);

        section.setTerm("Su");

        Calendar calendar = new GregorianCalendar(2009, 10, 14);
        section.setStartDate(calendar.getTime());

        Calendar endCalendar = new GregorianCalendar(2010, 5, 14);
        section.setEndDate(endCalendar.getTime());

        section.setStandardMeetingSchedule("A");
        section.setStandardMeetingTime("A");
        section.setAttendanceType("PA");

        section.setBuilding("Adams");
        section.setRoom("100");

        section.setCourse(course);
        course.getSections().add(section);

        ((CourseOfferingForm) form).setCourseSection(section);

        return super.start(form, result, request, response);
    }
}
