package org.kuali.ole.ingest.action;

import org.apache.log4j.Logger;
import org.kuali.ole.DataCarrierService;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.ingest.pojo.ProfileAttributeBo;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.framework.engine.Action;

import java.util.List;

/**
 * CreateBibAction is the action class for BatchIngest(Staff upload screen) which creates a bibliographic
 * record in Docstore.
 */
public class OverlayAction implements Action {
    private static final Logger LOG = Logger.getLogger(OverlayAction.class);
    private UpdateBibExcludingGPFieldAction updateBibExcludingGPFieldAction;
    private UpdateBibIncludingGPFieldAction updateBibIncludingGPFieldAction;
    private DataCarrierService dataCarrierService;

    /**
     *  This method takes the initial request when creating the BibAction.
     * @param executionEnvironment
     */
    @Override
    public void execute(ExecutionEnvironment executionEnvironment) {
        dataCarrierService = getDataCarrierService();
        List<ProfileAttributeBo> profileAttributesList = (List<ProfileAttributeBo>) dataCarrierService.getData(OLEConstants.PROFILE_ATTRIBUTE_LIST);

        String overlayOption = new String();
        try{
            for(ProfileAttributeBo profileAttributeBo : profileAttributesList){
                if(profileAttributeBo.getAttributeName().equals(OLEConstants.HIGHLEVEL_OVERLAY_OPTION)){
                    overlayOption = profileAttributeBo.getAttributeValue();
                }
            }
            if(LOG.isDebugEnabled()){
                LOG.debug("overlayOption------------->"+overlayOption);
            }
            if(overlayOption.equals(OLEConstants.OVERLAY_OPTION_DONT_IGNORE_GPF)){
                getUpdateBibExcludingGPFieldAction().execute(executionEnvironment);
            }else if(overlayOption.equals(OLEConstants.OVERLAY_OPTION_IGNORE_GPF)){
                getUpdateBibIncludingGPFieldAction().execute(executionEnvironment);
            }
            else {
                getUpdateBibExcludingGPFieldAction().execute(executionEnvironment);
            }
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.OVERLAY, true);
        }catch (Exception e){
            executionEnvironment.getEngineResults().setAttribute(OLEConstants.OVERLAY, null);
        }
    }

    /**
     *    Gets the updateBibExcludingGPFieldAction attribute.
     * @return  updateBibExcludingGPFieldAction.
     */
    public UpdateBibExcludingGPFieldAction getUpdateBibExcludingGPFieldAction() {
        if (null == updateBibExcludingGPFieldAction) {
            updateBibExcludingGPFieldAction = new UpdateBibExcludingGPFieldAction();
        }
        return updateBibExcludingGPFieldAction;
    }

    /**
     *    Gets the updateBibExcludingGPFieldAction attribute.
     * @return  updateBibExcludingGPFieldAction.
     */
    public UpdateBibIncludingGPFieldAction getUpdateBibIncludingGPFieldAction() {
        if (null == updateBibIncludingGPFieldAction) {
            updateBibIncludingGPFieldAction = new UpdateBibIncludingGPFieldAction();
        }
        return updateBibIncludingGPFieldAction;
    }

    /**
     *  Gets the dataCarrierService attribute.
     * @return  Returns dataCarrierService.
     */
    protected DataCarrierService getDataCarrierService() {
        return GlobalResourceLoader.getService(OLEConstants.DATA_CARRIER_SERVICE);
    }
    /**
     *   This method simulate the executionEnvironment.
     * @param executionEnvironment
     */

    @Override
    public void executeSimulation(ExecutionEnvironment executionEnvironment) {
        execute(executionEnvironment);
    }

}
