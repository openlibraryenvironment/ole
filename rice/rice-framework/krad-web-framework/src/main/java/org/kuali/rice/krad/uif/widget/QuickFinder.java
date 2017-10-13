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
import org.kuali.rice.krad.bo.DataObjectRelationship;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifParameters;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.container.CollectionGroup;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.field.InputField;
import org.kuali.rice.krad.uif.util.ViewModelUtils;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.util.KRADConstants;
import org.kuali.rice.krad.util.KRADUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Widget for navigating to a lookup from a field (called a quickfinder)
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "quickFinder-bean", parent = "Uif-QuickFinder"),
        @BeanTag(name = "quickFinderByScript-bean", parent = "Uif-QuickFinderByScript"),
        @BeanTag(name = "collectionQuickFinder-bean", parent = "Uif-CollectionQuickFinder")})
public class QuickFinder extends WidgetBase {
    private static final long serialVersionUID = 3302390972815386785L;

    // lookup configuration
    private String baseLookupUrl;
    private String dataObjectClassName;
    private String viewName;

    private String referencesToRefresh;

    private Map<String, String> fieldConversions;
    private Map<String, String> lookupParameters;

    // lookup view options
    private String readOnlySearchFields;

    private Boolean hideReturnLink;
    private Boolean suppressActions;
    private Boolean autoSearch;
    private Boolean renderLookupCriteria;
    private Boolean supplementalActionsEnabled;
    private Boolean renderSearchButtons;
    private Boolean renderHeader;
    private Boolean showMaintenanceLinks;

    private Boolean multipleValuesSelect;
    private String lookupCollectionName;

    private Action quickfinderAction;
    private LightBox lightBoxLookup;

    public QuickFinder() {
        super();

        fieldConversions = new HashMap<String, String>();
        lookupParameters = new HashMap<String, String>();
    }

