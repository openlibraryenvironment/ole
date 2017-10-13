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
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Layout manager that organizes its components in a table based grid
 *
 * <p>
 * Items are laid out from left to right (with each item taking up one column)
 * until the configured number of columns is reached. If the item count is
 * greater than the number of columns, a new row will be created to render the
 * remaining items (and so on until all items are placed). Labels for the fields
 * can be pulled out (default) and rendered as a separate column. The manager
 * also supports the column span and row span options for the field items. If
 * not specified the default is 1.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "gridLayout-bean", parent = "Uif-GridLayoutBase"),
        @BeanTag(name = "twoColumnGridLayout-bean", parent = "Uif-TwoColumnGridLayout"),
        @BeanTag(name = "fourColumnGridLayout-bean", parent = "Uif-FourColumnGridLayout"),
        @BeanTag(name = "sixColumnGridLayout-bean", parent = "Uif-SixColumnGridLayout")})
public class GridLayoutManager extends LayoutManagerBase {
    private static final long serialVersionUID = 1890011900375071128L;

    private int numberOfColumns;

    private boolean suppressLineWrapping;
    private boolean applyAlternatingRowStyles;
    private boolean applyDefaultCellWidths;
    private boolean renderFirstRowHeader;
    private boolean renderAlternatingHeaderColumns;
    private boolean renderRowFirstCellHeader;

    private List<String> rowCssClasses;

    public GridLayoutManager() {
        super();

        rowCssClasses = new ArrayList<String>();
    }

    /**
     * The following finalization is performed:
     *
     * <ul>
     * <li>If suppressLineWrapping is true, sets the number of columns to the
     * container's items list size</li>
     * <li>Adjust the cell attributes for the container items</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.layout.LayoutManagerBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.container.Container)
     */
    @Override
    public void performFinalize(View view, Object model, Container container) {
        super.performFinalize(view, model, container);

        if (suppressLineWrapping) {
            numberOfColumns = container.getItems().size();
        }

        for (Component component : container.getItems()) {
            if (!(this instanceof TableLayoutManager)) {
                component.addCellCssClass("uif-gridLayoutCell");
            }
            setCellAttributes(component);
        }
    }

    /**
     * Moves the width, align, and valign settings of the component to the corresponding cell properties (if not
     * already configured)
     *
     * @param component instance to adjust settings for
     */
    protected void setCellAttributes(Component component) {
        if (StringUtils.isNotBlank(component.getWidth()) && StringUtils.isBlank(component.getCellWidth())) {
            component.setCellWidth(component.getWidth());
            component.setWidth("");
        }

        if (StringUtils.isNotBlank(component.getAlign()) && !StringUtils.contains(component.getCellStyle(),
                CssConstants.TEXT_ALIGN)) {
            if (component.getCellStyle() == null) {
                component.setCellStyle("");
            }

            component.setCellStyle(component.getCellStyle() + CssConstants.TEXT_ALIGN + component.getAlign() + ";");
            component.setAlign("");
        }

        if (StringUtils.isNotBlank(component.getValign()) && !StringUtils.contains(component.getCellStyle(),
                CssConstants.VERTICAL_ALIGN)) {
            if (component.getCellStyle() == null) {
                component.setCellStyle("");
            }

            component.setCellStyle(
                    component.getCellStyle() + CssConstants.VERTICAL_ALIGN + component.getValign() + ";");
            component.setValign("");
        }
    }

    /**
     * @see LayoutManagerBase#getSupportedContainer()
     */
    @Override
    public Class<? extends Container> getSupportedContainer() {
        return Group.class;
    }

    /**
     * Indicates the number of columns that should make up one row of data
     *
     * <p>
     * If the item count is greater than the number of columns, a new row will
     * be created to render the remaining items (and so on until all items are
     * placed).
     * </p>
     *
     * <p>
     * Note this does not include any generated columns by the layout manager,
     * so the final column count could be greater (if label fields are
     * separate).
     * </p>
     *
     * @return int
     */
    @BeanTagAttribute(name = "numberOfColumns")
    public int getNumberOfColumns() {
        return this.numberOfColumns;
    }

    /**
     * Setter for the number of columns (each row)
     *
     * @param numberOfColumns
     */
    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    /**
     * Indicates whether the number of columns for the table data should match
     * the number of fields given in the container's items list (so that each
     * field takes up one column without wrapping), this overrides the configured
     * numberOfColumns
     *
     * <p>
     * If set to true during the initialize phase the number of columns will be
     * set to the size of the container's field list, if false the configured
     * number of columns is used
     * </p>
     *
     * @return true if the column count should match the container's
     *         field count, false to use the configured number of columns
     */
    @BeanTagAttribute(name = "suppressLineWrapping")
    public boolean isSuppressLineWrapping() {
        return this.suppressLineWrapping;
    }

    /**
     * Setter for the suppressLineWrapping indicator
     *
     * @param suppressLineWrapping
     */
    public void setSuppressLineWrapping(boolean suppressLineWrapping) {
        this.suppressLineWrapping = suppressLineWrapping;
    }

    /**
     * Indicates whether alternating row styles should be applied
     *
     * <p>
     * Indicator to layout manager templates to apply alternating row styles.
     * See the configured template for the actual style classes used
     * </p>
     *
     * @return true if alternating styles should be applied, false if
     *         all rows should have the same style
     */
    @BeanTagAttribute(name = "applyAlternatingRowStyles")
    public boolean isApplyAlternatingRowStyles() {
        return this.applyAlternatingRowStyles;
    }

