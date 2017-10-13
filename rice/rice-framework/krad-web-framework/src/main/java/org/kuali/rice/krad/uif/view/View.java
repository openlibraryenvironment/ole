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
package org.kuali.rice.krad.uif.view;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.DataDictionary;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.parse.BeanTags;
import org.kuali.rice.krad.datadictionary.state.StateMapping;
import org.kuali.rice.krad.datadictionary.validator.ValidationTrace;
import org.kuali.rice.krad.datadictionary.validator.Validator;
import org.kuali.rice.krad.service.KRADServiceLocatorWeb;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifConstants.ViewStatus;
import org.kuali.rice.krad.uif.UifConstants.ViewType;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.ReferenceCopy;
import org.kuali.rice.krad.uif.component.RequestParameter;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.container.ContainerBase;
import org.kuali.rice.krad.uif.container.Group;
import org.kuali.rice.krad.uif.container.PageGroup;
import org.kuali.rice.krad.uif.element.Header;
import org.kuali.rice.krad.uif.element.Link;
import org.kuali.rice.krad.uif.element.ViewHeader;
import org.kuali.rice.krad.uif.layout.LayoutManager;
import org.kuali.rice.krad.uif.service.ViewHelperService;
import org.kuali.rice.krad.uif.util.BooleanMap;
import org.kuali.rice.krad.uif.util.BreadcrumbItem;
import org.kuali.rice.krad.uif.util.BreadcrumbOptions;
import org.kuali.rice.krad.uif.util.ClientValidationUtils;
import org.kuali.rice.krad.uif.util.CloneUtils;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.util.ComponentUtils;
import org.kuali.rice.krad.uif.util.ParentLocation;
import org.kuali.rice.krad.uif.util.ScriptUtils;
import org.kuali.rice.krad.uif.widget.BlockUI;
import org.kuali.rice.krad.uif.widget.Breadcrumbs;
import org.kuali.rice.krad.uif.widget.Growls;
import org.kuali.rice.krad.util.KRADUtils;
import org.kuali.rice.krad.util.ObjectUtils;
import org.kuali.rice.krad.web.form.HistoryFlow;
import org.kuali.rice.krad.web.form.UifFormBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Root of the component tree which encompasses a set of related
 * <code>GroupContainer</code> instances tied together with a common page layout
 * and navigation.
 *
 * <p>
 * The <code>View</code> component ties together all the components and
 * configuration of the User Interface for a piece of functionality. In Rice
 * applications the view is typically associated with a <code>Document</code>
 * instance.
 * </p>
 *
 * <p>
 * The view template lays out the common header, footer, and navigation for the
 * related pages. In addition the view renders the HTML head element bringing in
 * common script files and style sheets, along with optionally rendering a form
 * element for pages that need to post data back to the server.
 * </p>
 *
 * <p>
 * Configuration of UIF features such as model validation is also done through
 * the <code>View</code>
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTags({@BeanTag(name = "view-bean", parent = "Uif-View"),
        @BeanTag(name = "view-knsTheme-bean", parent = "Uif-View-KnsTheme")})
public class View extends ContainerBase {
    private static final long serialVersionUID = -1220009725554576953L;

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(View.class);

    private String namespaceCode;
    private String viewName;
    private ViewTheme theme;

    private int idSequence;

    private String stateObjectBindingPath;
    private StateMapping stateMapping;

    // view header setting
    private boolean unifiedHeader;

    // additional view group(s)
    private Group topGroup;

    // application
    private Header applicationHeader;
    private Group applicationFooter;

    // sticky flags
    private boolean stickyTopGroup;
    private boolean stickyBreadcrumbs;
    private boolean stickyHeader;
    private boolean stickyApplicationHeader;
    private boolean stickyFooter;
    private boolean stickyApplicationFooter;

    // Breadcrumbs
    private Breadcrumbs breadcrumbs;
    private BreadcrumbOptions breadcrumbOptions;
    private BreadcrumbItem breadcrumbItem;
    private ParentLocation parentLocation;
    private List<BreadcrumbItem> pathBasedBreadcrumbs;

    // Growls support
    private Growls growls;
    private boolean growlMessagingEnabled;

    private BlockUI refreshBlockUI;
    private BlockUI navigationBlockUI;

    private String entryPageId;

    @RequestParameter
    private String currentPageId;

    private Group navigation;

    private Class<?> formClass;
    private String defaultBindingObjectPath;
    private Map<String, Class<?>> objectPathToConcreteClassMapping;

    private List<String> additionalScriptFiles;
    private List<String> additionalCssFiles;
    private boolean useLibraryCssClasses;

    private ViewType viewTypeName;

    private String viewStatus;
    protected ViewIndex viewIndex;
    private Map<String, String> viewRequestParameters;

    private boolean persistFormToSession;
    private ViewSessionPolicy sessionPolicy;

    private ViewPresentationController presentationController;
    private ViewAuthorizer authorizer;

    private BooleanMap actionFlags;
    private BooleanMap editModes;

    private Map<String, String> expressionVariables;

    private boolean singlePageView;
    private boolean mergeWithPageItems;
    private PageGroup page;

    private List<? extends Group> items;
    private List<Group> dialogs;

    private Link viewMenuLink;
    private String viewMenuGroupName;

    private boolean applyDirtyCheck;
    private boolean translateCodesOnReadOnlyDisplay;
    private boolean supportsRequestOverrideOfReadOnlyFields;
    private boolean disableNativeAutocomplete;
    private boolean disableBrowserCache;

    private String preLoadScript;

    private List<String> viewTemplates;

    private Class<? extends ViewHelperService> viewHelperServiceClass;

    @ReferenceCopy
    private ViewHelperService viewHelperService;

    public View() {
        singlePageView = false;
        mergeWithPageItems = true;
        translateCodesOnReadOnlyDisplay = false;
        viewTypeName = ViewType.DEFAULT;
        viewStatus = ViewStatus.CREATED;
        formClass = UifFormBase.class;
        supportsRequestOverrideOfReadOnlyFields = true;
        disableBrowserCache = true;
        persistFormToSession = true;
        sessionPolicy = new ViewSessionPolicy();

        idSequence = 0;
        this.viewIndex = new ViewIndex();

        additionalScriptFiles = new ArrayList<String>();
        additionalCssFiles = new ArrayList<String>();
        items = new ArrayList<Group>();
        objectPathToConcreteClassMapping = new HashMap<String, Class<?>>();
        viewRequestParameters = new HashMap<String, String>();
        expressionVariables = new HashMap<String, String>();

        dialogs = new ArrayList<Group>();
        viewTemplates = new ArrayList<String>();
    }

    /**
     * The following initialization is performed:
     *
     * <ul>
     * <li>If a single paged view, set items in page group and put the page in
     * the items list</li>
     * <li>If {@link ViewSessionPolicy#enableTimeoutWarning} is enabled add the session timeout dialogs to the
     * views list of dialog groups</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.container.ContainerBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void performInitialization(View view, Object model) {
        super.performInitialization(view, model);

        // populate items on page for single paged view
        if (singlePageView) {
            if (page != null) {
                // remove default sections of page when requested
                if (!mergeWithPageItems) {
                    page.setItems(new ArrayList<Group>());
                }

                view.assignComponentIds(page);

                // add the items configured on the view to the page items, and set as the
                // new page items
                List<Component> newItems = (List<Component>) page.getItems();
                newItems.addAll(items);
                page.setItems(newItems);

                // reset the items list to include the one page
                items = new ArrayList<Group>();
                ((List<Group>) items).add(page);
            } else {
                throw new RuntimeException("For single paged views the page Group must be set.");
            }
        }
        // if items is only size one and instance of page, set singlePageView to true
        else if ((this.items != null) && (this.items.size() == 1)) {
            Component itemComponent = this.items.get(0);

            if (itemComponent instanceof PageGroup) {
                this.singlePageView = true;
            }
        }

        if (sessionPolicy.isEnableTimeoutWarning()) {
            Group warningDialog = ComponentFactory.getSessionTimeoutWarningDialog();

            warningDialog.setId(ComponentFactory.SESSION_TIMEOUT_WARNING_DIALOG);
            view.assignComponentIds(warningDialog);
            getDialogs().add(warningDialog);

            Group timeoutDialog = ComponentFactory.getSessionTimeoutDialog();

            timeoutDialog.setId(ComponentFactory.SESSION_TIMEOUT_DIALOG);
            view.assignComponentIds(timeoutDialog);
            getDialogs().add(timeoutDialog);
        }

        breadcrumbOptions.setupBreadcrumbs(view, model);
    }

    /**
     * The following updates are done here:
     *
     * <ul>
     * <li>Invoke expression evaluation on view theme</li>
     * <li>Invoke theme to configure defaults</li>
     * </ul>
     */
    public void performApplyModel(View view, Object model, Component parent) {
        super.performApplyModel(view, model, parent);

        if (theme != null) {
            view.getViewHelperService().getExpressionEvaluator().evaluateExpressionsOnConfigurable(view, theme,
                    getContext());

            theme.configureThemeDefaults();
        }

        //handle parentLocation breadcrumb chain
        parentLocation.constructParentLocationBreadcrumbItems(view, model, view.getContext());
    }