    /**
     * The following initialization is performed:
     *
     * <ul>
     * <li>Set defaults for binding</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        super.performInitialization(view, model);

        if (quickfinderAction != null && (lightBoxLookup != null && lightBoxLookup.isRender())) {
            quickfinderAction.setActionScript("voidAction");
        }
    }

    /**
     * The following finalization is performed:
     *
     * <ul>
     * <li>
     * Sets defaults on collectionLookup such as collectionName, and the class if not set
     *
     * <p>
     * If the data object class was not configured for the lookup, the class configured for the collection group will
     * be used if it has a lookup defined. If not data object class is found for the lookup it will be disabled. The
     * collection name is also defaulted to the binding path for this collection group, so the results returned from
     * the lookup will populate this collection. Finally field conversions will be generated based on the PK fields of
     * the collection object class
     * </p>
     * </li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.widget.Widget#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        // TODO: add flag to enable quick finder when the input field (parent) is read-only
        if (parent.isReadOnly()) {
            setRender(false);
        }

        if (!isRender()) {
            return;
        }

        if (parent instanceof InputField) {
            InputField field = (InputField) parent;

            // determine lookup class, field conversions and lookup parameters in
            // not set
            if (StringUtils.isBlank(dataObjectClassName)) {
                DataObjectRelationship relationship = getRelationshipForField(view, model, field);

                // if no relationship found cannot have a quickfinder
                if (relationship == null) {
                    setRender(false);
                    return;
                }

                dataObjectClassName = relationship.getRelatedClass().getName();

                if ((fieldConversions == null) || fieldConversions.isEmpty()) {
                    generateFieldConversions(field, relationship);
                }

                if ((lookupParameters == null) || lookupParameters.isEmpty()) {
                    generateLookupParameters(field, relationship);
                }
            }

            // adjust paths based on associated attribute field
            updateFieldConversions(field.getBindingInfo());
            updateLookupParameters(field.getBindingInfo());
            updateReferencesToRefresh(field.getBindingInfo());
        } else if (parent instanceof CollectionGroup) {
            CollectionGroup collectionGroup = (CollectionGroup) parent;

            // check to see if data object class is configured for lookup, if so we will assume it should be enabled
            // if not and the class configured for the collection group is lookupable, use that
            if (StringUtils.isBlank(getDataObjectClassName())) {
                Class<?> collectionObjectClass = collectionGroup.getCollectionObjectClass();
                boolean isCollectionClassLookupable = KRADServiceLocatorWeb.getViewDictionaryService().isLookupable(
                        collectionObjectClass);
                if (isCollectionClassLookupable) {
                    setDataObjectClassName(collectionObjectClass.getName());

                    if ((fieldConversions == null) || fieldConversions.isEmpty()) {
                        // use PK fields for collection class
                        List<String> collectionObjectPKFields =
                                KRADServiceLocatorWeb.getDataObjectMetaDataService().listPrimaryKeyFieldNames(
                                        collectionObjectClass);

                        for (String pkField : collectionObjectPKFields) {
                            fieldConversions.put(pkField, pkField);
                        }
                    }
                } else {
                    // no available data object class to lookup so need to disable quickfinder
                    setRender(false);
                }
            }

            // set the lookup return collection name to this collection path
            if (isRender() && StringUtils.isBlank(getLookupCollectionName())) {
                setLookupCollectionName(collectionGroup.getBindingInfo().getBindingPath());
            }
        }

        quickfinderAction.addActionParameter(UifParameters.BASE_LOOKUP_URL, baseLookupUrl);
        quickfinderAction.addActionParameter(UifParameters.DATA_OBJECT_CLASS_NAME, dataObjectClassName);

        if (!fieldConversions.isEmpty()) {
            quickfinderAction.addActionParameter(UifParameters.CONVERSION_FIELDS, KRADUtils.buildMapParameterString(
                    fieldConversions));
        }

        if (!lookupParameters.isEmpty()) {
            quickfinderAction.addActionParameter(UifParameters.LOOKUP_PARAMETERS, KRADUtils.buildMapParameterString(
                    lookupParameters));
        }

        addActionParameterIfNotNull(UifParameters.VIEW_NAME, viewName);
        addActionParameterIfNotNull(UifParameters.READ_ONLY_FIELDS, readOnlySearchFields);
        addActionParameterIfNotNull(UifParameters.HIDE_RETURN_LINK, hideReturnLink);
        addActionParameterIfNotNull(UifParameters.SUPPRESS_ACTIONS, suppressActions);
        addActionParameterIfNotNull(UifParameters.REFERENCES_TO_REFRESH, referencesToRefresh);
        addActionParameterIfNotNull(UifParameters.AUTO_SEARCH, autoSearch);
        addActionParameterIfNotNull(UifParameters.RENDER_LOOKUP_CRITERIA, renderLookupCriteria);
        addActionParameterIfNotNull(UifParameters.SUPPLEMENTAL_ACTIONS_ENABLED, supplementalActionsEnabled);
        addActionParameterIfNotNull(UifParameters.RENDER_SEARCH_BUTTONS, renderSearchButtons);
        addActionParameterIfNotNull(UifParameters.RENDER_HEADER, renderHeader);
        addActionParameterIfNotNull(UifParameters.SHOW_MAINTENANCE_LINKS, showMaintenanceLinks);
        addActionParameterIfNotNull(UifParameters.MULTIPLE_VALUES_SELECT, multipleValuesSelect);
        addActionParameterIfNotNull(UifParameters.LOOKUP_COLLECTION_NAME, lookupCollectionName);

        // TODO:
        // org.kuali.rice.kns.util.FieldUtils.populateQuickfinderDefaultsForLookup(Class,
        // String, Field)
    }

    protected void addActionParameterIfNotNull(String parameterName, Object parameterValue) {
        if ((parameterValue != null) && StringUtils.isNotBlank(parameterValue.toString())) {
            quickfinderAction.addActionParameter(parameterName, parameterValue.toString());
        }
    }

    protected DataObjectRelationship getRelationshipForField(View view, Object model, InputField field) {
        String propertyName = field.getBindingInfo().getBindingName();

        // get object instance and class for parent
        Object parentObject = ViewModelUtils.getParentObjectForMetadata(view, model, field);
        Class<?> parentObjectClass = null;
        if (parentObject != null) {
            parentObjectClass = parentObject.getClass();
        }

        // get relationship from metadata service
        return KRADServiceLocatorWeb.getDataObjectMetaDataService().getDataObjectRelationship(parentObject,
                parentObjectClass, propertyName, "", true, true, false);
    }

    protected void generateFieldConversions(InputField field, DataObjectRelationship relationship) {
        fieldConversions = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : relationship.getParentToChildReferences().entrySet()) {
            String fromField = entry.getValue();
            String toField = entry.getKey();

            // TODO: displayedFieldnames in
            // org.kuali.rice.kns.lookup.LookupUtils.generateFieldConversions(BusinessObject,
            // String, DataObjectRelationship, String, List, String)

            fieldConversions.put(fromField, toField);
        }
    }

    protected void generateLookupParameters(InputField field, DataObjectRelationship relationship) {
        lookupParameters = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : relationship.getParentToChildReferences().entrySet()) {
            String fromField = entry.getKey();
            String toField = entry.getValue();

            // TODO: displayedFieldnames and displayedQFFieldNames in
            // generateLookupParameters(BusinessObject,
            // String, DataObjectRelationship, String, List, String)

            if (relationship.getUserVisibleIdentifierKey() == null || relationship.getUserVisibleIdentifierKey().equals(
                    fromField)) {
                lookupParameters.put(fromField, toField);
            }
        }
    }

    /**
     * Adjusts the path on the field conversion to property to match the binding
     * path prefix of the given <code>BindingInfo</code>
     *
     * @param bindingInfo binding info instance to copy binding path prefix from
     */
    public void updateFieldConversions(BindingInfo bindingInfo) {
        Map<String, String> adjustedFieldConversions = new HashMap<String, String>();
        for (String fromField : fieldConversions.keySet()) {
            String toField = fieldConversions.get(fromField);
            String adjustedToFieldPath = bindingInfo.getPropertyAdjustedBindingPath(toField);

            adjustedFieldConversions.put(fromField, adjustedToFieldPath);
        }

        this.fieldConversions = adjustedFieldConversions;
    }

