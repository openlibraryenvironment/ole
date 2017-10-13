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
package org.kuali.rice.krms.impl.ui;

import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krms.impl.repository.ActionBo;
import org.kuali.rice.krms.impl.repository.AgendaBo;
import org.kuali.rice.krms.impl.repository.AgendaItemBo;
import org.kuali.rice.krms.impl.repository.ContextBo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * synthetic (not directly persisted) BO for the KRMS agenda editor
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class AgendaEditor extends PersistableBusinessObjectBase {
	
	private static final long serialVersionUID = 1L;
	
	private ContextBo context;
    private String namespace;
	private AgendaBo agenda;
    private String contextName;
	private AgendaItemBo agendaItemLine;
    private ActionBo agendaItemLineRuleAction;
    private String selectedAgendaItemId;
    private String cutAgendaItemId;
    private String selectedPropositionId;
    private String cutPropositionId;
    private String copyRuleName;
    private String oldContextId;
    private String ruleEditorMessage;
    private boolean addRuleInProgress = false;
    private boolean disableButtons = false;
    private Map<String, String> customAttributesMap = new HashMap<String, String>();
    private Map<String, String> customRuleAttributesMap = new HashMap<String, String>();
    private Map<String, String> customRuleActionAttributesMap = new HashMap<String, String>();

    public AgendaEditor() {
        agenda = new AgendaBo();
        // ToDo: Determine proper default values of agenda's typeId
        agenda.setTypeId("");
        agendaItemLine = new AgendaItemBo();
        agendaItemLineRuleAction = new ActionBo();
    }

	private void addAgendaItemAndChildren(Node<AgendaTreeNode, String> parent, AgendaItemBo agendaItem) {
	    Node<AgendaTreeNode, String> child = new Node<AgendaTreeNode, String>();
	    child.setNodeLabel(agendaItem.getRuleText());
	    child.setNodeType("agendaNode ruleNode");
	    child.setData(new AgendaTreeRuleNode(agendaItem));
	    parent.getChildren().add(child);
	    
	    // deal with peer nodes:
	    if (agendaItem.getAlways() != null) addAgendaItemAndChildren(parent, agendaItem.getAlways());
	    
	    // deal with child nodes:
	    if (agendaItem.getWhenTrue() != null) {
	        Node<AgendaTreeNode, String> whenTrue = new Node<AgendaTreeNode, String>();
	        whenTrue.setNodeLabel("When TRUE");
	        whenTrue.setNodeType("agendaNode logicNode whenTrueNode");
	        whenTrue.setData(new AgendaTreeLogicNode());
	        child.getChildren().add(whenTrue);
	        
	        addAgendaItemAndChildren(whenTrue, agendaItem.getWhenTrue());
	    }
        if (agendaItem.getWhenFalse() != null) {
            Node<AgendaTreeNode, String> whenFalse = new Node<AgendaTreeNode, String>();
            whenFalse.setNodeLabel("When FALSE");
            whenFalse.setNodeType("agendaNode logicNode whenFalseNode");
            whenFalse.setData(new AgendaTreeLogicNode());
            child.getChildren().add(whenFalse);
            
            addAgendaItemAndChildren(whenFalse, agendaItem.getWhenFalse());
        }
	}
	
    public Tree<? extends AgendaTreeNode, String> getAgendaRuleTree() {
        Tree<AgendaTreeNode, String> agendaTree = new Tree<AgendaTreeNode, String>();

        Node<AgendaTreeNode, String> rootNode = new Node<AgendaTreeNode, String>();
        agendaTree.setRootElement(rootNode);
        
        if (agenda != null) {
            String firstItemId = agenda.getFirstItemId();
            List<AgendaItemBo> items = agenda.getItems();
            AgendaItemBo firstItem = null;

            if (items != null && firstItemId != null) {
                for (AgendaItemBo item : items) {
                    if (firstItemId.equals(item.getId())) {
                        firstItem = item;
                        break;
                    }
                }
            }

            if (firstItem != null) addAgendaItemAndChildren(rootNode, firstItem);
        } 
        
        return agendaTree;
    }	
	
	/**
     * @return the agendaItemLine
     */
    public AgendaItemBo getAgendaItemLine() {
        return this.agendaItemLine;
    }

    /**
     * @param agendaItemLine the agendaItemLine to set
     */
    public void setAgendaItemLine(AgendaItemBo agendaItemLine) {
        this.agendaItemLine = agendaItemLine;
    }

    public ActionBo getAgendaItemLineRuleAction() {
        return agendaItemLineRuleAction;
    }

    public void setAgendaItemLineRuleAction(ActionBo actionBo) {
        this.agendaItemLineRuleAction = actionBo;
    }

    /**
     * @return the selectedAgendaItemId
     */
    public String getSelectedAgendaItemId() {
        return this.selectedAgendaItemId;
    }

    /**
     * @param selectedAgendaItemId the selectedAgendaItemId to set
     */
    public void setSelectedAgendaItemId(String selectedAgendaItemId) {
        this.selectedAgendaItemId = selectedAgendaItemId;
    }

    /**
     * @return the cutAgendaItemId
     */
    public String getCutAgendaItemId() {
        return this.cutAgendaItemId;
    }

    /**
     * @param cutAgendaItemId the cutAgendaItemId to set
     */
    public void setCutAgendaItemId(String cutAgendaItemId) {
        this.cutAgendaItemId = cutAgendaItemId;
    }

    /**
	 * @return the context
	 */
	public ContextBo getContext() {
		return this.context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(ContextBo context) {
		this.context = context;
	}

	/**
	 * @return the agenda
	 */
	public AgendaBo getAgenda() {
		return this.agenda;
	}

	/**
	 * @param agenda the agenda to set
	 */
	public void setAgenda(AgendaBo agenda) {
		this.agenda = agenda;
	}

    public Map<String, String> getCustomAttributesMap() {
        return customAttributesMap;
    }

    public void setCustomAttributesMap(Map<String, String> customAttributesMap) {
        this.customAttributesMap = customAttributesMap;
    }

    public Map<String, String> getCustomRuleAttributesMap() {
        return customRuleAttributesMap;
    }

    public void setCustomRuleAttributesMap(Map<String, String> customRuleAttributesMap) {
        this.customRuleAttributesMap = customRuleAttributesMap;
    }

    public Map<String, String> getCustomRuleActionAttributesMap() {
        return customRuleActionAttributesMap;
    }

    public void setCustomRuleActionAttributesMap(Map<String, String> customRuleActionAttributesMap) {
        this.customRuleActionAttributesMap = customRuleActionAttributesMap;
    }

    /**
     * @param copyRuleName the rule name from which to copy
     */
    public void setCopyRuleName(String copyRuleName) {
        this.copyRuleName = copyRuleName;
    }

    /**
     * @return the rule name from which to copy
     */
    public String getCopyRuleName() {
        return copyRuleName;
    }

    /**
     * @param oldContextId the contextId that the agenda currently has (i.e. before the next context change)
     */
    public void setOldContextId(String oldContextId) {
        this.oldContextId = oldContextId;
    }

    /**
     * @return the contextId that the agenda had before the context change
     */
    public String getOldContextId() {
        return oldContextId;
    }

    /**
     * @return the selectedPropositionId
     */
    public String getSelectedPropositionId() {
        return this.selectedPropositionId;
    }

    /**
     * @param selectedPropositionId the selectedPropositionId to set
     */
    public void setSelectedPropositionId(String selectedPropositionId) {
        this.selectedPropositionId = selectedPropositionId;
    }


    /**
     * @return the cutPropositionId
     */
    public String getCutPropositionId() {
        return cutPropositionId;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getRuleEditorMessage() {
        return this.ruleEditorMessage;
    }

    public void setRuleEditorMessage(String message) {
        this.ruleEditorMessage = message;
    }

    public boolean isAddRuleInProgress() {
        return addRuleInProgress;
    }

    public void setAddRuleInProgress(boolean addRuleInProgress) {
        this.addRuleInProgress = addRuleInProgress;
    }

    /**
     *
     * @return if the tree buttons should be disabled
     */
    public boolean isDisableButtons() {
        return disableButtons;
    }

    /**
     * Setter for disableButtons. Set to true when the Agenda is submitted
     *
     * @param disableButtons
     */
    public void setDisableButtons(boolean disableButtons) {
        this.disableButtons = disableButtons;
    }

    /**
     * @param cutPropositionId the cutPropositionId to set
     */
    public void setCutPropositionId(String cutPropositionId) {
        this.cutPropositionId = cutPropositionId;
    }

    // Need to override this method since the actual persistable BO is wrapped inside dataObject.
    @Override
    public void refreshNonUpdateableReferences() {
        getPersistenceService().refreshAllNonUpdatingReferences(this.getAgenda());
    }

    /**
     * @see org.kuali.rice.krad.bo.BusinessObject#refresh()
     */
    @Override
    public void refresh() {
        getPersistenceService().retrieveNonKeyFields(this.getAgenda());
    }
}
