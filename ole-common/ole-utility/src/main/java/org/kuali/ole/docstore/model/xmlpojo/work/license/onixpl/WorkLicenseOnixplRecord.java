package org.kuali.ole.docstore.model.xmlpojo.work.license.onixpl;

/**
 * Created by IntelliJ IDEA.
 * User: Sreekanth
 * Date: 5/30/12
 * Time: 3:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLicenseOnixplRecord {

    /**
     * Method to get WorkLicenseOnixplRecord from given XML string
     *
     * @param xml
     * @return
     */

    // TODO: OnxiPl Impl task
    private String title;
    private String contractNumber;
    private String licensee;
    private String licensor;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLicensor() {
        return licensor;
    }

    public void setLicensor(String licensor) {
        this.licensor = licensor;
    }

    public String getLicensee() {
        return licensee;
    }

    public void setLicensee(String licensee) {
        this.licensee = licensee;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
}
