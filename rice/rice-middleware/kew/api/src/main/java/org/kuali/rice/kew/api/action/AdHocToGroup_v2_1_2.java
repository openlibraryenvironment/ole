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

@XmlRootElement(name = AdHocToGroup_v2_1_2.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = AdHocToGroup_v2_1_2.Constants.TYPE_NAME, propOrder = {
		AdHocToGroup_v2_1_2.Elements.TARGET_GROUP_ID
})
public final class AdHocToGroup_v2_1_2 extends AdHocCommand {

	private static final long serialVersionUID = 1543126020560887187L;

	@XmlElement(name = Elements.TARGET_GROUP_ID, required = true)
	private final String targetGroupId;

	private AdHocToGroup_v2_1_2() {
		this.targetGroupId = null;
	}
	
	private AdHocToGroup_v2_1_2(Builder builder) {
		super(builder);
		this.targetGroupId = builder.getTargetGroupId();
	}

	public String getTargetGroupId() {
		return targetGroupId;
	}
	
	public static final class Builder extends AdHocCommand.Builder<AdHocToGroup_v2_1_2> {
		
		private static final long serialVersionUID = 3062630774766721773L;

		private String targetGroupId;
		
		private Builder(ActionRequestType actionRequested, String nodeName, String targetGroupId) {
			super(actionRequested, nodeName);
			setTargetGroupId(targetGroupId);
		}
		
		public static Builder create(ActionRequestType actionRequested, String nodeName, String targetGroupId) {
			return new Builder(actionRequested, nodeName, targetGroupId);
		}
		
		public String getTargetGroupId() {
			return targetGroupId;
		}
		
		public void setTargetGroupId(String targetGroupId) {
			if (StringUtils.isBlank(targetGroupId)) {
				throw new IllegalArgumentException("targetGroupId was null or blank");
			}
			this.targetGroupId = targetGroupId;
		}
		
		@Override
		public AdHocToGroup_v2_1_2 build() {
			return new AdHocToGroup_v2_1_2(this);
		}

	}
	
	/**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "adHocToGroup";
        final static String TYPE_NAME = "AdHocToGroupType";
    }
    
    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String TARGET_GROUP_ID = "targetGroupId";
    }

	public static AdHocToGroup to(AdHocToGroup_v2_1_2 adHocToGroup) {
		return AdHocToGroup.Builder.create(adHocToGroup.getActionRequested(), adHocToGroup.getNodeName(), adHocToGroup.getTargetGroupId()).build();
	}
}
