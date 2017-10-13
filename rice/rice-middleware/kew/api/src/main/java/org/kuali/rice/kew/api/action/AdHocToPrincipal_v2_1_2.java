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
package org.kuali.rice.kew.api.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

@XmlRootElement(name = AdHocToPrincipal_v2_1_2.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = AdHocToPrincipal_v2_1_2.Constants.TYPE_NAME, propOrder = {
		AdHocToPrincipal_v2_1_2.Elements.TARGET_PRINCIPAL_ID
})
public final class AdHocToPrincipal_v2_1_2 extends AdHocCommand {

	private static final long serialVersionUID = -4512561589558793736L;

	@XmlElement(name = Elements.TARGET_PRINCIPAL_ID, required = true)
	private final String targetPrincipalId;

	private AdHocToPrincipal_v2_1_2() {
		this.targetPrincipalId = null;
	}
	
	private AdHocToPrincipal_v2_1_2(Builder builder) {
		super(builder);
		this.targetPrincipalId = builder.getTargetPrincipalId();
	}

	public String getTargetPrincipalId() {
		return targetPrincipalId;
	}
	
	public static final class Builder extends AdHocCommand.Builder<AdHocToPrincipal_v2_1_2> {
		
		private static final long serialVersionUID = 5288681963619747957L;

		private String targetPrincipalId;
		
		private Builder(ActionRequestType actionRequested, String nodeName, String targetPrincipalId) {
			super(actionRequested, nodeName);
			setTargetPrincipalId(targetPrincipalId);
		}
		
		public static Builder create(ActionRequestType actionRequested, String nodeName, String targetPrincipalId) {
			return new Builder(actionRequested, nodeName, targetPrincipalId);
		}
		
		public String getTargetPrincipalId() {
			return targetPrincipalId;
		}
		
		public void setTargetPrincipalId(String targetPrincipalId) {
			if (StringUtils.isBlank(targetPrincipalId)) {
				throw new IllegalArgumentException("targetPrincipalId was null or blank");
			}
			this.targetPrincipalId = targetPrincipalId;
		}
		
		@Override
		public AdHocToPrincipal_v2_1_2 build() {
			return new AdHocToPrincipal_v2_1_2(this);
		}

	}
	
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "adHocToPrincipal";
        final static String TYPE_NAME = "AdHocToPrincipalType";
    }
    
    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String TARGET_PRINCIPAL_ID = "targetPrincipalId";
    }

	public static AdHocToPrincipal to(AdHocToPrincipal_v2_1_2 adHocToPrincipal) {
		return AdHocToPrincipal.Builder.create(adHocToPrincipal.getActionRequested(), adHocToPrincipal.getNodeName(), adHocToPrincipal.getTargetPrincipalId()).build();
	}
}
