package org.kuali.ole.service.impl;

import org.kuali.ole.docstore.common.document.content.bib.marc.BibMarcRecord;
import org.kuali.ole.docstore.common.document.content.bib.marc.DataField;
import org.kuali.ole.docstore.common.document.content.bib.marc.SubField;
import org.kuali.ole.service.OverlayFileReaderService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: vivekb
 * Date: 2/23/13
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class OverlayMrcFileReaderServiceImpl implements OverlayFileReaderService{
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OverlayMrcFileReaderServiceImpl.class);

    private Object object;

    @Override
    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String getInputFieldValue(String incomingField) {
        if(object!=null && object instanceof BibMarcRecord) {
            BibMarcRecord bibMarcRecord = (BibMarcRecord) object;
            List<DataField> dataFields = bibMarcRecord.getDataFields();
            OverlayLookupTableServiceImpl overlayLookupTableService = new OverlayLookupTableServiceImpl();
            try {
                LinkedHashMap<String,SubField> subFieldMap =  overlayLookupTableService.getOverlayDataFieldService().getSubFieldValueMap(dataFields);
                String incomingFieldValue = overlayLookupTableService.getFieldValueFromSubField(incomingField, subFieldMap);
                return  incomingFieldValue!=null?incomingFieldValue:"";
            } catch (Exception e) {
                e.printStackTrace();
                LOG.error("Exception while getting input field value"+e.getMessage());
            }
        }
        return "";
    }
}
