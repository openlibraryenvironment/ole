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
package org.kuali.rice.krad.datadictionary.impl;

import org.kuali.rice.krad.datadictionary.FieldOverride;

import java.util.List;

/**
 * A Field Override used to insert elements into a Data Dictionary Bean.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class FieldOverrideForListElementInsertImpl extends FieldOverrideForListElementBase implements FieldOverride {

    private Object insertBefore;
    private Object insertAfter;

    public Object getInsertBefore() {
        return insertBefore;
    }

    public void setInsertBefore(Object insertBefore) {
        this.insertBefore = insertBefore;
    }

    public Object getInsertAfter() {
        return insertAfter;
    }

    public void setInsertAfter(Object insertAfter) {
        this.insertAfter = insertAfter;
    }

    protected void varifyConfig() {
        if (insertBefore != null && insertAfter != null) {
            throw new RuntimeException("Configuration Error, insertBefore and insertAfter can not be both NOT-NULL");
        }
        if (insertBefore == null && insertAfter == null) {
            throw new RuntimeException("Configuration Error, Either insertBefore or insertAfter should be NOT-NULL");
        }
    }

    private Object getObjectToInsert() {
        Object objToInsert = null;
        if (insertBefore != null) {
            objToInsert = insertBefore;
        }
        if (insertAfter != null) {
            if (objToInsert != null) {
                throw new RuntimeException(
                        "Configuration Error, insertBefore and insertAfter can not be both NOT-NULL");
            }
            objToInsert = insertAfter;
        }
        if (objToInsert == null) {
            throw new RuntimeException("Configuration Error, Either insertBefore or insertAfter must be NOT-NULL");
        }
        return objToInsert;
    }

    public Object performFieldOverride(Object bean, Object property) {
        Object objToInsert = getObjectToInsert();

        List oldList = (List) property;

        int insertPos = getElementPositionInList(getElement(), oldList);

        if (insertPos == -1) {
            insertPos = oldList.size();
        } else {
            if (insertAfter != null) {
                insertPos = insertPos + 1;
            }
        }

        if (objToInsert instanceof List) {
            oldList.addAll(insertPos, (List) objToInsert);
        } else {
            oldList.add(insertPos, objToInsert);
        }
        return oldList;
    }
}
