package org.kuali.ole.factory;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.pojo.bib.BibliographicRecord;
import org.kuali.ole.pojo.edi.LineItemOrder;
import org.kuali.ole.service.OverlayFileReaderService;

import java.util.HashMap;
/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 2/23/13
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayFileReaderFactory {

    private OverlayFileReaderService overlayMrcFileReaderService;
    private OverlayFileReaderService overlayEdiFileReaderService;

    public OverlayFileReaderService getOverlayMrcFileReaderService() {
        return overlayMrcFileReaderService;
    }

    public void setOverlayMrcFileReaderService(OverlayFileReaderService overlayMrcFileReaderService) {
        this.overlayMrcFileReaderService = overlayMrcFileReaderService;
    }

    public OverlayFileReaderService getOverlayEdiFileReaderService() {
        return overlayEdiFileReaderService;
    }

    public void setOverlayEdiFileReaderService(OverlayFileReaderService overlayEdiFileReaderService) {
        this.overlayEdiFileReaderService = overlayEdiFileReaderService;
    }

    public OverlayFileReaderService getOverlayFileReaderService(String fileType,HashMap<String,Object> objects){
        if(fileType!=null && fileType.equalsIgnoreCase(OLEConstants.EDI)){
            overlayEdiFileReaderService.setObject(objects.get(fileType));
            return overlayEdiFileReaderService;
        }else if (fileType!=null && fileType.equalsIgnoreCase(OLEConstants.MRC)){
            overlayMrcFileReaderService.setObject(objects.get(fileType));
            return overlayMrcFileReaderService;
        }
        return null;
    }

}
