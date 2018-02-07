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

import java.util.Map.Entry;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.apache.commons.lang.StringUtils


import org.kuali.rice.krms.api.repository.action.ActionDefinition;
import org.kuali.rice.krms.api.repository.action.ActionDefinitionContract;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition
import org.kuali.rice.krad.util.ObjectUtils

/**
 * The Action Business Object is the Action mutable class.
 *
 * @see ActionDefinition
 * @see ActionDefinitionContract
 * @see org.kuali.rice.krms.framework.engine.Action
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionBo extends PersistableBusinessObjectBase implements ActionDefinitionContract {

	def String id
	def String namespace
	def String name
	def String description
	def String typeId
	def String ruleId
	def Integer sequenceNumber
	
	def Set<ActionAttributeBo> attributeBos

    @Override
	public Map<String, String> getAttributes() {
		HashMap<String, String> attributes = new HashMap<String, String>();
		for (attr in attributeBos) {
            if (attr.attributeDefinition == null) {
                attributes.put("", "");
            } else {
			  attributes.put( attr.attributeDefinition.name, attr.value )
            }
		}
		return attributes;
	}

    /**
     * Set the Action Attributes
     * @param attributes to add to this Action
     */
    public void setAttributes(Map<String, String> attributes) {
        this.attributeBos  = new ArrayList<ActionAttributeBo>();

        if (!StringUtils.isBlank(this.typeId)) {
            List<KrmsAttributeDefinition> attributeDefinitions = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService().findAttributeDefinitionsByType(this.getTypeId());
            Map<String, KrmsAttributeDefinition> attributeDefinitionsByName = new HashMap<String, KrmsAttributeDefinition>();
            if (attributeDefinitions != null) for (KrmsAttributeDefinition attributeDefinition : attributeDefinitions) {
                attributeDefinitionsByName.put(attributeDefinition.getName(), attributeDefinition);
            }

            for (Map.Entry<String, String> attr : attributes) {
                KrmsAttributeDefinition attributeDefinition = attributeDefinitionsByName.get(attr.key);
                if (ObjectUtils.isNotNull(attributeDefinition)) {
                    ActionAttributeBo attributeBo = new ActionAttributeBo();
                    attributeBo.setActionId(this.getId());
                    attributeBo.setAttributeDefinitionId(attributeDefinition.getId());
                    attributeBo.setValue(attr.getValue());
                    attributeBo.setAttributeDefinition(KrmsAttributeDefinitionBo.from(attributeDefinition));
                    attributeBos.add(attributeBo);
                }
            }
        }
    }

	/**
	* Converts a mutable bo to it's immutable counterpart
	* @param bo the mutable business object
	* @return the immutable object
	*/
   static ActionDefinition to(ActionBo bo) {
	   if (bo == null) { return null }
	   return org.kuali.rice.krms.api.repository.action.ActionDefinition.Builder.create(bo).build()
   }

   /**
	* Converts a immutable object to it's mutable bo counterpart
	* @param im immutable object
	* @return the mutable bo
	*/
   static ActionBo from(ActionDefinition im) {
	   if (im == null) { return null }

	   ActionBo bo = new ActionBo()
	   bo.id = im.id
	   bo.namespace = im.namespace
	   bo.name = im.name
	   bo.typeId = im.typeId
	   bo.description = im.description
	   bo.ruleId = im.ruleId
	   bo.sequenceNumber = im.sequenceNumber
	   bo.versionNumber = im.versionNumber
	   
	   // build the set of action attribute BOs
	   Set<ActionAttributeBo> attrs = new HashSet<ActionAttributeBo>();

	   // for each converted pair, build an ActionAttributeBo and add it to the set
	   ActionAttributeBo attributeBo;
	   for (Entry<String,String> entry  : im.getAttributes().entrySet()){
		   KrmsAttributeDefinitionBo attrDefBo = KrmsRepositoryServiceLocator
		   		.getKrmsAttributeDefinitionService()
		   		.getKrmsAttributeBo(entry.getKey(), im.getNamespace());
		   attributeBo = new ActionAttributeBo();
		   attributeBo.setActionId( im.getId() );
		   attributeBo.setAttributeDefinitionId( attrDefBo.getId() );
		   attributeBo.setValue( entry.getValue() );
		   attributeBo.setAttributeDefinition( attrDefBo );
		   attrs.add( attributeBo );
	   }
	   bo.setAttributeBos(attrs);
	   return bo
   }
 
} 
