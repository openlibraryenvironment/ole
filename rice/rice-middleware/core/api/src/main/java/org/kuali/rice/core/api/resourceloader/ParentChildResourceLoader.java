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
package org.kuali.rice.core.api.resourceloader;

import java.util.List;

import javax.xml.namespace.QName;

import org.kuali.rice.core.api.reflect.ObjectDefinition;

/**
 * A resource loader implementation which checks a child resource loader first and if the resource is not found there,
 * checks a parent resource loader.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
class ParentChildResourceLoader implements ResourceLoader {

    private final ResourceLoader parent;
    private final ResourceLoader child;
    private QName name;

    ParentChildResourceLoader(ResourceLoader parent, ResourceLoader child) {
    	if (parent == null) {
    		throw new IllegalArgumentException("parent resource loader was null");
    	}
    	if (child == null) {
    		throw new IllegalArgumentException("child resource loader was null");
    	}
    	this.parent = parent;
    	this.child = child;
    	this.name = new QName(child.getName().toString() + " to parent " + parent.getName().toString());
    }

	@Override
	public <T> T getObject(ObjectDefinition definition) {
		T object = child.<T>getObject(definition);
    	if (object == null) {
    		object = parent.<T>getObject(definition);
    	}
    	return object;
	}

	@Override
	public <T> T getService(QName qname) {
		T service = child.<T>getService(qname);
		if (service == null) {
			service = parent.<T>getService(qname);
		}
		return service;
    }

	/**
	 * "Starts" this resource loader.  When started, this method will simply
	 * invoke start on the child resource loader.  It's assumed that the
	 * parent will be started from it's context.
	 * 
	 * @see org.kuali.rice.core.api.lifecycle.Lifecycle#start()
	 */
	@Override
    public void start() throws Exception {
		if (!child.isStarted()) {
			child.start();
		}
	}

	/**
	 * Just stops the internal child resource loader.
	 * 
	 * @see org.kuali.rice.core.api.lifecycle.Lifecycle#stop()
	 */
	@Override
    public void stop() throws Exception {
    	if (child.isStarted()) {
    		child.stop();
    	}
    }

	@Override
    public void addResourceLoader(ResourceLoader resourceLoader) {
    	this.child.addResourceLoader(resourceLoader);
    }

	@Override
    public void addResourceLoaderFirst(ResourceLoader resourceLoader) {
    	this.child.addResourceLoaderFirst(resourceLoader);
    }

	@Override
    public ResourceLoader getResourceLoader(QName name) {
    	ResourceLoader resourceLoader = this.child.getResourceLoader(name);
    	if (resourceLoader == null && parent != null) {
    		return parent.getResourceLoader(name);
    	} else {
    		return resourceLoader;
    	}
    }
  
	@Override
    public List<QName> getResourceLoaderNames() {
		return this.child.getResourceLoaderNames();
    }

	@Override
    public List<ResourceLoader> getResourceLoaders() {
		return this.child.getResourceLoaders();
    }

	@Override
    public void removeResourceLoader(QName name) {
		this.child.removeResourceLoader(name);
    }

	@Override
	public boolean isStarted() {
		return false;
	}

	@Override
	public void setName(QName name) {
		this.name = name;
	}

	@Override
	public QName getName() {
		return this.name;
	}

	@Override
	public String getContents(String indent, boolean servicePerLine) {
		StringBuilder contents = new StringBuilder();
		contents.append(parent.getContents(indent, servicePerLine)).append("\n");
		contents.append(child.getContents(indent, servicePerLine));
		return contents.toString();
	}

	

}
