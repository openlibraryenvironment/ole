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
package org.kuali.rice.kim.lookup;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.criteria.Predicate;
import org.kuali.rice.core.api.criteria.PredicateUtils;
import org.kuali.rice.core.api.criteria.QueryByCriteria;
import org.kuali.rice.core.api.util.ClassLoaderUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.core.web.format.BooleanFormatter;
import org.kuali.rice.core.web.format.CollectionFormatter;
import org.kuali.rice.core.web.format.DateFormatter;
import org.kuali.rice.core.web.format.Formatter;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.group.Group;
import org.kuali.rice.kim.api.group.GroupQueryResults;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.identity.principal.PrincipalQueryResults;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimAttributeField;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.framework.services.KimFrameworkServiceLocator;
import org.kuali.rice.kim.framework.type.KimTypeService;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.type.KimTypeLookupableHelperServiceImpl;
import org.kuali.rice.kim.util.KimCommonUtilsInternal;
import org.kuali.rice.kns.document.authorization.BusinessObjectRestrictions;
import org.kuali.rice.kns.kim.type.DataDictionaryTypeServiceHelper;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.web.comparator.CellComparatorHelper;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.kns.web.ui.Column;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.ResultRow;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.keyvalues.IndicatorValuesFinder;
import org.kuali.rice.krad.keyvalues.KeyValuesFinder;
import org.kuali.rice.krad.keyvalues.KimAttributeValuesFinder;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.util.UrlFactory;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.kuali.rice.core.api.criteria.PredicateFactory.*;

public class GroupLookupableHelperServiceImpl  extends KimLookupableHelperServiceImpl {

	// need this so kimtypeId value can be retained in 'rows'
	// 1st pass populate the grprows
	// 2nd pass for jsp, no populate, so return the existing one.
    private static final String KIM_TYPE_ID_PROPERTY_NAME = "kimTypeId";
	private List<Row> grpRows = new ArrayList<Row>();
	private List<Row> attrRows = new ArrayList<Row>();
	private String typeId = "";
	private List<KimAttributeField> attrDefinitions;
	private final Map<String, String> groupTypeValuesCache = new HashMap<String, String>();

    @Override
    public List<HtmlData> getCustomActionUrls(BusinessObject bo, List pkNames) {
    	GroupBo groupImpl = (GroupBo) bo;
        List<HtmlData> anchorHtmlDataList = new ArrayList<HtmlData>();
        if(allowsNewOrCopyAction(KimConstants.KimUIConstants.KIM_GROUP_DOCUMENT_TYPE_NAME)){
        	anchorHtmlDataList.add(getEditGroupUrl(groupImpl));	
        }
    	return anchorHtmlDataList;
    }
    
