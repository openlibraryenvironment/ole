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
package org.kuali.rice.ksb.security.admin.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.ksb.messaging.web.KSBAction;
import org.kuali.rice.ksb.security.admin.ExportServlet;
import org.kuali.rice.ksb.security.admin.KeyStoreEntryDataContainer;
import org.kuali.rice.ksb.service.KSBServiceLocator;


/**
 * Struts action for admin users to manage keys and keystore files for client applications 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class JavaSecurityManagementAction extends KSBAction {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(JavaSecurityManagementAction.class);

    /**
     * @see org.kuali.rice.ksb.messaging.web.KSBAction#establishRequiredState(javax.servlet.http.HttpServletRequest, org.apache.struts.action.ActionForm)
     */
    @Override
    public ActionMessages establishRequiredState(HttpServletRequest request, ActionForm form) throws Exception {
        request.setAttribute("rice_constant", getServlet().getServletContext().getAttribute("RiceConstants"));
        request.setAttribute("entryListPageSize", 30);
        Collection<KeyStoreEntryDataContainer> keyStoreEntryList = KSBServiceLocator.getJavaSecurityManagementService().getListOfModuleKeyStoreEntries();
        LOG.info("Found " + keyStoreEntryList.size() + " entries in module keystore");
        request.setAttribute("keyStoreEntryList", keyStoreEntryList);
        return null;
    }

    /**
     * @see org.kuali.rice.ksb.messaging.web.KSBAction#start(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("report");
    }

    /**
     *  Method to sort the list of keystore entries
     */
    public ActionForward sort(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("report");
    }

    /**
     *  Clear the form
     */
    public ActionForward clear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        form = new JavaSecurityManagementForm();
        return mapping.findForward("restart");
    }

    /**
     *  Remove the entry associated with the given alias parameter
     */
    public ActionForward removeEntry(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String aliasToRemove = request.getParameter("aliasToRemove");
        LOG.info("Removing alias " + aliasToRemove + " from module keystore file");
        KSBServiceLocator.getJavaSecurityManagementService().removeClientCertificate(aliasToRemove);
        return mapping.findForward("restart");
    }

    public ActionForward generateClientKeyStore(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        JavaSecurityManagementForm managementForm = (JavaSecurityManagementForm)form;
        ActionMessages errors = managementForm.validateGenerateClientKeystore(mapping, request);
        if (errors == null || errors.isEmpty()) {
            KeyStore clientKeyStore = KSBServiceLocator.getJavaSecurityManagementService().generateClientKeystore(managementForm.getAlias(), managementForm.getPassword());
            byte[] data = {};
            ByteArrayOutputStream baos = null;
            try {
                baos = new ByteArrayOutputStream();
                clientKeyStore.store(baos, managementForm.getPassword().toCharArray());
                data = baos.toByteArray();
            } catch (KeyStoreException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (CertificateException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                try {
                    baos.close();
                } catch (IOException e) {}
            }

            form = new JavaSecurityManagementForm();
            request.getSession().setAttribute(ExportServlet.CLIENT_KEYSTORE_DATA, data);
            return new ActionForward(ExportServlet.generateExportPath(managementForm.getAlias() + "_keystore", request), true);
        } else {
            // found at least one error
            saveErrors(request, errors);
            return mapping.findForward("report");
        }
    }
}
