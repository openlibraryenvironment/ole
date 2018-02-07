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
package org.kuali.rice.krad.rules.rule.event;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.krad.document.Document;
import org.kuali.rice.krad.util.KRADPropertyConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract superclass for document-related events.
 */
abstract public class KualiDocumentEventBase implements KualiDocumentEvent {
    private static final Logger LOG = Logger.getLogger(KualiDocumentEventBase.class);

    private final String description;
    private final String errorPathPrefix;
    protected Document document;

    /**
     *
     * As a general rule, business rule classes should not change the original object. This constructor was created so that
     * PreRulesCheckEvent, a UI level rule checker, can make changes.
     *
     * @param description
     * @param errorPathPrefix
     */
    protected KualiDocumentEventBase(String description, String errorPathPrefix) {
        this.description = description;
        this.errorPathPrefix = errorPathPrefix;
    }

    /**
     * Constructs a KualiEvent with the given description and errorPathPrefix for the given document.
     *
     * @param errorPathPrefix
     * @param document
     * @param description
     */
    public KualiDocumentEventBase(String description, String errorPathPrefix, Document document) {
        this.description = description;
        this.errorPathPrefix = errorPathPrefix;
        this.document = document;

        LOG.debug(description);
    }


    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#getDocument()
     */
    public final Document getDocument() {
        return document;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#getName()
     */
    public final String getName() {
        return this.getClass().getName();
    }

    /**
     * @return a description of this event
     */
    public final String getDescription() {
        return description;
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#getErrorPathPrefix()
     */
    public String getErrorPathPrefix() {
        return errorPathPrefix;
    }


    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getName();
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#validate()
     */
    public void validate() {
        if (getDocument() == null) {
            throw new IllegalArgumentException("invalid (null) event document");
        }
    }

    /**
     * @see org.kuali.rice.krad.rules.rule.event.KualiDocumentEvent#generateEvents()
     */
    public List<KualiDocumentEvent> generateEvents() {
        return new ArrayList<KualiDocumentEvent>();
    }

    /**
     * Provides null-safe access to the documentNumber of the given document.
     *
     * @param document
     * @return String containing the documentNumber of the given document, or some indication of why the documentNumber isn't
     *         accessible
     */
    protected static String getDocumentId(Document document) {
        String docId = "(null document)";

        if (document != null) {
            String documentNumber = document.getDocumentNumber();
            if (StringUtils.isBlank(documentNumber)) {
                docId = "(blank " + KRADPropertyConstants.DOCUMENT_NUMBER + ")";
            }
            else {
                docId = documentNumber;
            }
        }

        return docId;
    }
}