    /**
     * The following is performed:
     *
     * <ul>
     * <li>Adds to its document ready script the setupValidator js function for setting
     * up the validator for this view</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.container.ContainerBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        String preLoadScript = "";
        if (this.getPreLoadScript() != null) {
            preLoadScript = this.getPreLoadScript();
        }

        // Retrieve Growl and BlockUI settings
        Growls gw = view.getGrowls();
        if (!gw.getTemplateOptions().isEmpty()) {
            preLoadScript += "setGrowlDefaults(" + gw.getTemplateOptionsJSString() + ");";
        }

        BlockUI navBlockUI = view.getNavigationBlockUI();
        if (!navBlockUI.getTemplateOptions().isEmpty()) {
            preLoadScript += "setBlockUIDefaults("
                    + navBlockUI.getTemplateOptionsJSString()
                    + ", '"
                    + UifConstants.BLOCKUI_NAVOPTS
                    + "');";
        }

        BlockUI refBlockUI = view.getRefreshBlockUI();
        if (!refBlockUI.getTemplateOptions().isEmpty()) {
            preLoadScript += "setBlockUIDefaults("
                    + refBlockUI.getTemplateOptionsJSString()
                    + ", '"
                    + UifConstants.BLOCKUI_REFRESHOPTS
                    + "');";
        }

        this.setPreLoadScript(preLoadScript);

        String onReadyScript = "";
        if (this.getOnDocumentReadyScript() != null) {
            onReadyScript = this.getOnDocumentReadyScript();
        }

        // initialize session timers for giving timeout warnings
        if (sessionPolicy.isEnableTimeoutWarning()) {
            // warning minutes gives us the time before the timeout occurs to give the warning,
            // so we need to determine how long that should be from the session start
            int sessionTimeoutInterval = ((UifFormBase) model).getSessionTimeoutInterval();
            int sessionWarningMilliseconds = (sessionPolicy.getTimeoutWarningSeconds() * 1000);

            if (sessionWarningMilliseconds >= sessionTimeoutInterval) {
                throw new RuntimeException(
                        "Time until giving the session warning should be less than the session timeout. Session Warning is "
                                + sessionWarningMilliseconds
                                + "ms, session timeout is "
                                + sessionTimeoutInterval
                                + "ms.");
            }

            int sessionWarningInterval = sessionTimeoutInterval - sessionWarningMilliseconds;

            onReadyScript = ScriptUtils.appendScript(onReadyScript, ScriptUtils.buildFunctionCall(
                    UifConstants.JsFunctions.INITIALIZE_SESSION_TIMERS, sessionWarningInterval,
                    sessionTimeoutInterval));
        }

        onReadyScript = ScriptUtils.appendScript(onReadyScript, "jQuery.extend(jQuery.validator.messages, "
                + ClientValidationUtils.generateValidatorMessagesOption()
                + ");");

        this.setOnDocumentReadyScript(onReadyScript);

        // breadcrumb handling
        breadcrumbOptions.finalizeBreadcrumbs(view, model, this, breadcrumbItem);

        // add validation default js options for validation framework to View's data attributes
        Object groupValidationDataDefaults = KRADServiceLocatorWeb.getDataDictionaryService().getDictionaryObject(
                UifConstants.GROUP_VALIDATION_DEFAULTS_MAP_ID);
        Object fieldValidationDataDefaults = KRADServiceLocatorWeb.getDataDictionaryService().getDictionaryObject(
                UifConstants.FIELD_VALIDATION_DEFAULTS_MAP_ID);

        this.addDataAttribute(UifConstants.DataAttributes.GROUP_VALIDATION_DEFAULTS, ScriptUtils.convertToJsValue(
                (Map<String, String>) groupValidationDataDefaults));
        this.addDataAttribute(UifConstants.DataAttributes.FIELD_VALIDATION_DEFAULTS, ScriptUtils.convertToJsValue(
                (Map<String, String>) fieldValidationDataDefaults));

        // give view role attribute for js selections
        this.addDataAttribute(UifConstants.DataAttributes.ROLE, "view");
    }

    /**
     * Assigns an id (if not configured) to a component and all its child components
     *
     * @param component component instance to assign id to
     */
    public void assignComponentIds(Component component) {
        if (component == null) {
            return;
        }

        int origIdSequence = -1;

        // Get the old sequence if the baseId exists in the sequence snapshot
        if (component.getBaseId() != null && viewIndex != null && viewIndex.getIdSequenceSnapshot() != null &&
                viewIndex.getIdSequenceSnapshot().containsKey(component.getBaseId())){
            origIdSequence = idSequence;
            idSequence = viewIndex.getIdSequenceSnapshot().get(component.getBaseId());
        }

        assignComponentId(component);

        // assign id to nested components
        List<Component> allNested = new ArrayList<Component>(component.getComponentsForLifecycle());
        allNested.addAll(component.getComponentPrototypes());
        for (Component nestedComponent : allNested) {
            assignComponentIds(nestedComponent);
        }

        if (origIdSequence != -1){
            idSequence = origIdSequence;
        }
    }

    /**
     * Special handling of assigning ids for the view component to assure each page and all its children gets an
     * id and the page ids are always the same
     *
     * <p>
     * First the pages are assigned ids sequentially so they always get the same ids regardless of what page is
     * being displayed. Also, since the view doesn't include pages that are not being displayed (or are not the current
     * page) in its tree (components for lifecycle) we need to loop through them explicity and assign ids
     * </p>
     *
     * @param view view instance containing the pages
     */
    protected void assignPageIds(View view) {
        // single page view
        if (view.isSinglePageView() && view.getPage() != null) {
            assignComponentId(view.getPage());

            return;
        }

        // mult-page view
        if (view.getItems() != null) {
            // get each page and set the id, assigning to just the pages first so they will have the
            // first sequential ids
            for (Component item : view.getItems()) {
                if (item instanceof PageGroup) {
                    assignComponentId(item);
                }
            }

            // now assign ids for all page child components
            for (Component item : view.getItems()) {
                if (item instanceof PageGroup) {
                    assignComponentIds(item);
                }
            }
        }
    }

