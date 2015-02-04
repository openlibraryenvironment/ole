package org.kuali.ole.select.document;



import org.kuali.rice.krad.bo.PersistableBusinessObject;
import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;
import org.kuali.rice.krad.document.TransactionalDocumentBase;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: arunag
 * Date: 2/25/14
 * Time: 6:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEInvoiceIngestLoadReport extends PersistableBusinessObjectBase{

    private Integer id;
    private String vendor;
    private String fileName;
    private Date dateUploaded;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }
}
