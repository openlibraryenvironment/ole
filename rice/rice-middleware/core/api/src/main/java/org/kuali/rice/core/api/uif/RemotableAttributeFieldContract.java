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
package org.kuali.rice.core.api.uif;

import java.util.Collection;

/**
 * This interface describes an attribute.  It can be considered the definition for an attribute.
 * It also contains preferred rendering instructions for an attribute. ie when rendering an attribute
 * in a user interface use this control with these widgets.
 */
public interface RemotableAttributeFieldContract {

    /**
     * The name of the attribute.  Cannot be null or blank.
     *
     * @return the name.
     */
    String getName();

    /**
     * The dataType of the attribute. Can be null.
     *
     * @return the datatype or null.
     */
    DataType getDataType();

    /**
     * The short label of the attribute. Can be null.
     *
     * @return the short label or null.
     */
    String getShortLabel();

    /**
     * The long label of the attribute. Can be null.
     *
     * @return the long label or null.
     */
    String getLongLabel();

    /**
     * The help summary of the attribute. Can be null.
     *
     * @return the help summary or null.
     */
    String getHelpSummary();

    /**
     * The help constraint of the attribute. Can be null.
     *
     * @return the help constraint or null.
     */
    String getConstraintText();

    /**
     * The help description of the attribute. Can be null.
     *
     * @return the help description or null.
     */
    String getHelpDescription();

    /**
     * Should the attribute always be in uppercase. Defaults to false.
     *
     * @return force uppercase.
     */
    boolean isForceUpperCase();

    /**
     * The inclusive minimum length of the attribute. Can be null. Cannot be less than 1.
     *
     * @return minimum length.
     */
    Integer getMinLength();

    /**
     * The inclusive maximum length of the attribute. Can be null. Cannot be less than 1.
     *
     * @return maximum length.
     */
    Integer getMaxLength();

    /**
     * The inclusive minimum value of the attribute. Can be null.
     *
     * @return minimum value.
     */
    Double getMinValue();

    /**
     * The inclusive maximum value of the attribute. Can be null.
     *
     * @return maximum value.
     */
    Double getMaxValue();

    /**
     * The regex constraint to apply to the attribute field for validation. Can be null.
     *
     * @return the constraint.
     */
    String getRegexConstraint();

    /**
     * The message to display if the regex constraint fails. Can be null.
     *
     * @return the constraint message.
     */
    String getRegexContraintMsg();

    /**
        * The name of the formatter
        *
        * @return the formatter name.
        */
       String getFormatterName();


    /**
     * Whether the attribute is a required attribute. Defaults to false.
     * @return whether the attribute is required.
     */
    boolean isRequired();

    /**
     * The default values for the attribute.  In the case where the "control" associated
     * with the attribute only allows a single default value then only one item in this list will be used.
     * Cannot be null.  Will always return an immutable list.
     *
     * @return collection of default values
     */
    Collection<String> getDefaultValues();

    /**
     * The control associated with the attribute.  Can be null.
     * @return the control.
     */
    RemotableControlContract getControl();

    /**
     * The widgets for the attribute. Will always return an immutable list.
     *
     * @return collection of widgets
     */
    Collection<? extends RemotableWidgetContract> getWidgets();

    /**
     * If this method returns a non-null value, it defines various settings for this attribute whenever
     * it is used on a lookup.
     *
     * @return the attribute range configuration for this attribute, or null if this attribute should
     * not be treated as a range in a lookup
     */
    AttributeLookupSettings getAttributeLookupSettings();

}
