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
package org.kuali.rice.krad.uif.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * A custom FreeMarker directive that adds escapes to nested content to make it valid for enclosure within a JSON
 * string.
 *
 * <p>In other words, the content that is generated within this tag should be able to be enclosed in quotes within
 * a JSON document without breaking strict JSON parsers.  Note that this doesn't presently handle a wide variety of
 * cases, just enough to properly escape basic html.</p>
 *
 * <p>
 *     There are three types of replacements this performs:
 *     <ul>
 *         <li>the quote character '"' is prefixed with a backslash</li>
 *         <li>newline characters are replaced with backslash followed by 'n'</li>
 *         <li>carriage return characters are replaced with backslash followed by 'r'</li>
 *     </ul>
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class JsonStringEscapeDirective implements TemplateDirectiveModel {

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        // Check if no parameters were given:
        if (!params.isEmpty()) {
            throw new TemplateModelException(
                    getClass().getSimpleName() + " doesn't allow parameters.");
        }
        if (loopVars.length != 0) {
                throw new TemplateModelException(
                        getClass().getSimpleName() + " doesn't allow loop variables.");
        }

        // If there is non-empty nested content:
        if (body != null) {
            // Executes the nested body. Same as <#nested> in FTL, except
            // that we use our own writer instead of the current output writer.
            body.render(new JsonEscapingFilterWriter(env.getOut()));
        } else {
            throw new RuntimeException("missing body");
        }
    }

    /**
     * A {@link Writer} that does escaping of nested content to make it valid for enclosure within a JSON string.
     */
    private static class JsonEscapingFilterWriter extends Writer {

        private final Writer out;

        /**
         * Constructs a JsonEscapingFilterWriter which decorates the passed in Writer
         *
         * @param out the Writer to decorate
         */
        JsonEscapingFilterWriter(Writer out) {
            this.out = out;
        }

        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {

            // We need to allocate a buffer big enough to hold the escapes too, which take up extra chars
            int needsEscapingCount = 0; // count up how many chars needing escapes are in the buffer

            for (int i=0; i<len; i++) {
                if (isNeedsEscaping(cbuf[i + off])) { needsEscapingCount += 1; }
            }

            char[] transformedCbuf = new char[len + needsEscapingCount]; // allocate additional space for escapes
            int escapesAddedCount = 0; // the count of how many chars we've had to escape so far

            for (int i = 0; i < len; i++) {
                if (isNeedsEscaping(cbuf[i + off])) {
                    transformedCbuf[i+escapesAddedCount] = '\\';
                    escapesAddedCount += 1;
                }

                if (cbuf[i + off] == '\n') {
                    // newlines need to be replaced with literal "\n" <-- two chars
                    transformedCbuf[i+escapesAddedCount] = 'n';
                } else if (cbuf[i + off] == '\r') {
                    // carriage returns need to be replaced with literal "\r" <-- two chars
                    transformedCbuf[i+escapesAddedCount] = 'r';
                } else {
                    // standard escaping where we still use the original char
                    transformedCbuf[i+escapesAddedCount] = cbuf[i + off];
                }
            }

            out.write(transformedCbuf);
        }

        @Override
        public void flush() throws IOException {
            out.flush();
        }

        @Override
        public void close() throws IOException {
            out.close();
        }

        /**
         * Does the given character need escaping to be rendered as part of a JSON string?
         *
         * @param c the character to test
         * @return true if the character needs escaping for inclusion in a JSON string.
         */
        private static boolean isNeedsEscaping(char c) {
            return (c == '"' || c == '\n' || c == '\r');
        }
    }
}
