package org.kuali.ole.ingest.pojo;

/**
 * IngestRecord is a business object class for Ingest Record
 */
public class IngestRecord {
    private String originalMarcFileName;
    private String marcFileContent;
    private String originalEdiFileName;
    private String ediFileContent;
    private String agendaName;
    private Boolean byPassPreProcessing;
    private String agendaDescription;
    private boolean isUpdate =  false;
    private String user;


    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    /**
     * Gets the agendaDescription attribute.
     * @return Returns the agendaDescription value.
     */
    public String getAgendaDescription() {
        return agendaDescription;
    }

    /**
     * Sets the agendaDescription attribute value.
     * @param agendaDescription  The agendaDescription to set.
     */
    public void setAgendaDescription(String agendaDescription) {
        this.agendaDescription = agendaDescription;
    }

    /**
     * Gets the originalMarcFileName.
     * @return  Returns the originalMarcFileName.
     */
    public String getOriginalMarcFileName() {
        return originalMarcFileName;
    }

    /**
     *   Sets the originalMarcFileName attribute value.
     * @param originalMarcFileName . The originalMarcFileName to set.
     */
    public void setOriginalMarcFileName(String originalMarcFileName) {
        this.originalMarcFileName = originalMarcFileName;
    }
    /**
     * Gets the marcFileContent attribute
     * @return  Returns the marcFileContent.
     */
    public String getMarcFileContent() {
        return marcFileContent;
    }
    /**
     * Sets the marcFileContent attribute value.
     * @param marcFileContent  The marcFileContent to set.
     */
    public void setMarcFileContent(String marcFileContent) {
        this.marcFileContent = marcFileContent;
    }
    /**
     * Gets the originalEdiFileName the attribute.
     * @return  Returns the originalEdiFileName.
     */
    public String getOriginalEdiFileName() {
        return originalEdiFileName;
    }
    /**
     * Sets the originalEdiFileName attribute value.
     * @param originalEdiFileName  The originalEdiFileName to set.
     */
    public void setOriginalEdiFileName(String originalEdiFileName) {
        this.originalEdiFileName = originalEdiFileName;
    }
    /**
     * Gets the ediFileContent attribute.
     * @return  Returns the ediFileContent.
     */
    public String getEdiFileContent() {
        return ediFileContent;
    }
    /**
     * Sets the ediFileContent attribute value.
     * @param ediFileContent  The ediFileContent to set.
     */
    public void setEdiFileContent(String ediFileContent) {
        this.ediFileContent = ediFileContent;
    }
    /**
     * Gets the agendaName attribute.
     * @return  Returns the agendaName.
     */
    public String getAgendaName() {
        return agendaName;
    }
    /**
     * Sets the agendaName attribute value.
     * @param agendaName  The agendaName to set.
     */
    public void setAgendaName(String agendaName) {
        this.agendaName = agendaName;
    }
    /**
     * Gets the byPassPreProcessing attribute.
     * @return  Returns the byPassPreProcessing.
     */
    public Boolean getByPassPreProcessing() {
        return byPassPreProcessing;
    }
    /**
     * Sets the byPassPreProcessing attribute value.
     * @param byPassPreProcessing  The byPassPreProcessing to set.
     */
    public void setByPassPreProcessing(Boolean byPassPreProcessing) {
        this.byPassPreProcessing = byPassPreProcessing;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
