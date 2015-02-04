package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

/**
 * Created with IntelliJ IDEA.
 * User: aurojyotit
 * Date: 7/8/13
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchProcessFileTypeBo extends PersistableBusinessObjectBase {
    private String id;
    private String fileTypeDecsription;
    private String fileType;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    private String dataType;
    private boolean activeIndicator;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileTypeDecsription() {
        return fileTypeDecsription;
    }

    public void setFileTypeDecsription(String fileTypeDecsription) {
        this.fileTypeDecsription = fileTypeDecsription;
    }

    public boolean isActiveIndicator() {
        return activeIndicator;
    }

    public void setActiveIndicator(boolean activeIndicator) {
        this.activeIndicator = activeIndicator;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
