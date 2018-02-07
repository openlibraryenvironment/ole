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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.core.api.reflect.ObjectDefinition;

/**
 * A {@link ResourceLoader} which acts as a container for other ResourceLoaders.
 * Effectively, implements a composite pattern for ResourceLoaders.
 *
 * @see ResourceLoader
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ResourceLoaderContainer extends BaseLifecycle implements ResourceLoader {

	private static final Logger LOG = Logger.getLogger(ResourceLoaderContainer.class);

	private QName name;

	private List<ResourceLoader> resourceLoaders = new ArrayList<ResourceLoader>();

	public ResourceLoaderContainer(QName name) {
		this.name = name;
	}

	public void start() throws Exception {
		for (ResourceLoader resourceLoader : this.resourceLoaders) {
			LOG.info("Starting ResourceLoader " + resourceLoader.getName());
			resourceLoader.start();
		}
		super.start();
	}

	public void stop() throws Exception {
		while (!this.resourceLoaders.isEmpty()) {
			ResourceLoader rl = this.resourceLoaders.get(this.resourceLoaders.size() - 1);
			rl.stop();
			this.resourceLoaders.remove(rl);
		}

		super.stop();
		this.resourceLoaders.clear();
	}

	public void addResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoaders.add(resourceLoader);
		
		//FIXME: RICE MODULARITY
		moveKSBLoadersDownHack();		
	}
	
	/** hack to move the ksb Resource loaders down in the RL stack. */
	private void moveKSBLoadersDownHack() {
		//FIXME: RICE MODULARITY 
		for (int i = 0; i < resourceLoaders.size(); i++) {
			final boolean remote = resourceLoaders.get(i).getName().toString().toUpperCase().contains("KSB");
			if (remote) {
				final ResourceLoader removed = this.resourceLoaders.remove(i);
				this.resourceLoaders.add(this.resourceLoaders.size(), removed);
			}
		}
	}

	public void addResourceLoaderFirst(ResourceLoader resourceLoader) {
		this.resourceLoaders.add(0, resourceLoader);

		//FIXME: RICE MODULARITY
		moveKSBLoadersDownHack();
	}

	public boolean containsResourceLoader(ResourceLoader resourceLoader) {
		return this.resourceLoaders.contains(resourceLoader);
	}

	public ResourceLoader getResourceLoader(QName name) {

		if (this.getName().equals(name)) {
			return this;
		}

		for (Iterator<ResourceLoader> iter = this.resourceLoaders.iterator(); iter.hasNext();) {
			ResourceLoader loader = iter.next();
			if (loader.getName().equals(name)) {
				return loader;
			}
			ResourceLoader loader2 = loader.getResourceLoader(name);
			if (loader2 != null) {
				return loader2;
			}
		}
		return null;
	}

	public List<QName> getResourceLoaderNames() {
		List<QName> names = new ArrayList<QName>();
		for (Iterator<ResourceLoader> iter = this.resourceLoaders.iterator(); iter.hasNext();) {
			names.add(iter.next().getName());
		}
		return names;
	}

	public void removeAllResourceLoaders() {
	    this.resourceLoaders.clear();
	}

	public void removeResourceLoader(QName name) {
		ResourceLoader loaderToRemove = null;
		for (Iterator<ResourceLoader> iter = this.resourceLoaders.iterator(); iter.hasNext();) {
			ResourceLoader loader = iter.next();
			if (loader.getName().equals(name)) {
				loaderToRemove = loader;
			}
		}
		if (loaderToRemove != null) {
			try {
				loaderToRemove.stop();
			} catch (Exception e) {
				LOG.error("Failed to stop plugin " + loaderToRemove.getName() + " on removal", e);
			}
			this.resourceLoaders.remove(loaderToRemove);
		}
	}

	public List<ResourceLoader> getResourceLoaders() {
		return this.resourceLoaders;
	}



	public Object getObject(ObjectDefinition definition) {
		for (ResourceLoader resourceLoader : this.resourceLoaders) {
			Object object = resourceLoader.getObject(definition);
			if (object != null) {
				return object;
			}
		}
		return null;
	}

	public Object getService(QName qname) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("ResourceLoader " + getName() + " fetching service " + qname);
		}
		for (ResourceLoader resourceLoader : this.resourceLoaders) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Delegating fetch to " + this);
			}
			Object service = resourceLoader.getService(qname);
			if (service != null) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Found service from " + this);
				}
				return service;
			}
		}
		return null;
	}

	public String getContents(String indent, boolean servicePerLine) {
		String contents = indent + this + "\n";

		for (ResourceLoader resourceLoader : this.resourceLoaders) {
			contents += resourceLoader.getContents(indent + "+++", servicePerLine);
		}

		return contents;
	}

	@Override
	public String toString() {
		return "Resource Loader: " + this.name + " (" + ObjectUtils.identityToString(this) + ") direct children resource loaders size: " + this.resourceLoaders.size();
	}

	public QName getName() {
		return this.name;
	}

	public void setName(QName name) {
		this.name = name;
	}





}
