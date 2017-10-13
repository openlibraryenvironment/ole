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

import java.util.Collections;
import java.util.EventObject;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.kuali.rice.krms.api.engine.ExecutionEnvironment;
import org.kuali.rice.krms.api.engine.ResultEvent;

/**
 * An implementation of {@link ResultEvent}
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BasicResult extends EventObject implements ResultEvent {
	private static final long serialVersionUID = -4124200802034785921L;

    private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd HH.mm.ss.SSS");

	protected String type;
	protected DateTime timestamp;
	protected ExecutionEnvironment environment;
	protected Boolean result = null;
	protected String description;
	protected Map<String, ?> resultDetails;

    // TODO Consider static factory methods in stead of constructors - Item 1 Effective Java 2nd Edition
    // TODO Consider a builder when faced with many constructor parameters - Item 2 Effective Java 2nd Edition
    /**
     * Constructor 
     * @param resultDetails of the ResultEvent
     * @param eventType String of the ResultEvent
     * @param source Object of the ResultEvent
     * @param environment {@link ExecutionEnvironment}
     * @param result boolean of the ResultEvent
     */
    public BasicResult(Map<String, ?> resultDetails, String eventType, Object source, ExecutionEnvironment environment, boolean result) {
        this(resultDetails, null, eventType, source, environment, result);
    }

    /**
     * Constructor
     * @param resultDetails of the ResultEvent
     * @param description String of the ResultEvent
     * @param eventType String of the ResultEvent
     * @param source Object of the ResultEvent
     * @param environment {@link ExecutionEnvironment}
     * @param result boolean of the ResultEvent
     */
    public BasicResult(Map<String, ?> resultDetails, String description, String eventType, Object source, ExecutionEnvironment environment, boolean result) {
        this(eventType, source, environment);
        this.resultDetails = resultDetails;
        this.result = new Boolean(result);
        this.description = (description == null) ? StringUtils.EMPTY : description;
    }

    /**
     * Constructor
     * @param description String of the ResultEvent
     * @param eventType String of the ResultEvent
     * @param source Object of the ResultEvent
     * @param environment {@link ExecutionEnvironment}
     * @param result boolean of the ResultEvent
     */
    public BasicResult(String description, String eventType, Object source, ExecutionEnvironment environment, boolean result) {
		this(eventType, source, environment);
		this.result = new Boolean(result);
		this.description = description;
	}

    /**
     * Constructor
     * @param eventType String of the ResultEvent
     * @param source Object of the ResultEvent
     * @param environment {@link ExecutionEnvironment}
     * @param result boolean of the ResultEvent
     */
	public BasicResult(String eventType, Object source, ExecutionEnvironment environment, boolean result) {
		this(eventType, source, environment);
		this.result = new Boolean(result);
	}

    /**
     * Constructor
     * @param eventType String of the ResultEvent
     * @param source Object of the ResultEvent
     * @param environment {@link ExecutionEnvironment}
     */
	public BasicResult(String eventType, Object source, ExecutionEnvironment environment) {
		super(source);
		this.type = eventType;
		this.timestamp = new DateTime(); 
		this.environment = environment;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public DateTime getTimestamp() {
		return timestamp;
	}
	
	@Override
	public ExecutionEnvironment getEnvironment(){
		return environment;
	}
	
	@Override
	public Boolean getResult(){
		return result;
	}

	@Override
	public String getDescription() {
	    return description;
	}

    /**
     * Returns the result details of the ResultEvent as an unmodifiable Map<?,?>
     * @return result details of the ResultEvent as an unmodifiable Map<?,?>
     */
	@Override
	public Map<String, ?> getResultDetails() {
	    if (resultDetails == null) {
	        return Collections.emptyMap();
	    } else {
	        return Collections.unmodifiableMap(resultDetails);
	    }
	}

    @Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append(fmt.print(this.getTimestamp()));
		sb.append(" EventType: "+ getType());
		sb.append(" ( "+ this.getSource().toString());
		if (this.getResult() != null){
			sb.append(" evaluated to: "+ this.getResult().toString());
		}
		sb.append(" )");
		return sb.toString();
	}
}
