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
package org.kuali.rice.kew.lifecycle;

import org.kuali.rice.core.api.config.property.ConfigContext;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.lifecycle.BaseCompositeLifecycle;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.kew.mail.EmailReminderLifecycle;

import java.util.LinkedList;
import java.util.List;


/**
 * A temporary lifecycle that lives in embedded space.  Will be removed when the embedded plugin is factored out.
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class StandaloneLifeCycle extends BaseCompositeLifecycle {

    @Override
    protected List<Lifecycle> loadLifecycles() throws Exception {
    	List<Lifecycle> lifecycles = new LinkedList<Lifecycle>();
    	if (ConfigContext.getCurrentContextConfig().getXmlPipelineLifeCycleEnabled()) {
            lifecycles.add(new XmlPipelineLifeCycle());
    	}
    	if (ConfigContext.getCurrentContextConfig().getEmailReminderLifecycleEnabled()) {
            lifecycles.add(new EmailReminderLifecycle());
    	}
    	return lifecycles;
	}

}
