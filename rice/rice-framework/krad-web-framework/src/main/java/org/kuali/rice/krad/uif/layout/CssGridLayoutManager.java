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
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Css Grid Layout manager is a layout manager which creates div "rows" and "cells" to replicate a table look by
 * using div elements for its items.  Items are added into rows based on their colSpan setting, while each row has a
 * max
 * size of 9 columns.  By default, if colSpan is not set on an item, that item will take a full row.
 */
@BeanTags({@BeanTag(name = "cssGridLayout-bean", parent = "Uif-CssGridLayoutBase"),
        @BeanTag(name = "fixedCssGridLayout-bean", parent = "Uif-FixedCssGridLayout"),
        @BeanTag(name = "fluidCssGridLayout-bean", parent = "Uif-FluidCssGridLayout")})
public class CssGridLayoutManager extends LayoutManagerBase {
    private static final long serialVersionUID = 1830635073147703757L;

    private static final int NUMBER_OF_COLUMNS = 9;
    private static final String BOOTSTRAP_SPAN_PREFIX = "span";

    private Map<String, String> conditionalRowCssClasses;
    private String rowLayoutCssClass;
    private int defaultItemColSpan;

    // non-settable
    protected List<List<Component>> rows;
    protected List<String> rowCssClassAttributes;
    protected List<String> cellCssClassAttributes;

    public CssGridLayoutManager() {
        rows = new ArrayList<List<Component>>();
        conditionalRowCssClasses = new HashMap<String, String>();
        cellCssClassAttributes = new ArrayList<String>();
        rowCssClassAttributes = new ArrayList<String>();
    }

    /**
     * CssGridLayoutManager's performFinalize method calculates and separates the items into rows
     * based on their colSpan settings and the defaultItemColSpan setting
     * 
     * @see Component#performFinalize(org.kuali.rice.krad.uif.view.View, Object,
     *      org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Container container) {
        super.performFinalize(view, model, container);

        int rowSpaceLeft = NUMBER_OF_COLUMNS;
        int rowIndex = 0;
        boolean isOdd = true;
        List<Component> currentRow = new ArrayList<Component>();
        for (Component item : container.getItems()) {
            if (item == null) {
                continue;
            }
            isOdd = rowIndex % 2 == 0;

            // set colSpan to default setting (9 is the default)
            int colSpan = this.defaultItemColSpan;

            // if the item's set colSpan is greater than 1 set it to that number; 1 is the default colSpan for Component
            if (item.getColSpan() > 1 && item.getColSpan() <= NUMBER_OF_COLUMNS) {
                colSpan = item.getColSpan();
            }

            // determine "cell" div css
            List<String> cellCssClasses = item.getCellCssClasses();
            if (cellCssClasses == null) {
                item.setCellCssClasses(cellCssClasses = new ArrayList<String>());
            }
            cellCssClasses.add(0, BOOTSTRAP_SPAN_PREFIX + colSpan);
            cellCssClassAttributes.add(getCellStyleClassesAsString(cellCssClasses));

            // calculate space left in row
            rowSpaceLeft = rowSpaceLeft - colSpan;

            if (rowSpaceLeft > 0) {
                // space is left, just add item to row
                currentRow.add(item);
            } else if (rowSpaceLeft < 0) {
                // went over, add item to next new row
                rows.add(new ArrayList<Component>(currentRow));
                currentRow = new ArrayList<Component>();
                currentRow.add(item);

                // determine "row" div css
                String rowCss = rowLayoutCssClass + " " + KRADUtils.generateRowCssClassString(conditionalRowCssClasses,
                        rowIndex, isOdd, null, null);
                rowCssClassAttributes.add(rowCss);

                rowIndex++;
                rowSpaceLeft = NUMBER_OF_COLUMNS - colSpan;
            } else if (rowSpaceLeft == 0) {
                // last item in row, create new row
                currentRow.add(item);
                rows.add(new ArrayList<Component>(currentRow));
                currentRow = new ArrayList<Component>();

                // determine "row" div css
                String rowCss = rowLayoutCssClass + " " + KRADUtils.generateRowCssClassString(conditionalRowCssClasses,
                        rowIndex, isOdd, null, null);
                rowCssClassAttributes.add(rowCss);

                rowIndex++;
                rowSpaceLeft = NUMBER_OF_COLUMNS;
            }
        }

        isOdd = rowIndex % 2 == 0;
        // add the last row if it wasn't full (but has items)
        if (!currentRow.isEmpty()) {
            // determine "row" div css
            String rowCss = rowLayoutCssClass + " " + KRADUtils.generateRowCssClassString(conditionalRowCssClasses,
                    rowIndex, isOdd, null, null);
            rowCssClassAttributes.add(rowCss);

            rows.add(currentRow);
        }
    }

    /**
     * Builds the HTML class attribute string by combining the cellStyleClasses list with a space
     * delimiter
     * 
     * @return class attribute string
     */
    private String getCellStyleClassesAsString(List<String> cellCssClasses) {
        if (cellCssClasses != null) {
            return StringUtils.join(cellCssClasses, " ");
        }

        return "";
    }

