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
package org.kuali.rice.krad.theme.util;

import org.apache.log4j.Logger;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

/**
 * Reports any error occurring during JavaScript files compression
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JavaScriptErrorReporter implements ErrorReporter {
    private static final Logger LOG = Logger.getLogger(JavaScriptErrorReporter.class);

    private String filename;

    /**
     * Error reporter constructor
     *
     * @param filename JavaScript source filename
     */
    public JavaScriptErrorReporter(String filename) {
        this.filename = filename;
    }

    /**
     * Reports a warning
     *
     * @param message a String describing the warning
     * @param sourceName a String describing the JavaScript source where the warning occured; typically a filename or
     * URL
     * @param line the line number associated with the warning
     * @param lineSource the text of the line (may be null)
     * @param lineOffset the offset into lineSource where problem was detected
     */
    @Override
    public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
        if (line < 0) {
            LOG.warn(message);
        } else {
            LOG.warn("[" + filename + ":" + line + "] " + message);
        }
    }

    /**
     * Reports an error. If execution has not yet begun, the JavaScript engine is free to find additional errors rather
     * than terminating the translation. However, it will not execute a script that had errors.
     *
     * @param message a String describing the warning
     * @param sourceName a String describing the JavaScript source where the warning occured; typically a filename or
     * URL
     * @param line the line number associated with the warning
     * @param lineSource the text of the line (may be null)
     * @param lineOffset the offset into lineSource where problem was detected
     */
    @Override
    public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
        if (line < 0) {
            LOG.error(message);
        } else {
            LOG.error("[" + filename + ":" + line + "] " + message);
        }
    }

    /**
     * Creates an EvaluatorException that may be thrown. runtimeErrors, unlike errors, will always terminate the
     * current script
     *
     * @param message a String describing the warning
     * @param sourceName a String describing the JavaScript source where the warning occured; typically a filename or
     * URL
     * @param line the line number associated with the warning
     * @param lineSource the text of the line (may be null)
     * @param lineOffset the offset into lineSource where problem was detected
     */
    @Override
    public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource,
            int lineOffset) {
        error(message, sourceName, line, lineSource, lineOffset);

        return new EvaluatorException(message);
    }
}