    /**
     * Assigns an id to the given component
     *
     * @param component component to assign id to
     */
    protected void assignComponentId(Component component) {
        Integer currentSequenceVal = idSequence;

        // assign ID if necessary
        if (StringUtils.isBlank(component.getId())) {
            component.setId(UifConstants.COMPONENT_ID_PREFIX + getNextId());
        }

        // capture current sequence value for component refreshes
        getViewIndex().addSequenceValueToSnapshot(component.getId(), currentSequenceVal);

        if (component instanceof Container) {
            LayoutManager layoutManager = ((Container) component).getLayoutManager();

            if ((layoutManager != null) && StringUtils.isBlank(layoutManager.getId())) {
                layoutManager.setId(UifConstants.COMPONENT_ID_PREFIX + getNextId());
            }
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = new ArrayList<Component>();

        components.add(applicationHeader);
        components.add(applicationFooter);
        components.add(topGroup);
        components.add(navigation);
        components.add(breadcrumbs);
        components.add(growls);
        components.addAll(dialogs);
        components.add(viewMenuLink);
        components.add(navigationBlockUI);
        components.add(refreshBlockUI);
        components.add(breadcrumbItem);

        if (parentLocation != null) {
            components.add(parentLocation.getPageBreadcrumbItem());
            components.add(parentLocation.getViewBreadcrumbItem());
            components.addAll(parentLocation.getResolvedBreadcrumbItems());
        }

        // Note super items should be added after navigation and other view components so
        // conflicting ids between nav and page do not occur on page navigation via ajax
        components.addAll(super.getComponentsForLifecycle());

        // remove all pages that are not the current page
        if (!singlePageView && (this.getItems() != null)) {
            for (Group group : this.getItems()) {
                if ((group instanceof PageGroup) && !StringUtils.equals(group.getId(), getCurrentPageId()) && components
                        .contains(group)) {
                    components.remove(group);
                }
            }
        }

        return components;
    }

    /**
     * @see org.kuali.rice.krad.uif.container.Container#getSupportedComponents()
     */
    @Override
    public Set<Class<? extends Component>> getSupportedComponents() {
        Set<Class<? extends Component>> supportedComponents = new HashSet<Class<? extends Component>>();
        supportedComponents.add(Group.class);

        return supportedComponents;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#getComponentTypeName()
     */
    @Override
    public String getComponentTypeName() {
        return "view";
    }

    /**
     * Iterates through the contained page items and returns the Page that
     * matches the set current page id
     *
     * @return Page instance
     */
    public PageGroup getCurrentPage() {
        for (Group pageGroup : this.getItems()) {
            if (StringUtils.equals(pageGroup.getId(), getCurrentPageId()) && pageGroup instanceof PageGroup) {
                return (PageGroup) pageGroup;
            }
        }

        return null;
    }

    /**
     * Override sort method to prevent sorting in the case of a single page view, since the items
     * will get pushed into the configured page and sorted through the page
     */
    @Override
    protected void sortItems(View view, Object model) {
        if (!singlePageView) {
            super.sortItems(view, model);
        }
    }

    /**
     * Namespace code the view should be associated with
     *
     * <p>
     * The namespace code is used within the framework in such places as permission checks and parameter
     * retrieval
     * </p>
     *
     * @return namespace code
     */
    @BeanTagAttribute(name = "namespaceCode")
    public String getNamespaceCode() {
        return namespaceCode;
    }

    /**
     * Setter for the view's namespace code
     *
     * @param namespaceCode
     */
    public void setNamespaceCode(String namespaceCode) {
        this.namespaceCode = namespaceCode;
    }

    /**
     * View name provides an identifier for a view within a type. That is if a
     * set of <code>View</code> instances have the same values for the
     * properties that are used to retrieve them by their type, the name can be
     * given to further qualify the view that should be retrieved.
     * <p>
     * A view type like the <code>LookupView</code> might have several views for
     * the same object class, but one that is the 'default' lookup and another
     * that is the 'advanced' lookup. Therefore the name on the first could be
     * set to 'default', and likewise the name for the second 'advanced'.
     * </p>
     *
     * @return name of view
     */
    @BeanTagAttribute(name = "viewName")
    public String getViewName() {
        return this.viewName;
    }

    /**
     * Setter for the view's name
     *
     * @param viewName
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    /**
     * When true, this view will use a unified header - the page header will be omitted and its title will be used
     * in the ViewHeader supportTitle property (dynamically updated on page change)
     *
     * @return true if using a unified header
     */
    @BeanTagAttribute(name = "unifiedHeader")
    public boolean isUnifiedHeader() {
        return unifiedHeader;
    }

    /**
     * Set to true, to use unified header functionality
     *
     * @param unifiedHeader
     */
    public void setUnifiedHeader(boolean unifiedHeader) {
        this.unifiedHeader = unifiedHeader;
    }

    /**
     * TopGroup is an optional group of content that appears above the breadcrumbs and view header
     *
     * @return the topGroup component
     */
    @BeanTagAttribute(name = "topGroup", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Group getTopGroup() {
        return topGroup;
    }

    /**
     * Set the topGroup component which appears the breadcrumbs and view header
     *
     * @param topGroup
     */
    public void setTopGroup(Group topGroup) {
        this.topGroup = topGroup;
    }

    /**
     * Header for the application containing the view
     *
     * <p>
     * When deploying outside a portal, the application header and footer property can be configured to
     * display a consistent header/footer across all views. Here application logos, menus, login controls
     * and so on can be rendered.
     * </p>
     *
     * @return application header
     */
    @BeanTagAttribute(name = "applicationHeader", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Header getApplicationHeader() {
        return applicationHeader;
    }

    /**
     * Setter for the application header
     *
     * @param applicationHeader
     */
    public void setApplicationHeader(Header applicationHeader) {
        this.applicationHeader = applicationHeader;
    }

    /**
     * Footer for the application containing the view
     *
     * <p>
     * When deploying outside a portal, the application header and footer property can be configured to
     * display a consistent header/footer across all views. Here such things as application links, copyrights
     * and so on can be rendered.
     * </p>
     *
     * @return application footer
     */
    @BeanTagAttribute(name = "applicationFooter", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Group getApplicationFooter() {
        return applicationFooter;
    }

    /**
     * Setter for the application footer
     *
     * @param applicationFooter
     */
    public void setApplicationFooter(Group applicationFooter) {
        this.applicationFooter = applicationFooter;
    }

    /**
     * If true, the top group will be sticky (fixed to top of window)
     *
     * @return true if the top group is sticky, false otherwise
     */
    @BeanTagAttribute(name = "stickyTopGroup")
    public boolean isStickyTopGroup() {
        return stickyTopGroup;
    }

    /**
     * Set to true to make the top group sticky (fixed to top of window)
     *
     * @param stickyTopGroup
     */
    public void setStickyTopGroup(boolean stickyTopGroup) {
        this.stickyTopGroup = stickyTopGroup;
    }

    /**
     * If true, the breadcrumb widget will be sticky (fixed to top of window)
     *
     * @return true if breadcrumbs are sticky, false otherwise
     */
    @BeanTagAttribute(name = "stickyBreadcrumbs")
    public boolean isStickyBreadcrumbs() {
        return stickyBreadcrumbs;
    }

    /**
     * Set to true to make the breadcrumbs sticky
     *
     * @param stickyBreadcrumbs
     */
    public void setStickyBreadcrumbs(boolean stickyBreadcrumbs) {
        this.stickyBreadcrumbs = stickyBreadcrumbs;
    }

    /**
     * If true, the ViewHeader for this view will be sticky (fixed to top of window)
     *
     * @return true if the header is sticky, false otherwise
     */
    @BeanTagAttribute(name = "stickyHeader")
    public boolean isStickyHeader() {
        if (this.getHeader() != null && this.getHeader() instanceof ViewHeader) {
            return ((ViewHeader) this.getHeader()).isSticky();
        } else {
            return false;
        }
    }

    /**
     * Set to true to make the ViewHeader sticky
     *
     * @param stickyHeader
     */
    public void setStickyHeader(boolean stickyHeader) {
        this.stickyHeader = stickyHeader;
        if (this.getHeader() != null && this.getHeader() instanceof ViewHeader) {
            ((ViewHeader) this.getHeader()).setSticky(stickyHeader);
        }
    }

    /**
     * Set to true to make the applicationHeader sticky (fixed to top of window)
     *
     * @return true if applicationHeader is sticky, false otherwise
     */
    @BeanTagAttribute(name = "stickyApplicationHeader")
    public boolean isStickyApplicationHeader() {
        return stickyApplicationHeader;
    }

    /**
     * Set to true to make the applicationHeader sticky
     *
     * @param stickyApplicationHeader
     */
    public void setStickyApplicationHeader(boolean stickyApplicationHeader) {
        this.stickyApplicationHeader = stickyApplicationHeader;
    }

    /**
     * If true, the view footer will become sticky (fixed to bottom of window)
     *
     * @return ture if the view footer is sticky, false otherwise
     */
    @BeanTagAttribute(name = "stickyFooter")
    public boolean isStickyFooter() {
        return stickyFooter;
    }

    /**
     * Set to true to make the view footer sticky
     *
     * @param stickyFooter
     */
    public void setStickyFooter(boolean stickyFooter) {
        this.stickyFooter = stickyFooter;
        if (this.getFooter() != null) {
            this.getFooter().addDataAttribute(UifConstants.DataAttributes.STICKY_FOOTER, Boolean.toString(
                    stickyFooter));
        }
    }

    /**
     * If true, the applicationFooter will become sticky (fixed to bottom of window)
     *
     * @return true if the application footer is sticky, false otherwise
     */
    @BeanTagAttribute(name = "stickyApplicationFooter")
    public boolean isStickyApplicationFooter() {
        return stickyApplicationFooter;
    }

    /**
     * Set to true to make the application footer sticky
     *
     * @param stickyApplicationFooter
     */
    public void setStickyApplicationFooter(boolean stickyApplicationFooter) {
        this.stickyApplicationFooter = stickyApplicationFooter;
    }

    /**
     * Current sequence value for id assignment
     *
     * @return id sequence
     */
    public int getIdSequence() {
        return idSequence;
    }

    /**
     * Setter for the current id sequence value
     *
     * @param idSequence
     */
    public void setIdSequence(int idSequence) {
        this.idSequence = idSequence;
    }

    /**
     * Returns the next unique id available for components within the view instance
     *
     * @return next id available
     */
    public String getNextId() {
        idSequence += 1;
        return Integer.toString(idSequence);
    }

    /**
     * Specifies what page should be rendered by default. This is the page that
     * will be rendered when the <code>View</code> is first rendered or when the
     * current page is not set
     *
     * @return id of the page to render by default
     */
    @BeanTagAttribute(name = "entryPageId")
    public String getEntryPageId() {
        return this.entryPageId;
    }

    /**
     * Setter for default Page id
     *
     * @param entryPageId
     */
    public void setEntryPageId(String entryPageId) {
        this.entryPageId = entryPageId;
    }

    /**
     * The id for the page within the view that should be displayed in the UI.
     * Other pages of the view will not be rendered
     *
     * <p>
     * If current page id is not set, it is set to the configured entry page or first item in list id
     * </p>
     *
     * @return id of the page that should be displayed
     */
    public String getCurrentPageId() {
        // default current page if not set
        if (StringUtils.isBlank(currentPageId)) {
            if (StringUtils.isNotBlank(entryPageId)) {
                currentPageId = entryPageId;
            } else if ((getItems() != null) && !getItems().isEmpty()) {
                Group firstPageGroup = getItems().get(0);
                currentPageId = firstPageGroup.getId();
            }
        }

        return this.currentPageId;
    }

    /**
     * Setter for the page id to display
     *
     * @param currentPageId
     */
    public void setCurrentPageId(String currentPageId) {
        this.currentPageId = currentPageId;
    }

    /**
     * <code>NavigationGroup</code> instance for the <code>View</code>
     * <p>
     * Provides configuration necessary to render the navigation. This includes
     * navigation items in addition to configuration for the navigation
     * renderer.
     * </p>
     *
     * @return NavigationGroup
     */
    @BeanTagAttribute(name = "navigation", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Group getNavigation() {
        return this.navigation;
    }

    /**
     * Setter for the View's <code>NavigationGroup</code>
     *
     * @param navigation
     */
    public void setNavigation(Group navigation) {
        this.navigation = navigation;
    }

    /**
     * Class of the Form that should be used with the <code>View</code>
     * instance. The form is the top level object for all the view's data and is
     * used to present and accept data in the user interface. All form classes
     * should extend UifFormBase
     *
     * @return class for the view's form
     * @see org.kuali.rice.krad.web.form.UifFormBase
     */
    @BeanTagAttribute(name = "formClass")
    public Class<?> getFormClass() {
        return this.formClass;
    }

    /**
     * Setter for the form class
     *
     * @param formClass
     */
    public void setFormClass(Class<?> formClass) {
        this.formClass = formClass;
    }

    /**
     * For <code>View</code> types that work primarily with one nested object of
     * the form (for instance document, or bo) the default binding object path
     * can be set for each of the views <code>DataBinding</code> components. If
     * the component does not set its own binding object path it will inherit
     * the default
     *
     * @return binding path to the object from the form
     */
    @BeanTagAttribute(name = "defaultObjectPath")
    public String getDefaultBindingObjectPath() {
        return this.defaultBindingObjectPath;
    }

    /**
     * Setter for the default binding object path to use for the view
     *
     * @param defaultBindingObjectPath
     */
    public void setDefaultBindingObjectPath(String defaultBindingObjectPath) {
        this.defaultBindingObjectPath = defaultBindingObjectPath;
    }

    /**
     * Configures the concrete classes that will be used for properties in the
     * form object graph that have an abstract or interface type
     *
     * <p>
     * For properties that have an abstract or interface type, it is not
     * possible to perform operations like getting/settings property values and
     * getting information from the dictionary. When these properties are
     * encountered in the object graph, this <code>Map</code> will be consulted
     * to determine the concrete type to use.
     * </p>
     *
     * <p>
     * e.g. Suppose we have a property document.accountingLine.accountNumber and
     * the accountingLine property on the document instance has an interface
     * type 'AccountingLine'. We can then put an entry into this map with key
     * 'document.accountingLine', and value
     * 'org.kuali.rice.sampleapp.TravelAccountingLine'. When getting the
     * property type or an entry from the dictionary for accountNumber, the
     * TravelAccountingLine class will be used.
     * </p>
     *
     * @return Map<String, Class> of class implementations keyed by path
     */
    @BeanTagAttribute(name = "objectPathConcreteClassMapping", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, Class<?>> getObjectPathToConcreteClassMapping() {
        return this.objectPathToConcreteClassMapping;
    }

    /**
     * Setter for the Map of class implementations keyed by path
     *
     * @param objectPathToConcreteClassMapping
     */
    public void setObjectPathToConcreteClassMapping(Map<String, Class<?>> objectPathToConcreteClassMapping) {
        this.objectPathToConcreteClassMapping = objectPathToConcreteClassMapping;
    }

    /**
     * Declares additional script files that should be included with the
     * <code>View</code>. These files are brought into the HTML page along with
     * common script files configured for the Rice application. Each entry
     * contain the path to the CSS file, either a relative path, path from web
     * root, or full URI
     * <p>
     * e.g. '/krad/scripts/myScript.js', '../scripts/myScript.js',
     * 'http://my.edu/web/myScript.js'
     * </p>
     *
     * @return script file locations
     */
    @BeanTagAttribute(name = "additionalScriptFiles", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getAdditionalScriptFiles() {
        return this.additionalScriptFiles;
    }

    /**
     * Setter for the List of additional script files to included with the
     * <code>View</code>
     *
     * @param additionalScriptFiles
     */
    public void setAdditionalScriptFiles(List<String> additionalScriptFiles) {
        this.additionalScriptFiles = additionalScriptFiles;
    }

    /**
     * Declares additional CSS files that should be included with the
     * <code>View</code>. These files are brought into the HTML page along with
     * common CSS files configured for the Rice application. Each entry should
     * contain the path to the CSS file, either a relative path, path from web
     * root, or full URI
     * <p>
     * e.g. '/krad/css/stacked-view.css', '../css/stacked-view.css',
     * 'http://my.edu/web/stacked-view.css'
     * </p>
     *
     * @return CSS file locations
     */
    @BeanTagAttribute(name = "additionalCssFiles", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getAdditionalCssFiles() {
        return this.additionalCssFiles;
    }

    /**
     * Setter for the List of additional CSS files to included with the
     * <code>View</code>
     *
     * @param additionalCssFiles
     */
    public void setAdditionalCssFiles(List<String> additionalCssFiles) {
        this.additionalCssFiles = additionalCssFiles;
    }

    /**
     * True if the libraryCssClasses set on components will be output to their class attribute, false otherwise.
     *
     * @return true if using libraryCssClasses on components
     */
    public boolean isUseLibraryCssClasses() {
        return useLibraryCssClasses;
    }

    /**
     * Set useLibraryCssClasses
     *
     * @param useLibraryCssClasses
     */
    public void setUseLibraryCssClasses(boolean useLibraryCssClasses) {
        this.useLibraryCssClasses = useLibraryCssClasses;
    }

    /**
     * List of templates that are used to render the view
     *
     * <p>
     * This list will be populated by unique template names as the components of the view are being processed.
     * Additional templates can be added in the view configuration if desired. At the beginning of the the view
     * rendering, each template in the list will then be included or processed by the template language
     * </p>
     *
     * <p>
     * Note the user of this depends on the template language being used for rendering. Some languages might require
     * including the template for each component instance (for example JSP templates). While others might simply
     * include markup that is then available for rendering each component instance (for example FreeMarker which has
     * a macro for each component that the template defines)
     * </p>
     *
     * @return list of template names that should be included for rendering the view
     */
    public List<String> getViewTemplates() {
        return viewTemplates;
    }

    /**
     * Setter for the the list of template names that should be included to render the view
     *
     * @param viewTemplates
     */
    public void setViewTemplates(List<String> viewTemplates) {
        this.viewTemplates = viewTemplates;
    }

    /**
     * View type name the view is associated with the view instance
     *
     * <p>
     * Views that share common features and functionality can be grouped by the
     * view type. Usually view types extend the <code>View</code> class to
     * provide additional configuration and to set defaults. View types can also
     * implement the <code>ViewTypeService</code> to add special indexing and
     * retrieval of views.
     * </p>
     *
     * @return view type name for the view
     */
    @BeanTagAttribute(name = "viewTypeName", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public ViewType getViewTypeName() {
        return this.viewTypeName;
    }

    /**
     * Setter for the view's type name
     *
     * @param viewTypeName
     */
    public void setViewTypeName(ViewType viewTypeName) {
        this.viewTypeName = viewTypeName;
    }

    /**
     * Class name of the <code>ViewHelperService</code> that handles the various
     * phases of the Views lifecycle
     *
     * @return Class for the spring bean
     * @see org.kuali.rice.krad.uif.service.ViewHelperService
     */
    @BeanTagAttribute(name = "viewHelperServiceClass")
    public Class<? extends ViewHelperService> getViewHelperServiceClass() {
        return this.viewHelperServiceClass;
    }

    /**
     * Setter for the <code>ViewHelperService</code> class name
     * Also initializes the viewHelperService
     *
     * @param viewHelperServiceClass
     */
    public void setViewHelperServiceClass(Class<? extends ViewHelperService> viewHelperServiceClass) {
        this.viewHelperServiceClass = viewHelperServiceClass;
        if ((this.viewHelperService == null) && (this.viewHelperServiceClass != null)) {
            viewHelperService = ObjectUtils.newInstance(viewHelperServiceClass);
        }
    }

    /**
     * Creates the <code>ViewHelperService</code> associated with the View
     *
     * @return ViewHelperService instance
     */
    @BeanTagAttribute(name = "viewHelperService", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public ViewHelperService getViewHelperService() {
        return viewHelperService;
    }

    /**
     * Setter for the <code>ViewHelperService</code>
     *
     * @param viewHelperService
     */
    public void setViewHelperService(ViewHelperService viewHelperService) {
        this.viewHelperService = viewHelperService;
    }

    /**
     * Invoked to produce a ViewIndex of the current view's components
     */
    public void index() {
        if (this.viewIndex == null) {
            this.viewIndex = new ViewIndex();
        }
        this.viewIndex.index(this);
    }

    /**
     * Holds field indexes of the <code>View</code> instance for retrieval
     *
     * @return ViewIndex instance
     */
    @BeanTagAttribute(name = "viewIndex", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public ViewIndex getViewIndex() {
        return this.viewIndex;
    }

    /**
     * Map of parameters from the request that set view options, used to rebuild
     * the view on each post
     *
     * <p>
     * Views can be configured by parameters. These might impact which parts of
     * the view are rendered or how the view behaves. Generally these would get
     * passed in when a new view is requested (by request parameters). These
     * will be used to initially populate the view properties. In addition, on a
     * post the view will be rebuilt and properties reset again by the allow
     * request parameters.
     * </p>
     *
     * <p>
     * Example parameter would be for MaintenaceView whether a New, Edit, or
     * Copy was requested (maintenance mode)
     * </p>
     *
     * @return
     */
    public Map<String, String> getViewRequestParameters() {
        return this.viewRequestParameters;
    }

    /**
     * Setter for the view's request parameters map
     *
     * @param viewRequestParameters
     */
    public void setViewRequestParameters(Map<String, String> viewRequestParameters) {
        this.viewRequestParameters = viewRequestParameters;
    }

    /**
     * Indicates whether the form (model) associated with the view should be stored in the user session
     *
     * <p>
     * The form class (or model) is used to hold the data that backs the view along with the built view object. Storing
     * the form instance in session allows many things:
     *
     * <ul>
     * <li>Data does not need to be rebuilt for each server request (for example a collection)</li>
     * <li>Data that does not need to go to the user can remain on the form, reducing the size of the response and
     * improving security</li>
     * <li>Data can be keep around in a 'pre-save' state. When requested by the user changes can then be persisted to
     * the database</li>
     * <li>Certain information about the view that was rendered, such as input fields, collection paths, and refresh
     * components can be kept on the form to support UI interaction</li>
     * </ul>
     *
     * Setting this flag to false will prevent the form from being kept in session and as a result will limit what can
     * be done by the framework. In almost all cases this is not recommended.
     * </p>
     *
     * <p>
     * Note all forms will be cleared when the user session expires (based on the rice configuration). In addition, the
     * framework enables clear points on certain actions to remove the form when it is no longer needed
     * </p>
     *
     * @return true if the form should be stored in the user session, false if only request based
     */
    @BeanTagAttribute(name = "persistFormToSession")
    public boolean isPersistFormToSession() {
        return persistFormToSession;
    }

    /**
     * Setter for the persist form to session indicator
     *
     * @param persistFormToSession
     */
    public void setPersistFormToSession(boolean persistFormToSession) {
        this.persistFormToSession = persistFormToSession;
    }

    public ViewSessionPolicy getSessionPolicy() {
        return sessionPolicy;
    }

    public void setSessionPolicy(ViewSessionPolicy sessionPolicy) {
        this.sessionPolicy = sessionPolicy;
    }

    /**
     * PresentationController that should be used for the <code>View</code> instance
     *
     * <p>
     * The presentation controller is consulted to determine component (group,
     * field) state such as required, read-only, and hidden. The presentation
     * controller does not take into account user permissions. The presentation
     * controller can also output action flags and edit modes that will be set
     * onto the view instance and can be referred to by conditional expressions
     * </p>
     *
     * @return PresentationController
     */
    @BeanTagAttribute(name = "presentationController", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public ViewPresentationController getPresentationController() {
        return this.presentationController;
    }

    /**
     * Setter for the view's presentation controller
     *
     * @param presentationController
     */
    public void setPresentationController(ViewPresentationController presentationController) {
        this.presentationController = presentationController;
    }

    /**
     * Setter for the view's presentation controller by class
     *
     * @param presentationControllerClass
     */
    public void setPresentationControllerClass(
            Class<? extends ViewPresentationController> presentationControllerClass) {
        this.presentationController = ObjectUtils.newInstance(presentationControllerClass);
    }

    /**
     * Authorizer that should be used for the <code>View</code> instance
     *
     * <p>
     * The authorizer class is consulted to determine component (group, field)
     * state such as required, read-only, and hidden based on the users
     * permissions. It typically communicates with the Kuali Identity Management
     * system to determine roles and permissions. It is used with the
     * presentation controller and dictionary conditional logic to determine the
     * final component state. The authorizer can also output action flags and
     * edit modes that will be set onto the view instance and can be referred to
     * by conditional expressions
     * </p>
     *
     * @return Authorizer
     */
    @BeanTagAttribute(name = "authorizer", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public ViewAuthorizer getAuthorizer() {
        return this.authorizer;
    }

    /**
     * Setter for the view's authorizer
     *
     * @param authorizer
     */
    public void setAuthorizer(ViewAuthorizer authorizer) {
        this.authorizer = authorizer;
    }

    /**
     * Setter for the view's authorizer by class
     *
     * @param authorizerClass
     */
    public void setAuthorizerClass(Class<? extends ViewAuthorizer> authorizerClass) {
        this.authorizer = ObjectUtils.newInstance(authorizerClass);
    }

    /**
     * Map of strings that flag what actions can be taken in the UI
     * <p>
     * These can be used in conditional expressions in the dictionary or by
     * other UI logic
     * </p>
     *
     * @return action flags
     */
    @BeanTagAttribute(name = "actionFlags", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public BooleanMap getActionFlags() {
        return this.actionFlags;
    }

    /**
     * Setter for the action flags Map
     *
     * @param actionFlags
     */
    public void setActionFlags(BooleanMap actionFlags) {
        this.actionFlags = actionFlags;
    }

    /**
     * Map of edit modes that enabled for the view
     *
     * <p>
     * These can be used in conditional expressions in the dictionary or by
     * other UI logic
     * </p>
     *
     * @return edit modes
     */
    @BeanTagAttribute(name = "editModes", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public BooleanMap getEditModes() {
        return this.editModes;
    }

    /**
     * Setter for the edit modes Map
     *
     * @param editModes
     */
    public void setEditModes(BooleanMap editModes) {
        this.editModes = editModes;
    }

    /**
     * Map that contains expressions to evaluate and make available as variables
     * for conditional expressions within the view
     *
     * <p>
     * Each Map entry contains one expression variables, where the map key gives
     * the name for the variable, and the map value gives the variable
     * expression. The variables expressions will be evaluated before
     * conditional logic is run and made available as variables for other
     * conditional expressions. Variable expressions can be based on the model
     * and any object contained in the view's context
     * </p>
     *
     * @return variable expressions
     */
    @BeanTagAttribute(name = "expressionVariables", type = BeanTagAttribute.AttributeType.MAPVALUE)
    public Map<String, String> getExpressionVariables() {
        return this.expressionVariables;
    }

    /**
     * Setter for the view's map of variable expressions
     *
     * @param expressionVariables
     */
    public void setExpressionVariables(Map<String, String> expressionVariables) {
        this.expressionVariables = expressionVariables;
    }

    /**
     * Indicates whether the <code>View</code> only has a single page
     * <code>Group</code> or contains multiple page <code>Group</code>
     * instances. In the case of a single page it is assumed the group's items
     * list contains the section groups for the page, and the page itself is
     * given by the page property ({@link #getPage()}. This is for convenience
     * of configuration and also can drive other configuration like styling.
     *
     * @return true if the view only contains one page group, false if
     *         it contains multple pages
     */
    @BeanTagAttribute(name = "singlePageView")
    public boolean isSinglePageView() {
        return this.singlePageView;
    }

    /**
     * Setter for the single page indicator
     *
     * @param singlePageView
     */
    public void setSinglePageView(boolean singlePageView) {
        this.singlePageView = singlePageView;
    }

    /**
     * Indicates whether the default sections specified in the page items list
     * should be included for this view.  This only applies to single paged views.
     *
     * @return true if the view should contain the default sections
     *         specified in the page
     */
    public boolean isMergeWithPageItems() {
        return mergeWithPageItems;
    }

    /**
     * Setter for the include page default sections indicator
     *
     * @param mergeWithPageItems
     */
    public void setMergeWithPageItems(boolean mergeWithPageItems) {
        this.mergeWithPageItems = mergeWithPageItems;
    }

    /**
     * For single paged views ({@link #isSinglePageView()}, gives the page
     * <code>Group</code> the view should render. The actual items for the page
     * is taken from the group's items list ({@link #getItems()}, and set onto
     * the give page group. This is for convenience of configuration.
     *
     * @return page group for single page views
     */
    @BeanTagAttribute(name = "page", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public PageGroup getPage() {
        return this.page;
    }

    /**
     * Setter for the page group for single page views
     *
     * @param page
     */
    public void setPage(PageGroup page) {
        this.page = page;
    }

    /**
     * @see org.kuali.rice.krad.uif.container.ContainerBase#getItems()
     */
    @Override
    @BeanTagAttribute(name = "items", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<? extends Group> getItems() {
        return this.items;
    }

    /**
     * Setter for the view's <code>Group</code> instances
     *
     * @param items
     */
    @Override
    public void setItems(List<? extends Component> items) {
        // TODO: fix this generic issue
        this.items = (List<? extends Group>) items;
    }

    /**
     * Provide a list of dialog groups associated with this view
     *
     * @return List of dialog Groups
     */
    @BeanTagAttribute(name = "dialogs", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<Group> getDialogs() {
        return dialogs;
    }

    /**
     * Sets the list of dialog groups for this view
     *
     * @param dialogs List of dialog groups
     */
    public void setDialogs(List<Group> dialogs) {
        this.dialogs = dialogs;
    }

    /**
     * Provides configuration for displaying a link to the view from an
     * application menu
     *
     * @return view link field
     */
    @BeanTagAttribute(name = "viewMenuLink", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Link getViewMenuLink() {
        return this.viewMenuLink;
    }

    /**
     * Setter for the views link field
     *
     * @param viewMenuLink
     */
    public void setViewMenuLink(Link viewMenuLink) {
        this.viewMenuLink = viewMenuLink;
    }

    /**
     * Provides a grouping string for the view to group its menu link (within a
     * portal for instance)
     *
     * @return menu grouping
     */
    @BeanTagAttribute(name = "viewMenuGroupName")
    public String getViewMenuGroupName() {
        return this.viewMenuGroupName;
    }

    /**
     * Setter for the views menu grouping
     *
     * @param viewMenuGroupName
     */
    public void setViewMenuGroupName(String viewMenuGroupName) {
        this.viewMenuGroupName = viewMenuGroupName;
    }

    /**
     * Indicates what lifecycle phase the View instance is in
     *
     * <p>
     * The view lifecycle begins with the CREATED status. In this status a new
     * instance of the view has been retrieved from the dictionary, but no
     * further processing has been done. After the initialize phase has been run
     * the status changes to INITIALIZED. After the model has been applied and
     * the view is ready for render the status changes to FINAL
     * </p>
     *
     * @return view status
     * @see org.kuali.rice.krad.uif.UifConstants.ViewStatus
     */
    public String getViewStatus() {
        return this.viewStatus;
    }

    /**
     * Setter for the view status
     *
     * @param viewStatus
     */
    public void setViewStatus(String viewStatus) {
        this.viewStatus = viewStatus;
    }

    /**
     * Indicates whether the view has been initialized
     *
     * @return true if the view has been initialized, false if not
     */
    public boolean isInitialized() {
        return StringUtils.equals(viewStatus, ViewStatus.INITIALIZED) || StringUtils.equals(viewStatus,
                ViewStatus.FINAL);
    }

    /**
     * Indicates whether the view has been updated from the model and final
     * updates made
     *
     * @return true if the view has been updated, false if not
     */
    public boolean isFinal() {
        return StringUtils.equals(viewStatus, ViewStatus.FINAL);
    }

    /**
     * Breadcrumb widget used for displaying homeward path and history
     *
     * @return the breadcrumbs
     */
    @BeanTagAttribute(name = "breadcrumbs", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Breadcrumbs getBreadcrumbs() {
        return this.breadcrumbs;
    }

    /**
     * @param breadcrumbs the breadcrumbs to set
     */
    public void setBreadcrumbs(Breadcrumbs breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }

    /**
     * The breadcrumbOptions for this view.
     *
     * <p>Render options set at the view level are always ignored (only apply to
     * page level BreadcrumbOptions).  BreadcrumbOptions for homewardPathBreadcrumbs,
     * preViewBreadcrumbs, prePageBreadcrumbs,
     * and breadcrumbOverrides are inherited by
     * child pages unless they override them themselves.</p>
     *
     * @return the BreadcrumbOptions for this view
     */
    @BeanTagAttribute(name = "breadcrumbOptions", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public BreadcrumbOptions getBreadcrumbOptions() {
        return breadcrumbOptions;
    }

    /**
     * Set the breadcrumbOptions
     *
     * @param breadcrumbOptions
     */
    public void setBreadcrumbOptions(BreadcrumbOptions breadcrumbOptions) {
        this.breadcrumbOptions = breadcrumbOptions;
    }

    /**
     * The View's breadcrumbItem defines settings for the breadcrumb which appears in the breadcrumb list for this
     * view.
     *
     * @return the breadcrumbItem
     */
    @BeanTagAttribute(name = "breadcrumbItem", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public BreadcrumbItem getBreadcrumbItem() {
        return breadcrumbItem;
    }

    /**
     * Set the breadcrumbItem
     *
     * @param breadcrumbItem
     */
    public void setBreadcrumbItem(BreadcrumbItem breadcrumbItem) {
        this.breadcrumbItem = breadcrumbItem;
    }

    /**
     * The parentLocation defines urls that represent the parent of a View in a conceptial site hierarchy.
     *
     * <p>
     * By defining a parent with these urls defined, a breadcrumb chain can be generated and displayed automatically
     * before this View's breadcrumbItem(s).  To chain multiple views, the urls must be defining viewId and
     * controllerMapping settings instead of setting an href directly (this will end the chain).  If labels are
     * not set on parentLocations, the labels will attempt to be derived from parent views/pages breadcrumbItem
     * and headerText - if these contain expressions which cannot be evaluated in the current context an exception
     * will be thrown.
     * </p>
     *
     * @return the parentLocation
     */
    @BeanTagAttribute(name = "parentLocation", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public ParentLocation getParentLocation() {
        return parentLocation;
    }

    /**
     * Set the parentLocation
     *
     * @param parentLocation
     */
    public void setParentLocation(ParentLocation parentLocation) {
        this.parentLocation = parentLocation;
    }

    /**
     * The pathBasedBreadcrumbs for this View.  These can only be set by the framework.
     *
     * @return the path based breadcrumbs
     */
    public List<BreadcrumbItem> getPathBasedBreadcrumbs() {
        return pathBasedBreadcrumbs;
    }

    /**
     * The pathBasedBreadcrumbs for this View.  This has been added for copyProperties().
     *
     * @param pathBasedBreadcrumbs
     */
    public void setPathBasedBreadcrumbs(List<BreadcrumbItem> pathBasedBreadcrumbs) {
        this.pathBasedBreadcrumbs = pathBasedBreadcrumbs;
    }

    /**
     * Growls widget which sets up global settings for the growls used in this
     * view and its pages
     *
     * @return the growls
     */
    @BeanTagAttribute(name = "growls", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Growls getGrowls() {
        return this.growls;
    }

    /**
     * @param growls the growls to set
     */
    public void setGrowls(Growls growls) {
        this.growls = growls;
    }

    /**
     * Set the refresh BlockUI used with single element blocking
     * (such as ajax based element loading/updates)
     *
     * @param refreshBlockUI
     */
    public void setRefreshBlockUI(BlockUI refreshBlockUI) {
        this.refreshBlockUI = refreshBlockUI;
    }

    /**
     * @return returns the refresh block object
     */
    @BeanTagAttribute(name = "refreshBlockUI", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public BlockUI getRefreshBlockUI() {
        return refreshBlockUI;
    }

    /**
     * Set the navigation BlockUI used with single page blocking
     * (such as full page loading/saving)
     *
     * @param navigationBlockUI
     */
    public void setNavigationBlockUI(BlockUI navigationBlockUI) {
        this.navigationBlockUI = navigationBlockUI;
    }

    /**
     * @return returns the navigation block object
     */
    @BeanTagAttribute(name = "navigationBlockUI", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public BlockUI getNavigationBlockUI() {
        return navigationBlockUI;
    }

    /**
     * whether to use growls to show messages - info, warning and error
     *
     * <p>Growls use the messages contained in the message map. If enabled, info
     * messages in their entirety will be displayed in growls, for warning and
     * error messages a growl message will notify the user that these messages
     * exist on the page.</p>
     *
     * <p> If this setting is disabled, it is recommended that
     * infoMessage display be enabled for the page ValidationMessages bean in order to
     * display relevant information to the user. Note: the growl scripts are
     * built out in the PageGroup class.</p>
     *
     * @return the growlMessagingEnabled
     */
    @BeanTagAttribute(name = "growlMessagingEnabled")
    public boolean isGrowlMessagingEnabled() {
        return this.growlMessagingEnabled;
    }

    /**
     * enable or disable showing of messages using growls
     *
     * @param growlMessagingEnabled the growlMessagingEnabled to set
     */
    public void setGrowlMessagingEnabled(boolean growlMessagingEnabled) {
        this.growlMessagingEnabled = growlMessagingEnabled;
    }

    /**
     * Indicates whether the form should be validated for dirtyness
     *
     * <p>
     * For FormView, it's necessary to validate when the user tries to navigate out of the form. If set, all the
     * InputFields will be validated on refresh, navigate, cancel or close Action or on form
     * unload and if dirty, displays a message and user can decide whether to continue with
     * the action or stay on the form. For lookup and inquiry, it's not needed to validate.
     * </p>
     *
     * @return true if dirty validation is set
     */
    @BeanTagAttribute(name = "applyDirtyCheck")
    public boolean isApplyDirtyCheck() {
        return this.applyDirtyCheck;
    }

    /**
     * Setter for dirty validation.
     */
    public void setApplyDirtyCheck(boolean applyDirtyCheck) {
        this.applyDirtyCheck = applyDirtyCheck;
    }

    /**
     * Indicates whether the Name of the Code should be displayed when a property is of type <code>KualiCode</code>
     *
     * @param translateCodesOnReadOnlyDisplay indicates whether <code>KualiCode</code>'s name should be included.
     */
    public void setTranslateCodesOnReadOnlyDisplay(boolean translateCodesOnReadOnlyDisplay) {
        this.translateCodesOnReadOnlyDisplay = translateCodesOnReadOnlyDisplay;
    }

    /**
     * Returns whether the current view supports displaying <code>KualiCode</code>'s name as additional display value
     *
     * @return true if the current view supports
     */
    @BeanTagAttribute(name = "translateCodesOnReadOnlyDisplay")
    public boolean isTranslateCodesOnReadOnlyDisplay() {
        return translateCodesOnReadOnlyDisplay;
    }

    /**
     * Indicates whether the view allows read only fields to be specified on the request URL which will
     * override the view setting
     *
     * <p>
     * If enabled, the readOnlyFields request parameter can be sent to indicate fields that should be set read only
     * </p>
     *
     * @return true if read only request overrides are allowed, false if not
     */
    @BeanTagAttribute(name = "supportsRequestOverrideOfReadOnlyFields")
    public boolean isSupportsRequestOverrideOfReadOnlyFields() {
        return supportsRequestOverrideOfReadOnlyFields;
    }

    /**
     * Setter for the the read only field override indicator
     *
     * @param supportsRequestOverrideOfReadOnlyFields
     */
    public void setSupportsRequestOverrideOfReadOnlyFields(boolean supportsRequestOverrideOfReadOnlyFields) {
        this.supportsRequestOverrideOfReadOnlyFields = supportsRequestOverrideOfReadOnlyFields;
    }

    /**
     * Indicates whether the browser autocomplete functionality should be disabled for the
     * entire form (adds autocomplete="off")
     *
     * <p>
     * The browser's native autocomplete functionality can cause issues with security fields and also fields
     * with the UIF suggest widget enabled
     * </p>
     *
     * @return true if the native autocomplete should be turned off for the form, false if not
     */
    public boolean isDisableNativeAutocomplete() {
        return disableNativeAutocomplete;
    }

    /**
     * Setter to disable browser autocomplete for the view's form
     *
     * @param disableNativeAutocomplete
     */
    public void setDisableNativeAutocomplete(boolean disableNativeAutocomplete) {
        this.disableNativeAutocomplete = disableNativeAutocomplete;
    }

    /**
     * Enables functionality to bust the browsers cache by appending an unique cache key
     *
     * <p>
     * Since response headers are unreliable for preventing caching in all browsers, the
     * framework uses a technique for updating the URL to include an unique cache key. If the
     * HTML 5 History API is supported a parameter can be added to the URL which causes the browser
     * to not find the cached page when the user goes back. If not the framework falls back to using
     * a hash key and resubmitting using script to pull the latest
     * </p>
     *
     * @return true if cache for the view should be disabled, false if not
     */
    public boolean isDisableBrowserCache() {
        return disableBrowserCache;
    }

    /**
     * Setter to disable browser caching of the view
     *
     * @param disableBrowserCache
     */
    public void setDisableBrowserCache(boolean disableBrowserCache) {
        this.disableBrowserCache = disableBrowserCache;
    }

    /**
     * Script that is executed at the beginning of page load (before any other script)
     *
     * <p>
     * Many used to set server variables client side
     * </p>
     *
     * @return pre load script
     */
    @BeanTagAttribute(name = "preLoadScript")
    public String getPreLoadScript() {
        return preLoadScript;
    }

    /**
     * Setter for the pre load script
     *
     * @param preLoadScript
     */
    public void setPreLoadScript(String preLoadScript) {
        this.preLoadScript = preLoadScript;
    }

    /**
     * The theme which contains stylesheets for this view
     *
     * @return ViewTheme
     */
    @BeanTagAttribute(name = "theme", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public ViewTheme getTheme() {
        return theme;
    }

    /**
     * Setter for The theme which contains stylesheets for this view
     *
     * @return
     */
    public void setTheme(ViewTheme theme) {
        this.theme = theme;
    }

    /**
     * The stateObject's binding path, this will be used along with the StateMapping's statePropertyName to
     * determine what field in the model state information is stored in for this view.  Used during View validation.
     *
     * @return stateObjectBindingPath path to the object storing state information
     */
    @BeanTagAttribute(name = "stateObjectBindingPath")
    public String getStateObjectBindingPath() {
        return stateObjectBindingPath;
    }

    /**
     * The stateObject's binding path, this will be used along with the StateMapping's statePropertyName to
     * determine what field in the model state information is stored in for this view.  Used during View validation.
     *
     * @param stateObjectBindingPath
     */
    public void setStateObjectBindingPath(String stateObjectBindingPath) {
        this.stateObjectBindingPath = stateObjectBindingPath;
    }

    /**
     * Gets the stateMapping.
     *
     * <p>The state mapping object is used to determine the state information for a view,
     * it must include an ordered list of states, and where to find the state information for the view.
     * A stateMapping must be set for state based validation to occur.  When stateMapping information is
     * not included, the view's model is considered stateless and all constraints will apply regardless of their
     * state information or replacements (ie, they will function as they did in version 2.1).</p>
     *
     * @return information needed for state based validation, if null no state based validation
     *         functionality will exist and configured constraints will apply regardless of state
     * @since 2.2
     */
    @BeanTagAttribute(name = "stateMapping", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public StateMapping getStateMapping() {
        return stateMapping;
    }

    /**
     * Set the stateMapping
     *
     * @param stateMapping
     */
    public void setStateMapping(StateMapping stateMapping) {
        this.stateMapping = stateMapping;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);

        View viewCopy = (View) component;

        viewCopy.setNamespaceCode(this.namespaceCode);
        viewCopy.setViewName(this.viewName);

        if (this.theme != null) {
            viewCopy.setTheme((ViewTheme) this.theme.copy());
        }

        viewCopy.setIdSequence(this.idSequence);
        viewCopy.setStateObjectBindingPath(this.stateObjectBindingPath);

        if (this.stateMapping != null) {
            viewCopy.setStateMapping(CloneUtils.deepClone(this.stateMapping));
        }

        viewCopy.setUnifiedHeader(this.unifiedHeader);

        if (this.topGroup != null) {
            viewCopy.setTopGroup((Group) this.topGroup.copy());
        }

        if (this.applicationHeader != null) {
            viewCopy.setApplicationHeader((Header) this.applicationHeader.copy());
        }

        if (this.applicationFooter != null) {
            viewCopy.setApplicationFooter((Group) this.applicationFooter.copy());
        }

        viewCopy.setStickyApplicationFooter(this.stickyApplicationFooter);
        viewCopy.setStickyApplicationHeader(this.stickyApplicationHeader);
        viewCopy.setStickyBreadcrumbs(this.stickyBreadcrumbs);
        viewCopy.setStickyFooter(this.stickyFooter);
        viewCopy.setStickyHeader(this.stickyHeader);
        viewCopy.setStickyTopGroup(this.stickyTopGroup);

        if (this.breadcrumbItem != null) {
            viewCopy.setBreadcrumbItem((BreadcrumbItem) this.breadcrumbItem.copy());
        }

        if (this.breadcrumbs != null) {
            viewCopy.setBreadcrumbs((Breadcrumbs) this.breadcrumbs.copy());
        }

        if (this.breadcrumbOptions != null) {
            viewCopy.setBreadcrumbOptions((BreadcrumbOptions) this.breadcrumbOptions.copy());
        }

        if (this.parentLocation != null) {
            viewCopy.setParentLocation((ParentLocation) this.parentLocation.copy());
        }

        if (this.pathBasedBreadcrumbs != null) {
            List<BreadcrumbItem> pathBasedBreadcrumbsCopy = Lists.newArrayListWithExpectedSize(
                    this.pathBasedBreadcrumbs.size());
            for (BreadcrumbItem pathBasedBreadcrumb : this.pathBasedBreadcrumbs) {
                pathBasedBreadcrumbs.add((BreadcrumbItem) pathBasedBreadcrumb.copy());
            }
            viewCopy.setPathBasedBreadcrumbs(pathBasedBreadcrumbsCopy);
        }

        viewCopy.setGrowlMessagingEnabled(this.growlMessagingEnabled);

        if (this.growls != null) {
            viewCopy.setGrowls((Growls) this.growls.copy());
        }

        if (this.refreshBlockUI != null) {
            viewCopy.setRefreshBlockUI((BlockUI) this.refreshBlockUI.copy());
        }

        if (this.navigationBlockUI != null) {
            viewCopy.setNavigationBlockUI((BlockUI) this.navigationBlockUI.copy());
        }

        viewCopy.setEntryPageId(this.entryPageId);
        viewCopy.setCurrentPageId(this.currentPageId);

        if (this.navigation != null) {
            viewCopy.setNavigation((Group) this.navigation.copy());
        }

        viewCopy.setFormClass(this.formClass);
        viewCopy.setDefaultBindingObjectPath(this.defaultBindingObjectPath);

        if (this.objectPathToConcreteClassMapping != null) {
            viewCopy.setObjectPathToConcreteClassMapping(new HashMap<String, Class<?>>(this.objectPathToConcreteClassMapping));
        }

        if (this.additionalCssFiles != null) {
            viewCopy.setAdditionalCssFiles(new ArrayList<String>(this.additionalCssFiles));
        }

        if (this.additionalScriptFiles != null) {
            viewCopy.setAdditionalScriptFiles(new ArrayList<String>(this.additionalScriptFiles));
        }

        viewCopy.setUseLibraryCssClasses(this.useLibraryCssClasses);
        viewCopy.setViewTypeName(this.viewTypeName);
        viewCopy.setViewStatus(this.viewStatus);

        if (this.viewIndex != null) {
            viewCopy.viewIndex = this.viewIndex.copy();
        }

        if (this.viewRequestParameters != null) {
            viewCopy.setViewRequestParameters(new HashMap<String, String>(this.viewRequestParameters));
        }

        viewCopy.setPersistFormToSession(this.persistFormToSession);

        if (this.sessionPolicy != null) {
            viewCopy.setSessionPolicy(CloneUtils.deepClone(this.sessionPolicy));
        }

        if (this.presentationController != null) {
            viewCopy.setPresentationController(this.presentationController);
        }

        if (this.authorizer != null) {
            viewCopy.setAuthorizer(this.authorizer);
        }

        if (this.actionFlags != null) {
            viewCopy.setActionFlags(new BooleanMap(this.actionFlags));
        }

        if (this.editModes != null) {
            viewCopy.setEditModes(new BooleanMap(this.editModes));
        }

        if (this.expressionVariables != null) {
            viewCopy.setExpressionVariables(new HashMap<String, String>(this.expressionVariables));
        }

        viewCopy.setSinglePageView(this.singlePageView);
        viewCopy.setMergeWithPageItems(this.mergeWithPageItems);

        if (this.page != null) {
            viewCopy.setPage((PageGroup) this.page.copy());
        }

        if (this.dialogs != null) {
            List<Group> dialogsCopy = Lists.newArrayListWithExpectedSize(this.dialogs.size());
            for (Group dialog : this.dialogs) {
                dialogsCopy.add((Group) dialog.copy());
            }
            viewCopy.setDialogs(dialogsCopy);
        }

        if (this.viewMenuLink != null) {
            viewCopy.setViewMenuLink((Link) this.viewMenuLink.copy());
        }

        viewCopy.setViewMenuGroupName(this.viewMenuGroupName);
        viewCopy.setApplyDirtyCheck(this.applyDirtyCheck);
        viewCopy.setTranslateCodesOnReadOnlyDisplay(this.translateCodesOnReadOnlyDisplay);
        viewCopy.setSupportsRequestOverrideOfReadOnlyFields(this.supportsRequestOverrideOfReadOnlyFields);
        viewCopy.setDisableBrowserCache(this.disableBrowserCache);
        viewCopy.setDisableNativeAutocomplete(this.disableNativeAutocomplete);
        viewCopy.setPreLoadScript(this.preLoadScript);

        if (this.viewTemplates != null) {
            viewCopy.setViewTemplates(new ArrayList<String>(this.viewTemplates));
        }

        if (this.viewHelperServiceClass != null) {
            viewCopy.setViewHelperServiceClass(this.viewHelperServiceClass);
        }
        else if (this.viewHelperService != null) {
            viewCopy.setViewHelperService(CloneUtils.deepClone(this.viewHelperService));
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.Component#completeValidation
     */
    @Override
    public void completeValidation(ValidationTrace tracer) {
        tracer.addBean(this);

        // Check for the presence of a valid item with an not-null EntryPageId
        boolean validPageId = false;
        if (getEntryPageId() != null) {
            for (int i = 0; i < getItems().size(); i++) {
                if (getEntryPageId().compareTo(getItems().get(i).getId()) == 0) {
                    validPageId = true;
                }
            }
        } else {
            validPageId = true;
        }
        if (!validPageId) {
            String currentValues[] = {"entryPageId = " + getEntryPageId()};
            tracer.createError("Items must contain an item with a matching id to entryPageId", currentValues);
        }

        // Check to insure the view as not already been set
        if (tracer.getValidationStage() == ValidationTrace.START_UP) {
            if (getViewStatus().compareTo(ViewStatus.CREATED) != 0) {
                String currentValues[] = {"viewStatus = " + getViewStatus()};
                tracer.createError("ViewStatus should not be set", currentValues);
            }
        }

        // Check to insure the binding object path is a valid property
        boolean validDefaultBindingObjectPath = false;
        if (getDefaultBindingObjectPath() == null) {
            validDefaultBindingObjectPath = true;
        } else if (DataDictionary.isPropertyOf(getFormClass(), getDefaultBindingObjectPath())) {
            validDefaultBindingObjectPath = true;
        }
        if (!validDefaultBindingObjectPath) {
            String currentValues[] =
                    {"formClass = " + getFormClass(), "defaultBindingPath = " + getDefaultBindingObjectPath()};
            tracer.createError("DefaultBingdingObjectPath must be a valid property of the formClass", currentValues);
        }

        // Check to insure the page is set if the view is a single page
        if (isSinglePageView()) {
            if (getPage() == null) {
                String currentValues[] = {"singlePageView = " + isSinglePageView(), "page = " + getPage()};
                tracer.createError("Page must be set if singlePageView is true", currentValues);
            }
            for (int i = 0; i < getItems().size(); i++) {
                if (getItems().get(i).getClass() == PageGroup.class) {
                    String currentValues[] =
                            {"singlePageView = " + isSinglePageView(), "items(" + i + ") = " + getItems().get(i)
                                    .getClass()};
                    tracer.createError("Items cannot be pageGroups if singlePageView is true", currentValues);
                }
            }
        }

        // Checks to insure the Growls are set if growl messaging is enabled
        if (isGrowlMessagingEnabled() == true && getGrowls() == null) {
            if (Validator.checkExpressions(this, "growls")) {
                String currentValues[] =
                        {"growlMessagingEnabled = " + isGrowlMessagingEnabled(), "growls = " + getGrowls()};
                tracer.createError("Growls cannot be null if Growl Messaging is enabled", currentValues);
            }
        }

        // Checks that there are items present if the view is not a single page
        if (!isSinglePageView()) {
            if (getItems().size() == 0) {
                String currentValues[] =
                        {"singlePageView = " + isSinglePageView(), "items.size = " + getItems().size()};
                tracer.createWarning("Items cannot be empty if singlePageView is false", currentValues);
            } else {
                for (int i = 0; i < getItems().size(); i++) {
                    if (getItems().get(i).getClass() != PageGroup.class) {
                        String currentValues[] =
                                {"singlePageView = " + isSinglePageView(), "items(" + i + ") = " + getItems().get(i)
                                        .getClass()};
                        tracer.createError("Items must be pageGroups if singlePageView is false", currentValues);
                    }
                }
            }
        }
        super.completeValidation(tracer.getCopy());
    }
}
