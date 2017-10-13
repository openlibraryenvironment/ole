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
package org.kuali.rice.kew.routeheader;

import java.io.Serializable;

import org.kuali.rice.kew.engine.RouteContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * A facade for the XML content of the document.  Provides methods for
 * accessing the various relevant portions of the content as well as the
 * entire document content in DOM or String form.  If the document does
 * not contain a particular piece, then the associated getter should return
 * null.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface DocumentContent extends Serializable {

    public Document getDocument();
    public Element getApplicationContent();
    public Element getAttributeContent();
    public Element getSearchableContent();
    public String getDocContent();
    
    /*
     * This method is a total hacks, once we fix the interface for role attributes and regular attributes, they can go away
     */
    public RouteContext getRouteContext();
    
}
