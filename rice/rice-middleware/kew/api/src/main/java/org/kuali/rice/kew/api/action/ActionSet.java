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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.kuali.rice.core.api.CoreConstants;
import org.kuali.rice.core.api.mo.AbstractDataTransferObject;
import org.kuali.rice.core.api.mo.ModelBuilder;
import org.kuali.rice.kew.api.KewApiConstants;
import org.kuali.rice.kew.api.actionlist.DisplayParameters;
import org.w3c.dom.Element;


/**
 * Specifies a set of Action codes.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@XmlRootElement(name = ActionSet.Constants.ROOT_ELEMENT_NAME)
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = ActionSet.Constants.TYPE_NAME, propOrder = {
    ActionSet.Elements.ACTION_SET_LIST,
    CoreConstants.CommonElements.FUTURE_ELEMENTS
})
public final class ActionSet extends AbstractDataTransferObject implements ActionSetContract {

	private static final long serialVersionUID = 7857749268529671300L;
	
	@XmlElement(name = Elements.ACTION_SET_LIST, required = false)
	private List<String> actionSetList;
	
	@SuppressWarnings("unused")
    @XmlAnyElement
    private final Collection<Element> _futureElements = null;
	
	/**
     * Private constructor used only by JAXB.
     * 
     */
	private ActionSet() {
	    this.actionSetList = null;
	}
	
	/**
     * Constructs an ActionSet from the given builder.  This constructor is private and should only
     * ever be invoked from the builder.
     * 
     * @param builder the Builder from which to construct the ActionSet
     */
	private ActionSet(Builder builder) {
	    this.actionSetList = builder.getActionSet();
	}
	
	@Override
	public boolean hasAction(String actionCode) {
		return actionSetList != null && actionSetList.contains(actionCode);
	}

    /**
     * 
     * @deprecated  As of release 2.1.2 addAction should be performed using { @link ActionSet.Builder#addAction }
     * 
     * @param actionCode
     * @return
     */
    @Override
	@Deprecated public boolean addAction(String actionCode) {
		if (!actionSetList.contains(actionCode)) {
			actionSetList.add(actionCode);
			return true;
		}
		return false;
	}

    /**
     *
     * @deprecated  As of release 2.1.2 removeAction should be performed using { @link ActionSet.Builder#removeAction }
     *
     * @param actionCode
     * @return
     */
    @Override
    @Deprecated public boolean removeAction(String actionCode) {
		return actionSetList.remove(actionCode);
	}
	
	// some convienance methods for common actions
	@Override
	public boolean hasApprove() {
		return hasAction(KewApiConstants.ACTION_TAKEN_APPROVED_CD);
	}
	
	@Override
	public boolean hasComplete() {
		return hasAction(KewApiConstants.ACTION_TAKEN_COMPLETED_CD);
	}
	
	@Override
	public boolean hasAcknowledge() {
		return hasAction(KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD);
	}
	
	@Override
	public boolean hasFyi() {
		return hasAction(KewApiConstants.ACTION_TAKEN_FYI_CD);
	}
	
	@Override
	public boolean hasDisapprove() {
		return hasAction(KewApiConstants.ACTION_TAKEN_DENIED_CD);
	}
	
	@Override
	public boolean hasCancel() {
		return hasAction(KewApiConstants.ACTION_TAKEN_CANCELED_CD);
	}

	@Override
    public boolean hasRouted() {
        return hasAction(KewApiConstants.ACTION_TAKEN_ROUTED_CD);
    }

    /**
     *
     * @deprecated  As of release 2.1.2 addApprove should be performed using { @link ActionSet.Builder#addApprove }
     *
     * @return
     */
	@Override
    @Deprecated public boolean addApprove() {
		return addAction(KewApiConstants.ACTION_TAKEN_APPROVED_CD);
	}

    /**
     *
     * @deprecated  As of release 2.1.2 addComplete should be performed using { @link ActionSet.Builder#addComplete }
     *
     * @return
     */
	@Override
    @Deprecated public boolean addComplete() {
		return addAction(KewApiConstants.ACTION_TAKEN_COMPLETED_CD);
	}

    /**
     *
     * @deprecated  As of release 2.1.2 addAcknowledge should be performed using { @link ActionSet.Builder#addAcknowledge }
     *
     * @return
     */
	@Override
    @Deprecated public boolean addAcknowledge() {
		return addAction(KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD);
	}

    /**
     *
     * @deprecated  As of release 2.1.2 addFyi should be performed using { @link ActionSet.Builder#addFyi }
     *
     * @return
     */
	@Override
    @Deprecated public boolean addFyi() {
		return addAction(KewApiConstants.ACTION_TAKEN_FYI_CD);
	}

    /**
     *
     * @deprecated  As of release 2.1.2 addDisapprove should be performed using { @link ActionSet.Builder#addDisapprove }
     *
     * @return
     */
	@Override
    @Deprecated public boolean addDisapprove() {
		return addAction(KewApiConstants.ACTION_TAKEN_DENIED_CD);
	}

    /**
     *
     * @deprecated  As of release 2.1.2 addCancel should be performed using { @link ActionSet.Builder#addCancel }
     *
     * @return
     */
	@Override
    @Deprecated public boolean addCancel() {
		return addAction(KewApiConstants.ACTION_TAKEN_CANCELED_CD);
	}

    /**
     *
     * @deprecated  As of release 2.1.2 addRouted should be performed using { @link ActionSet.Builder#addRouted }
     *
     * @return
     */
	@Override
    @Deprecated public boolean addRouted() {
        return addAction(KewApiConstants.ACTION_TAKEN_ROUTED_CD);
    }
    
	/**
     * A builder which can be used to construct {@link ActionSet} instances.  Enforces the constraints of the {@link ActionSetContract}.
     */
	public final static class Builder implements Serializable, ModelBuilder, ActionSetContract {
	    
	    private List<String> actionSet;
	    
	    /**
         * Private constructor for creating a builder with all of it's required attributes.
         */
	    private Builder(List<String> actionSet) {
	        setActionSetList(actionSet);
	    }
	    
	    public static Builder create() {
	        return new Builder(new ArrayList<String>());
	    }
	    
	    /**
         * Creates a builder by populating it with data from the given {@linkActionSet}.
         * 
         * @param contract the contract from which to populate this builder
         * @return an instance of the builder populated with data from the contract
         */
	    public static Builder create(ActionSetContract contract) {
	        if (contract == null) {
                throw new IllegalArgumentException("contract was null");
            }
            Builder builder = create();
            return builder;
	    }
	   
	    public ActionSet build() {
	        return new ActionSet(this);
	    }
	    public List<String> getActionSet() {
	        return this.actionSet;
	    }
	    public void setActionSetList(List<String> actionSet) {
	        this.actionSet = actionSet;
	    }

        @Override
        public boolean hasAction(String actionCode) {
            return actionSet.contains(actionCode);
        }

        @Override
        public boolean addAction(String actionCode) {
            if (!actionSet.contains(actionCode)) {
                actionSet.add(actionCode);
                return true;
            }
            return false;
        }

        @Override
        public boolean removeAction(String actionCode) {
            return actionSet.remove(actionCode);
        }

        @Override
        public boolean hasApprove() {
            return hasAction(KewApiConstants.ACTION_TAKEN_APPROVED_CD);
        }

        @Override
        public boolean hasComplete() {
            return hasAction(KewApiConstants.ACTION_TAKEN_COMPLETED_CD);
        }

        @Override
        public boolean hasAcknowledge() {
            return hasAction(KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD);
        }

        @Override
        public boolean hasFyi() {
            return hasAction(KewApiConstants.ACTION_TAKEN_FYI_CD);
        }

        @Override
        public boolean hasDisapprove() {
            return hasAction(KewApiConstants.ACTION_TAKEN_DENIED_CD);
        }

        @Override
        public boolean hasCancel() {
            return hasAction(KewApiConstants.ACTION_TAKEN_CANCELED_CD);
        }

        @Override
        public boolean hasRouted() {
            return hasAction(KewApiConstants.ACTION_TAKEN_ROUTED_CD);
        }

        @Override
        public boolean addApprove() {
            return addAction(KewApiConstants.ACTION_TAKEN_APPROVED_CD);
        }

        @Override
        public boolean addComplete() {
            return addAction(KewApiConstants.ACTION_TAKEN_COMPLETED_CD);
        }

        /**
         * This overridden method ...
         * 
         * @see org.kuali.rice.kew.api.action.ActionSetContract#addAcknowledge()
         */
        @Override
        public boolean addAcknowledge() {
            return addAction(KewApiConstants.ACTION_TAKEN_ACKNOWLEDGED_CD);
        }

        @Override
        public boolean addFyi() {
            return addAction(KewApiConstants.ACTION_TAKEN_FYI_CD);
        }

        @Override
        public boolean addDisapprove() {
            return addAction(KewApiConstants.ACTION_TAKEN_DENIED_CD);
        }

        @Override
        public boolean addCancel() {
            return addAction(KewApiConstants.ACTION_TAKEN_CANCELED_CD);
        }

        @Override
        public boolean addRouted() {
            return addAction(KewApiConstants.ACTION_TAKEN_ROUTED_CD);
        }	        
	}
	
    /**
     * Defines some internal constants used on this class.
     * 
     */
    static class Constants {

        final static String ROOT_ELEMENT_NAME = "actionSet";
        final static String TYPE_NAME = "ActionSetType";
    }

    /**
     * A private class which exposes constants which define the XML element names to use when this object is marshalled to XML.
     * 
     */
    static class Elements {

        final static String ACTION_SET_LIST = "actionSetList";
    }
}
