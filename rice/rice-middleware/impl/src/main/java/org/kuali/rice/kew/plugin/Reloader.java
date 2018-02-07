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
package org.kuali.rice.kew.plugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A runnable which continuously polls Reloadable to see if they need to be reloaded.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class Reloader implements Runnable {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(Reloader.class);

    private final Set<Reloadable> reloadables = Collections.synchronizedSet(new HashSet<Reloadable>());

    public void run() {
    	try {
    		LOG.debug("Checking if any reloading is necessary...");
    		synchronized (reloadables) {
    			for (Iterator iterator = reloadables.iterator(); iterator.hasNext();) {
    				Reloadable reloadable = (Reloadable) iterator.next();
    				LOG.debug("Checking reloadable: " + reloadable);
    				if (reloadable.isReloadable() && reloadable.isReloadNeeded()) {
    					/*long reloadWaitTime = getPluginReloadWaitTime();
    					 LOG.info("Detected that a reload was needed...sleeping for "+(reloadWaitTime/1000)+" seconds...");
    					 sleep(getPluginReloadWaitTime());*/
    					LOG.info("Reloading: " + reloadable);
    					reloadable.reload();
                    	/*sleep(5000);*/
    				}
    			}
    		}
    	} catch (Throwable t) {
    		LOG.error("Failed to reload plugin.", t);
    	}
    }

    public void addReloadable(Reloadable reloadable) {
        reloadables.add(reloadable);
    }

    public void removeReloadable(Reloadable reloadable) {
        reloadables.remove(reloadable);
    }

    public Set<Reloadable> getReloadables() {
    	return reloadables;
    }

}