    protected HtmlData getEditGroupUrl(GroupBo groupBo) {
    	String href = "";

        Properties parameters = new Properties();
        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.DOC_HANDLER_METHOD);
        parameters.put(KRADConstants.PARAMETER_COMMAND, KewApiConstants.INITIATE_COMMAND);
        parameters.put(KRADConstants.DOCUMENT_TYPE_NAME, KimConstants.KimUIConstants.KIM_GROUP_DOCUMENT_TYPE_NAME);
        parameters.put(KimConstants.PrimaryKeyConstants.GROUP_ID, groupBo.getId());
        if (StringUtils.isNotBlank(getReturnLocation())) {
        	parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, getReturnLocation());
		}
        href = UrlFactory.parameterizeUrl(KimCommonUtilsInternal.getKimBasePath()+KimConstants.KimUIConstants.KIM_GROUP_DOCUMENT_ACTION, parameters);
        
        HtmlData.AnchorHtmlData anchorHtmlData = new HtmlData.AnchorHtmlData(href,
        		KRADConstants.DOC_HANDLER_METHOD, KRADConstants.MAINTENANCE_EDIT_METHOD_TO_CALL);
        return anchorHtmlData;
    }

    /**
     * Converts GroupInfo objects to GroupBo objects.
     * 
     * @param  fieldValues  names and values returned by the Group Lookup screen
     * @return  groupImplList  a list of GroupImpl objects
     */
    @Override
    public List<GroupBo> getSearchResults(java.util.Map<String,String> fieldValues)  {
        Map<String, String> criteriaMap = new HashMap<String, String>(fieldValues);
        QueryByCriteria.Builder criteria = QueryByCriteria.Builder.create();
        criteriaMap.remove(KRADConstants.DOC_FORM_KEY);
        criteriaMap.remove(KRADConstants.BACK_LOCATION);
        criteriaMap.remove(KRADConstants.DOC_NUM);
        String refToRef = criteriaMap.get(KRADConstants.REFERENCES_TO_REFRESH);
        if (StringUtils.isNotBlank(refToRef) && refToRef.equalsIgnoreCase("GroupBo")) {
            criteriaMap.remove(KRADConstants.REFERENCES_TO_REFRESH);
        }

        if (!criteriaMap.isEmpty()) {
            List<Predicate> predicates = new ArrayList<Predicate>();
            //principalId doesn't exist on 'Group'.  Lets do this predicate conversion separately
            if (StringUtils.isNotBlank(criteriaMap.get(KimConstants.UniqueKeyConstants.PRINCIPAL_NAME))) {
                QueryByCriteria.Builder principalCriteria = QueryByCriteria.Builder.create();
                Predicate principalPred = like("principalName", criteriaMap.get(KimConstants.UniqueKeyConstants.PRINCIPAL_NAME));
                principalCriteria.setPredicates(principalPred);
                //String principalId = KimApiServiceLocator.getIdentityService()
                //        .getPrincipalByPrincipalName(criteriaMap.get(KimConstants.UniqueKeyConstants.PRINCIPAL_NAME)).getPrincipalId();
                PrincipalQueryResults principals = KimApiServiceLocator.getIdentityService()
                        .findPrincipals(principalCriteria.build());
                List<String> principalIds = new ArrayList<String>();
                for (Principal principal : principals.getResults()) {
                    principalIds.add(principal.getPrincipalId());
                }
                if (CollectionUtils.isNotEmpty(principalIds)) {
                    Timestamp currentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
                    predicates.add( and(
                                    in("members.memberId", principalIds.toArray(
                                            new String[principalIds.size()])),
                                    equal("members.typeCode", KimConstants.KimGroupMemberTypes.PRINCIPAL_MEMBER_TYPE.getCode()),
                                    and(
                                            or(isNull("members.activeFromDateValue"), lessThanOrEqual("members.activeFromDateValue", currentTime)),
                                            or(isNull("members.activeToDateValue"), greaterThan("members.activeToDateValue", currentTime))
                                    )
                                ));
                }

            }
            criteriaMap.remove(KimConstants.UniqueKeyConstants.PRINCIPAL_NAME);

            predicates.add(PredicateUtils.convertMapToPredicate(criteriaMap));
            criteria.setPredicates(and(predicates.toArray(new Predicate[predicates.size()])));
        }
    	GroupQueryResults groupResults = KimApiServiceLocator.getGroupService().findGroups(criteria.build());
    	List<Group> groups = groupResults.getResults();

        //have to convert back to Bos :(
        Map<String, GroupBo> groupBos = new HashMap<String, GroupBo>(groups.size());
        for (Group group : groups) {
            if (groupBos.get(group.getId()) == null) {
                groupBos.put(group.getId(), GroupBo.from(group));
            }
        }


    	return new ArrayList<GroupBo>(groupBos.values());
    }

    @Override
    public boolean checkForAdditionalFields(Map<String, String> fieldValues) {
        List<Row> attributeRows = setupAttributeRows(fieldValues);
        if (attributeRows.isEmpty()) {
            setAttrRows(attributeRows);
        } else if (CollectionUtils.isEmpty(getAttrRows())) {
            setAttrRows(attributeRows);
        }
        if (getAttrRows().size() > 0) {
            return true;
        }
        return false;
    }


	@Override
	public List<Row> getRows() {
		if (getGrpRows().isEmpty()) {
			List<Row> rows = super.getRows();
			List<Row> returnRows = new ArrayList<Row>();
			for (Row row : rows) {
				for (int i = row.getFields().size() - 1; i >= 0; i--) {
					Field field = row.getFields().get(i);
					if (field.getPropertyName().equals(KIM_TYPE_ID_PROPERTY_NAME)) {
						Field typeField = new Field();
						typeField.setFieldLabel("Type");
						typeField.setPropertyName(KIM_TYPE_ID_PROPERTY_NAME);
						typeField.setFieldValidValues(getGroupTypeOptions());
						typeField.setFieldType(Field.DROPDOWN);
						row.getFields().set(i, typeField);
					}
				}
				returnRows.add(row);
			}
			// principalName
			Field typeField = new Field();
			typeField.setFieldLabel("Principal Name");
			typeField.setPropertyName(KIMPropertyConstants.Person.PRINCIPAL_NAME);
			typeField.setFieldType(Field.TEXT);
			typeField.setMaxLength(40);
			typeField.setSize(20);
			typeField.setQuickFinderClassNameImpl("org.kuali.rice.kim.api.identity.Person");
			typeField.setFieldConversions( "principalName:principalName" );
			typeField.setLookupParameters( "principalName:principalName" );
			// Identify the best spot to insert the "Principal Name" search field. Note that the code below assumes that the final row of the
			// group search fields is not a row with hidden fields; if this ever becomes the case in the future, this fix may need to
			// be modified accordingly.
			List<Field> fields = (returnRows.isEmpty()) ? new ArrayList<Field>() : returnRows.get(returnRows.size() - 1).getFields();
			if (!fields.isEmpty() && fields.get(fields.size() - 1).getFieldType().equals(Field.BLANK_SPACE)) {
				// If the last row in the list has a BLANK_SPACE field coming after any non-BLANK_SPACE fields, add the new field to that row.
				int insertLoc = fields.size() - 1;
				while (insertLoc >= 0 && fields.get(insertLoc).getFieldType().equals(Field.BLANK_SPACE)) {
					insertLoc--;
				}
				fields.set(insertLoc + 1, typeField);
				returnRows.get(returnRows.size() - 1).setFields(fields);
			} else {
				// Otherwise, add a new row containing that field.
				int fieldLen = fields.size();
				fields = new ArrayList<Field>();
				fields.add(typeField);
				for (int i = 1; i < fieldLen; i++) {
					Field blankSpace = new Field();
					blankSpace.setFieldType(Field.BLANK_SPACE);
					blankSpace.setPropertyName(Field.BLANK_SPACE);
					fields.add(blankSpace);
				}
				returnRows.add(new Row(fields));
			}
			
			setGrpRows(returnRows);
		}
		if (getAttrRows().isEmpty()) {
			setAttrDefinitions(Collections.<KimAttributeField>emptyList());
			return getGrpRows();
		} 
		List<Row> fullRows = new ArrayList<Row>();
		fullRows.addAll(getGrpRows());
		fullRows.addAll(getAttrRows());
		return fullRows;
	}


	@Override
	public List<Column> getColumns() {
		List<Column> columns =  super.getColumns();
        for (Row row : attrRows) {
            for (Field field : row.getFields()) {
                Column newColumn = new Column();
                newColumn.setColumnTitle(field.getFieldLabel());
                newColumn.setMaxLength(getColumnMaxLength(field.getPropertyName()));
                newColumn.setPropertyName(field.getPropertyName());
                newColumn.setFormatter(field.getFormatter());
                columns.add(newColumn);
            }
        }
        return columns;
	}

    @Override
	public Collection<GroupBo> performLookup(LookupForm lookupForm, Collection resultTable, boolean bounded) {
        setBackLocation((String) lookupForm.getFieldsForLookup().get(KRADConstants.BACK_LOCATION));
        setDocFormKey((String) lookupForm.getFieldsForLookup().get(KRADConstants.DOC_FORM_KEY));
        List<GroupBo> displayList;

        // call search method to get results
        if (bounded) {
            displayList = getSearchResults(lookupForm.getFieldsForLookup());
        }
        else {
            displayList = (List<GroupBo>)getSearchResultsUnbounded(lookupForm.getFieldsForLookup());
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

            List<Column> columns = getColumns();
            for (Object element2 : columns) {

                Column col = (Column) element2;
                Formatter formatter = col.getFormatter();

                // pick off result column from result list, do formatting
                String propValue = KRADConstants.EMPTY_STRING;
                Object prop = null;
                if (col.getPropertyName().matches("\\w+\\.\\d+$")) {
                    String id = col.getPropertyName().substring(col.getPropertyName().lastIndexOf('.') + 1); //.split("\\d+$"))[1];
                    prop = ((GroupBo)element).getGroupAttributeValueById(id);
                }
                if (prop == null) {
                    prop = ObjectUtils.getPropertyValue(element, col.getPropertyName());
                } else {
                }

                // set comparator and formatter based on property type
                Class propClass = propertyTypes.get(col.getPropertyName());
                if ( propClass == null /*&& !skipPropTypeCheck*/) {
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
                        if (col.getPropertyName().equals(KIM_TYPE_ID_PROPERTY_NAME)) {
                            propValue = groupTypeValuesCache.get(prop.toString());
                        }
                    }
                }

                // comparator
                col.setComparator(CellComparatorHelper.getAppropriateComparatorForPropertyClass(propClass));
                col.setValueComparator(CellComparatorHelper.getAppropriateValueComparatorForPropertyClass(propClass));

                propValue = maskValueIfNecessary(element.getClass(), col.getPropertyName(), propValue, businessObjectRestrictions);

                col.setPropertyValue(propValue);

                if (StringUtils.isNotBlank(propValue)) {
                    col.setColumnAnchor(getInquiryUrl(element, col.getPropertyName()));

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


	private List<KeyValue> getGroupTypeOptions() {
		List<KeyValue> options = new ArrayList<KeyValue>();
		options.add(new ConcreteKeyValue("", ""));

		Collection<KimType> kimGroupTypes = KimApiServiceLocator.getKimTypeInfoService().findAllKimTypes();
		// get the distinct list of type IDs from all groups in the system
        for (KimType kimType : kimGroupTypes) {
            if (KimTypeLookupableHelperServiceImpl.hasGroupTypeService(kimType) && groupTypeValuesCache.get(kimType.getId()) == null) {
                String value = kimType.getNamespaceCode().trim() + KRADConstants.FIELD_CONVERSION_PAIR_SEPARATOR + kimType.getName().trim();
                options.add(new ConcreteKeyValue(kimType.getId(), value));
            }
        }
        Collections.sort(options, new Comparator<KeyValue>() {
           @Override
           public int compare(KeyValue k1, KeyValue k2) {
               return k1.getValue().compareTo(k2.getValue());
           }
        });
		return options;
	}

	private List<Row> setupAttributeRows(Map fieldValues) {
		List<Row> returnRows = new ArrayList<Row>();
		for (Row row : getGrpRows()) {
			Field field = row.getFields().get(0);
			if (field.getPropertyName().equals(KIM_TYPE_ID_PROPERTY_NAME) && StringUtils.isNotBlank(field.getPropertyValue())) {
				if (!StringUtils.isBlank(getTypeId()) || !getTypeId().equals(field.getPropertyValue())) {
					setTypeId(field.getPropertyValue());
					setAttrRows(new ArrayList<Row>());
					KimType kimType = getTypeInfoService().getKimType( field.getPropertyValue() );
					KimTypeService kimTypeService = KimFrameworkServiceLocator.getKimTypeService(kimType);
			        List<KimAttributeField> definitions = kimTypeService.getAttributeDefinitions(kimType.getId());
			        setAttrDefinitions(definitions);
		            for (KimAttributeField d  : definitions) {
				        final AttributeDefinition definition  = DataDictionaryTypeServiceHelper
                                .toKimAttributeDefinition(d);
                        List<Field> fields = new ArrayList<Field>();
						Field typeField = new Field();

						String attrDefnId = d.getId();
						typeField.setFieldLabel(definition.getLabel());
						typeField.setPropertyName("attributes(" + definition.getName()+")");
						typeField.setPropertyValue(fieldValues.get(typeField.getPropertyName()));
						if (definition.getControl().isSelect()) {
                            typeField.setFieldValidValues(definition.getOptionsFinder().getKeyValues());
                            typeField.setFieldType(Field.DROPDOWN);
						} else if (definition.getControl().isText()){
							typeField.setMaxLength(definition.getMaxLength());
							if (definition.getControl().getSize() != null) {
							    typeField.setSize(definition.getControl().getSize());
							}
						    typeField.setFieldType(Field.TEXT);
						} else if (definition.getControl().isRadio()) {
                            typeField.setFieldValidValues(definition.getOptionsFinder().getKeyValues());
                            typeField.setFieldType(Field.RADIO);
						} else if (definition.getControl().isCheckbox()) {
						    KeyValuesFinder finder = new IndicatorValuesFinder();
                            typeField.setFieldValidValues(finder.getKeyValues());
                            typeField.setFieldType(Field.RADIO);
						    //typeField.setFieldType(Field.CHECKBOX);
						} else if (definition.getControl().isHidden()) {
						    typeField.setFieldType(Field.HIDDEN);
						} else if (definition.getControl().isLookupReadonly()) {
						    typeField.setFieldType(Field.LOOKUP_READONLY);
						} else if (definition.getControl().isTextarea()) {
						    typeField.setMaxLength(definition.getMaxLength());
                            if (definition.getControl().getSize() != null) {
                                typeField.setSize(definition.getControl().getSize());
                            }
                            typeField.setFieldType(Field.TEXT_AREA);
						}
						fields.add(typeField);
						returnRows.add(new Row(fields));
		            }
				} else {
					return getAttrRows();
				}
			} else if (field.getPropertyName().equals(KIM_TYPE_ID_PROPERTY_NAME) && StringUtils.isBlank(field.getPropertyValue())) {
				setTypeId("");
			}
		}
		return returnRows;
	}

	public List<Row> getGrpRows() {
		return this.grpRows;
	}

	public void setGrpRows(List<Row> grpRows) {
		this.grpRows = grpRows;
	}

	public List<KimAttributeField> getAttrDefinitions() {
		return this.attrDefinitions;
	}

	public void setAttrDefinitions(List<KimAttributeField> attrDefinitions) {
		this.attrDefinitions = attrDefinitions;
	}

	public List<Row> getAttrRows() {
		return this.attrRows;
	}

	public void setAttrRows(List<Row> attrRows) {
		this.attrRows = attrRows;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

    @Override
    public void performClear(LookupForm lookupForm) {
        super.performClear(lookupForm);
        this.attrRows = new ArrayList<Row>();
    }

}
