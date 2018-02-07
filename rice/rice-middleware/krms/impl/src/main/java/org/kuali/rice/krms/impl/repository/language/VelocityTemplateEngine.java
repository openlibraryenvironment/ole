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
package org.kuali.rice.krms.impl.repository.language;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.apache.velocity.tools.generic.ComparisonDateTool;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;
import org.apache.velocity.tools.generic.NumberTool;
import org.apache.velocity.tools.generic.SortTool;

/**
 * Velocity template engine.
 * <p>Velocity tools supported (See <a href="http://velocity.apache.org/tools/devel/generic.html">http://velocity.apache.org/tools/devel/generic.html<a/>}:</p>
 * <ul>
 * <li><a href="http://velocity.apache.org/tools/devel/javadoc/org/apache/velocity/tools/generic/DateTool.html">DateTool</a></li>
 * <li><a href="http://velocity.apache.org/tools/devel/javadoc/org/apache/velocity/tools/generic/ComparisonDateTool.html">ComparisonDateTool</a></li>
 * <li><a href="http://velocity.apache.org/tools/devel/javadoc/org/apache/velocity/tools/generic/MathTool.html">MathTool</a></li>
 * <li><a href="http://velocity.apache.org/tools/devel/javadoc/org/apache/velocity/tools/generic/NumberTool.html">NumberTool</a></li>
 * <li><a href="http://velocity.apache.org/tools/devel/javadoc/org/apache/velocity/tools/generic/SortTool.html">SortTool</a></li>
 * </ul>
 * 
 * Examples:
 * <pre>
 * $dateTool:            <code>$dateTool.get('yyyy-M-d H:m:s')                                 -> 2003-10-19 21:54:50</code>
 * 
 * $dateComparisonTool:  <code>$dateComparisonTool.difference('2005-07-04','2007-02-15').abbr  -> 1 yr</code>
 * 
 * $mathTool:            <code>$mathTool.toNumber($value)                                      -> Converts java.lang.String $value to a java.lang.Number</code>
 * 
 * $numberTool:          <code>$numberTool.currency($myNumber)                                 -> $13.55</code>
 * 
 * $sortTool:            <code>$sorter.sort($collection, "name:asc")                           -> Sorts $collection with property 'name' in ascending order</code>
 * </pre>
 */
public class VelocityTemplateEngine {

	private final VelocityEngine velocityEngine = new VelocityEngine();
	private VelocityContext defaultContext;
	private Map<String,Object> configMap = new HashMap<String, Object>();

	/**
	 * Constructs a velocity template engine.
	 */
	public VelocityTemplateEngine() {
		init();
	}

	/**
	 * Constructs a velocity template engine with velocity tools configurations.
	 * 
	 * @param config Velocity tools configurations
	 */
	public VelocityTemplateEngine(final Map<String,Object> config) {
		this.configMap = config;
		init();
	}

	/**
	 * Initializes Velocity engine
	 */
	private void init() {
        velocityEngine.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        setLogFile();

        DateTool dateTool = new DateTool();
        dateTool.configure(this.configMap);
        MathTool mathTool = new MathTool();
        NumberTool numberTool = new NumberTool();
        numberTool.configure(this.configMap);
        SortTool sortTool = new SortTool();
        
        defaultContext = new VelocityContext();
        defaultContext.put("dateTool", dateTool);
        defaultContext.put("dateComparisonTool", new ComparisonDateTool());
        defaultContext.put("mathTool", mathTool);
        defaultContext.put("numberTool", numberTool);
        defaultContext.put("sortTool", sortTool);
        // Following tools need VelocityTools version 2.0+
        //defaultContext.put("displayTool", new DisplayTool());
        //defaultContext.put("xmlTool", new XmlTool());
        
        try {
			velocityEngine.init();
		} catch (Exception e) {
			throw new VelocityException(e);
		}
	}

	/**
	 * Sets logging on or off.
	 *   
	 * @param enableLogging True enables logging; false disables logging
	 */
	public void setLogging(boolean enableLogging) {
		if (!enableLogging) {
	        // Line below to disables logging, remove to enable
	    	velocityEngine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");
        } else {
	    	velocityEngine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, null);
	    	setLogFile();
        }
	}
		
	/**
	 * Sets the Velocity Log File to the default location.  Either 
	 * <code>{catalina.base}/velocity.log</code> or <code>target/velocity.log</code>. 
	 */
	public void setLogFile() {
		if (System.getProperty("catalina.base") != null) {
			setLogFile(System.getProperty("catalina.base") + "/logs/velocity.log");
		} else {
			setLogFile("target/velocity.log");
		}
	}
	
	/**
	 * Sets the Velocity Log File.
	 *   
	 * @param logFile the path and filename for velocity logging 
	 */
	public void setLogFile(final String logfile) {
		velocityEngine.setProperty(VelocityEngine.RUNTIME_LOG, logfile);
	}
	
	/**
	 * Evaluates a template with a map of objects. <code>contextMap</code> can
	 * be null if no keys/tokens are referenced in the <code>template</code>
	 * 
	 * @param contextMap Map of objects to be used in the template
	 * @param template Velocity Template
	 * @return Evaluated template
	 */
	public String evaluate(final Map<String, Object> contextMap, final String template) throws VelocityException {
		Reader readerOut = null;
		try {
			readerOut = new BufferedReader(new StringReader(template));
			return evaluate(contextMap, readerOut);
		} finally {
			if(readerOut != null) {
				try {
					readerOut.close();
				} catch (IOException e) {
					throw new VelocityException(e);
				}
					
			}
		}
	}

	/**
	 * Evaluates a template with a map of objects.
	 * 
	 * @param mapContext Map of Objects to be used in the template
	 * @param template Velocity Template
	 * @return Evaluated template
	 */
	public String evaluate(final Map<String, Object> mapContext, final Reader template) throws VelocityException {
		VelocityContext context = new VelocityContext(mapContext, defaultContext);
		
		StringWriter writerOut = new StringWriter();
		try {
			velocityEngine.evaluate(context, writerOut, "VelocityEngine", template);
			return writerOut.toString();
		} catch(Exception e) {
			throw new VelocityException(e);
		}
	}

}