    /**
     * Setter for the alternating row styles indicator
     *
     * @param applyAlternatingRowStyles
     */
    public void setApplyAlternatingRowStyles(boolean applyAlternatingRowStyles) {
        this.applyAlternatingRowStyles = applyAlternatingRowStyles;
    }

    /**
     * Indicates whether the manager should default the cell widths
     *
     * <p>
     * If true, the manager will set the cell width by equally dividing by the
     * number of columns
     * </p>
     *
     * @return true if default cell widths should be applied, false if
     *         no defaults should be applied
     */
    @BeanTagAttribute(name = "applyDefaultCellWidths")
    public boolean isApplyDefaultCellWidths() {
        return this.applyDefaultCellWidths;
    }

    /**
     * Setter for the default cell width indicator
     *
     * @param applyDefaultCellWidths
     */
    public void setApplyDefaultCellWidths(boolean applyDefaultCellWidths) {
        this.applyDefaultCellWidths = applyDefaultCellWidths;
    }

    /**
     * Indicates whether the first cell of each row should be rendered as a header cell (th)
     *
     * <p>
     * When this flag is turned on, the first cell for each row will be rendered as a header cell. If
     * {@link #isRenderAlternatingHeaderColumns()} is false, the remaining cells for the row will be rendered
     * as data cells, else they will alternate between cell headers
     * </p>
     *
     * @return true if first cell of each row should be rendered as a header cell
     */
    @BeanTagAttribute(name = "renderRowFirstCellHeader")
    public boolean isRenderRowFirstCellHeader() {
        return renderRowFirstCellHeader;
    }

    /**
     * Setter for render first row column as header indicator
     *
     * @param renderRowFirstCellHeader
     */
    public void setRenderRowFirstCellHeader(boolean renderRowFirstCellHeader) {
        this.renderRowFirstCellHeader = renderRowFirstCellHeader;
    }

    /**
     * Indicates whether the first row of items rendered should all be rendered as table header (th) cells
     *
     * <p>
     * Generally when using a grid layout all the cells will be tds or alternating th/td (with the label in the
     * th cell). However in some cases it might be desired to display the labels in one row as table header cells (th)
     * followed by a row with the corresponding fields in td cells. When this is enabled this type of layout is
     * possible
     * </p>
     *
     * @return true if first row should be rendered as header cells
     */
    @BeanTagAttribute(name = "renderFirstRowHeader")
    public boolean isRenderFirstRowHeader() {
        return renderFirstRowHeader;
    }

    /**
     * Setter for the first row as header indicator
     *
     * @param renderFirstRowHeader
     */
    public void setRenderFirstRowHeader(boolean renderFirstRowHeader) {
        this.renderFirstRowHeader = renderFirstRowHeader;
    }

    /**
     * Indicates whether header columns (th for tables) should be rendered for
     * every other item (alternating)
     *
     * <p>
     * If true the first cell of each row will be rendered as an header, with
     * every other cell in the row as a header
     * </p>
     *
     * @return true if alternating headers should be rendered, false if not
     */
    @BeanTagAttribute(name = "renderAlternatingHeaderColumns")
    public boolean isRenderAlternatingHeaderColumns() {
        return this.renderAlternatingHeaderColumns;
    }

    /**
     * Setter for the render alternating header columns indicator
     *
     * @param renderAlternatingHeaderColumns
     */
    public void setRenderAlternatingHeaderColumns(boolean renderAlternatingHeaderColumns) {
        this.renderAlternatingHeaderColumns = renderAlternatingHeaderColumns;
    }

    /**
     * The list of styles for each row
     *
     * <p>
     * Each entry in the list gives the style for the row with the same index. This style will be added the the <tr>
     * tag
     * when the table rows are rendered in the grid.tag. This is used to store the styles for newly added lines and
     * other special cases like the add item row.
     * </p>
     *
     * @return list of styles for the rows
     */
    @BeanTagAttribute(name = "rowCssClasses", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getRowCssClasses() {
        return rowCssClasses;
    }

    /**
     * Setter for the list that stores the css style names of each row
     *
     * @param rowCssClasses
     */
    public void setRowCssClasses(List<String> rowCssClasses) {
        this.rowCssClasses = rowCssClasses;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T layoutManager) {
        super.copyProperties(layoutManager);
        GridLayoutManager gridLayoutManagerCopy = (GridLayoutManager) layoutManager;
        gridLayoutManagerCopy.setNumberOfColumns(this.numberOfColumns);
        gridLayoutManagerCopy.setSuppressLineWrapping(this.suppressLineWrapping);
        gridLayoutManagerCopy.setApplyAlternatingRowStyles(this.applyAlternatingRowStyles);
        gridLayoutManagerCopy.setApplyDefaultCellWidths(this.applyDefaultCellWidths);
        gridLayoutManagerCopy.setRenderFirstRowHeader(this.renderFirstRowHeader);
        gridLayoutManagerCopy.setRenderAlternatingHeaderColumns(this.renderAlternatingHeaderColumns);
        gridLayoutManagerCopy.setRenderRowFirstCellHeader(this.renderRowFirstCellHeader);

        if (rowCssClasses != null) {
            gridLayoutManagerCopy.setRowCssClasses(new ArrayList<String>(rowCssClasses));
        }
    }
}
