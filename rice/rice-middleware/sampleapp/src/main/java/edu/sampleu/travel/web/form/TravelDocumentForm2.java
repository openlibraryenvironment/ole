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
package edu.sampleu.travel.web.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.kuali.rice.kew.api.WorkflowDocument;
import org.kuali.rice.kns.web.struts.form.KualiTransactionalDocumentFormBase;
import org.kuali.rice.kns.web.ui.HeaderField;

import edu.sampleu.travel.bo.TravelAccount;
import edu.sampleu.travel.document.TravelDocument2;

public class TravelDocumentForm2 extends KualiTransactionalDocumentFormBase {

    @Override
	public void populateHeaderFields(WorkflowDocument workflowDocument) {
		getDocInfo().clear();
		getDocInfo().addAll(getStandardHeaderFields(workflowDocument));
		getDocInfo().add(new HeaderField("DataDictionary.AttributeReferenceDummy.attributes.initiatorNetworkId", "Yahoo!"));
		getDocInfo().add(new HeaderField("DataDictionary.AttributeReferenceDummy.attributes.initiatorNetworkId", "Yahoo!"));
	}

	private TravelAccount travelAccount = new TravelAccount();

    public TravelDocumentForm2() {
        super();
        this.setDocument(new TravelDocument2());
    }

    /*
     * Reset method - reset attributes of form retrieved from session otherwise
     * we will always call docHandler action
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.setMethodToCall(null);
        this.setRefreshCaller(null);
        this.setAnchor(null);
        this.setCurrentTabIndex(0);
    }

    public TravelAccount getTravelAccount() {
        return travelAccount;
    }

    public void setTravelAccount(TravelAccount travelAccount) {
        this.travelAccount = travelAccount;
    }

}
