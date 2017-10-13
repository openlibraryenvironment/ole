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
package org.kuali.rice.kew.test;

import org.kuali.rice.core.api.lifecycle.BaseLifecycle;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;

public class TestSpringLifecycle extends BaseLifecycle {

	private String altAppContextFile;
	private String altOjbFile;

	public TestSpringLifecycle(String altAppContextFile, String altOjbFile) {
		this.altAppContextFile = altAppContextFile;
		this.altOjbFile = altOjbFile;
	}

	public void start() throws Exception {
		//this starts Spring
//		SpringLoader.getInstance().setToTestMode(altAppContextFile, altOjbFile);
//		if (!GlobalResourceLoader.isInitialized()) {
//			GlobalResourceLoader.addResourceLoader(new BaseResourceLoader(ResourceLoader.ROOT_RESOURCE_LOADER_NAME, SpringLoader.getInstance()));
//			GlobalResourceLoader.addResourceLoader(new RemoteResourceServiceLocatorImpl());
//			GlobalResourceLoader.start();
//		}
//		TransactionManager transactionManager = SpringServiceLocator.getTransactionManager();
//		if (transactionManager instanceof Current) {
//        	((Current)transactionManager).setDefaultTimeout(3600);
//        }
//        super.start();
		throw new UnsupportedOperationException("I'm broken from the test refactoring.  Have a nice day");
	}

	public void stop() throws Exception {
		GlobalResourceLoader.stop();
		super.stop();
	}

	public String getAltAppContextFile() {
		return altAppContextFile;
	}

	public String getAltOjbFile() {
		return altOjbFile;
	}

}
