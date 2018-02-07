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

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.web.format.Formatter;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.service.ModuleService;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.element.Link;
import org.kuali.rice.krad.uif.field.DataField;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.util.LookupInquiryUtils;
import org.kuali.rice.krad.uif.util.ObjectPropertyUtils;
import org.kuali.rice.krad.uif.util.ViewModelUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.UrlFactory;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Widget for rendering an Inquiry link or DirectInquiry action field
 *
 * <p>
 * The inquiry widget will render a button for the field value when
 * that field is editable. When read only the widget will create a link on the display value.
 * It points to the associated inquiry view for the field. The inquiry can be configured to point to a certain
 * {@code InquiryView}, or the framework will attempt to associate the field with a inquiry based on
 * its metadata (in particular its relationships in the model).
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "inquiry-bean", parent = "Uif-Inquiry")
public class Inquiry extends WidgetBase {
    private static final long serialVersionUID = -2154388007867302901L;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(Inquiry.class);

    public static final String INQUIRY_TITLE_PREFIX = "title.inquiry.url.value.prependtext";

    private String baseInquiryUrl;

    private String dataObjectClassName;
    private String viewName;

    private Map<String, String> inquiryParameters;

    private Link inquiryLink;

    private Action directInquiryAction;
    private boolean enableDirectInquiry;

    private boolean adjustInquiryParameters;
    private BindingInfo fieldBindingInfo;

    private boolean parentReadOnly;

    public Inquiry() {
        super();

        inquiryParameters = new HashMap<String, String>();
    }

    /**
     * @see org.kuali.rice.krad.uif.widget.WidgetBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        if (!isRender()) {
            return;
        }

        // set render to false until we find an inquiry class
        setRender(false);

        // used to determine whether a normal or direct inquiry should be enabled
        setParentReadOnly(parent.isReadOnly());

        // Do checks for inquiry when read only
        if (isParentReadOnly()) {
            if (StringUtils.isBlank(((DataField) parent).getBindingInfo().getBindingPath()) || ((DataField) parent)
                    .getBindingInfo().getBindingPath().equals("null")) {
                return;
            }

            // check if field value is null, if so no inquiry
            try {
                Object propertyValue = ObjectPropertyUtils.getPropertyValue(model,
                        ((DataField) parent).getBindingInfo().getBindingPath());

                if ((propertyValue == null) || StringUtils.isBlank(propertyValue.toString())) {
                    return;
                }
            } catch (Exception e) {
                // if we can't get the value just swallow the exception and don't set an inquiry
                return;
            }
        }

        // Do checks for direct inquiry when editable
        if (!isParentReadOnly() && parent instanceof InputField) {
            if (!enableDirectInquiry) {
                return;
            }

            // determine whether inquiry parameters will need adjusted
            if (StringUtils.isBlank(getDataObjectClassName())
                    || (getInquiryParameters() == null)
                    || getInquiryParameters().isEmpty()) {
                // if inquiry parameters not given, they will not be adjusted by super
                adjustInquiryParameters = true;
                fieldBindingInfo = ((InputField) parent).getBindingInfo();
            }
        }

        setupLink(view, model, (DataField) parent);
    }

    /**
     * Get parent object and field name and build the inquiry link
     *
     * <p>
     * This was moved from the performFinalize because overlapping and to be used
     * by DirectInquiry.
     * </p>
     *
     * @param view Container View
     * @param model model
     * @param field The parent Attribute field
     */
    public void setupLink(View view, Object model, DataField field) {
        String propertyName = field.getBindingInfo().getBindingName();

        // if class and parameters configured, build link from those
        if (StringUtils.isNotBlank(getDataObjectClassName()) && (getInquiryParameters() != null) &&
                !getInquiryParameters().isEmpty()) {
            Class<?> inquiryObjectClass;
            try {
                inquiryObjectClass = Class.forName(getDataObjectClassName());
            } catch (ClassNotFoundException e) {
                LOG.error("Unable to get class for: " + getDataObjectClassName());
                throw new RuntimeException(e);
            }

            updateInquiryParameters(field.getBindingInfo());

            buildInquiryLink(model, propertyName, inquiryObjectClass, getInquiryParameters());
        }
        // get inquiry class and parameters from view helper
        else {
            // get parent object for inquiry metadata
            Object parentObject = ViewModelUtils.getParentObjectForMetadata(view, model, field);
            view.getViewHelperService().buildInquiryLink(parentObject, propertyName, this);
        }
    }

