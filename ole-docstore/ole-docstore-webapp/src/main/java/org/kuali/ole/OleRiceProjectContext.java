package org.kuali.ole;

import org.kuali.common.util.DefaultProjectContext;
import org.kuali.ole.utility.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 6/11/13
 * Time: 10:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class OleRiceProjectContext extends DefaultProjectContext {

    private static final String ARTIFACT_ID = "ole-docstore-webapp";

    public OleRiceProjectContext() {
        super(Constants.GROUP_ID, ARTIFACT_ID);
    }
}
