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

import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;
import org.kuali.rice.krms.api.repository.term.TermResolverDefinitionContract;

public class TermResolverBo extends PersistableBusinessObjectBase implements TermResolverDefinitionContract {
    
    def String id
    def String namespace
    def String name
    def String contextId
    def String typeId
    def String outputId
    def boolean active = true

    def TermSpecificationBo output
    def Set<TermSpecificationBo> prerequisites
    def Set<TermResolverParameterSpecificationBo> parameterSpecifications;
    def Set<TermResolverAttributeBo> attributeBos

    public void setParameterNames(Set<String> pns) {
        if (pns != null) {
            parameterSpecifications = new HashSet<TermResolverParameterSpecificationBo>();
            for (String pn : pns) {
                TermResolverParameterSpecificationBo paramSpecBo = new TermResolverParameterSpecificationBo();
                paramSpecBo.setName(pn);
                paramSpecBo.setTermResolverId(id);
                parameterSpecifications.add(paramSpecBo);
            }
        }
    }

    public Set<String> getParameterNames() {
        Set<String> results = Collections.emptySet();

        if (parameterSpecifications != null && parameterSpecifications.size() > 0) {
            results = new HashSet<String>();
            for (parmSpec in parameterSpecifications) {
                results.add( parmSpec.name );
            }
        }
        return results;
    }
    
	public Map<String, String> getAttributes() {
		HashMap<String, String> attributes = new HashMap<String, String>();
		for (attr in attributeBos) {
			attributes.put( attr.attributeDefinition.name, attr.value )
		}
		return attributes;
	}

    /**
     * Converts a mutable bo to it's immutable counterpart
     * @param bo the mutable business object
     * @return the immutable object
     */
    static TermResolverDefinition to(TermResolverBo bo) {
        if (bo == null) { return null }
        return org.kuali.rice.krms.api.repository.term.TermResolverDefinition.Builder.create(bo).build()
    }

    /**
     * Converts a immutable object to it's mutable bo counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    static TermResolverBo from(TermResolverDefinition im) {
        if (im == null) { return null }

        TermResolverBo bo = new TermResolverBo()
        bo.id = im.id
        bo.namespace = im.namespace
        bo.name = im.name
        bo.typeId = im.typeId
        bo.output = TermSpecificationBo.from(im.output)
        bo.outputId = im.output.id
        bo.parameterNames = new HashSet<String>()
		for (paramName in im.parameterNames) {
			bo.parameterSpecifications.add(TermResolverParameterSpecificationBo.from(im, paramName))
		}
		bo.prerequisites = new HashSet<TermSpecificationBo>()
		for (prereq in im.prerequisites){
			bo.prerequisites.add (TermSpecificationBo.from(prereq))
		}
		
		// build the set of term resolver attribute BOs
		Set<TermResolverAttributeBo> attrs = new HashSet<TermResolverAttributeBo>();

		// for each converted pair, build an TermResolverAttributeBo and add it to the set
		TermResolverAttributeBo attributeBo;
		for (Entry<String,String> entry  : im.getAttributes().entrySet()){
			KrmsAttributeDefinitionBo attrDefBo = KrmsRepositoryServiceLocator
					.getKrmsAttributeDefinitionService()
					.getKrmsAttributeBo(entry.getKey(), im.getNamespace());
			attributeBo = new TermResolverAttributeBo();
			attributeBo.setTermResolverId( im.getId() );
			attributeBo.setAttributeDefinitionId( attrDefBo.getId() );
			attributeBo.setValue( entry.getValue() );
			attributeBo.setAttributeDefinition( attrDefBo );
			attrs.add( attributeBo );
		}
		bo.setAttributeBos(attrs);
        bo.active = im.active
		bo.versionNumber = im.versionNumber
		return bo
	}
	public TermSpecificationBo getOutput(){
		return output;
    }

}