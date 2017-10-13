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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeBoService;
import org.kuali.rice.krms.api.repository.type.KrmsTypeDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService;
import static org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService.AGENDA_SERVICE_NAME;
import static org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService.CONTEXT_SERVICE_NAME;
import static org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService.PROPOSITION_PARAMETER_SERVICE_NAMES;
import static org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService.PROPOSITION_SERVICE_NAMES;
import static org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService.RULE_SERVICE_NAME;
import static org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService.TERM_PARAMETER_SERVICE_NAME;
import static org.kuali.rice.krms.api.repository.type.KrmsTypeRepositoryService.TERM_PROPOSITION_PARAMETER_SERVICE_NAME;
import org.kuali.rice.krms.api.repository.typerelation.RelationshipType;
import org.kuali.rice.krms.api.repository.typerelation.TypeTypeRelation;

/**
 *
 * @author nwright
 */
public class KrmsTypeRepositoryServiceImpl implements KrmsTypeRepositoryService {

    private KrmsTypeBoService krmsTypeBoService = new KrmsTypeBoServiceImpl();
    private TypeTypeRelationBoService typeTypeRelationBoService = new TypeTypeRelationBoServiceImpl();

    @Override
    public KrmsTypeDefinition createKrmsType(KrmsTypeDefinition krmsType) throws RiceIllegalArgumentException, RiceIllegalStateException {
        return krmsTypeBoService.createKrmsType(krmsType);
    }

    @Override
    public KrmsTypeDefinition updateKrmsType(KrmsTypeDefinition krmsType) throws RiceIllegalArgumentException, RiceIllegalStateException {
        return krmsTypeBoService.updateKrmsType(krmsType);
    }

    @Override
    public KrmsTypeDefinition getTypeById(String id) throws RiceIllegalArgumentException {
        return krmsTypeBoService.getTypeById(id);
    }

    @Override
    public KrmsTypeDefinition getTypeByName(String namespaceCode, String name) throws RiceIllegalArgumentException, RiceIllegalStateException {
        return krmsTypeBoService.getTypeByName(namespaceCode, name);
    }

    @Override
    public List<KrmsTypeDefinition> findAllTypesByNamespace(String namespaceCode) throws RiceIllegalArgumentException {
        return krmsTypeBoService.findAllTypesByNamespace(namespaceCode);
    }

    @Override
    public List<KrmsTypeDefinition> findAllTypes() {
        return krmsTypeBoService.findAllTypes();
    }

    @Override
    public List<KrmsTypeDefinition> findAllAgendaTypesByContextId(String contextId) throws RiceIllegalArgumentException {
        return krmsTypeBoService.findAllAgendaTypesByContextId(contextId);
    }

    @Override
    public KrmsTypeDefinition getAgendaTypeByAgendaTypeIdAndContextId(String agendaTypeId, String contextId) throws RiceIllegalArgumentException {
        return krmsTypeBoService.getAgendaTypeByAgendaTypeIdAndContextId(agendaTypeId, contextId);
    }

    @Override
    public List<KrmsTypeDefinition> findAllRuleTypesByContextId(String contextId) throws RiceIllegalArgumentException {
        return krmsTypeBoService.findAllRuleTypesByContextId(contextId);
    }

    @Override
    public KrmsTypeDefinition getRuleTypeByRuleTypeIdAndContextId(String ruleTypeId, String contextId) throws RiceIllegalArgumentException {
        return krmsTypeBoService.getRuleTypeByRuleTypeIdAndContextId(ruleTypeId, contextId);
    }

    @Override
    public List<KrmsTypeDefinition> findAllActionTypesByContextId(String contextId) throws RiceIllegalArgumentException {
        return krmsTypeBoService.findAllActionTypesByContextId(contextId);
    }

    @Override
    public KrmsTypeDefinition getActionTypeByActionTypeIdAndContextId(String actionTypeId, String contextId) throws RiceIllegalArgumentException {
        return krmsTypeBoService.getActionTypeByActionTypeIdAndContextId(actionTypeId, contextId);
    }

    @Override
    public KrmsAttributeDefinition getAttributeDefinitionById(String attributeDefinitionId) throws RiceIllegalArgumentException {
        return krmsTypeBoService.getAttributeDefinitionById(attributeDefinitionId);
    }

    @Override
    public KrmsAttributeDefinition getAttributeDefinitionByName(String namespaceCode, String name) throws RiceIllegalArgumentException {
        return krmsTypeBoService.getAttributeDefinitionByName(namespaceCode, name);
    }

    ////
    //// type type relation methods
    ////
    @Override
    public TypeTypeRelation createTypeTypeRelation(TypeTypeRelation typeTypeRelation) {
        return typeTypeRelationBoService.createTypeTypeRelation(typeTypeRelation);
    }

    @Override
    public TypeTypeRelation getTypeTypeRelation(String typeTypeRelationId) {
        return typeTypeRelationBoService.getTypeTypeRelation(typeTypeRelationId);
    }

    @Override
    public void updateTypeTypeRelation(TypeTypeRelation typeTypeRelation) {
        typeTypeRelationBoService.updateTypeTypeRelation(typeTypeRelation);
    }

    @Override
    public void deleteTypeTypeRelation(String typeTypeRelationId) {
        typeTypeRelationBoService.deleteTypeTypeRelation(typeTypeRelationId);
    }

    @Override
    public List<TypeTypeRelation> findTypeTypeRelationsByFromType(String fromTypeId) {
        return typeTypeRelationBoService.findTypeTypeRelationsByFromType(fromTypeId);
    }
    
    @Override
    public List<TypeTypeRelation> findTypeTypeRelationsByToType(String toTypeId) {
        return typeTypeRelationBoService.findTypeTypeRelationsByToType(toTypeId);
    }

