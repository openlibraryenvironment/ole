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
package org.kuali.ole.select.document.web;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.ole.select.document.OleDefaultTableColumnValueDocument;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.core.api.util.RiceKeyConstants;
import org.kuali.rice.kns.util.KNSGlobalVariables;
import org.kuali.rice.kns.web.struts.action.KualiTransactionalDocumentActionBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OleDefaultTableColumnValueAction extends KualiTransactionalDocumentActionBase {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OleDefaultTableColumnValueAction.class);

    public OleDefaultTableColumnValueAction() {
        super();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.execute(mapping, form, request, response);

    }

    public ActionForward start(ActionMapping mapping, ActionForm form,
                               HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.docHandler(mapping, form, request, response);
    }

    @Override
    public ActionForward docHandler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward actionForward = super.docHandler(mapping, form, request, response);
        return actionForward;

    }

    public ActionForward valueSubmit(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response) throws Exception {

        OleDefaultTableColumnValueForm oleDefaultTableColumnValueForm = (OleDefaultTableColumnValueForm) form;
        OleDefaultTableColumnValueDocument oleDefaultTableColumnValueDocument = (OleDefaultTableColumnValueDocument) oleDefaultTableColumnValueForm.getDocument();
        oleDefaultTableColumnValueDocument.valueSubmit();
        KNSGlobalVariables.getMessageList().add(RiceKeyConstants.MESSAGE_ROUTE_SUCCESSFUL);
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

}
