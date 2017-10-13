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
package org.kuali.rice.ksb.messaging.web;

import org.displaytag.decorator.TableDecorator;

/**
 * A {@link TableDecorator} for the Display Tag library which is used to add mouseover 
 * highlighting to rows in the results on the Document Search screen.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class KSBTableDecorator extends TableDecorator {
    
    public String startRow() {
        StringBuffer tbody = new StringBuffer();
        tbody.append("<tbody onmouseover=\"this.className = 'over';\" "); 
        tbody.append("onmouseout=\"this.className = this.className.replace('over', '');\">");
        return tbody.toString();
    }    
    
    public String finishRow() {
        return "</tbody>";
    }
    
}
