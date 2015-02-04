package org.kuali.ole;

import org.kuali.common.util.DefaultProjectContext;
import org.kuali.ole.utility.Constants;

public class OleDocstoreProjectContext extends DefaultProjectContext {

    private static final String ARTIFACT_ID = "ole-docstore-webapp";

    public OleDocstoreProjectContext() {
        super(Constants.GROUP_ID, ARTIFACT_ID);
    }

}
