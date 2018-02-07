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

import java.util.Map.Entry

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase
import org.kuali.rice.krms.api.repository.context.ContextDefinition;
import org.kuali.rice.krms.api.repository.context.ContextDefinitionContract
import org.kuali.rice.krad.util.ObjectUtils
import org.apache.commons.lang.StringUtils;


public class ContextBo extends PersistableBusinessObjectBase implements ContextDefinitionContract {

	def String id
	def String name
	def String namespace
	def String typeId
    def String description
    def boolean active = true

	List<AgendaBo> agendas = new ArrayList<AgendaBo>()

	List<ContextAttributeBo> attributeBos = new ArrayList<ContextAttributeBo>()

//	List<ContextValidEventBo> validEvents = new ArrayList<ContextValidEventBo>()
//	List<ContextValidActionBo> validActions = new ArrayList<ContextValidActionBo>()

	Long versionNumber


	@Override
	public List<AgendaBo> getAgendas(){
		return agendas
	}

	@Override
	public Map<String, String> getAttributes() {
		Map<String, String> attributes = new HashMap<String, String>();
		for (attr in attributeBos) {
			attributes.put( attr.attributeDefinition.name, attr.value )
		}
		return attributes;
	}

    public ContextBo copyContext(String additionalNameText) {
        ContextBo copy = ObjectUtils.deepCopy(this);
        
        //
        // set all IDs to null
        //
        
        copy.setId(null);

        // copying a context does not copy the associated agendas
        copy.setAgendas(null);
        for (ContextAttributeBo attributeBo : copy.getAttributeBos()) {
            attributeBo.setId(null);
        }

        if (!StringUtils.isEmpty(additionalNameText)) {
            copy.setName(copy.getName() + additionalNameText);
        }
        
        return copy;
    }

	/**
	 * Converts a mutable bo to it's immutable counterpart
	 * @param bo the mutable business object
	 * @return the immutable object
	 */
	static ContextDefinition to(ContextBo bo) {
		if (bo == null) { return null }
		return org.kuali.rice.krms.api.repository.context.ContextDefinition.Builder.create(bo).build()
	}

	/**
	 * Converts a immutable object to it's mutable bo counterpart
	 * @param im immutable object
	 * @return the mutable bo
	 */
	static ContextBo from(ContextDefinition im) {
		if (im == null) { return null }

		ContextBo bo = new ContextBo()
		bo.id = im.id
		bo.namespace = im.namespace
		bo.name = im.name
		bo.typeId = im.typeId
        bo.description = im.description
        bo.active = im.active
		bo.agendas = new ArrayList<AgendaBo>()
		for (agenda in im.agendas){
			bo.agendas.add( KrmsRepositoryServiceLocator.getAgendaBoService().from(agenda) )
		}
		// build the list of agenda attribute BOs
		List<ContextAttributeBo> attrs = new ArrayList<ContextAttributeBo>();

		// for each converted pair, build an AgendaAttributeBo and add it to the list
		ContextAttributeBo attributeBo;
		for (Entry<String,String> entry  : im.getAttributes().entrySet()){
			KrmsAttributeDefinitionBo attrDefBo = KrmsRepositoryServiceLocator
			.getKrmsAttributeDefinitionService()
			.getKrmsAttributeBo(entry.getKey(), im.getNamespace());
			attributeBo = new ContextAttributeBo();
			attributeBo.setContextId( im.getId() );
			attributeBo.setAttributeDefinitionId( attrDefBo.getId() );
			attributeBo.setValue( entry.getValue() );
			attributeBo.setAttributeDefinition( attrDefBo );
			attrs.add( attributeBo );
		}
		bo.setAttributeBos(attrs);

		bo.versionNumber = im.versionNumber
		return bo
	}

} 