package org.kuali.ole.describe.defaultload;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.service.OleLocationConverterService;
import org.kuali.rice.coreservice.api.parameter.Parameter;
import org.kuali.rice.coreservice.api.parameter.ParameterType;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: peris
 * Date: 10/31/12
 * Time: 8:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class LoadDefaultLocationsBean {
    private String fileName;
    private List<String> locationFiles = new ArrayList<String>();
    private OleLocationConverterService oleLocationIngestService;
    protected ParameterService parameterService;

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LoadDefaultLocationsBean.class);
    protected static final String LOAD_DEFAULT_LOCATIONS_IND = "LOAD_DEFAULT_LOCATIONS_IND";

    public void loadDefaultLocations(boolean forceLoad) throws Exception {
        if (forceLoad || parameterService.getParameterValueAsBoolean(OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_LOCATIONS_IND, Boolean.TRUE)) {
            LOG.info("Starting Load of Default Locations");

            for (int i = 0; i < locationFiles.size(); i++) {
                fileName = this.locationFiles.get(i);
                URL resource = getClass().getResource(fileName);
                File file = new File(resource.toURI());
                String fileContent = new FileUtil().readFile(file);
                oleLocationIngestService.persistLocationFromFileContent(fileContent, file.getName());
                LOG.info("Completed Load of Default Locations");

                Parameter existingParameter = parameterService.getParameter(OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_LOCATIONS_IND);
                if (existingParameter != null) {
                    Parameter.Builder updatedParameter = Parameter.Builder.create(existingParameter);
                    updatedParameter.setValue("N");
                    parameterService.updateParameter(updatedParameter.build());
                } else {
                    Parameter.Builder newParameter = Parameter.Builder.create(OLEConstants.APPL_ID, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, LOAD_DEFAULT_LOCATIONS_IND, ParameterType.Builder.create("CONFG"));
                    newParameter.setDescription("Set to 'Y' to have the application ingest the default circulation policies upon next startup.");
                    newParameter.setValue("N");
                    parameterService.createParameter(newParameter.build());
                }
            }
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public OleLocationConverterService getOleLocationIngestService() {
        return oleLocationIngestService;
    }

    public void setOleLocationIngestService(OleLocationConverterService oleLocationIngestService) {
        this.oleLocationIngestService = oleLocationIngestService;
    }

    public List<String> getLocationFiles() {
        return locationFiles;
    }

    public void setLocationFiles(List<String> locationFiles) {
        this.locationFiles = locationFiles;
    }

    public void setParameterService(ParameterService parameterService) {
        this.parameterService = parameterService;
    }
}
