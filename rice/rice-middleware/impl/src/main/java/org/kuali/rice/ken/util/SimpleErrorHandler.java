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
package org.kuali.rice.ken.util;

import org.apache.log4j.Logger;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A simple SAX ErrorHandler implementation that logs to a global logger for this
 * class, or the one provided.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SimpleErrorHandler implements ErrorHandler {
    private static final Logger LOG = Logger.getLogger(SimpleErrorHandler.class);

    private final Logger log;

    /**
     * Constructs a SimpleErrorHandler.java.
     */
    public SimpleErrorHandler() {
        this.log = LOG;
    }

    /**
     * Constructs a SimpleErrorHandler.java.
     * @param log
     */
    public SimpleErrorHandler(Logger log) {
        this.log = log;
    }

    /**
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
     */
    public void warning(SAXParseException se) {
        log.warn("Warning parsing xml doc " + se.getSystemId(), se);
    }

    /**
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
     */
    public void error(SAXParseException se) throws SAXException {
        log.error("Error parsing xml doc " + se.getSystemId(), se);
        throw se;
    }

    /**
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     */
    public void fatalError(SAXParseException se) throws SAXException {
        log.error("Fatal error parsing xml doc " + se.getSystemId(), se);
        throw se;
    }
}
