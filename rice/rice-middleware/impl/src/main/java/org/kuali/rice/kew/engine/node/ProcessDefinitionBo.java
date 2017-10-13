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
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.api.doctype.ProcessDefinitionContract;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.service.KEWServiceLocator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import java.io.Serializable;


/**
 * Represents a route path defined on a {@link DocumentType}.  A ProcessDefinition is a named entity which
 * simply points to an initial {@link RouteNode} which represents the beginning of the ProcessDefinition.
 * The path of the process can then be followed using the next nodes defined on the route nodes. 
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_DOC_TYP_PROC_T")
//@Sequence(name="KREW_RTE_NODE_S",property="processId")
public class ProcessDefinitionBo implements Serializable, ProcessDefinitionContract {

	private static final long serialVersionUID = -6338857095673479752L;
    
    @Id
    @GeneratedValue(generator="KREW_RTE_NODE_S")
	@GenericGenerator(name="KREW_RTE_NODE_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RTE_NODE_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="DOC_TYP_PROC_ID")
	private String processId;
	@Column(name="NM")
	private String name;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="DOC_TYP_ID")
	private DocumentType documentType;
	@OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="INIT_RTE_NODE_ID")
	private RouteNode initialRouteNode;
    @Column(name="INIT_IND")
	private boolean initial = false;
	@Version
	@Column(name="VER_NBR")
	private Integer lockVerNbr;
	
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public DocumentType getDocumentType() {
		return documentType;
	}
	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}
	public RouteNode getInitialRouteNode() {
		return initialRouteNode;
	}
	public void setInitialRouteNode(RouteNode initialRouteNode) {
		this.initialRouteNode = initialRouteNode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isInitial() {
		return initial;
	}
	public void setInitial(boolean initial) {
		this.initial = initial;
	}
	public Integer getLockVerNbr() {
		return lockVerNbr;
	}
	public void setLockVerNbr(Integer lockVerNbr) {
		this.lockVerNbr = lockVerNbr;
	}

	//@PrePersist
	public void beforeInsert(){
		OrmUtils.populateAutoIncValue(this, KEWServiceLocator.getEntityManagerFactory().createEntityManager());
	}

	@Override
    public String getId() {
        if (processId == null) {
            return null;
        }
        return processId.toString();
    }

    @Override
    public Long getVersionNumber() {
        if (lockVerNbr == null) {
            return null;
        }
        return new Long(lockVerNbr.longValue());
    }

    @Override
    public String getDocumentTypeId() {
        if (documentType == null) {
            return null;
        }
        return documentType.getId();
    }

}
