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
package org.kuali.rice.krad.uif.field;

import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Label;

import java.util.List;

/**
 * Component that contains one or more user interface elements and can be placed
 * into a <code>Container</code>
 *
 * <p>
 * Provides a wrapper for various user interface elements so they can be treated
 * uniformly by a container and rendered using a <code>LayoutManager</code>.
 * Implementations exist for various types of elements and properties to
 * configure that element.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface Field extends Component {

	/**
	 * Label text for the field
	 *
	 * <p>
	 * The label is generally used to identify the field in the user interface
	 * </p>
	 *
	 * @return String label text
	 */
	public String getLabel();

	/**
	 * Setter for the field's label text
	 *
	 * @param labelText
	 */
	public void setLabel(String labelText);

	/**
	 * Short label for the field
	 *
	 * <p>
	 * For areas of the user interface that have limited area (such as table
	 * headers), the short label can be used to identify the field
	 * </p>
	 *
	 * @return String short label
	 */
	public String getShortLabel();

	/**
	 * Setter for the field's short label text
	 *
	 * @param shortLabel
	 */
	public void setShortLabel(String shortLabel);

	/**
	 * <code>Label</code> instance for the field
	 *
	 * <p>
	 * The label field contains the labeling text for the field in addition to
	 * configuration for rendering in the user interface (such as the styling
	 * for the label area)
	 * </p>
	 *
	 * @return Label instance
	 */
	public Label getFieldLabel();

	/**
	 * Setter for the field's label field
	 *
	 * @param label
	 */
	public void setFieldLabel(Label label);

	/**
	 * Indicates whether the contained <code>Label</code> has been rendered
	 * as part of another field and thus should not be rendered with the
	 * attribute
	 *
	 * @return boolean true if the label field has been rendered, false if it
	 *         should be rendered with the attribute
	 */
	public boolean isLabelRendered();

	/**
	 * Setter for the label field rendered indicator
	 *
	 * @param labelFieldRendered
	 */
	public void setLabelRendered(boolean labelFieldRendered);

    /**
     * Label style classes for the field
     *
     * @return List<String> label style classes
     */
    public List<String> getLabelStyleClasses();

    /**
     * Setter for the field's label style classes
     *
     * @param labelStyleClasses
     */
    public void setLabelStyleClasses(List<String> labelStyleClasses);

    /**
     * Label column span for the field
     *
     * @return int label column span
     */
    public int getLabelColSpan();

    /**
     * Setter for the field's label column span
     *
     * @param labelColSpan
     */
    public void setLabelColSpan(int labelColSpan);

    /**
     * Field Security object that indicates what authorization (permissions) exist for the field
     *
     * @return FieldSecurity instance
     */
    public FieldSecurity getFieldSecurity();

}
