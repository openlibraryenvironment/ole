/*
 * Copyright 2013 The Kuali Foundation.
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.businessobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.HashMap;
import java.util.List;

public class OlePatronDocumentList extends PersistableBusinessObjectBase {

    private HashMap<String, List<OLERequestorPatronDocument>> patronListMap;

    /**
     * Gets the patronListMap attribute.
     *
     * @return Returns the patronListMap.
     */
    public HashMap<String, List<OLERequestorPatronDocument>> getPatronListMap() {
        return patronListMap;
    }

    /**
     * Sets the patronListMap attribute value.
     *
     * @param patronListMap The patronListMap to set.
     */
    public void setPatronListMap(HashMap<String, List<OLERequestorPatronDocument>> patronListMap) {
        this.patronListMap = patronListMap;
    }


}
