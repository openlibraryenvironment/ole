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
package org.kuali.rice.kew.impl.peopleflow

import org.kuali.rice.core.api.exception.RiceIllegalArgumentException
import org.kuali.rice.core.api.mo.common.active.MutableInactivatable
import org.kuali.rice.kew.api.KEWPropertyConstants
import org.kuali.rice.kew.api.KewApiServiceLocator
import org.kuali.rice.kew.api.peopleflow.PeopleFlowContract
import org.kuali.rice.kew.api.peopleflow.PeopleFlowDefinition
import org.kuali.rice.kew.api.peopleflow.PeopleFlowMember
import org.kuali.rice.kew.api.repository.type.KewAttributeDefinition
import org.kuali.rice.kew.api.repository.type.KewTypeAttribute
import org.kuali.rice.kew.api.repository.type.KewTypeDefinition
import org.kuali.rice.kew.impl.type.KewTypeBo
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.krad.util.BeanPropertyComparator

/**
 * Mapped entity for PeopleFlows
 */
class PeopleFlowBo extends PersistableBusinessObjectBase implements MutableInactivatable, PeopleFlowContract {

    String id
    String name
    String namespaceCode
    String typeId
    String description
    boolean active = true

    KewTypeBo typeBo;

    List<PeopleFlowAttributeBo> attributeBos = new ArrayList<PeopleFlowAttributeBo>();
    List<PeopleFlowMemberBo> members = new ArrayList<PeopleFlowMemberBo>();

    // non-persisted, used for maintenance
    Map<String, String> attributeValues = new HashMap<String, String>();

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> results = new HashMap<String, String>();

        if (attributeBos != null)
            for (PeopleFlowAttributeBo attr: attributeBos) {
                results.put(attr.attributeDefinition.name, attr.value);
            }

