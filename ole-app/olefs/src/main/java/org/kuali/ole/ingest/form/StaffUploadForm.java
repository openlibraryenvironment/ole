package org.kuali.ole.ingest.form;

import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.multipart.MultipartFile;

/**
 * StaffUploadForm is the Form class for Staff Upload Document
 */
public class StaffUploadForm extends UifFormBase {

    private MultipartFile marcFile;

    private MultipartFile ediFile;

    private String agenda;

    private String agendaDescription;

    private String message;

    private String loadReportURL;

    private String user;
    /**
     * Gets the marcFile attribute.
     * @return Returns the marcFile.
     */
    public MultipartFile getMarcFile() {
        return marcFile;
    }
    /**
     * Sets the marcFile attribute value.
     * @param marcFile The marcFile to set.
     */
    public void setMarcFile(MultipartFile marcFile) {
        this.marcFile = marcFile;
    }
    /**
     * Gets the ediFile attribute.
     * @return Returns the ediFile.
     */
    public MultipartFile getEdiFile() {
        return ediFile;
    }
    /**
     * Sets the ediFile attribute value.
     * @param ediFile The ediFile to set.
     */
    public void setEdiFile(MultipartFile ediFile) {
        this.ediFile = ediFile;
    }
    /**
     * Gets the agenda attribute.
     * @return Returns the agenda.
     */
    public String getAgenda() {
        return agenda;
    }
    /**
     * Sets the agenda attribute value.
     * @param agenda The agenda to set.
     */
    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }
    /**
     * Gets the agendaDescription attribute.
     * @return Returns the agendaDescription.
     */
    public String getAgendaDescription() {
        return agendaDescription;
    }
    /**
     * Sets the agendaDescription attribute value.
     * @param agendaDescription The agendaDescription to set.
     */
    public void setAgendaDescription(String agendaDescription) {
        this.agendaDescription = agendaDescription;
    }
    /**
     * Gets the message attribute.
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }
    /**
     * Sets the profileFile message value.
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * Gets the loadReportURL attribute.
     * @return Returns the loadReportURL.
     */
    public String getLoadReportURL() {
        return loadReportURL;
    }
    /**
     * Sets the loadReportURL attribute value.
     * @param loadReportURL The loadReportURL to set.
     */
    public void setLoadReportURL(String loadReportURL) {
        this.loadReportURL = loadReportURL;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
