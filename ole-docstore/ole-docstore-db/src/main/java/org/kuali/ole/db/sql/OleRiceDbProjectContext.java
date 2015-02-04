package org.kuali.ole.db.sql;

import org.kuali.common.util.DefaultProjectContext;
import org.kuali.common.util.Str;
import org.kuali.ole.utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class OleRiceDbProjectContext extends DefaultProjectContext {

    private static final String ARTIFACT_ID = "ole-docstore-db";

    public OleRiceDbProjectContext() {
        super(Constants.GROUP_ID, ARTIFACT_ID, getLocations());
    }

    private static List<String> getLocations() {
        List<String> locations = new ArrayList<String>();
        locations.add("classpath:" + Str.getPath(Constants.GROUP_ID) + "/" + ARTIFACT_ID + "/sql-controller.properties");
        return locations;
    }
}
