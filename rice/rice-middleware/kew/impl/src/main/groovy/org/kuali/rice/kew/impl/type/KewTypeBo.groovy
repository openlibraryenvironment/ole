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
package org.kuali.rice.kew.impl.type

import org.kuali.rice.kew.api.repository.type.KewTypeDefinitionContract
import org.kuali.rice.core.api.mo.common.active.MutableInactivatable
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase

public class KewTypeBo extends PersistableBusinessObjectBase implements MutableInactivatable, KewTypeDefinitionContract {

	def String id
	def String name
	def String namespace
	def String serviceName
	def boolean active = true
	def List<KewTypeAttributeBo> attributes

    public List<KewTypeAttributeBo> getAttributes() {
        if (attributes == null) attributes = new ArrayList<KewTypeAttributeBo>();
        return attributes;
    }

    public String getQualifiedName() {
        if ((name != null) && (namespace != null)) {
            return name + " - " + namespace;
        }

        return "";
    }

    /**
    * Converts a mutable bo to it's immutable counterpart
    * @param bo the mutable business object
    * @return the immutable object
    */
    static org.kuali.rice.kew.api.repository.type.KewTypeDefinition to(KewTypeBo bo) {
        if (bo == null) { return null }
        return org.kuali.rice.kew.api.repository.type.KewTypeDefinition.Builder.create(bo).build();
    }
        
    /**
     * Converts a immutable object to it's mutable bo counterpart
     * @param im immutable object
     * @return the mutable bo
     */
    static KewTypeBo from(org.kuali.rice.kew.api.repository.type.KewTypeDefinition im) {
        if (im == null) { return null }

        KewTypeBo bo = new KewTypeBo()
        bo.id = im.id
        bo.name = im.name
        bo.namespace = im.namespace
        bo.serviceName = im.serviceName
        bo.active = (im.active == null) ? true : im.active;
        bo.attributes = new ArrayList<KewTypeAttributeBo>()
        for( attr in im.attributes ){
            bo.attributes.add(KewTypeAttributeBo.from(attr))
        }
        bo.versionNumber = im.versionNumber
        return bo
    }

} 