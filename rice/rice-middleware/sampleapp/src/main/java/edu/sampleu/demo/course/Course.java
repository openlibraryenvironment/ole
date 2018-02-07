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

import org.kuali.rice.core.api.util.type.KualiDecimal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class Course implements Serializable {
    private static final long serialVersionUID = 4700818811272201371L;

    private String subjectId;
    private String prefix;
    private String number;
    private String suffix;
    private String title;
    private String instanceId;
    private String activityType;
    private Integer minCredits;
    private Integer maxCredits;
    private String gradingOptions;
    private String transcriptTitle;
    private KualiDecimal fee;
    private String offeringStatus;

    private List<CourseSection> sections;

    public Course() {
        sections = new ArrayList<CourseSection>();
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public Integer getMinCredits() {
        return minCredits;
    }

    public void setMinCredits(Integer minCredits) {
        this.minCredits = minCredits;
    }

    public Integer getMaxCredits() {
        return maxCredits;
    }

    public void setMaxCredits(Integer maxCredits) {
        this.maxCredits = maxCredits;
    }

    public String getGradingOptions() {
        return gradingOptions;
    }

    public void setGradingOptions(String gradingOptions) {
        this.gradingOptions = gradingOptions;
    }

    public String getTranscriptTitle() {
        return transcriptTitle;
    }

    public void setTranscriptTitle(String transcriptTitle) {
        this.transcriptTitle = transcriptTitle;
    }

    public KualiDecimal getFee() {
        return fee;
    }

    public void setFee(KualiDecimal fee) {
        this.fee = fee;
    }

    public String getOfferingStatus() {
        return offeringStatus;
    }

    public void setOfferingStatus(String offeringStatus) {
        this.offeringStatus = offeringStatus;
    }

    public List<CourseSection> getSections() {
        return sections;
    }

    public void setSections(List<CourseSection> sections) {
        this.sections = sections;
    }
}
