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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.exception.RiceRuntimeException;
import org.kuali.rice.core.api.util.type.TypeUtils;
import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.bo.KualiCode;
import org.kuali.rice.krad.datadictionary.AttributeDefinition;
import org.kuali.rice.krad.datadictionary.mask.MaskFormatter;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.datadictionary.validator.Validator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.ComponentSecurity;
import org.kuali.rice.krad.uif.component.DataBinding;
import org.kuali.rice.krad.uif.component.KeepExpression;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.util.ViewModelUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.widget.Help;
import org.kuali.rice.krad.uif.widget.Helpable;
import org.kuali.rice.krad.uif.widget.Inquiry;
import org.kuali.rice.krad.uif.widget.Tooltip;
import org.kuali.rice.krad.util.KRADPropertyConstants;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.valuefinder.ValueFinder;

import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.List;

/**
 * Field that renders data from the application, such as the value of a data object property
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "dataField-bean", parent = "Uif-DataField"),
        @BeanTag(name = "dataField-labelTop-bean", parent = "Uif-DataField-LabelTop"),
        @BeanTag(name = "dataField-labelRight-bean", parent = "Uif-DataField-LabelRight"),
        @BeanTag(name = "dataField-withoutLabel-bean", parent = "Uif-DataField-withoutLabel")})
public class DataField extends FieldBase implements DataBinding, Helpable {
    private static final long serialVersionUID = -4129678891948564724L;

    // binding
    private String propertyName;
    private BindingInfo bindingInfo;

    private String dictionaryAttributeName;
    private String dictionaryObjectEntry;

    // value props
    private String defaultValue;
    private Class<? extends ValueFinder> defaultValueFinderClass;
    private Object[] defaultValues;
    private String forcedValue;

    private PropertyEditor propertyEditor;

    private boolean addHiddenWhenReadOnly;

    // read only display properties
    protected String readOnlyDisplayReplacementPropertyName;
    protected String readOnlyDisplaySuffixPropertyName;

    private String readOnlyDisplayReplacement;
    private String readOnlyDisplaySuffix;

    private String readOnlyListDisplayType;
    private String readOnlyListDelimiter;

    private boolean applyMask;
    private MaskFormatter maskFormatter;

    private List<String> additionalHiddenPropertyNames;
    private List<String> propertyNamesForAdditionalDisplay;

    private boolean escapeHtmlInPropertyValue;
    private boolean multiLineReadOnlyDisplay;

    // widgets
    private Inquiry inquiry;
    private boolean enableAutoInquiry;

    private Help help;

    public DataField() {
        super();

        enableAutoInquiry = true;
        escapeHtmlInPropertyValue = true;

        additionalHiddenPropertyNames = new ArrayList<String>();
        propertyNamesForAdditionalDisplay = new ArrayList<String>();
    }

    /**
     * The following initialization is performed:
     *
     * <ul>
     * <li>Set defaults for binding</li>
     * <li>Default the model path if not set</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        super.performInitialization(view, model);

        if (bindingInfo != null) {
            bindingInfo.setDefaults(view, getPropertyName());
        }
    }

    /**
     * The following updates are done here:
     *
     * <ul>
     * <li>If readOnlyHidden set to true, set field to readonly and add to hidden property names</li>
     * </ul>
     */
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        if (this.enableAutoInquiry && (this.inquiry == null) && isReadOnly()) {
            buildAutomaticInquiry(view, model, false);
        }

        if (isAddHiddenWhenReadOnly()) {
            setReadOnly(true);
            getAdditionalHiddenPropertyNames().add(getPropertyName());
        }
    }

    /**
     * The following actions are performed:
     *
     * <ul>
     * <li>Set the ids for the various attribute components</li>
     * <li>Sets up the client side validation for constraints on this field. In
     * addition, it sets up the messages applied to this field</li>
     * <li>If this field is of type list and readOnly, generates the appropriate readOnly output.  Handles other
     * readOnlyReplacement cases, as well.</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        // adjust the path for hidden fields
        // TODO: should this check the view#readOnly?
        List<String> hiddenPropertyPaths = new ArrayList<String>();
        for (String hiddenPropertyName : getAdditionalHiddenPropertyNames()) {
            String hiddenPropertyPath = getBindingInfo().getPropertyAdjustedBindingPath(hiddenPropertyName);
            hiddenPropertyPaths.add(hiddenPropertyPath);
        }
        this.additionalHiddenPropertyNames = hiddenPropertyPaths;

        // adjust paths on informational property names
        List<String> informationalPropertyPaths = new ArrayList<String>();
        for (String infoPropertyName : getPropertyNamesForAdditionalDisplay()) {
            String infoPropertyPath = getBindingInfo().getPropertyAdjustedBindingPath(infoPropertyName);
            informationalPropertyPaths.add(infoPropertyPath);
        }
        this.propertyNamesForAdditionalDisplay = informationalPropertyPaths;

        //Special processing for List<?> readOnly
        Class<?> type = ObjectPropertyUtils.getPropertyType(model, getBindingInfo().getBindingPath());
        if (this.isReadOnly() && type != null && List.class.isAssignableFrom(type) && StringUtils.isBlank(
                getReadOnlyDisplayReplacement()) && StringUtils.isBlank(getReadOnlyDisplayReplacementPropertyName())) {
            //get the list
            Object fieldValue = ObjectPropertyUtils.getPropertyValue(model, getBindingInfo().getBindingPath());

            //check for null, empty or non-simple type content (not supported by DataField)
            if (fieldValue != null && fieldValue instanceof List<?> && !((List) fieldValue).isEmpty()) {
                List<?> list = (List<?>) fieldValue;
                processReadOnlyListDisplay(model, list);
            } else {
                this.setReadOnlyDisplayReplacement("&nbsp;");
            }

        } else {
            // Additional and Alternate display value
            setAlternateAndAdditionalDisplayValue(view, model);
        }

        if (this.getFieldLabel() != null && StringUtils.isNotBlank(this.getId())) {
            this.getFieldLabel().setLabelForComponentId(this.getId() + UifConstants.IdSuffixes.CONTROL);
        }
    }

    /**
     * Creates a new {@link org.kuali.rice.krad.uif.widget.Inquiry} and then invokes the lifecycle process for
     * the inquiry to determine if a relationship was found, if so the inquiry is assigned to the field
     *
     * @param view view instance being processed
     * @param model object containing the view data
     * @param enableDirectInquiry whether direct inquiry should be enabled if an inquiry is found
     */
    protected void buildAutomaticInquiry(View view, Object model, boolean enableDirectInquiry) {
        Inquiry autoInquiry = ComponentFactory.getInquiry();

        view.getViewHelperService().spawnSubLifecyle(view, model, autoInquiry, this, null, null);

        // if render flag is true, that means the inquiry was able to find a relationship
        if (autoInquiry.isRender()) {
            this.inquiry = autoInquiry;
        }
    }

    /**
     * This method is called when the list is readOnly as determined in DataField's performFinalize method.  This
     * method
     * should be overridden to perform any additional processing to the values before calling
     * generateReadOnlyListDisplayReplacement.  The default implementation calls it directly with the originalList.
     *
     * @param model the model
     * @param originalList originalList of values
     */
    protected void processReadOnlyListDisplay(Object model, List<?> originalList) {
        this.setReadOnlyDisplayReplacement(generateReadOnlyListDisplayReplacement(originalList));
    }

    /**
     * Generates the html to be used and sets the readOnlyDisplayReplacement for DataFields that contain lists and
     * do not have their own readOnlyDisplayReplacement defined.  The type of html generated is based on the options
     * set on readOnlyListDisplayType and readOnlyListDelimiter.
     *
     * @param list the list to be converted to readOnly html
     */
    protected String generateReadOnlyListDisplayReplacement(List<?> list) {
        String generatedHtml = "";

        //Default to delimited if nothing is set
        if (getReadOnlyListDisplayType() == null) {
            this.setReadOnlyListDisplayType(UifConstants.ReadOnlyListTypes.DELIMITED.name());
        }

        //begin generation setup
        if (getReadOnlyListDisplayType().equalsIgnoreCase(UifConstants.ReadOnlyListTypes.UL.name())) {
            generatedHtml = "<ul class='uif-readOnlyStringList'>";
        } else if (getReadOnlyListDisplayType().equalsIgnoreCase(UifConstants.ReadOnlyListTypes.OL.name())) {
            generatedHtml = "<ol class='uif-readOnlyStringList'>";
        } else if (getReadOnlyListDisplayType().equalsIgnoreCase(UifConstants.ReadOnlyListTypes.BREAK.name())) {
            setReadOnlyListDelimiter("<br/>");
        } else if (this.getReadOnlyListDelimiter() == null) {
            setReadOnlyListDelimiter(", ");
        }

        //iterate through each value
        for (Object value : list) {
            //if blank skip
            if (!TypeUtils.isSimpleType(value.getClass()) || StringUtils.isBlank(value.toString())) {
                continue;
            }

            //handle mask if any
            if (isApplyMask()) {
                value = getMaskFormatter().maskValue(value);
            }

            //TODO the value should use the formatted text property value we would expect to see instead of toString
            //two types - delimited and html list
            if (getReadOnlyListDisplayType().equalsIgnoreCase(UifConstants.ReadOnlyListTypes.UL.name())
                    || getReadOnlyListDisplayType().equalsIgnoreCase(UifConstants.ReadOnlyListTypes.OL.name())) {
                generatedHtml = generatedHtml + "<li>" + StringEscapeUtils.escapeHtml(value.toString()) + "</li>";
            } else {
                //no matching needed - delimited is always the fallback and break uses same logic
                generatedHtml = generatedHtml + StringEscapeUtils.escapeHtml(value.toString())
                        + this.getReadOnlyListDelimiter();
            }
        }

        //end the generation
        if (getReadOnlyListDisplayType().equalsIgnoreCase(UifConstants.ReadOnlyListTypes.UL.name())) {
            generatedHtml = generatedHtml + "</ul>";
        } else if (getReadOnlyListDisplayType().equalsIgnoreCase(UifConstants.ReadOnlyListTypes.OL.name())) {
            generatedHtml = generatedHtml + "</ol>";
        } else {
            generatedHtml = StringUtils.removeEnd(generatedHtml, this.getReadOnlyListDelimiter());
        }

        if (StringUtils.isNotBlank(generatedHtml)) {
            return generatedHtml;
        } else {
            //this must be done or the ftl will skip and throw error
            return "&nbsp;";
        }
    }

    /**
     * Sets alternate and additional property value for this field.
     *
     * <p>
     * If <code>AttributeSecurity</code> present in this field, make sure the current user has permission to view the
     * field value. If user doesn't have permission to view the value, mask the value as configured and set it
     * as alternate value for display. If security doesn't exists for this field but
     * <code>alternateDisplayPropertyName</code> present, get its value and format it based on that
     * fields formatting and set for display.
     * </p>
     *
     * <p>
     * For additional display value, if <code>AttributeSecurity</code> not present, sets the value if
     * <code>additionalDisplayPropertyName</code> present. If not present, check whether this field is a
     * <code>KualiCode</code> and get the relationship configured in the datadictionary file and set the name
     * additional display value which will be displayed along with the code. If additional display property not
     * present, check whether this field is has <code>MultiValueControlBase</code>. If yes, get the Label
     * for the value and set it as additional display value.
     * </p>
     *
     * @param view the current view instance
     * @param model model instance
     */
    protected void setAlternateAndAdditionalDisplayValue(View view, Object model) {
        // if alternate or additional display values set don't use property names
        if (StringUtils.isNotBlank(readOnlyDisplayReplacement) || StringUtils.isNotBlank(readOnlyDisplaySuffix)) {
            return;
        }

        // check whether field value needs to be masked, and if so apply masking as alternateDisplayValue
        if (isApplyMask()) {
            Object fieldValue = ObjectPropertyUtils.getPropertyValue(model, getBindingInfo().getBindingPath());
            readOnlyDisplayReplacement = getMaskFormatter().maskValue(fieldValue);

            return;
        }

        // if not read only, return without trying to set alternate and additional values
        if (!isReadOnly()) {
            return;
        }

        // if field is not secure, check for alternate and additional display properties
        if (StringUtils.isNotBlank(getReadOnlyDisplayReplacementPropertyName())) {
            String alternateDisplayPropertyPath = getBindingInfo().getPropertyAdjustedBindingPath(
                    getReadOnlyDisplayReplacementPropertyName());

            Object alternateFieldValue = ObjectPropertyUtils.getPropertyValue(model, alternateDisplayPropertyPath);
            if (alternateFieldValue != null) {
                // TODO: format by type
                readOnlyDisplayReplacement = alternateFieldValue.toString();
            }
        }

        // perform automatic translation for code references if enabled on view
        if (StringUtils.isBlank(getReadOnlyDisplaySuffixPropertyName()) && view.isTranslateCodesOnReadOnlyDisplay()) {
            // check for any relationship present for this field and it's of type KualiCode
            Class<?> parentObjectClass = ViewModelUtils.getParentObjectClassForMetadata(view, model, this);
            DataObjectRelationship relationship =
                    KRADServiceLocatorWeb.getDataObjectMetaDataService().getDataObjectRelationship(null,
                            parentObjectClass, getBindingInfo().getBindingName(), "", true, false, false);

            if (relationship != null
                    && getPropertyName().startsWith(relationship.getParentAttributeName())
                    && KualiCode.class.isAssignableFrom(relationship.getRelatedClass())) {
                readOnlyDisplaySuffixPropertyName =
                        relationship.getParentAttributeName() + "." + KRADPropertyConstants.NAME;
            }
        }

        // now check for an additional display property and if set get the value
        if (StringUtils.isNotBlank(getReadOnlyDisplaySuffixPropertyName())) {
            String additionalDisplayPropertyPath = getBindingInfo().getPropertyAdjustedBindingPath(
                    getReadOnlyDisplaySuffixPropertyName());

            Object additionalFieldValue = ObjectPropertyUtils.getPropertyValue(model, additionalDisplayPropertyPath);
            if (additionalFieldValue != null) {
                // TODO: format by type
                readOnlyDisplaySuffix = additionalFieldValue.toString();
            }
        }
    }

    /**
     * Defaults the properties of the <code>DataField</code> to the
     * corresponding properties of its <code>AttributeDefinition</code>
     * retrieved from the dictionary (if such an entry exists). If the field
     * already contains a value for a property, the definitions value is not
     * used.
     *
     * @param view view instance the field belongs to
     * @param attributeDefinition AttributeDefinition instance the property values should be
     * copied from
     */
    public void copyFromAttributeDefinition(View view, AttributeDefinition attributeDefinition) {
        // label
        if (StringUtils.isEmpty(getLabel())) {
            setLabel(attributeDefinition.getLabel());
        }

        // short label
        if (StringUtils.isEmpty(getShortLabel())) {
            setShortLabel(attributeDefinition.getShortLabel());
        }

        // security
        if (getDataFieldSecurity().getAttributeSecurity() == null) {
            getDataFieldSecurity().setAttributeSecurity(attributeDefinition.getAttributeSecurity());
        }

        // alternate property name
        if (getReadOnlyDisplayReplacementPropertyName() == null && StringUtils.isNotBlank(
                attributeDefinition.getAlternateDisplayAttributeName())) {
            setReadOnlyDisplayReplacementPropertyName(attributeDefinition.getAlternateDisplayAttributeName());
        }

        // additional property display name
        if (getReadOnlyDisplaySuffixPropertyName() == null && StringUtils.isNotBlank(
                attributeDefinition.getAdditionalDisplayAttributeName())) {
            setReadOnlyDisplaySuffixPropertyName(attributeDefinition.getAdditionalDisplayAttributeName());
        }

        // property editor
        if (getPropertyEditor() == null) {
            setPropertyEditor(attributeDefinition.getPropertyEditor());
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(inquiry);
        components.add(help);

        return components;
    }

    /**
     * Indicates whether the data field instance allows input, subclasses should override and set to
     * true if input is allowed
     *
     * @return true if input is allowed, false if read only
     */
    public boolean isInputAllowed() {
        return false;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.DataBinding#getPropertyName()
     */
    @BeanTagAttribute(name = "propertyName")
    public String getPropertyName() {
        return this.propertyName;
    }

    /**
     * Setter for the component's property name
     *
     * @param propertyName
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Performs formatting of the field value for display and then converting the value back to its
     * expected type from a string
     *
     * <p>
     * Note property editors exist and are already registered for the basic Java types and the
     * common Kuali types such as [@link KualiDecimal}. Registration with this property is only
     * needed for custom property editors
     * </p>
     *
     * @return PropertyEditor property editor instance to use for this field
     */
    @BeanTagAttribute(name = "propertyEditor", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public PropertyEditor getPropertyEditor() {
        return propertyEditor;
    }

    /**
     * Setter for the custom property editor to use for the field
     *
     * @param propertyEditor
     */
    public void setPropertyEditor(PropertyEditor propertyEditor) {
        this.propertyEditor = propertyEditor;
    }

    /**
     * Convenience setter for configuring a property editor by class
     *
     * @param propertyEditorClass
     */
    public void setPropertyEditorClass(Class<? extends PropertyEditor> propertyEditorClass) {
        this.propertyEditor = ObjectUtils.newInstance(propertyEditorClass);
    }

    /**
     * @see org.kuali.rice.krad.uif.component.DataBinding#getBindingInfo()
     */
    @BeanTagAttribute(name = "bindingInfo", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public BindingInfo getBindingInfo() {
        return this.bindingInfo;
    }

    /**
     * Setter for the field's binding info
     *
     * @param bindingInfo
     */
    public void setBindingInfo(BindingInfo bindingInfo) {
        this.bindingInfo = bindingInfo;
    }

    /**
     * Returns the full binding path (the path used in the name attribute of the input).
     * This differs from propertyName in that it uses BindingInfo to determine the path.
     *
     * @return full binding path name
     */
    public String getName() {
        return this.getBindingInfo().getBindingPath();
    }

    /**
     * Name of the attribute within the data dictionary the attribute field is
     * associated with
     *
     * <p>
     * During the initialize phase for the <code>View</code>, properties for
     * attribute fields are defaulted from a corresponding
     * <code>AttributeDefinition</code> in the data dictionary. Based on the
     * propertyName and parent object class the framework attempts will
     * determine the attribute definition that is associated with the field and
     * set this property. However this property can also be set in the fields
     * configuration to use another dictionary attribute.
     * </p>
     *
     * <p>
     * The attribute name is used along with the dictionary object entry to find
     * the <code>AttributeDefinition</code>
     * </p>
     *
     * @return attribute name
     */
    @BeanTagAttribute(name = "dictionaryAttributeName")
    public String getDictionaryAttributeName() {
        return this.dictionaryAttributeName;
    }

    /**
     * Setter for the dictionary attribute name
     *
     * @param dictionaryAttributeName
     */
    public void setDictionaryAttributeName(String dictionaryAttributeName) {
        this.dictionaryAttributeName = dictionaryAttributeName;
    }

    /**
     * Object entry name in the data dictionary the associated attribute is
     * apart of
     *
     * <p>
     * During the initialize phase for the <code>View</code>, properties for
     * attribute fields are defaulted from a corresponding
     * <code>AttributeDefinition</code> in the data dictionary. Based on the
     * parent object class the framework will determine the object entry for the
     * associated attribute. However the object entry can be set in the field's
     * configuration to use another object entry for the attribute
     * </p>
     *
     * <p>
     * The attribute name is used along with the dictionary object entry to find
     * the <code>AttributeDefinition</code>
     * </p>
     *
     * @return String
     */
    @BeanTagAttribute(name = "dictionaryObjectEntry")
    public String getDictionaryObjectEntry() {
        return this.dictionaryObjectEntry;
    }

    /**
     * Setter for the dictionary object entry
     *
     * @param dictionaryObjectEntry
     */
    public void setDictionaryObjectEntry(String dictionaryObjectEntry) {
        this.dictionaryObjectEntry = dictionaryObjectEntry;
    }

    /**
     * Default value for the model property the field points to
     *
     * <p>
     * When a new <code>View</code> instance is requested, the corresponding
     * model will be newly created. During this initialization process the value
     * for the model property will be set to the given default value, if it was null.
     * This will only work on properties which can be determined to be null.
     * Therefore a String property with an empty string value will
     * not be ovewritten with the defaultValue set here.
     * </p>
     *
     * <p>
     * In addition, int, boolean, and other primitive types
     * will not use this default value because they inherently have a value in Java (0 for int, false for boolean, etc).
     * To use such types either using a primitive wrapper type (Integer, Boolean, etc) so an unset variable can
     * be determined to be null, or explicitly set the default value on the form/object itself for these types and
     * not through this property.
     * </p>
     *
     * @return default value
     */
    @BeanTagAttribute(name = "defaultValue")
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Setter for the fields default value
     *
     * @param defaultValue
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gives Class that should be invoked to produce the default value for the
     * field
     *
     * @return default value finder class
     */
    @BeanTagAttribute(name = "defaultValueFinderClass")
    public Class<? extends ValueFinder> getDefaultValueFinderClass() {
        return this.defaultValueFinderClass;
    }

    /**
     * Setter for the default value finder class
     *
     * @param defaultValueFinderClass
     */
    public void setDefaultValueFinderClass(Class<? extends ValueFinder> defaultValueFinderClass) {
        this.defaultValueFinderClass = defaultValueFinderClass;
    }

    /**
     * Array of default values for the model property the field points to
     *
     * <p>
     * When a new <code>View</code> instance is requested, the corresponding
     * model will be newly created. During this initialization process the value
     * for the model property will be set to the given default values (if set)
     * </p>
     *
     * @return default value
     */
    @BeanTagAttribute(name = "defaultValues", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public Object[] getDefaultValues() {
        return this.defaultValues;
    }

    /**
     * Setter for the fields default values
     *
     * @param defaultValues
     */
    public void setDefaultValues(Object[] defaultValues) {
        this.defaultValues = defaultValues;
    }

    public String getForcedValue() {
        return forcedValue;
    }

    public void setForcedValue(String forcedValue) {
        this.forcedValue = forcedValue;
    }

    /**
     * Summary help text for the field
     *
     * @return summary help text
     */
    @BeanTagAttribute(name = "helpSummary")
    public String getHelpSummary() {
        return this.help.getTooltipHelpContent();
    }

    /**
     * Setter for the summary help text
     *
     * @param helpSummary
     */
    public void setHelpSummary(String helpSummary) {
        this.help.setTooltipHelpContent(helpSummary);
    }

    /**
     * Data Field Security object that indicates what authorization (permissions) exist for the field
     *
     * @return DataFieldSecurity instance
     */
    public DataFieldSecurity getDataFieldSecurity() {
        return (DataFieldSecurity) super.getComponentSecurity();
    }

    /**
     * Override to assert a {@link DataFieldSecurity} instance is set
     *
     * @param componentSecurity instance of DataFieldSecurity
     */
    @Override
    public void setComponentSecurity(ComponentSecurity componentSecurity) {
        if ((componentSecurity != null) && !(componentSecurity instanceof DataFieldSecurity)) {
            throw new RiceRuntimeException("Component security for DataField should be instance of DataFieldSecurity");
        }

        super.setComponentSecurity(componentSecurity);
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentSecurityClass()
     */
    @Override
    protected Class<? extends ComponentSecurity> getComponentSecurityClass() {
        return DataFieldSecurity.class;
    }

    /**
     * Indicates the field should be read-only but also a hidden should be generated for the field
     *
     * <p>
     * Useful for when a value is just displayed but is needed by script
     * </p>
     *
     * @return true if field should be readOnly hidden, false if not
     */
    @BeanTagAttribute(name = "addHiddenWhenReadOnly")
    public boolean isAddHiddenWhenReadOnly() {
        return addHiddenWhenReadOnly;
    }

    /**
     * Setter for the read-only hidden indicator
     *
     * @param addHiddenWhenReadOnly
     */
    public void setAddHiddenWhenReadOnly(boolean addHiddenWhenReadOnly) {
        this.addHiddenWhenReadOnly = addHiddenWhenReadOnly;
    }

    /**
     * Inquiry widget for the field
     *
     * <p>
     * The inquiry widget will render a link for the field value when read-only
     * that points to the associated inquiry view for the field. The inquiry can
     * be configured to point to a certain <code>InquiryView</code>, or the
     * framework will attempt to associate the field with a inquiry based on its
     * metadata (in particular its relationships in the model)
     * </p>
     *
     * @return Inquiry field inquiry
     */
    @BeanTagAttribute(name = "inquiry", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Inquiry getInquiry() {
        return this.inquiry;
    }

    /**
     * Setter for the inquiry widget
     *
     * @param inquiry
     */
    public void setInquiry(Inquiry inquiry) {
        this.inquiry = inquiry;
    }

    /**
     * Indicates whether inquiries should be automatically set when a relationship for the field's property
     * is found
     *
     * <p>
     * Note this only applies when the {@link #getInquiry()} widget has not been configured (is null)
     * and is set to true by default
     * </p>
     *
     * @return true if auto inquiries are enabled, false if not
     */
    public boolean isEnableAutoInquiry() {
        return enableAutoInquiry;
    }

    /**
     * Setter for enabling automatic inquiries
     *
     * @param enableAutoInquiry
     */
    public void setEnableAutoInquiry(boolean enableAutoInquiry) {
        this.enableAutoInquiry = enableAutoInquiry;
    }

    /**
     * Help configuration object for the datafield
     *
     * <p>
     * External help information can be configured for the datafield. The
     * <code>Help</code> object can the configuration for rendering a link to
     * that help information.
     * </p>
     *
     * @return Help for datafield
     */
    @Override
    @BeanTagAttribute(name = "help", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Help getHelp() {
        return this.help;
    }

    /**
     * Setter for the datafield help content
     *
     * @param help
     */
    @Override
    public void setHelp(Help help) {
        this.help = help;
    }

    /**
     * For data fields the help tooltip is placed on the label.
     *
     * @see org.kuali.rice.krad.uif.widget.Helpable#setTooltipOfComponent(org.kuali.rice.krad.uif.widget.Tooltip))
     */
    @Override
    @BeanTagAttribute(name = "tooltipOfComponent", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public void setTooltipOfComponent(Tooltip tooltip) {
        getFieldLabel().setToolTip(tooltip);
    }

    /**
     * Return the field label for the help title
     *
     * @return field label
     * @see org.kuali.rice.krad.uif.widget.Helpable#setTooltipOfComponent(org.kuali.rice.krad.uif.widget.Tooltip)
     */
    @Override
    public String getHelpTitle() {
        return this.getLabel();
    }

    /**
     * Additional display attribute name, which will be displayed next to the actual field value
     * when the field is readonly with hyphen in between like PropertyValue - AdditionalPropertyValue
     *
     * @param readOnlyDisplaySuffixPropertyName name of the additional display property
     */
    public void setReadOnlyDisplaySuffixPropertyName(String readOnlyDisplaySuffixPropertyName) {
        this.readOnlyDisplaySuffixPropertyName = readOnlyDisplaySuffixPropertyName;
    }

    /**
     * Returns the additional display attribute name to be displayed when the field is readonly
     *
     * @return additional display attribute name
     */
    @BeanTagAttribute(name = "readOnlyDisplaceSuffixPropertyName")
    public String getReadOnlyDisplaySuffixPropertyName() {
        return this.readOnlyDisplaySuffixPropertyName;
    }

    /**
     * Sets the alternate display attribute name to be displayed when the field is readonly.
     * This properties value will be displayed instead of actual fields value when the field is readonly.
     *
     * @param readOnlyDisplayReplacementPropertyName alternate display property name
     */
    public void setReadOnlyDisplayReplacementPropertyName(String readOnlyDisplayReplacementPropertyName) {
        this.readOnlyDisplayReplacementPropertyName = readOnlyDisplayReplacementPropertyName;
    }

    /**
     * Returns the alternate display attribute name to be displayed when the field is readonly.
     *
     * @return alternate Display Property Name
     */
    @BeanTagAttribute(name = "readOnlyDisplayReplacementPropertyName")
    public String getReadOnlyDisplayReplacementPropertyName() {
        return this.readOnlyDisplayReplacementPropertyName;
    }

    /**
     * Returns the alternate display value
     *
     * @return the alternate display value set for this field
     */
    @BeanTagAttribute(name = "readOnlyDisplayReplacement")
    public String getReadOnlyDisplayReplacement() {
        return readOnlyDisplayReplacement;
    }

    /**
     * Setter for the alternative display value
     *
     * @param value
     */
    public void setReadOnlyDisplayReplacement(String value) {
        this.readOnlyDisplayReplacement = value;
    }

    /**
     * Returns the additional display value.
     *
     * @return the additional display value set for this field
     */
    @BeanTagAttribute(name = "readOnlyDispalySuffix")
    public String getReadOnlyDisplaySuffix() {
        return readOnlyDisplaySuffix;
    }

    /**
     * Setter for the additional display value
     *
     * @param value
     */
    public void setReadOnlyDisplaySuffix(String value) {
        this.readOnlyDisplaySuffix = value;
    }

    /**
     * Gets the readOnlyListDisplayType.
     *
     * <p>When this is not set, the list will default to the delimited list display with a default of comma and space
     * (", ") - if readOnlyListDelimiter is not set as well.  The type can be set as the following:
     * <ul>
     * <li>"DELIMITED" - list will be output with delimiters between each item defined by readOnlyListDelimiter</li>
     * <li>"BREAK" - list will be output with breaks between each item</li>
     * <li>"OL" - list will be output in ordered list format (numbered)</li>
     * <li>"UL" - list will be output in unordered list format (bulleted)</li>
     * </ul>
     * </p>
     *
     * @return the display type to use
     */
    public String getReadOnlyListDisplayType() {
        return readOnlyListDisplayType;
    }

    /**
     * Set the readOnlyListDisplayType
     *
     * @param readOnlyListDisplayType
     */
    public void setReadOnlyListDisplayType(String readOnlyListDisplayType) {
        this.readOnlyListDisplayType = readOnlyListDisplayType;
    }

    /**
     * The readOnlyListDelimiter is used to set the delimiter used when "DELIMITED" type is set for
     * readOnlyListDisplayType
     *
     * @return the delimiter to use in readOnly list output with "DELIMITED" type set
     */
    public String getReadOnlyListDelimiter() {
        return readOnlyListDelimiter;
    }

    /**
     * Set the readOnlyListDelimiter
     *
     * @param readOnlyListDelimiter
     */
    public void setReadOnlyListDelimiter(String readOnlyListDelimiter) {
        this.readOnlyListDelimiter = readOnlyListDelimiter;
    }

    /**
     * Indicates whether the value for the field should be masked (or partially masked) on display
     *
     * <p>
     * If set to true, the field value will be masked by applying the configured {@link #getMaskFormatter()}
     * </p>
     *
     * <p>
     * If a KIM permission exists that should be checked to determine whether the value should be masked or not,
     * this value should not be set but instead the mask or partialMask property on {@link #getComponentSecurity()}
     * should be set to true. This indicates there is a mask permission that should be consulted. If the user
     * does not have the permission, this flag will be set to true by the framework and the value masked using
     * the mask formatter configured on the security object
     * </p>
     *
     * @return true if the field value should be masked, false if not
     */
    @BeanTagAttribute(name = "applyMask")
    public boolean isApplyMask() {
        return applyMask;
    }

    /**
     * Setter for the apply value mask flag
     *
     * @param applyMask
     */
    public void setApplyMask(boolean applyMask) {
        this.applyMask = applyMask;
    }

    /**
     * MaskFormatter instance that will be used to mask the field value when {@link #isApplyMask()} is true
     *
     * <p>
     * Note in cases where the mask is applied due to security (KIM permissions), the mask or partial mask formatter
     * configured on {@link #getComponentSecurity()} will be used instead of this mask formatter
     * </p>
     *
     * @return MaskFormatter instance
     */
    @BeanTagAttribute(name = "maskFormatter", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public MaskFormatter getMaskFormatter() {
        return maskFormatter;
    }

    /**
     * Setter for the MaskFormatter instance to apply when the value is masked
     *
     * @param maskFormatter
     */
    public void setMaskFormatter(MaskFormatter maskFormatter) {
        this.maskFormatter = maskFormatter;
    }

    /**
     * Allows specifying hidden property names without having to specify as a
     * field in the group config (that might impact layout)
     *
     * @return hidden property names
     */
    @BeanTagAttribute(name = "additionalHiddenPropertyNames", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getAdditionalHiddenPropertyNames() {
        return additionalHiddenPropertyNames;
    }

    /**
     * Setter for the hidden property names
     *
     * @param additionalHiddenPropertyNames
     */
    public void setAdditionalHiddenPropertyNames(List<String> additionalHiddenPropertyNames) {
        this.additionalHiddenPropertyNames = additionalHiddenPropertyNames;
    }

    /**
     * List of property names whose values should be displayed read-only under this field
     *
     * <p>
     * In the attribute field template for each information property name given its values is
     * outputted read-only. Informational property values can also be updated dynamically with
     * the use of field attribute query
     * </p>
     *
     * <p>
     * Simple property names can be given if the property has the same binding parent as this
     * field, in which case the binding path will be adjusted by the framework. If the property
     * names starts with org.kuali.rice.krad.uif.UifConstants#NO_BIND_ADJUST_PREFIX, no binding
     * prefix will be added.
     * </p>
     *
     * @return informational property names
     */
    @BeanTagAttribute(name = "propertyNamesForAdditionalDisplay", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getPropertyNamesForAdditionalDisplay() {
        return propertyNamesForAdditionalDisplay;
    }

    /**
     * Setter for the list of informational property names
     *
     * @param propertyNamesForAdditionalDisplay
     */
    public void setPropertyNamesForAdditionalDisplay(List<String> propertyNamesForAdditionalDisplay) {
        this.propertyNamesForAdditionalDisplay = propertyNamesForAdditionalDisplay;
    }

    /**
     * Sets HTML escaping for this property value. HTML escaping will be handled in alternate and additional fields
     * also.
     */
    public void setEscapeHtmlInPropertyValue(boolean escapeHtmlInPropertyValue) {
        this.escapeHtmlInPropertyValue = escapeHtmlInPropertyValue;
    }

    /**
     * Returns true if HTML escape allowed for this field
     *
     * @return true if escaping allowed
     */
    @BeanTagAttribute(name = "escapeHtmlInPropertyValue")
    public boolean isEscapeHtmlInPropertyValue() {
        return this.escapeHtmlInPropertyValue;
    }

    /**
     * Returns true if this field is of type {@code TextAreaControl}.
     *
     * <p>
     * Used to preserve text formatting in a textarea when the view
     * is readOnly by enclosing the text in a </pre> tag.
     * </p>
     *
     * @return true if the field is of type {@code TextAreaControl}
     */
    public boolean isMultiLineReadOnlyDisplay() {
        return multiLineReadOnlyDisplay;
    }

    /**
     * Setter for multiLineReadOnlyDisplay
     *
     * @param multiLineReadOnlyDisplay
     */
    public void setMultiLineReadOnlyDisplay(boolean multiLineReadOnlyDisplay) {
        this.multiLineReadOnlyDisplay = multiLineReadOnlyDisplay;
    }

    /**
     * Indicates whether the value for the field is secure
     *
     * <p>
     * A value will be secured if masking has been applied (by configuration or a failed KIM permission) or the field
     * has been marked as hidden due to an authorization check
     * </p>
     *
     * @return true if value is secure, false if not
     */
    public boolean hasSecureValue() {
        return isApplyMask() || ((getComponentSecurity().isViewAuthz()
                || getDataFieldSecurity().isViewInLineAuthz()
                || ((getDataFieldSecurity().getAttributeSecurity() != null) && getDataFieldSecurity()
                .getAttributeSecurity().isHide())) && isHidden());
    }

    public boolean isRenderFieldset() {
        return (!this.isReadOnly()
                && inquiry != null
                && inquiry.isRender()
                && inquiry.getInquiryLink() != null
                && inquiry.getInquiryLink().isRender()) || (help != null
                && help.isRender()
                && help.getHelpAction() != null
                && help.getHelpAction().isRender());
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean(this);

        // Checks that the property is connected to the field
        if (getPropertyName() == null) {
            if (!Validator.checkExpressions(this, "propertyName")) {
                String currentValues[] = {"propertyName = " + getPropertyName()};
                tracer.createError("Property name not set", currentValues);
            }
        }

        // Checks that the default values  present
/*        if (getDefaultValue() != null && getDefaultValues() != null) {
            String currentValues[] =
                    {"defaultValue =" + getDefaultValue(), "defaultValues Size =" + getDefaultValues().length};
            tracer.createWarning("Both Default Value and Default Values set", currentValues);
        }*/

        // Checks that a mask formatter is set if the data field is to be masked
        if (isApplyMask()) {
            if (maskFormatter == null) {
                String currentValues[] = {"applyMask =" + isApplyMask(), "maskFormatter =" + maskFormatter};
                tracer.createWarning("Apply mask is true, but no value is set for maskFormatter", currentValues);
            }
        }

        super.completeValidation(tracer.getCopy());
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        DataField dataFieldCopy = (DataField) component;
        dataFieldCopy.setAddHiddenWhenReadOnly(this.addHiddenWhenReadOnly);
        dataFieldCopy.setAdditionalHiddenPropertyNames(new ArrayList<String>(this.additionalHiddenPropertyNames));
        dataFieldCopy.setApplyMask(this.applyMask);

        if (this.maskFormatter != null) {
            dataFieldCopy.setMaskFormatter(this.maskFormatter);
        }

        if (this.bindingInfo != null) {
            dataFieldCopy.setBindingInfo((BindingInfo) this.bindingInfo.copy());
        }

        dataFieldCopy.setDefaultValue(this.defaultValue);

        if (this.defaultValues != null) {
            dataFieldCopy.setDefaultValues(this.defaultValues);
        }

        dataFieldCopy.setDictionaryAttributeName(this.dictionaryAttributeName);
        dataFieldCopy.setDictionaryObjectEntry(this.dictionaryObjectEntry);
        dataFieldCopy.setEnableAutoInquiry(this.enableAutoInquiry);
        dataFieldCopy.setEscapeHtmlInPropertyValue(this.escapeHtmlInPropertyValue);
        dataFieldCopy.setForcedValue(this.forcedValue);
        dataFieldCopy.setMultiLineReadOnlyDisplay(this.multiLineReadOnlyDisplay);

        if (this.propertyEditor != null) {
            dataFieldCopy.setPropertyEditor(this.propertyEditor);
        }

        dataFieldCopy.setPropertyName(this.propertyName);

        if (this.propertyNamesForAdditionalDisplay != null) {
            dataFieldCopy.setPropertyNamesForAdditionalDisplay(new ArrayList<String>(
                    this.propertyNamesForAdditionalDisplay));
        }

        dataFieldCopy.setReadOnlyDisplayReplacement(this.readOnlyDisplayReplacement);
        dataFieldCopy.setReadOnlyDisplayReplacementPropertyName(this.readOnlyDisplayReplacementPropertyName);
        dataFieldCopy.setReadOnlyDisplaySuffix(this.readOnlyDisplaySuffix);
        dataFieldCopy.setReadOnlyDisplaySuffixPropertyName(this.readOnlyDisplaySuffixPropertyName);
        dataFieldCopy.setReadOnlyListDelimiter(this.readOnlyListDelimiter);
        dataFieldCopy.setReadOnlyListDisplayType(this.readOnlyListDisplayType);
        dataFieldCopy.setDefaultValueFinderClass(this.defaultValueFinderClass);

        if (this.help != null) {
            dataFieldCopy.setHelp((Help) this.help.copy());
        }

        if (this.inquiry != null) {
            dataFieldCopy.setInquiry((Inquiry) this.inquiry.copy());
        }
    }
}
