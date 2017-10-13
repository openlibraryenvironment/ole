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
package org.kuali.rice.krms.impl.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.krms.api.repository.LogicalOperator;
import org.kuali.rice.krms.api.repository.NaturalLanguageTree;
import org.kuali.rice.krms.api.repository.RuleManagementService;
import org.kuali.rice.krms.api.repository.TranslateBusinessMethods;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplate;
import org.kuali.rice.krms.api.repository.language.NaturalLanguageTemplaterContract;
import org.kuali.rice.krms.api.repository.proposition.PropositionDefinition;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameter;
import org.kuali.rice.krms.api.repository.proposition.PropositionParameterType;
import org.kuali.rice.krms.api.repository.proposition.PropositionType;
import org.kuali.rice.krms.api.repository.rule.RuleDefinition;
import org.kuali.rice.krms.api.repository.term.TermDefinition;
import org.kuali.rice.krms.api.repository.term.TermParameterDefinition;
import org.kuali.rice.krms.api.repository.term.TermRepositoryService;

/**
 * @author nwright
 */
public class TranslationUtility implements TranslateBusinessMethods {

    private RuleManagementService ruleManagementService;
    private TermRepositoryService termRepositoryService;
    private NaturalLanguageTemplaterContract templater;

    public TranslationUtility(RuleManagementService ruleManagementService, TermRepositoryService termRepositoryService,
            NaturalLanguageTemplaterContract templater) {
        this.ruleManagementService = ruleManagementService;
        this.termRepositoryService = termRepositoryService;
        this.templater = templater;
    }

    public RuleManagementService getRuleManagementService() {
        return ruleManagementService;
    }

    public void setRuleManagementService(RuleManagementService ruleManagementService) {
        this.ruleManagementService = ruleManagementService;
    }

    public NaturalLanguageTemplaterContract getTemplater() {
        return templater;
    }

    public void setTemplater(NaturalLanguageTemplaterContract templater) {
        this.templater = templater;
    }

    @Override
    public String translateNaturalLanguageForObject(String naturalLanguageUsageId, String typeId, String krmsObjectId, String languageCode)
            throws RiceIllegalArgumentException {

        // TODO: find out what RICE intended for this typeId? Was it supposed to be the Simple Class name?
        if (typeId.equals("agenda")) {
            AgendaDefinition agenda = this.ruleManagementService.getAgenda(krmsObjectId);
            if (agenda == null) {
                throw new RiceIllegalArgumentException(krmsObjectId + " is not an Id for an agenda");
            }
            return this.translateNaturalLanguageForAgenda(naturalLanguageUsageId, agenda, languageCode);
        } else if (typeId.equals("rule")) {
            RuleDefinition rule = this.ruleManagementService.getRule(krmsObjectId);
            if (rule == null) {
                throw new RiceIllegalArgumentException(krmsObjectId + " is not an Id for a rule");
            }
            return this.translateNaturalLanguageForRule(naturalLanguageUsageId, rule, languageCode);
        } else if (typeId.equals("proposition")) {
            PropositionDefinition proposition = this.ruleManagementService.getProposition(krmsObjectId);
            if (proposition == null) {
                throw new RiceIllegalArgumentException(krmsObjectId + " is not an Id for a proposition");
            }
            return this.translateNaturalLanguageForProposition(naturalLanguageUsageId, proposition, languageCode);
        }

        return StringUtils.EMPTY;
    }

    protected String translateNaturalLanguageForAgenda(String naturalLanguageUsageId, AgendaDefinition agenda, String languageCode) throws RiceIllegalArgumentException {
        if (agenda.getFirstItemId() == null) {
            throw new RiceIllegalArgumentException("Agenda has no first item");
        }

        AgendaItemDefinition item = this.ruleManagementService.getAgendaItem(agenda.getFirstItemId());
        return translateNaturalLanguageForAgendaItem(naturalLanguageUsageId, item, languageCode);
    }

