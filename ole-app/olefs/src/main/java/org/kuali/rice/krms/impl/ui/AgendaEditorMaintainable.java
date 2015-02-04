/**
 * Copyright 2005-2012 The Kuali Foundation
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.ojb.broker.metadata.ClassNotPersistenceCapableException;
import org.kuali.rice.core.api.uif.DataType;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableTextInput;
import org.kuali.rice.core.api.util.tree.Node;
import org.kuali.rice.core.api.util.tree.Tree;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.web.form.MaintenanceDocumentForm;
import org.kuali.rice.krms.api.repository.term.TermResolverDefinition;
import org.kuali.rice.krms.api.repository.type.KrmsAttributeDefinition;
import org.kuali.rice.krms.impl.repository.*;
import org.kuali.rice.krms.impl.util.KrmsImplConstants;
import org.kuali.rice.krms.impl.util.KrmsRetriever;

import java.util.*;

/**
 * {@link org.kuali.rice.krad.maintenance.Maintainable} for the {@link org.kuali.rice.krms.impl.ui.AgendaEditor}
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class AgendaEditorMaintainable extends MaintainableImpl {

    private static final long serialVersionUID = 1L;

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(AgendaEditorMaintainable.class);

    public static final String NEW_AGENDA_EDITOR_DOCUMENT_TEXT = "New Agenda Editor Document";

    private transient SequenceAccessorService sequenceAccessorService;

    private transient KrmsRetriever krmsRetriever = new KrmsRetriever();

    /**
     * @return the boService
     */
    public BusinessObjectService getBoService() {
        return KRADServiceLocator.getBusinessObjectService();
    }

    /**
     * return the contextBoService
     */
    private ContextBoService getContextBoService() {
        return KrmsRepositoryServiceLocator.getContextBoService();
    }

    public List<RemotableAttributeField> retrieveAgendaCustomAttributes(View view, Object model, Container container) {
        AgendaEditor agendaEditor = getAgendaEditor(model);
        return krmsRetriever.retrieveAgendaCustomAttributes(agendaEditor);
    }

    /**
     * Retrieve a list of {@link org.kuali.rice.core.api.uif.RemotableAttributeField}s for the parameters (if any) required by the resolver for
     * the selected term in the proposition that is under edit.
     */
    public List<RemotableAttributeField> retrieveTermParameters(View view, Object model, Container container) {

        List<RemotableAttributeField> results = new ArrayList<RemotableAttributeField>();

        AgendaEditor agendaEditor = getAgendaEditor(model);

        // Figure out which rule is being edited
        RuleBo rule = agendaEditor.getAgendaItemLine().getRule();
        // Figure out which proposition is being edited
        Tree<RuleTreeNode, String> propositionTree = rule.getPropositionTree();
        Node<RuleTreeNode, String> editedPropositionNode = findEditedProposition(propositionTree.getRootElement());

        if (editedPropositionNode != null) {
            PropositionBo propositionBo = editedPropositionNode.getData().getProposition();
            if (StringUtils.isEmpty(propositionBo.getCompoundOpCode()) && CollectionUtils.size(propositionBo.getParameters()) > 0) {
                // Get the term ID; if it is a new parameterized term, it will have a special prefix
                PropositionParameterBo param = propositionBo.getParameters().get(0);
                if (param.getValue().startsWith(KrmsImplConstants.PARAMETERIZED_TERM_PREFIX)) {
                    String termSpecId = param.getValue().substring(KrmsImplConstants.PARAMETERIZED_TERM_PREFIX.length());
                    TermResolverDefinition simplestResolver = getSimplestTermResolver(termSpecId, rule.getNamespace());

                    // Get the parameters and build RemotableAttributeFields
                    if (simplestResolver != null) {
                        List<String> parameterNames = new ArrayList<String>(simplestResolver.getParameterNames());
                        Collections.sort(parameterNames); // make param order deterministic

                        for (String parameterName : parameterNames) {
                            // TODO: also allow for DD parameters if there are matching type attributes
                            RemotableTextInput.Builder controlBuilder = RemotableTextInput.Builder.create();
                            controlBuilder.setSize(64);

                            RemotableAttributeField.Builder builder = RemotableAttributeField.Builder.create(parameterName);

                            builder.setRequired(true);
                            builder.setDataType(DataType.STRING);
                            builder.setControl(controlBuilder);
                            builder.setLongLabel(parameterName);
                            builder.setShortLabel(parameterName);
                            builder.setMinLength(Integer.valueOf(1));
                            builder.setMaxLength(Integer.valueOf(64));

                            results.add(builder.build());
                        }
                    }
                }
            }
        }

        return results;
    }

    /**
     * finds the term resolver with the fewest parameters that resolves the given term specification
     * @param termSpecId the id of the term specification
     * @param namespace the  namespace of the term specification
     * @return the simples {@link org.kuali.rice.krms.api.repository.term.TermResolverDefinition} found, or null if none was found
     */
    // package access so that AgendaEditorController can use it too
    static TermResolverDefinition getSimplestTermResolver(String termSpecId,
            String namespace) {// Get the term resolver for the term spec

        List<TermResolverDefinition> resolvers =
                KrmsRepositoryServiceLocator.getTermBoService().findTermResolversByOutputId(
                        termSpecId, namespace);

        TermResolverDefinition simplestResolver = null;

        for (TermResolverDefinition resolver : resolvers) {
            if (simplestResolver == null ||
                    simplestResolver.getParameterNames().size() < resolver.getParameterNames().size()) {
                simplestResolver = resolver;
            }
        }

        return simplestResolver;
    }

    /**
     * Find and return the node containing the proposition that is in currently in edit mode
     * @param node the node to start searching from (typically the root)
     * @return the node that is currently being edited, if any.  Otherwise, null.
     */
    private Node<RuleTreeNode, String> findEditedProposition(Node<RuleTreeNode, String> node) {
        Node<RuleTreeNode, String> result = null;
        if (node.getData() != null && node.getData().getProposition() != null &&
                node.getData().getProposition().getEditMode()) {
            result = node;
        } else {
            for (Node<RuleTreeNode, String> child : node.getChildren()) {
                result = findEditedProposition(child);
                if (result != null) break;
            }
        }
        return result;
    }

    /**
     * Get the AgendaEditor out of the MaintenanceDocumentForm's newMaintainableObject
     * @param model the MaintenanceDocumentForm
     * @return the AgendaEditor
     */
    private AgendaEditor getAgendaEditor(Object model) {
        MaintenanceDocumentForm maintenanceForm = (MaintenanceDocumentForm)model;
        return (AgendaEditor)maintenanceForm.getDocument().getNewMaintainableObject().getDataObject();
    }

    public List<RemotableAttributeField> retrieveRuleActionCustomAttributes(View view, Object model, Container container) {
        AgendaEditor agendaEditor = getAgendaEditor((MaintenanceDocumentForm) model);
        return krmsRetriever.retrieveRuleActionCustomAttributes(agendaEditor);
    }

    /**
     *  This only supports a single action within a rule.
     */
    public List<RemotableAttributeField> retrieveRuleCustomAttributes(View view, Object model, Container container) {
        AgendaEditor agendaEditor = getAgendaEditor((MaintenanceDocumentForm) model);
        return krmsRetriever.retrieveRuleCustomAttributes(agendaEditor);
    }

    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {
        Object dataObject = null;

        try {
            // Since the dataObject is a wrapper class we need to build it and populate with the agenda bo.
            AgendaEditor agendaEditor = new AgendaEditor();
            AgendaBo agenda = getLookupService().findObjectBySearch(((AgendaEditor) getDataObject()).getAgenda().getClass(), dataObjectKeys);
            if (KRADConstants.MAINTENANCE_COPY_ACTION.equals(getMaintenanceAction())) {
                String dateTimeStamp = (new Date()).getTime() + "";
                String newAgendaName = AgendaItemBo.COPY_OF_TEXT + agenda.getName() + " " + dateTimeStamp;

                AgendaBo copiedAgenda = agenda.copyAgenda(newAgendaName, dateTimeStamp);

                document.getDocumentHeader().setDocumentDescription(NEW_AGENDA_EDITOR_DOCUMENT_TEXT);
                document.setFieldsClearedOnCopy(true);
                agendaEditor.setAgenda(copiedAgenda);
            } else {
                // set custom attributes map in AgendaEditor
                //                agendaEditor.setCustomAttributesMap(agenda.getAttributes());
                agendaEditor.setAgenda(agenda);
            }
            agendaEditor.setCustomAttributesMap(agenda.getAttributes());


            // set extra fields on AgendaEditor
            agendaEditor.setNamespace(agenda.getContext().getNamespace());
            agendaEditor.setContextName(agenda.getContext().getName());

            dataObject = agendaEditor;
        } catch (ClassNotPersistenceCapableException ex) {
            if (!document.getOldMaintainableObject().isExternalBusinessObject()) {
                throw new RuntimeException("Data Object Class: " + getDataObjectClass() +
                        " is not persistable and is not externalizable - configuration error");
            }
            // otherwise, let fall through
        }

        return dataObject;
    }

    /**
     *  Returns the sequenceAssessorService
     * @return {@link org.kuali.rice.krad.service.SequenceAccessorService}
     */
    private SequenceAccessorService getSequenceAccessorService() {
        if ( sequenceAccessorService == null ) {
            sequenceAccessorService = KRADServiceLocator.getSequenceAccessorService();
        }
        return sequenceAccessorService;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void processAfterNew(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterNew(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription(NEW_AGENDA_EDITOR_DOCUMENT_TEXT);
    }

    @Override
    public void processAfterEdit(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterEdit(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription("Modify Agenda Editor Document");
    }

    @Override
    public void prepareForSave() {
        // set agenda attributes
        AgendaEditor agendaEditor = (AgendaEditor) getDataObject();
        agendaEditor.getAgenda().setAttributes(agendaEditor.getCustomAttributesMap());
    }

    @Override
    public void saveDataObject() {
        AgendaBo agendaBo = ((AgendaEditor) getDataObject()).getAgenda();

        // handle saving new parameterized terms
        for (AgendaItemBo agendaItem : agendaBo.getItems()) {
            PropositionBo propositionBo = agendaItem.getRule().getProposition();
            if (propositionBo != null) {
                saveNewParameterizedTerms(propositionBo);
            }
        }

        if (agendaBo instanceof PersistableBusinessObject) {
        	        	
            Map<String,String> primaryKeys = new HashMap<String, String>();
            primaryKeys.put("id", agendaBo.getId());
            AgendaBo blah = getBusinessObjectService().findByPrimaryKey(AgendaBo.class, primaryKeys);
            
        	// need to be sure we delete the agenda tree from the top down in order to prevent violating
        	// any foreign key constraints, so do a pre-order traversal here on each node in the agenda tree
        	
        	preOrderTraversalDelete(getFirstAgendaItem(blah));
            
            getBusinessObjectService().delete(blah);

            getBusinessObjectService().linkAndSave(agendaBo);
        } else {
            throw new RuntimeException(
                    "Cannot save object of type: " + agendaBo + " with business object service");
        }
    }
    
    private AgendaItemBo getFirstAgendaItem(AgendaBo agenda) {
    	String firstItemId = agenda.getFirstItemId();
    	if (firstItemId == null) {
    		return null;
    	}
    	for (AgendaItemBo item : agenda.getItems()) {
    		if (item.getId().equals(firstItemId)) {
    			return item;
    		}
    	}
    	throw new IllegalStateException("Failed to locate the first agenda item on the agenda with an id of " + firstItemId + ", agenda id is " + agenda.getId());
    }
    
    private void preOrderTraversalDelete(AgendaItemBo agendaItem) {
    	if (agendaItem == null) {
    		return;
    	}
    	getBusinessObjectService().delete(agendaItem);
    	if (agendaItem.getWhenFalse() != null) {
			preOrderTraversalDelete(agendaItem.getWhenFalse());
		}
    	if (agendaItem.getWhenTrue() != null) {
    		preOrderTraversalDelete(agendaItem.getWhenTrue());
    	}    	
    	preOrderTraversalDelete(agendaItem.getAlways());
    }
        
    /**
     * walk the proposition tree and save any new parameterized terms that are contained therein
     * @param propositionBo the root proposition from which to search
     */
    private void saveNewParameterizedTerms(PropositionBo propositionBo) {
        if (StringUtils.isBlank(propositionBo.getCompoundOpCode())) {
            // it is a simple proposition
            String termId = propositionBo.getParameters().get(0).getValue();
            if (termId.startsWith(KrmsImplConstants.PARAMETERIZED_TERM_PREFIX)) {
                String termSpecId = termId.substring(KrmsImplConstants.PARAMETERIZED_TERM_PREFIX.length());
                // create new term
                TermBo newTerm = new TermBo();
                newTerm.setDescription(propositionBo.getNewTermDescription());
                newTerm.setSpecificationId(termSpecId);
                newTerm.setId(KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRMS_TERM_S").toString());

                List<TermParameterBo> params = new ArrayList<TermParameterBo>();
                for (Map.Entry<String, String> entry : propositionBo.getTermParameters().entrySet()) {
                    TermParameterBo param = new TermParameterBo();
                    param.setTermId(newTerm.getId());
                    param.setName(entry.getKey());
                    param.setValue(entry.getValue());
                    param.setId(KRADServiceLocator.getSequenceAccessorService().getNextAvailableSequenceNumber("KRMS_TERM_PARM_S").toString());

                    params.add(param);
                }

                newTerm.setParameters(params);

                KRADServiceLocator.getBusinessObjectService().linkAndSave(newTerm);
                propositionBo.getParameters().get(0).setValue(newTerm.getId());
            }
        } else {
            // recurse
            for (PropositionBo childProp : propositionBo.getCompoundComponents()) {
                saveNewParameterizedTerms(childProp);
            }
        }
    }

    /**
     * Build a map from attribute name to attribute definition from all the defined attribute definitions for the
     * specified agenda type
     * @param agendaTypeId
     * @return
     */
    private Map<String, KrmsAttributeDefinition> buildAttributeDefinitionMap(String agendaTypeId) {
        KrmsAttributeDefinitionService attributeDefinitionService = KrmsRepositoryServiceLocator.getKrmsAttributeDefinitionService();

        // build a map from attribute name to definition
        Map<String, KrmsAttributeDefinition> attributeDefinitionMap = new HashMap<String, KrmsAttributeDefinition>();

        List<KrmsAttributeDefinition> attributeDefinitions =
                attributeDefinitionService.findAttributeDefinitionsByType(agendaTypeId);

        for (KrmsAttributeDefinition attributeDefinition : attributeDefinitions) {
            attributeDefinitionMap.put(attributeDefinition.getName(), attributeDefinition);
        }
        return attributeDefinitionMap;
    }

    @Override
    public boolean isOldDataObjectInDocument() {
        boolean isOldDataObjectInExistence = true;

        if (getDataObject() == null) {
            isOldDataObjectInExistence = false;
        } else {
            // dataObject contains a non persistable wrapper - use agenda from the wrapper object instead
            Map<String, ?> keyFieldValues = getDataObjectMetaDataService().getPrimaryKeyFieldValues(((AgendaEditor) getDataObject()).getAgenda());
            for (Object keyValue : keyFieldValues.values()) {
                if (keyValue == null) {
                    isOldDataObjectInExistence = false;
                } else if ((keyValue instanceof String) && StringUtils.isBlank((String) keyValue)) {
                    isOldDataObjectInExistence = false;
                }

                if (!isOldDataObjectInExistence) {
                    break;
                }
            }
        }

        return isOldDataObjectInExistence;
    }

    // Since the dataObject is a wrapper class we need to return the agendaBo instead.
    @Override
    public Class getDataObjectClass() {
        return AgendaBo.class;
    }

    @Override
    protected void processBeforeAddLine(View view, CollectionGroup collectionGroup, Object model, Object addLine) {
        AgendaEditor agendaEditor = getAgendaEditor(model);
        if (addLine instanceof ActionBo) {
            ((ActionBo) addLine).setNamespace(agendaEditor.getAgendaItemLine().getRule().getNamespace());
        }

        super.processBeforeAddLine(view, collectionGroup, model, addLine);
    }
}
