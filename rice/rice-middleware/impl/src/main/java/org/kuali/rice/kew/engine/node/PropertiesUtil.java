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
package org.kuali.rice.kew.engine.node;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.kuali.rice.kew.engine.RouteContext;
import org.kuali.rice.kew.engine.node.var.Property;
import org.kuali.rice.kew.engine.node.var.PropertyScheme;


/**
 * A utility class for reading properties from a document.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public final class PropertiesUtil {
    private static final Logger LOG = Logger.getLogger(PropertiesUtil.class);

	private PropertiesUtil() {
		throw new UnsupportedOperationException("do not call");
	}

    public static String readResource(InputStream stream) throws IOException {
        StringBuffer sb = new StringBuffer(2048);
        InputStreamReader reader = new InputStreamReader(stream);
        char[] buf = new char[1024];
        int read;
        try {
            while ((read = reader.read(buf)) != -1) {
                sb.append(buf, 0, read);
            }
        } finally {
            reader.close();
        }
        return sb.toString();
    }

    /**
     * Resolves the specified name as a qualified property
     * @param name the qualified property name
     * @return value if found, null otherwise
     */
    public static Object retrieveProperty(String name, RouteContext context) {
        return retrieveProperty(new Property(name), context);
    }

    /**
     * Resolves the specified name as an unqualified property
     * @param name the potentially unqualified property name
     * @param defaultScheme the default scheme to use if the property is unqualified
     * @return value if found, null otherwise
     */
    public static Object retrieveProperty(String name, PropertyScheme defaultScheme, RouteContext context) {
        return retrieveProperty(new Property(name), defaultScheme, context);
    }

    /**
     * Resolves the specified name as an unqualified property
     * @param prop the potentially unqualified property
     * @param defaultScheme the default scheme to use if the property is unqualified
     * @return value if found, null otherwise
     */
    public static Object retrieveProperty(Property prop, PropertyScheme defaultScheme, RouteContext context) {
        if (prop.scheme == null && defaultScheme != null) {
            prop.scheme = defaultScheme.getName();
        }
        return retrieveProperty(prop, context);
    }

    /**
     * Resolves the specified name as a qualified property
     * @param prop the qualified property
     * @return value if found, null otherwise
     */
    public static Object retrieveProperty(Property prop, RouteContext context) {
        Iterator schemes = PropertyScheme.SCHEMES.iterator();
        while (schemes.hasNext()) {
            PropertyScheme scheme = (PropertyScheme) schemes.next();
            if (scheme.getName().equals(prop.scheme) ||
                scheme.getShortName().equals(prop.scheme)) {
                LOG.debug("Loading prop " + prop + " with scheme " + scheme);
                return scheme.load(prop, context);
            }
        }
        String message = "Invalid property scheme: '" + prop.scheme + "'"; 
        LOG.error(message);
        throw new RuntimeException(message);
    }
}
