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
package org.kuali.rice.krms.impl.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.criteria.CriteriaLookupService;
import org.kuali.rice.core.api.criteria.GenericQueryResults;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.core.api.exception.RiceIllegalStateException;
import org.kuali.rice.core.api.mo.ModelObjectUtils;
import org.kuali.rice.core.impl.services.CoreImplServiceLocator;
import org.kuali.rice.coreservice.api.CoreServiceApiServiceLocator;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition;
import org.kuali.rice.krms.api.repository.agenda.AgendaItemDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.impl.util.KrmsImplConstants.PropertyNames;
import org.springframework.util.CollectionUtils;

import static org.kuali.rice.core.api.criteria.PredicateFactory.in;

/**
 * Implementation of the interface for accessing KRMS repository Agenda related
 * business objects. 
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public final class AgendaBoServiceImpl implements AgendaBoService {

    // TODO: deal with active flag

    private BusinessObjectService businessObjectService;
    private CriteriaLookupService criteriaLookupService;
    private KrmsAttributeDefinitionService attributeDefinitionService;
    private SequenceAccessorService sequenceAccessorService;

    // used for converting lists of BOs to model objects
    private static final ModelObjectUtils.Transformer<AgendaItemBo, AgendaItemDefinition> toAgendaItemDefinition =
            new ModelObjectUtils.Transformer<AgendaItemBo, AgendaItemDefinition>() {
                public AgendaItemDefinition transform(AgendaItemBo input) {
                    return AgendaItemBo.to(input);
                };
            };

    // used for converting lists of BOs to model objects
    private static final ModelObjectUtils.Transformer<AgendaBo, AgendaDefinition> toAgendaDefinition =
            new ModelObjectUtils.Transformer<AgendaBo, AgendaDefinition>() {
                public AgendaDefinition transform(AgendaBo input) {
                    return AgendaBo.to(input);
                };
            };


    /**
     * This overridden method creates a KRMS Agenda in the repository
     */
    @Override
    public AgendaDefinition createAgenda(AgendaDefinition agenda) {
        if (agenda == null){
            throw new RiceIllegalArgumentException("agenda is null");
        }
        final String nameKey = agenda.getName();
        final String contextId = agenda.getContextId();
        final AgendaDefinition existing = getAgendaByNameAndContextId(nameKey, contextId);
        if (existing != null){
            throw new IllegalStateException("the agenda to create already exists: " + agenda);
        }

        AgendaBo agendaBo = from(agenda);
        businessObjectService.save(agendaBo);
        return to(agendaBo);
    }

    /**
     * This overridden method updates an existing Agenda in the repository
     */
    @Override
    public void updateAgenda(AgendaDefinition agenda) {
        if (agenda == null){
            throw new RiceIllegalArgumentException("agenda is null");
        }

        // must already exist to be able to update
        final String agendaIdKey = agenda.getId();
        final AgendaBo existing = businessObjectService.findBySinglePrimaryKey(AgendaBo.class, agendaIdKey);
        if (existing == null) {
            throw new IllegalStateException("the agenda does not exist: " + agenda);
        }
        final AgendaDefinition toUpdate;
        if (existing.getId().equals(agenda.getId())) {
            toUpdate = agenda;
        } else {
            // if passed in id does not match existing id, correct it
            final AgendaDefinition.Builder builder = AgendaDefinition.Builder.create(agenda);
            builder.setId(existing.getId());
            toUpdate = builder.build();
        }

        // copy all updateable fields to bo
        AgendaBo boToUpdate = from(toUpdate);

        // delete any old, existing attributes
        Map<String,String> fields = new HashMap<String,String>(1);
        fields.put(PropertyNames.Agenda.AGENDA_ID, toUpdate.getId());
        businessObjectService.deleteMatching(AgendaAttributeBo.class, fields);

        // update new agenda and create new attributes
        businessObjectService.save(boToUpdate);
    }

    @Override
    public void deleteAgenda(String agendaId) {
        if (agendaId == null){ throw new RiceIllegalArgumentException("agendaId is null"); }
        final AgendaBo bo = businessObjectService.findBySinglePrimaryKey(AgendaBo.class, agendaId);
        if (bo == null){ throw new IllegalStateException("the Agenda to delete does not exists: " + agendaId);}

        List<AgendaItemDefinition> agendaItems = this.getAgendaItemsByAgendaId(bo.getId());
        for( AgendaItemDefinition agendaItem : agendaItems) {
            businessObjectService.delete(AgendaItemBo.from(agendaItem));
        }

        businessObjectService.delete(bo);
    }

    /**
     * This overridden method retrieves an Agenda from the repository
     */
    @Override
    public AgendaDefinition getAgendaByAgendaId(String agendaId) {
        if (StringUtils.isBlank(agendaId)){
            throw new RiceIllegalArgumentException("agenda id is null or blank");
        }
        AgendaBo bo = businessObjectService.findBySinglePrimaryKey(AgendaBo.class, agendaId);
        return to(bo);
    }

    /**
     * This overridden method retrieves an agenda from the repository
     */
    @Override
    public AgendaDefinition getAgendaByNameAndContextId(String name, String contextId) {
        if (StringUtils.isBlank(name)) {
            throw new RiceIllegalArgumentException("name is blank");
        }
        if (StringUtils.isBlank(contextId)) {
            throw new RiceIllegalArgumentException("contextId is blank");
        }

        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", name);
        map.put("contextId", contextId);

        AgendaBo myAgenda = businessObjectService.findByPrimaryKey(AgendaBo.class, Collections.unmodifiableMap(map));
        return to(myAgenda);
    }

    /**
     * This overridden method retrieves a set of agendas from the repository
     */
    @Override
    public List<AgendaDefinition> getAgendasByContextId(String contextId) {
        if (StringUtils.isBlank(contextId)){
            throw new RiceIllegalArgumentException("context ID is null or blank");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("contextId", contextId);
        List<AgendaBo> bos = (List<AgendaBo>) businessObjectService.findMatching(AgendaBo.class, map);
        return convertAgendaBosToImmutables(bos);
    }

    /**
     * This overridden method creates a new Agenda in the repository
     */
    @Override
    public AgendaItemDefinition createAgendaItem(AgendaItemDefinition agendaItem) {
        if (agendaItem == null){
            throw new RiceIllegalArgumentException("agendaItem is null");
        }
        if (agendaItem.getId() != null){
            final AgendaDefinition existing = getAgendaByAgendaId(agendaItem.getId());
            if (existing != null){
                throw new IllegalStateException("the agendaItem to create already exists: " + agendaItem);
            }
        }

        AgendaItemBo bo = AgendaItemBo.from(agendaItem);
        businessObjectService.save(bo);
        return AgendaItemBo.to(bo);
    }

    /**
     * This overridden method updates an existing Agenda in the repository
     */
    @Override
    public void updateAgendaItem(AgendaItemDefinition agendaItem) {
        if (agendaItem == null){
            throw new RiceIllegalArgumentException("agendaItem is null");
        }
        final String agendaItemIdKey = agendaItem.getId();
        final AgendaItemDefinition existing = getAgendaItemById(agendaItemIdKey);
        if (existing == null) {
            throw new IllegalStateException("the agenda item does not exist: " + agendaItem);
        }
        final AgendaItemDefinition toUpdate;
        if (existing.getId().equals(agendaItem.getId())) {
            toUpdate = agendaItem;
        } else {
            final AgendaItemDefinition.Builder builder = AgendaItemDefinition.Builder.create(agendaItem);
            builder.setId(existing.getId());
            toUpdate = builder.build();
        }

        AgendaItemBo aiBo = AgendaItemBo.from(toUpdate);
        updateActionAttributes(aiBo);
        businessObjectService.save(aiBo);
    }

    private void updateActionAttributes(AgendaItemBo aiBo) {
        if(aiBo.getRule()!=null){
            updateActionAttributes(aiBo.getRule().getActions());
        }
        if(aiBo.getWhenTrue()!=null){
            updateActionAttributes(aiBo.getWhenTrue());
        }
        if(aiBo.getWhenFalse()!=null){
            updateActionAttributes(aiBo.getWhenFalse());
        }
        if(aiBo.getAlways()!=null){
            updateActionAttributes(aiBo.getAlways());
        }
    }

    private void updateActionAttributes(List<ActionBo> actionBos) {
        for (ActionBo action : actionBos) {
            for (ActionAttributeBo aa : action.getAttributeBos()) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("actionId", action.getId());
                Collection<ActionAttributeBo> aaBos = businessObjectService.findMatching(ActionAttributeBo.class, map);

                for (ActionAttributeBo aaBo : aaBos) {
                    if (aaBo.getAttributeDefinitionId().equals(aa.getAttributeDefinitionId())) {
                        aa.setId(aaBo.getId());
                        aa.setVersionNumber(aaBo.getVersionNumber());
                    }
                }
            }
        }
    }

    /**
     * This overridden method adds a new AgendaItemDefinition to the repository
     */
    @Override
    public void addAgendaItem(AgendaItemDefinition agendaItem, String parentId, Boolean position) {
        if (agendaItem == null){
            throw new RiceIllegalArgumentException("agendaItem is null");
        }
        AgendaItemDefinition parent = null;
        if (parentId != null){
            parent = getAgendaItemById(parentId);
            if (parent == null){
                throw new IllegalStateException("parent agendaItem does not exist in repository. parentId = " + parentId);
            }
        }
        // create new AgendaItemDefinition
        final AgendaItemDefinition toCreate;
        if (agendaItem.getId() == null) {
            SequenceAccessorService sas = getSequenceAccessorService();
            final AgendaItemDefinition.Builder builder = AgendaItemDefinition.Builder.create(agendaItem);
            final String newId =sas.getNextAvailableSequenceNumber(
                    "KRMS_AGENDA_ITM_S", AgendaItemBo.class).toString();
            builder.setId(newId);
            toCreate = builder.build();
        } else {
            toCreate = agendaItem;
        }
        createAgendaItem(toCreate);

        // link it to it's parent (for whenTrue/whenFalse, sibling for always
        if (parentId != null) {
            final AgendaItemDefinition.Builder builder = AgendaItemDefinition.Builder.create(parent);
            if (position == null){
                builder.setAlwaysId( toCreate.getId() );
            } else if (position.booleanValue()){
                builder.setWhenTrueId( toCreate.getId() );
            } else if (!position.booleanValue()){
                builder.setWhenFalseId( toCreate.getId() );
            }
            final AgendaItemDefinition parentToUpdate = builder.build();
            updateAgendaItem( parentToUpdate );
        }
    }

    /**
     * This overridden method retrieves an AgendaItemDefinition from the repository
     */
    @Override
    public AgendaItemDefinition getAgendaItemById(String id) {
        if (StringUtils.isBlank(id)){
            throw new RiceIllegalArgumentException("agenda item id is null or blank");
        }
        AgendaItemBo bo = businessObjectService.findBySinglePrimaryKey(AgendaItemBo.class, id);
        return AgendaItemBo.to(bo);
    }

    @Override
    public List<AgendaItemDefinition> getAgendaItemsByAgendaId(String agendaId) {
        if (StringUtils.isBlank(agendaId)){
            throw new RiceIllegalArgumentException("agenda id is null or null");
        }
        List<AgendaItemDefinition> results = null;

        Collection<AgendaItemBo> bos = businessObjectService.findMatching(AgendaItemBo.class, Collections.singletonMap("agendaId", agendaId));

        if (CollectionUtils.isEmpty(bos)) {
            results = Collections.emptyList();
        } else {
            results = Collections.unmodifiableList(ModelObjectUtils.transform(bos, toAgendaItemDefinition));
        }

        return results;
    }

    @Override
    public List<AgendaDefinition> getAgendasByType(String typeId) throws RiceIllegalArgumentException {
        if (StringUtils.isBlank(typeId)){
            throw new RiceIllegalArgumentException("type ID is null or blank");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("typeId", typeId);
        List<AgendaBo> bos = (List<AgendaBo>) businessObjectService.findMatching(AgendaBo.class, map);
        return convertAgendaBosToImmutables(bos);
    }

    @Override
    public List<AgendaDefinition> getAgendasByTypeAndContext(String typeId,
            String contextId) throws RiceIllegalArgumentException {
        if (StringUtils.isBlank(typeId)){
            throw new RiceIllegalArgumentException("type ID is null or blank");
        }
        if (StringUtils.isBlank(contextId)){
            throw new RiceIllegalArgumentException("context ID is null or blank");
        }
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("typeId", typeId);
        map.put("contextId", contextId);
        Collection<AgendaBo> bos = businessObjectService.findMatching(AgendaBo.class, map);
        return convertAgendaBosToImmutables(bos);
    }

    @Override
    public List<AgendaItemDefinition> getAgendaItemsByType(String typeId) throws RiceIllegalArgumentException {
        return findAgendaItemsForAgendas(getAgendasByType(typeId));
    }

    @Override
    public List<AgendaItemDefinition> getAgendaItemsByContext(String contextId) throws RiceIllegalArgumentException {
        return findAgendaItemsForAgendas(getAgendasByContextId(contextId));
    }

    @Override
    public List<AgendaItemDefinition> getAgendaItemsByTypeAndContext(String typeId,
            String contextId) throws RiceIllegalArgumentException {
        return findAgendaItemsForAgendas(getAgendasByTypeAndContext(typeId, contextId));
    }

    @Override
    public void deleteAgendaItem(String agendaItemId) throws RiceIllegalArgumentException {
        if (StringUtils.isBlank(agendaItemId)) {
            throw new RiceIllegalArgumentException("agendaItemId must not be blank or null");
        }

        businessObjectService.deleteMatching(AgendaItemBo.class, Collections.singletonMap("id", agendaItemId));
    }

    private List<AgendaItemDefinition> findAgendaItemsForAgendas(List<AgendaDefinition> agendaDefinitions) {
        List<AgendaItemDefinition> results = null;

        if (!CollectionUtils.isEmpty(agendaDefinitions)) {
            List<AgendaItemBo> boResults = new ArrayList<AgendaItemBo>(agendaDefinitions.size());

            List<String> agendaIds = new ArrayList<String>(20);
            for (AgendaDefinition agendaDefinition : agendaDefinitions) {
                agendaIds.add(agendaDefinition.getId());

                if (agendaIds.size() == 20) {
                    // fetch batch

                    Predicate predicate = in("agendaId", agendaIds.toArray());
                    QueryByCriteria criteria = QueryByCriteria.Builder.fromPredicates(predicate);
                    GenericQueryResults<AgendaItemBo> batch = this.getCriteriaLookupService().lookup(AgendaItemBo.class, criteria);

                    boResults.addAll(batch.getResults());

                    // reset agendaIds
                    agendaIds.clear();
                }
            }

            if (agendaIds.size() > 0) {
                Predicate predicate = in("agendaId", agendaIds.toArray());
                QueryByCriteria criteria = QueryByCriteria.Builder.fromPredicates(predicate);
                GenericQueryResults<AgendaItemBo> batch = this.getCriteriaLookupService().lookup(AgendaItemBo.class, criteria);

                boResults.addAll(batch.getResults());
            }

            results = Collections.unmodifiableList(ModelObjectUtils.transform(boResults, toAgendaItemDefinition));
        } else {
            results = Collections.emptyList();
        }

        return results;
    }

    public CriteriaLookupService getCriteriaLookupService() {
        if (criteriaLookupService == null) {
            criteriaLookupService = KrmsRepositoryServiceLocator.getCriteriaLookupService();
        }
        return criteriaLookupService;
    }

    public void setCriteriaLookupService(CriteriaLookupService criteriaLookupService) {
        this.criteriaLookupService = criteriaLookupService;
    }

    /**
     * Sets the businessObjectService attribute value.
     *
     * @param businessObjectService The businessObjectService to set.
     */
    public void setBusinessObjectService(final BusinessObjectService businessObjectService) {
        this.businessObjectService = businessObjectService;
    }

    protected BusinessObjectService getBusinessObjectService() {
        if ( businessObjectService == null ) {
            businessObjectService = KRADServiceLocator.getBusinessObjectService();
        }
        return businessObjectService;
    }

    /**
     * Sets the sequenceAccessorService attribute value.
     *
     * @param sequenceAccessorService The sequenceAccessorService to set.
     */
    public void setSequenceAccessorService(final SequenceAccessorService sequenceAccessorService) {
        this.sequenceAccessorService = sequenceAccessorService;
    }

    protected SequenceAccessorService getSequenceAccessorService() {
        if ( sequenceAccessorService == null ) {
            sequenceAccessorService = KRADServiceLocator.getSequenceAccessorService();
        }
        return sequenceAccessorService;
    }

    protected KrmsAttributeDefinitionService getAttributeDefinitionService() {
        if (attributeDefinitionService == null) {
            attributeDefinitionService = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService();
        }
        return attributeDefinitionService;
    }

    public void setAttributeDefinitionService(KrmsAttributeDefinitionService attributeDefinitionService) {
        this.attributeDefinitionService = attributeDefinitionService;
    }

    /**
     * Converts a Set<AgendaBo> to an Unmodifiable Set<Agenda>
     *
     * @param agendaBos a mutable Set<AgendaBo> to made completely immutable.
     * @return An unmodifiable Set<Agenda>
     */
    public List<AgendaDefinition> convertAgendaBosToImmutables(final Collection<AgendaBo> agendaBos) {
        if (CollectionUtils.isEmpty(agendaBos)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(ModelObjectUtils.transform(agendaBos, toAgendaDefinition));
    }

    /**
     * Converts a mutable bo to it's immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    @Override
    public AgendaDefinition to(AgendaBo bo) {
        if (bo == null) { return null; }
        return org.kuali.rice.krms.api.repository.agenda.AgendaDefinition.Builder.create(bo).build();
    }


    /**
     * Converts a immutable object to it's mutable bo counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    @Override
    public AgendaBo from(AgendaDefinition im) {
        if (im == null) { return null; }

        AgendaBo bo = new AgendaBo();
        bo.setId(im.getId());
        bo.setName( im.getName() );
        bo.setTypeId( im.getTypeId() );
        bo.setContextId( im.getContextId() );
        bo.setFirstItemId( im.getFirstItemId() );
        bo.setVersionNumber( im.getVersionNumber() );
        bo.setActive(im.isActive());
        Set<AgendaAttributeBo> attributes = buildAgendaAttributeBo(im);

        bo.setAttributeBos(attributes);

        return bo;
    }

    private Set<AgendaAttributeBo> buildAgendaAttributeBo(AgendaDefinition im) {
        Set<AgendaAttributeBo> attributes = new HashSet<AgendaAttributeBo>();

        // build a map from attribute name to definition
        Map<String, KrmsAttributeDefinition> attributeDefinitionMap = new HashMap<String, KrmsAttributeDefinition>();

        List<KrmsAttributeDefinition> attributeDefinitions =
                getAttributeDefinitionService().findAttributeDefinitionsByType(im.getTypeId());

        for (KrmsAttributeDefinition attributeDefinition : attributeDefinitions) {
            attributeDefinitionMap.put(attributeDefinition.getName(), attributeDefinition);
        }

        // for each entry, build an AgendaAttributeBo and add it to the set
        for (Entry<String,String> entry  : im.getAttributes().entrySet()){
            KrmsAttributeDefinition attrDef = attributeDefinitionMap.get(entry.getKey());

            if (attrDef != null) {
                AgendaAttributeBo attributeBo = new AgendaAttributeBo();
                attributeBo.setAgendaId( im.getId() );
                attributeBo.setAttributeDefinitionId(attrDef.getId());
                attributeBo.setValue(entry.getValue());
                attributeBo.setAttributeDefinition(KrmsAttributeDefinitionBo.from(attrDef));
                attributes.add( attributeBo );
            } else {
                throw new RiceIllegalStateException("there is no attribute definition with the name '" +
                        entry.getKey() + "' that is valid for the agenda type with id = '" + im.getTypeId() +"'");
            }
        }
        return attributes;
    }

}
