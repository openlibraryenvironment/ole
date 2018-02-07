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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

/**
 * LSResourceResolver implementation that delegates in sequence to a list of LSResourceResolvers,
 * returning the first match. 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class CompoundLSResourceResolver implements LSResourceResolver {
    private final List<LSResourceResolver> resolvers;

    /**
     * Constructs a CompoundLSResourceResolver.java.
     * @param first
     * @param second
     */
    public CompoundLSResourceResolver(LSResourceResolver first, LSResourceResolver second) {
        this.resolvers = new ArrayList<LSResourceResolver>(2);
        this.resolvers.add(first);
        this.resolvers.add(second);
    }

    /**
     * Constructs a CompoundLSResourceResolver.java.
     * @param resolvers
     */
    public CompoundLSResourceResolver(List<LSResourceResolver> resolvers) {
        this.resolvers = resolvers;
    }

    /**
     * @see org.w3c.dom.ls.LSResourceResolver#resolveResource(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
	for (LSResourceResolver resolver: resolvers) {
	    LSInput input = resolver.resolveResource(type, namespaceURI, publicId, systemId, baseURI);
	    if (input != null) {
		return input;
	    }
	}
	return null;
    }
}
