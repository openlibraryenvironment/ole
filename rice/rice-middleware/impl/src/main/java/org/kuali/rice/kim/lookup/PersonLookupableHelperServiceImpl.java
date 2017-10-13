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
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kim.impl.KIMPropertyConstants;
import org.kuali.rice.kim.impl.identity.PersonImpl;
import org.kuali.rice.kim.util.KimCommonUtilsInternal;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.UrlFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class PersonLookupableHelperServiceImpl  extends KimLookupableHelperServiceImpl {
	
	private static final long serialVersionUID = 1971744785776844579L;
	
	@Override
	public List<? extends BusinessObject> getSearchResults(
			Map<String, String> fieldValues) {
		if (fieldValues != null && StringUtils.isNotEmpty(fieldValues.get(KIMPropertyConstants.Person.PRINCIPAL_NAME))) {
			fieldValues.put(KIMPropertyConstants.Person.PRINCIPAL_NAME, fieldValues.get(KIMPropertyConstants.Person.PRINCIPAL_NAME).toLowerCase());
		}

		return super.getSearchResults(fieldValues);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HtmlData> getCustomActionUrls(BusinessObject bo, List pkNames) {
        List<HtmlData> anchorHtmlDataList = new ArrayList<HtmlData>();
		if(allowsNewOrCopyAction(KimConstants.KimUIConstants.KIM_PERSON_DOCUMENT_TYPE_NAME)){
			String href = "";
			Properties parameters = new Properties();
	        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.DOC_HANDLER_METHOD);
	        parameters.put(KRADConstants.PARAMETER_COMMAND, KewApiConstants.INITIATE_COMMAND);
	        parameters.put(KRADConstants.DOCUMENT_TYPE_NAME, KimConstants.KimUIConstants.KIM_PERSON_DOCUMENT_TYPE_NAME);
	        parameters.put(KimConstants.PrimaryKeyConstants.PRINCIPAL_ID, ((PersonImpl)bo).getPrincipalId());
	        if (StringUtils.isNotBlank(getReturnLocation())) {
	        	parameters.put(KRADConstants.RETURN_LOCATION_PARAMETER, getReturnLocation());
			}
	        href = UrlFactory.parameterizeUrl(KimCommonUtilsInternal.getKimBasePath()+KimConstants.KimUIConstants.KIM_PERSON_DOCUMENT_ACTION, parameters);
	
	        HtmlData.AnchorHtmlData anchorHtmlData = new HtmlData.AnchorHtmlData(href,
	        		KRADConstants.DOC_HANDLER_METHOD, KRADConstants.MAINTENANCE_EDIT_METHOD_TO_CALL);
	
	    	anchorHtmlDataList.add(anchorHtmlData);
		}
    	return anchorHtmlDataList;
	}

	@Override
	public HtmlData getInquiryUrl(BusinessObject bo, String propertyName) {
		HtmlData inqUrl = super.getInquiryUrl(bo, propertyName);
		Properties parameters = new Properties();
        parameters.put(KewApiConstants.COMMAND_PARAMETER, KewApiConstants.INITIATE_COMMAND);
        parameters.put(KRADConstants.DOCUMENT_TYPE_NAME, KimConstants.KimUIConstants.KIM_PERSON_DOCUMENT_TYPE_NAME);
        parameters.put(KimConstants.PrimaryKeyConstants.PRINCIPAL_ID, ((Person)bo).getPrincipalId());
        String href = UrlFactory.parameterizeUrl(KimCommonUtilsInternal.getKimBasePath()+ KimConstants.KimUIConstants.KIM_PERSON_INQUIRY_ACTION, parameters);
	    ((HtmlData.AnchorHtmlData)inqUrl).setHref(href);
	    return inqUrl;
	}
	
	/**
	 * Checks for the special role lookup parameters and removes/marks read-only the fields in the search criteria.
	 * If present, this method also has a side-effect of updating the title with the role name.
	 * 
	 * @see org.kuali.rice.krad.lookup.AbstractLookupableHelperServiceImpl#getRows()
	 */
	@Override
	public List<Row> getRows() {
		title.remove(); 
		List<Row> rows = super.getRows();
		Iterator<Row> i = rows.iterator();
		String roleName = null;
		String namespaceCode = null;
		while ( i.hasNext() ) {
			Row row = i.next();
			int numFieldsRemoved = 0;
			boolean rowIsBlank = true;
			// Since multi-column lookups can be specified on Rice lookups, all the fields in each row must be iterated over as well,
			// just in case the person lookup ever gets configured to have multiple columns per row.
			for (Iterator<Field> fieldIter = row.getFields().iterator(); fieldIter.hasNext();) {
			    Field field = fieldIter.next();
			    String propertyName = field.getPropertyName();
			    if ( propertyName.equals("lookupRoleName") ) {
				    Object val = getParameters().get( propertyName );
				    String propVal = null;
				    if ( val != null ) {
					    propVal = (val instanceof String)?(String)val:((String[])val)[0];
				    }
				    if ( StringUtils.isBlank( propVal ) ) {
					    fieldIter.remove();
					    numFieldsRemoved++;
				    } else {
					    field.setReadOnly(true);
					    field.setDefaultValue( propVal );
					    roleName = propVal;
					    rowIsBlank = false;
				    }
			    } else if ( propertyName.equals("lookupRoleNamespaceCode") ) {
				    Object val = getParameters().get( propertyName );
				    String propVal = null;
				    if ( val != null ) {
					    propVal = (val instanceof String)?(String)val:((String[])val)[0];
				    }
				    if ( StringUtils.isBlank( propVal ) ) {
					    fieldIter.remove();
					    numFieldsRemoved++;
				    } else {
					    field.setReadOnly(true);
					    field.setDefaultValue( propVal );
					    namespaceCode = propVal;
					    rowIsBlank = false;
				    }				
			    } else if (!Field.BLANK_SPACE.equals(field.getFieldType())) {
			    	rowIsBlank = false;
			    }
			}
			// Check if any fields were removed from the row.
			if (numFieldsRemoved > 0) {
				// If fields were removed, check whether or not the remainder of the row is empty or only has blank space fields.
				if (rowIsBlank) {
					// If so, then remove the row entirely.
					i.remove();
				} else {
					// Otherwise, add one blank space for each field that was removed, using a technique similar to FieldUtils.createBlankSpace.
					// It is safe to just add blank spaces as needed, since the two removable fields are the last of the visible ones in the list.
					while (numFieldsRemoved > 0) {
						Field blankSpace = new Field();
						blankSpace.setFieldType(Field.BLANK_SPACE);
						blankSpace.setPropertyName(Field.BLANK_SPACE);
						row.getFields().add(blankSpace);
						numFieldsRemoved--;
					}
				}
			}
		}
		if ( roleName != null && namespaceCode != null ) {
			title.set( namespaceCode + " " + roleName + " Lookup" );
		}
		return rows;
	}
	
	private ThreadLocal<String> title = new ThreadLocal<String>();
	public String getTitle() {
		if ( title.get() == null ) {
			return super.getTitle();
		}
		return title.get();
	}
}
