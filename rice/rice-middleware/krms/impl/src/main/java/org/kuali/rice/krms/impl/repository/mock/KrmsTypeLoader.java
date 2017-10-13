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

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;

/**
 *
 * @author nwright
 */
public class KrmsTypeLoader {

    private KrmsTypeRepositoryService krmsTypeRepositoryService = null;

    public KrmsTypeRepositoryService getKrmsTypeRepositoryService() {
        return krmsTypeRepositoryService;
    }

    public void setKrmsTypeRepositoryService(KrmsTypeRepositoryService krmsTypeRepositoryService) {
        this.krmsTypeRepositoryService = krmsTypeRepositoryService;
    }

    public void loadType(String id, String name, String nameSpace, String serviceName) {
        KrmsTypeDefinition.Builder bldr = KrmsTypeDefinition.Builder.create(name, nameSpace);
        bldr.setId(id);
        bldr.setActive(true);
        bldr.setServiceName(serviceName);
        KrmsTypeDefinition existing = this.findExisting(bldr);
        if (existing == null) {
            this.getKrmsTypeRepositoryService().createKrmsType(bldr.build());
        } else {
            // consider comparing and only update if different!
            bldr.setVersionNumber(existing.getVersionNumber());
            this.getKrmsTypeRepositoryService().updateKrmsType(bldr.build());
        }
    }

    private KrmsTypeDefinition findExisting(KrmsTypeDefinition.Builder bldr) {
        if (bldr.getId() != null) {
            try {
                return this.getKrmsTypeRepositoryService().getTypeById(bldr.getId());
            } catch (RiceIllegalArgumentException ex) {
                return null;
            }
        }
        return this.getKrmsTypeRepositoryService().getTypeByName(bldr.getNamespace(), bldr.getName());
    }

