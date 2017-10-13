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
package org.kuali.rice.kim.impl;

import org.kuali.rice.krad.util.KRADPropertyConstants;

public final class KIMPropertyConstants {

	public static final class Entity {
		public static final String ID = "id";
        public static final String ENTITY_ID = "entityId";
        public static final String ACTIVE = KRADPropertyConstants.ACTIVE;
        public static final String ENTITY_TYPE_CODE = "entityTypeCode";
		
		private Entity() {
			throw new UnsupportedOperationException("do not call");
		}
	}

    public static final class Type {
		public static final String CODE = "code";

		private Type() {
			throw new UnsupportedOperationException("do not call");
		}
	}
	
	public static final class Principal {
		public static final String PRINCIPAL_ID = "principalId";
		public static final String PRINCIPAL_NAME = "principalName";
		public static final String PASSWORD = "password";
		public static final String ACTIVE = KRADPropertyConstants.ACTIVE;
		
		private Principal() {
			throw new UnsupportedOperationException("do not call");
		}
	}

	public static final class Person {
		public static final String ENTITY_ID = "entityId";
		public static final String PRINCIPAL_ID = Principal.PRINCIPAL_ID;
		public static final String PRINCIPAL_NAME = Principal.PRINCIPAL_NAME;
		public static final String FIRST_NAME = "firstName";
		public static final String MIDDLE_NAME = "middleName";
		public static final String LAST_NAME = "lastName";
		public static final String NAME = "name";
		public static final String EMAIL_ADDRESS = KRADPropertyConstants.EMAIL_ADDRESS;
		public static final String PHONE_NUMBER = "phoneNumber";
		public static final String ACTIVE = KRADPropertyConstants.ACTIVE;
		public static final String EMPLOYEE_ID = "employeeId";
		public static final String EMPLOYEE_STATUS_CODE = "employeeStatusCode";
		public static final String EMPLOYEE_TYPE_CODE = "employeeTypeCode";
		public static final String EXTERNAL_ID = "externalId";
		public static final String EXTERNAL_IDENTIFIER_TYPE_CODE = "externalIdentifierTypeCode";
		public static final String ADDRESS_LINE_1 = "line1";
		public static final String ADDRESS_LINE_2 = "line2";
		public static final String ADDRESS_LINE_3 = "line3";
        public static final String CITY = "city";
        public static final String STATE_CODE = KRADPropertyConstants.STATE_CODE;
		public static final String POSTAL_CODE = KRADPropertyConstants.POSTAL_CODE;
		public static final String COUNTRY_CODE = KRADPropertyConstants.COUNTY_CODE;
		public static final String CAMPUS_CODE = KRADPropertyConstants.CAMPUS_CODE;
		public static final String AFFILIATION_TYPE_CODE = "affiliationTypeCode";
		public static final String PRIMARY_DEPARTMENT_CODE = "primaryDepartmentCode";
		public static final String BASE_SALARY_AMOUNT = "baseSalaryAmount";
		
		private Person() {
			throw new UnsupportedOperationException("do not call");
		}
	}
	
	public static final class Group {
	    public static final String GROUP_ID = "id";
        public static final String GROUP_NAME = "name";
        
		private Group() {
			throw new UnsupportedOperationException("do not call");
		}
	}
	
	public static final class KimType {
	    public static final String KIM_TYPE_ID = "kimTypeId";
	    
		private KimType() {
			throw new UnsupportedOperationException("do not call");
		}
	}
	
	public static final class Role {
		public static final String ROLE_ID = "id";
		public static final String ROLE_NAME = "name";
	    public static final String KIM_TYPE_ID = KimType.KIM_TYPE_ID;
		public static final String ACTIVE = KRADPropertyConstants.ACTIVE;
		
		private Role() {
			throw new UnsupportedOperationException("do not call");
		}
	}

	public static final class KimMember {
		public static final String MEMBER_ID = "id";
		public static final String MEMBER_TYPE_CODE = "typeCode";
		public static final String ACTIVE_FROM_DATE = "activeFromDate";
		public static final String ACTIVE_TO_DATE = "activeToDate";
		
		private KimMember() {
			throw new UnsupportedOperationException("do not call");
		}
	}
	
	public static final class RoleMember {
		public static final String ROLE_MEMBER_ID = "roleMemberId";
		public static final String ROLE_ID = "roleId";
		public static final String MEMBER_ID = "memberId";
		public static final String MEMBER_TYPE_CODE = "typeCode";
		public static final String ACTIVE_FROM_DATE = "activeFromDateValue";
		public static final String ACTIVE_TO_DATE = "activeToDateValue";
		
		private RoleMember() {
			throw new UnsupportedOperationException("do not call");
		}
	}

	public static final class GroupMember {
		public static final String GROUP_MEMBER_ID = "id";
		public static final String GROUP_ID = "groupId";
		public static final String MEMBER_ID = "memberId";
		public static final String MEMBER_TYPE_CODE = KimMember.MEMBER_TYPE_CODE;
		public static final String ACTIVE_FROM_DATE = KimMember.ACTIVE_FROM_DATE;
		public static final String ACTIVE_TO_DATE = KimMember.ACTIVE_TO_DATE;
		
		private GroupMember() {
			throw new UnsupportedOperationException("do not call");
		}
	}
	
	public static final class DelegationMember {
		public static final String DELEGATION_MEMBER_ID = "delegationMemberId";
		public static final String DELEGATION_ID = Delegation.DELEGATION_ID;
		public static final String MEMBER_ID = "memberId";
		public static final String MEMBER_TYPE_CODE = "typeCode";
		public static final String ACTIVE_FROM_DATE = KimMember.ACTIVE_FROM_DATE;
		public static final String ACTIVE_TO_DATE = KimMember.ACTIVE_TO_DATE;
		
		private DelegationMember() {
			throw new UnsupportedOperationException("do not call");
		}
	}
	
	public static final class Delegation {
		public static final String ROLE_ID = "roleId";
		public static final String DELEGATION_ID = "delegationId";
		public static final String ACTIVE = KRADPropertyConstants.ACTIVE;
		
		private Delegation() {
			throw new UnsupportedOperationException("do not call");
		}
	}
	
	private KIMPropertyConstants() {
		throw new UnsupportedOperationException("do not call");
	}
}