        return results;
    }

    public static PeopleFlowBo from(PeopleFlowDefinition peopleFlow, KewTypeDefinition kewTypeDefinition) {
        return PeopleFlowBo.fromAndUpdate(peopleFlow, kewTypeDefinition, null);
    }

    /**
     * Translates from the given PeopleFlowDefinition to a PeopleFlowBo, optionally updating the given "toUpdate" parameter
     * instead of creating a new PeopleFlowBo.  If it's not passed then a new PeopleFlowBo will be created.
     */
    public static PeopleFlowBo fromAndUpdate(PeopleFlowDefinition peopleFlow, KewTypeDefinition kewTypeDefinition, PeopleFlowBo toUpdate) {
        PeopleFlowBo result = toUpdate;
        if (toUpdate == null) {
            result = new PeopleFlowBo();
        }

        result.id = peopleFlow.getId();
        result.name = peopleFlow.getName();
        result.namespaceCode = peopleFlow.getNamespaceCode();
        result.typeId = peopleFlow.getTypeId();
        result.description = peopleFlow.getDescription();
        result.active = peopleFlow.isActive();
        result.versionNumber = peopleFlow.getVersionNumber();

        // we need to translate attributes over, this is a bit more work, first let's do some validation
        if (peopleFlow.getTypeId() == null) {
            if (kewTypeDefinition != null) {
                throw new RiceIllegalArgumentException("PeopleFlow has no type id, but a KewTypeDefinition was supplied when it should not have been.");
            }
        }
        if (peopleFlow.getTypeId() != null) {
            if (kewTypeDefinition == null) {
                throw new RiceIllegalArgumentException("PeopleFlow has a type id of '" + peopleFlow.getTypeId() + "' but no KewTypeDefinition was supplied.");
            }
            if (!kewTypeDefinition.getId().equals(peopleFlow.getTypeId())) {
                throw new RiceIllegalArgumentException("Type id of given KewTypeDefinition does not match PeopleFlow type id:  " + kewTypeDefinition.getId() + " != " + peopleFlow.getTypeId());
            }
        }

        // now we need to effectively do a diff with the given attributes, first let's add new entries and update existing ones
        result.attributeBos = new ArrayList<PeopleFlowAttributeBo>();
        if (peopleFlow.getTypeId() != null ) { // if type is null drop attributes
            peopleFlow.getAttributes().each { key, value ->
                KewAttributeDefinition attributeDefinition = kewTypeDefinition.getAttributeDefinitionByName(key);
                if (attributeDefinition == null) {
                    throw new RiceIllegalArgumentException("There is no attribute definition for the given attribute name '" + key + "'");
                }
                // they have no way to pass us the id of the attribute from the given contract
                result.attributeBos.add(PeopleFlowAttributeBo.from(attributeDefinition, null, peopleFlow.getId(), value));
            }
        }

        handleMembersUpdate(result, peopleFlow);

        return result;
    }

    /**
     * Translate the members, if the members have changed at all, we want to clear so that the current set of members
     * are removed by OJB's removal aware list.
     */
    private static void handleMembersUpdate(PeopleFlowBo peopleFlowBo, PeopleFlowDefinition peopleFlow) {
        Set<PeopleFlowMember> currentMembers = new HashSet<PeopleFlowMember>();
        if (peopleFlowBo.getMembers() == null) {
            peopleFlowBo.setMembers(new ArrayList<PeopleFlowMemberBo>());
        }
        peopleFlowBo.getMembers().each {
            currentMembers.add(PeopleFlowMember.Builder.create(it).build());
        }
        if (!currentMembers.equals(new HashSet<PeopleFlowMember>(peopleFlow.getMembers()))) {
            // this means that the membership has been updated, we need to rebuild it
            peopleFlowBo.getMembers().clear();
            peopleFlow.getMembers().each {
                peopleFlowBo.getMembers().add(PeopleFlowMemberBo.from(it));
            }
        }
    }

    public static PeopleFlowDefinition maintenanceCopy(PeopleFlowBo peopleFlowBo) {
        if (peopleFlowBo == null) {
            return null;
        }
        PeopleFlowDefinition.Builder builder = PeopleFlowDefinition.Builder.createMaintenanceCopy(peopleFlowBo);
        return builder.build();
    }

    public static PeopleFlowDefinition to(PeopleFlowBo peopleFlowBo) {
        if (peopleFlowBo == null) {
            return null;
        }
        PeopleFlowDefinition.Builder builder = PeopleFlowDefinition.Builder.create(peopleFlowBo);
        return builder.build();
    }

    /**
     * Invoked to rebuild the type attribute bos and attributes value map based on the type id
     */
    public void rebuildTypeAttributes() {
        attributeBos = new ArrayList<PeopleFlowAttributeBo>();
        attributeValues = new HashMap<String, String>();

        KewTypeDefinition typeDefinition = KewApiServiceLocator.getKewTypeRepositoryService().getTypeById(this.typeId);
        if ((typeDefinition.getAttributes() != null) && !typeDefinition.getAttributes().isEmpty()) {
            List<KewTypeAttribute> typeAttributes = new ArrayList<KewTypeAttribute>(typeDefinition.getAttributes());

            List<String> sortAttributes = new ArrayList<String>();
            sortAttributes.add(KEWPropertyConstants.SEQUENCE_NUMBER);
            Collections.sort(typeAttributes, new BeanPropertyComparator(sortAttributes));

            for (KewTypeAttribute typeAttribute: typeAttributes) {
                PeopleFlowAttributeBo attributeBo = PeopleFlowAttributeBo.from(typeAttribute.attributeDefinition, null,
                        this.id, null);
                attributeBos.add(attributeBo);

                attributeValues.put(typeAttribute.getAttributeDefinition().name, "");
            }
        }
    }

    /**
     * Updates the values in the attribute bos from the attribute values map
     */
    public void updateAttributeBoValues() {
        for (PeopleFlowAttributeBo attributeBo: attributeBos) {
            if (attributeValues.containsKey(attributeBo.getAttributeDefinition().getName())) {
                String attributeValue = attributeValues.get(attributeBo.getAttributeDefinition().getName());
                attributeBo.setValue(attributeValue);
            }
        }
    }

    @Override
    protected void postLoad() {
        attributeValues = new HashMap<String, String>();
        for (PeopleFlowAttributeBo attributeBo: attributeBos) {
            attributeValues.put(attributeBo.attributeDefinition.name, attributeBo.value);
        }
        for (PeopleFlowMemberBo member: members) {
            if (member.getMemberName() == null) {
                member.updateRelatedObject();
            }
            for (PeopleFlowDelegateBo delegate: member.getDelegates()) {
                if (delegate.getMemberName() == null) {
                    delegate.updateRelatedObject();
                }
            }
        }
    }

}
