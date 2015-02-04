package org.kuali.ole.deliver.defaultload;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.bo.OlePatronIngestSummaryRecord;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.service.OlePatronConverterService;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterType;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: peris
 * Date: 10/31/12
 * Time: 11:06 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoadDefaultPatronsBean {
    private String fileName;
    private OlePatronConverterService olePatronIngestService;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LoadDefaultPatronsBean.class);
    protected static final String LOAD_DEFAULT_PATRONS_IND = "LOAD_DEFAULT_PATRONS_IND";
    protected ParameterService parameterService;

    public List<OlePatronDocument> loadDefaultPatrons(boolean forceLoad) throws Exception {
        if (forceLoad || parameterService.getParameterValueAsBoolean(OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_PATRONS_IND, Boolean.TRUE)) {
            LOG.debug("Starting Load of Default Patrons");

            URL resource = getClass().getResource(fileName);
            File file = new File(resource.toURI());
            String fileContent = new FileUtil().readFile(file);
            List<OlePatronDocument> olePatronDocuments =
                    olePatronIngestService.persistPatronFromFileContent(fileContent,
                            true,
                            file.getName(),
                            new OlePatronIngestSummaryRecord(), null, "");
            LOG.debug("Completed Load of Default Patrons");
            Parameter existingParameter = parameterService.getParameter(OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_PATRONS_IND);
            if (existingParameter != null) {
                Parameter.Builder updatedParameter = Parameter.Builder.create(existingParameter);
                updatedParameter.setValue("N");
                parameterService.updateParameter(updatedParameter.build());
            } else {
                Parameter.Builder newParameter = Parameter.Builder.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_PATRONS_IND, ParameterType.Builder.create("CONFG"));
                newParameter.setDescription("Set to 'Y' to have the application ingest the default patrons upon next startup.");
                newParameter.setValue("N");
                parameterService.createParameter(newParameter.build());
            }
            return olePatronDocuments;
        }
        return Collections.emptyList();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public OlePatronConverterService getOlePatronIngestService() {
        return olePatronIngestService;
    }

    public void setOlePatronIngestService(OlePatronConverterService olePatronIngestService) {
        this.olePatronIngestService = olePatronIngestService;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }
}
