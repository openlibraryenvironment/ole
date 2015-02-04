/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.businessobject;

import org.kuali.rice.krad.keyvalues.KeyValuesBase;

import java.util.List;


public class RequestorTypeValueFinder extends KeyValuesBase {

    @SuppressWarnings("rawtypes")
    @Override
    public List getKeyValues() {

        //TODO will be finishing up this tomorrow - Friday March 3rd, 2011 - Peri
        
        
       /* List<OleRequestor> codes = (List<OleRequestor>) SpringContext.getBean(KeyValuesService.class).findAll(OleRequestor.class);
        // copy the list of codes before sorting, since we can't modify the results from this method
        if (codes == null) {
            codes = new ArrayList<OleRequestor>(0);
        }
        else {
            codes = new ArrayList<OleRequestor>(codes);
        }
        // sort using comparator.
        Collections.sort(codes, new AccountTypeCodeComparator());

        List<KeyValue> labels = new ArrayList<KeyValue>();
        labels.add(new ConcreteKeyValue("", ""));

        for (AccountType acctType : codes) {
            if (acctType.isActive()) {
                labels.add(new ConcreteKeyValue(acctType.getAccountTypeCode(), acctType.getAccountTypeCode() + " - " + acctType.getAccountTypeName()));
            }
        }

        return labels;*/

        return null;
    }


}
