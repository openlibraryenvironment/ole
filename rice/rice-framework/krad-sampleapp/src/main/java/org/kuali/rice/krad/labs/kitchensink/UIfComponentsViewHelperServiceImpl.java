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
package org.kuali.rice.krad.labs.kitchensink;

import org.kuali.rice.core.api.search.SearchOperator;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.core.api.uif.RemotableSelect;
import org.kuali.rice.core.api.uif.RemotableTextInput;
import org.kuali.rice.krad.demo.travel.account.TravelAccount;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.service.impl.ViewHelperServiceImpl;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Custom view helper service for demonstration (supports the kitchen sink)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UIfComponentsViewHelperServiceImpl extends ViewHelperServiceImpl {

    public List<RemotableAttributeField> retrieveRemoteFields(View view, Object model, Container container) {
        List<RemotableAttributeField> remoteFields = new ArrayList<RemotableAttributeField>();

        // note this would generally invoke a remote service to get the list of fields
        RemotableAttributeField.Builder builder = RemotableAttributeField.Builder.create("remoteField1");
        builder.setLongLabel("Remote Field 1");

        RemotableTextInput.Builder controlBuilder = RemotableTextInput.Builder.create();
        controlBuilder.setSize(30);
        builder.setControl(controlBuilder);
        remoteFields.add(builder.build());

        builder.setName("remoteField2");
        builder.setLongLabel("Remote Field 2");
        remoteFields.add(builder.build());

        controlBuilder.setSize(5);
        builder.setName("remoteField3");
        builder.setLongLabel("Remote Field 3");
        remoteFields.add(builder.build());

        Map<String, String> options = new HashMap<String, String>();
        options.put("Fruit", "Fruit");
        options.put("Vegetables", "Vegetables");

        RemotableSelect.Builder selectBuilder = RemotableSelect.Builder.create(options);
        builder.setName("remoteField4");
        builder.setLongLabel("Remote Field 4");
        builder.setControl(selectBuilder);
        remoteFields.add(builder.build());

        return remoteFields;
    }

    public List<TravelAccount> retrieveTravelAccounts(String term) {
        List<TravelAccount> matchingAccounts = new ArrayList<TravelAccount>();

        Map<String, String> lookupCriteria = new HashMap<String, String>();
        lookupCriteria.put("number", term + SearchOperator.LIKE_MANY.op());

        matchingAccounts = (List<TravelAccount>) KRADServiceLocatorWeb.getLookupService().findCollectionBySearch(
                TravelAccount.class, lookupCriteria);

        return matchingAccounts;
    }

    public List<TravelAccount> retrieveTravelAccountsBySubAcctAndTerm(String subAccount, String term) {
        List<TravelAccount> matchingAccounts = new ArrayList<TravelAccount>();

        Map<String, String> lookupCriteria = new HashMap<String, String>();
        lookupCriteria.put("subAccount", subAccount);
        lookupCriteria.put("number", term + SearchOperator.LIKE_MANY.op());

        matchingAccounts = (List<TravelAccount>) KRADServiceLocatorWeb.getLookupService().findCollectionBySearch(
                TravelAccount.class, lookupCriteria);

        return matchingAccounts;
    }



}