    protected String translateNaturalLanguageForAgendaItem(String naturalLanguageUsageId, AgendaItemDefinition item, String languageCode) {
        if(item==null){
            return StringUtils.EMPTY;
        }

        String naturalLanguage = StringUtils.EMPTY;
        if (item.getRuleId() != null) {
            RuleDefinition rule = this.ruleManagementService.getRule(item.getRuleId());
            naturalLanguage += this.translateNaturalLanguageForRule(naturalLanguageUsageId, rule, languageCode);
        }
        naturalLanguage += translateNaturalLanguageForAgendaItem(naturalLanguageUsageId, item.getWhenTrue(), languageCode);
        naturalLanguage += translateNaturalLanguageForAgendaItem(naturalLanguageUsageId, item.getWhenFalse(), languageCode);
        naturalLanguage += translateNaturalLanguageForAgendaItem(naturalLanguageUsageId, item.getAlways(), languageCode);
        return naturalLanguage;
    }

    protected String translateNaturalLanguageForRule(String naturalLanguageUsageId, RuleDefinition rule, String languageCode) throws RiceIllegalArgumentException {
        if(rule==null){
            return StringUtils.EMPTY;
        }

        NaturalLanguageTemplate nlTemplate = ruleManagementService.findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId(languageCode, rule.getTypeId(), naturalLanguageUsageId);
        String naturalLanguage = nlTemplate.getTemplate() + " ";

        if(rule.getProposition()!=null){
            naturalLanguage += this.translateNaturalLanguageForProposition(naturalLanguageUsageId, rule.getProposition(), languageCode);
        }

        return naturalLanguage;
    }

    @Override
    public String translateNaturalLanguageForProposition(String naturalLanguageUsageId,
            PropositionDefinition proposition, String languageCode)
            throws RiceIllegalArgumentException {
        return translateNaturalLanguageForProposition(naturalLanguageUsageId, proposition, languageCode, true) + ". ";
    }

    /**
     * This method is added because from a functional point of view the root proposition is ignored when it is a group
     * and therefore handled differently.
     *
     * @param naturalLanguageUsageId
     * @param proposition
     * @param languageCode
     * @param isRoot
     * @return
     */
    private String translateNaturalLanguageForProposition(String naturalLanguageUsageId, PropositionDefinition proposition, String languageCode, boolean isRoot) {
        NaturalLanguageTemplate naturalLanguageTemplate = this.ruleManagementService.findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId(
                languageCode, proposition.getTypeId(), naturalLanguageUsageId);

        StringBuilder naturalLanguage = new StringBuilder();
        if (proposition.getPropositionTypeCode().equals(PropositionType.SIMPLE.getCode())) {
            if(naturalLanguageTemplate!=null){
                Map<String, Object> contextMap = this.buildSimplePropositionContextMap(proposition);
                naturalLanguage.append(templater.translate(naturalLanguageTemplate, contextMap));
            }

        } else if (proposition.getPropositionTypeCode().equals(PropositionType.COMPOUND.getCode())) {
            if(naturalLanguageTemplate!=null){
                Map<String, Object> contextMap = this.buildCompoundPropositionContextMap(naturalLanguageUsageId, proposition, languageCode);
                naturalLanguage.append(templater.translate(naturalLanguageTemplate, contextMap));
            }

            //Null check because newly created compound propositions should also be translateable.
            if(proposition.getCompoundComponents()!=null){
                String operator = getCompoundSeperator(proposition, isRoot);
                for (PropositionDefinition child : proposition.getCompoundComponents()) {
                    if(proposition.getCompoundComponents().indexOf(child)!=0){
                        naturalLanguage.append(operator);
                    }
                    naturalLanguage.append(this.translateNaturalLanguageForProposition(naturalLanguageUsageId, child, languageCode, false));
                }
            }

        } else {
            throw new RiceIllegalArgumentException("Unknown proposition type: " + proposition.getPropositionTypeCode());
        }

        return naturalLanguage.toString();
    }

    private String getCompoundSeperator(PropositionDefinition proposition, boolean isRoot) {
        String operator = getCompoundOperator(proposition);
        if (isRoot){
            return ". " + StringUtils.capitalize(operator) + " ";
        }
        return "; " + operator + " ";
    }

    private String getCompoundOperator(PropositionDefinition proposition) {
        String operator = null;
        if (LogicalOperator.AND.getCode().equalsIgnoreCase(proposition.getCompoundOpCode())) {
            operator = "and";
        } else if (LogicalOperator.OR.getCode().equalsIgnoreCase(proposition.getCompoundOpCode())) {
            operator = "or";
        }
        return operator;
    }

