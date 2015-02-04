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

import org.kuali.ole.module.purap.businessobject.PurApItem;
import org.kuali.rice.krad.bo.PersistableBusinessObject;

/**
 * This class... OleNotes Business Object
 */
public interface OleNotes extends PersistableBusinessObject {

    public Integer getItemNoteId();

    public void setItemNoteId(Integer ItemNoteId);

    public Integer getItemIdentifier();

    public void setItemIdentifier(Integer itemIdentifier);

    public String getNote();

    public void setNote(String note);

    public Integer getNoteTypeId();

    public void setNoteTypeId(Integer noteTypeId);

    public OleNoteType getNoteType();

    public void setNoteType(OleNoteType noteType);

    public <T extends PurApItem> T getPurapItem();

    public void setPurapItem(PurApItem item);

}
 
