/*
 * Copyright 2007 The Kuali Foundation
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

package org.kuali.ole.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * Defines exception-handling for the XML parses
 */
public class XmlErrorHandler implements ErrorHandler {
    // TODO: need to use SLF4j here.
    private static Logger LOG = LoggerFactory.getLogger(XmlErrorHandler.class);
    /**
     *
     * Default constructor.
     */
    public XmlErrorHandler() {
    }

    /**
     *  This method displays the warning message for SAXParseException
     * @param e
     */
    public void warning(SAXParseException e) {
        String parseMessage = assembleMessage("warning", e);
        LOG.error(parseMessage);
        throw new ParseException(parseMessage, e);
    }

    /**
     *  This method displays the SAXParseException error.
     * @param e
     */
    public void error(SAXParseException e) {
        String parseMessage = assembleMessage("error", e);
        LOG.error(parseMessage);
        throw new ParseException(parseMessage, e);
    }

    /**
     *  This method displays the SAXParseException fatalError.
     * @param e
     */
    public void fatalError(SAXParseException e) {
        String parseMessage = assembleMessage("fatal error", e);
        LOG.error(parseMessage);
        throw new ParseException(parseMessage, e);
    }

    /**
     * This method assemble the error message and error line numbers
     * @param messageType
     * @param e
     * @return String
     */
    private String assembleMessage(String messageType, SAXParseException e) {
        StringBuffer message = new StringBuffer(messageType);
        message.append(" Parsing error was encountered on line ");
        message.append(e.getLineNumber());
        message.append(", column ");
        message.append(e.getColumnNumber());
        message.append(": ");
        message.append(e.getMessage());

        return message.toString();
    }
}
