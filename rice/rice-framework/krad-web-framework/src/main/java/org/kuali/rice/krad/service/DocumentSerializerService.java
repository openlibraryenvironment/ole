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
package org.kuali.rice.krad.service;

import org.kuali.rice.krad.document.Document;

/**
 * Implementations of this interface are able to serialize documents into XML that's used by the workflow engine to perform routing, searches,
 * etc.
 *
 */
public interface DocumentSerializerService extends SerializerService  {
    /**
     * Serializes a document into XML for the workflow engine
     *
     * @param document the document
     * @return a XML representation of the document
     */
    public String serializeDocumentToXmlForRouting(Document document);

}
