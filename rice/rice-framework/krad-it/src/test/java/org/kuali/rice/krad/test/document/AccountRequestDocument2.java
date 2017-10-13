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
package org.kuali.rice.krad.test.document;

import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.document.SessionDocument;
import org.kuali.rice.krad.document.TransactionalDocumentBase;
import org.kuali.rice.krad.exception.PessimisticLockingException;
import org.kuali.rice.krad.test.document.bo.Account;
import org.kuali.rice.krad.util.GlobalVariables;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a test copy of AccountRequestDocument that has been modified to allow for custom lock descriptors.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AccountRequestDocument2 extends TransactionalDocumentBase implements SessionDocument {

    private static final long serialVersionUID = 7616690365412064056L;
    
    /**
     * A test key to map to some value indicating what fields the user can edit on this doc. Note that such behavior is not enforced; it is simply
     * here for testing the PessimisticLockService's custom lock descriptor functionality.
     */
    public static final String ACCT_REQ_DOC_2_EDITABLE_FIELDS = "acctReqDoc2EditableFields";
    /**
     * A test value indicating that the user has permission to edit everything except "reason1" and "reason2". This is here only for testing the
     * generation of custom lock descriptors for PessimisticLockServiceTest; AccountRequestDocument2 does not actually enforce any such behavior.
     */
    public static final String EDIT_ALL_BUT_REASONS = "editAllButReasons";
    /**
     * A test value indicating that the user only has permission to edit "reason1" and "reason2". This is here only for testing the generation of
     * custom lock descriptors for PessimisticLockServiceTest; AccountRequestDocument2 does not actually enforce any such behavior.
     */
    public static final String EDIT_REASONS_ONLY = "editReasonsOnly";
    
	private String requester;
    private String reason1;
    private String reason2;
    private String requestType;
    private String accountTypeCode;

    private List<Account> accounts;

    public AccountRequestDocument2() {
        accounts = new ArrayList<Account>();
    }

    public String getReason2() {
        return reason2;
    }

    public void setReason2(String reason2) {
        this.reason2 = reason2;
    }

    public String getReason1() {
        return reason1;
    }

    public void setReason1(String reason1) {
        this.reason1 = reason1;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Account getAccount(int index) {
        while(accounts.size() - 1 < index) {
            accounts.add(new Account());
        }
        return accounts.get(index);
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setAccountTypeCode(String accountType) {
        this.accountTypeCode = accountType;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    /**
     * Since this class was made to test custom lock descriptors, this method will always return true.
     * 
     * @see org.kuali.rice.krad.document.DocumentBase#useCustomLockDescriptors()
     */
    @Override
    public boolean useCustomLockDescriptors() {
    	return true;
    }
    
    /**
     * Generates a custom lock descriptor with a format of "[documentNumber]-[The user session's 'acctReqDoc2EditableFields' parameter value]".
     * Will throw a PessimisticLockingException if this parameter is not defined or if its value is neither "editAllButReasons" nor "editReasonsOnly".
     * 
     * @see org.kuali.rice.krad.document.DocumentBase#getCustomLockDescriptor(org.kuali.rice.kim.api.identity.Person)
     */
    @Override
    public String getCustomLockDescriptor(Person user) {
    	String editableFields = (String) GlobalVariables.getUserSession().retrieveObject(ACCT_REQ_DOC_2_EDITABLE_FIELDS);
    	if (editableFields == null) {
    		throw new PessimisticLockingException("The user session's '" + ACCT_REQ_DOC_2_EDITABLE_FIELDS + "' property cannot be null");
    	}
    	else if (!EDIT_ALL_BUT_REASONS.equals(editableFields) && !EDIT_REASONS_ONLY.equals(editableFields)) {
    		throw new PessimisticLockingException("The value of the user session's '" + ACCT_REQ_DOC_2_EDITABLE_FIELDS + "' property is invalid");
    	}
    	return getDocumentNumber() + "-" + editableFields;
    }
}
