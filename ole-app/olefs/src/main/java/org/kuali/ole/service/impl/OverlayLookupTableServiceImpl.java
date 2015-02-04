package org.kuali.ole.service.impl;

import org.apache.log4j.Logger;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.service.*;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: premkb
 * Date: 2/2/13
 * Time: 7:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayLookupTableServiceImpl implements OverlayLookupTableService {

    private static final Logger LOG = Logger.getLogger(OverlayLookupTableServiceImpl.class);
    private OverlayDataFieldService overlayDataFieldService;
    @Override
    public String getFieldValueFromSubField(String incomingField,LinkedHashMap<String,SubField> subFieldMap)throws Exception{
        String incomingFieldValue = null;
        SubField subField = subFieldMap.get(incomingField);
        if(subField != null){
            incomingFieldValue = subField.getValue();
        }
        return incomingFieldValue;
    }
    public OverlayDataFieldService getOverlayDataFieldService() {
        if(overlayDataFieldService == null){
            overlayDataFieldService = GlobalResourceLoader.getService(OLEConstants.OVERLAY_DATAFIELD_SERVICE);
        }
        return overlayDataFieldService;
    }

    public void setOverlayDataFieldService(OverlayDataFieldService overlayDataFieldService) {
        this.overlayDataFieldService = overlayDataFieldService;
    }
}
            