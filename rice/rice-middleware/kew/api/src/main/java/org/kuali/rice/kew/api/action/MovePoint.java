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

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.w3c.dom.Element;

@XmlRootElement(name = MovePoint.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = MovePoint.Constants.TYPE_NAME, propOrder = {
		MovePoint.Elements.START_NODE_NAME,
		MovePoint.Elements.STEPS_TO_MOVE,
		CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class MovePoint extends AbstractDataTransferObject {
    
	@XmlElement(name = Elements.START_NODE_NAME, required = true)
    private final String startNodeName;
	
	@XmlElement(name = Elements.STEPS_TO_MOVE, required = true)
    private final int stepsToMove;
    
    @SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;

    private MovePoint() {
    	this.startNodeName = null;
    	this.stepsToMove = 0;
    }
    
    private MovePoint(String startNodeName, int stepsToMove) {
    	if (StringUtils.isBlank(startNodeName)) {
    		throw new IllegalArgumentException("startNodeName was null or blank");
    	}
        this.startNodeName = startNodeName;
        this.stepsToMove = stepsToMove;
    }
    
    public static MovePoint create(String startNodeName, int stepsToMove) {
    	return new MovePoint(startNodeName, stepsToMove);
    }
    
    public String getStartNodeName() {
        return startNodeName;
    }

    public int getStepsToMove() {
        return stepsToMove;
    }
    
    /**
     * Defines some internal constants used on this class.
     */
    static class Constants {
        final static String ROOT_ELEMENT_NAME = "movePoint";
        final static String TYPE_NAME = "MovePointType";
    }
    
    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     */
    static class Elements {
        final static String START_NODE_NAME = "startNodeName";
        final static String STEPS_TO_MOVE = "stepsToMove";
    }

}