    /**
     * Get the rows (which are a list of components each)
     * 
     * @return the List of Lists of Components which represents rows for this layout
     */
    public List<List<Component>> getRows() {
        return rows;
    }

    /**
     * List of css class HTML attribute values ordered by index of row
     * 
     * @return the list of css class HTML attributes for rows
     */
    public List<String> getRowCssClassAttributes() {
        return rowCssClassAttributes;
    }

    /**
     * List of css class HTML attribute values ordered by the order in which the cell appears
     * 
     * @return the list of css class HTML attributes for cells
     */
    public List<String> getCellCssClassAttributes() {
        return cellCssClassAttributes;
    }

    /**
     * The default cell colSpan to use for this layout (max setting, and the bean default, is 9)
     * 
     * @return int representing the default colSpan for cells in this layout
     */
    @BeanTagAttribute(name = "defaultItemColSpan")
    public int getDefaultItemColSpan() {
        return defaultItemColSpan;
    }

    /**
     * Set the default colSpan for this layout's items
     * 
     * @param defaultItemColSpan
     */
    public void setDefaultItemColSpan(int defaultItemColSpan) {
        this.defaultItemColSpan = defaultItemColSpan;
    }

    /**
     * The row css classes for the rows of this layout
     * 
     * <p>
     * To set a css class on all rows, use "all" as a key. To set a class for even rows, use "even"
     * as a key, for odd rows, use "odd". Use a one-based index to target a specific row by index.
     * </p>
     * 
     * @return a map which represents the css classes of the rows of this layout
     */
    @BeanTagAttribute(name = "conditionalRowCssClasses", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getConditionalRowCssClasses() {
        return conditionalRowCssClasses;
    }

    /**
     * Set conditionalRowCssClasses
     * 
     * @param conditionalRowCssClasses
     */
    public void setConditionalRowCssClasses(Map<String, String> conditionalRowCssClasses) {
        this.conditionalRowCssClasses = conditionalRowCssClasses;
    }

    /**
     * The layout css class used by the framework to represent the row as a row visually (currently
     * using a bootstrap class), which should not be manually reset in most situations
     * 
     * @return the css structure class for the rows of this layout
     */
    @BeanTagAttribute(name = "rowLayoutCssClass")
    public String getRowLayoutCssClass() {
        return rowLayoutCssClass;
    }

    /**
     * Set the rowLayoutCssClass
     * 
     * @param rowLayoutCssClass
     */
    public void setRowLayoutCssClass(String rowLayoutCssClass) {
        this.rowLayoutCssClass = rowLayoutCssClass;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        CssGridLayoutManager cssGridLayoutManagerCopy = (CssGridLayoutManager) component;

        if (this.rowLayoutCssClass != null) {
            cssGridLayoutManagerCopy.setRowLayoutCssClass(this.rowLayoutCssClass);
        }

        cssGridLayoutManagerCopy.setDefaultItemColSpan(this.defaultItemColSpan);

        if (this.conditionalRowCssClasses != null) {
            cssGridLayoutManagerCopy.setConditionalRowCssClasses(new HashMap<String, String>(
                    this.conditionalRowCssClasses));
        }

        if (this.cellCssClassAttributes != null) {
            cssGridLayoutManagerCopy.cellCssClassAttributes = new ArrayList<String>(this.cellCssClassAttributes);
        }

        if (this.rowCssClassAttributes != null) {
            cssGridLayoutManagerCopy.rowCssClassAttributes = new ArrayList<String>(this.rowCssClassAttributes);
        }

        if (this.rows != null) {
            cssGridLayoutManagerCopy.rows = new ArrayList<List<Component>>();
            for (List<Component> row : this.rows) {
                List<Component> rowCopy = new ArrayList<Component>();

                if (row == null) {
                    cssGridLayoutManagerCopy.rows.add(row);
                    continue;
                }

                for (Component cellComp : row) {
                    if (cellComp == null) {
                        rowCopy.add(cellComp);
                        continue;
                    }
                    rowCopy.add((Component) cellComp.copy());
                }

                cssGridLayoutManagerCopy.rows.add(rowCopy);
            }
        }

    }
}