    /**
     * Adjusts the path on the inquiry parameter property to match the binding
     * path prefix of the given {@code BindingInfo}
     *
     * @param bindingInfo binding info instance to copy binding path prefix from
     */
    public void updateInquiryParameters(BindingInfo bindingInfo) {
        Map<String, String> adjustedInquiryParameters = new HashMap<String, String>();
        for (Entry<String, String> stringEntry : inquiryParameters.entrySet()) {
            String toField = stringEntry.getValue();
            String adjustedFromFieldPath = bindingInfo.getPropertyAdjustedBindingPath(stringEntry.getKey());

            adjustedInquiryParameters.put(adjustedFromFieldPath, toField);
        }

        this.inquiryParameters = adjustedInquiryParameters;
    }

    /**
     * Builds the inquiry link based on the given inquiry class and parameters
     *
     * @param dataObject parent object that contains the data (used to pull inquiry
     * parameters)
     * @param propertyName name of the property the inquiry is set on
     * @param inquiryObjectClass class of the object the inquiry should point to
     * @param inquiryParams map of key field mappings for the inquiry
     */
    public void buildInquiryLink(Object dataObject, String propertyName, Class<?> inquiryObjectClass,
            Map<String, String> inquiryParams) {

        Properties urlParameters = new Properties();

        urlParameters.setProperty(UifParameters.DATA_OBJECT_CLASS_NAME, inquiryObjectClass.getName());
        urlParameters.setProperty(UifParameters.METHOD_TO_CALL, UifConstants.MethodToCallNames.START);
        if (StringUtils.isNotBlank(this.viewName)) {
            urlParameters.setProperty(UifParameters.VIEW_NAME, this.viewName);
        }

        // add inquiry specific parms to url
        if (getInquiryLink().getLightBox() != null) {
            getInquiryLink().getLightBox().setAddAppParms(true);
        }

        // configure inquiry when read only
        if (isParentReadOnly()) {
            for (Entry<String, String> inquiryParameter : inquiryParams.entrySet()) {
                String parameterName = inquiryParameter.getKey();

                Object parameterValue = ObjectPropertyUtils.getPropertyValue(dataObject, parameterName);

                // TODO: need general format util that uses spring
                if (parameterValue == null) {
                    parameterValue = "";
                } else if (parameterValue instanceof java.sql.Date) {
                    if (Formatter.findFormatter(parameterValue.getClass()) != null) {
                        Formatter formatter = Formatter.getFormatter(parameterValue.getClass());
                        parameterValue = formatter.format(parameterValue);
                    }
                } else {
                    parameterValue = parameterValue.toString();
                }

                // Encrypt value if it is a field that has restriction that prevents a value from being shown to
                // user, because we don't want the browser history to store the restricted attributes value in the URL
                if (KRADServiceLocatorWeb.getDataObjectAuthorizationService()
                        .attributeValueNeedsToBeEncryptedOnFormsAndLinks(inquiryObjectClass,
                                inquiryParameter.getValue())) {
                    try {
                        parameterValue = CoreApiServiceLocator.getEncryptionService().encrypt(parameterValue);
                    } catch (GeneralSecurityException e) {
                        throw new RuntimeException("Exception while trying to encrypted value for inquiry framework.",
                                e);
                    }
                }

                // add inquiry parameter to URL
                urlParameters.put(inquiryParameter.getValue(), parameterValue);
            }

            /* build inquiry URL */
            String inquiryUrl;

            // check for EBOs for an alternate inquiry URL
            ModuleService responsibleModuleService =
                    KRADServiceLocatorWeb.getKualiModuleService().getResponsibleModuleService(inquiryObjectClass);
            if (responsibleModuleService != null && responsibleModuleService.isExternalizable(inquiryObjectClass)) {
                inquiryUrl = responsibleModuleService.getExternalizableDataObjectInquiryUrl(inquiryObjectClass,
                        urlParameters);
            } else {
                inquiryUrl = UrlFactory.parameterizeUrl(getBaseInquiryUrl(), urlParameters);
            }

            getInquiryLink().setHref(inquiryUrl);

            // set inquiry title
            String linkTitle = createTitleText(inquiryObjectClass);
            linkTitle = LookupInquiryUtils.getLinkTitleText(linkTitle, inquiryObjectClass, getInquiryParameters());
            getInquiryLink().setTitle(linkTitle);

            setRender(true);
        }
        // configure direct inquiry when editable
        else {
            // Direct inquiry
            String inquiryUrl = UrlFactory.parameterizeUrl(getBaseInquiryUrl(), urlParameters);

            StringBuilder paramMapString = new StringBuilder();

            // Build parameter string using the actual names of the fields as on the html page
            for (Entry<String, String> inquiryParameter : inquiryParams.entrySet()) {
                String inquiryParameterFrom = inquiryParameter.getKey();

                if (adjustInquiryParameters && (fieldBindingInfo != null)) {
                    inquiryParameterFrom = fieldBindingInfo.getPropertyAdjustedBindingPath(inquiryParameterFrom);
                }

                paramMapString.append(inquiryParameterFrom);
                paramMapString.append(":");
                paramMapString.append(inquiryParameter.getValue());
                paramMapString.append(",");
            }
            paramMapString.deleteCharAt(paramMapString.length() - 1);

            // Check if lightbox is set. Get lightbox options.
            String lightBoxOptions = "";
            boolean lightBoxShow = (getInquiryLink().getLightBox() != null);
            if (lightBoxShow) {
                lightBoxOptions = getInquiryLink().getLightBox().getTemplateOptionsJSString();
            }

            // Create onlick script to open the inquiry window on the click event
            // of the direct inquiry
            StringBuilder onClickScript = new StringBuilder("showDirectInquiry(\"");
            onClickScript.append(inquiryUrl);
            onClickScript.append("\", \"");
            onClickScript.append(paramMapString);
            onClickScript.append("\", ");
            onClickScript.append(lightBoxShow);
            onClickScript.append(", ");
            onClickScript.append(lightBoxOptions);
            onClickScript.append(");");

            directInquiryAction.setPerformDirtyValidation(false);
            directInquiryAction.setActionScript(onClickScript.toString());

            setRender(true);
        }
    }

