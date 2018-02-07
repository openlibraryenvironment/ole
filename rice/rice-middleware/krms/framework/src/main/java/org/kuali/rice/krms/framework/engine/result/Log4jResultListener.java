/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.krms.framework.engine.result;

import org.kuali.rice.krms.api.engine.ResultEvent;

/**
 * A Log4j implementation of {@link ResultListener} which logs the output of the {@link ResultEvent} toString at the INFO
 * level to the Log4jResultListener.class org.apache.log4j.Logger
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class Log4jResultListener  implements ResultListener {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(Log4jResultListener.class);

    /**
     * Constructor
     */
	public Log4jResultListener(){}
	
	@Override
	public void handleEvent(ResultEvent resultEvent) {
		// TODO Auto-generated method stub
		if (LOG.isInfoEnabled()){
			LOG.info(resultEvent);
		}
		
	}

}
