/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.kns.maintainable;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.kns.maintenance.KualiMaintainableImpl;
import org.kuali.rice.krad.exception.PessimisticLockingException;
import org.kuali.rice.krad.util.GlobalVariables;

/**
 * This is a subclass of KualiMaintainableImpl designed specifically for testing custom lock descriptors. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AccountType2MaintainableImpl extends KualiMaintainableImpl {
	private static final long serialVersionUID = -396652994644246467L;

    /**
     * A test key that maps to a UserSession variable indicating what part of the maintainable or BO is editable. Note that this particular maintainable
     * does not try to enforce such restrictions, since it is simply here for testing the custom lock descriptor functionality of PessimisticLockService.
     */
	public static final String ACCT_TYPE_2_MAINT_FIELDS_TO_EDIT = "acctType2MaintFieldsToEdit"; 
    /**
     * A test value indicating that the user has permission to edit only the AccountType2's accountTypeCode. This is here only for testing the
     * generation of custom lock descriptors for PessimisticLockServiceTest; the maintainable and its BO do not actually enforce any such behavior.
     */
	public static final String EDIT_CODE_ONLY = "editCodeOnly";
    /**
     * A test value indicating that the user has permission to edit only the AccountType2's name. This is here only for testing the
     * generation of custom lock descriptors for PessimisticLockServiceTest; the maintainable and its BO do not actually enforce any such behavior.
     */
	public static final String EDIT_NAME_ONLY = "editNameOnly";
	
	/**
	 * Since this class was made to test custom lock descriptors, this method will always return true.
	 * 
	 * @see org.kuali.rice.krad.maintenance.KualiMaintainableImpl#useCustomLockDescriptors()
	 */
	@Override
	public boolean useCustomLockDescriptors() {
		return true;
	}
	
	/**
     * Generates a custom lock descriptor with a format of "[documentNumber]-[The user session's 'acctType2MaintFieldsToEdit' parameter value]".
     * Will throw a PessimisticLockingException if this parameter is not defined or if its value is neither "editCodeOnly" nor "editNameOnly".
	 * 
	 * @see org.kuali.rice.krad.maintenance.KualiMaintainableImpl#getCustomLockDescriptor(org.kuali.rice.kim.api.identity.Person)
	 */
	@Override
	public String getCustomLockDescriptor(Person user) {
    	String fieldsToEdit = (String) GlobalVariables.getUserSession().retrieveObject(ACCT_TYPE_2_MAINT_FIELDS_TO_EDIT);
    	if (fieldsToEdit == null) {
    		throw new PessimisticLockingException("The user session's '" + ACCT_TYPE_2_MAINT_FIELDS_TO_EDIT + "' property cannot be null");
    	}
    	else if (!EDIT_CODE_ONLY.equals(fieldsToEdit) && !EDIT_NAME_ONLY.equals(fieldsToEdit)) {
    		throw new PessimisticLockingException("The value of the user session's '" + ACCT_TYPE_2_MAINT_FIELDS_TO_EDIT + "' property is invalid");
    	}
    	return getDocumentNumber() + "-" + fieldsToEdit;
	}
}
