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
package org.kuali.rice.krms.impl.repository

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinitionContract
import org.apache.commons.lang.StringUtils
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition
import org.kuali.rice.krad.util.ObjectUtils
import org.kuali.rice.krad.service.SequenceAccessorService
import org.kuali.rice.krad.service.KRADServiceLocator
import org.kuali.rice.krms.api.repository.agenda.AgendaDefinition


public class AgendaBo extends PersistableBusinessObjectBase implements AgendaDefinitionContract {

    private static final String KRMS_AGENDA_S = "KRMS_AGENDA_S";

	def String id
	def String name
	def String typeId
	def String contextId
	def boolean active = true

	def String firstItemId
	def Set<AgendaAttributeBo> attributeBos
	
	def List<AgendaItemBo> items

    def ContextBo context

    private static SequenceAccessorService sequenceAccessorService;

    public AgendaBo() {
        active = true;
        items = new ArrayList<AgendaItemBo>();
    }

    public Map<String, String> getAttributes() {
        HashMap<String, String> attributes = new HashMap<String, String>();
        for (attr in attributeBos) {
            attributes.put( attr.attributeDefinition.name, attr.value )
        }
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributeBos  = new ArrayList<AgendaAttributeBo>();

        if (!StringUtils.isBlank(this.typeId)) {
            List<KrmsAttributeDefinition> attributeDefinitions = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService().findAttributeDefinitionsByType(this.getTypeId());
            Map<String, KrmsAttributeDefinition> attributeDefinitionsByName = new HashMap<String, KrmsAttributeDefinition>();
            if (attributeDefinitions != null) for (KrmsAttributeDefinition attributeDefinition : attributeDefinitions) {
                attributeDefinitionsByName.put(attributeDefinition.getName(), attributeDefinition);
            }

            for (Map.Entry<String, String> attr : attributes) {
                KrmsAttributeDefinition attributeDefinition = attributeDefinitionsByName.get(attr.key);
                AgendaAttributeBo attributeBo = new AgendaAttributeBo();
                attributeBo.setAgendaId(this.getId());
                attributeBo.setAttributeDefinitionId((attributeDefinition == null) ? null : attributeDefinition.getId());
                attributeBo.setValue(attr.getValue());
                attributeBo.setAttributeDefinition(KrmsAttributeDefinitionBo.from(attributeDefinition));
                attributeBos.add(attributeBo);
            }
        }
    }

    /**
     * Returns of copy of this agenda, with the given newAgendaName and new ids.
     * @param newAgendaName name of the newly copied AgendaBo
     * @param dateTimeStamp to append to the names of objects
     * @return AgendaBo copy of this Agenda with new ids and name
     */
    public AgendaBo copyAgenda(String newAgendaName, String dateTimeStamp) {
        List<AgendaItemBo> agendaItems = this.getItems();
        AgendaBo copiedAgenda = (AgendaBo) ObjectUtils.deepCopy(this);
        copiedAgenda.setName(newAgendaName);
        // Previous Comment:
        // If we don't clear the primary key and set the fieldsClearedOnCopy flag then the
        // MaintenanceDocumentServiceImpl.processMaintenanceObjectForCopy() will try to locate the primary keys in
        // an attempt to clear them which again would cause an exception due to the wrapper class.
        // agenda.setId(null);
        // Update: Using a copiedAgenda we don't mess with the existing agenda at all.
        copiedAgenda.setId(getNewId());

        String initAgendaItemId = this.getFirstItemId();
        List<AgendaItemBo> copiedAgendaItems = new ArrayList<AgendaItemBo>();
        Map<String, RuleBo> oldRuleIdToNew = new HashMap<String, RuleBo>();
        Map<String, AgendaItemBo> oldAgendaItemIdToNew = new HashMap<String, AgendaItemBo>();

        for (AgendaItemBo agendaItem: agendaItems) {
            if (!oldAgendaItemIdToNew.containsKey(agendaItem.getId())) {
                AgendaItemBo copiedAgendaItem = agendaItem.copyAgendaItem(copiedAgenda, oldRuleIdToNew, oldAgendaItemIdToNew, copiedAgendaItems, dateTimeStamp);
                if (initAgendaItemId != null && initAgendaItemId.equals(agendaItem.getId())) {
                    copiedAgenda.setFirstItemId(copiedAgendaItem.getId());
                }
                copiedAgendaItems.add(copiedAgendaItem);
                oldAgendaItemIdToNew.put(agendaItem.getId(), copiedAgendaItem);
            }
        }
        copiedAgenda.setItems(copiedAgendaItems);
        return copiedAgenda;
    }

    /**
     * Set the SequenceAccessorService, useful for testing.
     * @param sas SequenceAccessorService to use for getNewId()
     */
    public static void setSequenceAccessorService(SequenceAccessorService sas) {
        sequenceAccessorService = sas;
    }

    /**
     * Returns the next available Agenda id.
     * @return String the next available id
     */
    private static String getNewId(){
        if (sequenceAccessorService == null) {
            // we don't assign to sequenceAccessorService to preserve existing behavior
            return KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber(KRMS_AGENDA_S, AgendaBo.class) + "";
        }
        Long id = sequenceAccessorService.getNextAvailableSequenceNumber(KRMS_AGENDA_S, AgendaBo.class);
        return id.toString();
    }

    /**
     * Converts a mutable bo to it's immutable counterpart
     * @param AgendaBo the mutable business object
     * @return the immutable object AgendaDefinition
     */
    static AgendaDefinition to(AgendaBo bo) {
        if (bo == null) { return null; }
        return org.kuali.rice.krms.api.repository.agenda.AgendaDefinition.Builder.create(bo).build();
    }
}
