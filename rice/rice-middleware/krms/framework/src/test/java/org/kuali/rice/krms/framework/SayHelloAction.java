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
package org.kuali.rice.krms.framework;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.ResultEvent;
import org.kuali.rice.krms.framework.engine.Action;
import org.kuali.rice.krms.framework.engine.ResultLogger;
import org.kuali.rice.krms.framework.engine.result.BasicResult;

/**
 * A test action class for the KRMS POC
 *
 */
public class SayHelloAction implements Action {
	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SayHelloAction.class);
	private static final ResultLogger KLog = ResultLogger.getInstance();
	
	public SayHelloAction(){}
	
	@Override
	public void execute(ExecutionEnvironment environment) {
		LOG.info("Hello!  Im executing an action.");
		KLog.logResult(new BasicResult(ResultEvent.ACTION_EXECUTED, this, environment));
	}
	
	@Override
	public void executeSimulation(ExecutionEnvironment environment) {
		throw new UnsupportedOperationException();
	}

	public String toString(){
		return getClass().getSimpleName();
	}
}
