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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.kuali.rice.krms.impl.repository.mock;

import java.util.LinkedHashMap;
import java.util.Map;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.RuleManagementService;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
//import org.kuali.student.krms.naturallanguage.util.KsKrmsConstants;

/**
 *
 * @author nwright
 */
public class KrmsNaturalLanguageTemplateLoader {

    private RuleManagementService ruleManagementService = null;

    public RuleManagementService getRuleManagementService() {
        return ruleManagementService;
    }

    public void setRuleManagementService(RuleManagementService ruleManagementService) {
        this.ruleManagementService = ruleManagementService;
    }

    public void loadNaturalLanguageTemplate(String id,
            String languageCode,
            String naturalLanguageUsageId,
            String typeId,
            String template,
            String componentId,
            String componentBuilderClass) {
        NaturalLanguageTemplate.Builder bldr = NaturalLanguageTemplate.Builder.create(languageCode, naturalLanguageUsageId, template, typeId);
        bldr.setId(id);
        bldr.setActive(true);
//        Map<String, String> attrs = new LinkedHashMap<String, String>();
//        bldr.setAttributes(attrs);
//        if (componentId != null && !componentId.isEmpty()) {
//            attrs.put(KsKrmsConstants.ATTRIBUTE_COMPONENT_ID, componentId);
//        }
//        if (componentBuilderClass != null && !componentBuilderClass.isEmpty()) {
//            attrs.put(KsKrmsConstants.ATTRIBUTE_COMPONENT_BUILDER_CLASS, componentBuilderClass);
//        }
//        if (!attrs.isEmpty()) {
//            bldr.setAttributes(attrs);
//        }
        NaturalLanguageTemplate existing = this.findExisting(bldr);
        if (existing == null) {
            this.getRuleManagementService().createNaturalLanguageTemplate(bldr.build());
        } else {
            bldr.setVersionNumber(existing.getVersionNumber());
            this.getRuleManagementService().createNaturalLanguageTemplate(bldr.build());
        }
    }

    private NaturalLanguageTemplate findExisting(NaturalLanguageTemplate.Builder bldr) {
        if (bldr.getId() != null) {
            try {
                return this.getRuleManagementService().getNaturalLanguageTemplate(bldr.getId());
            } catch (RiceIllegalArgumentException ex) {
                return null;
            }
        }
        return this.getRuleManagementService().findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId(bldr.getLanguageCode(),
                bldr.getTypeId(),
                bldr.getNaturalLanguageUsageId());
    }

