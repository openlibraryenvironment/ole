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
package org.kuali.rice.kim.inquiry;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.coreservice.impl.namespace.NamespaceBo;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.impl.group.GroupBo;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.kim.util.KimCommonUtilsInternal;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.uif.widget.Inquiry;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.util.UrlFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * This is a description of what this class does - kellerj don't forget to fill this in.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class GroupInquirableImpl extends KualiInquirableImpl {


	protected final String GROUP_NAME = "name";
	protected final String GROUP_ID = "id";
	protected final String NAMESPACE_CODE = "namespaceCode";
	
	@Override
	public void buildInquirableLink(Object dataObject, String propertyName, Inquiry inquiry){
		
		if(GROUP_NAME.equals(propertyName)){
			Map<String, String> primaryKeys = new HashMap<String, String>();
			primaryKeys.put(GROUP_ID, GROUP_ID);
			inquiry.buildInquiryLink(dataObject, propertyName, GroupBo.class, primaryKeys);
		} else if(NAMESPACE_CODE.equals(propertyName)){
			Map<String, String> primaryKeys = new HashMap<String, String>();
			primaryKeys.put(propertyName, "code");
			inquiry.buildInquiryLink(dataObject, propertyName, NamespaceBo.class, primaryKeys);
		} else if("kimTypeInfo.name".equals(propertyName)){
			Map<String, String> primaryKeys = new HashMap<String, String>();
			primaryKeys.put("kimTypeInfo.id", KimConstants.PrimaryKeyConstants.KIM_TYPE_ID);
			inquiry.buildInquiryLink(dataObject, propertyName, KimTypeBo.class, primaryKeys);
        }
	}
	
    @Override
    public HtmlData getInquiryUrl(BusinessObject businessObject, String attributeName, boolean forceInquiry) {
		if(GROUP_NAME.equals(attributeName)){
			List<String> primaryKeys = new ArrayList<String>();
			primaryKeys.add(GROUP_ID);
		    String href = (getInquiryUrlForPrimaryKeys(GroupBo.class, businessObject, primaryKeys, null)).getHref();
		    HtmlData.AnchorHtmlData htmlData = new HtmlData.AnchorHtmlData();
		    htmlData.setHref(getCustomGroupInquiryHref(href));
			return htmlData;
		} else if(NAMESPACE_CODE.equals(attributeName)){
			List<String> primaryKeys = new ArrayList<String>();
			primaryKeys.add("code");
			NamespaceBo parameterNamespace = new NamespaceBo();
			parameterNamespace.setCode((String)ObjectUtils.getPropertyValue(businessObject, attributeName));
			return getInquiryUrlForPrimaryKeys(NamespaceBo.class, parameterNamespace, primaryKeys, null);
		} else if("kimTypeInfo.name".equals(attributeName)){
			KimTypeBo kimType = new KimTypeBo();
			kimType.setId( ((GroupBo)businessObject).getKimTypeId() );
			return getInquiryUrlForPrimaryKeys(KimTypeBo.class, kimType, Collections.singletonList( KimConstants.PrimaryKeyConstants.KIM_TYPE_ID ), null);
        }
		
        return super.getInquiryUrl(businessObject, attributeName, forceInquiry);
    }

	public String getCustomGroupInquiryHref(String href){
        Properties parameters = new Properties();
        String hrefPart = "";
		if (StringUtils.isNotBlank(href) && href.contains("&" + KimConstants.PrimaryKeyConstants.GROUP_ID + "=")) {
			int idx1 = href.indexOf("&"+KimConstants.PrimaryKeyConstants.GROUP_ID+"=");
		    int idx2 = href.indexOf("&", idx1+1);
		    if (idx2 < 0) {
		    	idx2 = href.length();
		    }
	        parameters.put(KRADConstants.DISPATCH_REQUEST_PARAMETER, KRADConstants.PARAM_MAINTENANCE_VIEW_MODE_INQUIRY);
	        hrefPart = href.substring(idx1, idx2);
	    }
		return UrlFactory.parameterizeUrl(KimCommonUtilsInternal.getKimBasePath()+
				KimConstants.KimUIConstants.KIM_GROUP_INQUIRY_ACTION, parameters)+hrefPart;
	}

	/**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.krad.inquiry.KualiInquirableImpl#getBusinessObject(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BusinessObject getBusinessObject(Map fieldValues) {
	    BusinessObject bo = super.getBusinessObject(fieldValues);
	    return bo;
	}

}
