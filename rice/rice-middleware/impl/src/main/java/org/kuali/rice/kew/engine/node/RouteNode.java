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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.kuali.rice.core.framework.persistence.jpa.OrmUtils;
import org.kuali.rice.kew.api.doctype.RouteNodeConfigurationParameterContract;
import org.kuali.rice.kew.api.doctype.RouteNodeContract;
import org.kuali.rice.kew.api.exception.ResourceUnavailableException;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.rule.service.RuleTemplateService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;

/**
 * Represents the prototype definition of a node in the route path of {@link DocumentType}.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@Entity
@Table(name="KREW_RTE_NODE_T")
//@Sequence(name="KREW_RTE_NODE_S", property="routeNodeId")
@NamedQueries({
	@NamedQuery(name="RouteNode.FindByRouteNodeId",query="select r from RouteNode as r where r.routeNodeId = :routeNodeId"),
	@NamedQuery(name="RouteNode.FindRouteNodeByName", query="select r from RouteNode as r where r.routeNodeName = :routeNodeName and r.documentTypeId = :documentTypeId"),
	@NamedQuery(name="RouteNode.FindApprovalRouteNodes", query="select r from RouteNode as r where r.documentTypeId = :documentTypeId and r.finalApprovalInd = :finalApprovalInd")
})
public class RouteNode implements Serializable, RouteNodeContract {    

    private static final long serialVersionUID = 4891233177051752726L;

    public static final String CONTENT_FRAGMENT_CFG_KEY = "contentFragment";
    public static final String RULE_SELECTOR_CFG_KEY = "ruleSelector";

    @Id
    @GeneratedValue(generator="KREW_RTE_NODE_S")
	@GenericGenerator(name="KREW_RTE_NODE_S",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",parameters={
			@Parameter(name="sequence_name",value="KREW_RTE_NODE_S"),
			@Parameter(name="value_column",value="id")
	})
	@Column(name="RTE_NODE_ID")
	private String routeNodeId;
    @Column(name="DOC_TYP_ID",insertable=false, updatable=false)
	private String documentTypeId;
    @Column(name="NM")
	private String routeNodeName;
    @Column(name="RTE_MTHD_NM")
	private String routeMethodName;
    @Column(name="FNL_APRVR_IND")
	private Boolean finalApprovalInd;
    @Column(name="MNDTRY_RTE_IND")
	private Boolean mandatoryRouteInd;
    @Column(name="GRP_ID")
	private String exceptionWorkgroupId;
    @Column(name="RTE_MTHD_CD")
	private String routeMethodCode;
    @Column(name="ACTVN_TYP")
    private String activationType = ActivationTypeEnum.PARALLEL.getCode();
    
    /**
     * The nextDocStatus property represents the value of the ApplicationDocumentStatus to be set 
     * in the RouteHeader upon transitioning from this node.
     */
    @Column(name="NEXT_DOC_STAT")
	private String nextDocStatus;

    @Version
	@Column(name="VER_NBR")
	private Integer lockVerNbr;
    @ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="DOC_TYP_ID")
	private DocumentType documentType;
    @Transient
    private String exceptionWorkgroupName;

    @Transient
    private RuleTemplateBo ruleTemplate;
    @Column(name="TYP")
    private String nodeType = RequestsNode.class.getName();
    
    //@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}, mappedBy="nextNodes")
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST}, mappedBy="nextNodes")
    @Fetch(value = FetchMode.SELECT)
    //@JoinTable(name = "KREW_RTE_NODE_LNK_T", joinColumns = @JoinColumn(name = "TO_RTE_NODE_ID"), inverseJoinColumns = @JoinColumn(name = "FROM_RTE_NODE_ID"))
    private List<RouteNode> previousNodes = new ArrayList<RouteNode>();
    //@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST})
    @Fetch(value = FetchMode.SELECT)
    @JoinTable(name = "KREW_RTE_NODE_LNK_T", joinColumns = @JoinColumn(name = "FROM_RTE_NODE_ID"), inverseJoinColumns = @JoinColumn(name = "TO_RTE_NODE_ID"))
    private List<RouteNode> nextNodes = new ArrayList<RouteNode>();
    @OneToOne(fetch=FetchType.EAGER, cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="BRCH_PROTO_ID")
	private BranchPrototype branch;
    @OneToMany(fetch=FetchType.EAGER,mappedBy="routeNode",cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @Fetch(value = FetchMode.SELECT)
    private List<RouteNodeConfigParam> configParams  = new ArrayList<RouteNodeConfigParam>(0);

    /**
     * Looks up a config parameter for this route node definition
     * @param key the config param key 
     * @return the RouteNodeConfigParam if present
     */
    protected RouteNodeConfigParam getConfigParam(String key) {
        Map<String, RouteNodeConfigParam> configParamMap = Utilities.getKeyValueCollectionAsLookupTable(configParams);
        return configParamMap.get(key);
    }

    /**
     * Sets a config parameter for this route node definition.  If the key already exists
     * the existing RouteNodeConfigParam is modified, otherwise a new one is created
     * @param key the key of the parameter to set
     * @param value the value to set
     */
    protected void setConfigParam(String key, String value) {
        Map<String, RouteNodeConfigParam> configParamMap = Utilities.getKeyValueCollectionAsLookupTable(configParams);
        RouteNodeConfigParam cfCfgParam = configParamMap.get(key);
        if (cfCfgParam == null) {
            cfCfgParam = new RouteNodeConfigParam(this, key, value);
            configParams.add(cfCfgParam);
        } else {
            cfCfgParam.setValue(value);
        }
    }

    public List<RouteNodeConfigParam> getConfigParams() {
        return configParams;
    }

    public void setConfigParams(List<RouteNodeConfigParam> configParams) {
        this.configParams = configParams;
    }

    /**
     * @return the RouteNodeConfigParam value under the 'contentFragment'  key
     */
    public String getContentFragment() {
        RouteNodeConfigParam cfCfgParam = getConfigParam(CONTENT_FRAGMENT_CFG_KEY);
        if (cfCfgParam == null) return null;
        return cfCfgParam.getValue();
    }

    /**
     * @param contentFragment the content fragment of the node, which will be set as a RouteNodeConfigParam under the 'contentFragment' key
     */
    public void setContentFragment(String contentFragment) {
        setConfigParam(CONTENT_FRAGMENT_CFG_KEY, contentFragment);
    }

    public String getActivationType() {
        return activationType;
    }

    public void setActivationType(String activationType) {
        /* Cleanse the input.
         * This is surely not the best way to validate the activation types;
         * it would probably be better to use typesafe enums accross the board
         * but that would probably entail refactoring large swaths of code, not
         * to mention reconfiguring OJB (can typesafe enums be used?) and dealing
         * with serialization compatibility issues (if any).
         * So instead, let's just be sure to fail-fast.
         */
        ActivationTypeEnum at = ActivationTypeEnum.lookupCode(activationType);
        this.activationType = at.getCode();
    }

    public Group getExceptionWorkgroup() {
    	if (!StringUtils.isBlank(exceptionWorkgroupId)) {
    		return KimApiServiceLocator.getGroupService().getGroup(exceptionWorkgroupId);
    	}
    	return null;
    }
    
    public boolean isExceptionGroupDefined() {
    	return getExceptionWorkgroupId() != null;
    }

    public String getExceptionWorkgroupId() {
        return exceptionWorkgroupId;
    }

    public void setExceptionWorkgroupId(String workgroupId) {
        this.exceptionWorkgroupId = workgroupId;
    }

    public void setFinalApprovalInd(Boolean finalApprovalInd) {
        this.finalApprovalInd = finalApprovalInd;
    }

    public void setMandatoryRouteInd(Boolean mandatoryRouteInd) {
        this.mandatoryRouteInd = mandatoryRouteInd;
    }

    public String getRouteMethodName() {
        return routeMethodName;
    }

    public void setRouteMethodName(String routeMethodName) {
        this.routeMethodName = routeMethodName;
    }

    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getRouteNodeId() {
        return routeNodeId;
    }

    public void setRouteNodeId(String routeNodeId) {
        this.routeNodeId = routeNodeId;
    }

    public String getRouteNodeName() {
        return routeNodeName;
    }

    public void setRouteNodeName(String routeLevelName) {
        this.routeNodeName = routeLevelName;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getRouteMethodCode() {
        return routeMethodCode;
    }

    public void setRouteMethodCode(String routeMethodCode) {
        this.routeMethodCode = routeMethodCode;
    }

	/**
	 * @param nextDocStatus the nextDocStatus to set
	 */
	public void setNextDocStatus(String nextDocStatus) {
		this.nextDocStatus = nextDocStatus;
	}

	/**
	 * @return the nextDocStatus
	 */
	public String getNextDocStatus() {
		return nextDocStatus;
	}
	
    public String getExceptionWorkgroupName() {
    	Group exceptionGroup = getExceptionWorkgroup();
        if (exceptionWorkgroupName == null || exceptionWorkgroupName.equals("")) {
            if (exceptionGroup != null) {
                return exceptionGroup.getName();
            }
        }
        return exceptionWorkgroupName;
    }

    public void setExceptionWorkgroupName(String exceptionWorkgroupName) {
        this.exceptionWorkgroupName = exceptionWorkgroupName;
    }

    public Integer getLockVerNbr() {
        return lockVerNbr;
    }

    public void setLockVerNbr(Integer lockVerNbr) {
        this.lockVerNbr = lockVerNbr;
    }

    public boolean isFlexRM() {
        return routeMethodCode != null && routeMethodCode.equals(KewApiConstants.ROUTE_LEVEL_FLEX_RM);
    }

    public boolean isRulesEngineNode() {
        return StringUtils.equals(routeMethodCode, KewApiConstants.ROUTE_LEVEL_RULES_ENGINE);
    }

    public boolean isPeopleFlowNode() {
        return StringUtils.equals(routeMethodCode, KewApiConstants.ROUTE_LEVEL_PEOPLE_FLOW);
    }
    
    public boolean isRoleNode() {
    	try {
    		return nodeType != null && NodeType.fromNode(this).isTypeOf(NodeType.ROLE);
    	} catch( ResourceUnavailableException ex ) {
    		Logger.getLogger( RouteNode.class ).info( "isRoleNode(): Unable to determine node type: " + ex.getMessage() );
    		return false;
    	}
    }

    public Boolean getFinalApprovalInd() {
        return finalApprovalInd;
    }

    public Boolean getMandatoryRouteInd() {
        return mandatoryRouteInd;
    }

    public void addNextNode(RouteNode nextNode) {
        getNextNodes().add(nextNode);
        nextNode.getPreviousNodes().add(this);
    }

    public List<RouteNode> getNextNodes() {
        return nextNodes;
    }

    public void setNextNodes(List<RouteNode> nextNodes) {
        this.nextNodes = nextNodes;
    }

    public List<RouteNode> getPreviousNodes() {
        return previousNodes;
    }

    public void setPreviousNodes(List<RouteNode> parentNodes) {
        this.previousNodes = parentNodes;
    }

    public RuleTemplateBo getRuleTemplate() {
        if (ruleTemplate == null) {
            RuleTemplateService ruleTemplateService = (RuleTemplateService) KEWServiceLocator.getService(KEWServiceLocator.RULE_TEMPLATE_SERVICE);
            ruleTemplate = ruleTemplateService.findByRuleTemplateName(getRouteMethodName());
        }
        return ruleTemplate;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public BranchPrototype getBranch() {
        return branch;
    }

    public void setBranch(BranchPrototype branch) {
        this.branch = branch;
    }

	//@PrePersist
	public void beforeInsert(){
		OrmUtils.populateAutoIncValue(this, KEWServiceLocator.getEntityManagerFactory().createEntityManager());
	}
	
	/**
	 * This overridden method ...
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RouteNode[routeNodeName="+routeNodeName+", nodeType="+nodeType+", activationType="+activationType+"]";
	}

    @Override
    public Long getVersionNumber() {
        if (lockVerNbr == null) {
            return null;
        }
        return Long.valueOf(lockVerNbr.longValue());
    }

    @Override
    public String getId() {
        if (routeNodeId == null) {
            return null;
        }
        return routeNodeId.toString();
    }

    @Override
    public String getName() {
        return getRouteNodeName();
    }

    @Override
    public boolean isFinalApproval() {
        if (finalApprovalInd == null) {
            return false;
        }
        return finalApprovalInd.booleanValue();
    }

    @Override
    public boolean isMandatory() {
        if (mandatoryRouteInd == null) {
            return false;
        }
        return mandatoryRouteInd.booleanValue();
    }

    @Override
    public String getExceptionGroupId() {
        return exceptionWorkgroupId;
    }

    @Override
    public String getType() {
        return nodeType;
    }

    @Override
    public String getBranchName() {
        if (branch == null) {
            return null;
        }
        return branch.getName();
    }

    @Override
    public String getNextDocumentStatus() {
        return nextDocStatus;
    }

    @Override
    public List<? extends RouteNodeConfigurationParameterContract> getConfigurationParameters() {
        return configParams;
    }

    @Override
    public List<String> getPreviousNodeIds() {
        List<String> previousNodeIds = new ArrayList<String>();
        if (previousNodes != null) {
            for (RouteNode previousNode : previousNodes) {
                previousNodeIds.add(previousNode.getRouteNodeId().toString());
            }
        }
        return previousNodeIds;
    }

    @Override
    public List<String> getNextNodeIds() {
        List<String> nextNodeIds = new ArrayList<String>();
        if (nextNodeIds != null) {
            for (RouteNode nextNode : nextNodes) {
                nextNodeIds.add(nextNode.getRouteNodeId().toString());
            }
        }
        return nextNodeIds;
    }
	
	

}
