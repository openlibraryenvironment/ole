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
package org.kuali.rice.krad.uif.widget;


import org.junit.Before;
import org.junit.Test;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.krad.datadictionary.validation.Employee;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.layout.TableLayoutManager;
import org.kuali.rice.krad.uif.service.ViewHelperService;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * test the RichTable widget
 */

public class RichTableTest {

    public static final String S_TYPE = "{\"bSortable\" : false, \"sType\" : \"numeric\", \"aTargets\": [0]}";
    public static final String S_SORT_DATA_TARGETS_1 = "{\"sType\" : \"string\", \"sSortDataType\" : \"dom-text\", \"aTargets\": [1]}";
    public static final String S_SORT_DATA_TARGETS_2 = S_SORT_DATA_TARGETS_1.replace("1", "2");
    public static final String S_SORT_DATA_TARGETS_3 = S_SORT_DATA_TARGETS_1.replace("1", "3");

    public static final String EXPECTED = S_TYPE + ", " +
            S_SORT_DATA_TARGETS_1 + " , " +
            S_SORT_DATA_TARGETS_2 + " , " +
            S_SORT_DATA_TARGETS_3;

    public static final String B_VISIBLE_FALSE_TARGETS_1 = "{bVisible: false, \"aTargets\": [1]}";
    public static final String B_SORTABLE_FALSE_TARGETS_3 = "{'bSortable': false, \"aTargets\": [3]}";

    private RichTable richTable;
    private CollectionGroup group;
    private View mockView;

    //private
    @Before
    public void setup() {
        richTable = new RichTable();

        richTable = spy(richTable);

        ConfigurationService configurationService = mock(ConfigurationService.class);
        doReturn(configurationService).when(richTable).getConfigurationService();

        group = new CollectionGroup();
        group.setCollectionObjectClass(Employee.class);

        TableLayoutManager layoutManager = new TableLayoutManager();
        layoutManager.setRenderSequenceField(true);

        List<Component> items = new ArrayList<Component>(1);
        DataField name = new DataField();
        name.setPropertyName("employeeId");
        items.add(name);
        DataField number = new DataField();
        number.setPropertyName("positionTitle");
        items.add(number);
        DataField contactEmail = new DataField();
        contactEmail.setPropertyName("contactEmail");
        items.add(contactEmail);

        layoutManager = spy(layoutManager);
        doReturn(items).when(layoutManager).getFirstRowFields();

        group.setLayoutManager(layoutManager);
        group.setIncludeLineSelectionField(false);
        group.setRenderLineActions(false);

        group.setItems(items);

        mockView =  mock(View.class);
        ViewHelperService mockViewHelperService = mock(ViewHelperService.class);
        when(mockView.getViewHelperService()).thenReturn(mockViewHelperService);
    }

    @Test
    /**
     * test that without aoColumns being set explicitly, the default behaviour continues
     */
    public void testComponentOptionsDefault() throws Exception {
        assertRichTableComponentOptions(null, "[" + EXPECTED + " ]", UifConstants.TableToolsKeys.AO_COLUMN_DEFS);
    }

    @Test
    /**
     * test that when aoColumns is explicitly set, it is integrated into the rich table rendering logic
     */
    public void testComponentOptionsAoColumnsJSOptions() throws Exception {
        String innerColValues = "{bVisible: false}, null, null";
        assertRichTableComponentOptions("[" + innerColValues + "]", "[" + EXPECTED + " ," + innerColValues + "]",
                UifConstants.TableToolsKeys.AO_COLUMN_DEFS);
    }

    @Test
    /**
     * test whether a hidden column, when marked as sortable is still hidden
     */
    public void testComponentOptionsHideColumnOnRichTable() {
        Set<String> hiddenColumns = new HashSet<String>();
        hiddenColumns.add("employeeId");
        Set<String> sortableColumns = new HashSet<String>();
        sortableColumns.add("positionTitle");
        richTable.setSortableColumns(sortableColumns);
        richTable.setHiddenColumns(hiddenColumns);
        String expected = "[" + S_TYPE + ", " +
                B_VISIBLE_FALSE_TARGETS_1 + ", " +
                S_SORT_DATA_TARGETS_2 + ", " +
                B_SORTABLE_FALSE_TARGETS_3 + "]";
        assertRichTableComponentOptions(null, expected, UifConstants.TableToolsKeys.AO_COLUMN_DEFS);
    }

    @Test
    /**
     * test that sortableColumns and hiddenColumns, when set on layoutManager, will not override those properties on the richTable
     */
    public void testComponentOptionsHideColumnOnLayoutManager() {
        // set rich table properties
        Set<String> richTableHiddenColumns = new HashSet<String>();
        richTableHiddenColumns.add("employeeId");
        Set<String> sortableColumns = new HashSet<String>();
        sortableColumns.add("positionTitle");
        richTable.setSortableColumns(sortableColumns);
        richTable.setHiddenColumns(richTableHiddenColumns);
        // set layout manager properties
        Set<String> lmHiddenColumns = new HashSet<String>();
        lmHiddenColumns.add("contactEmail");
        Set<String> lmSortableColumns = new HashSet<String>();
        lmSortableColumns.add("employeeId");
        ((TableLayoutManager) group.getLayoutManager()).setSortableColumns(lmSortableColumns);
        ((TableLayoutManager) group.getLayoutManager()).setHiddenColumns(lmHiddenColumns);
        // Watch out for spaces
        String expected = "[" + EXPECTED.replace(S_SORT_DATA_TARGETS_1 + " ,", B_VISIBLE_FALSE_TARGETS_1 + ",") + "]";
        expected = expected.replace(S_SORT_DATA_TARGETS_2 + " ,", S_SORT_DATA_TARGETS_2 + ",");
        expected = expected.replace(S_SORT_DATA_TARGETS_3, B_SORTABLE_FALSE_TARGETS_3);
        assertRichTableComponentOptions(null, expected, UifConstants.TableToolsKeys.AO_COLUMN_DEFS);
    }

    /**
     * a common method to test rich table options
     * 
     * @param optionsOnGroup - a string in JSON format of the options set on the collection group
     * @param optionsOnRichTable - a string in JSON format of the options set on the rich table
     * @param optionKey - a string with the rich table option key being tested
     */
    private void assertRichTableComponentOptions(String optionsOnGroup, String optionsOnRichTable, String optionKey) {
        
        if (richTable.getTemplateOptions() == null) {
            richTable.setTemplateOptions(new HashMap<String, String>());
        }
        
        richTable.getTemplateOptions().put(optionKey, optionsOnGroup);
        richTable.performFinalize(mockView, new UifFormBase(), group);
        assertEquals(optionsOnRichTable, richTable.getTemplateOptions().get(optionKey));
    }
}
