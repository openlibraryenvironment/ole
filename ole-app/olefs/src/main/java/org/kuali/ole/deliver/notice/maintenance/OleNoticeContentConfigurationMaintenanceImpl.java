package org.kuali.ole.deliver.notice.maintenance;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeContentConfigurationBo;
import org.kuali.ole.deliver.notice.bo.OleNoticeFieldLabelMapping;
import org.kuali.rice.krad.maintenance.MaintainableImpl;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by maheswarang on 7/9/15.
 */
public class OleNoticeContentConfigurationMaintenanceImpl extends MaintainableImpl {


    /**
     * This method will set the description for edit operation
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterNew(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterEdit(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription(OLEConstants.NEW_NOTICE_CONTENT_CONFIG_DOC);
    }
    /**
     * This method will set the description when copy is selected
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterCopy(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterCopy(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription(OLEConstants.COPY_NOTICE_CONTENT_CONFIG_DOC);

    }

    /**
     * This method will set the description for edit operation
     *
     * @param document
     * @param requestParameters
     */
    @Override
    public void processAfterEdit(MaintenanceDocument document, Map<String, String[]> requestParameters) {
        super.processAfterEdit(document, requestParameters);
        document.getDocumentHeader().setDocumentDescription(OLEConstants.EDIT_NOTICE_CONTENT_CONFIG_DOC);
    }

    @Override
    public Object retrieveObjectForEditOrCopy(MaintenanceDocument document, Map<String, String> dataObjectKeys) {
        Object object = super.retrieveObjectForEditOrCopy(document, dataObjectKeys);
        if (object != null && object instanceof OleNoticeContentConfigurationBo) {
            OleNoticeContentConfigurationBo oleNoticeContentConfigurationBo = (OleNoticeContentConfigurationBo)object;
            List<OleNoticeFieldLabelMapping> oleNoticeFieldLabelMappings =  oleNoticeContentConfigurationBo.getOleNoticeFieldLabelMappings();
              if(oleNoticeFieldLabelMappings !=null && oleNoticeFieldLabelMappings.size()>0){
                  for(OleNoticeFieldLabelMapping oleNoticeFieldLabelMapping : oleNoticeFieldLabelMappings){
                      if(StringUtils.isNotEmpty(oleNoticeFieldLabelMapping.getBelongsTo()) && oleNoticeFieldLabelMapping.getBelongsTo().equalsIgnoreCase("PATRON")){
                          oleNoticeContentConfigurationBo.getOleNoticePatronFieldLabelMappings().add(oleNoticeFieldLabelMapping);
                      }
                      else{
                          oleNoticeContentConfigurationBo.getOleNoticeItemFieldLabelMappings().add(oleNoticeFieldLabelMapping);
                      }
                  }
              }
        }
        return object;
    }
}
