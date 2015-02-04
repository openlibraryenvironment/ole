package org.kuali.ole;

import org.kuali.common.util.DefaultProjectContext;
import org.kuali.common.util.Str;
import org.kuali.ole.utility.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pvsubrah on 7/15/14.
 */
public class OleLiquibaseSqlProjectContext extends DefaultProjectContext {
    private static final String ARTIFACT_ID = "ole-liquibase-upgrade-sql";

    public OleLiquibaseSqlProjectContext() {
        super(Constants.GROUP_ID, ARTIFACT_ID, getLocations());
    }

    private static List<String> getLocations() {
        List<String> locations = new ArrayList<String>();
        locations.add("classpath:" + Str.getPath(Constants.GROUP_ID) + "/" + ARTIFACT_ID + "/ole-liquibase-upgrade-sql-controller.properties");
        return locations;
    }
}
