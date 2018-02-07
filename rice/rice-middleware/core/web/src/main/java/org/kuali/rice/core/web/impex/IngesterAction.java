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
package org.kuali.rice.core.web.impex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.impex.xml.CompositeXmlDocCollection;
import org.kuali.rice.core.api.impex.xml.FileXmlDocCollection;
import org.kuali.rice.core.api.impex.xml.XmlDoc;
import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.core.api.impex.xml.ZipXmlDocCollection;
import org.kuali.rice.core.api.util.RiceConstants;
import org.kuali.rice.kim.api.KimConstants;
import org.kuali.rice.kim.api.services.KimApiServiceLocator;
import org.kuali.rice.kns.web.struts.action.KualiAction;
import org.kuali.rice.krad.exception.AuthorizationException;
import org.kuali.rice.krad.util.GlobalVariables;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;

/**
 * Struts action that accepts uploaded files and feeds them to the XmlIngesterService
 * @see org.kuali.rice.kew.batch.XmlIngesterService
 * @see org.kuali.rice.core.web.impex.IngesterForm
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class IngesterAction extends KualiAction {
    private static final Logger LOG = Logger.getLogger(IngesterAction.class);

    @Override
	public ActionForward execute(
            ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {

    	checkAuthorization(form, "");
    	
    	if(isModuleLocked(form, "", request)) {
    	    return mapping.findForward(RiceConstants.MODULE_LOCKED_MAPPING);
    	}

        LOG.debug(request.getMethod());
        if (!"post".equals(request.getMethod().toLowerCase())) {
            LOG.debug("returning to view");
            return mapping.findForward("view");
        }

        IngesterForm iform = (IngesterForm) form;

        List<String> messages = new ArrayList<String>();
        List<File> tempFiles = new ArrayList<File>();
        try {
            Collection<FormFile> files = iform.getFiles();
            List<XmlDocCollection> collections = new ArrayList<XmlDocCollection>(files.size());
            LOG.debug(files);
            LOG.debug("" + files.size());

            for (FormFile file1 : files)
            {
                if (file1.getFileName() == null || file1.getFileName().length() == 0) {
					continue;
				}
                if (file1.getFileData() == null)
                {
                    messages.add("File '" + file1.getFileName() + "' contained no data");
                    continue;
                }
                LOG.debug("Processing file: " + file1.getFileName());
                // ok, we have to copy it to *another* file because Struts doesn't give us a File
                // reference (which itself is not a bad abstraction) and XmlDocs based on ZipFile
                // can't be constructed without a file reference.
                FileOutputStream fos = null;
                File temp = null;
                try
                {
                    temp = File.createTempFile("ingester", null);
                    tempFiles.add(temp);
                    fos = new FileOutputStream(temp);
                    fos.write(file1.getFileData());
                } catch (IOException ioe)
                {
                    messages.add("Error copying file data for '" + file1.getFileName() + "': " + ioe);
                    continue;
                } finally
                {
                    if (fos != null) {
						try
						{
						    fos.close();
						} catch (IOException ioe)
						{
						    LOG.error("Error closing temp file output stream: " + temp, ioe);
						}
					}
                }
                if (file1.getFileName().toLowerCase().endsWith(".zip"))
                {
                    try
                    {
                        collections.add(new ZipXmlDocCollection(temp));
                    } catch (IOException ioe)
                    {
                        String message = "Unable to load file: " + file1;
                        LOG.error(message);
                        messages.add(message);
                    }
                } else if (file1.getFileName().endsWith(".xml"))
                {
                    collections.add(new FileXmlDocCollection(temp, file1.getFileName()));
                } else
                {
                    messages.add("Ignoring extraneous file: " + file1.getFileName());
                }
            }

            if (collections.size() == 0) {
                String message = "No valid files to ingest";
                LOG.debug(message);
                messages.add(message);
            } else {
                // wrap in composite collection to make transactional
                CompositeXmlDocCollection compositeCollection = new CompositeXmlDocCollection(collections);
                int totalProcessed = 0;
                List<XmlDocCollection> c = new ArrayList<XmlDocCollection>(1);
                c.add(compositeCollection);
                try {
                    Collection<XmlDocCollection> failed = CoreApiServiceLocator.getXmlIngesterService().ingest(c, GlobalVariables.getUserSession().getPrincipalId());
                    boolean txFailed = failed.size() > 0;
                    if (txFailed) {
                        messages.add("Ingestion failed");
                    }
                    for (XmlDocCollection collection1 : collections)
                    {
                        List<? extends XmlDoc> docs = collection1.getXmlDocs();
                        for (XmlDoc doc1 : docs)
                        {
                            if (doc1.isProcessed())
                            {
                                if (!txFailed)
                                {
                                    totalProcessed++;
                                    messages.add("Ingested xml doc: " + doc1.getName() + (doc1.getProcessingMessage() == null ? "" : "\n" + doc1.getProcessingMessage()));
                                } else
                                {
                                    messages.add("Rolled back doc: " + doc1.getName() + (doc1.getProcessingMessage() == null ? "" : "\n" + doc1.getProcessingMessage()));
                                }
                            } else
                            {
                                messages.add("Failed to ingest xml doc: " + doc1.getName() + (doc1.getProcessingMessage() == null ? "" : "\n" + doc1.getProcessingMessage()));
                            }
                        }
                    }
                } catch (Exception e) {
                    String message = "Error during ingest";
                    LOG.error(message, e);
                    messages.add(message + ": " + e  + ":\n" + ExceptionUtils.getFullStackTrace(e));
                }
                if (totalProcessed == 0) {
                    String message = "No xml docs ingested";
                    LOG.debug(message);
                    messages.add(message);
                }
            }
        } finally {
            if (tempFiles.size() > 0) {
                for (File tempFile : tempFiles)
                {
                    if (!tempFile.delete())
                    {
                        LOG.warn("Error deleting temp file: " + tempFile);
                    }
                }
            }
        }

        request.setAttribute("messages", messages);
        return mapping.findForward("view");
    }

    @Override
	protected void checkAuthorization( ActionForm form, String methodToCall) throws AuthorizationException
    {
    	String principalId = GlobalVariables.getUserSession().getPrincipalId();
    	Map<String, String> roleQualifier = new HashMap<String, String>();
    	Map<String, String> permissionDetails = KRADUtils.getNamespaceAndActionClass(this.getClass());

        if (!KimApiServiceLocator.getPermissionService().isAuthorizedByTemplate(principalId,
                KRADConstants.KNS_NAMESPACE, KimConstants.PermissionTemplateNames.USE_SCREEN, permissionDetails,
                roleQualifier))
        {
            throw new AuthorizationException(GlobalVariables.getUserSession().getPrincipalName(),
            		methodToCall,
            		this.getClass().getSimpleName());
        }
    }


}
