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
package org.kuali.rice.krad.uif.modifier;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;
import org.kuali.rice.krad.uif.component.Ordered;

import java.io.Serializable;


/**
 * Provides configuration for comparing an object with another object
 *
 * <p>
 * Used with a comparison view (such as in maintenance documents edit mode)
 * where two objects with the same properties are compared. This class
 * configures the object paths for the objects that will be compared, and has
 * additional configuration for the generated comparison group
 * </p>
 *
 * <p>
 * All comparison objects must have the same fields and collection rows
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @see org.kuali.rice.krad.uif.modifier.CompareFieldCreateModifier
 */
@BeanTag(name = "compareConfig-bean", parent = "Uif-CompareConfig")
public class ComparableInfo extends UifDictionaryBeanBase implements Serializable, Ordered, Cloneable  {
	private static final long serialVersionUID = -5926058412202550266L;

	private String bindingObjectPath;
	private String headerText;
	private boolean readOnly;

	private int order;
	private String idSuffix;

	private boolean compareToForValueChange;
	private boolean highlightValueChange;

	public ComparableInfo() {
        super();

		readOnly = false;
		compareToForValueChange = false;
		highlightValueChange = true;
	}

	/**
	 * Returns the path (from the form) for the object to compare to
	 *
	 * <p>
	 * When a comparison view is rendered, a group will be rendered for each
	 * comparison object using the fields defined on the view. This gives the
	 * path to one of the comparison objects
	 * </p>
	 *
	 * <p>
	 * e.g. For maintenance documents the compare object paths would be
	 * document.newMaintainableObject.businessObject and
	 * document.oldMaintainableObject.businessObject
	 * </p>
	 *
	 * @return path to the compare object
	 */
    @BeanTagAttribute(name="bindingObjectPath")
	public String getBindingObjectPath() {
		return this.bindingObjectPath;
	}

	/**
	 * Setter for the path to the compare object
	 *
	 * @param bindingObjectPath
	 */
	public void setBindingObjectPath(String bindingObjectPath) {
		this.bindingObjectPath = bindingObjectPath;
	}

	/**
	 * Text that should display on the header for the compare group
	 *
	 * <p>
	 * In the comparison view each compare group can be labeled, this gives the
	 * text that should be used for that label. For example in the maintenance
	 * view the compare record is labeled 'Old' to indicate it is the old
	 * version of the record
	 * </p>
	 *
	 * @return header text
	 */
    @BeanTagAttribute(name="headerText")
	public String getHeaderText() {
		return this.headerText;
	}

	/**
	 * Setter for the compare group header text
	 *
	 * @param headerText
	 */
	public void setHeaderText(String headerText) {
		this.headerText = headerText;
	}

	/**
	 * Indicates whether the compare group should be read-only
	 *
	 * @return true if the group should be read-only, false if edits are
	 *         allowed
	 */
    @BeanTagAttribute(name="readOnly")
	public boolean isReadOnly() {
		return this.readOnly;
	}

	/**
	 * Setter for the read-only indicator
	 *
	 * @param readOnly
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * Sets the order value that will be used to determine where the compare
	 * group should be placed in relation to the other compare groups
	 *
	 * <p>
	 * For example if the compare groups are being rendered from left to right
	 * in columns, a lower order value would be placed to the left of a compare
	 * group with a higher order value
	 * </p>
	 *
	 * @see org.springframework.core.Ordered#getOrder()
	 */
    @BeanTagAttribute(name="order")
	public int getOrder() {
		return this.order;
	}

	/**
	 * Setter for the compare object order
	 *
	 * @param order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * Specifies an id suffix to use for the generated comparison fields
	 *
	 * <p>
	 * For the given string, all components created for the comparison group
	 * will contain the string on their id. This can be helpful for scripting.
	 * If not given, the items will receive a default id suffix
	 * </p>
	 *
	 * @return id suffix for comparison group
	 */
    @BeanTagAttribute(name="idSuffix")
	public String getIdSuffix() {
		return this.idSuffix;
	}

	/**
	 * Setter for the id prefix to use for the generated comparison components
	 *
	 * @param idSuffix
	 */
	public void setIdSuffix(String idSuffix) {
		this.idSuffix = idSuffix;
	}

    /**
     * Indicates whether this comparable group's field values should be compared
     * to when highlighting changes of values between comparables (versions)
     *
     * @return true if this comparable group should be used for
     *         comparison, false if not
     * @see #isHighlightValueChange
     */
    @BeanTagAttribute(name="compareToForValueChange")
    public boolean isCompareToForValueChange() {
        return this.compareToForValueChange;
    }

    /**
     * Setter for the use comparable group values for comparison indicator
     *
     * @param compareToForValueChange
     */
    public void setCompareToForValueChange(boolean compareToForValueChange) {
        this.compareToForValueChange = compareToForValueChange;
    }

    /**
     * Indicates whether the fields in this comparable group should be
     * highlighted if their values defer from the comparable group marked for
     * comparison
     *
     * @return true if the comparable fields should be highlighted,
     *         false if they should not be highlighted (no comparison will be
     *         performed)
     * @see #isCompareToForValueChange
     */
    @BeanTagAttribute(name="highlightValueChange")
    public boolean isHighlightValueChange() {
        return this.highlightValueChange;
    }

    /**
     * Setter for the highlight comparable field value changed indicator
     *
     * @param highlightValueChange
     */
    public void setHighlightValueChange(boolean highlightValueChange) {
        this.highlightValueChange = highlightValueChange;
    }

    /**
     * Returns a clone of the comparable info.
     *
     * @return ComparableInfo clone of the component
     */
    public <T extends ComparableInfo> T clone() {
        try {
            T clonedClass = (T)this.getClass().newInstance();
            clonedClass = (T)copyProperties(clonedClass);

            return clonedClass;
        }
        catch(Exception exception) {
            throw new RuntimeException();
        }
    }

    protected ComparableInfo copyProperties(Cloneable comparableInfo) {
        ComparableInfo comparableInfoCopy = (ComparableInfo) comparableInfo;
        comparableInfoCopy.setBindingObjectPath(this.bindingObjectPath);
        comparableInfoCopy.setCompareToForValueChange(this.compareToForValueChange);
        comparableInfoCopy.setHeaderText(this.headerText);
        comparableInfoCopy.setHighlightValueChange(this.highlightValueChange);
        comparableInfoCopy.setIdSuffix(this.idSuffix);
        comparableInfoCopy.setOrder(this.order);
        comparableInfoCopy.setReadOnly(this.readOnly);

        return comparableInfoCopy;
    }

    protected <T> void copyProperties(T comparableInfo) {
        super.copyProperties(comparableInfo);
        ComparableInfo comparableInfoCopy = (ComparableInfo) comparableInfo;
        comparableInfoCopy.setBindingObjectPath(this.bindingObjectPath);
        comparableInfoCopy.setCompareToForValueChange(this.compareToForValueChange);
        comparableInfoCopy.setHeaderText(this.headerText);
        comparableInfoCopy.setHighlightValueChange(this.highlightValueChange);
        comparableInfoCopy.setIdSuffix(this.idSuffix);
        comparableInfoCopy.setOrder(this.order);
        comparableInfoCopy.setReadOnly(this.readOnly);
    }
}
