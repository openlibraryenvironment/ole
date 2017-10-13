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

import edu.sampleu.demo.course.Course;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RegistrationForm extends UifFormBase {
    private static final long serialVersionUID = 5758492647092249491L;

    private String registrationTerm;
    private Integer registrationYear;

    private Map<String, String> criteriaFields;
    private List<Course> courseListing;

    public RegistrationForm() {
        super();

        criteriaFields = new HashMap<String, String>();
        courseListing = new ArrayList<Course>();
    }

    public String getRegistrationTerm() {
        return registrationTerm;
    }

    public void setRegistrationTerm(String registrationTerm) {
        this.registrationTerm = registrationTerm;
    }

    public Integer getRegistrationYear() {
        return registrationYear;
    }

    public void setRegistrationYear(Integer registrationYear) {
        this.registrationYear = registrationYear;
    }

    public Map<String, String> getCriteriaFields() {
        return criteriaFields;
    }

    public void setCriteriaFields(Map<String, String> criteriaFields) {
        this.criteriaFields = criteriaFields;
    }

    public List<Course> getCourseListing() {
        return courseListing;
    }

    public void setCourseListing(List<Course> courseListing) {
        this.courseListing = courseListing;
    }
}