    /**
     * Gets text to prepend to the inquiry link title
     *
     * @param dataObjectClass data object class being inquired into
     * @return title prepend text
     */
    public String createTitleText(Class<?> dataObjectClass) {
        String titleText = "";

        String titlePrefixProp = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                INQUIRY_TITLE_PREFIX);
        if (StringUtils.isNotBlank(titlePrefixProp)) {
            titleText += titlePrefixProp + " ";
        }

        String objectLabel = KRADServiceLocatorWeb.getDataDictionaryService().getDataDictionary().getDataObjectEntry(
                dataObjectClass.getName()).getObjectLabel();
        if (StringUtils.isNotBlank(objectLabel)) {
            titleText += objectLabel + " ";
        }

        return titleText;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(getInquiryLink());
        components.add(getDirectInquiryAction());

        return components;
    }

    /**
     * Returns the URL for the inquiry for which parameters will be added
     *
     * <p>
     * The base URL includes the domain, context, and controller mapping for the inquiry invocation. Parameters are
     * then added based on configuration to complete the URL. This is generally defaulted to the application URL and
     * internal KRAD servlet mapping, but can be changed to invoke another application such as the Rice standalone
     * server
     * </p>
     *
     * @return inquiry base URL
     */
    @BeanTagAttribute(name = "baseInquiryUrl")
    public String getBaseInquiryUrl() {
        return this.baseInquiryUrl;
    }

    /**
     * Setter for the inquiry base url (domain, context, and controller)
     *
     * @param baseInquiryUrl
     */
    public void setBaseInquiryUrl(String baseInquiryUrl) {
        this.baseInquiryUrl = baseInquiryUrl;
    }

    /**
     * Full class name the inquiry should be provided for
     *
     * <p>
     * This is passed on to the inquiry request for the data object the lookup should be rendered for. This is then
     * used by the inquiry framework to select the lookup view (if more than one inquiry view exists for the same
     * data object class name, the {@link #getViewName()} property should be specified to select the view to render).
     * </p>
     *
     * @return inquiry class name
     */
    @BeanTagAttribute(name = "dataObjectClassName")
    public String getDataObjectClassName() {
        return this.dataObjectClassName;
    }

    /**
     * Setter for the class name that inquiry should be provided for
     *
     * @param dataObjectClassName
     */
    public void setDataObjectClassName(String dataObjectClassName) {
        this.dataObjectClassName = dataObjectClassName;
    }

    /**
     * When multiple target inquiry views exists for the same data object class, the view name can be set to
     * determine which one to use
     *
     * <p>
     * When creating multiple inquiry views for the same data object class, the view name can be specified for the
     * different versions (for example 'simple' and 'advanced'). When multiple inquiry views exist the view name must
     * be sent with the data object class for the request. Note the view id can be alternatively used to uniquely
     * identify the inquiry view
     * </p>
     */
    @BeanTagAttribute(name = "viewName")
    public String getViewName() {
        return this.viewName;
    }

    /**
     * Setter for the view name configured on the inquiry view that should be invoked by the inquiry widget
     *
     * @param viewName
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    /**
     * Map that determines what properties from a calling view will be sent to properties on the inquiry data object
     *
     * <p>
     * When invoking an inquiry view, a query is done against the inquiries configured data object and the resulting
     * record is display. The values for the properties configured within the inquiry parameters Map will be
     * pulled and passed along as values for the inquiry data object properties (thus they form the criteria for
     * the inquiry)
     * </p>
     *
     * @return mapping of calling view properties to inquiry data object properties
     */
    @BeanTagAttribute(name = "inquiryParameters", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getInquiryParameters() {
        return this.inquiryParameters;
    }

    /**
     * Setter for the map that determines what property values on the calling view will be sent to properties on the
     * inquiry data object
     *
     * @param inquiryParameters
     */
    public void setInquiryParameters(Map<String, String> inquiryParameters) {
        this.inquiryParameters = inquiryParameters;
    }

    /**
     * {@code Link} that will be rendered for an inquiry
     *
     * @return the inquiry link
     */
    @BeanTagAttribute(name = "inquiryLink", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Link getInquiryLink() {
        return this.inquiryLink;
    }

    /**
     * Setter for the inquiry {@code Link}
     *
     * @param inquiryLink the inquiry {@link Link} object
     */
    public void setInquiryLink(Link inquiryLink) {
        this.inquiryLink = inquiryLink;
    }

    /**
     * {@code Action} that will be rendered next to the field for a direct inquiry
     *
     * @return the directInquiryAction
     */
    @BeanTagAttribute(name = "directInquiryAction", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Action getDirectInquiryAction() {
        return this.directInquiryAction;
    }

    /**
     * Setter for the direct inquiry {@code Action}
     *
     * @param directInquiryAction the direct inquiry {@link Action}
     */
    public void setDirectInquiryAction(Action directInquiryAction) {
        this.directInquiryAction = directInquiryAction;
    }

    /**
     * Indicates that the direct inquiry will not be rendered
     *
     * @return true if the direct inquiry should be rendered, false if not
     */
    @BeanTagAttribute(name = "enableDirectInquiry")
    public boolean isEnableDirectInquiry() {
        return enableDirectInquiry;
    }

    /**
     * Setter for the hideDirectInquiry flag
     *
     * @param enableDirectInquiry
     */
    public void setEnableDirectInquiry(boolean enableDirectInquiry) {
        this.enableDirectInquiry = enableDirectInquiry;
    }

    /**
     * Determines whether a normal or direct inquiry should be enabled
     *
     * @return true if parent component is read only, false otherwise
     */
    protected boolean isParentReadOnly() {
        return parentReadOnly;
    }

    /**
     * Determines whether a normal or direct inquiry should be enabled
     *
     * <p>
     * Used by unit tests and internally
     * </p>
     *
     * @param parentReadOnly true if parent component is read only, false otherwise
     */
    protected void setParentReadOnly(boolean parentReadOnly) {
        this.parentReadOnly = parentReadOnly;
    }

    /**
     * Determines whether inquiry parameters adjusted
     *
     * @return true if adjusted
     */
    public boolean isAdjustInquiryParameters() {
        return adjustInquiryParameters;
    }

    /**
     * Determines whether inquiry parameters adjusted
     *
     * <p>
     * Used internally
     * </p>
     *
     * @param adjustInquiryParameters
     */
    public void setAdjustInquiryParameters(boolean adjustInquiryParameters) {
        this.adjustInquiryParameters = adjustInquiryParameters;
    }

    /**
     * Sets the field binding information
     *
     * <p>
     * Sets the field binding information
     * </p>
     *
     * @param fieldBindingInfo
     */
    public void setFieldBindingInfo(BindingInfo fieldBindingInfo) {
        this.fieldBindingInfo = fieldBindingInfo;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Inquiry inquiryCopy = (Inquiry) component;
        inquiryCopy.setBaseInquiryUrl(this.getBaseInquiryUrl());
        inquiryCopy.setDataObjectClassName(this.getDataObjectClassName());
        inquiryCopy.setViewName(this.getViewName());
        inquiryCopy.setInquiryLink((Link) this.getInquiryLink().copy());
        inquiryCopy.setDirectInquiryAction((Action) this.getDirectInquiryAction().copy());
        inquiryCopy.setEnableDirectInquiry(this.isEnableDirectInquiry());
        inquiryCopy.setAdjustInquiryParameters(this.isAdjustInquiryParameters());
        inquiryCopy.setParentReadOnly(this.isParentReadOnly());

        if (inquiryParameters != null) {
            Map<String, String> inquiryParametersCopy = Maps.newHashMapWithExpectedSize(inquiryParameters.size());
            for (Map.Entry inquiryParameter : inquiryParameters.entrySet()) {
                inquiryParametersCopy.put(inquiryParameter.getKey().toString(), inquiryParameter.getValue().toString());
            }
            inquiryCopy.setInquiryParameters(inquiryParametersCopy);
        }

        if (fieldBindingInfo != null) {
            inquiryCopy.setFieldBindingInfo((BindingInfo) fieldBindingInfo.copy());
        }
    }
}
