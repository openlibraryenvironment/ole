package org.kuali.ole.describe.rule;

import org.kuali.ole.OLEConstants;
import org.kuali.ole.describe.bo.OleShelvingScheme;
import org.kuali.ole.describe.bo.ExternalDataSourceConfig;
import org.kuali.rice.krad.maintenance.MaintenanceDocument;
import org.kuali.rice.krad.rules.MaintenanceDocumentRuleBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: PJ7789
 * Date: 7/12/12
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleExternalDatSourceConfigRule
        extends MaintenanceDocumentRuleBase {


    protected boolean processCustomSaveDocumentBusinessRules(MaintenanceDocument document) {
        boolean isValid = true;
        ExternalDataSourceConfig externalDataSourceConfig = (ExternalDataSourceConfig) document
                .getNewMaintainableObject().getDataObject();

        isValid &= validateDuplicateDataSourceName(externalDataSourceConfig);
        isValid &= validateEmptyDataSourceName(externalDataSourceConfig);
        return isValid;
    }

    /**
     * This method  validates duplicate external dataSource name, Id and return boolean value.
     *
     * @param externalDataSourceConfig
     * @return boolean
     */
    private boolean validateDuplicateDataSourceName(ExternalDataSourceConfig externalDataSourceConfig) {

        if (externalDataSourceConfig.getName() != null) {
            Map<String, String> criteria = new HashMap<String, String>();
            criteria.put(OLEConstants.OleExternalDataSourceConfig.DATA_SOURCE_NM, externalDataSourceConfig.getName());
            List<ExternalDataSourceConfig> dataSourceNameInDatabase = (List<ExternalDataSourceConfig>) getBoService()
                    .findMatching(ExternalDataSourceConfig.class, criteria);

            if ((dataSourceNameInDatabase.size() > 0)) {
                for (ExternalDataSourceConfig dataSourceObj : dataSourceNameInDatabase) {
                    Integer dataSourceId = dataSourceObj.getId();
                    if (null == externalDataSourceConfig.getId() || (externalDataSourceConfig.getId().intValue()
                            != dataSourceId.intValue())) {
                        this.putFieldError(OLEConstants.OleExternalDataSourceConfig.DATA_SOURCE_NAME,
                                "error.duplicate.name");
                        return false;
                    }

                }
            }

        }
        return true;
    }


    /**
     * This method  validates empty external Data Source name and return boolean value.
     *
     * @param externalDataSourceConfig
     * @return boolean
     */
    private boolean validateEmptyDataSourceName(ExternalDataSourceConfig externalDataSourceConfig) {

        if (externalDataSourceConfig.getName() == null || externalDataSourceConfig.getName().equalsIgnoreCase("")
                || externalDataSourceConfig.getName().length() == 0) {
            this.putFieldError(OLEConstants.OleExternalDataSourceConfig.DATA_SOURCE_NAME, "error.empty.name");
            return false;
        }
        return true;
    }
}