    @Override
    public NaturalLanguageTree translateNaturalLanguageTreeForProposition(String naturalLanguageUsageId,
            PropositionDefinition proposition,
            String languageCode) throws RiceIllegalArgumentException {
        NaturalLanguageTemplate naturalLanguageTemplate = getNaturalLanguageTemplateForProposition(naturalLanguageUsageId, proposition, languageCode);

        NaturalLanguageTree.Builder tree = NaturalLanguageTree.Builder.create();
        if (proposition.getPropositionTypeCode().equals(PropositionType.SIMPLE.getCode())) {
            Map<String, Object> contextMap = this.buildSimplePropositionContextMap(proposition);
            String naturalLanguage = templater.translate(naturalLanguageTemplate, contextMap);
            tree.setNaturalLanguage(naturalLanguage);

        } else if (proposition.getPropositionTypeCode().equals(PropositionType.COMPOUND.getCode())) {
            Map<String, Object> contextMap = this.buildCompoundPropositionContextMap(naturalLanguageUsageId, proposition, languageCode);
            String naturalLanguage = templater.translate(naturalLanguageTemplate, contextMap);
            tree.setNaturalLanguage(naturalLanguage);

            //Null check because newly created compound propositions should also be translateable.
            if(proposition.getCompoundComponents()!=null){
                List<NaturalLanguageTree> children = new ArrayList<NaturalLanguageTree>();
                for (PropositionDefinition child : proposition.getCompoundComponents()) {
                    children.add(this.translateNaturalLanguageTreeForProposition(naturalLanguageUsageId, child, languageCode));
                }
                tree.setChildren(children);
            }

        } else {
            throw new RiceIllegalArgumentException("Unknown proposition type: " + proposition.getPropositionTypeCode());
        }

        return tree.build();
    }

    protected NaturalLanguageTemplate getNaturalLanguageTemplateForProposition(String naturalLanguageUsageId, PropositionDefinition proposition, String languageCode) {
        NaturalLanguageTemplate naturalLanguageTemplate = null;
        //Continue if typeid is null, some children may not be initialized yet.
        if (proposition.getTypeId() != null) {
            naturalLanguageTemplate = this.ruleManagementService.findNaturalLanguageTemplateByLanguageCodeTypeIdAndNluId(languageCode,
                    proposition.getTypeId(), naturalLanguageUsageId);
        }
        return naturalLanguageTemplate;
    }

    protected Map<String, Object> buildSimplePropositionContextMap(PropositionDefinition proposition) {
        if (!proposition.getPropositionTypeCode().equals(PropositionType.SIMPLE.getCode())) {
            throw new RiceIllegalArgumentException("proposition is not simple " + proposition.getPropositionTypeCode() + " " + proposition.getId() + proposition.getDescription());
        }
        Map<String, Object> contextMap = new LinkedHashMap<String, Object>();
        for (PropositionParameter param : proposition.getParameters()) {
            if (param.getParameterType().equals(PropositionParameterType.TERM.getCode())) {
                TermDefinition term = param.getTermValue();
                if ((term == null) && (StringUtils.isNotBlank(param.getValue()))) {
                   term = this.termRepositoryService.getTerm(param.getValue());
                }
                if (term != null) {
                    for (TermParameterDefinition termParam : term.getParameters()) {
                        contextMap.put(termParam.getName(), termParam.getValue());
                    }
                } else {
                    contextMap.put(param.getParameterType(), param.getValue());
                }
            } else {
                contextMap.put(param.getParameterType(), param.getValue());
            }
        }
        return contextMap;
    }

    protected Map<String, Object> buildCompoundPropositionContextMap(String naturalLanguageUsageId, PropositionDefinition proposition, String languageCode) {
        if (!proposition.getPropositionTypeCode().equals(PropositionType.COMPOUND.getCode())) {
            throw new RiceIllegalArgumentException("proposition us not compound " + proposition.getPropositionTypeCode() + " " + proposition.getId() + proposition.getDescription());
        }
        return new LinkedHashMap<String, Object>();
    }

}
