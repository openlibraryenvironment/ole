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
package org.kuali.ole.select.businessobject;

import org.kuali.ole.pojo.OleEditorResponse;
import org.kuali.ole.select.bo.OLEEditorResponse;

import java.util.HashMap;

public class OleDocstoreResponse {

    private static OleDocstoreResponse oleDocstoreResponse;

    private HashMap<String, OleEditorResponse> docstoreResponse;

    private HashMap<String, OLEEditorResponse> editorResponse;

    public static synchronized OleDocstoreResponse getInstance() {
        if (oleDocstoreResponse == null) {
            oleDocstoreResponse = new OleDocstoreResponse();
        }
        return oleDocstoreResponse;
    }

    private OleDocstoreResponse() {
        super();
    }

    public HashMap<String, OleEditorResponse> getDocstoreResponse() {
        return docstoreResponse;
    }

    public void setDocstoreResponse(HashMap<String, OleEditorResponse> docstoreResponse) {
        this.docstoreResponse = docstoreResponse;
    }

    public HashMap<String, OLEEditorResponse> getEditorResponse() {
        return editorResponse;
    }

    public void setEditorResponse(HashMap<String, OLEEditorResponse> editorResponse) {
        this.editorResponse = editorResponse;
    }
}
