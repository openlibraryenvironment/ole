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
package org.kuali.rice.kim.impl.responsibility;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.common.template.Template;
import org.kuali.rice.kim.api.responsibility.Responsibility;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kim.impl.common.attribute.KimAttributeDataBo;
import org.kuali.rice.kim.impl.permission.PermissionTemplateBo;
import org.kuali.rice.kns.document.MaintenanceDocument;
import org.kuali.rice.kns.maintenance.KualiMaintainableImpl;
import org.kuali.rice.kns.maintenance.Maintainable;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.kns.web.ui.Section;
import org.kuali.rice.krad.bo.BusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.util.KRADConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewResponsibilityMaintainable extends KualiMaintainableImpl {

	private static final Logger LOG = Logger.getLogger( ReviewResponsibilityMaintainable.class );
	private static final long serialVersionUID = -8102504656976243468L;

	private static Template REVIEW_TEMPLATE;

    public List getSections(MaintenanceDocument document, Maintainable oldMaintainable) {
        List<Section> sections = super.getSections(document, oldMaintainable);
        if(document.isEdit()){
        	for (Section section : sections) {
                for (Row row : section.getRows()) {
                    for (Field field : row.getFields()) {
                    	if(ReviewResponsibilityBo.ACTION_DETAILS_AT_ROLE_MEMBER_LEVEL_FIELD_NAME.equals(field.getPropertyName())){
                    		field.setReadOnly(true);
                    	}
                    }
                }
        	}
        }
        return sections;
    }
                
	/**
	 * Saves the responsibility via the responsibility service
	 * 
	 * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#saveDataObject()
	 */
	@Override
	public void saveDataObject() {
        if ( LOG.isInfoEnabled() ) {
            LOG.info( "Attempting to save ReviewResponsibilityBo BO via ResponsibilityService:" + getBusinessObject() );
        }
        populateReviewTemplateInfo();

        ReviewResponsibilityBo resp = (ReviewResponsibilityBo)getDataObject();
        // build the Map<String, String> with the details
        Map<String, String> details = new HashMap<String, String>();
        details.put( KimConstants.AttributeConstants.DOCUMENT_TYPE_NAME, resp.getDocumentTypeName() );
        details.put( KimConstants.AttributeConstants.ROUTE_NODE_NAME, resp.getRouteNodeName() );
        details.put( KimConstants.AttributeConstants.REQUIRED, resp.isRequired()?"true":"false" );
        details.put( KimConstants.AttributeConstants.ACTION_DETAILS_AT_ROLE_MEMBER_LEVEL, resp.isActionDetailsAtRoleMemberLevel()?"true":"false" );
        if ( StringUtils.isNotBlank(resp.getQualifierResolverProvidedIdentifier()) ) {
            details.put( KimConstants.AttributeConstants.QUALIFIER_RESOLVER_PROVIDED_IDENTIFIER, resp.getQualifierResolverProvidedIdentifier() );
        }

        //ResponsibilityBo bo = ReviewResponsibilityBo.toResponsibilityBo(resp);
        if (resp.getTemplateId() == null) {
            resp.setTemplateId(REVIEW_TEMPLATE.getId());
            resp.setTemplate(ResponsibilityTemplateBo.from(REVIEW_TEMPLATE));
        }

        else {
                resp.setTemplate(
                        ResponsibilityTemplateBo.from(KimApiServiceLocator.getResponsibilityService().getResponsibilityTemplate(
                                resp.getTemplateId())));
        }
        //set attributes
        resp.setAttributes(details);
        resp.setAttributeDetails(KimAttributeDataBo.createFrom(ResponsibilityAttributeBo.class, details, resp.getTemplate().getKimTypeId()));

        boolean responsibilityExists = false;
        if (resp.getId() != null) {
            responsibilityExists = KimApiServiceLocator.getResponsibilityService().getResponsibility(resp.getId()) != null;
        }

        if (responsibilityExists) {
            KimApiServiceLocator.getResponsibilityService().updateResponsibility(ResponsibilityBo.to(resp));
        } else {
            //if its a copy action the objectId should be  empty and versionNumber should be null.
            if(getMaintenanceAction().equals(KRADConstants.MAINTENANCE_COPY_ACTION)){
                if(org.apache.commons.lang.StringUtils.isNotBlank(resp.getObjectId())){
                    resp.setObjectId("");
                }
                if(null!= resp.getVersionNumber()){
                    resp.setVersionNumber(null);
                }
            }
            KimApiServiceLocator.getResponsibilityService().createResponsibility(ResponsibilityBo.to(resp));
        }
	}
	
	private static synchronized void populateReviewTemplateInfo() {
		if ( REVIEW_TEMPLATE == null ) {
		    REVIEW_TEMPLATE = KimApiServiceLocator.getResponsibilityService().findRespTemplateByNamespaceCodeAndName(KewApiConstants.KEW_NAMESPACE, KewApiConstants.DEFAULT_RESPONSIBILITY_TEMPLATE_NAME);
        }
	}

	@Override
	public Class<? extends BusinessObject> getBoClass() {
		return ReviewResponsibilityBo.class;
	}

	@Override
	public void prepareBusinessObject(BusinessObject businessObject) {
        if ( businessObject == null ) {
            throw new RuntimeException( "Configuration ERROR: ReviewResponsibilityBoMaintainable.prepareBusinessObject passed a null object." );
        }
        if ( businessObject instanceof ResponsibilityBo ) {
            ResponsibilityBo resp = getBusinessObjectService().findBySinglePrimaryKey(ResponsibilityBo.class, ((ResponsibilityBo)businessObject).getId() );
            businessObject = new ReviewResponsibilityBo( resp );
            setBusinessObject( (PersistableBusinessObject)businessObject );
        } else {
            throw new RuntimeException( "Configuration ERROR: ReviewResponsibilityBoMaintainable passed an unsupported object type: " + businessObject.getClass() );
        }
        super.prepareBusinessObject(businessObject);
	}

    /**
	 * This overridden method ...
	 *
	 * @see org.kuali.rice.kns.maintenance.KualiMaintainableImpl#isExternalBusinessObject()
	 */
	@Override
	public boolean isExternalBusinessObject() {
		return true;
	}

}
