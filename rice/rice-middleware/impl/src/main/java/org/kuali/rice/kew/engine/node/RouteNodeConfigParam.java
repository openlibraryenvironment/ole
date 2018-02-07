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
import org.kuali.rice.kew.api.doctype.RouteNodeConfigurationParameterContract;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A route node definition configuration parameter.  RouteNodeConfigParameters are
 * extracted when the route node is parsed, and depend on route node implementation.
 * (well, they actually depend on the route node parser because the parser is what
 * will parse them, but the parser is not specialized per-node-type at this point) 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@Entity
@Table(name="KREW_RTE_NODE_CFG_PARM_T")
@AttributeOverrides({@AttributeOverride(name="key", column=@Column(name="KEY_CD")), @AttributeOverride(name="value", column=@Column(name="VAL"))})
public class RouteNodeConfigParam extends PersistableBusinessObjectBase implements KeyValue, RouteNodeConfigurationParameterContract {
    private static final long serialVersionUID = 5592421070149273014L;

    /**
     * Primary key
     */ 
    @Id
	@Column(name="RTE_NODE_CFG_PARM_ID")
	@GeneratedValue(generator="KREW_RTE_NODE_CFG_PARM_S")
	@GenericGenerator(name="KREW_RTE_NODE_CFG_PARM_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RTE_NODE_CFG_PARM_S"),
			@Parameter(name="value_column",value="id")
	})
    //@SequenceGenerator(name="KREW_RTE_NODE_CFG_PARM_SEQ_GEN", sequenceName="KREW_RTE_NODE_CFG_PARM_S")	
	private String id;
	private String key;
    private String value;
    /**
     * Foreign key to routenode table
     */
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="RTE_NODE_ID")
	private RouteNode routeNode;

    public RouteNodeConfigParam() {}

    public RouteNodeConfigParam(RouteNode routeNode, String key, String value) {
    	this.key = key;
    	this.value = value;
        this.routeNode = routeNode;
    }

    /**
     * @return the id
     */
    @Override
    public String getId() {
		return this.id;
    }
    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    /**
     * @return the routeNode
     */
    public RouteNode getRouteNode() {
        return this.routeNode;
    }
    /**
     * @param routeNode the routeNode to set
     */
    public void setRouteNode(RouteNode routeNode) {
        this.routeNode = routeNode;
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

    @Override
    public String getRouteNodeId() {
        if (routeNode == null || routeNode.getRouteNodeId() == null) {
            return null;
        }
        return routeNode.getRouteNodeId().toString();
    }
	
	
}
