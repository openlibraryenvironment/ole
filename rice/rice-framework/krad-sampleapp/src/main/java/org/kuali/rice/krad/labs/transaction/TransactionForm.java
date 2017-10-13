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
package org.kuali.rice.krad.labs.transaction;

import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.List;

/**
 * KSA Transaction test form for lab
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TransactionForm extends UifFormBase {
    private static final long serialVersionUID = 7158452291069882385L;
    private List<TransactionModel> rollupTransactions = new ArrayList<TransactionModel>();
    private String testField;

    public TransactionForm(){
        for (int i = 0; i < 50; i++){
            rollupTransactions.add(new TransactionModel(5));
        }
    }

    public List<TransactionModel> getRollupTransactions() {
        return rollupTransactions;
    }

    public void setRollupTransactions(List<TransactionModel> rollupTransactions) {
        this.rollupTransactions = rollupTransactions;
    }

    public String getTestField() {
        return testField;
    }

    public void setTestField(String testField) {
        this.testField = testField;
    }
}
