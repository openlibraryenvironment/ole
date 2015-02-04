/*
 * Copyright 2012 The Kuali Foundation.
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
package org.kuali.ole.select.lookup;

import org.kuali.ole.sys.OLEConstants;
import org.kuali.rice.kns.lookup.KualiLookupableHelperServiceImpl;
import org.kuali.rice.kns.web.struts.form.LookupForm;
import org.kuali.rice.kns.web.ui.Field;
import org.kuali.rice.kns.web.ui.Row;
import org.kuali.rice.krad.bo.BusinessObject;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OleLoadSumRecordLookupableHelperServiceImpl extends KualiLookupableHelperServiceImpl {

    @Override
    public List<? extends BusinessObject> getSearchResults(Map<String, String> fieldValues) {
        String fileName = fieldValues.get("fileName");
        if (!fileName.contains("*")) {
            StringBuilder loadFileName = new StringBuilder();
            loadFileName.append("*");
            loadFileName.append(fileName);
            loadFileName.append("*");
            fieldValues.put("fileName", loadFileName.toString());
        }
        return super.getSearchResults(fieldValues);
    }

    @Override
    public void performClear(LookupForm lookupForm){
        super.performClear(lookupForm);
        for (Iterator iter = this.getRows().iterator(); iter.hasNext();) {
            Row row = (Row) iter.next();
            for (Iterator iterator = row.getFields().iterator(); iterator.hasNext();) {
                Field field = (Field) iterator.next();
                if(field.getPropertyName().equalsIgnoreCase(OLEConstants.PRINCIPAL_ID)){
                    field.setPropertyValue(null);
                }
            }
        }
    }

}
