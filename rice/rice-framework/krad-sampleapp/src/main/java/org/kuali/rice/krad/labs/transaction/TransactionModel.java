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

import org.apache.commons.lang.RandomStringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Test transaction model.  For testing only; does not represent any real object.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TransactionModel implements Serializable {
    private static final long serialVersionUID = -437696877653987556L;
    private String id;
    private Integer chargeAmount;
    private String transactionDisplayType;
    private List<TransactionModel> subTransactions = new ArrayList<TransactionModel>();

    public TransactionModel(){
        this.id = RandomStringUtils.randomAlphanumeric(4);
        this.chargeAmount = new Integer(RandomStringUtils.randomNumeric(2));
        this.transactionDisplayType = "type" + RandomStringUtils.randomNumeric(2);
    }

    public TransactionModel(int subTransactionsItemNumber){
        this();

        for(int i = 0; i < subTransactionsItemNumber; i++){
            this.subTransactions.add(new TransactionModel(0));
        }
    }

    public Integer getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(Integer chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TransactionModel> getSubTransactions() {
        return subTransactions;
    }

    public void setSubTransactions(List<TransactionModel> subTransactions) {
        this.subTransactions = subTransactions;
    }

    public String getTransactionDisplayType() {
        return transactionDisplayType;
    }

    public void setTransactionDisplayType(String transactionDisplayType) {
        this.transactionDisplayType = transactionDisplayType;
    }
}
