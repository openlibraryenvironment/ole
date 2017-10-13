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
package edu.sampleu.demo.kitchensink;

import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrainingApplicationForm extends UifFormBase {
    private static final long serialVersionUID = -7525378097732916419L;

    private String term;
    private String college;
    private String campus;

    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private Date dob;
    private boolean emailList;
    private List<String> ethnicity = new ArrayList<String>();
    private String otherEthnicity;

    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;

    private List<TrainingApplicationReference> references = new ArrayList<TrainingApplicationReference>();

    private List<TrainingApplicationPreviousEducation> previousEducation = new ArrayList<TrainingApplicationPreviousEducation>();

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public boolean isEmailList() {
        return emailList;
    }

    public List<String> getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(List<String> ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getOtherEthnicity() {
        return otherEthnicity;
    }

    public void setOtherEthnicity(String otherEthnicity) {
        this.otherEthnicity = otherEthnicity;
    }

    public void setEmailList(boolean emailList) {
        this.emailList = emailList;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public List<TrainingApplicationReference> getReferences() {
        return references;
    }

    public void setReferences(List<TrainingApplicationReference> references) {
        this.references = references;
    }

    public List<TrainingApplicationPreviousEducation> getPreviousEducation() {
        return previousEducation;
    }

    public void setPreviousEducation(List<TrainingApplicationPreviousEducation> previousEducation) {
        this.previousEducation = previousEducation;
    }
}
