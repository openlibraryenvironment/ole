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
package org.kuali.rice.krad.uif.util;

import static org.junit.Assert.assertEquals;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.Test;
import org.kuali.rice.krad.uif.util.ObjectPathExpressionParser.PathEntry;

public class ObjectPathExpressionParserTest extends ProcessLoggingUnitTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
        String afoo();
    }

    private static class DoIt implements PathEntry {

        @Override
        public String parse(Object node, String next, boolean inherit) {
            if (next == null) {
                return "";
            }
            String snode = (String) node;
            StringBuilder rv = new StringBuilder();
            if (snode != null && snode.length() > 0) {
                rv.append(snode);
            }
            if (inherit) {
                rv.append('<');
            } else if (rv.length() > 0) {
                rv.append('+');
            }
            rv.append(next);
            if (inherit) {
                rv.append('>');
            }

            return rv.toString();
        }

        @Override
        public Object prepare(Object prev) {
            return prev;
        }

        @Override
        public String dereference(Object prev) {
            return (String) prev;
        }

    }

    @Test
    public void testParsePathExpression() {
        assertEquals("foo+bar",
                ObjectPathExpressionParser.parsePathExpression(null, "foo.bar", new DoIt())
                        .toString());
        assertEquals("foo<bar>",
                ObjectPathExpressionParser.parsePathExpression(null, "foo[bar]", new DoIt())
                        .toString());
        assertEquals("foo<bar>+baz",
                ObjectPathExpressionParser
                        .parsePathExpression(null, "foo[bar].baz", new DoIt())
                        .toString());
        assertEquals(
                "foo<bar<baz>>",
                ObjectPathExpressionParser.parsePathExpression(null, "foo[bar[baz]]",
                        new DoIt()).toString());
        assertEquals(
                "foo+bar-bar.baz+fez",
                ObjectPathExpressionParser.parsePathExpression(null, "foo(bar-bar.baz)+fez",
                        new DoIt()).toString());
    }

}
