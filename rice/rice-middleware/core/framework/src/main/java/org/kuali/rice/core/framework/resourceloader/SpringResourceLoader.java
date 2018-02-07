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
package org.kuali.rice.core.framework.resourceloader;

import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.namespace.QName;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.config.ConfigurationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * A simple {@link org.kuali.rice.core.api.resourceloader.ResourceLoader} which wraps a Spring {@link ConfigurableApplicationContext}.
 *
 * Starts and stops the {@link ConfigurableApplicationContext}.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class SpringResourceLoader extends BaseResourceLoader {

	private SpringResourceLoader parentSpringResourceLoader;
	
	private ApplicationContext parentContext;
	private ConfigurableApplicationContext context;
	private ServletContext servletContextcontext;

	private final List<String> fileLocs;

	public SpringResourceLoader(QName name, String fileLoc, ServletContext context) {
	    this(name, Collections.singletonList(fileLoc), context);
	}

	public SpringResourceLoader(QName name, List<String> fileLocs, ServletContext servletContextcontext) {
		super(name);
		this.fileLocs = fileLocs;
		this.servletContextcontext = servletContextcontext;
	}

	@Override
	public Object getService(QName serviceName) {
	    	if (!isStarted()) {
	    	    return null;
	    	}
		if (this.getContext().containsBean(serviceName.toString())) {
			Object service = this.getContext().getBean(serviceName.toString());
			return postProcessService(serviceName, service);
		}
		return super.getService(serviceName);
	}

	@Override
	public void start() throws Exception {
		if(!isStarted()){
			LOG.info("Creating Spring context " + StringUtils.join(this.fileLocs, ","));
			if (parentSpringResourceLoader != null && parentContext != null) {
				throw new ConfigurationException("Both a parentSpringResourceLoader and parentContext were defined.  Only one can be defined!");
			}
			if (parentSpringResourceLoader != null) {
				parentContext = parentSpringResourceLoader.getContext();
			}
			
			if (servletContextcontext != null) {
				XmlWebApplicationContext lContext = new XmlWebApplicationContext();
				lContext.setServletContext(servletContextcontext);
				lContext.setParent(parentContext);
				lContext.setConfigLocations(this.fileLocs.toArray(new String[] {}));
				lContext.refresh();
				context = lContext;
			} else {
				this.context = new ClassPathXmlApplicationContext(this.fileLocs.toArray(new String[] {}), parentContext);
			}
           
			super.start();
		}
	}

	@Override
	public void stop() throws Exception {
		LOG.info("Stopping Spring context " + StringUtils.join(this.fileLocs, ","));
		this.context.close();
		super.stop();
	}

	public ConfigurableApplicationContext getContext() {
		return this.context;
	}
	
	public void setParentContext(ApplicationContext parentContext) {
		this.parentContext = parentContext;
	}

	public void setParentSpringResourceLoader(
			SpringResourceLoader parentSpringResourceLoader) {
		this.parentSpringResourceLoader = parentSpringResourceLoader;
	}

	@Override
	public String getContents(String indent, boolean servicePerLine) {
		String contents = "";
		for (String name : context.getBeanDefinitionNames()) {
			if (servicePerLine) {
				contents += indent + "+++" + name + "\n";
			} else {
				contents += name + ", ";
			}
		}
		return contents;
	}
	
	
}