    @Override
    public List<TypeTypeRelation> findTypeTypeRelationsByRelationshipType(RelationshipType relationshipType) {
        return typeTypeRelationBoService.findTypeTypeRelationsByRelationshipType(relationshipType);
    }

    /**
     * Sets the businessObjectService property.
     *
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
        if (krmsTypeBoService instanceof KrmsTypeBoServiceImpl) {
            ((KrmsTypeBoServiceImpl) krmsTypeBoService).setBusinessObjectService(businessObjectService);
        }
        if (typeTypeRelationBoService instanceof TypeTypeRelationBoServiceImpl) {
            ((TypeTypeRelationBoServiceImpl) typeTypeRelationBoService).setBusinessObjectService(businessObjectService);
        }
    }

    @Override
    public List<KrmsTypeDefinition> findAllTypesByServiceName(String serviceName) throws RiceIllegalArgumentException {
        return krmsTypeBoService.findAllTypesByServiceName(serviceName);
    }

    @Override
    public List<KrmsTypeDefinition> findAllContextTypes() throws RiceIllegalArgumentException {
        return this.findAllTypesByServiceName(CONTEXT_SERVICE_NAME);
    }

    @Override
    public List<KrmsTypeDefinition> findAllAgendaTypes() throws RiceIllegalArgumentException {
        return this.findAllTypesByServiceName(AGENDA_SERVICE_NAME);
    }

    @Override
    public List<KrmsTypeDefinition> findAllRuleTypes() throws RiceIllegalArgumentException {
        return this.findAllTypesByServiceName(RULE_SERVICE_NAME);
    }

    @Override
    public List<KrmsTypeDefinition> findAllPropositionTypes() throws RiceIllegalArgumentException {
        List<KrmsTypeDefinition> list = new ArrayList<KrmsTypeDefinition> ();
        for (String typeServiceName : PROPOSITION_SERVICE_NAMES) {
            list.addAll (this.findAllTypesByServiceName(typeServiceName));
        }
        return list;
    }

    @Override
    public List<KrmsTypeDefinition> findAllPropositionParameterTypes() throws RiceIllegalArgumentException {
        List<KrmsTypeDefinition> list = new ArrayList<KrmsTypeDefinition> ();
        for (String typeServiceName : PROPOSITION_PARAMETER_SERVICE_NAMES) {
            list.addAll (this.findAllTypesByServiceName(typeServiceName));
        }
        return list;
    }

    @Override
    public List<KrmsTypeDefinition> findAgendaTypesForContextType(String contextTypeId) throws RiceIllegalArgumentException {   
        return this._findTypesForType(contextTypeId, Arrays.asList(CONTEXT_SERVICE_NAME), Arrays.asList(AGENDA_SERVICE_NAME));
    }

    private List<KrmsTypeDefinition> _findTypesForType(String typeId, List<String> fromServiceNames, List<String> toServiceNames)
            throws RiceIllegalArgumentException {
        KrmsTypeDefinition fromType = this.getTypeById(typeId);
        if (fromType == null) {
            throw new RiceIllegalArgumentException(typeId + " does not exist");
        }
        if (!fromServiceNames.contains(fromType.getServiceName())) {
            throw new RiceIllegalArgumentException(typeId + "'s serviceTypeName is " + fromType.getServiceName() + " expected " + fromServiceNames);
        }
        List<TypeTypeRelation> rels = this.findTypeTypeRelationsByFromType(typeId);
        rels = new ArrayList (rels);
        Collections.sort(rels, new TypeTypeRelationSequenceComparator ());
        List<KrmsTypeDefinition> list = new ArrayList<KrmsTypeDefinition>(rels.size());
        for (TypeTypeRelation rel : rels) {
            KrmsTypeDefinition info = this.getTypeById(rel.getToTypeId());
            if (toServiceNames.contains(info.getServiceName())) {
                list.add(info);
            }
        }
        return list;
    }
    
    
    @Override
    public List<KrmsTypeDefinition> findAgendaTypesForAgendaType(String agendaTypeId) throws RiceIllegalArgumentException {
        return this._findTypesForType(agendaTypeId, Arrays.asList(AGENDA_SERVICE_NAME), Arrays.asList(AGENDA_SERVICE_NAME));
    }

    @Override
    public List<KrmsTypeDefinition> findRuleTypesForAgendaType(String agendaTypeId) throws RiceIllegalArgumentException {
        return this._findTypesForType(agendaTypeId, Arrays.asList(AGENDA_SERVICE_NAME), Arrays.asList(RULE_SERVICE_NAME));
    }

    @Override
    public List<KrmsTypeDefinition> findPropositionTypesForRuleType(String ruleTypeId) throws RiceIllegalArgumentException {
        return this._findTypesForType(ruleTypeId, Arrays.asList(RULE_SERVICE_NAME), Arrays.asList(PROPOSITION_SERVICE_NAMES));
    }

    @Override
    public List<KrmsTypeDefinition> findPropositionParameterTypesForPropositionType(String propositionTypeId)
            throws RiceIllegalArgumentException {
        return this._findTypesForType(propositionTypeId, Arrays.asList (PROPOSITION_SERVICE_NAMES), Arrays.asList(PROPOSITION_PARAMETER_SERVICE_NAMES));
    }
    
    @Override
    public List<KrmsTypeDefinition> findTermParameterTypesForTermPropositionParameterType(String termPropositionParameterTypeId)
            throws RiceIllegalArgumentException {
        return this._findTypesForType(termPropositionParameterTypeId, Arrays.asList (TERM_PROPOSITION_PARAMETER_SERVICE_NAME), Arrays.asList(TERM_PARAMETER_SERVICE_NAME));
    }
}
