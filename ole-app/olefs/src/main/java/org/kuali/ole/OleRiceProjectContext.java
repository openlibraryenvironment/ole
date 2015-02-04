package org.kuali.ole;

import org.kuali.common.util.DefaultProjectContext;
import org.kuali.ole.utility.Constants;

public class OleRiceProjectContext extends DefaultProjectContext {

	private static final String ARTIFACT_ID = "ole-rice-webapp";

	public OleRiceProjectContext() {
		super(Constants.GROUP_ID, ARTIFACT_ID);
	}

}
