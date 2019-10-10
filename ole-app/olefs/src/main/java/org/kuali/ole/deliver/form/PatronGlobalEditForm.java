package org.kuali.ole.deliver.form;

import org.kuali.ole.deliver.bo.*;
import org.kuali.ole.docstore.common.document.content.instance.Item;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * The OleLoanForm is the form class that defines all the loan fields required for a loan processing using getters and setters
 * and is involved in passing the data to the UI layer
 */
public class PatronGlobalEditForm extends UifFormBase {

    private String borrowerType;
    private Date expirationDate;
    private Date activationDate;
    private String statisticalCategory;
    private String source;
    private boolean activeIndicator;
    private String patronNoteTypeId;
    private String patronNoteText;

    public String getBorrowerType() {
        return borrowerType;
    }

    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public String getStatisticalCategory() {
        return statisticalCategory;
    }

    public void setStatisticalCategory(String statisticalCategory) {
        this.statisticalCategory = statisticalCategory;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isActiveIndicator() {
        return activeIndicator;
    }

    public void setActiveIndicator(boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }

    public String getPatronNoteTypeId() {
        return patronNoteTypeId;
    }

    public void setPatronNoteTypeId(String patronNoteTypeId) {
        this.patronNoteTypeId = patronNoteTypeId;
    }

    public String getPatronNoteText() {
        return patronNoteText;
    }

    public void setPatronNoteText(String patronNoteText) {
        this.patronNoteText = patronNoteText;
    }

}

