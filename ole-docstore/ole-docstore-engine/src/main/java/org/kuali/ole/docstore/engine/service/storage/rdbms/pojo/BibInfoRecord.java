package org.kuali.ole.docstore.engine.service.storage.rdbms.pojo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by sambasivam on 17/10/14.
 */

public class BibInfoRecord  extends PersistableBusinessObjectBase
        implements Serializable {

    private int bibId;
    private String bibIdStr;
    private String title;
    private String author;
    private String publisher;
    private String isxn;
    private Timestamp updatedDate;

    public int getBibId() {
        return bibId;
    }

    public void setBibId(int bibId) {
        this.bibId = bibId;
    }

    public String getBibIdStr() {
        return bibIdStr;
    }

    public void setBibIdStr(String bibIdStr) {
        this.bibIdStr = bibIdStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIsxn() {
        return isxn;
    }

    public void setIsxn(String isxn) {
        this.isxn = isxn;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }
}
