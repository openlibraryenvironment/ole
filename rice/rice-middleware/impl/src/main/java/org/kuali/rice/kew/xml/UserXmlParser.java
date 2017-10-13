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
package org.kuali.rice.kew.xml;

import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.kuali.rice.core.api.util.xml.XmlException;
import org.kuali.rice.core.api.util.xml.XmlHelper;
import org.kuali.rice.kim.api.identity.CodedAttribute;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.employment.EntityEmploymentBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.principal.PrincipalBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.service.SequenceAccessorService;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Parses users from XML.
 * 
 * This is really meant for use only in the unit tests and was written to help ease
 * transition over to KIM.  There are numerous unit tests which took advantage of
 * the ability to import "users" from XML in KEW.  KIM does not provide XML
 * import capabilities in the initial implementation so this class provides that.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class UserXmlParser {
    
    private static final Namespace NAMESPACE = Namespace.getNamespace("", "ns:workflow/User");

    private static final String USERS_ELEMENT = "users";
    private static final String USER_ELEMENT = "user";
    private static final String WORKFLOW_ID_ELEMENT = "workflowId";
    private static final String AUTHENTICATION_ID_ELEMENT = "authenticationId";
    private static final String PRINCIPAL_ID_ELEMENT = "principalId";
    private static final String PRINCIPAL_NAME_ELEMENT = "principalName";
    private static final String EMPL_ID_ELEMENT = "emplId";
    private static final String EMAIL_ELEMENT = "emailAddress";
    private static final String GIVEN_NAME_ELEMENT = "givenName";
    private static final String LAST_NAME_ELEMENT = "lastName";    
    private static final String TYPE_ELEMENT = "type";
    
    public void parseUsers(InputStream input) throws IOException, XmlException {
        try {
            Document doc = XmlHelper.trimSAXXml(input);
            Element root = doc.getRootElement();
            parseUsers(root);
        } catch (JDOMException e) {
            throw new XmlException("Parse error.", e);
        } catch (SAXException e){
            throw new XmlException("Parse error.",e);
        } catch(ParserConfigurationException e){
            throw new XmlException("Parse error.",e);
        }
    }

    public void parseUsers(Element root) throws XmlException {
    	for (Iterator usersElementIt = root.getChildren(USERS_ELEMENT, NAMESPACE).iterator(); usersElementIt.hasNext();) {
    		Element usersElement = (Element) usersElementIt.next();
    		for (Iterator iterator = usersElement.getChildren(USER_ELEMENT, NAMESPACE).iterator(); iterator.hasNext();) {
    			Element userElement = (Element) iterator.next();
    			EntityBo entity = constructEntity(userElement);
    			constructPrincipal(userElement, entity.getId());
    		}
    	}
    }
    
    protected EntityBo constructEntity(Element userElement) {
        SequenceAccessorService sas = KRADServiceLocator.getSequenceAccessorService();
    	
    	String firstName = userElement.getChildTextTrim(GIVEN_NAME_ELEMENT, NAMESPACE);
        String lastName = userElement.getChildTextTrim(LAST_NAME_ELEMENT, NAMESPACE);
        String emplId = userElement.getChildTextTrim(EMPL_ID_ELEMENT, NAMESPACE);
        String entityTypeCode = userElement.getChildTextTrim(TYPE_ELEMENT, NAMESPACE);
        if (StringUtils.isBlank(entityTypeCode)) {
        	entityTypeCode = "PERSON";
        }
    	
        Long entityId = sas.getNextAvailableSequenceNumber("KRIM_ENTITY_ID_S", 
        		EntityEmploymentBo.class);
        
        // if they define an empl id, let's set that up
        EntityEmploymentBo emplInfo = null;
        if (!StringUtils.isBlank(emplId)) {
        	emplInfo = new EntityEmploymentBo();
        	emplInfo.setActive(true);
        	emplInfo.setEmployeeId(emplId);
        	emplInfo.setPrimary(true);
        	emplInfo.setEntityId("" + entityId);
        	emplInfo.setId(emplId);
        	emplInfo.setEntityAffiliationId(null);
        }
        
    	
		EntityBo entity = new EntityBo();
		entity.setActive(true);
		entity.setId("" + entityId);
		List<EntityEmploymentBo> emplInfos = new ArrayList<EntityEmploymentBo>();
		if (emplInfo != null) {
			emplInfos.add(emplInfo);
		}
		entity.setEmploymentInformation(emplInfos);
		
		EntityTypeContactInfoBo entityType = new EntityTypeContactInfoBo();
		//identity.getEntityTypes().add(entityType);
		entityType.setEntityTypeCode(entityTypeCode);
		entityType.setEntityId(entity.getId());
		entityType.setActive(true);
		entityType.setVersionNumber(new Long(1));
		String emailAddress = userElement.getChildTextTrim(EMAIL_ELEMENT, NAMESPACE);
		if (!StringUtils.isBlank(emailAddress)) {
			Long emailId = sas.getNextAvailableSequenceNumber(
					"KRIM_ENTITY_EMAIL_ID_S", EntityEmailBo.class);
			EntityEmail.Builder email = EntityEmail.Builder.create();
			email.setActive(true);
			email.setId("" + emailId);
			email.setEntityTypeCode(entityTypeCode);
			// must be in krim_email_typ_t.email_typ_cd:
			email.setEmailType(CodedAttribute.Builder.create("WRK"));
			email.setVersionNumber(new Long(1));
			email.setEmailAddress(emailAddress);
			email.setDefaultValue(true);
			email.setEntityId(entity.getId());
			List<EntityEmailBo> emailAddresses = new ArrayList<EntityEmailBo>(1);
			emailAddresses.add(EntityEmailBo.from(email.build()));
			entityType.setEmailAddresses(emailAddresses);
			//email = (KimEntityEmailImpl)KRADServiceLocatorInternal.getBusinessObjectService().save(email);
		}
		List<EntityTypeContactInfoBo> entityTypes = new ArrayList<EntityTypeContactInfoBo>(1);
		entityTypes.add(entityType);
		entity.setEntityTypeContactInfos(entityTypes);
		
		if (!StringUtils.isBlank(firstName) || !StringUtils.isBlank(lastName)) {
			Long entityNameId = sas.getNextAvailableSequenceNumber(
					"KRIM_ENTITY_NM_ID_S", EntityNameBo.class);
			EntityNameBo name = new EntityNameBo();
			name.setActive(true);
			name.setId("" + entityNameId);
			name.setEntityId(entity.getId());
			// must be in krim_ent_nm_typ_t.ent_nm_typ_cd
			name.setNameCode("PRFR");
			name.setFirstName(firstName);
			name.setMiddleName("");
			name.setLastName(lastName);
			name.setDefaultValue(true);
			
			entity.setNames(Collections.singletonList(name));
		}

		entity =  KRADServiceLocator.getBusinessObjectService().save(entity);
		
		return entity;
    }
    
    protected PrincipalBo constructPrincipal(Element userElement, String entityId) {
    	String principalId = userElement.getChildTextTrim(WORKFLOW_ID_ELEMENT, NAMESPACE);
    	if (principalId == null) {
    		principalId = userElement.getChildTextTrim(PRINCIPAL_ID_ELEMENT, NAMESPACE);
    	}
    	String principalName = userElement.getChildTextTrim(AUTHENTICATION_ID_ELEMENT, NAMESPACE);
    	if (principalName == null) {
    		principalName = userElement.getChildTextTrim(PRINCIPAL_NAME_ELEMENT, NAMESPACE);
    	}
    	
		PrincipalBo principal = new PrincipalBo();
		principal.setActive(true);
		principal.setPrincipalId(principalId);
		principal.setPrincipalName(principalName);
		principal.setEntityId(entityId);
		principal = (PrincipalBo) KRADServiceLocator.getBusinessObjectService().save(principal);
		
		return principal;
    }

}