    public void load() {
        // Contexts
        loadType("10000", "kuali.krms.context.type.course", "KS-SYS", "contextTypeService");
        loadType("10001", "kuali.krms.context.type.program", "KS-SYS", "contextTypeService");
        loadType("10078", "kuali.krms.context.type.course.offering", "KS-SYS", "contextTypeService");
// Agendas
        loadType("10002", "kuali.krms.agenda.type.course.enrollmentEligibility", "KS-SYS", "agendaTypeService");
        loadType("10003", "kuali.krms.agenda.type.course.creditConstraints", "KS-SYS", "agendaTypeService");
        loadType("10004", "kuali.krms.agenda.type.schedule.eligibility", "KS-SYS", "agendaTypeService");
// Rules
        loadType("10005", "kuali.krms.rule.type.course.academicReadiness.antireq", "KS-SYS", "ruleTypeService");
        loadType("10006", "kuali.krms.rule.type.course.academicReadiness.coreq", "KS-SYS", "ruleTypeService");
        loadType("10008", "kuali.krms.rule.type.course.recommendedPreparation", "KS-SYS", "ruleTypeService");
        loadType("10009", "kuali.krms.rule.type.course.academicReadiness.studentEligibility", "KS-SYS", "ruleTypeService");
        loadType("10010", "kuali.krms.rule.type.course.academicReadiness.studentEligibilityPrereq", "KS-SYS", "ruleTypeService");
        loadType("10011", "kuali.krms.rule.type.course.credit.repeatable", "KS-SYS", "ruleTypeService");
        loadType("10012", "kuali.krms.rule.type.course.credit.restriction", "KS-SYS", "ruleTypeService");
        loadType("10013", "kuali.krms.rule.type.program.completion", "KS-SYS", "ruleTypeService");
        loadType("10014", "kuali.krms.rule.type.program.entrance", "KS-SYS", "ruleTypeService");
        loadType("10015", "kuali.krms.rule.type.program.satisfactoryProgress", "KS-SYS", "ruleTypeService");
        loadType("10016", "kuali.krms.rule.type.schedule.eligibility", "KS-SYS", "ruleTypeService");
// Propositions
        loadType("10017", "kuali.krms.proposition.type.success.compl.course", "KS-SYS", "simplePropositionTypeService");
        loadType("10018", "kuali.krms.proposition.type.success.course.courseset.completed.all", "KS-SYS", "simplePropositionTypeService");
        loadType("10019", "kuali.krms.proposition.type.success.course.courseset.completed.nof", "KS-SYS", "simplePropositionTypeService");
        loadType("10020", "kuali.krms.proposition.type.course.courseset.completed.none", "KS-SYS", "simplePropositionTypeService");
        loadType("10021", "kuali.krms.proposition.type.course.courseset.credits.completed.nof", "KS-SYS", "simplePropositionTypeService");
        loadType("10022", "kuali.krms.proposition.type.course.courseset.credits.completed.none", "KS-SYS", "simplePropositionTypeService");
        loadType("10023", "kuali.krms.proposition.type.course.courseset.enrolled.all", "KS-SYS", "simplePropositionTypeService");
        loadType("10024", "kuali.krms.proposition.type.course.courseset.enrolled.nof", "KS-SYS", "simplePropositionTypeService");
        loadType("10025", "kuali.krms.proposition.type.course.courseset.gpa.min", "KS-SYS", "simplePropositionTypeService");
        loadType("10026", "kuali.krms.proposition.type.course.courseset.grade.max", "KS-SYS", "simplePropositionTypeService");
        loadType("10027", "kuali.krms.proposition.type.course.courseset.grade.min", "KS-SYS", "simplePropositionTypeService");
        loadType("10028", "kuali.krms.proposition.type.course.courseset.nof.grade.min", "KS-SYS", "simplePropositionTypeService");
        loadType("10029", "kuali.krms.proposition.type.course.credits.repeat.max", "KS-SYS", "simplePropositionTypeService");
        loadType("10030", "kuali.krms.proposition.type.course.enrolled", "KS-SYS", "simplePropositionTypeService");
        loadType("10031", "kuali.krms.proposition.type.freeform.text", "KS-SYS", "simplePropositionTypeService");
        loadType("10032", "kuali.krms.proposition.type.course.notcompleted", "KS-SYS", "simplePropositionTypeService");
        loadType("10033", "kuali.krms.proposition.type.admitted.to.program.campus", "KS-SYS", "simplePropositionTypeService");
        loadType("10034", "kuali.krms.proposition.type.permission.instructor.required", "KS-SYS", "simplePropositionTypeService");
        loadType("10035", "kuali.krms.proposition.type.permission.admin.org", "KS-SYS", "simplePropositionTypeService");
        loadType("10036", "kuali.krms.proposition.type.notadmitted.to.program", "KS-SYS", "simplePropositionTypeService");
        loadType("10037", "kuali.krms.proposition.type.course.test.score.max", "KS-SYS", "simplePropositionTypeService");
        loadType("10038", "kuali.krms.proposition.type.course.test.score.min", "KS-SYS", "simplePropositionTypeService");
        loadType("10039", "kuali.krms.proposition.type.credits.earned.min", "KS-SYS", "simplePropositionTypeService");
        loadType("10040", "kuali.krms.proposition.type.cumulative.gpa.min", "KS-SYS", "simplePropositionTypeService");
        loadType("10041", "kuali.krms.proposition.type.duration.cumulative.gpa.min", "KS-SYS", "simplePropositionTypeService");
        loadType("10042", "kuali.krms.proposition.type.drop.min.credit.hours.due.to.attribute", "KS-SYS", "simplePropositionTypeService");
        loadType("10043", "kuali.krms.proposition.type.drop.min.credit.hours", "KS-SYS", "simplePropositionTypeService");
        loadType("10044", "kuali.krms.proposition.type.exceeds.minutes.overlap.allowed", "KS-SYS", "simplePropositionTypeService");
        loadType("10045", "kuali.krms.proposition.type.time.conflict.start.end", "KS-SYS", "simplePropositionTypeService");
        loadType("10046", "kuali.krms.proposition.type.max.limit.courses.for.program", "KS-SYS", "simplePropositionTypeService");
        loadType("10047", "kuali.krms.proposition.type.max.limit.credits.for.program", "KS-SYS", "simplePropositionTypeService");
        loadType("10050", "kuali.krms.proposition.type.max.limit.courses.for.campus.duration", "KS-SYS", "simplePropositionTypeService");
        loadType("10051", "kuali.krms.proposition.type.max.limit.credits.for.campus.duration", "KS-SYS", "simplePropositionTypeService");
        loadType("10052", "kuali.krms.proposition.type.admitted.to.program", "KS-SYS", "simplePropositionTypeService");
        loadType("10053", "kuali.krms.proposition.type.course.courseset.completed.nof", "KS-SYS", "simplePropositionTypeService");
        loadType("10054", "kuali.krms.proposition.type.success.credit.courseset.completed.nof", "KS-SYS", "simplePropositionTypeService");
        loadType("10055", "kuali.krms.proposition.type.success.credits.courseset.completed.nof.org", "KS-SYS", "simplePropositionTypeService");
        loadType("10056", "kuali.krms.proposition.type.cant.add.to.activity.offering.due.to.state", "KS-SYS", "simplePropositionTypeService");
        loadType("10057", "kuali.krms.proposition.type.no.repeat.course", "KS-SYS", "simplePropositionTypeService");
        loadType("10058", "kuali.krms.proposition.type.no.repeat.courses", "KS-SYS", "simplePropositionTypeService");
        loadType("10059", "kuali.krms.proposition.type.avail.seat", "KS-SYS", "simplePropositionTypeService");
        loadType("10060", "kuali.krms.proposition.type.success.compl.course.as.of.term", "KS-SYS", "simplePropositionTypeService");
        loadType("10061", "kuali.krms.proposition.type.success.compl.prior.to.term", "KS-SYS", "simplePropositionTypeService");
        loadType("10062", "kuali.krms.proposition.type.success.compl.course.between.terms", "KS-SYS", "simplePropositionTypeService");
        loadType("10064", "kuali.krms.proposition.type.notadmitted.to.program.in.class.standing", "KS-SYS", "simplePropositionTypeService");
        loadType("10065", "kuali.krms.proposition.type.admitted.to.program.org", "KS-SYS", "simplePropositionTypeService");
        loadType("10066", "kuali.krms.proposition.type.in.class.standing", "KS-SYS", "simplePropositionTypeService");
        loadType("10067", "kuali.krms.proposition.type.greater.than.class.standing", "KS-SYS", "simplePropositionTypeService");
        loadType("10068", "kuali.krms.proposition.type.less.than.class.standing", "KS-SYS", "simplePropositionTypeService");
        loadType("10069", "kuali.krms.proposition.type.notin.class.standing", "KS-SYS", "simplePropositionTypeService");
        loadType("10071", "kuali.krms.proposition.type.course.courseset.enrolled", "KS-SYS", "simplePropositionTypeService");
        loadType("10072", "kuali.krms.proposition.type.no.repeat.course.nof", "KS-SYS", "simplePropositionTypeService");
        loadType("10074", "kuali.krms.proposition.type.test.score.between.values", "KS-SYS", "simplePropositionTypeService");
        loadType("10075", "kuali.krms.proposition.type.test.score", "KS-SYS", "simplePropositionTypeService");
        loadType("10076", "kuali.krms.proposition.type.compound.and", "KS-SYS", "compoundPropositionTypeService");
        loadType("10077", "kuali.krms.proposition.type.compound.or", "KS-SYS", "compoundPropositionTypeService");
// Parameters
        loadType("10100", "kuali.krms.proposition.parameter.type.term.number.of.completed.courses", "KS-SYS", "termPropositionParameterTypeService");
        loadType("10101", "kuali.krms.proposition.parameter.type.operator.less.than.or.equal.to", "KS-SYS", "operatorPropositionParameterTypeService");
        loadType("10102", "kuali.krms.proposition.parameter.type.constant.value.n", "KS-SYS", "constantPropositionParameterTypeService");
        // term parameters
        loadType("10103", "kuali.term.parameter.type.course.clu.id", "KS-SYS", "termParameterTypeService");
        loadType("10104", "kuali.term.parameter.type.course.cluSet.id", "KS-SYS", "termParameterTypeService");
        loadType("10105", "kuali.term.parameter.type.free.text", "KS-SYS", "termParameterTypeService");
        loadType("10106", "kuali.term.parameter.type.grade.id", "KS-SYS", "termParameterTypeService");
        loadType("10107", "kuali.term.parameter.type.org.id", "KS-SYS", "termParameterTypeService");
        loadType("10108", "kuali.term.parameter.type.program.cluSet.id", "KS-SYS", "termParameterTypeService");
    }
}
