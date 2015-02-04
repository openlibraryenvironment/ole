/*
 * Copyright 2011 The Kuali Foundation.
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
package org.kuali.ole.select.businessobject;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.util.LinkedHashMap;

public class PreOrderRequestBibEntry extends PersistableBusinessObjectBase {
    private String title;
    private Long publisherId;
    private Long authorId;
    private String publicationYear;
    private String series;
    private String edition;
    private String placeOfPublication;
    private String standardNumber;
    private String typeOfStandardNumber;


    @SuppressWarnings("rawtypes")

    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        return map;
    }
}
