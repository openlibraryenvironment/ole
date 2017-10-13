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
package org.kuali.rice.krad.uif.util;

import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;
import org.kuali.rice.krad.uif.field.MessageField;

import java.io.Serializable;

/**
 * ColumnCalculationInfo is used to specify which columns and what types of calculations are performed on those columns
 * of table collection.  This functionality can only be used when the dataTables plugin is being used
 * (richTable.render="true" for TableLayoutManager)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "columnCalculationInfo-bean", parent = "Uif-ColumnCalculationInfo"),
        @BeanTag(name = "columnCalculationInfo-sum-bean", parent = "Uif-ColumnCalculationInfo-Sum"),
        @BeanTag(name = "columnCalculationInfo-average-bean", parent = "Uif-ColumnCalculationInfo-Average"),
        @BeanTag(name = "columnCalculationInfo-max-bean", parent = "Uif-ColumnCalculationInfo-Max"),
        @BeanTag(name = "columnCalculationInfo-min-bean", parent = "Uif-ColumnCalculationInfo-Min")})
public class ColumnCalculationInfo extends UifDictionaryBeanBase implements Serializable {
    private static final long serialVersionUID = 148856717025808296L;
    private Integer columnNumber;
    private String propertyName;

    private boolean showTotal;
    private boolean showPageTotal;
    private boolean showGroupTotal;

    private MessageField totalField;
    private MessageField pageTotalField;
    private MessageField groupTotalFieldPrototype;

    private String calculationFunctionName;
    private String calculationFunctionExtraData;

    private boolean calculateOnKeyUp;
    private boolean recalculateTotalClientSide;

    /**
     * Gets the column number.  This should not be set through configuration as it is overridden by the
     * propertyName's caclculated column number.  <b>Do not set through xml configuration</b>
     *
     * @return columnNumber to perform calculations on
     */
    @BeanTagAttribute(name = "columnNumber")
    public Integer getColumnNumber() {
        return columnNumber;
    }

    /**
     * Sets the column number. <b>Do not set through xml configuration</b>
     *
     * @param columnNumber
     */
    public void setColumnNumber(Integer columnNumber) {
        this.columnNumber = columnNumber;
    }

    /**
     * Gets showTotal. showTotal shows/calculates the total field when true, otherwise it is not rendered.
     *
     * @return true if showing the total, false otherwise.
     */
    @BeanTagAttribute(name = "showTotal")
    public boolean isShowTotal() {
        return showTotal;
    }

    /**
     * Sets showTotal. showTotal shows/calculates the total field when true, otherwise it is not rendered.
     *
     * @param showTotal
     */
    public void setShowTotal(boolean showTotal) {
        this.showTotal = showTotal;
    }

    /**
     * Gets showTotal. showTotal shows/calculates the total field when true, otherwise it is not rendered.
     *
     * @return true if showing the page total, false otherwise.
     */
    @BeanTagAttribute(name = "showPageTotal")
    public boolean isShowPageTotal() {
        return showPageTotal;
    }

    /**
     * Sets showPageTotal. showPageTotal shows/calculates the total field for the page when true (and only
     * when the table actually has pages), otherwise it is not rendered.
     *
     * @param showPageTotal
     */
    public void setShowPageTotal(boolean showPageTotal) {
        this.showPageTotal = showPageTotal;
    }

    /**
     * Gets showGroupTotal. showGroupTotal shows/calculates the total field for each grouping when true (and only
     * when the table actually has grouping turned on), otherwise it is not rendered.
     *
     * @return true if showing the group total, false otherwise.
     */
    @BeanTagAttribute(name = "showGroupTotal")
    public boolean isShowGroupTotal() {
        return showGroupTotal;
    }

    /**
     * Sets showGroupTotal. showGroupTotal shows/calculates the total field for each grouping when true (and only
     * when the table actually has grouping turned on), otherwise it is not rendered.
     *
     * @param showGroupTotal
     */
    public void setShowGroupTotal(boolean showGroupTotal) {
        this.showGroupTotal = showGroupTotal;
    }

    /**
     * Gets the js calculationFunctionName.  This is the name of the js function to use in column calculations.
     *
     * <p>
     * <b>This must be ONLY the function by name (no parenthesis or params)</b><br/>
     * The actual js function declaration MUST take in an array of values as its first parameter.  The values passed in
     * will be all the relavant values for the calculation.  Optionally, the function can also take a second parameter
     * which can be specified by calculationFunctionExtraData.  This parameter can take any valid javascript value
     * (integer, string, JSON object, etc).  In either case, the values parameter MUST be the first parameter.
     * </p>
     *
     * @return calculatinoFunctionName to call for column calculations in js
     */
    @BeanTagAttribute(name = "calculationFunctionName")
    public String getCalculationFunctionName() {
        return calculationFunctionName;
    }

    /**
     * Sets the calculationFunctionName to call when doing column calculations
     *
     * @param calculationFunctionName
     */
    public void setCalculationFunctionName(String calculationFunctionName) {
        this.calculationFunctionName = calculationFunctionName;
    }

    /**
     * Gets the totalField.  This field is the field which holds the total for the column and specifies its label.
     * This SHOULD NOT BE SET except by the base bean (in MOST cases).
     *
     * @return the totalField
     */
    @BeanTagAttribute(name = "totalField", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public MessageField getTotalField() {
        return totalField;
    }

    /**
     * Sets the totalField.  This SHOULD NOT BE SET except by the base bean (in MOST cases).  Setting this property
     * without the appropriate settings WILL break functionality.
     *
     * @param totalField
     */
    public void setTotalField(MessageField totalField) {
        this.totalField = totalField;
    }

    /**
     * Gets the pageTotalField.  This field is the field which holds the pageTotal for the column
     * and specifies its label. This SHOULD NOT BE SET except by the base bean (in MOST cases).
     *
     * @return the pageTotalField
     */
    @BeanTagAttribute(name = "pageTotalField", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public MessageField getPageTotalField() {
        return pageTotalField;
    }

    /**
     * Sets the pageTotalField.  This SHOULD NOT BE SET except by the base bean (in MOST cases).  Setting this property
     * without the appropriate settings WILL break functionality.
     *
     * @param pageTotalField
     */
    public void setPageTotalField(MessageField pageTotalField) {
        this.pageTotalField = pageTotalField;
    }

    /**
     * Gets the groupTotalFieldPrototype.  This field is copied and holds the groupTotal for the column
     * and specifies its label. This SHOULD NOT BE SET except by the base bean (in MOST cases).
     *
     * @return the groupTotalFieldPrototype
     */
    @BeanTagAttribute(name = "groupTotalFieldPrototype", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public MessageField getGroupTotalFieldPrototype() {
        return groupTotalFieldPrototype;
    }

    /**
     * Sets the groupTotalFieldPrototype.  This SHOULD NOT BE SET except by the base bean (in MOST cases).
     * Setting this property without the appropriate settings WILL break functionality.
     *
     * @param groupTotalFieldPrototype
     */
    public void setGroupTotalFieldPrototype(MessageField groupTotalFieldPrototype) {
        this.groupTotalFieldPrototype = groupTotalFieldPrototype;
    }

    /**
     * If true, the column is calculated when the user enters a character on each key up.  There is a small delay
     * built in to prevent calculations from being fired for each key stroke.
     *
     * @return true if calculated the column on key up, false if calculating on change (default)
     */
    @BeanTagAttribute(name = "calculationOnKeyUp")
    public boolean isCalculateOnKeyUp() {
        return calculateOnKeyUp;
    }

    /**
     * Sets calculateOnKeyUp which controls the type of handler used
     *
     * @param calculateOnKeyUp
     */
    public void setCalculateOnKeyUp(boolean calculateOnKeyUp) {
        this.calculateOnKeyUp = calculateOnKeyUp;
    }

    /**
     * When set to false, calculations will not be fired for the totalField client-side.  This ONLY effects the
     * totalField.  If page and group totals are still shown, they will (and can only) be calculated client-side.
     *
     * <p>
     * To use this particular feature: set this to false, and use springEL in the totalField's message.messageText
     * to get a pre-calculated total from a field on the form.  This will be refreshed when the table is refreshed,
     * but will not be updated by client-side interactions - used for complex or special calculation mechanisms
     * that may require server only information.
     * </p>
     *
     * @return true if calculating the totalField client-side, false otherwise
     */
    @BeanTagAttribute(name = "recalculateTotalClientSide")
    public boolean isRecalculateTotalClientSide() {
        return recalculateTotalClientSide;
    }

    /**
     * Set the recalculateTotalClientSide flag
     *
     * @param recalculateTotalClientSide
     */
    public void setRecalculateTotalClientSide(boolean recalculateTotalClientSide) {
        this.recalculateTotalClientSide = recalculateTotalClientSide;
    }

    /**
     * This specifies extra data to be sent to the calculation function.  This can be any valid javascript value
     * (number, string, JSON - for passing multiple settings, etc).
     * <br/>
     * <b>The function specified by calculationFunctionName MUST take a second parameter when using this option.</b>
     *
     * @return the extra data to pass into the function specified by name in calculationFunctionName
     */
    @BeanTagAttribute(name = "calculationFunctionExtraData")
    public String getCalculationFunctionExtraData() {
        return calculationFunctionExtraData;
    }

    /**
     * Sets the calculationFunctionExtraData which specifies additional data to pass into the calculationFunction.
     *
     * @param calculationFunctionExtraData
     */
    public void setCalculationFunctionExtraData(String calculationFunctionExtraData) {
        this.calculationFunctionExtraData = calculationFunctionExtraData;
    }

    /**
     * Get the propertyName of the field to do calculations.  This field MUST exist as one of the fields
     * of the collection.  <b>This property must be set or an exception will be thrown.</b>
     *
     * @return propertyName of the field(the column) to do calculations on
     */
    @BeanTagAttribute(name = "propertyName")
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Set the propertyName of the field to do calculations on
     *
     * @param propertyName
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * @see org.kuali.rice.krad.datadictionary.DictionaryBeanBase#copy()
     */
    @Override
    protected <T> void copyProperties(T columnCalculationInfo) {
        ColumnCalculationInfo columnCalculationInfoCopy = (ColumnCalculationInfo) columnCalculationInfo;
        columnCalculationInfoCopy.setCalculateOnKeyUp(this.calculateOnKeyUp);
        columnCalculationInfoCopy.setCalculationFunctionExtraData(this.calculationFunctionExtraData);
        columnCalculationInfoCopy.setCalculationFunctionName(this.calculationFunctionName);
        columnCalculationInfoCopy.setColumnNumber(this.columnNumber);

        if (this.groupTotalFieldPrototype != null) {
            columnCalculationInfoCopy.setGroupTotalFieldPrototype((MessageField) this.groupTotalFieldPrototype.copy());
        }

        if (this.pageTotalField != null) {
            columnCalculationInfoCopy.setPageTotalField((MessageField) this.pageTotalField.copy());
        }

        columnCalculationInfoCopy.setPropertyName(this.propertyName);
        columnCalculationInfoCopy.setRecalculateTotalClientSide(this.recalculateTotalClientSide);
        columnCalculationInfoCopy.setShowGroupTotal(this.showGroupTotal);
        columnCalculationInfoCopy.setShowPageTotal(this.showPageTotal);
        columnCalculationInfoCopy.setShowTotal(this.showTotal);

        if (this.totalField != null) {
            columnCalculationInfoCopy.setTotalField((MessageField) this.totalField.copy());
        }
    }
}
