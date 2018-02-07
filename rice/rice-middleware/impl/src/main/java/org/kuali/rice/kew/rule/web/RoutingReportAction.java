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
package org.kuali.rice.kew.rule.web;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.action.ActionRequestStatus;
import org.kuali.rice.kew.doctype.bo.DocumentType;
import org.kuali.rice.kew.doctype.service.DocumentTypeService;
import org.kuali.rice.kew.engine.ActivationContext;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.RouteNode;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.routeheader.AttributeDocumentContent;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.routelog.web.RouteLogAction;
import org.kuali.rice.kew.routelog.web.RouteLogForm;
import org.kuali.rice.kew.rule.FlexRM;
import org.kuali.rice.kew.rule.WorkflowRuleAttribute;
import org.kuali.rice.kew.rule.WorkflowRuleAttributeRows;
import org.kuali.rice.kew.rule.bo.RuleAttribute;
import org.kuali.rice.kew.rule.bo.RuleTemplateAttributeBo;
import org.kuali.rice.kew.rule.bo.RuleTemplateBo;
import org.kuali.rice.kew.rule.service.RuleTemplateService;
import org.kuali.rice.kew.rule.xmlrouting.GenericXMLRuleAttribute;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.util.GlobalVariables;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A Struts Action for executing routing reports and retrieving the results.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class RoutingReportAction extends KewKualiAction {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RoutingReportAction.class);

	public static final String DOC_TYPE_REPORTING = "documentType";
	public static final String TEMPLATE_REPORTING = "template";

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        this.initiateForm(request, form);
        RoutingReportForm routingForm = (RoutingReportForm)form;
        if (org.apache.commons.lang.StringUtils.isEmpty(routingForm.getDateRef())) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            routingForm.setEffectiveHour("5");
            routingForm.setEffectiveMinute("0");
            routingForm.setAmPm("1");
            routingForm.setDateRef(sdf.format(new Date()));
            routingForm.setReportType(TEMPLATE_REPORTING);
        }
        if (DOC_TYPE_REPORTING.equals(routingForm.getReportType())) {
            if (org.apache.commons.lang.StringUtils.isEmpty(routingForm.getDocumentTypeParam())) {
                throw new RuntimeException("No document type was given");
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(routingForm.getInitiatorPrincipalId())) {
                throw new RuntimeException("No initiator principal id was given");
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(routingForm.getDocumentContent())) {
                throw new RuntimeException("No document content was given");
            }
        } else if (!(TEMPLATE_REPORTING.equals(routingForm.getReportType()))) {
            // report type is not Document Type or Template Type... error out
            throw new RuntimeException("The Routing Report type is not set");
        }
        return super.execute(mapping, form, request, response);
    }

	public ActionForward calculateRoute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RoutingReportForm routingForm = (RoutingReportForm) form;

        // this is never actually used??
		List<RemotableAttributeError> errors = new ArrayList<RemotableAttributeError>();

		if (getDocumentTypeService().findByName(routingForm.getDocumentType()) == null) {
		    GlobalVariables.getMessageMap().putError("Document type is required.", "doctype.documenttypeservice.doctypename.required");
		}
		Timestamp date = null;
		if (!org.apache.commons.lang.StringUtils.isEmpty(routingForm.getDateRef())) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(routingForm.getDateRef()));
				calendar.set(Calendar.HOUR, Integer.parseInt(routingForm.getEffectiveHour()));
				calendar.set(Calendar.MINUTE, Integer.parseInt(routingForm.getEffectiveMinute()));
				calendar.set(Calendar.AM_PM, Integer.parseInt(routingForm.getAmPm()));
				date = new Timestamp(calendar.getTimeInMillis());
			} catch (Exception e) {
				LOG.error("error parsing date", e);
				GlobalVariables.getMessageMap().putError("Invalid date.", "routereport.effectiveDate.invalid");
			}
		}

		if (!GlobalVariables.getMessageMap().hasNoErrors()) {
            throw new ValidationException("Errors populating rule attributes.");
        }

		DocumentTypeService documentTypeService = (DocumentTypeService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE);
		DocumentType docType = documentTypeService.findByName(routingForm.getDocumentType());

		DocumentRouteHeaderValue routeHeader = new DocumentRouteHeaderValue();
		routeHeader.setDocumentId("");
		routeHeader.setDocumentTypeId(docType.getDocumentTypeId());
		routeHeader.setDocRouteLevel(new Integer(0));
        routeHeader.setDocVersion(new Integer(KewApiConstants.DocumentContentVersions.CURRENT));

        List<RouteReportRuleTemplateContainer> ruleTemplateContainers = new ArrayList<RouteReportRuleTemplateContainer>();
		if (routingForm.getReportType().equals(DOC_TYPE_REPORTING)) {

          List routeNodes = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(docType, true);
			for (Iterator iter = routeNodes.iterator(); iter.hasNext();) {
                RouteNode routeNode = (RouteNode) iter.next();
				if (routeNode.isFlexRM()) {
					RuleTemplateBo ruleTemplate = getRuleTemplateService().findByRuleTemplateName(routeNode.getRouteMethodName());
					if (ruleTemplate != null) {
					    ruleTemplateContainers.add(new RouteReportRuleTemplateContainer(ruleTemplate, routeNode));
						if (ruleTemplate.getDelegationTemplate() != null) {
						    ruleTemplateContainers.add(new RouteReportRuleTemplateContainer(ruleTemplate.getDelegationTemplate(), routeNode));
						}
					}
				}
			}
		} else {
			RuleTemplateBo ruleTemplate = getRuleTemplateService().findByRuleTemplateId(routingForm.getRuleTemplateId());
			RouteNode routeNode = new RouteNode();
			routeNode.setRouteNodeName(ruleTemplate.getName());
			ruleTemplateContainers.add(new RouteReportRuleTemplateContainer(ruleTemplate, routeNode));
			if (ruleTemplate.getDelegationTemplate() != null) {
			    ruleTemplateContainers.add(new RouteReportRuleTemplateContainer(ruleTemplate.getDelegationTemplate(), routeNode));
			}
		}

        String xmlDocumentContent = routingForm.getDocumentContent();
        if (routingForm.getReportType().equals(TEMPLATE_REPORTING)) {
            List<WorkflowRuleAttribute> attributes = new ArrayList<WorkflowRuleAttribute>();
            for (RouteReportRuleTemplateContainer ruleTemplateContainer : ruleTemplateContainers) {
                RuleTemplateBo ruleTemplate = ruleTemplateContainer.ruleTemplate;
                for (RuleTemplateAttributeBo ruleTemplateAttribute : ruleTemplate.getActiveRuleTemplateAttributes()) {
                    if (!ruleTemplateAttribute.isWorkflowAttribute()) {
                        continue;
                    }
                    WorkflowRuleAttribute workflowAttribute = ruleTemplateAttribute.getWorkflowAttribute();

                    RuleAttribute ruleAttribute = ruleTemplateAttribute.getRuleAttribute();
                    if (ruleAttribute.getType().equals(KewApiConstants.RULE_XML_ATTRIBUTE_TYPE)) {
                        ((GenericXMLRuleAttribute) workflowAttribute).setExtensionDefinition(RuleAttribute.to(ruleAttribute));
                    }
                    List<RemotableAttributeError> attValidationErrors = workflowAttribute.validateRoutingData(routingForm.getFields());
                    if (attValidationErrors != null && !attValidationErrors.isEmpty()) {
                        errors.addAll(attValidationErrors);
                    }
                    attributes.add(workflowAttribute);
                }
            }

            if (!GlobalVariables.getMessageMap().hasNoErrors()) {
                throw new ValidationException("errors in search criteria");
            }

            DocumentContent docContent = new AttributeDocumentContent(attributes);
            xmlDocumentContent = docContent.getDocContent();
        }

		routeHeader.setDocContent(xmlDocumentContent);
		routeHeader.setInitiatorWorkflowId(getUserSession(request).getPrincipalId());
		routeHeader.setDocRouteStatus(KewApiConstants.ROUTE_HEADER_INITIATED_CD);
		routeHeader.setDocTitle("Routing Report");
		routeHeader.setRoutingReport(true);
		long magicCounter = 0;

		FlexRM flexRM = new FlexRM(date);

		int numberOfRules = 0;
		int numberOfActionRequests = 0;
		Set<String> alreadyProcessedRuleTemplateNames = new HashSet<String>();
		for (Object element : ruleTemplateContainers) {
			// initialize the RouteContext
		    RouteContext context = RouteContext.createNewRouteContext();
		context.setActivationContext(new ActivationContext(ActivationContext.CONTEXT_IS_SIMULATION));
			try {
			    RouteReportRuleTemplateContainer ruleTemplateContainer = (RouteReportRuleTemplateContainer) element;
				RuleTemplateBo ruleTemplate = ruleTemplateContainer.ruleTemplate;
				RouteNode routeLevel = ruleTemplateContainer.routeNode;

				if (!alreadyProcessedRuleTemplateNames.contains(ruleTemplate.getName())) {
				    alreadyProcessedRuleTemplateNames.add(ruleTemplate.getName());
    				List<ActionRequestValue> actionRequests = flexRM.getActionRequests(routeHeader, routeLevel, null, ruleTemplate.getName());

    				numberOfActionRequests += actionRequests.size();
    				numberOfRules += flexRM.getNumberOfMatchingRules();

    				magicCounter = populateActionRequestsWithRouteLevelInformationAndIterateMagicCounter(routeLevel, actionRequests, magicCounter);
    				//routeHeader.getActionRequests().addAll(actionRequests);
    				routeHeader.getSimulatedActionRequests().addAll(actionRequests);
				}
			} finally {
				RouteContext.clearCurrentRouteContext();
			}
		}

		if (numberOfActionRequests == 0) {
			if (numberOfRules == 0) {
			    GlobalVariables.getMessageMap().putError("*", "routereport.noRules");
			} else {
			    GlobalVariables.getMessageMap().putError("*", "routereport.noMatchingRules");
			}
			if (GlobalVariables.getMessageMap().hasErrors()) {
	            throw new ValidationException("errors in search criteria");
	        }
		}


		// PROBLEM HERE!!!!
		RouteLogForm routeLogForm = new RouteLogForm();
		routeLogForm.setShowFuture(true);
        if (StringUtils.isNotBlank(routingForm.getBackUrl())) {
            routeLogForm.setReturnUrlLocation(routingForm.getBackUrl());
        }
        LOG.debug("Value of getDisplayCloseButton " + routingForm.getShowCloseButton());
        LOG.debug("Value of isDisplayCloseButton " + routingForm.isDisplayCloseButton());
        routeLogForm.setShowCloseButton(routingForm.isDisplayCloseButton());
		request.setAttribute("routeHeader", routeHeader);
		new RouteLogAction().populateRouteLogFormActionRequests(routeLogForm, routeHeader);
		request.setAttribute("KualiForm", routeLogForm);
		//END PROBLEM AREA

		//return mapping.findForward("basic");
		return mapping.findForward("routeLog");
	}

	private class RouteReportRuleTemplateContainer {
	    public RuleTemplateBo ruleTemplate = null;
	    public RouteNode routeNode = null;
	    public RouteReportRuleTemplateContainer(RuleTemplateBo template, RouteNode node) {
	        this.ruleTemplate = template;
	        this.routeNode = node;
	    }
	}

	public long populateActionRequestsWithRouteLevelInformationAndIterateMagicCounter(RouteNode routeLevel, List<ActionRequestValue> actionRequests, long magicCounter) {

		for (ActionRequestValue actionRequest : actionRequests) {
			populateActionRequestsWithRouteLevelInformationAndIterateMagicCounter(routeLevel, actionRequest.getChildrenRequests(), magicCounter);
			actionRequest.setStatus(ActionRequestStatus.INITIALIZED.getCode());
//			actionRequest.setRouteMethodName(routeLevel.getRouteMethodName());
			RouteNodeInstance routeNode = new RouteNodeInstance();
			routeNode.setRouteNode(routeLevel);
			actionRequest.setNodeInstance(routeNode);
			actionRequest.setRouteLevel(new Integer(0));
			magicCounter++;
			actionRequest.setActionRequestId(String.valueOf(magicCounter));
		}
		return magicCounter;
	}

	@Override
	public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
	    return mapping.findForward("basic");
	}

	private ActionMessages initiateForm(HttpServletRequest request, ActionForm form) throws Exception {
        RoutingReportForm routingReportForm = (RoutingReportForm) form;
        if (routingReportForm.getReportType() == null) {
            // no report type means we must check for potential setup
            if ( (!org.apache.commons.lang.StringUtils.isEmpty(routingReportForm.getDocumentTypeParam())) ||
                 (!org.apache.commons.lang.StringUtils.isEmpty(routingReportForm.getInitiatorPrincipalId())) ||
                 (!org.apache.commons.lang.StringUtils.isEmpty(routingReportForm.getDocumentContent())) ) {
                // at least one parameter was passed... attempt to use Doc Type Report
                routingReportForm.setReportType(DOC_TYPE_REPORTING);
            } else {
                // no parameters passed... default to Template Type Rreport
                routingReportForm.setReportType(TEMPLATE_REPORTING);
            }
        }

        if (routingReportForm.getReportType().equals(DOC_TYPE_REPORTING)) {
            if (org.apache.commons.lang.StringUtils.isEmpty(routingReportForm.getDocumentTypeParam())) {
                throw new RuntimeException("Document Type was not given");
            } else {
                DocumentType docType = getDocumentTypeService().findByName(routingReportForm.getDocumentTypeParam());
                if (docType == null) {
                    throw new RuntimeException("Document Type is invalid");
                }
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(routingReportForm.getInitiatorPrincipalId())) {
                throw new RuntimeException("Initiator Principal ID was not given");
            } else {
                KEWServiceLocator.getIdentityHelperService().getPrincipal(routingReportForm.getInitiatorPrincipalId());
            }
            if (org.apache.commons.lang.StringUtils.isEmpty(routingReportForm.getDocumentContent())) {
                throw new RuntimeException("Document Content was not given");
            }

            if (!org.apache.commons.lang.StringUtils.isEmpty(routingReportForm.getDocumentType())) {
                DocumentType docType = getDocumentTypeService().findByName(routingReportForm.getDocumentType());
                if (docType == null) {
                    throw new RuntimeException("Document Type is missing or invalid");
                }
                routingReportForm.getRuleTemplateAttributes().clear();
                List<RouteNode> routeNodes = KEWServiceLocator.getRouteNodeService().getFlattenedNodes(docType, true);
                for (RouteNode routeNode : routeNodes) {
                    if (routeNode.isFlexRM()) {
                        RuleTemplateBo ruleTemplate = getRuleTemplateService().findByRuleTemplateName(routeNode.getRouteMethodName());
                        if (ruleTemplate != null) {
                            loadRuleTemplateOnForm(ruleTemplate, routingReportForm, request, false);
                            if (ruleTemplate.getDelegationTemplate() != null) {
                                loadRuleTemplateOnForm(ruleTemplate.getDelegationTemplate(), routingReportForm, request, true);
                            }
                        }
                    }
                }
            }
//          routingReportForm.setShowFields(true);
        } else if (routingReportForm.getReportType().equals(TEMPLATE_REPORTING)) {
            routingReportForm.setRuleTemplates(getRuleTemplateService().findAll());
            if (routingReportForm.getRuleTemplateId() != null) {
                RuleTemplateBo ruleTemplate = getRuleTemplateService().findByRuleTemplateId(routingReportForm.getRuleTemplateId());
                routingReportForm.getRuleTemplateAttributes().clear();
                if (ruleTemplate != null) {
                    loadRuleTemplateOnForm(ruleTemplate, routingReportForm, request, false);
                    if (ruleTemplate.getDelegationTemplate() != null) {
                        loadRuleTemplateOnForm(ruleTemplate.getDelegationTemplate(), routingReportForm, request, true);
                    }
                }
            }
        }
        return null;
	}

	private void loadRuleTemplateOnForm(RuleTemplateBo ruleTemplate, RoutingReportForm routingReportForm, HttpServletRequest request, boolean isDelegate) {

		Map<String, String> fieldValues = new HashMap<String, String>();

		List<RuleTemplateAttributeBo> ruleTemplateAttributes = ruleTemplate.getActiveRuleTemplateAttributes();
		Collections.sort(ruleTemplateAttributes);

		List<Row> rows = new ArrayList<Row>();
		for (RuleTemplateAttributeBo ruleTemplateAttribute : ruleTemplateAttributes) {
			if (!ruleTemplateAttribute.isWorkflowAttribute()) {
				continue;
			}
			
            WorkflowRuleAttributeRows workflowRuleAttributeRows =
                    KEWServiceLocator.getWorkflowRuleAttributeMediator().getRoutingDataRows(fieldValues, ruleTemplateAttribute);

            for (Row row : workflowRuleAttributeRows.getRows()) {

				List<Field> fields = new ArrayList<Field>();
				for (Object element2 : row.getFields()) {
					Field field = (Field) element2;
					if (request.getParameter(field.getPropertyName()) != null) {
						field.setPropertyValue(request.getParameter(field.getPropertyName()));
					} else if (routingReportForm.getFields() != null && !routingReportForm.getFields().isEmpty()) {
						field.setPropertyValue((String) routingReportForm.getFields().get(field.getPropertyName()));
					}
					fields.add(field);
					fieldValues.put(field.getPropertyName(), field.getPropertyValue());
				}
			}
			            
			for (Row row : workflowRuleAttributeRows.getRows())
			{
				List<Field> fields = new ArrayList<Field>();
				List<Field> rowFields = row.getFields();
				for (Field field : rowFields )
				{
					if (request.getParameter(field.getPropertyName()) != null) {
						field.setPropertyValue(request.getParameter(field.getPropertyName()));
					} else if (routingReportForm.getFields() != null && !routingReportForm.getFields().isEmpty()) {
						field.setPropertyValue((String) routingReportForm.getFields().get(field.getPropertyName()));
					}
					fields.add(field);
					fieldValues.put(field.getPropertyName(), field.getPropertyValue());
				}
				row.setFields(fields);
				rows.add(row);

			}
		}

		routingReportForm.getFields().putAll(fieldValues);
		routingReportForm.getRuleTemplateAttributes().addAll(rows);
		routingReportForm.setShowFields(true);
		routingReportForm.setShowViewResults(true);
	}

	public ActionForward loadTemplate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		RoutingReportForm routingReportForm = (RoutingReportForm) form;
		if (org.apache.commons.lang.StringUtils.isEmpty(routingReportForm.getDateRef())) {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			routingReportForm.setEffectiveHour("5");
			routingReportForm.setEffectiveMinute("0");
			routingReportForm.setAmPm("1");
			routingReportForm.setDateRef(sdf.format(new Date()));
		}
		return mapping.findForward("basic");
	}

	private RuleTemplateService getRuleTemplateService() {
		return (RuleTemplateService) KEWServiceLocator.getService(KEWServiceLocator.RULE_TEMPLATE_SERVICE);
	}

	private DocumentTypeService getDocumentTypeService() {
		return (DocumentTypeService) KEWServiceLocator.getService(KEWServiceLocator.DOCUMENT_TYPE_SERVICE);
	}

	private UserSession getUserSession(HttpServletRequest request) {
	    return GlobalVariables.getUserSession();
	}




}
