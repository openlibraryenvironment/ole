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
package org.kuali.rice.kew.engine.node;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.util.LinkedHashMap;

/**
 * A KeyValuePair that adds an id fields that makes it sufficient for storing in a database.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@MappedSuperclass
//@Sequence(name="KREW_RTE_NODE_S", property="stateId")
public abstract class State extends PersistableBusinessObjectBase implements KeyValue {
    @Id
    @GeneratedValue(generator="KREW_RTE_NODE_S")
	@GenericGenerator(name="KREW_RTE_NODE_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RTE_NODE_S"),
			@Parameter(name="value_column",value="id")
	})
	protected String stateId;
	private String key;
    private String value;

    public State() {}
    
    public State(String key, String value) {
    	this.key = key;
    	this.value = value;
    }

    @PrePersist
    public void customPrePersist(){
        OrmUtils.populateAutoIncValue(this, KEWServiceLocator.getEntityManagerFactory().createEntityManager());
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }
    
    @Override
    public String getKey() {
    	return key;
    }
    
    @Override
    public String getValue() {
    	return value;
    }
    
    public void setKey(String key) {
		this.key = key;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	protected LinkedHashMap<String, Object> toStringMapperFields() {
		final LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("stateId", stateId);
		map.put("key", key);
		map.put("value", value);
		return map;
	}
}
