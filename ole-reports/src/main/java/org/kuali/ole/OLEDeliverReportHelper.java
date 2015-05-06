package org.kuali.ole;

import org.apache.commons.lang.StringUtils;
import org.kuali.ole.config.OLEReportDBConfig;

import java.sql.Timestamp;

/**
 * Created by chenchulakshmig on 4/8/15.
 */
public class OLEDeliverReportHelper {

    OLEReportDBConfig oleReportDBConfig = new OLEReportDBConfig();

    public String getLibraryLocation(String location, String locationLevel) {
        if (StringUtils.isNotBlank(locationLevel)) {
            String[] locationLevels = locationLevel.split("/");
            for (int index = 0; index < locationLevels.length; index++) {
                if (locationLevels[index].equalsIgnoreCase("Library")) {
                    String[] locations = location.split("/");
                    return locations[index];
                }
            }
        }
        return "";
    }

    public String getDbVendor() {
        String dbVendor = oleReportDBConfig.getPropertyByKey("db.vendor");
        if (StringUtils.isNotBlank(dbVendor)) {
            return dbVendor;
        }
        return null;
    }

    public String getItemAvailabilityStatus(String missingPiece){
        if (StringUtils.isNotBlank(missingPiece)) {
            if (missingPiece.equalsIgnoreCase("Y")){
                return "Missing";
            }
            else {
                return "Lost";
            }
        }
        return "";
    }
}
