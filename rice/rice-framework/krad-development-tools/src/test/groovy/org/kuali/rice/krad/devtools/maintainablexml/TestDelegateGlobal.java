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
package org.kuali.rice.krad.devtools.maintainablexml;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.bo.GlobalBusinessObject;
import org.kuali.rice.krad.bo.GlobalBusinessObjectDetail;
import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class simply acts as a container to hold the List of Delegate Changes and the list of Account entries, for the Global
 * Delegate Change Document.
 */
public class TestDelegateGlobal extends PersistableBusinessObjectBase implements GlobalBusinessObject {

    protected String documentNumber;

    protected String modelName;
    protected String modelChartOfAccountsCode;
    protected String modelOrganizationCode;

    protected TestDelegateModel model;

    protected List<TestGlobalDetail> accountGlobalDetails;
    protected List<TestDelegateGlobalDetail> delegateGlobals;

    /**
     * Constructs a DelegateGlobal.java.
     */
    public TestDelegateGlobal() {
        super();
        accountGlobalDetails = new ArrayList<TestGlobalDetail>();
        delegateGlobals = new ArrayList<TestDelegateGlobalDetail>();
    }

    /**
     * This method adds a single TestGlobalDetail instance to the list. If one is already present in the list with the same
     * chartCode and accountNumber, then this new one will not be added.
     * 
     * @param accountGlobalDetail - populated TestGlobalDetail instance
     */
    public void addAccount(TestGlobalDetail accountGlobalDetail) {

        // validate the argument
        if (accountGlobalDetail == null) {
            throw new IllegalArgumentException("The accountGlobalDetail instanced passed in was null.");
        }
        else if (StringUtils.isBlank(accountGlobalDetail.getChartOfAccountsCode())) {
            throw new IllegalArgumentException("The chartOfAccountsCode member of the accountGlobalDetail object was not populated.");
        }
        else if (StringUtils.isBlank(accountGlobalDetail.getAccountNumber())) {
            throw new IllegalArgumentException("The accountNumber member of the accountGlobalDetail object was not populated.");
        }

        // add the object if one doesnt already exist, otherwise silently do nothing
        TestGlobalDetail testObject = getAccount(accountGlobalDetail.getChartOfAccountsCode(), accountGlobalDetail.getAccountNumber());
        if (testObject == null) {
            this.accountGlobalDetails.add(accountGlobalDetail);
        }
    }

    /**
     * This method retrieves the specific TestGlobalDetail object that corresponds to your requested chartCode and accountNumber
     * (or a null object if there is no match).
     * 
     * @param chartCode
     * @param accountNumber
     * @return returns the TestGlobalDetail instance matching the chartCode & accountNumber passed in, or Null if none match
     */
    public TestGlobalDetail getAccount(String chartCode, String accountNumber) {

        // validate the argument
        if (StringUtils.isBlank(chartCode)) {
            throw new IllegalArgumentException("The chartCode argument was null or empty.");
        }
        else if (StringUtils.isBlank(accountNumber)) {
            throw new IllegalArgumentException("The accountNumber argument was null or empty.");
        }

        // walk the list of TestGlobalDetail objects
        for (Iterator iter = this.accountGlobalDetails.iterator(); iter.hasNext();) {
            TestGlobalDetail accountGlobalDetail = (TestGlobalDetail) iter.next();

            // if this one is a match, then quit
            if (chartCode.equalsIgnoreCase(accountGlobalDetail.getChartOfAccountsCode()) && accountNumber.equalsIgnoreCase(accountGlobalDetail.getAccountNumber())) {
                return accountGlobalDetail;
            }
        }

        // we return null if one is not found
        return null;
    }

    public List<PersistableBusinessObject> generateDeactivationsToPersist() {

        return new ArrayList<PersistableBusinessObject>();
    }


    @SuppressWarnings("deprecation")
    public List<PersistableBusinessObject> generateGlobalChangesToPersist() {


        List<TestDelegateGlobalDetail> changeDocuments = this.getDelegateGlobals();
        List<TestGlobalDetail> accountDetails = this.getAccountGlobalDetails();

        return new ArrayList<PersistableBusinessObject>();
    }
    
    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {

        LinkedHashMap m = new LinkedHashMap();
        return m;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }


    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;

    }

    /**
     * Gets the accountGlobalDetails attribute.
     * 
     * @return Returns the accountGlobalDetails.
     */
    public final List<TestGlobalDetail> getAccountGlobalDetails() {
        return accountGlobalDetails;
    }

    /**
     * Sets the accountGlobalDetails attribute value.
     * 
     * @param accountGlobalDetails The accountGlobalDetails to set.
     */
    public final void setAccountGlobalDetails(List<TestGlobalDetail> accountGlobalDetails) {
        this.accountGlobalDetails = accountGlobalDetails;
    }

    /**
     * Gets the delegateGlobals attribute.
     * 
     * @return Returns the delegateGlobals.
     */
    public final List<TestDelegateGlobalDetail> getDelegateGlobals() {
        return delegateGlobals;
    }

    /**
     * Sets the delegateGlobals attribute value.
     * 
     * @param delegateGlobals The delegateGlobals to set.
     */
    public final void setDelegateGlobals(List<TestDelegateGlobalDetail> delegateGlobals) {
        this.delegateGlobals = delegateGlobals;
    }

    public boolean isPersistable() {
        // fail if the PK for this object is emtpy

        // otherwise, its all good
        return true;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String loadModelName) {
        this.modelName = loadModelName;
    }

    public String getModelChartOfAccountsCode() {
        return modelChartOfAccountsCode;
    }

    public void setModelChartOfAccountsCode(String loadModelChartOfAccountsCode) {
        this.modelChartOfAccountsCode = loadModelChartOfAccountsCode;
    }

    public String getModelOrganizationCode() {
        return modelOrganizationCode;
    }

    public void setModelOrganizationCode(String loadModelOrganizationCode) {
        this.modelOrganizationCode = loadModelOrganizationCode;
    }

    public TestDelegateModel getModel() {
        return model;
    }

    public void setModel(TestDelegateModel loadModel) {
        this.model = loadModel;
    }

    public List<? extends GlobalBusinessObjectDetail> getAllDetailObjects() {
        ArrayList<GlobalBusinessObjectDetail> details = new ArrayList<GlobalBusinessObjectDetail>(accountGlobalDetails.size() + delegateGlobals.size());
        details.addAll(accountGlobalDetails);
        details.addAll(delegateGlobals);
        return details;
    }

    @Override
    public void linkEditableUserFields() {
        super.linkEditableUserFields();
        if (this == null) {
            throw new IllegalArgumentException("globalDelegate parameter passed in was null");
        }
        List<PersistableBusinessObject> bos = new ArrayList<PersistableBusinessObject>();
        bos.addAll(getDelegateGlobals());
    }

    /**
     * @see org.kuali.rice.krad.bo.PersistableBusinessObjectBase#buildListOfDeletionAwareLists()
     */
    @Override
    public List<Collection<PersistableBusinessObject>> buildListOfDeletionAwareLists() {
        List<Collection<PersistableBusinessObject>> managedLists = super.buildListOfDeletionAwareLists();

        managedLists.add( new ArrayList<PersistableBusinessObject>( getAccountGlobalDetails() ) );
        managedLists.add( new ArrayList<PersistableBusinessObject>( getDelegateGlobals() ) );

        return managedLists;
    }
}
