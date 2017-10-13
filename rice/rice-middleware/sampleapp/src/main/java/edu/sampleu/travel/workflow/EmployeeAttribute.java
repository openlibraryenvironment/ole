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
package edu.sampleu.travel.workflow;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.util.ConcreteKeyValue;
import org.kuali.rice.kew.api.WorkflowRuntimeException;
import org.kuali.rice.kew.api.identity.Id;
import org.kuali.rice.kew.api.identity.PrincipalId;
import org.kuali.rice.kew.api.identity.PrincipalName;
import org.kuali.rice.kew.api.rule.RoleName;
import org.kuali.rice.kew.api.user.UserId;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.exception.WorkflowServiceErrorImpl;
import org.kuali.rice.kew.routeheader.DocumentContent;
import org.kuali.rice.kew.rule.GenericRoleAttribute;
import org.kuali.rice.kew.rule.QualifiedRoleName;
import org.kuali.rice.kew.rule.ResolvedQualifiedRole;
import org.kuali.rice.kim.api.identity.principal.Principal;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An attribute implementation that can resolve organizational roles
 */
public class EmployeeAttribute extends GenericRoleAttribute {
    private static final RoleName EMPLOYEE_ROLE = new RoleName(EmployeeAttribute.class.getName(), "employee", "Employee");
    private static final RoleName SUPERVISOR_ROLE = new RoleName(EmployeeAttribute.class.getName(), "supervisr", "Supervisor");
    private static final RoleName DIRECTOR_ROLE = new RoleName(EmployeeAttribute.class.getName(), "director", "Dean/Director");
    private static final List<RoleName> ROLES;
    static {
        List<RoleName> tmp = new ArrayList<RoleName>(1);
        tmp.add(EMPLOYEE_ROLE);
        tmp.add(SUPERVISOR_ROLE);
        tmp.add(DIRECTOR_ROLE);
        ROLES = Collections.unmodifiableList(tmp);
    }

	private static String USERID_FORM_FIELDNAME = "userid";

    /**
     * Traveler to be set by client application so that doc content can be generated appropriately
     */
	private String traveler;

	//private AttributeParser _attributeParser = new AttributeParser(ATTRIBUTE_TAGNAME);

	public EmployeeAttribute() {
        super("employee");
	}

	public EmployeeAttribute(String traveler) {
        super("employee");
		this.traveler = traveler;
	}

    /** for edoclite?? */
    public void setTraveler(String traveler) {
        this.traveler = traveler;
    }

	/* RoleAttribute methods */
	public List<RoleName> getRoleNames() {
        return ROLES;
	}

    protected boolean isValidRole(String roleName) {
        for (RoleName role: ROLES) {
            if (role.getBaseName().equals(roleName)) {
                return true;
            }
        }
        return false;
    }


	@Override
    protected List<String> getRoleNameQualifiers(String roleName, DocumentContent documentContent) {
        if (!isValidRole(roleName)) {
            throw new WorkflowRuntimeException("Invalid role: " + roleName);
        }

        List<String> qualifiers = new ArrayList<String>();
        qualifiers.add(roleName);
        // find all traveller inputs in incoming doc
//        List<Map<String, String>> attrs;
//        try {
//            attrs = content.parseContent(documentContent.getAttributeContent());
//        } catch (XPathExpressionException xpee) {
//            throw new WorkflowRuntimeException("Error parsing attribute content: " + XmlJotter.jotNode(documentContent.getAttributeContent()));
//        }
//        for (Map<String, String> props: attrs) {
//            String attrTraveler = props.get("traveler");
//            if (attrTraveler != null) {
//                qualifiers.add(attrTraveler);
//            }
//        }
        return qualifiers;
    }

	@Override
	protected ResolvedQualifiedRole resolveQualifiedRole(RouteContext routeContext, QualifiedRoleName qualifiedRoleName) {
        List<Id> recipients = resolveRecipients(routeContext, qualifiedRoleName);
        ResolvedQualifiedRole rqr = new ResolvedQualifiedRole(getLabelForQualifiedRoleName(qualifiedRoleName),
                                                              recipients,
                                                              qualifiedRoleName.getBaseRoleName()); // default to no annotation...
        return rqr;
    }
	
	@Override
    protected List<Id> resolveRecipients(RouteContext routeContext, QualifiedRoleName qualifiedRoleName) {
        List<Id> members = new ArrayList<Id>();
        UserId roleUserId = null;
        String roleName = qualifiedRoleName.getBaseRoleName();
        String roleTraveler = qualifiedRoleName.getQualifier();

        /* EMPLOYEE role routes to traveler */
        if (StringUtils.equals(EMPLOYEE_ROLE.getBaseName(), roleName)) {
            roleUserId = new PrincipalId(roleTraveler);

        /* SUPERVISOR role routes to... supervisor */
        } else if (StringUtils.equals(SUPERVISOR_ROLE.getBaseName(), roleName)) {
            // HACK: need to create an organizational-hierarchy service which
            // has methods like
            // getSupervisor( user ), getDirector( user ), getSupervised( user
            // ), etc.
            // using q.uhuuid() as input
            roleUserId = new PrincipalName("supervisr");

        /* SUPERVISOR role routes to... director */
        } else if (StringUtils.equals(DIRECTOR_ROLE.getBaseName(), roleName)) {
            // HACK: need to create an organizational-hierarchy service which
            // has methods like
            // getSupervisor( user ), getDirector( user ), getSupervised( user
            // ), etc.
            // using q.uhuuid() as input
            roleUserId = new PrincipalName("director");
        } else {
            // throw an exception if you get an unrecognized roleName
            throw new WorkflowRuntimeException("unable to process unknown role '" + roleName + "'");
        }
        members.add(roleUserId);

        return members;
    }

    public Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("traveler", traveler);
        return properties;
    }

	/**
	 * Required to support flex routing report
	 *
	 * @see org.kuali.rice.kew.rule.WorkflowRuleAttribute#getFieldConversions()
	 */
	public List getFieldConversions() {
		List conversionFields = new ArrayList();
		conversionFields.add(new ConcreteKeyValue("userid", USERID_FORM_FIELDNAME));
		return conversionFields;
	}

	public List<Row> getRoutingDataRows() {
		List<Row> rows = new ArrayList<Row>();

		List<Field> fields = new ArrayList<Field>();
		fields.add(new Field("Traveler username", "", Field.TEXT, false, USERID_FORM_FIELDNAME, "", false, false, null, null));
		rows.add(new Row(fields));

		return rows;
	}

	public List validateRoutingData(Map paramMap) {
		List errors = new ArrayList();

		String userid = StringUtils.trim((String) paramMap.get(USERID_FORM_FIELDNAME));
		if (isRequired() && StringUtils.isBlank(userid)) {
			errors.add(new WorkflowServiceErrorImpl("userid is required", "uh.accountattribute.userid.required"));
		}

		Principal principal = null;
		if (!StringUtils.isBlank(userid)) {
			principal = KimApiServiceLocator.getIdentityService().getPrincipalByPrincipalName(userid);
		}
		if (principal == null) {
			errors.add(new WorkflowServiceErrorImpl("unable to retrieve user for userid '" + userid + "'", "uh.accountattribute.userid.invalid"));
		}
	
		if (errors.size() == 0) {
			traveler = principal.getPrincipalId();
		}

		return errors;
	}
}
