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
package org.kuali.rice.core.web.listener;

import org.apache.log4j.Logger;
import org.kuali.rice.core.api.config.property.Config;
import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.reflect.ObjectDefinition;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Finds and executes listeners in the config file.
 */
public class BootstrapListener implements HttpSessionListener {
    private static final Logger LOG = Logger.getLogger(BootstrapListener.class);  
 
    private static final String LISTENER_PREFIX = "listener.";
    private static final String CLASS_SUFFIX = ".class";
 
    private boolean initted;
    
    private final Map<String, HttpSessionListener> listeners = new HashMap<String, HttpSessionListener>();
    
    private void addListener(String name, String classname) {
        LOG.debug("Adding listener: " + name + "=" + classname);
        
        Object listenerObject = GlobalResourceLoader.getResourceLoader().getObject(new ObjectDefinition(classname));
        
        if (listenerObject == null) {
            LOG.error("Listener '" + name + "' class not found: " + classname);
            return;

        }
        if (!(listenerObject instanceof HttpSessionListener)) {
            LOG.error("Class '" + listenerObject.getClass() + "' does not implement servlet javax.servlet.http.HttpSessionListener");
            return;
        }
        
        HttpSessionListener listener = (HttpSessionListener) listenerObject;
        listeners.put(name, listener);
    }
    
    private synchronized void init() {
        if (initted) {
            return;
        }
        
        LOG.debug("Initializing BootstrapListener...");
        
        Config cfg = ConfigContext.getCurrentContextConfig();
        
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Map<String, String> properties = new HashMap<String, String>((Map) cfg.getProperties());
        
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            String key = entry.getKey().toString();
            if (key.startsWith(LISTENER_PREFIX) && key.endsWith(CLASS_SUFFIX)) {
                String name = key.substring(LISTENER_PREFIX.length(), key.length() - CLASS_SUFFIX.length());
                String value = entry.getValue();
                addListener(name, value);
            }
        }

        initted = true;
    }
    
    /**
     * {@inheritDoc}
     * @see javax.servlet.http.HttpSessionListener#sessionCreated(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionCreated(final HttpSessionEvent event) {
        LOG.debug("Begin BootstrapListener session created...");
        
        init();
        
        for (HttpSessionListener listener : listeners.values()) {
            listener.sessionCreated(event);
        }
        
        LOG.debug("...end BootstrapListener session created.");
    }

    /**
     * {@inheritDoc}
     * @see javax.servlet.http.HttpSessionListener#sessionDestroyed(javax.servlet.http.HttpSessionEvent)
     */
    public void sessionDestroyed(final HttpSessionEvent event) {
        LOG.debug("Begin BootstrapListener session destroyed...");
        
        init();
        
        for (HttpSessionListener listener : listeners.values()) {
            listener.sessionDestroyed(event);
        }
        
        LOG.debug("...end BootstrapListener session destroyed.");
    }
    
    public Map<String, HttpSessionListener> getListeners() {
    	return listeners;
    }
}
