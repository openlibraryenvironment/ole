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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.uif.CssConstants;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifPropertyPaths;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.DataBinding;
import org.kuali.rice.krad.uif.component.KeepExpression;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.element.Label;
import org.kuali.rice.krad.uif.element.Message;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.Field;
import org.kuali.rice.krad.uif.field.FieldGroup;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.field.MessageField;
import org.kuali.rice.krad.uif.util.ColumnCalculationInfo;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.ExpressionUtils;
import org.kuali.rice.krad.uif.view.ExpressionEvaluator;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.Pager;
import org.kuali.rice.krad.uif.widget.RichTable;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.web.form.UifFormBase;

import com.google.common.collect.Lists;

/**
 * Layout manager that works with {@code CollectionGroup} components and renders the collection as a
 * Table
 * 
 * <p>
 * Based on the fields defined, the {@code TableLayoutManager} will dynamically create instances of
 * the fields for each collection row. In addition, the manager can create standard fields like the
 * action and sequence fields for each row. The manager supports options inherited from the
 * {@code GridLayoutManager} such as rowSpan, colSpan, and cell width settings.
 * </p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "tableCollectionLayout-bean", parent = "Uif-TableCollectionLayout")
public class TableLayoutManager extends GridLayoutManager implements CollectionLayoutManager {
    private static final long serialVersionUID = 3622267585541524208L;

    private boolean useShortLabels;
    private boolean repeatHeader;
    private Label headerLabelPrototype;

    private boolean renderSequenceField;
    private boolean generateAutoSequence;
    private Field sequenceFieldPrototype;

    private FieldGroup actionFieldPrototype;
    private FieldGroup subCollectionFieldGroupPrototype;
    private Field selectFieldPrototype;

    private boolean separateAddLine;
    private Group addLineGroup;

    // internal counter for the data columns (not including sequence, action)
    private int numberOfDataColumns;

    private List<Label> headerLabels;
    private List<Field> allRowFields;
    private List<Field> firstRowFields;

    private Pager pagerWidget;
    private RichTable richTable;
    private boolean headerAdded;

    private int actionColumnIndex = -1;
    private String actionColumnPlacement;

    //row details properties
    private Group rowDetailsGroup;
    private boolean rowDetailsOpen;
    private boolean showToggleAllDetails;
    private Action toggleAllDetailsAction;
    private boolean ajaxDetailsRetrieval;
    private Action expandDetailsActionPrototype;

    //grouping properties
    @KeepExpression
    private String groupingTitle;
    private String groupingPrefix;
    private int groupingColumnIndex;
    private List<String> groupingPropertyNames;

    //total properties
    private boolean renderOnlyLeftTotalLabels;
    private boolean showTotal;
    private boolean showPageTotal;
    private boolean showGroupTotal;
    private boolean generateGroupTotalRows;
    private Label totalLabel;
    private Label pageTotalLabel;
    private Label groupTotalLabelPrototype;

    private List<String> columnsToCalculate;
    private List<ColumnCalculationInfo> columnCalculations;
    private List<Component> footerCalculationComponents;

    //row css
    private Map<String, String> conditionalRowCssClasses;

    public TableLayoutManager() {
        useShortLabels = false;
        repeatHeader = false;
        renderSequenceField = true;
        generateAutoSequence = false;
        separateAddLine = false;
        rowDetailsOpen = false;

        headerLabels = new ArrayList<Label>();
        allRowFields = new ArrayList<Field>();
        firstRowFields = new ArrayList<Field>();
        columnsToCalculate = new ArrayList<String>();
        columnCalculations = new ArrayList<ColumnCalculationInfo>();
        conditionalRowCssClasses = new HashMap<String, String>();
    }

    /**
     * The following actions are performed:
     * 
     * <ul>
     * <li>Sets sequence field prototype if auto sequence is true</li>
     * <li>Initializes the prototypes</li>
     * </ul>
     * 
     * @see org.kuali.rice.krad.uif.layout.BoxLayoutManager#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.container.Container)
     */
    @Override
    public void performInitialization(View view, Object model, Container container) {
        CollectionGroup collectionGroup = (CollectionGroup) container;

        this.setupDetails(collectionGroup, view);
        this.setupGrouping(collectionGroup, view);

        if (collectionGroup.isAddViaLightBox()) {
            setSeparateAddLine(true);
        }

        super.performInitialization(view, model, container);

        getRowCssClasses().clear();

        if (generateAutoSequence && !(getSequenceFieldPrototype() instanceof MessageField)) {
            sequenceFieldPrototype = ComponentFactory.getMessageField();
            view.assignComponentIds(getSequenceFieldPrototype());
        }
    }

    /**
     * performApplyModel override. Takes expressions that may be set in the columnCalculation
     * objects and populates them correctly into those component's propertyExpressions.
     * 
     * @param view view instance to which the layout manager belongs
     * @param model Top level object containing the data (could be the form or a top level business
     *        object, dto)
     * @param container
     */
    @Override
    public void performApplyModel(View view, Object model, Container container) {
        super.performApplyModel(view, model, container);

        for (ColumnCalculationInfo cInfo : columnCalculations) {
            ExpressionUtils.populatePropertyExpressionsFromGraph(cInfo, false);
        }
    }

    /**
     * Sets up the final column count for rendering based on whether the sequence and action fields
     * have been generated, sets up column calculations, and richTable rowGrouping options
     * 
     * @see org.kuali.rice.krad.uif.layout.LayoutManagerBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.container.Container)
     */
    @Override
    public void performFinalize(View view, Object model, Container container) {
        super.performFinalize(view, model, container);

        UifFormBase formBase = (UifFormBase) model;

        CollectionGroup collectionGroup = (CollectionGroup) container;

        int totalColumns = getNumberOfDataColumns();
        if (renderSequenceField) {
            totalColumns++;
        }

        if (collectionGroup.isIncludeLineSelectionField()) {
            totalColumns++;
        }

        if (collectionGroup.isRenderLineActions() && !collectionGroup.isReadOnly()) {
            totalColumns++;
        }

        setNumberOfColumns(totalColumns);

        // if add line event, add highlighting for added row
        if (UifConstants.ActionEvents.ADD_LINE.equals(formBase.getActionEvent())) {
            String highlightScript = "jQuery(\"#" + container.getId() + " tr:first\").effect(\"highlight\",{}, 6000);";
            String onReadyScript = collectionGroup.getOnDocumentReadyScript();
            if (StringUtils.isNotBlank(onReadyScript)) {
                highlightScript = onReadyScript + highlightScript;
            }
            collectionGroup.setOnDocumentReadyScript(highlightScript);
        }

        //setup the column calculations functionality and components
        if (columnCalculations != null && richTable != null &&
                this.getAllRowFields() != null && !this.getAllRowFields().isEmpty()) {
            setupColumnCalculations(view, model, container, totalColumns);
        }

        //set the js properties for rowGrouping on richTables
        if ((groupingPropertyNames != null || StringUtils.isNotBlank(this.getGroupingTitle())) && richTable != null) {
            richTable.setGroupingOptionsJSString("{iGroupingColumnIndex: "
                    + groupingColumnIndex
                    + ", bGenerateGroupTotalRows:"
                    + this.generateGroupTotalRows
                    + ", bSetGroupingClassOnTR: true"
                    + ", sGroupingClass: 'uif-groupRow'"
                    + (this.getGroupingPrefix() != null ? ", sGroupLabelPrefix: '" + this.getGroupingPrefix() + "'" :
                            "")
                    + "}");
        }

        // Calculate the number of pages for the pager widget if we are using server paging
        if ((this.getRichTable() == null || !this.getRichTable().isRender())
                && ((CollectionGroup) container).isUseServerPaging()
                && this.getPagerWidget() != null) {
            // Set the appropriate page, total pages, and link script into the Pager
            CollectionLayoutUtils.setupPagerWidget(pagerWidget, collectionGroup, model);
        }

    }

    /**
     * Sets up the grouping MessageField to be used in the first column of the table layout for
     * grouping collection content into groups based on values of the line's fields
     * 
     * @param collectionGroup collection group for this layout
     * @param view the view
     */
    private void setupGrouping(CollectionGroup collectionGroup, View view) {
        //Grouping setup
        String groupingTitleExpression = "";
        if (StringUtils.isNotBlank(this.getPropertyExpression(UifPropertyPaths.GROUPING_TITLE))) {
            groupingTitleExpression = this.getPropertyExpression(UifPropertyPaths.GROUPING_TITLE);
            this.setGroupingTitle(this.getPropertyExpression(UifPropertyPaths.GROUPING_TITLE));
        } else if (this.getGroupingPropertyNames() != null) {

            for (String propertyName : this.getGroupingPropertyNames()) {
                groupingTitleExpression = groupingTitleExpression + ", " + propertyName;
            }

            groupingTitleExpression = groupingTitleExpression.replaceFirst(", ",
                    "@{" + UifConstants.LINE_PATH_BIND_ADJUST_PREFIX);
            groupingTitleExpression = groupingTitleExpression.replace(", ",
                    "}, @{" + UifConstants.LINE_PATH_BIND_ADJUST_PREFIX);
            groupingTitleExpression = groupingTitleExpression.trim() + "}";
        }

        if (StringUtils.isNotBlank(groupingTitleExpression)) {
            MessageField groupingMessageField = ComponentFactory.getColGroupingField();
            groupingMessageField.getMessage().getPropertyExpressions().put(UifPropertyPaths.MESSAGE_TEXT,
                    groupingTitleExpression);

            groupingMessageField.addDataAttribute(UifConstants.DataAttributes.ROLE,
                    UifConstants.RoleTypes.ROW_GROUPING);

            view.assignComponentIds(groupingMessageField);

            List<Component> theItems = new ArrayList<Component>();
            theItems.add(groupingMessageField);
            theItems.addAll(collectionGroup.getItems());
            collectionGroup.setItems(theItems);
        }
    }

    /**
     * Setup the column calculations functionality and components
     * 
     * @param view the view
     * @param model the model
     * @param container the parent container
     * @param totalColumns total number of columns in the table
     */
    protected void setupColumnCalculations(View view, Object model, Container container, int totalColumns) {
        footerCalculationComponents = new ArrayList<Component>(totalColumns);

        //add nulls for each column to start - nulls will be processed by the ftl as a blank cell
        for (int i = 0; i < totalColumns; i++) {
            footerCalculationComponents.add(null);
        }

        int leftLabelColumnIndex = 0;
        if (groupingPropertyNames != null || StringUtils.isNotBlank(this.getGroupingTitle())) {
            leftLabelColumnIndex = 1;
        }

        //process each column calculation
        for (ColumnCalculationInfo cInfo : columnCalculations) {
            //propertyName is REQUIRED throws exception if not set
            if (StringUtils.isNotBlank(cInfo.getPropertyName())) {
                for (int i = 0; i < this.getNumberOfColumns(); i++) {
                    Component component = this.getAllRowFields().get(i);
                    if (component != null && component instanceof DataField &&
                            ((DataField) component).getPropertyName().equals(cInfo.getPropertyName())) {
                        cInfo.setColumnNumber(i);
                    }
                }

                this.getColumnsToCalculate().add(cInfo.getColumnNumber().toString());
            } else {
                throw new RuntimeException("TableLayoutManager(" + container.getId() + "->" + this.getId() +
                        ") ColumnCalculationInfo MUST have a propertyName set");
            }

            // create a new field group to hold the totals fields
            FieldGroup calculationFieldGroup = ComponentFactory.getFieldGroup();
            calculationFieldGroup.addDataAttribute(UifConstants.DataAttributes.ROLE, "totalsBlock");

            List<Component> calculationFieldGroupItems = new ArrayList<Component>();

            //setup page total field and add it to footer's group for this column
            if (cInfo.isShowPageTotal()) {
                Field pageTotalDataField = setupTotalField(cInfo.getPageTotalField(), cInfo, this.isShowPageTotal(),
                        this.getPageTotalLabel(), "pageTotal", leftLabelColumnIndex);
                calculationFieldGroupItems.add(pageTotalDataField);
            }

            //setup total field and add it to footer's group for this column
            if (cInfo.isShowTotal()) {
                Field totalDataField = setupTotalField(cInfo.getTotalField(), cInfo, this.isShowTotal(),
                        this.getTotalLabel(), "total", leftLabelColumnIndex);

                if (!cInfo.isRecalculateTotalClientSide()) {
                    totalDataField.addDataAttribute(UifConstants.DataAttributes.SKIP_TOTAL, "true");
                }

                calculationFieldGroupItems.add(totalDataField);
            }

            //setup total field and add it to footer's group for this column
            //do not generate group total rows if group totals are not being shown
            if (cInfo.isShowGroupTotal()) {
                Field groupTotalDataField = setupTotalField(cInfo.getGroupTotalFieldPrototype(), cInfo,
                        this.isShowGroupTotal(), this.getGroupTotalLabelPrototype(), "groupTotal",
                        leftLabelColumnIndex);
                groupTotalDataField.setId(container.getId() + "_gTotal" + cInfo.getColumnNumber());
                groupTotalDataField.setStyle("display: none;");

                calculationFieldGroupItems.add(groupTotalDataField);

                if (this.isRenderOnlyLeftTotalLabels() && !this.isShowGroupTotal()) {
                    generateGroupTotalRows = false;
                } else {
                    generateGroupTotalRows = true;
                }
            }

            calculationFieldGroup.setItems(calculationFieldGroupItems);

            view.assignComponentIds(calculationFieldGroup);

            //Determine if there is already a fieldGroup present for this column's footer
            //if so create a new group and add the new calculation fields to the already existing ones
            //otherwise just add it
            Component component = footerCalculationComponents.get(cInfo.getColumnNumber());
            if (component != null && component instanceof FieldGroup) {
                Group verticalComboCalcGroup = ComponentFactory.getVerticalBoxGroup();
                view.assignComponentIds(verticalComboCalcGroup);

                List<Component> comboGroupItems = new ArrayList<Component>();
                comboGroupItems.add(component);
                comboGroupItems.add(calculationFieldGroup);
                verticalComboCalcGroup.setItems(comboGroupItems);

                footerCalculationComponents.set(cInfo.getColumnNumber(), verticalComboCalcGroup);
            } else if (component != null && component instanceof Group) {
                List<Component> comboGroupItems = new ArrayList<Component>();
                comboGroupItems.addAll(((Group) component).getItems());
                comboGroupItems.add(calculationFieldGroup);

                ((Group) component).setItems(comboGroupItems);

                footerCalculationComponents.set(cInfo.getColumnNumber(), component);
            } else {
                footerCalculationComponents.set(cInfo.getColumnNumber(), calculationFieldGroup);
            }
        }

        //special processing for the left labels - when there are no total fields in this column
        //add the label to the column footer directly
        if (this.renderOnlyLeftTotalLabels && footerCalculationComponents.get(leftLabelColumnIndex) == null) {
            Group labelGroup = ComponentFactory.getVerticalBoxGroup();
            view.assignComponentIds(labelGroup);
            List<Component> groupItems = new ArrayList<Component>();

            if (this.isShowGroupTotal()) {
                //display none - this label is copied by the javascript
                groupTotalLabelPrototype.setStyle("display: none;");
                groupTotalLabelPrototype.addDataAttribute(UifConstants.DataAttributes.ROLE, "groupTotalLabel");
                view.assignComponentIds(groupTotalLabelPrototype);
                groupItems.add(groupTotalLabelPrototype);
            }

            if (this.isShowPageTotal()) {
                view.assignComponentIds(pageTotalLabel);
                pageTotalLabel.addDataAttribute(UifConstants.DataAttributes.ROLE, "pageTotal");
                groupItems.add(pageTotalLabel);
            }

            if (this.isShowTotal()) {
                view.assignComponentIds(totalLabel);
                groupItems.add(totalLabel);
            }

            labelGroup.setItems(groupItems);

            footerCalculationComponents.set(leftLabelColumnIndex, labelGroup);
        }

        // perform the lifecycle for all the newly generated components as a result of processing the
        // column calculations
        for (Component component : footerCalculationComponents) {
            view.getViewHelperService().spawnSubLifecyle(view, model, component, container, null, null);
        }
    }

    /**
     * Setup the totalField with the columnCalculationInfo(cInfo) passed in. Param show represents
     * the tableLayoutManager's setting for the type of total being processed.
     * 
     * @param totalField the field to setup
     * @param cInfo ColumnCalculation info to use to setup the field
     * @param show show the field (if renderOnlyLeftTotalLabels is true, otherwise uses value in
     *        cInfo)
     * @param leftLabel the leftLabel, not used if renderOnlyLeftTotalLabels is false
     * @param type type used to set the dataAttribute role - used by the js for selection
     * @param leftLabelColumnIndex index of the leftLabelColumn (0 or 1 if grouping enabled - hidden
     *        column)
     * @return the field with cInfo and tableLayoutManager settings applied as appropriate
     */
    protected Field setupTotalField(Field totalField, ColumnCalculationInfo cInfo, boolean show, Label leftLabel,
            String type, int leftLabelColumnIndex) {
        //setup the totals field
        Field totalDataField = totalField;
        totalDataField.addDataAttribute(UifConstants.DataAttributes.ROLE, type);
        totalDataField.addDataAttribute("function", cInfo.getCalculationFunctionName());
        totalDataField.addDataAttribute("params", cInfo.getCalculationFunctionExtraData());

        if (cInfo.getColumnNumber() != leftLabelColumnIndex) {
            //do not render labels for columns which have totals and the renderOnlyLeftTotalLabels
            //flag is set
            totalDataField.getFieldLabel().setRender(!this.isRenderOnlyLeftTotalLabels());
        } else if (cInfo.getColumnNumber() == leftLabelColumnIndex && this.isRenderOnlyLeftTotalLabels()) {
            //renderOnlyLeftTotalLabel is set to true, but the column has a total itself - set the layout
            //manager settings directly into the field
            totalDataField.setFieldLabel(leftLabel);
        }

        if (this.isRenderOnlyLeftTotalLabels()) {
            totalDataField.setRender(show);
        }

        return totalDataField;
    }

    /**
     * Assembles the field instances for the collection line. The given sequence field prototype is
     * copied for the line sequence field. Likewise a copy of the actionFieldPrototype is made and
     * the given actions are set as the items for the action field. Finally the generated items are
     * assembled together into the allRowFields list with the given lineFields.
     * 
     * @see org.kuali.rice.krad.uif.layout.CollectionLayoutManager#buildLine(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.container.CollectionGroup, java.util.List,
     *      java.util.List, java.lang.String, java.util.List, java.lang.String, java.lang.Object,
     *      int)
     */
    public void buildLine(View view, Object model, CollectionGroup collectionGroup, List<Field> lineFields,
            List<FieldGroup> subCollectionFields, String bindingPath, List<Action> actions, String idSuffix,
            Object currentLine, int lineIndex) {

        // since expressions are not evaluated on child components yet, we need to evaluate any properties
        // we are going to read for building the table
        ExpressionEvaluator expressionEvaluator = view.getViewHelperService().getExpressionEvaluator();
        for (Field lineField : lineFields) {
            lineField.pushObjectToContext(UifConstants.ContextVariableNames.PARENT, collectionGroup);
            lineField.pushAllToContext(view.getViewHelperService().getCommonContext(view, lineField));

            expressionEvaluator.evaluatePropertyExpression(view, lineField.getContext(), lineField,
                    UifPropertyPaths.ROW_SPAN, true);
            expressionEvaluator.evaluatePropertyExpression(view, lineField.getContext(), lineField,
                    UifPropertyPaths.COL_SPAN, true);
            expressionEvaluator.evaluatePropertyExpression(view, lineField.getContext(), lineField,
                    UifPropertyPaths.REQUIRED, true);
            expressionEvaluator.evaluatePropertyExpression(view, lineField.getContext(), lineField,
                    UifPropertyPaths.READ_ONLY, true);
        }

        // if first line for table set number of data columns
        if (allRowFields.isEmpty()) {
            if (isSuppressLineWrapping()) {
                setNumberOfDataColumns(lineFields.size());
            } else {
                setNumberOfDataColumns(getNumberOfColumns());
            }
        }

        boolean isAddLine = false;

        // If first row or row wrap is happening
        if (lineIndex == -1 || (lineFields.size() != numberOfDataColumns
                && ((lineIndex + 1) * numberOfDataColumns) < lineFields.size())) {
            isAddLine = true;
        }

        // capture the first row of fields for widgets that build off the table
        if (lineIndex == 0 || this.firstRowFields.isEmpty()) {
            this.firstRowFields = lineFields;
        }

        boolean renderActions = collectionGroup.isRenderLineActions() && !collectionGroup.isReadOnly();
        int extraColumns = 0;
        String rowCss = "";
        boolean addLineInTable =
                collectionGroup.isRenderAddLine() && !collectionGroup.isReadOnly() && !isSeparateAddLine();

        if (collectionGroup.isHighlightNewItems() && ((UifFormBase) model).isAddedCollectionItem(currentLine)) {
            rowCss = collectionGroup.getNewItemsCssClass();
        } else if (isAddLine && addLineInTable) {
            rowCss = collectionGroup.getAddItemCssClass();
            this.addStyleClass(CssConstants.Classes.HAS_ADD_LINE);
        }

        // do not allow null rowCss
        if (rowCss == null) {
            rowCss = "";
        }

        // conditionalRowCssClass generation logic, if applicable
        if (conditionalRowCssClasses != null && !conditionalRowCssClasses.isEmpty()) {
            int oddRemainder = 1;
            if (!addLineInTable) {
                oddRemainder = 0;
            }

            boolean isOdd = lineIndex % 2 == oddRemainder || lineIndex == -1;
            Map<String, Object> lineContext = new HashMap<String, Object>();

            lineContext.putAll(this.getContext());
            lineContext.put(UifConstants.ContextVariableNames.LINE, currentLine);
            lineContext.put(UifConstants.ContextVariableNames.MANAGER, this);
            lineContext.put(UifConstants.ContextVariableNames.VIEW, view);
            lineContext.put(UifConstants.ContextVariableNames.LINE_SUFFIX, idSuffix);
            lineContext.put(UifConstants.ContextVariableNames.INDEX, Integer.valueOf(lineIndex));
            lineContext.put(UifConstants.ContextVariableNames.COLLECTION_GROUP, collectionGroup);
            lineContext.put(UifConstants.ContextVariableNames.IS_ADD_LINE, isAddLine && !isSeparateAddLine());
            lineContext.put(UifConstants.ContextVariableNames.READONLY_LINE, collectionGroup.isReadOnly());

            // get row css based on conditionalRowCssClasses map
            rowCss = rowCss + " " + KRADUtils.generateRowCssClassString(conditionalRowCssClasses, lineIndex, isOdd,
                    lineContext, expressionEvaluator);
        }

        rowCss = StringUtils.removeStart(rowCss, " ");
        this.getRowCssClasses().add(rowCss);

        // if separate add line prepare the add line group
        if (isAddLine && separateAddLine) {
            if (StringUtils.isBlank(addLineGroup.getTitle()) && StringUtils.isBlank(
                    addLineGroup.getHeader().getHeaderText())) {
                addLineGroup.getHeader().setHeaderText(collectionGroup.getAddLabel());
            }
            addLineGroup.setItems(lineFields);

            List<Component> footerItems = new ArrayList<Component>(actions);
            footerItems.addAll(addLineGroup.getFooter().getItems());
            addLineGroup.getFooter().setItems(footerItems);

            if (collectionGroup.isAddViaLightBox()) {
                String actionScript = "showLightboxComponent('" + addLineGroup.getId() + "');";
                if (StringUtils.isNotBlank(collectionGroup.getAddViaLightBoxAction().getActionScript())) {
                    actionScript = collectionGroup.getAddViaLightBoxAction().getActionScript() + actionScript;
                }
                collectionGroup.getAddViaLightBoxAction().setActionScript(actionScript);
                addLineGroup.setStyle("display: none");
            }

            return;
        }

        // TODO: implement repeat header
        if (!headerAdded) {
            headerLabels = new ArrayList<Label>();
            allRowFields = new ArrayList<Field>();

            buildTableHeaderRows(collectionGroup, lineFields);
            ComponentUtils.pushObjectToContext(headerLabels, UifConstants.ContextVariableNames.LINE, currentLine);
            ComponentUtils.pushObjectToContext(headerLabels, UifConstants.ContextVariableNames.INDEX, new Integer(
                    lineIndex));
            headerAdded = true;
        }

        // set label field rendered to true on line fields and adjust cell properties
        for (Field field : lineFields) {
            field.setLabelRendered(true);
            field.setFieldLabel(null);

            setCellAttributes(field);
        }

        int rowCount = calculateNumberOfRows(lineFields);
        int rowSpan = rowCount + subCollectionFields.size();

        if (actionColumnIndex == 1 && renderActions) {
            addActionColumn(idSuffix, currentLine, lineIndex, rowSpan, actions);
        }

        // sequence field is always first and should span all rows for the line
        if (renderSequenceField) {
            Field sequenceField = null;
            if (!isAddLine) {
                sequenceField = ComponentUtils.copy(getSequenceFieldPrototype(), idSuffix);

                //Ignore in validation processing
                sequenceField.addDataAttribute(UifConstants.DataAttributes.VIGNORE, "yes");

                if (generateAutoSequence && (sequenceField instanceof MessageField)) {
                    ((MessageField) sequenceField).setMessageText(Integer.toString(lineIndex + 1));
                }
            } else {
                sequenceField = ComponentFactory.getMessageField();
                view.assignComponentIds(sequenceField);

                Message sequenceMessage = ComponentUtils.copy(collectionGroup.getAddLineLabel(), idSuffix);
                ((MessageField) sequenceField).setMessage(sequenceMessage);

                // adjusting add line label to match sequence prototype cells attributes
                sequenceField.setCellWidth(getSequenceFieldPrototype().getCellWidth());
                sequenceField.setCellStyle(getSequenceFieldPrototype().getCellStyle());
            }

            sequenceField.setRowSpan(rowSpan);

            if (sequenceField instanceof DataBinding) {
                ((DataBinding) sequenceField).getBindingInfo().setBindByNamePrefix(bindingPath);
            }

            setCellAttributes(sequenceField);

            ComponentUtils.updateContextForLine(sequenceField, currentLine, lineIndex, idSuffix);
            allRowFields.add(sequenceField);
            extraColumns++;

            if (actionColumnIndex == 2 && renderActions) {
                addActionColumn(idSuffix, currentLine, lineIndex, rowSpan, actions);
            }
        }

        // select field will come after sequence field (if enabled) or be first column
        if (collectionGroup.isIncludeLineSelectionField()) {
            Field selectField = ComponentUtils.copy(getSelectFieldPrototype(), idSuffix);
            CollectionLayoutUtils.prepareSelectFieldForLine(selectField, collectionGroup, bindingPath, currentLine);

            ComponentUtils.updateContextForLine(selectField, currentLine, lineIndex, idSuffix);
            setCellAttributes(selectField);

            allRowFields.add(selectField);

            extraColumns++;

            if (renderActions) {
                if ((actionColumnIndex == 3 && renderSequenceField) || (actionColumnIndex == 2
                        && !renderSequenceField)) {
                    addActionColumn(idSuffix, currentLine, lineIndex, rowSpan, actions);
                }
            }
        }

        // now add the fields in the correct position
        int cellPosition = 0;

        boolean renderActionsLast = actionColumnIndex == -1 || actionColumnIndex > lineFields.size() + extraColumns;
        boolean hasGrouping = (groupingPropertyNames != null || StringUtils.isNotBlank(this.getGroupingTitle()));
        boolean insertActionField = false;

        for (Field lineField : lineFields) {
            //Check to see if ActionField needs to be inserted before this lineField because of wrapping.
            // Since actionField has a colSpan of 1 add that to the previous cellPosition instead of the
            // current lineField's colSpan.
            // Only insert if ActionField has to be placed at the end. Else the specification of actionColumnIndex should
            // take care of putting it in the right location
            insertActionField = (cellPosition != 0 && lineFields.size() != numberOfDataColumns)
                    && renderActions
                    && renderActionsLast
                    && ((cellPosition % numberOfDataColumns) == 0);

            cellPosition += lineField.getColSpan();

            //special handling for grouping field - this field MUST be first
            Map<String, String> lineFieldDataAttributes = lineField.getDataAttributes();
            if (hasGrouping
                    && (lineField instanceof MessageField)
                    &&
                    lineFieldDataAttributes != null
                    && UifConstants.RoleTypes.ROW_GROUPING.equals(lineFieldDataAttributes
                            .get(UifConstants.DataAttributes.ROLE))) {
                int groupFieldIndex = allRowFields.size() - extraColumns;
                allRowFields.add(groupFieldIndex, lineField);
                groupingColumnIndex = 0;
                if (isAddLine) {
                    ((MessageField) lineField).getMessage().getPropertyExpressions().remove(
                            UifPropertyPaths.MESSAGE_TEXT);
                    ((MessageField) lineField).getMessage().setMessageText("addLine");
                }
            } else {
                // If the row wraps before the last element
                if (insertActionField) {
                    addActionColumn(idSuffix, currentLine, lineIndex, rowSpan, actions);
                }

                allRowFields.add(lineField);
            }

            // action field
            if (!renderActionsLast && cellPosition == (actionColumnIndex - extraColumns - 1)) {
                addActionColumn(idSuffix, currentLine, lineIndex, rowSpan, actions);
            }

            //details action
            if (lineField instanceof FieldGroup && ((FieldGroup) lineField).getItems() != null) {
                for (Component component : ((FieldGroup) lineField).getItems()) {
                    if (component != null
                            && component instanceof Action
                            && (component.getDataAttributes() != null)
                            && component.getDataAttributes().get("role") != null
                            && component.getDataAttributes().get("role").equals("detailsLink")
                            && StringUtils.isBlank(((Action) component).getActionScript())) {
                        ((Action) component).setActionScript("rowDetailsActionHandler(this,'" + this.getId() + "');");
                    }
                }
            }

            //special column calculation handling to identify what type of handler will be attached
            //and add special styling
            if (lineField instanceof InputField && columnCalculations != null) {
                for (ColumnCalculationInfo cInfo : columnCalculations) {
                    if (cInfo.getPropertyName().equals(((InputField) lineField).getPropertyName())) {
                        if (cInfo.isCalculateOnKeyUp()) {
                            lineField.addDataAttribute(UifConstants.DataAttributes.TOTAL, "keyup");
                        } else {
                            lineField.addDataAttribute(UifConstants.DataAttributes.TOTAL, "change");
                        }
                        lineField.addStyleClass("uif-calculationField");
                    }
                }
            }
        }

        if (lineFields.size() == numberOfDataColumns && renderActions && renderActionsLast) {
            addActionColumn(idSuffix, currentLine, lineIndex, rowSpan, actions);
        }

        // update colspan on sub-collection fields
        for (FieldGroup subCollectionField : subCollectionFields) {
            subCollectionField.setColSpan(numberOfDataColumns);
        }

        // add sub-collection fields to end of data fields
        allRowFields.addAll(subCollectionFields);
    }

    /**
     * Adds the action field in a row
     * 
     * @param idSuffix
     * @param currentLine
     * @param lineIndex
     * @param rowSpan
     * @param actions
     */
    private void addActionColumn(String idSuffix, Object currentLine, int lineIndex, int rowSpan,
            List<Action> actions) {
        FieldGroup lineActionsField = ComponentUtils.copy(getActionFieldPrototype(), idSuffix);

        ComponentUtils.updateContextForLine(lineActionsField, currentLine, lineIndex, idSuffix);
        lineActionsField.setRowSpan(rowSpan);
        lineActionsField.setItems(actions);

        setCellAttributes(lineActionsField);

        allRowFields.add(lineActionsField);
    }

    /**
     * Create the {@code Label} instances that will be used to render the table header
     * 
     * <p>
     * For each column, a copy of headerLabelPrototype is made that determines the label
     * configuration. The actual label text comes from the field for which the header applies to.
     * The first column is always the sequence (if enabled) and the last column contains the
     * actions. Both the sequence and action header fields will span all rows for the header.
     * </p>
     * 
     * <p>
     * The headerLabels list will contain the final list of header fields built
     * </p>
     * 
     * @param collectionGroup CollectionGroup container the table applies to
     * @param lineFields fields for the data columns from which the headers are pulled
     */
    protected void buildTableHeaderRows(CollectionGroup collectionGroup, List<Field> lineFields) {
        // row count needed to determine the row span for the sequence and
        // action fields, since they should span all rows for the line
        int rowCount = calculateNumberOfRows(lineFields);

        boolean renderActions = collectionGroup.isRenderLineActions() && !collectionGroup.isReadOnly();

        String idSuffix = collectionGroup.getSubCollectionSuffix();

        int extraColumns = 0;

        if (actionColumnIndex == 1 && renderActions) {
            addActionHeader(rowCount, idSuffix, 1);
        }

        // first column is sequence label (if action column not 1)
        if (renderSequenceField) {
            getSequenceFieldPrototype().setLabelRendered(true);
            getSequenceFieldPrototype().setRowSpan(rowCount);
            addHeaderField(getSequenceFieldPrototype(), idSuffix, 1);
            extraColumns++;

            if (actionColumnIndex == 2 && renderActions) {
                addActionHeader(rowCount, idSuffix, 2);
            }
        }

        // next is select field
        if (collectionGroup.isIncludeLineSelectionField()) {
            getSelectFieldPrototype().setLabelRendered(true);
            getSelectFieldPrototype().setRowSpan(rowCount);
            addHeaderField(getSelectFieldPrototype(), idSuffix, 1);
            extraColumns++;

            if (actionColumnIndex == 3 && renderActions && renderSequenceField) {
                addActionHeader(rowCount, idSuffix, 3);
            } else if (actionColumnIndex == 2 && renderActions) {
                addActionHeader(rowCount, idSuffix, 2);
            }
        }

        // pull out label fields from the container's items
        int cellPosition = 0;
        boolean renderActionsLast = actionColumnIndex == -1 || actionColumnIndex > lineFields.size() + extraColumns;
        boolean insertActionHeader = false;
        for (Field field : lineFields) {
            if (!field.isRender() && StringUtils.isEmpty(field.getProgressiveRender())) {
                continue;
            }

            //Check to see if ActionField needs to be inserted before this lineField because of wrapping.
            // Since actionField has a colSpan of 1 add that to the previous cellPosition instead of the
            // current lineField's colSpan.
            // Only Insert if ActionField has to be placed at the end. Else the specification of actionColumnIndex
            // should take care of putting it in the right location
            insertActionHeader = (cellPosition != 0
                    && lineFields.size() != numberOfDataColumns
                    && renderActions
                    && renderActionsLast
                    && ((cellPosition % numberOfDataColumns) == 0));

            if (insertActionHeader) {
                addActionHeader(rowCount, idSuffix, cellPosition);
            }

            cellPosition += field.getColSpan();
            addHeaderField(field, idSuffix, cellPosition);

            // add action header
            if (renderActions && !renderActionsLast && cellPosition == actionColumnIndex - extraColumns - 1) {
                cellPosition += 1;
                addActionHeader(rowCount, idSuffix, cellPosition);
            }
        }

        if (lineFields.size() == numberOfDataColumns && renderActions && renderActionsLast) {
            cellPosition += 1;
            addActionHeader(rowCount, idSuffix, cellPosition);
        }
    }

    /**
     * Adds the action header
     * 
     * @param rowCount
     * @param idSuffix suffix for the header id, also column will be added
     * @param cellPosition
     */
    protected void addActionHeader(int rowCount, String idSuffix, int cellPosition) {
        getActionFieldPrototype().setLabelRendered(true);
        getActionFieldPrototype().setRowSpan(rowCount);
        addHeaderField(getActionFieldPrototype(), idSuffix, cellPosition);
    }

    /**
     * Creates a new instance of the header field prototype and then sets the label to the short (if
     * useShortLabels is set to true) or long label of the given component. After created the header
     * field is added to the list making up the table header
     * 
     * @param field field instance the header field is being created for
     * @param idSuffix suffix for the header id, also column will be added
     * @param column column number for the header, used for setting the id
     */
    protected void addHeaderField(Field field, String idSuffix, int column) {
        String labelSuffix = UifConstants.IdSuffixes.COLUMN + column;
        if (StringUtils.isNotBlank(idSuffix)) {
            labelSuffix = idSuffix + labelSuffix;
        }

        Label headerLabel = ComponentUtils.copy(getHeaderLabelPrototype(), labelSuffix);

        if (useShortLabels) {
            headerLabel.setLabelText(field.getShortLabel());
        } else {
            headerLabel.setLabelText(field.getLabel());
        }

        headerLabel.setRowSpan(field.getRowSpan());
        headerLabel.setColSpan(field.getColSpan());

        if ((field.getRequired() != null) && field.getRequired().booleanValue()) {
            headerLabel.getRequiredMessage().setRender(!field.isReadOnly());
        } else {
            headerLabel.getRequiredMessage().setRender(false);
        }

        setCellAttributes(field);

        // copy cell attributes from the field to the label
        headerLabel.setCellCssClasses(field.getCellCssClasses());
        headerLabel.setCellStyle(field.getCellStyle());
        headerLabel.setCellWidth(field.getCellWidth());

        headerLabels.add(headerLabel);
    }

    /**
     * Calculates how many rows will be needed per collection line to display the list of fields.
     * Assumption is made that the total number of cells the fields take up is evenly divisible by
     * the configured number of columns
     * 
     * @param items list of items that make up one collection line
     * @return number of rows
     */
    protected int calculateNumberOfRows(List<? extends Field> items) {
        int rowCount = 0;

        // check flag that indicates only one row should be created
        if (isSuppressLineWrapping()) {
            return 1;
        }

        // If Overflow is greater than 0 then calculate the col span for the last item in the overflowed row
        if (items.size() % getNumberOfDataColumns() > 0) {
            //get the last line item
            Field field = items.get(items.size() - 1);

            int colSize = 0;
            for (Field f : items) {
                colSize += f.getColSpan();
            }

            field.setColSpan(1 + (numberOfDataColumns - (colSize % numberOfDataColumns)));
            rowCount = ((items.size() / getNumberOfDataColumns()) + 1);
        } else {
            rowCount = items.size() / getNumberOfDataColumns();
        }
        return rowCount;
    }

    /**
     * @see CollectionLayoutManager#getSupportedContainer()
     */
    @Override
    public Class<? extends Container> getSupportedContainer() {
        return CollectionGroup.class;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManagerBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(pagerWidget);
        components.add(richTable);
        components.add(addLineGroup);
        components.addAll(headerLabels);
        components.addAll(allRowFields);

        if (columnCalculations != null) {
            for (ColumnCalculationInfo cInfo : columnCalculations) {
                components.add(cInfo.getTotalField());
                components.add(cInfo.getPageTotalField());
                components.add(cInfo.getGroupTotalFieldPrototype());
            }
        }

        if (isShowToggleAllDetails()) {
            components.add(toggleAllDetailsAction);
        }

        return components;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getComponentPrototypes()
     */
    @Override
    public List<Component> getComponentPrototypes() {
        List<Component> components = super.getComponentPrototypes();

        components.add(getHeaderLabelPrototype());
        components.add(getSequenceFieldPrototype());
        components.add(getActionFieldPrototype());
        components.add(getSubCollectionFieldGroupPrototype());
        components.add(getSelectFieldPrototype());

        return components;
    }

    /**
     * Indicates whether the short label for the collection field should be used as the table header
     * or the regular label
     * 
     * @return true if short label should be used, false if long label should be used
     */
    @BeanTagAttribute(name = "useShortLabels")
    public boolean isUseShortLabels() {
        return this.useShortLabels;
    }

    /**
     * Setter for the use short label indicator
     * 
     * @param useShortLabels
     */
    public void setUseShortLabels(boolean useShortLabels) {
        this.useShortLabels = useShortLabels;
    }

    /**
     * Indicates whether the header should be repeated before each collection row. If false the
     * header is only rendered at the beginning of the table
     * 
     * @return true if header should be repeated, false if it should only be rendered once
     */
    @BeanTagAttribute(name = "repeatHeader")
    public boolean isRepeatHeader() {
        return this.repeatHeader;
    }

    /**
     * Setter for the repeat header indicator
     * 
     * @param repeatHeader
     */
    public void setRepeatHeader(boolean repeatHeader) {
        this.repeatHeader = repeatHeader;
    }

    /**
     * {@code Label} instance to use as a prototype for creating the tables header fields. For each
     * header field the prototype will be copied and adjusted as necessary
     * 
     * @return Label instance to serve as prototype
     */
    @BeanTagAttribute(name = "headerLabelPrototype", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Label getHeaderLabelPrototype() {
        return this.headerLabelPrototype;
    }

    /**
     * Setter for the header field prototype
     * 
     * @param headerLabelPrototype
     */
    public void setHeaderLabelPrototype(Label headerLabelPrototype) {
        this.headerLabelPrototype = headerLabelPrototype;
    }

    /**
     * List of {@code Label} instances that should be rendered to make up the tables header
     * 
     * @return List of label field instances
     */
    public List<Label> getHeaderLabels() {
        return this.headerLabels;
    }

    /**
     * Indicates whether the sequence field should be rendered for the collection
     * 
     * @return true if sequence field should be rendered, false if not
     */
    @BeanTagAttribute(name = "renderSequenceField")
    public boolean isRenderSequenceField() {
        return this.renderSequenceField;
    }

    /**
     * Setter for the render sequence field indicator
     * 
     * @param renderSequenceField
     */
    public void setRenderSequenceField(boolean renderSequenceField) {
        this.renderSequenceField = renderSequenceField;
    }

    /**
     * Attribute name to use as sequence value. For each collection line the value of this field on
     * the line will be retrieved and used as the sequence value
     * 
     * @return sequence property name
     */
    @BeanTagAttribute(name = "sequencePropertyName")
    public String getSequencePropertyName() {
        if ((getSequenceFieldPrototype() != null) && (getSequenceFieldPrototype() instanceof DataField)) {
            return ((DataField) getSequenceFieldPrototype()).getPropertyName();
        }

        return null;
    }

    /**
     * Setter for the sequence property name
     * 
     * @param sequencePropertyName
     */
    public void setSequencePropertyName(String sequencePropertyName) {
        if ((getSequenceFieldPrototype() != null) && (getSequenceFieldPrototype() instanceof DataField)) {
            ((DataField) getSequenceFieldPrototype()).setPropertyName(sequencePropertyName);
        }
    }

    /**
     * Indicates whether the sequence field should be generated with the current line number
     * 
     * <p>
     * If set to true the sequence field prototype will be changed to a message field (if not
     * already a message field) and the text will be set to the current line number
     * </p>
     * 
     * @return true if the sequence field should be generated from the line number, false if not
     */
    @BeanTagAttribute(name = "generateAutoSequence")
    public boolean isGenerateAutoSequence() {
        return this.generateAutoSequence;
    }

    /**
     * Setter for the generate auto sequence field
     * 
     * @param generateAutoSequence
     */
    public void setGenerateAutoSequence(boolean generateAutoSequence) {
        this.generateAutoSequence = generateAutoSequence;
    }

    /**
     * {@code Field} instance to serve as a prototype for the sequence field. For each collection
     * line this instance is copied and adjusted as necessary
     * 
     * @return Attribute field instance
     */
    @BeanTagAttribute(name = "sequenceFieldPrototype", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Field getSequenceFieldPrototype() {
        return this.sequenceFieldPrototype;
    }

    /**
     * Setter for the sequence field prototype
     * 
     * @param sequenceFieldPrototype
     */
    public void setSequenceFieldPrototype(Field sequenceFieldPrototype) {
        this.sequenceFieldPrototype = sequenceFieldPrototype;
    }

    /**
     * {@code FieldGroup} instance to serve as a prototype for the actions column. For each
     * collection line this instance is copied and adjusted as necessary. Note the actual actions
     * for the group come from the collection groups actions List
     * (org.kuali.rice.krad.uif.container.CollectionGroup.getActions()). The FieldGroup prototype is
     * useful for setting styling of the actions column and for the layout of the action fields.
     * Note also the label associated with the prototype is used for the action column header
     * 
     * @return GroupField instance
     */
    @BeanTagAttribute(name = "actionFieldPrototype", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public FieldGroup getActionFieldPrototype() {
        return this.actionFieldPrototype;
    }

    /**
     * Setter for the action field prototype
     * 
     * @param actionFieldPrototype
     */
    public void setActionFieldPrototype(FieldGroup actionFieldPrototype) {
        this.actionFieldPrototype = actionFieldPrototype;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.CollectionLayoutManager#getSubCollectionFieldGroupPrototype()
     */
    @BeanTagAttribute(name = "subCollectionFieldGroupPrototype", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public FieldGroup getSubCollectionFieldGroupPrototype() {
        return this.subCollectionFieldGroupPrototype;
    }

    /**
     * Setter for the sub-collection field group prototype
     * 
     * @param subCollectionFieldGroupPrototype
     */
    public void setSubCollectionFieldGroupPrototype(FieldGroup subCollectionFieldGroupPrototype) {
        this.subCollectionFieldGroupPrototype = subCollectionFieldGroupPrototype;
    }

    /**
     * Field instance that serves as a prototype for creating the select field on each line when
     * {@link org.kuali.rice.krad.uif.container.CollectionGroup#isIncludeLineSelectionField()} is
     * true
     * 
     * <p>
     * This prototype can be used to set the control used for the select field (generally will be a
     * checkbox control) in addition to styling and other setting. The binding path will be formed
     * with using the
     * {@link org.kuali.rice.krad.uif.container.CollectionGroup#getLineSelectPropertyName()} or if
     * not set the framework will use
     * {@link org.kuali.rice.krad.web.form.UifFormBase#getSelectedCollectionLines()}
     * </p>
     * 
     * @return select field prototype instance
     */
    @BeanTagAttribute(name = "selectFieldPrototype", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Field getSelectFieldPrototype() {
        return selectFieldPrototype;
    }

    /**
     * Setter for the prototype instance for select fields
     * 
     * @param selectFieldPrototype
     */
    public void setSelectFieldPrototype(Field selectFieldPrototype) {
        this.selectFieldPrototype = selectFieldPrototype;
    }

    /**
     * Indicates whether the add line should be rendered in a separate group, or as part of the
     * table (first line)
     * 
     * <p>
     * When separate add line is enabled, the fields for the add line will be placed in the
     * {@link #getAddLineGroup()}. This group can be used to configure the add line presentation. In
     * addition to the fields, the header on the group (unless already set) will be set to
     * {@link org.kuali.rice.krad.uif.container.CollectionGroup#getAddLabel()} and the add line
     * actions will be placed into the group's footer.
     * </p>
     * 
     * @return true if add line should be separated, false if it should be placed into the table
     */
    @BeanTagAttribute(name = "separateAddLine")
    public boolean isSeparateAddLine() {
        return separateAddLine;
    }

    /**
     * Setter for the separate add line indicator
     * 
     * @param separateAddLine
     */
    public void setSeparateAddLine(boolean separateAddLine) {
        this.separateAddLine = separateAddLine;
    }

    /**
     * When {@link #isSeparateAddLine()} is true, this group will be used to render the add line
     * 
     * <p>
     * This group can be used to configure how the add line will be rendered. For example the layout
     * manager configured on the group will be used to rendered the add line fields. If the header
     * (title) is not set on the group, it will be set from
     * {@link org.kuali.rice.krad.uif.container.CollectionGroup#getAddLabel()}. In addition,
     * {@link org.kuali.rice.krad.uif.container.CollectionGroup#getAddLineActions()} will be added
     * to the group footer items.
     * </p>
     * 
     * @return Group instance for the collection add line
     */
    @BeanTagAttribute(name = "addLineGroup", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Group getAddLineGroup() {
        return addLineGroup;
    }

    /**
     * Setter for the add line Group
     * 
     * @param addLineGroup
     */
    public void setAddLineGroup(Group addLineGroup) {
        this.addLineGroup = addLineGroup;
    }

    /**
     * List of {@link Field} instances that make up all the table's rows of data
     * 
     * @return table body fields
     */
    public List<Field> getAllRowFields() {
        return this.allRowFields;
    }

    /**
     * List of {@link Field} instances that make us the table's first row of data
     * 
     * @return list of field instances
     */
    public List<Field> getFirstRowFields() {
        return firstRowFields;
    }

    /**
     * The Pager widget for this TableLayoutManager which defines settings for paging
     * 
     * <p>
     * The settings in this widget are only used by TableLayoutManagers which DO NOT take advantage
     * of the RichTable option (this has its own paging implementation). To turn off RichTable and
     * use a basic table with server paging set richTable.render="false" and useServerPaging="true"
     * on the CollectionGroup which uses this layout manager.
     * </p>
     * 
     * @return the Pager widget
     */
    public Pager getPagerWidget() {
        return pagerWidget;
    }

    /**
     * Set the Pager widget
     * 
     * @param pagerWidget
     */
    public void setPagerWidget(Pager pagerWidget) {
        this.pagerWidget = pagerWidget;
    }

    /**
     * Widget associated with the table to add functionality such as sorting, paging, and export
     * 
     * @return RichTable instance
     */
    @BeanTagAttribute(name = "richTable", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public RichTable getRichTable() {
        return this.richTable;
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
     * @return the numberOfDataColumns
     */
    @BeanTagAttribute(name = "numberOfDataColumns")
    public int getNumberOfDataColumns() {
        return this.numberOfDataColumns;
    }

    /**
     * @param numberOfDataColumns the numberOfDataColumns to set
     */
    public void setNumberOfDataColumns(int numberOfDataColumns) {
        this.numberOfDataColumns = numberOfDataColumns;
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
     * @see 
     *      org.kuali.rice.krad.uif.widget.RichTable#setHiddenColumns(java.util.Set<java.lang.String>
     *      )
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
     * @see 
     *      org.kuali.rice.krad.uif.widget.RichTable#setSortableColumns(java.util.Set<java.lang.String
     *      >)
     */
    public void setSortableColumns(Set<String> sortableColumns) {
        if (richTable != null) {
            richTable.setSortableColumns(sortableColumns);
        }
    }

    /**
     * Indicates the index of the action column
     * 
     * @return the action column index
     */
    @BeanTagAttribute(name = "actionColumnIndex")
    public int getActionColumnIndex() {
        return actionColumnIndex;
    }

    /**
     * Indicates the actions column placement
     * 
     * <p>
     * Valid values are 'LEFT', 'RIGHT' or any valid number. The default is 'RIGHT' or '-1'. The
     * column placement index takes all displayed columns, including sequence and selection columns,
     * into account.
     * </p>
     * 
     * @return the action column placement
     */
    @BeanTagAttribute(name = "actionColumnPlacement")
    public String getActionColumnPlacement() {
        return actionColumnPlacement;
    }

    /**
     * Setter for the action column placement
     * 
     * @param actionColumnPlacement action column placement string
     */
    public void setActionColumnPlacement(String actionColumnPlacement) {
        this.actionColumnPlacement = actionColumnPlacement;

        if ("LEFT".equals(actionColumnPlacement)) {
            actionColumnIndex = 1;
        } else if ("RIGHT".equals(actionColumnPlacement)) {
            actionColumnIndex = -1;
        } else if (StringUtils.isNumeric(actionColumnPlacement)) {
            actionColumnIndex = Integer.parseInt(actionColumnPlacement);
        }
    }

    /**
     * The row details info group to use when using a TableLayoutManager with the a richTable.
     * 
     * <p>
     * This group will be displayed when the user clicks the "Details" link/image on a row. This
     * allows extra/long data to be hidden in table rows and then revealed during interaction with
     * the table without the need to leave the page. Allows for any group content.
     * </p>
     * 
     * <p>
     * Does not currently work with javascript required content.
     * </p>
     * 
     * @return rowDetailsGroup component
     */
    @BeanTagAttribute(name = "rowDetailsGroup", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Group getRowDetailsGroup() {
        return rowDetailsGroup;
    }

    /**
     * Set the row details info group
     * 
     * @param rowDetailsGroup row details group
     */
    public void setRowDetailsGroup(Group rowDetailsGroup) {
        this.rowDetailsGroup = rowDetailsGroup;
    }

    /**
     * Creates the details group for the line using the information setup through the setter methods
     * of this interface. Line details are currently only supported in TableLayoutManagers which use
     * richTable.
     * 
     * @param collectionGroup the CollectionGroup for this TableLayoutManager
     * @param view the current view
     */
    public void setupDetails(CollectionGroup collectionGroup, View view) {
        if (getRowDetailsGroup() == null || this.getRichTable() == null || !this.getRichTable().isRender()) {
            return;
        }

        //data attribute to mark this group to open itself when rendered
        collectionGroup.addDataAttribute(UifConstants.DataAttributes.DETAILS_DEFAULT_OPEN, Boolean.toString(
                this.rowDetailsOpen));

        toggleAllDetailsAction.addDataAttribute("open", Boolean.toString(this.rowDetailsOpen));
        toggleAllDetailsAction.addDataAttribute("tableid", this.getId());

        this.getRowDetailsGroup().setHidden(true);

        FieldGroup detailsFieldGroup = ComponentFactory.getFieldGroup();

        TreeMap<String, String> dataAttributes = new TreeMap<String, String>();
        dataAttributes.put(UifConstants.DataAttributes.ROLE, "detailsFieldGroup");
        detailsFieldGroup.setDataAttributes(dataAttributes);

        Action rowDetailsAction = this.getExpandDetailsActionPrototype();
        rowDetailsAction.addDataAttribute(UifConstants.DataAttributes.ROLE, "detailsLink");
        rowDetailsAction.setId(collectionGroup.getId() + UifConstants.IdSuffixes.DETAIL_LINK);

        List<Component> detailsItems = new ArrayList<Component>();
        detailsItems.add(rowDetailsAction);

        dataAttributes = new TreeMap<String, String>();
        dataAttributes.put("role", "details");
        dataAttributes.put("open", Boolean.toString(this.rowDetailsOpen));
        this.getRowDetailsGroup().setDataAttributes(dataAttributes);

        if (ajaxDetailsRetrieval) {
            this.getRowDetailsGroup().setRender(false);
            this.getRowDetailsGroup().setDisclosedByAction(true);
        }

        detailsItems.add(getRowDetailsGroup());
        detailsFieldGroup.setItems(detailsItems);
        detailsFieldGroup.setId(collectionGroup.getId() + UifConstants.IdSuffixes.DETAIL_GROUP);
        view.assignComponentIds(detailsFieldGroup);

        List<Component> theItems = new ArrayList<Component>();
        theItems.add(detailsFieldGroup);
        theItems.addAll(collectionGroup.getItems());

        collectionGroup.setItems(theItems);
    }

    /**
     * A list of all the columns to be calculated
     * 
     * <p>
     * The list must contain valid column indexes. The indexes takes all displayed columns into
     * account.
     * </p>
     * 
     * @return the total columns list
     */
    public List<String> getColumnsToCalculate() {
        return columnsToCalculate;
    }

    /**
     * Validates different requirements of component compiling a series of reports detailing information on errors
     * found in the component.  Used by the RiceDictionaryValidator.
     *
     * @param tracer record of component's location
     * @return a list of ErrorReports detailing errors found within the component and referenced within it
     */
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean("TableLayoutManager", getId());

        if (getRowDetailsGroup() != null) {
            boolean validTable = false;
            if (getRichTable() != null) {
                if (getRichTable().isRender()) {
                    validTable = true;
                }
            }
            if (!validTable) {
                String currentValues[] = {"rowDetailsGroup =" + getRowDetailsGroup(), "richTable =" + getRichTable()};
                tracer.createError("If rowDetailsGroup is set richTable must be set and its render true",
                        currentValues);
            }

        }
    }

    /**
     * Gets showTotal. showTotal shows/calculates the total field when true, otherwise it is not rendered.
     * <br/>
     * <b>Only used when renderOnlyLeftTotalLabels is TRUE, this overrides the ColumnConfigurationInfo setting.
     * Otherwise, the ColumnConfigurationInfo setting takes precedence.</b>
     *
     * @return true if showing the total, false otherwise.
     */
    @BeanTagAttribute(name = "showTotal")
    public boolean isShowTotal() {
        return showTotal;
    }

    /**
     * Sets showTotal. showTotal shows/calculates the total field when true, otherwise it is not
     * rendered. <br/>
     * <b>Only used when renderOnlyLeftTotalLabels is TRUE, this overrides the
     * ColumnConfigurationInfo setting. Otherwise, the ColumnConfigurationInfo setting takes
     * precedence.</b>
     * 
     * @param showTotal
     */
    public void setShowTotal(boolean showTotal) {
        this.showTotal = showTotal;
    }

    /**
     * Gets showTotal. showTotal shows/calculates the total field when true, otherwise it is not
     * rendered. <br/>
     * <b>Only used when renderOnlyLeftTotalLabels is TRUE, this overrides the
     * ColumnConfigurationInfo setting. Otherwise, the ColumnConfigurationInfo setting takes
     * precedence.</b>
     * 
     * @return true if showing the page total, false otherwise.
     */
    @BeanTagAttribute(name = "showPageTotal")
    public boolean isShowPageTotal() {
        return showPageTotal;
    }

    /**
     * Sets showPageTotal. showPageTotal shows/calculates the total field for the page when true
     * (and only when the table actually has pages), otherwise it is not rendered. <br/>
     * <b>Only used when renderOnlyLeftTotalLabels is TRUE, this overrides the
     * ColumnConfigurationInfo setting. Otherwise, the ColumnConfigurationInfo setting takes
     * precedence.</b>
     * 
     * @param showPageTotal
     */
    public void setShowPageTotal(boolean showPageTotal) {
        this.showPageTotal = showPageTotal;
    }

    /**
     * Gets showGroupTotal. showGroupTotal shows/calculates the total field for each grouping when
     * true (and only when the table actually has grouping turned on), otherwise it is not rendered. <br/>
     * <b>Only used when renderOnlyLeftTotalLabels is TRUE, this overrides the
     * ColumnConfigurationInfo setting. Otherwise, the ColumnConfigurationInfo setting takes
     * precedence.</b>
     * 
     * @return true if showing the group total, false otherwise.
     */
    @BeanTagAttribute(name = "showGroupTotal")
    public boolean isShowGroupTotal() {
        return showGroupTotal;
    }

    /**
     * Sets showGroupTotal. showGroupTotal shows/calculates the total field for each grouping when
     * true (and only when the table actually has grouping turned on), otherwise it is not rendered. <br/>
     * <b>Only used when renderOnlyLeftTotalLabels is TRUE, this overrides the
     * ColumnConfigurationInfo setting. Otherwise, the ColumnConfigurationInfo setting takes
     * precedence.</b>
     * 
     * @param showGroupTotal
     */
    public void setShowGroupTotal(boolean showGroupTotal) {
        this.showGroupTotal = showGroupTotal;
    }

    /**
     * The total label to use when renderOnlyLeftTotalLabels is TRUE for total. This label will
     * appear in the left most column.
     * 
     * @return the totalLabel
     */
    @BeanTagAttribute(name = "totalLabel", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Label getTotalLabel() {
        return totalLabel;
    }

    /**
     * Sets the total label to use when renderOnlyLeftTotalLabels is TRUE for total.
     * 
     * @param totalLabel
     */
    public void setTotalLabel(Label totalLabel) {
        this.totalLabel = totalLabel;
    }

    /**
     * The pageTotal label to use when renderOnlyLeftTotalLabels is TRUE for total. This label will
     * appear in the left most column.
     * 
     * @return the totalLabel
     */
    @BeanTagAttribute(name = "pageTotalLabel", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Label getPageTotalLabel() {
        return pageTotalLabel;
    }

    /**
     * Sets the pageTotal label to use when renderOnlyLeftTotalLabels is TRUE for total.
     * 
     * @param pageTotalLabel
     */
    public void setPageTotalLabel(Label pageTotalLabel) {
        this.pageTotalLabel = pageTotalLabel;
    }

    /**
     * The groupTotal label to use when renderOnlyLeftTotalLabels is TRUE. This label will appear in
     * the left most column.
     * 
     * @return the totalLabel
     */
    @BeanTagAttribute(name = "groupTotalLabelPrototype", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Label getGroupTotalLabelPrototype() {
        return groupTotalLabelPrototype;
    }

    /**
     * Sets the groupTotal label to use when renderOnlyLeftTotalLabels is TRUE.
     * 
     * @param groupTotalLabelPrototype
     */
    public void setGroupTotalLabelPrototype(Label groupTotalLabelPrototype) {
        this.groupTotalLabelPrototype = groupTotalLabelPrototype;
    }

    /**
     * Gets the column calculations. This is a list of ColumnCalcuationInfo that when set provides
     * calculations to be performed on the columns they specify. These calculations appear in the
     * table's footer. This feature is only available when using richTable functionality.
     * 
     * @return the columnCalculations to use
     */
    @BeanTagAttribute(name = "columnCalculations", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<ColumnCalculationInfo> getColumnCalculations() {
        return columnCalculations;
    }

    /**
     * Sets the columnCalculations.
     * 
     * @param columnCalculations
     */
    public void setColumnCalculations(List<ColumnCalculationInfo> columnCalculations) {
        this.columnCalculations = columnCalculations;
    }

    /**
     * When true, labels for the totals fields will only appear in the left most column. Showing of
     * the totals is controlled by the settings on the TableLayoutManager itself when this property
     * is true.
     * 
     * @return true when rendering totals footer labels in the left-most column, false otherwise
     */
    @BeanTagAttribute(name = "renderOnlyLeftTotalLabels")
    public boolean isRenderOnlyLeftTotalLabels() {
        return renderOnlyLeftTotalLabels;
    }

    /**
     * Set the renderOnlyLeftTotalLabels flag for rendring total labels in the left-most column
     * 
     * @param renderOnlyLeftTotalLabels
     */
    public void setRenderOnlyLeftTotalLabels(boolean renderOnlyLeftTotalLabels) {
        this.renderOnlyLeftTotalLabels = renderOnlyLeftTotalLabels;
    }

    /**
     * Gets the footer calculation components to be used by the layout. These are set by the
     * framework and cannot be set directly.
     * 
     * @return the list of components for the footer
     */
    public List<Component> getFooterCalculationComponents() {
        return footerCalculationComponents;
    }

    /**
     * Gets the list of property names to use for grouping.
     * 
     * <p>
     * When this property is set, grouping for this collection will be enabled and the lines of the
     * collection will be grouped by the propertyName(s) supplied. Supplying multiple property names
     * will cause the grouping to be on multiple fields and ordered alphabetically on
     * "propetyValue1, propertyValue2" (this is also how the group title will display for each
     * group). The property names supplied must be relative to the line, so #lp SHOULD NOT be used
     * (it is assumed automatically).
     * </p>
     * 
     * @return propertyNames to group on
     */
    @BeanTagAttribute(name = "groupingPropertyNames", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getGroupingPropertyNames() {
        return groupingPropertyNames;
    }

    /**
     * Sets the list of property names to use for grouping.
     * 
     * @param groupingPropertyNames
     */
    public void setGroupingPropertyNames(List<String> groupingPropertyNames) {
        this.groupingPropertyNames = groupingPropertyNames;
    }

    /**
     * Get the groupingTitle. The groupingTitle MUST contain a SpringEL expression to uniquely
     * identify a group's line (ie it cannot be a static string because each group must be
     * identified by some value). <b>This overrides groupingPropertyNames(if set) because it
     * provides full control of grouping value used by the collection. SpringEL defined here must
     * use #lp if referencing values of the line.</b>
     * 
     * @return groupingTitle to be used
     */
    @BeanTagAttribute(name = "groupingTitle")
    public String getGroupingTitle() {
        return groupingTitle;
    }

    /**
     * Set the groupingTitle. This will throw an exception if the title does not contain a SpringEL
     * expression.
     * 
     * @param groupingTitle
     */
    public void setGroupingTitle(String groupingTitle) {
        if (groupingTitle != null && !groupingTitle.contains("@{")) {
            throw new RuntimeException("groupingTitle MUST contain a springEL expression to uniquely"
                    + " identify a collection group (often related to some value of the line). "
                    + "Value provided: "
                    + this.getGroupingTitle());
        }
        this.groupingTitle = groupingTitle;
    }

    /**
     * Get the groupingPrefix. The groupingPrefix is used to prefix the generated title (not used
     * when groupingTitle is set directly) when using groupingPropertyNames.
     * 
     * @return String
     */
    @BeanTagAttribute(name = "groupingPrefix")
    public String getGroupingPrefix() {
        return groupingPrefix;
    }

    /**
     * Set the groupingPrefix. This is not used when groupingTitle is set directly.
     * 
     * @param groupingPrefix
     */
    public void setGroupingPrefix(String groupingPrefix) {
        this.groupingPrefix = groupingPrefix;
    }

    /**
     * If true, all details will be opened by default when the table loads. Can only be used on
     * tables that have row details setup.
     * 
     * @return true if row details
     */
    public boolean isRowDetailsOpen() {
        return rowDetailsOpen;
    }

    /**
     * Set if row details should be open on table load
     * 
     * @param rowDetailsOpen
     */
    public void setRowDetailsOpen(boolean rowDetailsOpen) {
        this.rowDetailsOpen = rowDetailsOpen;
    }

    /**
     * If true, the toggleAllDetailsAction will be shown. This button allows all details to be
     * open/closed simultaneously.
     * 
     * @return true if the action button to toggle all row details opened/closed
     */
    public boolean isShowToggleAllDetails() {
        return showToggleAllDetails;
    }

    /**
     * Set if the toggleAllDetailsAction should be shown
     * 
     * @param showToggleAllDetails
     */
    public void setShowToggleAllDetails(boolean showToggleAllDetails) {
        this.showToggleAllDetails = showToggleAllDetails;
    }

    /**
     * The toggleAllDetailsAction action component used to toggle all row details open/closed. This
     * property is set by the default configuration and should not be reset in most cases.
     * 
     * @return Action component to use for the toggle action button
     */
    public Action getToggleAllDetailsAction() {
        return toggleAllDetailsAction;
    }

    /**
     * Set the toggleAllDetailsAction action component used to toggle all row details open/closed.
     * This property is set by the default configuration and should not be reset in most cases.
     * 
     * @param toggleAllDetailsAction
     */
    public void setToggleAllDetailsAction(Action toggleAllDetailsAction) {
        this.toggleAllDetailsAction = toggleAllDetailsAction;
    }

    /**
     * If true, when a row details open action is performed, it will get the details content from
     * the server the first time it is opened. The methodToCall will be a component "refresh" call
     * by default (this can be set on expandDetailsActionPrototype) and the additional action
     * parameters sent to the server will be those set on the expandDetailsActionPrototype
     * (lineIndex will be sent by default).
     * 
     * @return true if ajax row details retrieval will be used
     */
    public boolean isAjaxDetailsRetrieval() {
        return ajaxDetailsRetrieval;
    }

    /**
     * Set if row details content should be retrieved fromt he server
     * 
     * @param ajaxDetailsRetrieval
     */
    public void setAjaxDetailsRetrieval(boolean ajaxDetailsRetrieval) {
        this.ajaxDetailsRetrieval = ajaxDetailsRetrieval;
    }

    /**
     * The Action prototype used for the row details expand link. Should be set to
     * "Uif-ExpandDetailsAction" or "Uif-ExpandDetailsImageAction". Properties can be configured to
     * allow for different methodToCall and actionParameters to be set for ajax row details
     * retrieval.
     * 
     * @return the Action details link prototype
     */
    public Action getExpandDetailsActionPrototype() {
        return expandDetailsActionPrototype;
    }

    /**
     * Gets the grouping column index
     * 
     * @return the grouping column index
     */
    public int getGroupingColumnIndex() {
        return groupingColumnIndex;
    }

    /**
     * Set the expand details Action prototype link
     * 
     * @param expandDetailsActionPrototype
     */
    public void setExpandDetailsActionPrototype(Action expandDetailsActionPrototype) {
        this.expandDetailsActionPrototype = expandDetailsActionPrototype;
    }

    /**
     * Set the header labels
     * 
     * @param headerLabels
     */
    public void setHeaderLabels(List<Label> headerLabels) {
        this.headerLabels = headerLabels;
    }

    /**
     * Set the row fields
     * 
     * @param allRowFields
     */
    public void setAllRowFields(List<Field> allRowFields) {
        this.allRowFields = allRowFields;
    }

    /**
     * Set the first row fields
     * 
     * @param firstRowFields
     */
    public void setFirstRowFields(List<Field> firstRowFields) {
        this.firstRowFields = firstRowFields;
    }

    /**
     * Set flag of whether a header is added
     * 
     * @param headerAdded
     */
    public void setHeaderAdded(boolean headerAdded) {
        this.headerAdded = headerAdded;
    }

    /**
     * Sets action column index
     * 
     * @param actionColumnIndex
     */
    public void setActionColumnIndex(int actionColumnIndex) {
        this.actionColumnIndex = actionColumnIndex;
    }

    /**
     * Set grouping column index
     * 
     * @param groupingColumnIndex
     */
    public void setGroupingColumnIndex(int groupingColumnIndex) {
        this.groupingColumnIndex = groupingColumnIndex;
    }

    /**
     * Set flag generate group total rows
     * 
     * @param generateGroupTotalRows
     */
    public void setGenerateGroupTotalRows(boolean generateGroupTotalRows) {
        this.generateGroupTotalRows = generateGroupTotalRows;
    }

    /**
     * Set columns to calculate
     * 
     * @param columnsToCalculate
     */
    public void setColumnsToCalculate(List<String> columnsToCalculate) {
        this.columnsToCalculate = columnsToCalculate;
    }

    /**
     * Set footer calculation components
     * 
     * @param footerCalculationComponents
     */
    public void setFooterCalculationComponents(List<Component> footerCalculationComponents) {
        this.footerCalculationComponents = footerCalculationComponents;
    }

    /**
     * The row css classes for the rows of this layout
     * 
     * <p>
     * To set a css class on all rows, use "all" as a key. To set a class for even rows, use "even"
     * as a key, for odd rows, use "odd". Use a one-based index to target a specific row by index.
     * SpringEL can be used as a key and the expression will be evaluated; if evaluated to true, the
     * class(es) specified will be applied.
     * </p>
     * 
     * @return a map which represents the css classes of the rows of this layout
     */
    @BeanTagAttribute(name = "conditionalRowCssClasses", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getConditionalRowCssClasses() {
        return conditionalRowCssClasses;
    }

    /**
     * Set the conditionalRowCssClasses
     * 
     * @param conditionalRowCssClasses
     */
    public void setConditionalRowCssClasses(Map<String, String> conditionalRowCssClasses) {
        this.conditionalRowCssClasses = conditionalRowCssClasses;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T layoutManager) {
        super.copyProperties(layoutManager);
        TableLayoutManager tableLayoutManagerCopy = (TableLayoutManager) layoutManager;
        tableLayoutManagerCopy.setUseShortLabels(this.isUseShortLabels());
        tableLayoutManagerCopy.setRepeatHeader(this.isRepeatHeader());

        if (this.headerLabelPrototype != null) {
            tableLayoutManagerCopy.setHeaderLabelPrototype((Label) this.getHeaderLabelPrototype().copy());
        }

        tableLayoutManagerCopy.setRenderSequenceField(this.isRenderSequenceField());
        tableLayoutManagerCopy.setGenerateAutoSequence(this.isGenerateAutoSequence());

        if (this.sequenceFieldPrototype != null) {
            tableLayoutManagerCopy.setSequenceFieldPrototype((Field) this.getSequenceFieldPrototype().copy());
        }

        if (this.actionFieldPrototype != null) {
            tableLayoutManagerCopy.setActionFieldPrototype((FieldGroup) this.getActionFieldPrototype().copy());
        }

        if (this.subCollectionFieldGroupPrototype != null) {
            tableLayoutManagerCopy.setSubCollectionFieldGroupPrototype(
                    (FieldGroup) this.getSubCollectionFieldGroupPrototype().copy());
        }

        if (this.selectFieldPrototype != null) {
            tableLayoutManagerCopy.setSelectFieldPrototype((Field) this.getSelectFieldPrototype().copy());
        }

        tableLayoutManagerCopy.setSeparateAddLine(this.isSeparateAddLine());

        if (this.addLineGroup != null) {
            tableLayoutManagerCopy.setAddLineGroup((Group) this.getAddLineGroup().copy());
        }

        tableLayoutManagerCopy.setNumberOfDataColumns(this.numberOfDataColumns);

        if (this.headerLabels != null) {
            List<Label> headerLabelsCopy = Lists.newArrayListWithExpectedSize(this.headerLabels.size());
            for (Label headerLabel : headerLabels) {
                if (headerLabel != null) {
                    headerLabelsCopy.add((Label) headerLabel.copy());
                }
            }
            tableLayoutManagerCopy.setHeaderLabels(headerLabelsCopy);
        }

        if (this.allRowFields != null) {
            List<Field> allRowFieldsCopy = Lists.newArrayListWithExpectedSize(allRowFields.size());
            for (Field allRowField : allRowFields) {
                if (allRowField != null) {
                    allRowFieldsCopy.add((Field) allRowField.copy());
                }
            }
            tableLayoutManagerCopy.setAllRowFields(allRowFieldsCopy);
        }

        if (this.firstRowFields != null) {
            List<Field> firstRowFieldsCopy = Lists.newArrayListWithExpectedSize(firstRowFields.size());
            for (Field firstRowField : firstRowFields) {
                if (firstRowField != null) {
                    firstRowFieldsCopy.add((Field) firstRowField.copy());
                }
            }
            tableLayoutManagerCopy.setFirstRowFields(firstRowFieldsCopy);
        }

        if (this.pagerWidget != null) {
            tableLayoutManagerCopy.setPagerWidget((Pager)this.pagerWidget.copy());
        }

        if (this.richTable != null) {
            tableLayoutManagerCopy.setRichTable((RichTable) this.getRichTable().copy());
        }

        tableLayoutManagerCopy.setHeaderAdded(this.headerAdded);
        tableLayoutManagerCopy.setActionColumnIndex(this.getActionColumnIndex());

        if (this.rowDetailsGroup != null) {
            tableLayoutManagerCopy.setRowDetailsGroup((Group) this.getRowDetailsGroup().copy());
        }

        tableLayoutManagerCopy.setRowDetailsOpen(this.isRowDetailsOpen());
        tableLayoutManagerCopy.setShowToggleAllDetails(this.isShowToggleAllDetails());

        if (this.toggleAllDetailsAction != null) {
            tableLayoutManagerCopy.setToggleAllDetailsAction((Action) this.getToggleAllDetailsAction().copy());
        }

        tableLayoutManagerCopy.setAjaxDetailsRetrieval(this.isAjaxDetailsRetrieval());

        if (this.expandDetailsActionPrototype != null) {
            tableLayoutManagerCopy.setExpandDetailsActionPrototype(
                    (Action) this.getExpandDetailsActionPrototype().copy());
        }

        tableLayoutManagerCopy.setGroupingTitle(this.getGroupingTitle());
        tableLayoutManagerCopy.setGroupingPrefix(this.getGroupingPrefix());
        tableLayoutManagerCopy.setGroupingColumnIndex(this.getGroupingColumnIndex());

        if (this.groupingPropertyNames != null) {
            tableLayoutManagerCopy.setGroupingPropertyNames(new ArrayList<String>(groupingPropertyNames));
        }

        tableLayoutManagerCopy.setRenderOnlyLeftTotalLabels(this.isRenderOnlyLeftTotalLabels());
        tableLayoutManagerCopy.setShowTotal(this.isShowTotal());
        tableLayoutManagerCopy.setShowPageTotal(this.isShowPageTotal());
        tableLayoutManagerCopy.setShowGroupTotal(this.isShowGroupTotal());
        tableLayoutManagerCopy.setGenerateGroupTotalRows(this.generateGroupTotalRows);

        if (this.totalLabel != null) {
            tableLayoutManagerCopy.setTotalLabel((Label) this.getTotalLabel().copy());
        }

        if (this.pageTotalLabel != null) {
            tableLayoutManagerCopy.setPageTotalLabel((Label) this.getPageTotalLabel().copy());
        }

        if (this.groupTotalLabelPrototype != null) {
            tableLayoutManagerCopy.setGroupTotalLabelPrototype((Label) this.getGroupTotalLabelPrototype().copy());
        }

        if (this.columnsToCalculate != null) {
            tableLayoutManagerCopy.setColumnsToCalculate(new ArrayList<String>(columnsToCalculate));
        }

        if (this.columnCalculations != null) {
            List<ColumnCalculationInfo> columnCalculationsCopy = Lists.newArrayListWithExpectedSize(
                    columnCalculations.size());
            for (ColumnCalculationInfo columnCalculation : columnCalculations) {
                columnCalculationsCopy.add((ColumnCalculationInfo) columnCalculation.copy());
            }
            tableLayoutManagerCopy.setColumnCalculations(columnCalculationsCopy);
        }

        if (this.footerCalculationComponents != null) {
            List<Component> footerCalculationComponentsCopy = Lists.newArrayListWithExpectedSize(
                    footerCalculationComponents.size());
            for (Component footerCalculationComponent : footerCalculationComponents) {
                if (footerCalculationComponent != null) {
                    footerCalculationComponentsCopy.add((Component) footerCalculationComponent.copy());
                }
            }
            tableLayoutManagerCopy.setFooterCalculationComponents(footerCalculationComponentsCopy);
        }

        if (this.conditionalRowCssClasses != null) {
            tableLayoutManagerCopy.setConditionalRowCssClasses(new HashMap<String, String>(
                    this.conditionalRowCssClasses));
        }
    }
}

