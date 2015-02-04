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
package org.kuali.ole.docstore.common.document.content.bib.dc.unqualified;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ListRecords.
 *
 * @author Rajesh Chowdary K
 */
public class ListRecords {

    private List<Record> recordsList = new ArrayList<Record>();

    public void addRecord(Record record) {
        recordsList.add(record);
    }

    public List<Record> getRecords() {
        return recordsList;
    }

    public void setRecords(List<Record> records) {
        this.recordsList = records;
    }

    @Override
    public String toString() {

        return "List of Records: " + recordsList;
    }

}