    /**
     * Adjusts the path on the lookup parameter from property to match the binding
     * path prefix of the given <code>BindingInfo</code>
     *
     * @param bindingInfo binding info instance to copy binding path prefix from
     */
    public void updateLookupParameters(BindingInfo bindingInfo) {
        Map<String, String> adjustedLookupParameters = new HashMap<String, String>();
        for (String fromField : lookupParameters.keySet()) {
            String toField = lookupParameters.get(fromField);
            String adjustedFromFieldPath = bindingInfo.getPropertyAdjustedBindingPath(fromField);

            adjustedLookupParameters.put(adjustedFromFieldPath, toField);
        }

        this.lookupParameters = adjustedLookupParameters;
    }

    /**
     * Adjust the path on the referencesToRefresh parameter to match the binding path
     * prefix of the given <code>BindingInfo</code>
     *
     * @param bindingInfo binding info instance to copy binding path prefix from
     */
    public void updateReferencesToRefresh (BindingInfo bindingInfo) {
        String adjustedReferencesToRefresh = new String();

        if (referencesToRefresh == null) {
            referencesToRefresh = adjustedReferencesToRefresh;
        }

        for (String reference : StringUtils.split(referencesToRefresh, KRADConstants.REFERENCES_TO_REFRESH_SEPARATOR )){

            // add separator between references to refresh
            if (StringUtils.isNotBlank(adjustedReferencesToRefresh)) {
                adjustedReferencesToRefresh = adjustedReferencesToRefresh + KRADConstants.REFERENCES_TO_REFRESH_SEPARATOR;
            }

            String adjustedReference = bindingInfo.getPropertyAdjustedBindingPath(reference);
            adjustedReferencesToRefresh = adjustedReferencesToRefresh + adjustedReference;
        }
        this.referencesToRefresh = adjustedReferencesToRefresh;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(quickfinderAction);
        components.add(lightBoxLookup);

        return components;
    }

    /**
     * Returns the URL for the lookup for which parameters will be added
     *
     * <p>
     * The base URL includes the domain, context, and controller mapping for the lookup invocation. Parameters are
     * then added based on configuration to complete the URL. This is generally defaulted to the application URL and
     * internal KRAD servlet mapping, but can be changed to invoke another application such as the Rice standalone
     * server
     * </p>
     *
     * @return lookup base URL
     */
    @BeanTagAttribute(name = "baseLookupUrl")
    public String getBaseLookupUrl() {
        return this.baseLookupUrl;
    }

