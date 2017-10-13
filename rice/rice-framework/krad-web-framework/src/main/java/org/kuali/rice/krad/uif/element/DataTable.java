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
package org.kuali.rice.krad.uif.element;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.widget.RichTable;

import java.util.List;
import java.util.Set;

/**
 * Content element that renders a table using the {@link RichTable} widget configured with an Ajax (or Javascript)
 * data source
 *
 * <p>
 * Note this is different from the table layout manager in that it does not render nested components. The data is
 * provided directly to the rich table widget which will create the table rows (unlike the table layout which creates
 * the table from components then invokes the table plugin to decorate). Therefore this component just creates a table
 * element tag and invokes the rich table script
 * </p>
 *
 * <p>
 * Nested HTML can be given through the rich table data. However generally this will be read-only data with possibly
 * some inquiry links
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "dataTable-bean", parent = "Uif-DataTable")
public class DataTable extends ContentElementBase {
    private static final long serialVersionUID = 6201998559169962349L;

    private RichTable richTable;

    public DataTable() {
        super();
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(richTable);

        return components;
    }

    /**
     * Widget that will render the data table client side
     *
     * @return RichTable instance
     */
    @BeanTagAttribute(name = "richTable", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public RichTable getRichTable() {
        return richTable;
    }

    /**
     * Setter for the rich table widget
     *
     * @param richTable
     */
    public void setRichTable(RichTable richTable) {
        this.richTable = richTable;
    }

    /**
     * @see org.kuali.rice.krad.uif.widget.RichTable#getAjaxSource()
     */
    @BeanTagAttribute(name = "ajaxSource")
    public String getAjaxSource() {
        if (richTable != null) {
            return richTable.getAjaxSource();
        }

        return null;
    }

    /**
     * @see org.kuali.rice.krad.uif.widget.RichTable#setAjaxSource(java.lang.String)
     */
    public void setAjaxSource(String ajaxSource) {
        if (richTable != null) {
            richTable.setAjaxSource(ajaxSource);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.widget.RichTable#getHiddenColumns()
     */
    @BeanTagAttribute(name = "hiddenColumns", type = BeanTagAttribute.AttributeType.SETVALUE)
    public Set<String> getHiddenColumns() {
        if (richTable != null) {
            return richTable.getHiddenColumns();
        }

        return null;
    }

    /**
     * @see org.kuali.rice.krad.uif.widget.RichTable#setHiddenColumns(java.util.Set<java.lang.String>)
     */
    public void setHiddenColumns(Set<String> hiddenColumns) {
        if (richTable != null) {
            richTable.setHiddenColumns(hiddenColumns);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.widget.RichTable#getSortableColumns()
     */
    @BeanTagAttribute(name = "sortableColumns", type = BeanTagAttribute.AttributeType.SETVALUE)
    public Set<String> getSortableColumns() {
        if (richTable != null) {
            return richTable.getSortableColumns();
        }

        return null;
    }

    /**
     * @see org.kuali.rice.krad.uif.widget.RichTable#setSortableColumns(java.util.Set<java.lang.String>)
     */
    public void setSortableColumns(Set<String> sortableColumns) {
        if (richTable != null) {
            richTable.setSortableColumns(sortableColumns);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        DataTable dataTableCopy = (DataTable) component;

        if (this.richTable != null) {
            dataTableCopy.setRichTable((RichTable) this.richTable.copy());
        }
    }
}
