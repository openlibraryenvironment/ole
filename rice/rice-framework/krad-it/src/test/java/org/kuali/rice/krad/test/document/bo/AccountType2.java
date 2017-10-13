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
package org.kuali.rice.krad.test.document.bo;

/**
 * This is a copy of AccountType for usage in the BaseBOClassAndBaseDocumentClassTest unit test.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AccountType2 extends AccountType2Parent {
    
    private static final long serialVersionUID = -7442266938947384160L;
	private String accountTypeCode;
    private String name;
    

    public String getAccountTypeCode() {
		return accountTypeCode;
	}


	public void setAccountTypeCode(String accountTypeCode) {
		this.accountTypeCode = accountTypeCode;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

	public String getCodeAndDescription() {
		return accountTypeCode + " - " + name;
	}
}
