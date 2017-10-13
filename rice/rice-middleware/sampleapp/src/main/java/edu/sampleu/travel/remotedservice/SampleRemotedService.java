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
package edu.sampleu.travel.remotedservice;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceIllegalArgumentException;
import org.kuali.rice.ksb.messaging.service.KSBXMLService;

/**
 * Sample remoted service 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class SampleRemotedService implements KSBXMLService {

    public void invoke(String message) {
	    if (StringUtils.isBlank(message)) {
            throw new RiceIllegalArgumentException("message is null or blank");
        }

    System.out.println("A MESSAGE WAS RECIEVED!!!");
	System.out.println("");
	System.out.println("");
	System.out.println("");
	System.out.println("");
	System.out.println("");
	System.out.println("");
	System.out.println("THE MESSAGE WAS -->" + message);
    }
}
