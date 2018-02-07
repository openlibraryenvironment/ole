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
package org.kuali.rice.ken.util;

import java.util.concurrent.Callable;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * A Callable that performs work within a transaction and returns the
 * TransactionCallback's result
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class TransactionedCallable implements Callable, TransactionCallback {
    private TransactionTemplate txTemplate;
    private TransactionCallback callback;
    
    /**
     * Constructs a TransactionedCallable.java.
     * @param txTemplate
     */
    public TransactionedCallable(TransactionTemplate txTemplate) {
        this.txTemplate = txTemplate;
    }

    /**
     * Constructs a TransactionedCallable.java.
     * @param txTemplate
     * @param callback
     */
    public TransactionedCallable(TransactionTemplate txTemplate, TransactionCallback callback) {
        this.txTemplate = txTemplate;
        this.callback = callback;
    }

    /**
     * @see java.util.concurrent.Callable#call()
     */
    public Object call() throws Exception {
        TransactionCallback cb;
        if (callback != null) {
            cb = callback;
        } else {
            cb = this;
        }
        return txTemplate.execute(cb);
    }

    /**
     * @see org.springframework.transaction.support.TransactionCallback#doInTransaction(org.springframework.transaction.TransactionStatus)
     */
    public Object doInTransaction(TransactionStatus txStatus) {
        return null;
    }
}
