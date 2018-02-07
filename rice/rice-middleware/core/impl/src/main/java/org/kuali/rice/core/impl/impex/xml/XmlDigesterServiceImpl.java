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
package org.kuali.rice.core.impl.impex.xml;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.impex.xml.XmlDoc;
import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.core.framework.impex.xml.XmlLoader;


/**
 * XmlDigesterService implementation.  This class simply loads the specified xml doc
 * with the specified XmlLoader.
 * @see org.kuali.rice.core.impl.impex.xml.batch.XmlDigesterService
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XmlDigesterServiceImpl implements XmlDigesterService {
    private static final Logger LOG = Logger.getLogger(XmlDigesterServiceImpl.class);

    private static void addProcessingException(XmlDoc xmlDoc, String message, Throwable t) {
        String msg = xmlDoc.getProcessingMessage();
        if (msg == null) {
            msg = "";
        }
        msg += message + "\n" + ExceptionUtils.getFullStackTrace(t);
        xmlDoc.setProcessingMessage(msg);
    }

    public void digest(XmlLoader xmlLoader, XmlDocCollection xmlDocCollection, String principalId) throws IOException {
        for (XmlDoc xmlDoc : xmlDocCollection.getXmlDocs())
        {
            InputStream inputStream = null;
            try
            {
                inputStream = new BufferedInputStream(xmlDoc.getStream());
                // NOTE: do we need XmlLoader to return a boolean to indicate
                // whether the document was handled at all, now that we are handing
                // all docs to all services?
                // e.g. if (xmlLoader.loadXml(inputstream)) xmlDoc.setProcessed(true);
                // it's ok if the xml doc is processed multiple times however
                // because it could have multiple types of content
                // but we need to know if it was not processed ANY times
                // (so just don't setProcessed to false)
                xmlLoader.loadXml(inputStream, principalId);
                // it would be nice to know if the xmlLoader actually handled the doc
                // so we could print out a log entry ONLY if the doc was handled, not
                // if it was just (successfully) ignored

                // should only be set on successful loading
                xmlDoc.setProcessed(true);
            } catch (Exception e)
            {
                xmlDoc.setProcessed(false);
                addProcessingException(xmlDoc, "Caught Exception loading xml data from " + xmlDoc + ".  Will move associated file to problem dir.", e);
                LOG.error("Caught Exception loading xml data from " + xmlDoc + ".  Will move associated file to problem dir.", e);
                if (e instanceof RuntimeException)
                {
                    throw (RuntimeException) e;
                } else if (e instanceof IOException)
                {
                    throw (IOException) e;
                }
            } finally
            {
                if (inputStream != null) try
                {
                    inputStream.close();
                } catch (IOException ioe)
                {
                    LOG.warn("Error closing stream for xml doc: " + xmlDoc, ioe);
                }
            }
        }
    }
}
