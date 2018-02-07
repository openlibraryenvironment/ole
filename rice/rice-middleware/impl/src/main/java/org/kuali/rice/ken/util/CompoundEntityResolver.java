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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * EntityResolver implementation that delegates in sequence to a list of EntityResolvers,
 * returning the first match.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CompoundEntityResolver implements EntityResolver {
    private static final Logger LOG = Logger.getLogger(CompoundEntityResolver.class);

    private final List<EntityResolver> resolvers;

    /**
     * Constructs a CompoundEntityResolver.java.
     * @param first
     * @param second
     */
    public CompoundEntityResolver(EntityResolver first, EntityResolver second) {
        this.resolvers = new ArrayList<EntityResolver>(2);
        this.resolvers.add(first);
        this.resolvers.add(second);
    }

    /**
     * Constructs a CompoundEntityResolver.java.
     * @param resolvers
     */
    public CompoundEntityResolver(List<EntityResolver> resolvers) {
        this.resolvers = resolvers;
    }

    /**
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        LOG.debug("resolveEntity: " + publicId + " " + systemId);
        for (EntityResolver resolver: resolvers) {
            LOG.debug("Invoking entity resolver: " + resolver);
            InputSource source = resolver.resolveEntity(publicId, systemId);
            if (source != null) {
                LOG.debug("source != null: " + source);
                return source;
            }
        }
        return null;
    }
}
