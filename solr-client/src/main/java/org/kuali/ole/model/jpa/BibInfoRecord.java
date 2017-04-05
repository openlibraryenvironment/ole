package org.kuali.ole.model.jpa;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by sheiks on 27/10/16.
 */
@Entity
@Table(name="ole_ds_bib_info_t")
@NamedQuery(name="BibInfoRecord.findAll", query="SELECT b FROM BibInfoRecord b")
public class BibInfoRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="BIB_ID_STR")
    private String bibIdStr;

    private String author;

    @Column(name="BIB_ID")
    private int bibId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="DATE_UPDATED")
    private Date dateUpdated;

    private String isxn;

    private String publisher;

    private String title;

    public BibInfoRecord() {
    }

    public String getBibIdStr() {
        return this.bibIdStr;
    }

    public void setBibIdStr(String bibIdStr) {
        this.bibIdStr = bibIdStr;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getBibId() {
        return this.bibId;
    }

    public void setBibId(int bibId) {
        this.bibId = bibId;
    }

    public Date getDateUpdated() {
        return this.dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getIsxn() {
        return this.isxn;
    }

    public void setIsxn(String isxn) {
        this.isxn = isxn;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
