/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.service;

import org.kuali.ole.coa.businessobject.Account;

import java.util.List;
import java.util.Map;

public interface OleAccountService {

    /**
     * This method returns list of temporary Restricted accounts
     *
     * @return List<Account>
     */
    public List<Account> getTemporaryRestrictedAccounts();

    /**
     * This method takes account number as parameter and return account details of that account number
     *
     * @param accountNumber
     * @return
     */
    public Account getAccountDetailsWithAccountNumber(String accountNumber);

    /**
     * This method returns the list of accounts exists in database
     *
     * @return
     */
    public List<Account> getAllAccountDetails();

    /**
     * This method takes map as a parameter consists account details and returns matching accounts
     *
     * @param parameters
     * @return
     */
    public List<Account> getMatchingAccountDetails(Map<String, String> parameters);


    public boolean shouldAccountRouteForApproval(String documentId);
}
