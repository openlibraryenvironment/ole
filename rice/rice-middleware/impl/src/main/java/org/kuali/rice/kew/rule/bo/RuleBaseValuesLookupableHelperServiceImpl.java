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
package org.kuali.rice.kew.rule.bo;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.uif.RemotableAttributeError;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.core.web.format.BooleanFormatter;
import org.kuali.rice.core.web.format.CollectionFormatter;
import org.kuali.rice.core.web.format.DateFormatter;
import org.kuali.rice.core.web.format.Formatter;
import org.kuali.rice.kew.api.KewApiServiceLocator;
import org.kuali.rice.kew.api.rule.RuleTemplate;
import org.kuali.rice.kew.api.rule.RuleTemplateAttribute;
import org.kuali.rice.kew.lookupable.MyColumns;
import org.kuali.rice.kew.rule.RuleBaseValues;
import org.kuali.rice.kew.rule.WorkflowRuleAttributeRows;
import org.kuali.rice.kew.rule.service.RuleServiceInternal;
import org.kuali.rice.kew.rule.service.RuleTemplateService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.document.authorization.BusinessObjectRestrictions;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;
import org.kuali.rice.kns.lookup.LookupableHelperService;
import org.kuali.rice.kns.service.KNSServiceLocator;
import org.kuali.rice.kns.web.comparator.CellComparatorHelper;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.kns.web.ui.Column;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.ResultRow;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This is a description of what this class does - jjhanso don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RuleBaseValuesLookupableHelperServiceImpl extends KualiLookupableHelperServiceImpl {
    private List<Row> rows = new ArrayList<Row>();
    //private List<Column> columns = establishColumns();
    //private Long previousRuleTemplateId;
    private LookupableHelperService ruleDelegationLookupableHelperService;
    private List<?> delegationPkNames;

    private static final String RULE_TEMPLATE_PROPERTY_NAME = "ruleTemplate.name";
    private static final String RULE_ID_PROPERTY_NAME = "id";
    private static final String RULE_TEMPLATE_ID_PROPERTY_NAME = "ruleTemplateId";
    private static final String ACTIVE_IND_PROPERTY_NAME = "active";
    private static final String DELEGATE_RULE_PROPERTY_NAME = "delegateRule";
    private static final String GROUP_REVIEWER_PROPERTY_NAME = "groupReviewer";
    private static final String GROUP_REVIEWER_NAME_PROPERTY_NAME = "groupReviewerName";
    private static final String GROUP_REVIEWER_NAMESPACE_PROPERTY_NAME = "groupReviewerNamespace";
    private static final String PERSON_REVIEWER_PROPERTY_NAME = "personReviewer";
    private static final String PERSON_REVIEWER_TYPE_PROPERTY_NAME = "personReviewerType";
    private static final String DOC_TYP_NAME_PROPERTY_NAME = "documentType.name";
    private static final String RULE_DESC_PROPERTY_NAME = "description";

    private static final String BACK_LOCATION = "backLocation";
    private static final String DOC_FORM_KEY = "docFormKey";
    private static final String INVALID_WORKGROUP_ERROR = "The Group Reviewer Namespace and Name combination is not valid";
    private static final String INVALID_PERSON_ERROR = "The Person Reviewer is not valid";

    @Override
	public List<Row> getRows() {
        List<Row> superRows = super.getRows();
        List<Row> returnRows = new ArrayList<Row>();
        returnRows.addAll(superRows);
        returnRows.addAll(rows);
        return returnRows;
    }

    @Override
    public boolean checkForAdditionalFields(Map<String, String> fieldValues) {
        String ruleTemplateNameParam = fieldValues.get(RULE_TEMPLATE_PROPERTY_NAME);

        if (StringUtils.isNotBlank(ruleTemplateNameParam)) {
            rows = new ArrayList<Row>();
            RuleTemplate ruleTemplate = KewApiServiceLocator.getRuleService().getRuleTemplateByName(ruleTemplateNameParam);

            for (RuleTemplateAttribute ruleTemplateAttribute : ruleTemplate.getActiveRuleTemplateAttributes()) {
                if (!RuleAttribute.isWorkflowAttribute(ruleTemplateAttribute.getRuleAttribute().getType())) {
                    continue;
                }
                // run through the attributes fields once to populate field values we have to do this
                // to allow rows dependent on another row value to populate correctly in the loop below
                WorkflowRuleAttributeRows workflowRuleAttributeRows =
                        KEWServiceLocator.getWorkflowRuleAttributeMediator().getSearchRows(fieldValues, ruleTemplateAttribute);
                for (Row row : workflowRuleAttributeRows.getRows()) {
                    List<Field> fields = new ArrayList<Field>();
                    for (Iterator<Field> iterator2 = row.getFields().iterator(); iterator2.hasNext(); ) {
                        Field field = (Field) iterator2.next();
                        if (fieldValues.get(field.getPropertyName()) != null) {
                            field.setPropertyValue(fieldValues.get(field.getPropertyName()));
                        }
                        fields.add(field);
                        fieldValues.put(field.getPropertyName(), field.getPropertyValue());
                    }
                }
                // now run through a second time with our shiny new field values
                // ...by the way, just trying to preserve behavior from Rice 1.0.x here...generally speaking, this stuff is crazy!!!
                workflowRuleAttributeRows =
                        KEWServiceLocator.getWorkflowRuleAttributeMediator().getSearchRows(fieldValues, ruleTemplateAttribute);
                for (Row row : workflowRuleAttributeRows.getRows()) {
                    List<Field> fields = new ArrayList<Field>();
                    for (Iterator<Field> iterator2 = row.getFields().iterator(); iterator2.hasNext(); ) {
                        Field field = iterator2.next();
                        if (fieldValues.get(field.getPropertyName()) != null) {
                            field.setPropertyValue(fieldValues.get(field.getPropertyName()));
                        }
                        fields.add(field);
                        fieldValues.put(field.getPropertyName(), field.getPropertyValue());
                    }
                    row.setFields(fields);
                    rows.add(row);

                }
            }
            return true;
        }
        rows.clear();
        return false;
    }

    @Override
    public List<? extends BusinessObject> getSearchResults(Map<String, String> fieldValues) {
        String docTypeNameParam = fieldValues.get(DOC_TYP_NAME_PROPERTY_NAME);
        String ruleTemplateIdParam = fieldValues.get(RULE_TEMPLATE_ID_PROPERTY_NAME);
        String ruleTemplateNameParam = fieldValues.get(RULE_TEMPLATE_PROPERTY_NAME);
        String groupIdParam = fieldValues.get(GROUP_REVIEWER_PROPERTY_NAME);
        String groupNameParam = fieldValues.get(GROUP_REVIEWER_NAME_PROPERTY_NAME);
        String groupNamespaceParam = fieldValues.get(GROUP_REVIEWER_NAMESPACE_PROPERTY_NAME);
        String networkIdParam = fieldValues.get(PERSON_REVIEWER_PROPERTY_NAME);
        String userDirectiveParam = fieldValues.get(PERSON_REVIEWER_TYPE_PROPERTY_NAME);
        String activeParam = fieldValues.get(ACTIVE_IND_PROPERTY_NAME);
        String ruleIdParam = fieldValues.get(RULE_ID_PROPERTY_NAME);
        String ruleDescription = fieldValues.get(RULE_DESC_PROPERTY_NAME);
        String deleteSelection = fieldValues.get(DELEGATE_RULE_PROPERTY_NAME);

        String docTypeSearchName = null;
        String workflowId = null;
        String workgroupId = null;
        String ruleTemplateId = null;
        Boolean isDelegateRule = null;
        Boolean isActive = null;
        String ruleId = null;
      
        
        //for KULRICE-3678
        if(StringUtils.isNotBlank(deleteSelection))
        {
        	if(deleteSelection.equalsIgnoreCase("Y")) {
				isDelegateRule = Boolean.TRUE;
			} else {
				isDelegateRule = Boolean.FALSE;
			}
        }
        
        if (StringUtils.isNotBlank(ruleIdParam)) {
            ruleId = ruleIdParam.trim();
        }

        if (StringUtils.isNotBlank(activeParam)) {
            if (activeParam.equals("Y")) {
                isActive = Boolean.TRUE;
            } else {
                isActive = Boolean.FALSE;
            }
        }

        if (StringUtils.isNotBlank(docTypeNameParam)) {
            docTypeSearchName = docTypeNameParam.replace('*', '%');
            docTypeSearchName = "%" + docTypeSearchName.trim() + "%";
        }

        if (StringUtils.isNotBlank(networkIdParam)) {
            Principal principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(networkIdParam);
        	if (principal != null) {
        		workflowId = principal.getPrincipalId();
        	}
        }
        
        if (!StringUtils.isEmpty(groupIdParam) || !StringUtils.isEmpty(groupNameParam)) {
            Group group = null;
            if (groupIdParam != null && !"".equals(groupIdParam)) {
                group = getGroupService().getGroup(groupIdParam.trim());
            } else {
                if (groupNamespaceParam == null) {
                    groupNamespaceParam = KimConstants.KIM_GROUP_DEFAULT_NAMESPACE_CODE;
                }
                group = getGroupService().getGroupByNamespaceCodeAndName(groupNamespaceParam, groupNameParam.trim());
                if (group == null) {
                    GlobalVariables.getMessageMap().putError(GROUP_REVIEWER_NAMESPACE_PROPERTY_NAME, RiceKeyConstants.ERROR_CUSTOM, INVALID_WORKGROUP_ERROR);
                } else {
                    workgroupId = group.getId();
                }
            }
        }

        Map<String, String> attributes = null;
        MyColumns myColumns = new MyColumns();
        if (StringUtils.isNotBlank(ruleTemplateNameParam) || StringUtils.isNotBlank(ruleTemplateIdParam) && !"null".equals(ruleTemplateIdParam)) {
            RuleTemplate ruleTemplate = null;
            if (StringUtils.isNotBlank(ruleTemplateIdParam)) {
                ruleTemplate = KewApiServiceLocator.getRuleService().getRuleTemplate(ruleTemplateIdParam);
            } else {
                ruleTemplate = KewApiServiceLocator.getRuleService().getRuleTemplateByName(ruleTemplateNameParam.trim());
                ruleTemplateId = ruleTemplate.getId();
            }

            attributes = new HashMap<String, String>();
            for (RuleTemplateAttribute ruleTemplateAttribute : ruleTemplate.getActiveRuleTemplateAttributes()) {
                if (!RuleAttribute.isWorkflowAttribute(ruleTemplateAttribute.getRuleAttribute().getType())) {
                    continue;
                }
                WorkflowRuleAttributeRows workflowRuleAttributeRows =
                        KEWServiceLocator.getWorkflowRuleAttributeMediator().getSearchRows(fieldValues, ruleTemplateAttribute, false);

                for (RemotableAttributeError error : workflowRuleAttributeRows.getValidationErrors()) {
                    GlobalVariables.getMessageMap().putError(error.getAttributeName(), RiceKeyConstants.ERROR_CUSTOM, error.getMessage());
                }

                for (Row row : workflowRuleAttributeRows.getRows()) {
                    for (Field field : row.getFields()) {
                        if (fieldValues.get(field.getPropertyName()) != null) {
                            String attributeParam = fieldValues.get(field.getPropertyName());
                            if (!attributeParam.equals("")) {
                                attributes.put(field.getPropertyName(), attributeParam.trim());
                            }
                        }
                        if (field.getFieldType().equals(Field.TEXT) || field.getFieldType().equals(Field.DROPDOWN) || field.getFieldType().equals(Field.DROPDOWN_REFRESH) || field.getFieldType().equals(Field.RADIO)) {
                            myColumns.getColumns().add(new ConcreteKeyValue(field.getPropertyName(), ruleTemplateAttribute.getId()));
                        }
                    }
                }
            }
        }

        if (!StringUtils.isEmpty(ruleDescription)) {
            ruleDescription = ruleDescription.replace('*', '%');
            ruleDescription = "%" + ruleDescription.trim() + "%";
        }
       
        if (!GlobalVariables.getMessageMap().hasNoErrors()) {
            throw new ValidationException("errors in search criteria");
        }
       

        // TODO: replace this with new API find method ??
        List<RuleBaseValues> rules = getRuleService().search(docTypeSearchName, ruleId, ruleTemplateId, ruleDescription, workgroupId, workflowId, isDelegateRule, isActive, attributes, userDirectiveParam);
        List<RuleBaseValues> displayList = new ArrayList<RuleBaseValues>();

        for (RuleBaseValues record : rules) {
            if (StringUtils.isEmpty(record.getDescription())) {
                record.setDescription("");
            }

            if (ruleTemplateNameParam != null && !ruleTemplateNameParam.trim().equals("") || ruleTemplateIdParam != null && !"".equals(ruleTemplateIdParam) && !"null".equals(ruleTemplateIdParam)) {
                MyColumns myNewColumns = new MyColumns();
                for (KeyValue pair : myColumns.getColumns()) {
                    final KeyValue newPair;
                    if (record.getRuleExtensionValue(pair.getValue(), pair.getKey().toString()) != null) {
                    	newPair = new ConcreteKeyValue(pair.getKey(), record.getRuleExtensionValue(pair.getValue(), pair.getKey().toString()).getValue());
                    } else {
                    	newPair = new ConcreteKeyValue(pair.getKey(), "");
                    }
                    myNewColumns.getColumns().add(newPair);
                    record.getFieldValues().put(newPair.getKey(), newPair.getValue());
                }
                record.setMyColumns(myNewColumns);
            }

            StringBuffer returnUrl = new StringBuffer("<a href=\"");
            returnUrl.append(fieldValues.get(BACK_LOCATION)).append("?methodToCall=refresh&docFormKey=").append(fieldValues.get(DOC_FORM_KEY)).append("&");

            returnUrl.append(RULE_ID_PROPERTY_NAME);
            returnUrl.append("=").append(record.getId()).append("\">return value</a>");
            record.setReturnUrl(returnUrl.toString());

            String destinationUrl = "<a href=\"Rule.do?methodToCall=report&currentRuleId=" + record.getId() + "\">report</a>";

            record.setDestinationUrl(destinationUrl);

            displayList.add(record);
        }
        return displayList;

    }



    private GroupService getGroupService() {
       return KimApiServiceLocator.getGroupService();
    }

    private RuleTemplateService getRuleTemplateService() {
        return (RuleTemplateService) KEWServiceLocator.getService(KEWServiceLocator.RULE_TEMPLATE_SERVICE);
    }
    private RuleServiceInternal getRuleService() {
        return (RuleServiceInternal) KEWServiceLocator.getService(KEWServiceLocator.RULE_SERVICE);
    }

    @Override
    public void validateSearchParameters(Map<String, String> fieldValues) {
        super.validateSearchParameters(fieldValues);

        // make sure that if we have either groupName or Namespace, that both are filled in
        String groupName = (String)fieldValues.get(GROUP_REVIEWER_NAME_PROPERTY_NAME);
        String groupNamespace = (String)fieldValues.get(GROUP_REVIEWER_NAMESPACE_PROPERTY_NAME);
        String principalName = (String)fieldValues.get(PERSON_REVIEWER_PROPERTY_NAME);

        if (StringUtils.isEmpty(groupName) && !StringUtils.isEmpty(groupNamespace)) {
            String attributeLabel = getDataDictionaryService().getAttributeLabel(getBusinessObjectClass(), GROUP_REVIEWER_NAME_PROPERTY_NAME);
            GlobalVariables.getMessageMap().putError(GROUP_REVIEWER_NAME_PROPERTY_NAME, RiceKeyConstants.ERROR_REQUIRED, attributeLabel);
        }

        if  (!StringUtils.isEmpty(groupName) && StringUtils.isEmpty(groupNamespace)) {
            String attributeLabel = getDataDictionaryService().getAttributeLabel(getBusinessObjectClass(), GROUP_REVIEWER_NAMESPACE_PROPERTY_NAME);
            GlobalVariables.getMessageMap().putError(GROUP_REVIEWER_NAMESPACE_PROPERTY_NAME, RiceKeyConstants.ERROR_REQUIRED, attributeLabel);
        }

        if  (!StringUtils.isEmpty(groupName) && !StringUtils.isEmpty(groupNamespace)) {
            Group group = KimApiServiceLocator.getGroupService().getGroupByNamespaceCodeAndName(groupNamespace,
                    groupName);
            if (group == null) {
                GlobalVariables.getMessageMap().putError(GROUP_REVIEWER_NAME_PROPERTY_NAME, RiceKeyConstants.ERROR_CUSTOM, INVALID_WORKGROUP_ERROR);
            }
        }

        if  (!StringUtils.isEmpty(principalName)) {
            Person person = KimApiServiceLocator.getPersonService().getPersonByPrincipalName(principalName);
            if (person == null) {
                GlobalVariables.getMessageMap().putError(PERSON_REVIEWER_PROPERTY_NAME, RiceKeyConstants.ERROR_CUSTOM, INVALID_PERSON_ERROR);
            }
        }
        if (!GlobalVariables.getMessageMap().hasNoErrors()) {
            throw new ValidationException("errors in search criteria");
        }
    }

    @Override
    public Collection performLookup(LookupForm lookupForm,
            Collection resultTable, boolean bounded) {
        //return super.performLookup(lookupForm, resultTable, bounded);
        setBackLocation((String) lookupForm.getFieldsForLookup().get(KRADConstants.BACK_LOCATION));
        setDocFormKey((String) lookupForm.getFieldsForLookup().get(KRADConstants.DOC_FORM_KEY));
        Collection displayList;

        // call search method to get results
        if (bounded) {
            displayList = getSearchResults(lookupForm.getFieldsForLookup());
        }
        else {
            displayList = getSearchResultsUnbounded(lookupForm.getFieldsForLookup());
        }

        HashMap<String,Class> propertyTypes = new HashMap<String, Class>();

        boolean hasReturnableRow = false;

        List returnKeys = getReturnKeys();
        List pkNames = getBusinessObjectMetaDataService().listPrimaryKeyFieldNames(getBusinessObjectClass());
        Person user = GlobalVariables.getUserSession().getPerson();
        
        // iterate through result list and wrap rows with return url and action urls
        for (Iterator iter = displayList.iterator(); iter.hasNext();) {
            BusinessObject element = (BusinessObject) iter.next();
            if(element instanceof PersistableBusinessObject){
                lookupForm.setLookupObjectId(((PersistableBusinessObject)element).getObjectId());
            }

            BusinessObjectRestrictions businessObjectRestrictions = getBusinessObjectAuthorizationService().getLookupResultRestrictions(element, user);

            HtmlData returnUrl = getReturnUrl(element, lookupForm, returnKeys, businessObjectRestrictions);

            String actionUrls = getActionUrls(element, pkNames, businessObjectRestrictions);
            //Fix for JIRA - KFSMI-2417
            if("".equals(actionUrls)){
                actionUrls = ACTION_URLS_EMPTY;
            }

            // Determine whether or not this rule is a delegate rule.
            boolean isRuleDelegation = (element instanceof RuleBaseValues && ((RuleBaseValues) element).getDelegateRule().booleanValue());
            
            List<Column> columns = getColumns();
            for (Object element2 : columns) {

                Column col = (Column) element2;
                Formatter formatter = col.getFormatter();

                // pick off result column from result list, do formatting
                String propValue = KRADConstants.EMPTY_STRING;
                Object prop = null;
                boolean skipPropTypeCheck = false;
                //try to get value elsewhere
                if (element instanceof RuleBaseValues) {
                    prop = ((RuleBaseValues)element).getFieldValues().get(col.getPropertyName());
                    skipPropTypeCheck = true;
                }
                if (prop == null) {
                    prop = ObjectUtils.getPropertyValue(element, col.getPropertyName());
                }

                // set comparator and formatter based on property type
                Class propClass = propertyTypes.get(col.getPropertyName());
                if ( propClass == null && !skipPropTypeCheck) {
                    try {
                        propClass = ObjectUtils.getPropertyType( element, col.getPropertyName(), getPersistenceStructureService() );
                        propertyTypes.put( col.getPropertyName(), propClass );
                    } catch (Exception e) {
                        throw new RuntimeException("Cannot access PropertyType for property " + "'" + col.getPropertyName() + "' " + " on an instance of '" + element.getClass().getName() + "'.", e);
                    }
                }

                // formatters
                if (prop != null) {
                    // for Booleans, always use BooleanFormatter
                    if (prop instanceof Boolean) {
                        formatter = new BooleanFormatter();
                    }

                    // for Dates, always use DateFormatter
                    if (prop instanceof Date) {
                        formatter = new DateFormatter();
                    }

                    // for collection, use the list formatter if a formatter hasn't been defined yet
                    if (prop instanceof Collection && formatter == null) {
                    formatter = new CollectionFormatter();
                    }

                    if (formatter != null) {
                        propValue = (String) formatter.format(prop);
                    }
                    else {
                        propValue = prop.toString();
                    }
                }

                // comparator
                col.setComparator(CellComparatorHelper.getAppropriateComparatorForPropertyClass(propClass));
                col.setValueComparator(CellComparatorHelper.getAppropriateValueComparatorForPropertyClass(propClass));

                propValue = maskValueIfNecessary(element.getClass(), col.getPropertyName(), propValue, businessObjectRestrictions);

                col.setPropertyValue(propValue);

                if (StringUtils.isNotBlank(propValue)) {
                	if (RULE_ID_PROPERTY_NAME.equals(col.getPropertyName()) && isRuleDelegation) {
                		// If the row represents a delegate rule, make the ID column's inquiry link lead to the corresponding delegate rule instead.
                   		List<?> delegationList = KEWServiceLocator.getRuleDelegationService().findByDelegateRuleId(
                   				((RuleBaseValues) element).getId());
                		if (ObjectUtils.isNotNull(delegationList) && !delegationList.isEmpty()) {
                			BusinessObject ruleDelegation = (BusinessObject) delegationList.get(0);
                			col.setColumnAnchor(getInquiryUrl(ruleDelegation, "ruleDelegationId"));
                		} else {
                			col.setColumnAnchor(getInquiryUrl(element, col.getPropertyName()));
                		}
                	}else {
                		col.setColumnAnchor(getInquiryUrl(element, col.getPropertyName()));
                	}

                }
            }

            ResultRow row = new ResultRow(columns, returnUrl.constructCompleteHtmlTag(), actionUrls);
            row.setRowId(returnUrl.getName());
            row.setReturnUrlHtmlData(returnUrl);
            // because of concerns of the BO being cached in session on the ResultRow,
            // let's only attach it when needed (currently in the case of export)
            if (getBusinessObjectDictionaryService().isExportable(getBusinessObjectClass())) {
                row.setBusinessObject(element);
            }
            if(element instanceof PersistableBusinessObject){
                row.setObjectId((((PersistableBusinessObject)element).getObjectId()));
            }


            boolean rowReturnable = isResultReturnable(element);
            row.setRowReturnable(rowReturnable);
            if (rowReturnable) {
                hasReturnableRow = true;
            }
            resultTable.add(row);
        }

        lookupForm.setHasReturnableRow(hasReturnableRow);

        return displayList;
    }

    @Override
    public List<Column> getColumns() {
        List<Column> columns = super.getColumns();
        for (Row row : rows) {
            for (Field field : row.getFields()) {
                Column newColumn = new Column();
                newColumn.setColumnTitle(field.getFieldLabel());
                newColumn.setMaxLength(field.getMaxLength());
                newColumn.setPropertyName(field.getPropertyName());
                columns.add(newColumn);
            }
        }
        return columns;
    }

    @Override
    public List<HtmlData> getCustomActionUrls(BusinessObject businessObject,
            List pkNames) {
        RuleBaseValues ruleBaseValues = (RuleBaseValues)businessObject;
        List<HtmlData> htmlDataList = new ArrayList<HtmlData>();
        if (StringUtils.isNotBlank(ruleBaseValues.getRuleTemplateName()) && StringUtils.isNotBlank(getMaintenanceDocumentTypeName())) {
        	if (ruleBaseValues.getDelegateRule().booleanValue()) {
        		// If the rule is a delegate rule, have the edit/copy links open the rule delegation maintenance document screen instead.
        		List<?> delegationList = KEWServiceLocator.getRuleDelegationService().findByDelegateRuleId(ruleBaseValues.getId());
        		if (ObjectUtils.isNotNull(delegationList) && !delegationList.isEmpty()) {
        			BusinessObject ruleDelegation = (BusinessObject) delegationList.get(0);
    				// Retrieve the rule delegation lookupable helper service and the primary key names, if they have not been obtained yet.
        	        if (ruleDelegationLookupableHelperService == null) {
        				ruleDelegationLookupableHelperService = KNSServiceLocator.getLookupable(
                                KNSServiceLocator.getBusinessObjectDictionaryService()
                                        .getLookupableID(ruleDelegation.getClass())).getLookupableHelperService();
        				if (ruleDelegationLookupableHelperService.getBusinessObjectClass() == null) {
        					ruleDelegationLookupableHelperService.setBusinessObjectClass(ruleDelegation.getClass());
        				}
        				delegationPkNames = getBusinessObjectMetaDataService().listPrimaryKeyFieldNames(ruleDelegation.getClass());
        			}
        	        // Allow the rule delegation's lookupable helper service to handle the custom action URL generation instead.
        			htmlDataList = ruleDelegationLookupableHelperService.getCustomActionUrls(ruleDelegation, delegationPkNames);
        		}
        	} else {
        		// Otherwise, have the links open the regular routing rule maintenance document screen.
        		if (allowsMaintenanceEditAction(businessObject)) {
        			htmlDataList.add(getUrlData(businessObject, KRADConstants.MAINTENANCE_EDIT_METHOD_TO_CALL, pkNames));
        		}
        		if (allowsMaintenanceNewOrCopyAction()) {
                	htmlDataList.add(getUrlData(businessObject, KRADConstants.MAINTENANCE_COPY_METHOD_TO_CALL, pkNames));
            	}
        	}
        }
        
        return htmlDataList;
    }



}
