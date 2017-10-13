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

import org.kuali.rice.coreservice.impl.namespace.NamespaceBo;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.impl.role.RoleBo;
import org.kuali.rice.kim.impl.type.KimTypeBo;
import org.kuali.rice.kim.lookup.RoleLookupableHelperServiceImpl;
import org.kuali.rice.kns.inquiry.KualiInquirableImpl;
import org.kuali.rice.kns.lookup.HtmlData;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.uif.widget.Inquiry;
import org.kuali.rice.krad.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a description of what this class does - bhargavp don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class RoleInquirableImpl extends KualiInquirableImpl {

	protected final String ROLE_NAME = "name";
	protected final String ROLE_ID = "id";
	protected final String NAMESPACE_CODE = "namespaceCode";
	
	@Override
	public void buildInquirableLink(Object dataObject, String propertyName, Inquiry inquiry){
		if(ROLE_NAME.equals(propertyName)){
			Map<String, String> primaryKeys = new HashMap<String, String>();
			primaryKeys.put(ROLE_ID, ROLE_ID);
			inquiry.buildInquiryLink(dataObject, propertyName, RoleBo.class, primaryKeys);
		}else if(NAMESPACE_CODE.equals(propertyName)){
			Map<String, String> primaryKeys = new HashMap<String, String>();
			primaryKeys.put(propertyName, "code");
			inquiry.buildInquiryLink(dataObject, propertyName, NamespaceBo.class, primaryKeys);
		} else if("kimRoleType.name".equals(propertyName)){
			Map<String, String> primaryKeys = new HashMap<String, String>();
			primaryKeys.put("kimRoleType.id", KimConstants.PrimaryKeyConstants.KIM_TYPE_ID);
			inquiry.buildInquiryLink(dataObject, propertyName, KimTypeBo.class, primaryKeys);
        }else{
        	super.buildInquirableLink(dataObject, propertyName, inquiry);
        }
	}
	
    @Override
    public HtmlData getInquiryUrl(BusinessObject businessObject, String attributeName, boolean forceInquiry) {
		if(ROLE_NAME.equals(attributeName)){
			List<String> primaryKeys = new ArrayList<String>();
			primaryKeys.add(ROLE_ID);
		    //((AnchorHtmlData)inqUrl).setHref("../kim/identityManagementRoleDocument.do?methodToCall=inquiry&command=initiate&docTypeName=IdentityManagementRoleDocument"+href.substring(idx1, idx2));
		    String href = (getInquiryUrlForPrimaryKeys(RoleBo.class, businessObject, primaryKeys, null)).getHref();
		    HtmlData.AnchorHtmlData htmlData = new HtmlData.AnchorHtmlData();
		    htmlData.setHref(RoleLookupableHelperServiceImpl.getCustomRoleInquiryHref(href));
			return htmlData;
		} else if(NAMESPACE_CODE.equals(attributeName)){
			List<String> primaryKeys = new ArrayList<String>();
			primaryKeys.add("code");
			NamespaceBo parameterNamespace = new NamespaceBo();
			parameterNamespace.setCode((String)ObjectUtils.getPropertyValue(businessObject, attributeName));
			return getInquiryUrlForPrimaryKeys(NamespaceBo.class, parameterNamespace, primaryKeys, null);
		} else if("kimRoleType.name".equals(attributeName)){
			KimTypeBo kimType = new KimTypeBo();
			kimType.setId( ((RoleBo)businessObject).getKimTypeId() );
			return getInquiryUrlForPrimaryKeys(KimTypeBo.class, kimType, Collections.singletonList( KimConstants.PrimaryKeyConstants.KIM_TYPE_ID ), null);
        }
		
        return super.getInquiryUrl(businessObject, attributeName, forceInquiry);
    }

}
