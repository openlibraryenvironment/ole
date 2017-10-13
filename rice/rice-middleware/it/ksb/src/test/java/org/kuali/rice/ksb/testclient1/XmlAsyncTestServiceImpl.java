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
package org.kuali.rice.ksb.testclient1;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.ksb.messaging.service.KSBXMLService;

public class XmlAsyncTestServiceImpl implements KSBXMLService {

    private static final Logger LOG = Logger.getLogger(XmlAsyncTestServiceImpl.class);
    
	public void invoke(String message) {
        if (StringUtils.isBlank(message)) {
            throw new RiceIllegalArgumentException("message is null or blank");
        }

	    LOG.info("invoked with message: " + message);
	}


}
