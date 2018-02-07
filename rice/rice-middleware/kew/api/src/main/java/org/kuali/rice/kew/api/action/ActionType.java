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

import org.kuali.rice.core.api.mo.common.Coded;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import java.util.HashMap;
import java.util.Map;

/**
 * An enumeration which indicates valid actions that can be taken against workflow documents.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@XmlRootElement(name = "actionType")
@XmlType(name = "ActionTypeType")
@XmlEnum
public enum ActionType implements Coded {

    @XmlEnumValue("k") SU_ACKNOWLEDGE("k", "SUPER USER ACKNOWLEDGE"),
    
    @XmlEnumValue("f") SU_FYI("f", "SUPER USER FYI"),
    
    @XmlEnumValue("m") SU_COMPLETE("m", "SUPER USER COMPLETE"),
    
    @XmlEnumValue("v") SU_APPROVE("v", "SUPER USER APPROVE"),
    
    @XmlEnumValue("r") SU_ROUTE_NODE_APPROVE("r", "SUPER USER ROUTE NODE APPROVE"),
    
    @XmlEnumValue("z") SU_RETURN_TO_PREVIOUS("z", "SUPER USER RETURN TO PREVIOUS"),
    
    @XmlEnumValue("d") SU_DISAPPROVE("d", "SUPER USER DISAPPROVE"),
    
    @XmlEnumValue("c") SU_CANCEL("c", "SUPER USER CANCEL"),
    
    @XmlEnumValue("a") SU_BLANKET_APPROVE("a", "SUPER USER BLANKET APPROVE"),
    
    @XmlEnumValue("B") BLANKET_APPROVE("B", "BLANKET APPROVE"),
    
    @XmlEnumValue("F") FYI("F", "FYI"),
    
    /**
     * User has generated an action request to another user
     */
    @XmlEnumValue("H") ADHOC_REQUEST("H", "ADHOC REQUEST"),
    
    /**
     * AdHoc Request has been revoked
     */
    @XmlEnumValue("V") ADHOC_REQUEST_REVOKE("V", "ADHOC REQUEST_REVOKE"),
    
    /**
     * Document has been saved by the user for later work
     */
    @XmlEnumValue("S") SAVE("S", "SAVED"),
    
    /**
     * Document has been canceled.
     */
    @XmlEnumValue("X") CANCEL("X", "CANCEL"),
    
    /**
     * Document has been disapproved.
     */
    @XmlEnumValue("D") DISAPPROVE("D", "DISAPPROVE"),
    
    /**
     * Document has been opened by the designated recipient.
     */
    @XmlEnumValue("K") ACKNOWLEDGE("K", "ACKNOWLEDGE"),
    
    /**
     * Document has been completed as requested.
     */
    @XmlEnumValue("C") COMPLETE("C", "COMPLETE"),
    
    /**
     * Document has been submitted to the engine for processing.
     */
    @XmlEnumValue("O") ROUTE("O", "ROUTE"),
    
    /**
     * The document has been approved.
     */
    @XmlEnumValue("A") APPROVE("A", "APPROVE"),
    
    /**
     * The document is being returned to a previous routelevel
     */
    @XmlEnumValue("Z") RETURN_TO_PREVIOUS("Z", "RETURN TO PREVIOUS"),

    /**
     * The document is being recalled.
     * @since 2.1
     */
    @XmlEnumValue("L") RECALL("L", "RECALL"),

    /**
     * The document has non-routed activity against it that is recorded in the route log
     */
    @XmlEnumValue("R") LOG_MESSAGE("R", "LOG MESSAGE"),
    
    /**
     * The document is routed to a group and a user in the group wants to take authority from the group
     */
    @XmlEnumValue("w") TAKE_GROUP_AUTHORITY("w", "TAKE GROUP AUTHORITY"),
    		
    /**
     * The person who took group authority is releasing it
     */
    @XmlEnumValue("y") RELEASE_GROUP_AUTHORITY("y", "RELEASE GROUP AUTHORITY"),
    
    /**
     * The document is moved
     */
    @XmlEnumValue("M") MOVE("M", "MOVED");

    /**
     * Map of ActionTypes to their SuperUser equivalent
     */
    private static final Map<ActionType, ActionType> SU_ACTION_TYPE = new HashMap<ActionType, ActionType>();
    static {
        SU_ACTION_TYPE.put(APPROVE, SU_APPROVE);
        SU_ACTION_TYPE.put(COMPLETE, SU_COMPLETE);
        SU_ACTION_TYPE.put(FYI, SU_FYI);
        SU_ACTION_TYPE.put(ACKNOWLEDGE, SU_ACKNOWLEDGE);
    }

    private final String code;
	private final String label;
	
	private ActionType(String code, String label) {
		this.code = code;
		this.label = label;
	}
	
	@Override
	public String getCode() {
		return code;
	}
	
	public String getLabel() {
		return label;
	}
	
	public static ActionType fromCode(String code) {
		return fromCode(code, false);
	}
	
	public static ActionType fromCode(String code, boolean allowMissing) {
		if (code == null) {
			return null;
		}
		for (ActionType status : values()) {
			if (status.code.equals(code)) {
				return status;
			}
		}
		if (allowMissing) {
			return null;
		}
		throw new IllegalArgumentException("Failed to locate the ActionType with the given code: " + code);
	}

    public static ActionType fromLabel(String label) {
        if (label == null) {
            return null;
        }
        for (ActionType status : values()) {
            if (status.label.equals(label)) {
                return status;
            }
        }
        return null;
    }

    public static ActionType fromCodeOrLabel(String str) {
        ActionType at = ActionType.fromCode(str, true);
        if (at == null && str != null) {
            at = ActionType.fromLabel(str.toUpperCase());
        }
        return at;
    }

    /**
     * Converts a non-super-user ActionType to its SuperUser equivalent
     * @since 2.1.3
     * @param at the ActionType
     * @return super-user version of ActionType or null if no equivalent SU action type
     */
    public static ActionType toSuperUserActionType(ActionType at) {
        if (SU_ACTION_TYPE.containsKey(at)) return at;
        return SU_ACTION_TYPE.get(at);
    }
}