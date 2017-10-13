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
package org.kuali.rice.krad.service.impl;

import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.service.DocumentSerializerService;
import org.kuali.rice.krad.service.XmlObjectSerializerService;
import org.kuali.rice.krad.util.documentserializer.AlwaysTruePropertySerializibilityEvaluator;
import org.kuali.rice.krad.util.documentserializer.PropertySerializabilityEvaluator;
import org.kuali.rice.krad.util.documentserializer.SerializationState;

/**
 * Default implementation of the {@link DocumentSerializerService}.  If no &lt;workflowProperties&gt; have been defined in the
 * data dictionary for a document type (i.e. {@link Document#getDocumentPropertySerizabilityEvaluator()} returns an instance of 
 * {@link AlwaysTruePropertySerializibilityEvaluator}), then this service will revert to using the {@link XmlObjectSerializerService}
 * bean, which was the old way of serializing a document for routing.  If workflowProperties are defined, then this implementation
 * will selectively serialize items.
 */
public class DocumentSerializerServiceImpl extends SerializerServiceBase implements DocumentSerializerService {
    
    /**
     * Serializes a document for routing
     * 
     * @see org.kuali.rice.krad.service.DocumentSerializerService#serializeDocumentToXmlForRouting(org.kuali.rice.krad.document.Document)
     */
    public String serializeDocumentToXmlForRouting(Document document) {
        PropertySerializabilityEvaluator propertySerizabilityEvaluator = document.getDocumentPropertySerizabilityEvaluator();
        evaluators.set(propertySerizabilityEvaluator);
        SerializationState state = createNewDocumentSerializationState(document);
        serializationStates.set(state);
        
        Object xmlWrapper = wrapDocumentWithMetadata(document);
        String xml;
        if (propertySerizabilityEvaluator instanceof AlwaysTruePropertySerializibilityEvaluator) {
            xml = getXmlObjectSerializerService().toXml(xmlWrapper);
        }
        else {
            xml = xstream.toXML(xmlWrapper);
        }
        
        evaluators.set(null);
        serializationStates.set(null);
        return xml;
    }

    /**
     * Wraps the document before it is routed.  This implementation defers to {@link Document#wrapDocumentWithMetadataForXmlSerialization()}.
     * 
     * @param document
     * @return may return the document, or may return another object that wraps around the document to provide additional metadata
     */
    protected Object wrapDocumentWithMetadata(Document document) {
        return document.wrapDocumentWithMetadataForXmlSerialization();
    }
    
}
