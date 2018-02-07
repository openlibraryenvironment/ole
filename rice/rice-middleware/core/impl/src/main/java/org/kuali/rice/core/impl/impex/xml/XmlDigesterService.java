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

import java.io.IOException;

import org.kuali.rice.core.api.impex.xml.XmlDocCollection;
import org.kuali.rice.core.framework.impex.xml.XmlLoader;

/**
 * A service which is responsible for digesting (by delegating to other target services)
 * an xml document loaded at runtime. It exists so that we can apply Spring's automagical
 * transactioning.  Ordering of invocations with respect to service dependencies is the
 * caller's responsibility.
 * Pipeline:<br/>
 * <ol>
 *   <li>Acquisition: <code>XmlPollerService</code>, <i>Struts upload action</i></li>
 *   <li>Ingestion: XmlIngesterService</li>
 *   <li>Digestion: XmlDigesterService</li>
 * </ol>
 * @see org.kuali.rice.core.api.impex.xml.batch.XmlIngesterService
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface XmlDigesterService {

    /**
     * Digests an XmlDoc.  Workflow User is passed to XmlLoader and the content is routing in a document
     * as that user if the loader supports it.
     *
     * @param xmlLoader
     * @param xmlDocCollection
     * @param principalId
     * @throws IOException
     */
    public void digest(XmlLoader xmlLoader, XmlDocCollection xmlDocCollection, String principalId) throws IOException;
}
