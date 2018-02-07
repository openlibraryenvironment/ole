/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.test.lifecycles;

import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.core.api.lifecycle.Lifecycle;
import org.kuali.rice.test.data.PerTestUnitTestData;
import org.kuali.rice.test.data.UnitTestData;
import org.kuali.rice.test.data.UnitTestDataUtils;

import java.lang.reflect.Method;

/**
 * A lifecycle for loading SQL datasets based on the PerTestUnitTestData annotation. The individual SQL statements are
 * loaded first, followed by the statements inside the files (files are loaded sequentially in the order listed in the
 * annotation).
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PerTestDataLoaderLifecycle implements Lifecycle {
	private boolean started;
	private Method method;

	public PerTestDataLoaderLifecycle(Method method) {
		this.method = method;
	}

	public boolean isStarted() {
		return started;
	}

	public void start() throws Exception {
		if (method.getDeclaringClass().isAnnotationPresent(PerTestUnitTestData.class)) {
			UnitTestData[] data = method.getDeclaringClass().getAnnotation(PerTestUnitTestData.class).value();
            UnitTestDataUtils.executeDataLoader(data);
		}
		if (method.isAnnotationPresent(UnitTestData.class)) {
			UnitTestData data = method.getAnnotation(UnitTestData.class);
            UnitTestDataUtils.executeDataLoader(data);
		}
		started = true;
	}

	public void stop() throws Exception {
	    if (method.getDeclaringClass().isAnnotationPresent(PerTestUnitTestData.class)) {
            UnitTestData[] data = method.getDeclaringClass().getAnnotation(PerTestUnitTestData.class).tearDown();
            UnitTestDataUtils.executeDataLoader(data);
        }
		started = false;
	}
}
