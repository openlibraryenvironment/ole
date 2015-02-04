package org.kuali.ole.sys.context;

import org.kuali.common.util.DefaultProjectContext;
import org.kuali.ole.utility.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/31/13
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleFsProjectContext extends DefaultProjectContext {
    public OleFsProjectContext() {
        super(Constants.GROUP_ID, "olefs-webapp");
    }
}