    /**
     * Setter for the lookup base url (domain, context, and controller)
     *
     * @param baseLookupUrl
     */
    public void setBaseLookupUrl(String baseLookupUrl) {
        this.baseLookupUrl = baseLookupUrl;
    }

    /**
     * Full class name the lookup should be provided for
     *
     * <p>
     * This is passed on to the lookup request for the data object the lookup should be rendered for. This is then
     * used by the lookup framework to select the lookup view (if more than one lookup view exists for the same
     * data object class name, the {@link #getViewName()} property should be specified to select the view to render).
     * </p>
     *
     * @return lookup class name
     */
    @BeanTagAttribute(name = "dataOjbectClassName")
    public String getDataObjectClassName() {
        return this.dataObjectClassName;
    }

    /**
     * Setter for the class name that lookup should be provided for
     *
     * @param dataObjectClassName
     */
    public void setDataObjectClassName(String dataObjectClassName) {
        this.dataObjectClassName = dataObjectClassName;
    }

    /**
     * When multiple target lookup views exists for the same data object class, the view name can be set to
     * determine which one to use
     *
     * <p>
     * When creating multiple lookup views for the same data object class, the view name can be specified for the
     * different versions (for example 'simple' and 'advanced'). When multiple lookup views exist the view name must
     * be sent with the data object class for the request. Note the view id can be alternatively used to uniquely
     * identify the lookup view
     * </p>
     */
    @BeanTagAttribute(name = "viewName")
    public String getViewName() {
        return this.viewName;
    }

    /**
     * Setter for the view name configured on the lookup view that should be invoked by the quickfinder widget
     *
     * @param viewName
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    /**
     * List of property names on the model that should be refreshed when the lookup returns
     *
     * <p>
     * Note this is only relevant when the return by script option is not enabled (meaning the server will be invoked
     * on the lookup return call)
     * </p>
     *
     * <p>
     * When a lookup return call is made (to return a result value) the controller refresh method will be invoked. If
     * refresh properties are configured, a call to refresh those references from the database will be made. This is
     * useful if the lookup returns a foreign key field and the related record is needed.
     * </p>
     *
     * @return list of property names to refresh
     *         TODO: refactor this to be a List type
     */
    @BeanTagAttribute(name = "referencesToRefresh")
    public String getReferencesToRefresh() {
        return this.referencesToRefresh;
    }

    /**
     * Setter for the list of property names that should be refreshed when the lookup returns
     *
     * @param referencesToRefresh
     */
    public void setReferencesToRefresh(String referencesToRefresh) {
        this.referencesToRefresh = referencesToRefresh;
    }

