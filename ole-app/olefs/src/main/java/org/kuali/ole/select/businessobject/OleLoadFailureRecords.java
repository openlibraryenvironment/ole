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

import java.math.BigDecimal;
import java.util.LinkedHashMap;

public class OleLoadFailureRecords extends PersistableBusinessObjectBase {

    private BigDecimal acqFailId;
    private Integer acqLoadSumId;
    private BigDecimal errorId;
    private String vendorId;
    private String isbn;
    private String title;
    private OleLoadSumRecords loadSumRecord;
    private OleLoadError loadError;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public OleLoadError getLoadError() {
        return loadError;
    }

    public void setLoadError(OleLoadError loadError) {
        this.loadError = loadError;
    }

    public OleLoadSumRecords getLoadSumRecord() {
        return loadSumRecord;
    }

    public void setLoadSumRecord(OleLoadSumRecords loadSumRecord) {
        this.loadSumRecord = loadSumRecord;
    }

    public BigDecimal getAcqFailId() {
        return acqFailId;
    }

    public void setAcqFailId(BigDecimal acqFailId) {
        this.acqFailId = acqFailId;
    }

    public Integer getAcqLoadSumId() {
        return acqLoadSumId;
    }

    public void setAcqLoadSumId(Integer acqLoadSumId) {
        this.acqLoadSumId = acqLoadSumId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVendorId() {
        return vendorId;
    }

    public BigDecimal getErrorId() {
        return errorId;
    }

    public void setErrorId(BigDecimal errorId) {
        this.errorId = errorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }


    protected LinkedHashMap toStringMapper_RICE20_REFACTORME() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getContents() {
        StringBuffer content = new StringBuffer();
        content.append("ISBN :" + this.isbn);
        content.append("\n");
        content.append("TITLE :" + this.title);
        content.append("\n");
        content.append("VENDOR ORDER LINE NO:" + this.vendorId);
        content.append("\n");
        content.append("ERROR MESSAGE:" + this.getLoadError().getError());
        content.append("\n\n");

        return content.toString();
    }


}