    public void load() {
        // descriptions
        loadNaturalLanguageTemplate("10000-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10000", "Course Requirements", "", "");
        loadNaturalLanguageTemplate("10001-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10001", "Program Requirements", "", "");
        loadNaturalLanguageTemplate("10078-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10078", "Course Offering Requirements", "", "");
        loadNaturalLanguageTemplate("10002-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10002", "Enrollment Eligibility", "", "");
        loadNaturalLanguageTemplate("10003-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10003", "Credit Constraints", "", "");
        loadNaturalLanguageTemplate("10004-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10004", "Schedule Eligibility", "", "");
        loadNaturalLanguageTemplate("10005-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10005", "Anti-requisite", "", "");
        loadNaturalLanguageTemplate("10006-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10006", "Co-requisite", "", "");
        loadNaturalLanguageTemplate("10008-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10008", "Recommended Preparation", "", "");
        loadNaturalLanguageTemplate("10009-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10009", "Student Eligiblity", "", "");
        loadNaturalLanguageTemplate("10010-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10010", "Student Eligibility and Prerequisites", "", "");
        loadNaturalLanguageTemplate("10011-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10011", "Repeatable for Credit", "", "");
        loadNaturalLanguageTemplate("10012-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10012", "Restrictions on Credit", "", "");
        loadNaturalLanguageTemplate("10013-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10013", "Completion Requirements", "", "");
        loadNaturalLanguageTemplate("10014-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10014", "Entrance Requirements", "", "");
        loadNaturalLanguageTemplate("10015-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10015", "Satisfactory Progress", "", "");
        loadNaturalLanguageTemplate("10016-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10016", "Schedule Eligibility", "", "");

        loadNaturalLanguageTemplate("10021", "en", "KS-KRMS-NL-USAGE-1002", "10019", "Must have successfully completed a minimum of 2 courses from (MATH140,MATH111,STAT100)", "", "");
        loadNaturalLanguageTemplate("10100-10002-en", "en", "KS-KRMS-NL-USAGE-1002", "10100", "NumberOfCompletedCourses", "", "");
        loadNaturalLanguageTemplate("10101-10002-en", "en", "KS-KRMS-NL-USAGE-1002", "10101", "less than or equal to", "", "");
        loadNaturalLanguageTemplate("10102-10002-en", "en", "KS-KRMS-NL-USAGE-1002", "10102", "1", "", "");
        loadNaturalLanguageTemplate("10104-10002-en", "en", "KS-KRMS-NL-USAGE-1002", "10104", "A 32 digit UUID or sequence number", "", "");

        loadNaturalLanguageTemplate("10090", "en", "KS-KRMS-NL-USAGE-1004", "10019", "Must have successfully completed a minimum of <n> courses from <courses>", "", "");
        loadNaturalLanguageTemplate("10100-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10100", "A term that resolves into the number of courses that a student has completed from a specified set of courses", "", "");
        loadNaturalLanguageTemplate("10101-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10101", "Less than or equal to comparison operator", "", "");
        loadNaturalLanguageTemplate("10102-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10102", "A constant numeric value N", "", "");
        loadNaturalLanguageTemplate("10104-10004-en", "en", "KS-KRMS-NL-USAGE-1004", "10104", "The ID of a course set", "", "");

        loadNaturalLanguageTemplate("10375", "en", "KS-KRMS-NL-USAGE-1000", "KS-KRMS-NL-USAGE-1019", "#if($intValue == 1 && $courseCluSet.getCluList().size() == 1)Must have successfully completed $courseCluSet.getCluSetAsCode()#{else}Must have successfully completed a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"course\") from $courseCluSet.getCluSetAsCode()#end", "", "");
        loadNaturalLanguageTemplate("10100-10000-en", "en", "KS-KRMS-NL-USAGE-1000", "10100", "NumberOfCompletedCourses", "", "");
        loadNaturalLanguageTemplate("10101-10000-en", "en", "KS-KRMS-NL-USAGE-1000", "10101", "<=", "", "");
        loadNaturalLanguageTemplate("10102-10000-en", "en", "KS-KRMS-NL-USAGE-1000", "10102", "1", "KRMS-NumberOfCourses-ConstantValue", "");
        loadNaturalLanguageTemplate("10104-10000-en", "en", "KS-KRMS-NL-USAGE-1000", "10104", "CourseSetId", "KRMS-MultiCourse-Section", "org.kuali.student.enrollment.class1.krms.builder.MultiCourseComponentBuilder");
        loadNaturalLanguageTemplate("10336", "en", "KS-KRMS-NL-USAGE-1003", "10019", "#if($intValue == 1 && $courseCluSet.getCluList().size() == 1)Must have successfully completed#{else}Must have successfully completed a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"course\") from#end", "", "");








        loadNaturalLanguageTemplate("10000", "en", "KS-KRMS-NL-USAGE-1002", "10056", "Student cannot add Activity Offering with Draft of Cancelled", "", "");
        loadNaturalLanguageTemplate("10001", "en", "KS-KRMS-NL-USAGE-1002", "10057", "May not repeat MATH140", "", "");
        loadNaturalLanguageTemplate("10002", "en", "KS-KRMS-NL-USAGE-1002", "10058", "May not repeat any of (MATH140, MATH241)", "", "");
        loadNaturalLanguageTemplate("10003", "en", "KS-KRMS-NL-USAGE-1002", "10034", "Permission of instructor required", "", "");
        loadNaturalLanguageTemplate("10004", "en", "KS-KRMS-NL-USAGE-1002", "10031", "Free Form Text", "", "");
        loadNaturalLanguageTemplate("10006", "en", "KS-KRMS-NL-USAGE-1002", "10035", "Student cannot drop course without permission of Music Dept", "", "");
        loadNaturalLanguageTemplate("10007", "en", "KS-KRMS-NL-USAGE-1002", "10042", "If student is Athlete AND upon drop total credit hours would be less than 12 credit hours, prevent drop", "", "");
        loadNaturalLanguageTemplate("10008", "en", "KS-KRMS-NL-USAGE-1002", "10043", "If upon drop, total credit hours would be less than 12 credit hours, prevent course drop", "", "");
        loadNaturalLanguageTemplate("10009", "en", "KS-KRMS-NL-USAGE-1002", "10044", "Course has more than 10 minutes overlap with already enrolled course", "", "");
        loadNaturalLanguageTemplate("10010", "en", "KS-KRMS-NL-USAGE-1002", "10045", "Course has less than 5 minutes between start time or end time with already enrolled course", "", "");
        loadNaturalLanguageTemplate("10011", "en", "KS-KRMS-NL-USAGE-1002", "10046", "Student in Music Performance can enrol in a maximum of 3 courses for the term", "", "");
        loadNaturalLanguageTemplate("10012", "en", "KS-KRMS-NL-USAGE-1002", "10047", "Student in Music Performance can enrol in a maximum of 12 credits for the term", "", "");
        loadNaturalLanguageTemplate("10015", "en", "KS-KRMS-NL-USAGE-1002", "10059", "Student is in an existing seat pool for the course with an available seat", "", "");
        loadNaturalLanguageTemplate("10016", "en", "KS-KRMS-NL-USAGE-1002", "10017", "Must have successfully completed MATH140", "", "");
        loadNaturalLanguageTemplate("10017", "en", "KS-KRMS-NL-USAGE-1002", "10060", "Must have successfully completed MATH140 as of <term>", "", "");
        loadNaturalLanguageTemplate("10018", "en", "KS-KRMS-NL-USAGE-1002", "10061", "Must have successfully completed MATH140 prior to <term>", "", "");
        loadNaturalLanguageTemplate("10019", "en", "KS-KRMS-NL-USAGE-1002", "10062", "Must have successfully completed MATH140 between <term1> and <term2>", "", "");
        loadNaturalLanguageTemplate("10022", "en", "KS-KRMS-NL-USAGE-1002", "10018", "Must have successfully completed all courses from (MATH140,STAT100)", "", "");
        loadNaturalLanguageTemplate("10023", "en", "KS-KRMS-NL-USAGE-1002", "10054", "Must have successfully completed a minimum of 3 credits from MATH140", "", "");
        loadNaturalLanguageTemplate("10024", "en", "KS-KRMS-NL-USAGE-1002", "10055", "Must have successfully completed a minimum of 3 credits from courses in the Music Dept", "", "");
        loadNaturalLanguageTemplate("10025", "en", "KS-KRMS-NL-USAGE-1002", "10040", "Must have earned a minimum cumulative GPA of 3.12", "", "");
        loadNaturalLanguageTemplate("10026", "en", "KS-KRMS-NL-USAGE-1002", "10041", "Must have earned a minimum Cumulative GPA of 3.12 in Fall 2012", "", "");
        loadNaturalLanguageTemplate("10027", "en", "KS-KRMS-NL-USAGE-1002", "10025", "Must have earned a minimum GPA of 2.5  in (MATH140,MATH111)", "", "");
        loadNaturalLanguageTemplate("10028", "en", "KS-KRMS-NL-USAGE-1002", "10027", "Must have earned a minimum grade of Letter B in (MATH140,MATH111)", "", "");
        loadNaturalLanguageTemplate("10029", "en", "KS-KRMS-NL-USAGE-1002", "10028", "Must successfully complete a minimum of 2 courses  from (MATH140,MATH111,STAT100) with a minimum grade of Letter C", "", "");
        loadNaturalLanguageTemplate("10030", "en", "KS-KRMS-NL-USAGE-1002", "10026", "Must not have earned a grade of Letter D or higher in (MATH140,MATH111)", "", "");
        loadNaturalLanguageTemplate("10031", "en", "KS-KRMS-NL-USAGE-1002", "10038", "Must have achieved a minimum score of <score> on <tests>", "", "");
        loadNaturalLanguageTemplate("10032", "en", "KS-KRMS-NL-USAGE-1002", "10037", "Must have achieved a score no higher than <score> on <tests>", "", "");
        loadNaturalLanguageTemplate("10033", "en", "KS-KRMS-NL-USAGE-1002", "10032", "Must not have successfully completed MATH111", "", "");
        loadNaturalLanguageTemplate("10034", "en", "KS-KRMS-NL-USAGE-1002", "10020", "Must not have successfully completed any courses from MATH140", "", "");
        loadNaturalLanguageTemplate("10035", "en", "KS-KRMS-NL-USAGE-1002", "10022", "Must not have successfully completed any credits from (MATH111,MATH140)", "", "");
        loadNaturalLanguageTemplate("10036", "en", "KS-KRMS-NL-USAGE-1002", "10021", "Must successfully complete no more than 4 credits from (MATH111,MATH140)", "", "");
        loadNaturalLanguageTemplate("10037", "en", "KS-KRMS-NL-USAGE-1002", "10053", "Must have successfully completed no more than 2 courses from (MATH111,MATH140)", "", "");
        loadNaturalLanguageTemplate("10038", "en", "KS-KRMS-NL-USAGE-1002", "10030", "Must be concurrently enrolled in MATH140", "", "");
        loadNaturalLanguageTemplate("10039", "en", "KS-KRMS-NL-USAGE-1002", "10024", "Must be concurrently enrolled in a minimum of 2 courses from (MATH111,MATH140)", "", "");
        loadNaturalLanguageTemplate("10040", "en", "KS-KRMS-NL-USAGE-1002", "10023", "Must be concurrently enrolled in all courses from (MATH111,MATH140)", "", "");
        loadNaturalLanguageTemplate("10041", "en", "KS-KRMS-NL-USAGE-1002", "10039", "Must have earned a minimum of 6 total credits", "", "");
        loadNaturalLanguageTemplate("10042", "en", "KS-KRMS-NL-USAGE-1002", "10029", "May be repeater for a maximum of 8 credits", "", "");
        loadNaturalLanguageTemplate("10043", "en", "KS-KRMS-NL-USAGE-1002", "10050", "Students admitted to South Campus may take no more than 3 courses at North Campus in 1 Year", "", "");
        loadNaturalLanguageTemplate("10044", "en", "KS-KRMS-NL-USAGE-1002", "10051", "Students admitted to North Campus may take no more than 8 credits at South Campus in 1 Year", "", "");
        loadNaturalLanguageTemplate("10045", "en", "KS-KRMS-NL-USAGE-1002", "10052", "Must have been admitted to the Sociology Program", "", "");
        loadNaturalLanguageTemplate("10046", "en", "KS-KRMS-NL-USAGE-1002", "10033", "Must be admitted to any Program offered at the course campus location", "", "");
        loadNaturalLanguageTemplate("10047", "en", "KS-KRMS-NL-USAGE-1002", "10036", "Must not have been admitted to the Sociology Program", "", "");
        loadNaturalLanguageTemplate("10048", "en", "KS-KRMS-NL-USAGE-1002", "10064", "Must not have been admitted to the Sociology Program with a class standing of <Class Standing>", "", "");
        loadNaturalLanguageTemplate("10049", "en", "KS-KRMS-NL-USAGE-1002", "10065", "Must have been admitted to a Program offered by Music Dept", "", "");
        loadNaturalLanguageTemplate("10050", "en", "KS-KRMS-NL-USAGE-1002", "10066", "Student must be in a class standing of <class standing>", "", "");
        loadNaturalLanguageTemplate("10051", "en", "KS-KRMS-NL-USAGE-1002", "10067", "Student must be in a class standing of <class standing> or greater", "", "");
        loadNaturalLanguageTemplate("10052", "en", "KS-KRMS-NL-USAGE-1002", "10068", "Student must be in a class standing of <class standing> or less", "", "");
        loadNaturalLanguageTemplate("10053", "en", "KS-KRMS-NL-USAGE-1002", "10069", "Must not be in a class standing of <class standing>", "", "");
        loadNaturalLanguageTemplate("10055", "en", "KS-KRMS-NL-USAGE-1002", "10071", "Must be concurrently enrolled in (MATH111,MATH140)", "", "");
        loadNaturalLanguageTemplate("10056", "en", "KS-KRMS-NL-USAGE-1002", "10072", "May not repeat MATH111 if repeated 2 times", "", "");
        loadNaturalLanguageTemplate("10057", "en", "KS-KRMS-NL-USAGE-1002", "10074", "Must have achieved a score between <score> and <score> on <test>", "", "");
        loadNaturalLanguageTemplate("10058", "en", "KS-KRMS-NL-USAGE-1002", "10075", "Must have achieved a score of <score> on <test>", "", "");
        loadNaturalLanguageTemplate("10059", "en", "KS-KRMS-NL-USAGE-1004", "10002", "Enrollment Eligibility", "", "");
        loadNaturalLanguageTemplate("10060", "en", "KS-KRMS-NL-USAGE-1004", "10003", "Credit Constraints", "", "");
        loadNaturalLanguageTemplate("10061", "en", "KS-KRMS-NL-USAGE-1004", "10005", "Antirequisite", "", "");
        loadNaturalLanguageTemplate("10062", "en", "KS-KRMS-NL-USAGE-1004", "10006", "Corequisite", "", "");
        loadNaturalLanguageTemplate("10064", "en", "KS-KRMS-NL-USAGE-1004", "10009", "Student Eligibility", "", "");
        loadNaturalLanguageTemplate("10065", "en", "KS-KRMS-NL-USAGE-1004", "10008", "Recommended Preparation", "", "");
        loadNaturalLanguageTemplate("10066", "en", "KS-KRMS-NL-USAGE-1004", "10010", "Student Eligibility & Prerequisite", "", "");
        loadNaturalLanguageTemplate("10067", "en", "KS-KRMS-NL-USAGE-1004", "10011", "Repeatable for Credit", "", "");
        loadNaturalLanguageTemplate("10068", "en", "KS-KRMS-NL-USAGE-1004", "10012", "Restricted for Credit", "", "");
        loadNaturalLanguageTemplate("10069", "en", "KS-KRMS-NL-USAGE-1004", "10056", "Student cannot add Activity Offering with <Activity Offering State>  of <state>", "", "");
        loadNaturalLanguageTemplate("10070", "en", "KS-KRMS-NL-USAGE-1004", "10057", "May not repeat <course>", "", "");
        loadNaturalLanguageTemplate("10071", "en", "KS-KRMS-NL-USAGE-1004", "10058", "May not repeat any of  <courses>", "", "");
        loadNaturalLanguageTemplate("10072", "en", "KS-KRMS-NL-USAGE-1004", "10034", "Permission of instructor required", "", "");
        loadNaturalLanguageTemplate("10073", "en", "KS-KRMS-NL-USAGE-1004", "10031", "Free Form Text", "", "");
        loadNaturalLanguageTemplate("10075", "en", "KS-KRMS-NL-USAGE-1004", "10035", "Student cannot drop course without permission of <administering org>", "", "");
        loadNaturalLanguageTemplate("10076", "en", "KS-KRMS-NL-USAGE-1004", "10042", "If student is <attribute> AND upon drop total credit hours would be less than <min credit hours>, prevent drop", "", "");
        loadNaturalLanguageTemplate("10077", "en", "KS-KRMS-NL-USAGE-1004", "10043", "If upon drop, total credit hours would be less than <min credit hours>, prevent course drop", "", "");
        loadNaturalLanguageTemplate("10078", "en", "KS-KRMS-NL-USAGE-1004", "10044", "Course has more than <n> minutes overlap with already enrolled course", "", "");
        loadNaturalLanguageTemplate("10079", "en", "KS-KRMS-NL-USAGE-1004", "10045", "Course has less than <n> minutes between start time or end time with already enrolled course", "", "");
        loadNaturalLanguageTemplate("10080", "en", "KS-KRMS-NL-USAGE-1004", "10046", "Student in <Program> can enrol in a maximum of <n> courses for the term", "", "");
        loadNaturalLanguageTemplate("10081", "en", "KS-KRMS-NL-USAGE-1004", "10047", "Student in <Program> can enrol in a maximum of <n> credits for the term", "", "");
        loadNaturalLanguageTemplate("10084", "en", "KS-KRMS-NL-USAGE-1004", "10059", "Student is in an existing seat pool for the course with an available seat", "", "");
        loadNaturalLanguageTemplate("10085", "en", "KS-KRMS-NL-USAGE-1004", "10017", "Must have successfully completed <course>", "", "");
        loadNaturalLanguageTemplate("10086", "en", "KS-KRMS-NL-USAGE-1004", "10060", "Must have successfully completed <course> as of <term>", "", "");
        loadNaturalLanguageTemplate("10087", "en", "KS-KRMS-NL-USAGE-1004", "10061", "Must have successfully completed <course> prior to <term>", "", "");
        loadNaturalLanguageTemplate("10088", "en", "KS-KRMS-NL-USAGE-1004", "10062", "Must have successfully completed <course> between <term1> and <term2>", "", "");
        loadNaturalLanguageTemplate("10091", "en", "KS-KRMS-NL-USAGE-1004", "10018", "Must have successfully completed all courses from <courses>", "", "");
        loadNaturalLanguageTemplate("10092", "en", "KS-KRMS-NL-USAGE-1004", "10054", "Must have successfully completed a minimum of <n> credits from <course>", "", "");
        loadNaturalLanguageTemplate("10093", "en", "KS-KRMS-NL-USAGE-1004", "10055", "Must have successfully completed a minimum of <n> credits from courses in the <org>", "", "");
        loadNaturalLanguageTemplate("10094", "en", "KS-KRMS-NL-USAGE-1004", "10040", "Must have earned a minimum cumulative GPA of <GPA>", "", "");
        loadNaturalLanguageTemplate("10095", "en", "KS-KRMS-NL-USAGE-1004", "10041", "Must have earned a minimum Cumulative GPA of <GPA> in <duration>", "", "");
        loadNaturalLanguageTemplate("10096", "en", "KS-KRMS-NL-USAGE-1004", "10025", "Must have earned a minimum GPA of <GPA>  in <courses>", "", "");
        loadNaturalLanguageTemplate("10097", "en", "KS-KRMS-NL-USAGE-1004", "10027", "Must have earned a minimum grade of <gradeType> <grade> in  <courses>", "", "");
        loadNaturalLanguageTemplate("10098", "en", "KS-KRMS-NL-USAGE-1004", "10028", "Must successfully complete a minimum of <n> courses  from <courses> with a minimum grade of <gradeType> <grade>", "", "");
        loadNaturalLanguageTemplate("10099", "en", "KS-KRMS-NL-USAGE-1004", "10026", "Must not have earned a grade of <gradeType> <grade> or higher in <courses>", "", "");
        loadNaturalLanguageTemplate("10100", "en", "KS-KRMS-NL-USAGE-1004", "10038", "Must have achieved a minimum score of <score> on <tests>", "", "");
        loadNaturalLanguageTemplate("10101", "en", "KS-KRMS-NL-USAGE-1004", "10037", "Must have achieved a score no higher than <score> on <tests>", "", "");
        loadNaturalLanguageTemplate("10102", "en", "KS-KRMS-NL-USAGE-1004", "10032", "Must not have successfully completed <course>", "", "");
        loadNaturalLanguageTemplate("10103", "en", "KS-KRMS-NL-USAGE-1004", "10020", "Must not have successfully completed any courses from <courses>", "", "");
        loadNaturalLanguageTemplate("10104", "en", "KS-KRMS-NL-USAGE-1004", "10022", "Must not have successfully completed any credits from <courses>", "", "");
        loadNaturalLanguageTemplate("10105", "en", "KS-KRMS-NL-USAGE-1004", "10021", "Must successfully complete no more than  <n> credits from <courses>", "", "");
        loadNaturalLanguageTemplate("10106", "en", "KS-KRMS-NL-USAGE-1004", "10053", "Must have successfully completed no more than <n> courses from <courses>", "", "");
        loadNaturalLanguageTemplate("10107", "en", "KS-KRMS-NL-USAGE-1004", "10030", "Must be concurrently enrolled in <course>", "", "");
        loadNaturalLanguageTemplate("10108", "en", "KS-KRMS-NL-USAGE-1004", "10024", "Must be concurrently enrolled in a minimum of <n> courses from <courses>", "", "");
        loadNaturalLanguageTemplate("10109", "en", "KS-KRMS-NL-USAGE-1004", "10023", "Must be concurrently enrolled in all courses from <courses>", "", "");
        loadNaturalLanguageTemplate("10110", "en", "KS-KRMS-NL-USAGE-1004", "10039", "Must have earned a minimum of <n> total credits", "", "");
        loadNaturalLanguageTemplate("10111", "en", "KS-KRMS-NL-USAGE-1004", "10029", "May be repeater for a maximum of <n> credits", "", "");
        loadNaturalLanguageTemplate("10112", "en", "KS-KRMS-NL-USAGE-1004", "10050", "Students admitted to <campus> may take no more than <n> courses at <campus> in <duration> <durationType>", "", "");
        loadNaturalLanguageTemplate("10113", "en", "KS-KRMS-NL-USAGE-1004", "10051", "Students admitted to <campus> may take no more than <n  credits> at <campus> in <duration> <durationType>", "", "");
        loadNaturalLanguageTemplate("10114", "en", "KS-KRMS-NL-USAGE-1004", "10052", "Must have been admitted to the <Program> Program", "", "");
        loadNaturalLanguageTemplate("10115", "en", "KS-KRMS-NL-USAGE-1004", "10033", "Must be admitted to any Program offered at the course campus location", "", "");
        loadNaturalLanguageTemplate("10116", "en", "KS-KRMS-NL-USAGE-1004", "10036", "Must not have been admitted to the <Program> Program", "", "");
        loadNaturalLanguageTemplate("10117", "en", "KS-KRMS-NL-USAGE-1004", "10064", "Must not have been admitted to the <Program> Program with a class standing of <Class Standing>", "", "");
        loadNaturalLanguageTemplate("10118", "en", "KS-KRMS-NL-USAGE-1004", "10065", "Must have been admitted to a Program offered by <Org>", "", "");
        loadNaturalLanguageTemplate("10119", "en", "KS-KRMS-NL-USAGE-1004", "10066", "Student must be in a class standing of <class standing>", "", "");
        loadNaturalLanguageTemplate("10120", "en", "KS-KRMS-NL-USAGE-1004", "10067", "Student must be in a class standing of <class standing> or greater", "", "");
        loadNaturalLanguageTemplate("10121", "en", "KS-KRMS-NL-USAGE-1004", "10068", "Student must be in a class standing of <class standing> or less", "", "");
        loadNaturalLanguageTemplate("10122", "en", "KS-KRMS-NL-USAGE-1004", "10069", "Must not be in a class standing of <class standing>", "", "");
        loadNaturalLanguageTemplate("10124", "en", "KS-KRMS-NL-USAGE-1004", "10071", "Must be concurrently enrolled in <courses>", "", "");
        loadNaturalLanguageTemplate("10125", "en", "KS-KRMS-NL-USAGE-1004", "10072", "May not repeat <course> if repeated <n> times", "", "");
        loadNaturalLanguageTemplate("10126", "en", "KS-KRMS-NL-USAGE-1004", "10074", "Must have achieved a score between <score> and <score> on <test>", "", "");
        loadNaturalLanguageTemplate("10127", "en", "KS-KRMS-NL-USAGE-1004", "10075", "Must have achieved a score of <score> on <test>", "", "");
        loadNaturalLanguageTemplate("10128", "en", "KS-KRMS-NL-USAGE-1000", "10035", "Permission of $org.getLongName() required", "", "");
        loadNaturalLanguageTemplate("10129", "en", "KS-KRMS-NL-USAGE-1000", "10034", "Permission of instructor required", "", "");
        loadNaturalLanguageTemplate("10130", "en", "KS-KRMS-NL-USAGE-1000", "10038", "Must have achieved a minimum score of $fields.get('kuali.reqComponent.field.type.test.score') on $testCluSet.getCluSetAsLongName()", "", "");
        loadNaturalLanguageTemplate("10131", "en", "KS-KRMS-NL-USAGE-1000", "10037", "Must have achieved a score no higher than $fields.get('kuali.reqComponent.field.type.test.score') on $testCluSet.getCluSetAsLongName()", "", "");
        loadNaturalLanguageTemplate("10132", "en", "KS-KRMS-NL-USAGE-1000", "10028", "Must successfully complete a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"course\") with a minimum grade of #if($gradeType.getId().equals(\"kuali.result.scale.grade.letter\") || $gradeType.getId().equals(\"kuali.result.scale.grade.percentage\"))$gradeType.getName().toLowerCase() #{end}$grade from $courseCluSet.getCluSetAsCode()", "", "");
        loadNaturalLanguageTemplate("10133", "en", "KS-KRMS-NL-USAGE-1000", "10018", "#if($courseCluSet.getCluList().size() == 1)Must have successfully completed $courseCluSet.getCluSetAsCode()#{else}Must have successfully completed all courses from $courseCluSet.getCluSetAsCode()#end", "", "");
        loadNaturalLanguageTemplate("10169", "en", "KS-KRMS-NL-USAGE-1000", "10033", "Must be admitted to any program offered at the course campus location", "", "");
        loadNaturalLanguageTemplate("10180", "en", "KS-KRMS-NL-USAGE-1000", "10036", "Must not have been admitted to the $NLHelper.getCluOrCluSetAsLongNames($programClu,$programCluSet) program", "", "");
        loadNaturalLanguageTemplate("10181", "en", "KS-KRMS-NL-USAGE-1000", "10029", "May be repeated for a maximum of $intValue credits", "", "");
        loadNaturalLanguageTemplate("10182", "en", "KS-KRMS-NL-USAGE-1000", "10055", "Must have successfully completed a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"credit\") from courses in the $org.getLongName()", "", "");
        loadNaturalLanguageTemplate("10183", "en", "KS-KRMS-NL-USAGE-1000", "10052", "Must have been admitted to the $NLHelper.getCluOrCluSetAsLongNames($programClu,$programCluSet) program", "", "");
        loadNaturalLanguageTemplate("10184", "en", "KS-KRMS-NL-USAGE-1000", "10017", "Must have successfully completed $courseClu.getOfficialIdentifier().getCode()", "", "");
        loadNaturalLanguageTemplate("10185", "en", "KS-KRMS-NL-USAGE-1000", "10030", "Must be concurrently enrolled in $courseClu.getOfficialIdentifier().getCode()", "", "");
        loadNaturalLanguageTemplate("10186", "en", "KS-KRMS-NL-USAGE-1000", "10032", "Must not have successfully completed $courseClu.getOfficialIdentifier().getCode()", "", "");
        loadNaturalLanguageTemplate("10187", "en", "KS-KRMS-NL-USAGE-1000", "10053", "#if($intValue == 1 && $courseCluSet.getCluList().size() == 1)Must have successfully completed $courseCluSet.getCluSetAsCode()#{else}Must have successfully completed a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"course\") from $courseCluSet.getCluSetAsCode()#end", "", "");
        loadNaturalLanguageTemplate("10193", "en", "KS-KRMS-NL-USAGE-1001", "10039", "<reqCompFieldType=kuali.reqComponent.field.type.value.positive.integer;reqCompFieldLabel=Minimum Number of Credits>", "", "");
        loadNaturalLanguageTemplate("10194", "en", "KS-KRMS-NL-USAGE-1001", "10047", "<reqCompFieldType=kuali.reqComponent.field.type.value.positive.integer;reqCompFieldLabel=Maximum Number of Credits>", "", "");
        loadNaturalLanguageTemplate("10197", "en", "KS-KRMS-NL-USAGE-1001", "10020", "<reqCompFieldType=kuali.reqComponent.field.type.course.cluSet.id;reqCompFieldLabel=Courses>", "", "");
        loadNaturalLanguageTemplate("10203", "en", "KS-KRMS-NL-USAGE-1001", "10053", "<reqCompFieldType=kuali.reqComponent.field.type.value.positive.integer;reqCompFieldLabel=Number of Courses> from <reqCompFieldType=kuali.reqComponent.field.type.course.cluSet.id;reqCompFieldLabel=Courses>", "", "");
        loadNaturalLanguageTemplate("10204", "en", "KS-KRMS-NL-USAGE-1001", "10040", "<reqCompFieldType=kuali.reqComponent.field.type.gpa;reqCompFieldLabel=GPA>", "", "");
        loadNaturalLanguageTemplate("10205", "en", "KS-KRMS-NL-USAGE-1001", "10041", "<reqCompFieldType=kuali.reqComponent.field.type.durationType.id;reqCompFieldLabel=Duration Type> <reqCompFieldType=kuali.reqComponent.field.type.gpa;reqCompFieldLabel=GPA>", "", "");
        loadNaturalLanguageTemplate("10206", "en", "KS-KRMS-NL-USAGE-1001", "10018", "<reqCompFieldType=kuali.reqComponent.field.type.course.cluSet.id;reqCompFieldLabel=Courses>", "", "");
        loadNaturalLanguageTemplate("10208", "en", "KS-KRMS-NL-USAGE-1001", "10024", "<reqCompFieldType=kuali.reqComponent.field.type.value.positive.integer;reqCompFieldLabel=Number of Courses> from <reqCompFieldType=kuali.reqComponent.field.type.course.cluSet.id;reqCompFieldLabel=Courses>", "", "");
        loadNaturalLanguageTemplate("10209", "en", "KS-KRMS-NL-USAGE-1001", "10021", "<reqCompFieldType=kuali.reqComponent.field.type.value.positive.integer;reqCompFieldLabel=Credits> from <reqCompFieldType=kuali.reqComponent.field.type.course.cluSet.id;reqCompFieldLabel=Courses>", "", "");
        loadNaturalLanguageTemplate("10210", "en", "KS-KRMS-NL-USAGE-1001", "10022", "<reqCompFieldType=kuali.reqComponent.field.type.course.cluSet.id;reqCompFieldLabel=Courses>", "", "");
        loadNaturalLanguageTemplate("10212", "en", "KS-KRMS-NL-USAGE-1001", "10025", "<reqCompFieldType=kuali.reqComponent.field.type.gpa;reqCompFieldLabel=GPA> from <reqCompFieldType=kuali.reqComponent.field.type.course.cluSet.id;reqCompFieldLabel=Courses>", "", "");
        loadNaturalLanguageTemplate("10213", "en", "KS-KRMS-NL-USAGE-1001", "10023", "<reqCompFieldType=kuali.reqComponent.field.type.course.cluSet.id;reqCompFieldLabel=Courses>", "", "");
        loadNaturalLanguageTemplate("10214", "en", "KS-KRMS-NL-USAGE-1001", "10027", "<reqCompFieldType=kuali.reqComponent.field.type.gradeType.id;reqCompFieldLabel=Grade Type> of <reqCompFieldType=kuali.reqComponent.field.type.grade.id;reqCompFieldLabel=Grade> in <reqCompFieldType=kuali.reqComponent.field.type.course.cluSet.id;reqCompFieldLabel=Courses>", "", "");
        loadNaturalLanguageTemplate("10215", "en", "KS-KRMS-NL-USAGE-1001", "10026", "<reqCompFieldType=kuali.reqComponent.field.type.gradeType.id;reqCompFieldLabel=Grade Type> of <reqCompFieldType=kuali.reqComponent.field.type.grade.id;reqCompFieldLabel=Grade> in <reqCompFieldType=kuali.reqComponent.field.type.course.cluSet.id;reqCompFieldLabel=Courses>", "", "");
        loadNaturalLanguageTemplate("10216", "en", "KS-KRMS-NL-USAGE-1001", "10035", "<reqCompFieldType=kuali.reqComponent.field.type.org.id;reqCompFieldLabel=Organization>", "", "");
        loadNaturalLanguageTemplate("10217", "en", "KS-KRMS-NL-USAGE-1001", "10034", "<reqCompFieldType=kuali.reqComponent.field.type.person.id;reqCompFieldLabel=Instructor>", "", "");
        loadNaturalLanguageTemplate("10218", "en", "KS-KRMS-NL-USAGE-1001", "10038", "<reqCompFieldType=kuali.reqComponent.field.type.test.score;reqCompFieldLabel=Test Score> from <reqCompFieldType=kuali.reqComponent.field.type.test.cluSet.id;reqCompFieldLabel=Tests>", "", "");
        loadNaturalLanguageTemplate("10219", "en", "KS-KRMS-NL-USAGE-1001", "10037", "<reqCompFieldType=kuali.reqComponent.field.type.test.score;reqCompFieldLabel=Test Score> from <reqCompFieldType=kuali.reqComponent.field.type.test.cluSet.id;reqCompFieldLabel=Tests>", "", "");
        loadNaturalLanguageTemplate("10220", "en", "KS-KRMS-NL-USAGE-1001", "10028", "<reqCompFieldType=kuali.reqComponent.field.type.value.positive.integer;reqCompFieldLabel=Number of Courses> from <reqCompFieldType=kuali.reqComponent.field.type.course.cluSet.id;reqCompFieldLabel=Courses> with <reqCompFieldType=kuali.reqComponent.field.type.gradeType.id;reqCompFieldLabel=Grade Type> of <reqCompFieldType=kuali.reqComponent.field.type.grade.id;reqCompFieldLabel=Grade>", "", "");
        loadNaturalLanguageTemplate("10224", "en", "KS-KRMS-NL-USAGE-1001", "10036", "<reqCompFieldType=kuali.reqComponent.field.type.program.cluSet.id;reqCompFieldLabel=Program(s)>", "", "");
        loadNaturalLanguageTemplate("10225", "en", "KS-KRMS-NL-USAGE-1001", "10029", "<reqCompFieldType=kuali.reqComponent.field.type.value.positive.integer;reqCompFieldLabel=Credits>", "", "");
        loadNaturalLanguageTemplate("10226", "en", "KS-KRMS-NL-USAGE-1001", "10055", "<reqCompFieldType=kuali.reqComponent.field.type.value.positive.integer;reqCompFieldLabel=Credits> from courses in <reqCompFieldType=kuali.reqComponent.field.type.org.id;reqCompFieldLabel=Department>", "", "");
        loadNaturalLanguageTemplate("10227", "en", "KS-KRMS-NL-USAGE-1001", "10052", "<reqCompFieldType=kuali.reqComponent.field.type.program.cluSet.id;reqCompFieldLabel=Program(s)>", "", "");
        loadNaturalLanguageTemplate("10228", "en", "KS-KRMS-NL-USAGE-1001", "10017", "<reqCompFieldType=kuali.reqComponent.field.type.course.clu.id;reqCompFieldLabel=Course>", "", "");
        loadNaturalLanguageTemplate("10229", "en", "KS-KRMS-NL-USAGE-1000", "10020", "#if($courseCluSet.getCluList().size() == 1)Must not have successfully completed $courseCluSet.getCluSetAsCode()#{else}Must not have successfully completed any courses from $courseCluSet.getCluSetAsCode()#end", "", "");
        loadNaturalLanguageTemplate("10235", "en", "KS-KRMS-NL-USAGE-1000", "10039", "Must have earned a minimum of $intValue total $NLHelper.getProperGrammar($intValue, \"credit\")", "", "");
        loadNaturalLanguageTemplate("10236", "en", "KS-KRMS-NL-USAGE-1000", "10047", "Must not have earned more than $intValue $NLHelper.getProperGrammar($intValue, \"credit\")", "", "");
        loadNaturalLanguageTemplate("10239", "en", "KS-KRMS-NL-USAGE-1003", "10020", "#if($courseCluSet.getCluList().size() == 1)Must not have successfully completed#{else}Must not have successfully completed any courses from#end", "", "");
        loadNaturalLanguageTemplate("10246", "en", "KS-KRMS-NL-USAGE-1000", "10040", "Must have earned a minimum cumulative GPA of $gpa", "", "");
        loadNaturalLanguageTemplate("10247", "en", "KS-KRMS-NL-USAGE-1000", "10041", "Must have earned a minimum $durationType.getName().toLowerCase() GPA of $gpa", "", "");
        loadNaturalLanguageTemplate("10248", "en", "KS-KRMS-NL-USAGE-1003", "10018", "#if($courseCluSet.getCluList().size() == 1)Must have successfully completed#{else}Must have successfully completed all courses from#end", "", "");
        loadNaturalLanguageTemplate("10249", "en", "KS-KRMS-NL-USAGE-1003", "10053", "#if($intValue == 1 && $courseCluSet.getCluList().size() == 1)Must have successfully completed#{else}Must have successfully completed a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"course\") from#end", "", "");
        loadNaturalLanguageTemplate("10250", "en", "KS-KRMS-NL-USAGE-1003", "10024", "Must be concurrently enrolled in a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"course\") from", "", "");
        loadNaturalLanguageTemplate("10251", "en", "KS-KRMS-NL-USAGE-1003", "10021", "Must have successfully completed a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"credit\") from", "", "");
        loadNaturalLanguageTemplate("10252", "en", "KS-KRMS-NL-USAGE-1003", "10022", "Must not have successfully completed any credits from", "", "");
        loadNaturalLanguageTemplate("10254", "en", "KS-KRMS-NL-USAGE-1003", "10025", "Must have earned a minimum GPA of $gpa in", "", "");
        loadNaturalLanguageTemplate("10255", "en", "KS-KRMS-NL-USAGE-1003", "10023", "#if($courseCluSet.getCluList().size() == 1)Must be concurrently enrolled in#{else}Must be concurrently enrolled in all courses from#end", "", "");
        loadNaturalLanguageTemplate("10261", "en", "KS-KRMS-NL-USAGE-1003", "10039", "Must have earned a minimum of $intValue total $NLHelper.getProperGrammar($intValue, \"credit\")", "", "");
        loadNaturalLanguageTemplate("10262", "en", "KS-KRMS-NL-USAGE-1003", "10047", "Must not have earned more than $intValue $NLHelper.getProperGrammar($intValue, \"credit\")", "", "");
        loadNaturalLanguageTemplate("10271", "en", "KS-KRMS-NL-USAGE-1003", "10040", "Must have earned a minimum cumulative GPA of $gpa", "", "");
        loadNaturalLanguageTemplate("10272", "en", "KS-KRMS-NL-USAGE-1003", "10041", "Must have earned a minimum $durationType.getName().toLowerCase() GPA of $gpa", "", "");
        loadNaturalLanguageTemplate("10273", "en", "KS-KRMS-NL-USAGE-1003", "10027", "Must have earned a minimum grade of #if($gradeType.getId().equals(\"kuali.result.scale.grade.letter\") || $gradeType.getId().equals(\"kuali.result.scale.grade.percentage\"))$gradeType.getName().toLowerCase() #{end}$grade in", "", "");
        loadNaturalLanguageTemplate("10274", "en", "KS-KRMS-NL-USAGE-1003", "10026", "Must not have earned a maximum grade of #if($gradeType.getId().equals(\"kuali.result.scale.grade.letter\") || $gradeType.getId().equals(\"kuali.result.scale.grade.percentage\"))$gradeType.getName().toLowerCase() #{end}$grade or higher in", "", "");
        loadNaturalLanguageTemplate("10275", "en", "KS-KRMS-NL-USAGE-1003", "10035", "Permission of $org.getLongName() required", "", "");
        loadNaturalLanguageTemplate("10276", "en", "KS-KRMS-NL-USAGE-1003", "10034", "Permission of instructor required", "", "");
        loadNaturalLanguageTemplate("10277", "en", "KS-KRMS-NL-USAGE-1003", "10038", "Must have achieved a minimum score of $fields.get('kuali.reqComponent.field.type.test.score') on $testCluSet.getCluSetAsLongName()", "", "");
        loadNaturalLanguageTemplate("10278", "en", "KS-KRMS-NL-USAGE-1003", "10037", "Must have achieved a score no higher than $fields.get('kuali.reqComponent.field.type.test.score') on $testCluSet.getCluSetAsLongName()", "", "");
        loadNaturalLanguageTemplate("10279", "en", "KS-KRMS-NL-USAGE-1003", "10028", "Must successfully complete a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"course\") with a minimum grade of #if($gradeType.getId().equals(\"kuali.result.scale.grade.letter\") || $gradeType.getId().equals(\"kuali.result.scale.grade.percentage\"))$gradeType.getName().toLowerCase() #{end}$grade from", "", "");
        loadNaturalLanguageTemplate("10282", "en", "KS-KRMS-NL-USAGE-1003", "10033", "Must be admitted to any program offered at the course campus location", "", "");
        loadNaturalLanguageTemplate("10283", "en", "KS-KRMS-NL-USAGE-1003", "10036", "Must not have been admitted to the $NLHelper.getCluOrCluSetAsLongNames($programClu,$programCluSet) program", "", "");
        loadNaturalLanguageTemplate("10284", "en", "KS-KRMS-NL-USAGE-1003", "10029", "May be repeated for a maximum of $intValue credits", "", "");
        loadNaturalLanguageTemplate("10285", "en", "KS-KRMS-NL-USAGE-1003", "10055", "Must have successfully completed a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"credit\") from courses in the $org.getLongName()", "", "");
        loadNaturalLanguageTemplate("10286", "en", "KS-KRMS-NL-USAGE-1003", "10052", "Must have been admitted to the $NLHelper.getCluOrCluSetAsLongNames($programClu,$programCluSet) program", "", "");
        loadNaturalLanguageTemplate("10287", "en", "KS-KRMS-NL-USAGE-1003", "10017", "Must have successfully completed $courseClu.getOfficialIdentifier().getCode()", "", "");
        loadNaturalLanguageTemplate("10288", "en", "KS-KRMS-NL-USAGE-1003", "10030", "Must be concurrently enrolled in $courseClu.getOfficialIdentifier().getCode()", "", "");
        loadNaturalLanguageTemplate("10289", "en", "KS-KRMS-NL-USAGE-1003", "10032", "Must not have successfully completed $courseClu.getOfficialIdentifier().getCode()", "", "");
        loadNaturalLanguageTemplate("10290", "en", "KS-KRMS-NL-USAGE-1000", "10027", "Must have earned a minimum grade of #if($gradeType.getId().equals(\"kuali.result.scale.grade.letter\") || $gradeType.getId().equals(\"kuali.result.scale.grade.percentage\"))$gradeType.getName().toLowerCase() #{end}$grade in $courseCluSet.getCluSetAsCode()", "", "");
        loadNaturalLanguageTemplate("10291", "en", "KS-KRMS-NL-USAGE-1000", "10026", "Must not have earned a maximum grade of #if($gradeType.getId().equals(\"kuali.result.scale.grade.letter\") || $gradeType.getId().equals(\"kuali.result.scale.grade.percentage\"))$gradeType.getName().toLowerCase() #{end}$grade or higher in $courseCluSet.getCluSetAsCode()", "", "");
        loadNaturalLanguageTemplate("10292", "en", "KS-KRMS-NL-USAGE-1001", "10030", "<reqCompFieldType=kuali.reqComponent.field.type.course.clu.id;reqCompFieldLabel=Course>", "", "");
        loadNaturalLanguageTemplate("10293", "en", "KS-KRMS-NL-USAGE-1001", "10032", "<reqCompFieldType=kuali.reqComponent.field.type.course.clu.id;reqCompFieldLabel=Course>", "", "");
        loadNaturalLanguageTemplate("10294", "en", "KS-KRMS-NL-USAGE-1000", "10024", "Must be concurrently enrolled in a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"course\") from $courseCluSet.getCluSetAsCode()", "", "");
        loadNaturalLanguageTemplate("10304", "en", "KS-KRMS-NL-USAGE-1001", "10031", "<reqCompFieldType=kuali.reqComponent.field.type.value.freeform.text;reqCompFieldLabel=Text>", "", "");
        loadNaturalLanguageTemplate("10318", "en", "KS-KRMS-NL-USAGE-1003", "10031", "$freeText", "", "");
        loadNaturalLanguageTemplate("10325", "en", "KS-KRMS-NL-USAGE-1000", "10031", "$freeText", "", "");
        loadNaturalLanguageTemplate("10330", "en", "KS-KRMS-NL-USAGE-1000", "10021", "Must have successfully completed a minimum of $intValue $NLHelper.getProperGrammar($intValue, \"credit\") from $courseCluSet.getCluSetAsCode()", "", "");
        loadNaturalLanguageTemplate("10331", "en", "KS-KRMS-NL-USAGE-1000", "10022", "Must not have successfully completed any credits from $courseCluSet.getCluSetAsCode()", "", "");
        loadNaturalLanguageTemplate("10333", "en", "KS-KRMS-NL-USAGE-1000", "10025", "Must have earned a minimum GPA of $gpa in $courseCluSet.getCluSetAsCode()", "", "");
        loadNaturalLanguageTemplate("10334", "en", "KS-KRMS-NL-USAGE-1000", "10023", "#if($courseCluSet.getCluList().size() == 1)Must be concurrently enrolled in $courseCluSet.getCluSetAsCode()#{else}Must be concurrently enrolled in all courses from $courseCluSet.getCluSetAsCode()#end", "", "");
        loadNaturalLanguageTemplate("10335", "en", "KS-KRMS-NL-USAGE-1003", "10065", "Must have been admitted to a Program offered by <Org>", "", "");
        loadNaturalLanguageTemplate("10337", "en", "KS-KRMS-NL-USAGE-1003", "10074", "Must have achieved a score between <score> and <score> on <test>", "", "");
        loadNaturalLanguageTemplate("10338", "en", "KS-KRMS-NL-USAGE-1003", "10064", "Must not have been admitted to the <Program> Program with a class standing of <Class Standing>", "", "");
        loadNaturalLanguageTemplate("10339", "en", "KS-KRMS-NL-USAGE-1003", "10057", "May not repeat <course> if repeated <n> times ", "", "");
        loadNaturalLanguageTemplate("10340", "en", "KS-KRMS-NL-USAGE-1003", "10075", "Must have achieved a score between <score> and <score> on <test> ", "", "");
        loadNaturalLanguageTemplate("10341", "en", "KS-KRMS-NL-USAGE-1003", "10067", "Student must be in a class standing of <class standing> or greater ", "", "");
        loadNaturalLanguageTemplate("10342", "en", "KS-KRMS-NL-USAGE-1003", "10046", "Student in <Program> can enrol in a maximum of <n> courses for the term", "", "");
        loadNaturalLanguageTemplate("10343", "en", "KS-KRMS-NL-USAGE-1003", "10062", "Must have successfully completed <course> between <term1> and <term2>", "", "");
        loadNaturalLanguageTemplate("10344", "en", "KS-KRMS-NL-USAGE-1003", "10056", "Student cannot add Activity Offering with <Activity Offering State>  of <state> ", "", "");
        loadNaturalLanguageTemplate("10345", "en", "KS-KRMS-NL-USAGE-1003", "10069", "Must not be in a class standing of <class standing>", "", "");
        loadNaturalLanguageTemplate("10347", "en", "KS-KRMS-NL-USAGE-1003", "10058", "May not repeat any of  <courses>", "", "");
        loadNaturalLanguageTemplate("10348", "en", "KS-KRMS-NL-USAGE-1003", "10044", "Course has more than <n> minutes overlap with already enrolled course", "", "");
        loadNaturalLanguageTemplate("10349", "en", "KS-KRMS-NL-USAGE-1003", "10060", "Must have successfully completed <course> as of <term>", "", "");
        loadNaturalLanguageTemplate("10350", "en", "KS-KRMS-NL-USAGE-1003", "10054", "Must have successfully completed a minimum of <n> credits from <courses>", "", "");
        loadNaturalLanguageTemplate("10351", "en", "KS-KRMS-NL-USAGE-1003", "10071", "Must be concurrently enrolled in all courses from <courses>", "", "");
        loadNaturalLanguageTemplate("10352", "en", "KS-KRMS-NL-USAGE-1003", "10051", "Students admitted to <campus> may take no more than <n  credits> at <campus> in <duration> <durationType>", "", "");
        loadNaturalLanguageTemplate("10353", "en", "KS-KRMS-NL-USAGE-1003", "10050", "Students admitted to <campus> may take no more than <n> courses at <campus> in <duration> <durationType>", "", "");
        loadNaturalLanguageTemplate("10354", "en", "KS-KRMS-NL-USAGE-1003", "10043", "If student is <attribute> AND upon drop total credit hours would be less than <min credit hours>, prevent drop ", "", "");
        loadNaturalLanguageTemplate("10356", "en", "KS-KRMS-NL-USAGE-1003", "10072", "May not repeat <course> if repeated <n> times ", "", "");
        loadNaturalLanguageTemplate("10357", "en", "KS-KRMS-NL-USAGE-1003", "10061", "Must have successfully completed <course> prior to <term>", "", "");
        loadNaturalLanguageTemplate("10358", "en", "KS-KRMS-NL-USAGE-1003", "10068", "Student must be in a class standing of <class standing> or less ", "", "");
        loadNaturalLanguageTemplate("10359", "en", "KS-KRMS-NL-USAGE-1003", "10045", "Course has less than <n> minutes between start time or end time with already enrolled course", "", "");
        loadNaturalLanguageTemplate("10360", "en", "KS-KRMS-NL-USAGE-1003", "10066", "Student must be in a class standing of <class standing>", "", "");
        loadNaturalLanguageTemplate("10362", "en", "KS-KRMS-NL-USAGE-1003", "10059", "Student is in an existing seat pool for the course with an available seat", "", "");
        loadNaturalLanguageTemplate("10363", "en", "KS-KRMS-NL-USAGE-1003", "10042", "If student is <attribute> AND upon drop total credit hours would be less than <min credit hours>, prevent drop ", "", "");
        loadNaturalLanguageTemplate("10364", "en", "KS-KRMS-NL-USAGE-1000", "10077", "Must meet 1 of the following", "", "");
        loadNaturalLanguageTemplate("10365", "en", "KS-KRMS-NL-USAGE-1001", "10077", "Must meet 1 of the following", "", "");
        loadNaturalLanguageTemplate("10366", "en", "KS-KRMS-NL-USAGE-1002", "10077", "Must meet 1 of the following", "", "");
        loadNaturalLanguageTemplate("10367", "en", "KS-KRMS-NL-USAGE-1003", "10077", "Must meet 1 of the following", "", "");
        loadNaturalLanguageTemplate("10368", "en", "KS-KRMS-NL-USAGE-1005", "10077", "Must meet 1 of the following", "", "");
        loadNaturalLanguageTemplate("10369", "en", "KS-KRMS-NL-USAGE-1000", "10076", "Must meet all of the following", "", "");
        loadNaturalLanguageTemplate("10370", "en", "KS-KRMS-NL-USAGE-1001", "10076", "Must meet all of the following", "", "");
        loadNaturalLanguageTemplate("10371", "en", "KS-KRMS-NL-USAGE-1002", "10076", "Must meet all of the following", "", "");
        loadNaturalLanguageTemplate("10372", "en", "KS-KRMS-NL-USAGE-1003", "10076", "Must meet all of the following", "", "");
        loadNaturalLanguageTemplate("10373", "en", "KS-KRMS-NL-USAGE-1005", "10076", "Must meet all of the following", "", "");
        loadNaturalLanguageTemplate("10374", "en", "KS-KRMS-NL-USAGE-1000", "10065", "Must have been admitted to a Program offered by <Org>", "", "");
        loadNaturalLanguageTemplate("10376", "en", "KS-KRMS-NL-USAGE-1000", "10074", "Must have achieved a score between <score> and <score> on <test>", "", "");
        loadNaturalLanguageTemplate("10377", "en", "KS-KRMS-NL-USAGE-1000", "10064", "Must not have been admitted to the <Program> Program with a class standing of <Class Standing>", "", "");
        loadNaturalLanguageTemplate("10378", "en", "KS-KRMS-NL-USAGE-1000", "10057", "May not repeat <course> if repeated <n> times ", "", "");
        loadNaturalLanguageTemplate("10379", "en", "KS-KRMS-NL-USAGE-1000", "10075", "Must have achieved a score between <score> and <score> on <test> ", "", "");
        loadNaturalLanguageTemplate("10380", "en", "KS-KRMS-NL-USAGE-1000", "10067", "Student must be in a class standing of <class standing> or greater ", "", "");
        loadNaturalLanguageTemplate("10381", "en", "KS-KRMS-NL-USAGE-1000", "10046", "Student in <Program> can enrol in a maximum of <n> courses for the term", "", "");
        loadNaturalLanguageTemplate("10382", "en", "KS-KRMS-NL-USAGE-1000", "10062", "Must have successfully completed <course> between <term1> and <term2>", "", "");
        loadNaturalLanguageTemplate("10383", "en", "KS-KRMS-NL-USAGE-1000", "10056", "Student cannot add Activity Offering with <Activity Offering State>  of <state> ", "", "");
        loadNaturalLanguageTemplate("10384", "en", "KS-KRMS-NL-USAGE-1000", "10069", "Must not be in a class standing of <class standing>", "", "");
        loadNaturalLanguageTemplate("10386", "en", "KS-KRMS-NL-USAGE-1000", "10058", "May not repeat any of  <courses>", "", "");
        loadNaturalLanguageTemplate("10387", "en", "KS-KRMS-NL-USAGE-1000", "10044", "Course has more than <n> minutes overlap with already enrolled course", "", "");
        loadNaturalLanguageTemplate("10388", "en", "KS-KRMS-NL-USAGE-1000", "10060", "Must have successfully completed <course> as of <term>", "", "");
        loadNaturalLanguageTemplate("10389", "en", "KS-KRMS-NL-USAGE-1000", "10054", "Must have successfully completed a minimum of <n> credits from <courses>", "", "");
        loadNaturalLanguageTemplate("10390", "en", "KS-KRMS-NL-USAGE-1000", "10071", "Must be concurrently enrolled in all courses from <courses>", "", "");
        loadNaturalLanguageTemplate("10391", "en", "KS-KRMS-NL-USAGE-1000", "10051", "Students admitted to <campus> may take no more than <n  credits> at <campus> in <duration> <durationType>", "", "");
        loadNaturalLanguageTemplate("10392", "en", "KS-KRMS-NL-USAGE-1000", "10050", "Students admitted to <campus> may take no more than <n> courses at <campus> in <duration> <durationType>", "", "");
        loadNaturalLanguageTemplate("10393", "en", "KS-KRMS-NL-USAGE-1000", "10043", "If student is <attribute> AND upon drop total credit hours would be less than <min credit hours>, prevent drop ", "", "");
        loadNaturalLanguageTemplate("10395", "en", "KS-KRMS-NL-USAGE-1000", "10072", "May not repeat <course> if repeated <n> times ", "", "");
        loadNaturalLanguageTemplate("10396", "en", "KS-KRMS-NL-USAGE-1000", "10061", "Must have successfully completed <course> prior to <term>", "", "");
        loadNaturalLanguageTemplate("10397", "en", "KS-KRMS-NL-USAGE-1000", "10068", "Student must be in a class standing of <class standing> or less ", "", "");
        loadNaturalLanguageTemplate("10398", "en", "KS-KRMS-NL-USAGE-1000", "10045", "Course has less than <n> minutes between start time or end time with already enrolled course", "", "");
        loadNaturalLanguageTemplate("10399", "en", "KS-KRMS-NL-USAGE-1000", "10066", "Student must be in a class standing of <class standing>", "", "");
        loadNaturalLanguageTemplate("10401", "en", "KS-KRMS-NL-USAGE-1000", "10059", "Student is in an existing seat pool for the course with an available seat", "", "");
        loadNaturalLanguageTemplate("10402", "en", "KS-KRMS-NL-USAGE-1000", "10042", "If student is <attribute> AND upon drop total credit hours would be less than <min credit hours>, prevent drop ", "", "");
        loadNaturalLanguageTemplate("10403", "en", "KS-KRMS-NL-USAGE-1006", "10010", "Add conditions that will restrict student enrollment, addressing restrictions to majors, locations, credit level requirements, etc. or Add courses, with or without grade requirements, which a student must have completed in order to enroll.", "", "");
        loadNaturalLanguageTemplate("10404", "en", "KS-KRMS-NL-USAGE-1006", "10006", "Add conditions that will restrict student enrollment, addressing restrictions to majors, locations, credit level requirements, etc. or Add courses, with or without grade requirements, which a student must have completed in order to enroll. (TL: Why is this the same definition as above? It should at least specify that the restriction is based on concurrent enrolment, not completion.)", "", "");
        loadNaturalLanguageTemplate("10405", "en", "KS-KRMS-NL-USAGE-1006", "10008", "The courses and/or preparation added here will not prevent students from registering, but will be printed in the catalog.", "", "");
        loadNaturalLanguageTemplate("10406", "en", "KS-KRMS-NL-USAGE-1006", "10005", "Add courses that, if completed, would prevent a student from enrolling in this course.", "", "");
        loadNaturalLanguageTemplate("10407", "en", "KS-KRMS-NL-USAGE-1006", "10012", "Enrollment in or completion of another course that will restrict the credits to be awarded.", "", "");
        loadNaturalLanguageTemplate("10408", "en", "KS-KRMS-NL-USAGE-1006", "10011", "Course repeatable for credit.", "", "");
    }
}
