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
package org.kuali.rice.ksb.messaging;

/**
 * Represents an alternate endpoint URL host based on an endpoint URL host replacement pattern (reg-exp) to replace with the
 * given actual endpoint host value.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AlternateEndpointLocation {

    private String endpointHostReplacementPattern;
    private String endpointHostReplacementValue;

    public String getEndpointHostReplacementPattern() {
	return this.endpointHostReplacementPattern;
    }

    public void setEndpointHostReplacementPattern(String endpointHostReplacementPattern) {
	this.endpointHostReplacementPattern = endpointHostReplacementPattern;
    }

    public String getEndpointHostReplacementValue() {
	return this.endpointHostReplacementValue;
    }

    public void setEndpointHostReplacementValue(String endpointHostReplacementValue) {
	this.endpointHostReplacementValue = endpointHostReplacementValue;
    }

}
