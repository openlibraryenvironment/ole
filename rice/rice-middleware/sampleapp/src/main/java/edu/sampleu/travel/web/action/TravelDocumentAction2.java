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
package edu.sampleu.travel.web.action;

import edu.sampleu.travel.bo.TravelAccount;
import edu.sampleu.travel.document.TravelDocument2;
import edu.sampleu.travel.web.form.TravelDocumentForm2;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kns.web.struts.action.KualiTransactionalDocumentActionBase;
import org.kuali.rice.krad.exception.ValidationException;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

public class TravelDocumentAction2 extends KualiTransactionalDocumentActionBase {

    public ActionForward insertAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        TravelDocumentForm2 travelForm = (TravelDocumentForm2) form;
        TravelAccount travAcct = (TravelAccount) KRADServiceLocator.getBusinessObjectService().retrieve(travelForm.getTravelAccount());
        // Make sure a travel account was actually retrieved.
        if (travAcct == null) {
        	GlobalVariables.getMessageMap().putError("travelAccount.number", RiceKeyConstants.ERROR_CUSTOM, "Invalid travel account number");
        	throw new ValidationException("Invalid travel account number");
        }
        // Insert the travel account into the list, if the list does not already contain it.
        boolean containsNewAcct = false;
        for (Iterator<TravelAccount> travAcctIter = ((TravelDocument2) travelForm.getDocument()).getTravelAccounts().iterator(); travAcctIter.hasNext();) {
        	if (travAcctIter.next().getNumber().equals(travAcct.getNumber())) {
        		containsNewAcct = true;
        		break;
        	}
        }
        if (!containsNewAcct) {
        	((TravelDocument2) travelForm.getDocument()).getTravelAccounts().add(travAcct);
        }
        travelForm.setTravelAccount(new TravelAccount());
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward deleteAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	// Remove the travel account at the index specified in the "methodToCall" parameter.
    	TravelDocumentForm2 travelForm = (TravelDocumentForm2) form;
    	String strIndex = StringUtils.substringBetween((String) request.getAttribute(KRADConstants.METHOD_TO_CALL_ATTRIBUTE),
        		KRADConstants.METHOD_TO_CALL_PARM1_LEFT_DEL, KRADConstants.METHOD_TO_CALL_PARM1_RIGHT_DEL);
    	if (StringUtils.isNotBlank(strIndex)) {
    		((TravelDocument2) travelForm.getDocument()).getTravelAccounts().remove(Integer.parseInt(strIndex));
    	}
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    public ActionForward refresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
}
