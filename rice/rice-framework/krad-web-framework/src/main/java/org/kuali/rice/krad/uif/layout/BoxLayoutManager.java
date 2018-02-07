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
package org.kuali.rice.krad.uif.layout;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.uif.CssConstants;
import org.kuali.rice.krad.uif.CssConstants.Padding;
import org.kuali.rice.krad.uif.UifConstants.Orientation;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Layout manager that organizes components in a single row (horizontal) or
 * column (vertical)
 *
 * <p>
 * Although a table based template could be used, setup is done to also support
 * a CSS based template. The items in the <code>Container</code> instance are
 * rendered sequentially wrapping each one with a span element. The padding
 * property can be configured to space the elements as needed. To achieve a
 * vertical orientation, the span style is set to block. Additional styling can
 * be set for the items by using the itemSpanStyle property.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "boxLayout-bean", parent = "Uif-BoxLayoutBase"),
        @BeanTag(name = "horizontalBoxLayout-bean", parent = "Uif-HorizontalBoxLayout"),
        @BeanTag(name = "verticalBoxLayout-bean", parent = "Uif-VerticalBoxLayout")})
public class BoxLayoutManager extends LayoutManagerBase {
    private static final long serialVersionUID = 4467342272983290044L;

    private Orientation orientation;
    private String padding;

    private String itemStyle;
    private List<String> itemStyleClasses;

    public BoxLayoutManager() {
        super();

        itemStyle = "";
        orientation = Orientation.HORIZONTAL;
        itemStyleClasses = new ArrayList<String>();
    }

    /**
     * Sets the item span style
     *
     * @see org.kuali.rice.krad.uif.layout.LayoutManagerBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.container.Container)
     */
    @Override
    public void performFinalize(View view, Object model, Container container) {
        super.performFinalize(view, model, container);

        if (StringUtils.isBlank(itemStyle)) {
            itemStyle = "";
        }

        if (StringUtils.isNotEmpty(padding)) {
            if (orientation.equals(Orientation.VERTICAL)) {
                // set item to block which will cause a line break and margin
                // bottom for padding
                itemStyle += CssConstants.getCssStyle(Padding.PADDING_BOTTOM, padding);
            } else {
                // set margin right for padding
                itemStyle += CssConstants.getCssStyle(Padding.PADDING_RIGHT, padding);
            }
        }

        // classes to identify this layout in jQuery and to clear the float correctly in all browsers
        this.addStyleClass("clearfix");

        for (Component c : container.getItems()) {
            if (c != null) {
                // add item styles to the the item
                List<String> styleClasses = c.getCssClasses();
                if (styleClasses == null) {
                    styleClasses = new ArrayList<String>();
                }

                if (orientation.equals(Orientation.HORIZONTAL)) {
                    styleClasses.add("uif-boxLayoutHorizontalItem");
                    styleClasses.addAll(this.getItemStyleClasses());
                } else {
                    styleClasses.add("uif-boxLayoutVerticalItem");
                    styleClasses.addAll(this.getItemStyleClasses());
                    styleClasses.add("clearfix");
                }
                c.setCssClasses(styleClasses);

                if (c.getStyle() != null && !c.getStyle().endsWith(";")) {
                    c.appendToStyle(";" + this.getItemStyle());
                } else {
                    c.appendToStyle(this.getItemStyle());
                }
            }
        }
    }

    /**
     * Indicates whether the components should be rendered in a horizontal or
     * vertical column
     *
     * @return orientation configured for layout
     */
    @BeanTagAttribute(name = "orientation", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Orientation getOrientation() {
        return this.orientation;
    }

    /**
     * Setter for the orientation for layout
     *
     * @param orientation
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Amount of separation between each item
     *
     * <p>
     * For horizontal orientation, this will be the right padding for each item.
     * For vertical, it will be the bottom padding for each item. The value can
     * be a fixed length (like px) or percentage
     * </p>
     *
     * @return String
     */
    @BeanTagAttribute(name = "padding")
    public String getPadding() {
        return this.padding;
    }

    /**
     * Setter for the item padding
     *
     * @param padding
     */
    public void setPadding(String padding) {
        this.padding = padding;
    }

    /**
     * Used by the render to set the style on the span element that wraps the
     * item. By using a wrapping span the items can be aligned based on the
     * orientation and given the correct padding
     *
     * @return css style string
     */
    @BeanTagAttribute(name = "itemStyle")
    public String getItemStyle() {
        return this.itemStyle;
    }

    /**
     * Setter for the span style
     *
     * @param itemStyle
     */
    public void setItemStyle(String itemStyle) {
        this.itemStyle = itemStyle;
    }

    /**
     * List of style classes that should be applied to each span that wraps the item in the layout
     *
     * @return List<String>
     */
    @BeanTagAttribute(name = "itemStyleClasses", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getItemStyleClasses() {
        return itemStyleClasses;
    }

    /**
     * Setter for the list of style classes that should apply to each item span
     *
     * @param itemStyleClasses
     */
    public void setItemStyleClasses(List<String> itemStyleClasses) {
        this.itemStyleClasses = itemStyleClasses;
    }

    /**
     * Builds the HTML class attribute string by combining the item styleClasses list
     * with a space delimiter
     *
     * @return class attribute string
     */
    public String getItemStyleClassesAsString() {
        if (itemStyleClasses != null) {
            return StringUtils.join(itemStyleClasses, " ");
        }

        return "";
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManagerBase#copy()
     */
    @Override
    protected <T> void copyProperties(T layout) {
        super.copyProperties(layout);
        BoxLayoutManager boxLayoutManagerCopy = (BoxLayoutManager) layout;
        boxLayoutManagerCopy.setPadding(this.padding);
        boxLayoutManagerCopy.setItemStyle(this.itemStyle);
        boxLayoutManagerCopy.setOrientation(this.orientation);

        if (itemStyleClasses != null) {
            boxLayoutManagerCopy.setItemStyleClasses(new ArrayList<String>(itemStyleClasses));
        }
    }
}