    /**
     * Map that determines what properties from a result lookup row (if selected) will be returned to properties on
     * the calling view
     *
     * <p>
     * The purpose of using the lookup is to search for a particular value and return that value to the form being
     * completed. In order for the lookup framework to return the field back to us, we must specify the name of the
     * field on the data object class whose value we need, and the name of the field on the calling view. Furthermore,
     * we can choose to have the lookup return additional fields that populate other form fields or informational
     * properties (see ‘Field Queries and Informational Properties’). These pairs of fields are known as
     * ‘field conversions’.
     * </p>
     *
     * <p>
     * The fieldConversions property is a Map. Each entry represents a field that will be returned back from the
     * lookup, with the entry key being the field name on the data object class, and the entry value being the field
     * name on the calling view. It is helpful to think of this as a from-to mapping. Pulling from the data object
     * field (map key) to the calling view field (map value).
     * </p>
     *
     * @return mapping of lookup data object property names to view property names
     */
    @BeanTagAttribute(name = "fieldConversions", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getFieldConversions() {
        return this.fieldConversions;
    }

    /**
     * Setter for the map that determines what properties on a lookup result row are returned and how they map to
     * properties on the calling view
     *
     * @param fieldConversions
     */
    public void setFieldConversions(Map<String, String> fieldConversions) {
        this.fieldConversions = fieldConversions;
    }

    /**
     * Map that determines what properties from a calling view will be sent to properties on that are rendered
     * for the lookup view's search fields (they can be hidden)
     *
     * <p>
     * When invoking a lookup view, we can pre-populate search fields on the lookup view with data from the view
     * that called the lookup. The user can then perform the search with these values, or (if edited is allowed or
     * the fields are not hidden) change the passed in values. When the lookup is invoked, the values for the
     * properties configured within the lookup parameters Map will be pulled and passed along as values for the
     * lookup view properties
     * </p>
     *
     * @return mapping of calling view properties to lookup view search fields
     */
    @BeanTagAttribute(name = "lookupParameters", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getLookupParameters() {
        return this.lookupParameters;
    }

    /**
     * Setter for the map that determines what property values on the calling view will be sent to properties on the
     * lookup views search fields
     *
     * @param lookupParameters
     */
    public void setLookupParameters(Map<String, String> lookupParameters) {
        this.lookupParameters = lookupParameters;
    }

    /**
     * Comma delimited String of property names on the lookup view that should be read only
     *
     * <p>
     * When requesting a lookup view, property names for fields that are rendered as search criteria can be marked
     * as read-only. This is usually done when a lookup parameter for that property is sent in and the user should
     * not be allowed to change the value
     * </p>
     *
     * @return property names (delimited by a comma) whose criteria fields should be read-only on the
     *         lookup view
     */
    @BeanTagAttribute(name = "readOnlySearchFields")
    public String getReadOnlySearchFields() {
        return this.readOnlySearchFields;
    }

    /**
     * Setter for property names for criteria fields on the lookup view that should be read-only (multiple property
     * names are specified using a comma delimiter)
     *
     * @param readOnlySearchFields
     */
    public void setReadOnlySearchFields(String readOnlySearchFields) {
        this.readOnlySearchFields = readOnlySearchFields;
    }

    /**
     * Indicates whether the return links for lookup results should be rendered
     *
     * <p>
     * A lookup view can be invoked to allow the user to select a value (or set of values) to return back to the
     * calling view. For single value lookups this is done with a return link that is rendered for each row. This
     * return link can be disabled by setting this property to true
     * </p>
     *
     * @return true if the return link should not be shown, false if it should be
     */
    @BeanTagAttribute(name = "hideReturnLink")
    public Boolean getHideReturnLink() {
        return this.hideReturnLink;
    }

    /**
     * Setter for the hide return link indicator
     *
     * @param hideReturnLink
     */
    public void setHideReturnLink(Boolean hideReturnLink) {
        this.hideReturnLink = hideReturnLink;
    }

    /**
     * Indicates whether the maintenance actions (or others) are rendered on the invoked lookup view
     *
     * <p>
     * By default a lookup view will add an actions column for the result table that display maintenance links (in
     * addition to a new link at the top of the page) if a maintenance action is available. Custom links can also be
     * added to the action column as necessary. This flag can be set to true to suppress the rendering of the actions
     * for the lookup call.
     * </p>
     *
     * <p>
     * An example of when this might be useful is when invoking a lookup to return a value to a value. Generally in
     * these cases you don't want to the user going off to another view (such as the maintenance view)
     * </p>
     *
     * @return true if actions should be rendered, false if not
     */
    @BeanTagAttribute(name = "suppressActions")
    public Boolean getSuppressActions() {
        return suppressActions;
    }

    /**
     * Setter for the suppress actions indicator
     *
     * @param suppressActions
     */
    public void setSuppressActions(Boolean suppressActions) {
        this.suppressActions = suppressActions;
    }

    /**
     * Indicates whether the search should be executed when first rendering the lookup view
     *
     * <p>
     * By default the lookup view is rendered, the user enters search values and executes the results. This flag can
     * be set to true to indicate the search should be performed before showing the screen to the user. This is
     * generally used when search criteria is being passed in as well
     * </p>
     *
     * @return true if the search should be performed initially, false if not
     */
    @BeanTagAttribute(name = "autoSearch")
    public Boolean getAutoSearch() {
        return this.autoSearch;
    }

    /**
     * Setter for the auto search indicator
     *
     * @param autoSearch
     */
    public void setAutoSearch(Boolean autoSearch) {
        this.autoSearch = autoSearch;
    }

    /**
     * Indicates whether the lookup criteria (search group) should be enabled on the invoked lookup view
     *
     * <p>
     * Setting the this to false will not display the lookup criteria but only the results. Therefore this is only
     * useful when setting {@link #getAutoSearch()} to true and passing in criteria
     * </p>
     *
     * @return true if lookup criteria should be displayed, false if not
     */
    @BeanTagAttribute(name = "renderLookupCriteria")
    public Boolean getRenderLookupCriteria() {
        return this.renderLookupCriteria;
    }

    /**
     * Setter for enabling the lookup criteria group
     *
     * @param renderLookupCriteria
     */
    public void setRenderLookupCriteria(Boolean renderLookupCriteria) {
        this.renderLookupCriteria = renderLookupCriteria;
    }

    /**
     * TODO: not implemented currently
     *
     * @return Boolean
     */
    @BeanTagAttribute(name = "supplementalActionsEnabled")
    public Boolean getSupplementalActionsEnabled() {
        return this.supplementalActionsEnabled;
    }

    public void setSupplementalActionsEnabled(Boolean supplementalActionsEnabled) {
        this.supplementalActionsEnabled = supplementalActionsEnabled;
    }

    /**
     * Indicates that the action buttons like search in the criteria section should be rendered
     *
     * @return Boolean
     */
    @BeanTagAttribute(name = "renderSearchButtons")
    public Boolean getRenderSearchButtons() {
        return this.renderSearchButtons;
    }

    /**
     * Setter for the render search buttons flag
     *
     * @param renderSearchButtons
     */
    public void setRenderSearchButtons(Boolean renderSearchButtons) {
        this.renderSearchButtons = renderSearchButtons;
    }

    /**
     * Indicates whether the lookup header should be rendered
     *
     * <p>
     * Defaults to true. Can be set as bean property or passed as a request parameter in the lookup url.
     * </p>
     *
     * @return boolean
     */
    @BeanTagAttribute(name = "renderHeader")
    public Boolean getRenderHeader() {
        return this.renderHeader;
    }

    /**
     * Setter for the header render flag
     *
     * @param renderHeader
     */
    public void setRenderHeader(Boolean renderHeader) {
        this.renderHeader = renderHeader;
    }

    /**
     * Indicates whether the maintenance action links should be rendered for the invoked lookup view
     *
     * <p>
     * If a maintenance view exists for the data object associated with the lookup view, the framework will add
     * links to initiate a new maintenance document. This flag can be used to disable the rendering of these links
     * </p>
     *
     * <p>
     * Note this serves similar purpose to {@link #getSuppressActions()} but the intent is to only remove the
     * maintenance links in this situation, not the complete actions column TODO: this is not in place!
     * </p>
     *
     * @return true if maintenance links should be shown on the lookup view, false if not
     */
    @BeanTagAttribute(name = "showMaintenanceLinks")
    public Boolean getShowMaintenanceLinks() {
        return this.showMaintenanceLinks;
    }

    /**
     * Setter for the show maintenance links indicator
     *
     * @param showMaintenanceLinks
     */
    public void setShowMaintenanceLinks(Boolean showMaintenanceLinks) {
        this.showMaintenanceLinks = showMaintenanceLinks;
    }

    /**
     * Action component that is used to rendered for the field for invoking the quickfinder action (bringin up the
     * lookup)
     *
     * <p>
     * Through the action configuration the image (or link, button) rendered for the quickfinder can be modified. In
     * addition to other action component settings
     * </p>
     *
     * @return Action instance rendered for quickfinder
     */
    @BeanTagAttribute(name = "quickfinderAction", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Action getQuickfinderAction() {
        return this.quickfinderAction;
    }

    /**
     * Setter for the action field component to render for the quickfinder
     *
     * @param quickfinderAction
     */
    public void setQuickfinderAction(Action quickfinderAction) {
        this.quickfinderAction = quickfinderAction;
    }

    /**
     * Setter for the light box lookup widget
     *
     * @param lightBoxLookup <code>LightBoxLookup</code> widget to set
     */
    public void setLightBoxLookup(LightBox lightBoxLookup) {
        this.lightBoxLookup = lightBoxLookup;
    }

    /**
     * LightBoxLookup widget for the field
     *
     * <p>
     * The light box lookup widget will change the lookup behaviour to open the
     * lookup in a light box.
     * </p>
     *
     * @return the <code>DirectInquiry</code> field DirectInquiry
     */
    @BeanTagAttribute(name = "lightBoxLookup", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public LightBox getLightBoxLookup() {
        return lightBoxLookup;
    }

    /**
     * Indicates whether a multi-values lookup should be requested
     *
     * @return true if multi-value lookup should be requested, false for normal lookup
     */
    @BeanTagAttribute(name = "MultipleValuesSelect")
    public Boolean getMultipleValuesSelect() {
        return multipleValuesSelect;
    }

    /**
     * Setter for the multi-values lookup indicator
     *
     * @param multipleValuesSelect
     */
    public void setMultipleValuesSelect(Boolean multipleValuesSelect) {
        this.multipleValuesSelect = multipleValuesSelect;
    }

    /**
     * For the case of multi-value lookup, indicates the collection that should be populated with
     * the return results
     *
     * <p>
     * Note when the quickfinder is associated with a <code>CollectionGroup</code>, this property is
     * set automatically from the collection name associated with the group
     * </p>
     *
     * @return collection name (must be full binding path)
     */
    @BeanTagAttribute(name = "lookupCollectionName")
    public String getLookupCollectionName() {
        return lookupCollectionName;
    }

    /**
     * Setter for the name of the collection that should be populated with lookup results
     *
     * @param lookupCollectionName
     */
    public void setLookupCollectionName(String lookupCollectionName) {
        this.lookupCollectionName = lookupCollectionName;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        QuickFinder quickFinderCopy = (QuickFinder) component;
        quickFinderCopy.setBaseLookupUrl(this.getBaseLookupUrl());
        quickFinderCopy.setDataObjectClassName(this.getDataObjectClassName());
        quickFinderCopy.setViewName(this.getViewName());
        quickFinderCopy.setReferencesToRefresh(this.getReferencesToRefresh());

        if(fieldConversions != null) {
            Map<String, String> fieldConversionsCopy = Maps.newHashMapWithExpectedSize(fieldConversions.size());
            for(Map.Entry fieldConversion : fieldConversions.entrySet()) {
                fieldConversionsCopy.put(fieldConversion.getKey().toString(),fieldConversion.getValue().toString());
            }
            quickFinderCopy.setFieldConversions(fieldConversionsCopy);
        }

        if(lookupParameters != null) {
            Map<String, String> lookupParametersCopy = Maps.newHashMapWithExpectedSize(lookupParameters.size());
            for(Map.Entry lookupParameter : lookupParameters.entrySet()) {
                lookupParametersCopy.put(lookupParameter.getKey().toString(),lookupParameter.getValue().toString());
            }
            quickFinderCopy.setLookupParameters(lookupParametersCopy);
        }

        quickFinderCopy.setReadOnlySearchFields(this.getReadOnlySearchFields());
        quickFinderCopy.setHideReturnLink(this.getHideReturnLink());
        quickFinderCopy.setSuppressActions(this.getSuppressActions());
        quickFinderCopy.setAutoSearch(this.getAutoSearch());
        quickFinderCopy.setRenderLookupCriteria(this.getRenderLookupCriteria());
        quickFinderCopy.setSupplementalActionsEnabled(this.getSupplementalActionsEnabled());
        quickFinderCopy.setRenderSearchButtons(this.getRenderSearchButtons());
        quickFinderCopy.setRenderHeader(this.getRenderHeader());
        quickFinderCopy.setShowMaintenanceLinks(this.getShowMaintenanceLinks());
        quickFinderCopy.setMultipleValuesSelect(this.getMultipleValuesSelect());
        quickFinderCopy.setLookupCollectionName(this.getLookupCollectionName());

        if(lightBoxLookup != null) {
            quickFinderCopy.setLightBoxLookup((LightBox)this.getLightBoxLookup().copy());
        }

        if (this.quickfinderAction != null) {
            quickFinderCopy.setQuickfinderAction((Action)this.quickfinderAction.copy());
        }
    }
}
