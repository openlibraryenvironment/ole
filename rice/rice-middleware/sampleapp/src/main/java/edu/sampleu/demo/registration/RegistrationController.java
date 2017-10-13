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
package edu.sampleu.demo.registration;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.sampleu.demo.course.Course;
import edu.sampleu.demo.course.CourseSection;
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
@RequestMapping(value = "/registration")
public class RegistrationController extends UifControllerBase {

    /**
     * @see org.kuali.rice.krad.web.controller.UifControllerBase#createInitialForm(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected RegistrationForm createInitialForm(HttpServletRequest request) {
        return new RegistrationForm();
    }

    /**
     * Populate some data for demonstration
     */
    @Override
    @RequestMapping(method = RequestMethod.GET, params = "methodToCall=start")
    public ModelAndView start(@ModelAttribute("KualiForm") UifFormBase form, BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {

        RegistrationForm registrationForm = (RegistrationForm) form;
        registrationForm.setRegistrationTerm("Fall");
        registrationForm.setRegistrationYear(2011);

        List<Course> courseListing = new ArrayList<Course>();

        Course course = new Course();
        course.setSubjectId("CTWR");
        course.setNumber("106a");
        course.setTitle("Screenwriting Fundamentals");
        course.setMaxCredits(4);

        CourseSection section = new CourseSection();
        section.setSection("001");
        section.setRegistrationId("19177D");
        section.setStandardMeetingTime("10:00-12:50p | F");
        section.setRegisteredNumber(0);
        section.setWaitlistNumber(1);
        section.setTotalMaxEnrollment(14);
        section.setInstructor("Mardik Martin");
        section.setLocation("RZC119");
        course.getSections().add(section);

        CourseSection section2 = new CourseSection();
        section2.setSection("001");
        section2.setRegistrationId("19179D");
        section2.setStandardMeetingTime("4:00-6:50p | W");
        section2.setRegisteredNumber(0);
        section2.setWaitlistNumber(7);
        section2.setTotalMaxEnrollment(17);
        section2.setInstructor("Noreen Stone");
        section2.setLocation("RZC119");
        course.getSections().add(section2);
        courseListing.add(course);

        Course course2 = new Course();
        course2.setSubjectId("CTWR");
        course2.setNumber("206a");
        course2.setTitle("Writing the Screenplay");
        course2.setMaxCredits(4);
        course2.getSections().add(section);
        course2.getSections().add(section2);
        courseListing.add(course2);

        Course course3 = new Course();
        course3.setSubjectId("CTWR");
        course3.setNumber("305");
        course3.setTitle("Writing To Be Performed");
        course3.setMaxCredits(2);
        course3.getSections().add(section);
        course3.getSections().add(section2);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);
        courseListing.add(course3);

        registrationForm.setCourseListing(courseListing);

        return super.start(form, result, request, response);
    }
}
