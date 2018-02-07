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

import java.io.ByteArrayInputStream;
import java.io.File;

import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;

/**
 * This class is responsible for resolving Xsl from files and Strings.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class XslSourceResolver {
    protected final Logger LOG = Logger.getLogger(getClass());

    /**
     * Constructs a XslSourceResolver.java.
     */
    public XslSourceResolver() {
	super();
	// TODO Auto-generated constructor stub
    }
    
    /**
     * 
     * This method resolves xsl from a file
     * @param path location of xsl file
     * @return a StreamSource
     */
    public StreamSource resolveXslFromFile(String path) {
	StreamSource xslsource = new StreamSource(new File(path));
	return xslsource;
    }
    
    /**
     * 
     * This method resolves xsl from a string
     * @param path location of xsl file
     * @return a StreamSource
     */
    public StreamSource resolveXslFromString(String s) {
	ByteArrayInputStream bytestream = new ByteArrayInputStream(s.getBytes());
	StreamSource xslsource = new StreamSource(bytestream);
	return xslsource;
    }
}
