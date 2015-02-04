package org.kuali.ole.factory;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.service.OverlayOutputService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 2/24/13
 * Time: 6:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayOutputServiceFactory {

    private OverlayOutputService overlayDocstoreOutputService;
    private OverlayOutputService overlayTransactionOutputService;

    public OverlayOutputService getOverlayTransactionOutputService() {
        return overlayTransactionOutputService;
    }

    public void setOverlayTransactionOutputService(OverlayOutputService overlayTransactionOutputService) {
        this.overlayTransactionOutputService = overlayTransactionOutputService;
    }

    public OverlayOutputService getOverlayDocstoreOutputService() {
        return overlayDocstoreOutputService;
    }

    public void setOverlayDocstoreOutputService(OverlayOutputService overlayDocstoreOutputService) {
        this.overlayDocstoreOutputService = overlayDocstoreOutputService;
    }

    public OverlayOutputService getOverlayOutputServiceFactory(String outputTargetObject){
        if(outputTargetObject!=null && outputTargetObject.equalsIgnoreCase(OLEConstants.OVERLAY_DOCSTORE_OUTPUT_TARGET_OBJECT)){
            overlayDocstoreOutputService = GlobalResourceLoader.getService(OLEConstants.OVERLAY_DOCSTORE_OUTPUT_SERVICE);
            return overlayDocstoreOutputService;
        } else if (outputTargetObject!=null && outputTargetObject.equalsIgnoreCase(OLEConstants.OVERLAY_ORDERRECORD)){
            return overlayTransactionOutputService;
        }
        return null;
    }
}
