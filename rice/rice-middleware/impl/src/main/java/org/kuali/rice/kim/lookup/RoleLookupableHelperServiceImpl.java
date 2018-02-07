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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.core.api.util.KeyValue;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.api.type.KimAttributeField;
import org.kuali.rice.kim.api.type.KimType;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.role.RoleDao;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.kim.impl.type.KimTypeLookupableHelperServiceImpl;
import org.kuali.rice.kim.util.KimCommonUtilsInternal;
import org.kuali.rice.kim.web.struts.form.IdentityManagementRoleDocumentForm;
import org.kuali.rice.kns.document.authorization.BusinessObjectRestrictions;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.web.struts.form.KualiForm;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.datadictionary.BusinessObjectEntry;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.util.BeanPropertyComparator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RoleLookupableHelperServiceImpl extends KimLookupableHelperServiceImpl {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RoleLookupableHelperServiceImpl.class);

	// need this so kimtypeId value can be retained in 'rows'
	// 1st pass populate the grprows
	// 2nd pass for jsp, no populate, so return the existing one. 
	private List<Row> roleRows = new ArrayList<Row>();
	private List<Row> attrRows = new ArrayList<Row>();
	private RoleDao roleDao;
	private String typeId;
	private List<KimAttributeField> attrDefinitions;
	
    @Override
    public List<HtmlData> getCustomActionUrls(BusinessObject bo, List pkNames) {
    	RoleBo roleBo = (RoleBo) bo;
        List<HtmlData> anchorHtmlDataList = new ArrayList<HtmlData>();
    	if(allowsNewOrCopyAction(KimConstants.KimUIConstants.KIM_ROLE_DOCUMENT_TYPE_NAME)){
    		anchorHtmlDataList.add(getEditRoleUrl(roleBo));
    	}
    	return anchorHtmlDataList;
    }
    
    protected HtmlData getEditRoleUrl(RoleBo roleBo) {
        Properties parameters = new Properties();
        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.DOC_HANDLER_METHOD);
        parameters.put(KRADConstants.PARAMETER_COMMAND, KewApiConstants.INITIATE_COMMAND);
        parameters.put(KRADConstants.DOCUMENT_TYPE_NAME, KimConstants.KimUIConstants.KIM_ROLE_DOCUMENT_TYPE_NAME);
        parameters.put(KimConstants.PrimaryKeyConstants.SUB_ROLE_ID, roleBo.getId());
        if (StringUtils.isNotBlank(getReturnLocation())) {
        	parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, getReturnLocation());
		}
        String href = UrlFactory.parameterizeUrl(KimCommonUtilsInternal.getKimBasePath()+KimConstants.KimUIConstants.KIM_ROLE_DOCUMENT_ACTION, parameters);
        
        HtmlData.AnchorHtmlData anchorHtmlData = new HtmlData.AnchorHtmlData(href,
        		KRADConstants.DOC_HANDLER_METHOD, KRADConstants.MAINTENANCE_EDIT_METHOD_TO_CALL);
        return anchorHtmlData;
    }

    @Override
	protected HtmlData getReturnAnchorHtmlData(BusinessObject businessObject, Properties parameters, LookupForm lookupForm, List returnKeys, BusinessObjectRestrictions businessObjectRestrictions){
    	RoleBo roleBo = (RoleBo) businessObject;
    	HtmlData anchorHtmlData = super.getReturnAnchorHtmlData(businessObject, parameters, lookupForm, returnKeys, businessObjectRestrictions);
    	
    	// prevent derived roles from being selectable (except for identityManagementRoleDocuments)	
    	KualiForm myForm = (KualiForm) GlobalVariables.getUserSession().retrieveObject(getDocFormKey());
    	if (myForm == null || !(myForm instanceof IdentityManagementRoleDocumentForm)){
    		if(KimTypeLookupableHelperServiceImpl.hasDerivedRoleTypeService(KimTypeBo.to(roleBo.getKimRoleType()))){
    			((HtmlData.AnchorHtmlData)anchorHtmlData).setHref("");
    		}
    	}
    	return anchorHtmlData;
    }
    
    @Override
    public List<? extends BusinessObject> getSearchResults(java.util.Map<String,String> fieldValues) {
        //String kimTypeId = "";
        fieldValues.remove(KRADConstants.BACK_LOCATION);
        fieldValues.remove(KRADConstants.DOC_FORM_KEY);
        fieldValues.remove(KRADConstants.DOC_NUM);
    	/*for (Map.Entry<String,String> entry : fieldValues.entrySet()) {
        	if (entry.getKey().equals(KimConstants.PrimaryKeyConstants.KIM_TYPE_ID)) {
        		kimTypeId = entry.getValue();
        		break;
        	}
        }*/
    	List<RoleBo> roles = roleDao.getRoles(fieldValues);
        //List<RoleBo> baseLookup = (List<RoleBo>)super.getSearchResults(fieldValues);

        return roles;
    }

	private List<KeyValue> getRoleTypeOptions() {
		List<KeyValue> options = new ArrayList<KeyValue>();
		options.add(new ConcreteKeyValue("", ""));

		Collection<KimType> kimGroupTypes = KimApiServiceLocator.getKimTypeInfoService().findAllKimTypes();
		// get the distinct list of type IDs from all roles in the system
        for (KimType kimType : kimGroupTypes) {
            if (KimTypeLookupableHelperServiceImpl.hasRoleTypeService(kimType)) {
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
	
	public List<Row> getRoleRows() {
		return this.roleRows;
	}

	public void setRoleRows(List<Row> roleRows) {
		this.roleRows = roleRows;
	}

	public RoleDao getRoleDao() {
		return this.roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
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
	public List<Row> getRows() {
		new ArrayList<Row>();
		if (getRoleRows().isEmpty()) {
			List<Row> rows = super.getRows();
			List<Row> returnRows = new ArrayList<Row>();
			for (Row row : rows) {
				for (int i = row.getFields().size() - 1; i >= 0; i--) {
					Field field = row.getFields().get(i);
					if (field.getPropertyName().equals("kimTypeId")) {
						Field typeField = new Field();
						typeField.setFieldLabel("Type");
						typeField.setPropertyName("kimTypeId");
						typeField.setFieldValidValues(getRoleTypeOptions());
						typeField.setFieldType(Field.DROPDOWN);
						typeField.setMaxLength(100);
						typeField.setSize(40);
						// row.getFields().set(i, new Field("Type", "", Field.DROPDOWN_REFRESH,
						// false, "kimTypeId", "", getGroupTypeOptions(), null));
						row.getFields().set(i, typeField);
					}
				}
				returnRows.add(row);
			}
			setRoleRows(returnRows);
			//setAttrRows(setupAttributeRows());
		}
		if (getAttrRows().isEmpty()) {
			//setAttrDefinitions(new AttributeDefinitionMap());
			return getRoleRows();
		} else {
			List<Row> fullRows = new ArrayList<Row>();
			fullRows.addAll(getRoleRows());
			//fullRows.addAll(getAttrRows());
			return fullRows;
		}
		
	}

	@Override
	protected List<? extends BusinessObject> getSearchResultsHelper(
			Map<String, String> fieldValues, boolean unbounded) {
        List searchResults;
    	Map<String,String> nonBlankFieldValues = new HashMap<String, String>();
    	boolean includeAttr = false;
    	for (String fieldName : fieldValues.keySet()) {
    		if (StringUtils.isNotBlank(fieldValues.get(fieldName)) ) {
    			nonBlankFieldValues.put(fieldName, fieldValues.get(fieldName));
    			if (fieldName.contains(".")) {
    				includeAttr = true;
    			}
    		}
    	}

    	if (includeAttr) {
        	ModuleService eboModuleService = KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService( getBusinessObjectClass() );
        	BusinessObjectEntry ddEntry = eboModuleService.getExternalizableBusinessObjectDictionaryEntry(getBusinessObjectClass());
        	Map<String,String> filteredFieldValues = new HashMap<String, String>();
        	for (String fieldName : nonBlankFieldValues.keySet()) {
        		if (ddEntry.getAttributeNames().contains(fieldName) || fieldName.contains(".")) {
        			filteredFieldValues.put(fieldName, nonBlankFieldValues.get(fieldName));
        		}
        	}
        	searchResults = eboModuleService.getExternalizableBusinessObjectsListForLookup(getBusinessObjectClass(), (Map)filteredFieldValues, unbounded);

    	} else {
    		searchResults = super.getSearchResultsHelper(fieldValues, unbounded);
    	}
        List defaultSortColumns = getDefaultSortColumns();
        if (defaultSortColumns.size() > 0) {
            Collections.sort(searchResults, new BeanPropertyComparator(defaultSortColumns, true));
        }
        return searchResults;

	}
	
	private static final String ROLE_ID_URL_KEY = "&"+KimConstants.PrimaryKeyConstants.ROLE_ID+"=";

	public static String getCustomRoleInquiryHref(String href){
		return getCustomRoleInquiryHref("", href);
	}
	
	static String getCustomRoleInquiryHref(String backLocation, String href){
        Properties parameters = new Properties();
        String hrefPart = "";
    	String docTypeAction = "";
    	if(StringUtils.isBlank(backLocation) || backLocation.contains(KimConstants.KimUIConstants.KIM_ROLE_DOCUMENT_ACTION)
    			|| !backLocation.contains(KimConstants.KimUIConstants.KIM_GROUP_DOCUMENT_ACTION)){
    		docTypeAction = KimConstants.KimUIConstants.KIM_ROLE_INQUIRY_ACTION;
    	} else{
    		docTypeAction = KimConstants.KimUIConstants.KIM_GROUP_DOCUMENT_ACTION;
    	}
		if (StringUtils.isNotBlank(href) && href.contains(ROLE_ID_URL_KEY)) {
			int idx1 = href.indexOf("&"+ KimConstants.PrimaryKeyConstants.ROLE_ID+"=");
		    int idx2 = href.indexOf("&", idx1+1);
		    if (idx2 < 0) {
		    	idx2 = href.length();
		    }
	        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.PARAM_MAINTENANCE_VIEW_MODE_INQUIRY);
	        hrefPart = href.substring(idx1, idx2);
	    }
		return UrlFactory.parameterizeUrl(KimCommonUtilsInternal.getKimBasePath()+docTypeAction, parameters)+hrefPart;
	}

} 
