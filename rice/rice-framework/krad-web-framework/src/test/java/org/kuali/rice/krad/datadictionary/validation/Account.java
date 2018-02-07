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
package org.kuali.rice.krad.datadictionary.validation;

/**
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class Account {

	private String accountId;
	private String bankName;
	private String totalBalance;
	private String creditBalance;
	private String debitBalance;



	/**
	 * @param accountId
	 * @param bankName
	 * @param totalBalance
	 * @param creditBalance
	 * @param debitBalance
	 */
	public Account(String accountId, String bankName, String totalBalance, String creditBalance, String debitBalance) {
		this.accountId = accountId;
		this.bankName = bankName;
		this.totalBalance = totalBalance;
		this.creditBalance = creditBalance;
		this.debitBalance = debitBalance;
	}
	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return this.accountId;
	}
	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return this.bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * @return the totalBalance
	 */
	public String getTotalBalance() {
		return this.totalBalance;
	}
	/**
	 * @param totalBalance the totalBalance to set
	 */
	public void setTotalBalance(String totalBalance) {
		this.totalBalance = totalBalance;
	}
	/**
	 * @return the creditBalance
	 */
	public String getCreditBalance() {
		return this.creditBalance;
	}
	/**
	 * @param creditBalance the creditBalance to set
	 */
	public void setCreditBalance(String creditBalance) {
		this.creditBalance = creditBalance;
	}
	/**
	 * @return the debitBalance
	 */
	public String getDebitBalance() {
		return this.debitBalance;
	}
	/**
	 * @param debitBalance the debitBalance to set
	 */
	public void setDebitBalance(String debitBalance) {
		this.debitBalance = debitBalance;
	}

}
