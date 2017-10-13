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
package org.kuali.rice.krms.framework.engine;

import javax.swing.event.EventListenerList;

import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.ExecutionFlag;
import org.kuali.rice.krms.api.engine.ResultEvent;
import org.kuali.rice.krms.framework.engine.result.EngineResultListener;
import org.kuali.rice.krms.framework.engine.result.Log4jResultListener;
import org.kuali.rice.krms.framework.engine.result.ResultListener;

/**
 * A ResultLogger which invokes its listener's handleEvent method (passing in the {@link ResultEvent}) if the event's
 * Environment is enabled.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ResultLogger {
	private EventListenerList listenerList = new EventListenerList();
	
	private ResultLogger(){}
	
	/*using inner class provides thread safety.	 */
	private static class KRMSLoggerLoader{
		private static final ResultLogger INSTANCE = new ResultLogger();
	}

    /**
     * @return KRMSLoggerLoader.INSTANCE
     */
	public static ResultLogger getInstance(){
		return KRMSLoggerLoader.INSTANCE;
	}

    /**
     * Add a {@link ResultListener} see logResult
     * @param resultListener {@link ResultListener} to add
     */
	public void addListener(ResultListener resultListener) {
		listenerList.add(ResultListener.class, resultListener);
	}

    /**
     * Be kind, please rewind.  $1 charge for removing un-removed {@link ResultListener}
     * @param resultListener {@link ResultListener} to remove
     */
	public void removeListener(ResultListener resultListener){
		listenerList.remove(ResultListener.class, resultListener);
	}

    /**
     * Invoke the handleEvent method of the listeners if the event's Environment is enabled.
     * @param event {@link ResultEvent} to invoke with listeners handleEvent if the event's Environment is enabled.
     */
	public void logResult(ResultEvent event){
		if (isEnabled(event.getEnvironment())){
			// fire event to listeners
			Object[] listeners = listenerList.getListenerList();
			for (int i=1; i<listeners.length; i+=2){
				((ResultListener) listeners[i]).handleEvent(event);
			}
		}
	}

    /**
     * Returns true if the {@link ExecutionEnvironment}'s execution options {@link ExecutionFlag.LOG_EXECUTION} flag has been set.
     * @param environment {@link ExecutionEnvironment} to test for being enabled.
     * @return
     */
	public boolean isEnabled(ExecutionEnvironment environment){
	    return (
	            environment != null 
	            && environment.getExecutionOptions() != null 
	            && environment.getExecutionOptions().getFlag(ExecutionFlag.LOG_EXECUTION)
	    );
	}
}
