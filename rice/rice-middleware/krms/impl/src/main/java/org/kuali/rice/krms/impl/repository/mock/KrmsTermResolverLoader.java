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

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.term.TermRepositoryService;
import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;
import org.kuali.rice.krms.api.repository.term.TermSpecificationDefinition;

/**
 *
 * @author nwright
 */
public class KrmsTermResolverLoader {

    private TermRepositoryService termRepositoryService = null;

    public TermRepositoryService getTermRepositoryService() {
        return termRepositoryService;
    }

    public void setTermRepositoryService(TermRepositoryService termRepositoryService) {
        this.termRepositoryService = termRepositoryService;
    }

    public void loadTermResolver(String id,
            String namespace,
            String name,
            String typeId,
            String outputId,
            String prereqId1,
            String prereqId2,
            String prereqId3) {
        TermSpecificationDefinition.Builder output =
                TermSpecificationDefinition.Builder.create(this.getTermRepositoryService().getTermSpecificationById(outputId));
        Set<TermSpecificationDefinition.Builder> prerequisites =
                new LinkedHashSet<TermSpecificationDefinition.Builder>();
        if (prereqId1 != null && !prereqId1.isEmpty()) {
            prerequisites.add(TermSpecificationDefinition.Builder.create(this.getTermRepositoryService().getTermSpecificationById(prereqId1)));
        }
        if (prereqId2 != null && !prereqId2.isEmpty()) {
            prerequisites.add(TermSpecificationDefinition.Builder.create(this.getTermRepositoryService().getTermSpecificationById(prereqId2)));
        }
        if (prereqId3 != null && !prereqId3.isEmpty()) {
            prerequisites.add(TermSpecificationDefinition.Builder.create(this.getTermRepositoryService().getTermSpecificationById(prereqId3)));
        }
        Map<String, String> attributes = new HashMap<String, String>();
        // TODO: findout what parameter names are used for!
        Set<String> parameterNames = new LinkedHashSet<String>();
//        create(String id, 
//        String namespaceCode, 
//                String name, 
//                String typeId,
//                TermSpecificationDefinition.Builder output, 
//                Set<TermSpecificationDefinition.Builder> prerequisites, 
//                Map<String, String> attributes,
//                Set<String> parameterNames)
        TermResolverDefinition.Builder bldr = TermResolverDefinition.Builder.create(id,
                namespace,
                name,
                typeId,
                output,
                prerequisites,
                attributes,
                parameterNames);
        bldr.setActive(true);
        TermResolverDefinition existing = this.findExisting(bldr);
        if (existing == null) {
            this.getTermRepositoryService().createTermResolver(bldr.build());
        } else {
            bldr.setVersionNumber(existing.getVersionNumber());
            this.getTermRepositoryService().updateTermResolver(bldr.build());
        }
    }

    private TermResolverDefinition findExisting(TermResolverDefinition.Builder bldr) {
        if (bldr.getId() != null) {
            try {
                return this.getTermRepositoryService().getTermResolverById(bldr.getId());
            } catch (RiceIllegalArgumentException ex) {
                return null;
            }
        }
        return this.getTermRepositoryService().getTermResolverByNameAndNamespace(bldr.getName(), bldr.getNamespace());
    }

    public void load() {
        loadTermResolver("10000", "KS-SYS", "CompletedCourse", "10000", "10000", "", "", "");
        loadTermResolver("10001", "KS-SYS", "CompletedCourses", "10000", "10001", "", "", "");
        loadTermResolver("10002", "KS-SYS", "NumberOfCompletedCourses", "10000", "10002", "", "", "");
        loadTermResolver("10003", "KS-SYS", "NumberOfCreditsFromCompletedCourses", "10000", "10003", "", "", "");
        loadTermResolver("10004", "KS-SYS", "EnrolledCourses", "10000", "10004", "", "", "");
        loadTermResolver("10005", "KS-SYS", "GPAForCourses", "10000", "10005", "", "", "");
        loadTermResolver("10006", "KS-SYS", "GradeTypeForCourses", "10000", "10006", "", "", "");
        loadTermResolver("10007", "KS-SYS", "NumberOfCredits", "10000", "10007", "", "", "");
        loadTermResolver("10008", "KS-SYS", "NumberOfCreditsFromOrganization", "10000", "10008", "", "", "");
        loadTermResolver("10009", "KS-SYS", "AdminOrganizationPermissionRequired", "10000", "10009", "", "", "");
        loadTermResolver("10010", "KS-SYS", "ScoreOnTest", "10000", "10010", "", "", "");
        loadTermResolver("10011", "KS-SYS", "AdmittedToProgram", "10000", "10011", "", "", "");
        loadTermResolver("10012", "KS-SYS", "AdmittedToProgramLimitCoursesInOrgForDuration", "10000", "10012", "", "", "");
        loadTermResolver("10013", "KS-SYS", "FreeFormText", "10000", "10013", "", "", "");
    }
}
