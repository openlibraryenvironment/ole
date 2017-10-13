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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.coreservice.framework.CoreFrameworkServiceLocator;
import org.kuali.rice.coreservice.framework.parameter.ParameterService;
import org.kuali.rice.krad.datadictionary.HelpDefinition;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.element.Action;
import org.kuali.rice.krad.uif.view.View;
import org.kuali.rice.krad.uif.util.ComponentFactory;

import java.text.MessageFormat;
import java.util.List;

/**
 * Widget that renders help on a component
 *
 * <p>
 * If help URL is specified then display help icon and/or if help summary is specified then display help tooltip.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "help-bean", parent = "Uif-Help")
public class Help extends WidgetBase {
	private static final long serialVersionUID = -1514436681476297241L;

    private Action helpAction;
    private HelpDefinition helpDefinition;
    private String externalHelpUrl;

    private String tooltipHelpContent;

    /**
     * The following initialization is performed:
     *
     * <ul>
     * <li>If help action not initialized and external help is configured, get the default
     * help action component</li>
     * </ul>
     *
     * @see org.kuali.rice.krad.uif.component.ComponentBase#performInitialization(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object)
     */
    @Override
    public void performInitialization(View view, Object model) {
        super.performInitialization(view, model);

        if (helpAction == null) {
            // TODO: check for expressions on helpDefinition?
            if ((StringUtils.isNotBlank(externalHelpUrl) || (getPropertyExpression("externalHelpUrl") != null))
                    || ((helpDefinition != null) && StringUtils.isNotBlank(helpDefinition.getParameterName()))
                    && StringUtils.isNotBlank(helpDefinition.getParameterDetailType())) {
                helpAction = ComponentFactory.getHelpAction();

                view.assignComponentIds(helpAction);
                helpAction.addDataAttribute(UifConstants.DataAttributes.ROLE, "help");
            }
        }
        else{
            helpAction.addDataAttribute(UifConstants.DataAttributes.ROLE, "help");
        }
    }

    /**
     * Finalize the help widget for usage
     *
     * <p>
     * In addition to the standard finalization the following tasks are performed:
     * <li>Build the external help Url</li>
     * <li>Set the javascript action which opens the external help in window</li>
     * <li>Set render to false if help not configured</li>
     * </p>
     *
     * @see org.kuali.rice.krad.uif.widget.WidgetBase#performFinalize(org.kuali.rice.krad.uif.view.View,
     *      java.lang.Object, org.kuali.rice.krad.uif.component.Component)
     */
    @Override
    public void performFinalize(View view, Object model, Component parent) {
        super.performFinalize(view, model, parent);

        buildExternalHelp(view, parent);
        buildTooltipHelp(parent);

        // if help is not configured don't render the component
        if (StringUtils.isBlank(this.externalHelpUrl) && StringUtils.isBlank(this.tooltipHelpContent)) {
            setRender(false);
        }
    }

    /**
     * Build the external help
     *
     * <p>
     * When the externalHelpUrl is blank and the helpDefinition is specified then the external help URL is
     * looked up via the helpDefinition from the system parameters.  The namespace in the helpDefinition
     * does not need to be specified and will default to the namespace of the view.
     * </p>
     *
     * <p>
     * Set the javascript action to open the external help in a window.
     * </p>
     *
     * <p>
     * Set the html title attribute of the help icon.
     * </p>
     *
     * @param view used to get the default namespace
     * @param parent used to get the help title text used in the html title attribute of the help icon
     */
    protected void buildExternalHelp(View view, Component parent) {
        if (StringUtils.isBlank(externalHelpUrl) && (helpDefinition != null)) {
            if (StringUtils.isBlank(helpDefinition.getParameterNamespace())) {
                helpDefinition.setParameterNamespace(view.getNamespaceCode());
            }

            if (StringUtils.isNotBlank(helpDefinition.getParameterNamespace())
                    && StringUtils.isNotBlank(helpDefinition.getParameterDetailType())
                    && StringUtils.isNotBlank(helpDefinition.getParameterName())) {
                externalHelpUrl = getParameterService().getParameterValueAsString(helpDefinition.getParameterNamespace(),
                        helpDefinition.getParameterDetailType(), helpDefinition.getParameterName());
            }
        }

        if (StringUtils.isNotBlank(externalHelpUrl)) {
            // set the javascript action for the external help
            getHelpAction().setActionScript("openHelpWindow('" + externalHelpUrl + "')");

            // set the alt and title attribute of the image
            String helpTitle;

            // make sure that we are the component's native help and not a misconfigured standalone help bean.
            if ((parent instanceof Helpable) && (((Helpable) parent).getHelp() == this)) {
                helpTitle = MessageFormat.format(
                        CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                                "help.icon.title.tag.with.field.label"), ((Helpable) parent).getHelpTitle());
            } else {
                helpTitle = CoreApiServiceLocator.getKualiConfigurationService().getPropertyValueAsString(
                        "help.icon.title.tag");
            }

            getHelpAction().getActionImage().setAltText(helpTitle);
            getHelpAction().getActionImage().setTitle(helpTitle);
        }
    }

    /**
     * Build the tooltip help
     *
     * <p>
     * The help tooltip is set on the component.  To use the help tooltip bean definition, the help's tooltip is used
     * as and intermediary for setting up the tooltip widget and then copied to the component.
     * </p>
     *
     * @param parent used for checking misconfigurations
     */
    protected void buildTooltipHelp(Component parent) {
        if (StringUtils.isNotBlank(tooltipHelpContent) && this.isRender()) {
            // make sure that we are the component's native help and not a misconfigured standalone help bean.
            if ((parent instanceof Helpable) && (((Helpable) parent).getHelp() == this)) {
                this.getToolTip().setTooltipContent(tooltipHelpContent);
                ((Helpable) parent).setTooltipOfComponent(this.getToolTip());
            }
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#getComponentsForLifecycle()
     */
    @Override
    public List<Component> getComponentsForLifecycle() {
        List<Component> components = super.getComponentsForLifecycle();

        components.add(helpAction);

        return components;
    }

    /**
     * HelpActionField is used for rendering external help
     *
     * @return Action for external help
     */
    @BeanTagAttribute(name="helpAction",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public Action getHelpAction() {
        return helpAction;
    }

    /**
     * Setter for helpAction
     *
     * @param helpAction
     */
    public void setHelpAction(Action helpAction) {
        this.helpAction = helpAction;
    }

    /**
     * The help definition is used as the key to retrieve the external help Url from the parameter table of
     * the database
     *
     * @return HelpDefinition
     */
    @BeanTagAttribute(name="helpDefinition",type= BeanTagAttribute.AttributeType.SINGLEBEAN)
    public HelpDefinition getHelpDefinition() {
        return helpDefinition;
    }

    /**
     * Setter for the help definition of the database.
     *
     * @param helpDefinition
     */
    public void setHelpDefinition(HelpDefinition helpDefinition) {
        this.helpDefinition = helpDefinition;
    }

    /**
     * The external help Url
     *
     * <p>
     * This should contain a valid URL.  When specified this URL takes precedence over the external help URL from
     * the system parameters.
     * </p>
     *
     * @return Url of the external help
     */
    @BeanTagAttribute(name="externalHelpUrl")
    public String getExternalHelpUrl() {
        return this.externalHelpUrl;
    }

    /**
     * Setter for externalHelpUrl
     *
     * @param externalHelpUrl
     */
    public void setExternalHelpUrl(String externalHelpUrl) {
        this.externalHelpUrl = externalHelpUrl;
    }

    /**
     * TooltipHelpContent
     *
     * @return TooltipHelpContent
     */
    @BeanTagAttribute(name="tooltipHelpContent")
    public String getTooltipHelpContent() {
        return this.tooltipHelpContent;
    }

    /**
     * Setter for tooltipHelpContent
     *
     * @param tooltipHelpContent
     */
    public void setTooltipHelpContent(String tooltipHelpContent) {
        this.tooltipHelpContent = tooltipHelpContent;
    }

    /**
     * Retrieve the parameter service
     *
     * @return ParameterService
     */
    protected ParameterService getParameterService() {
        return CoreFrameworkServiceLocator.getParameterService();
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        Help helpCopy = (Help) component;
        helpCopy.setExternalHelpUrl(this.getExternalHelpUrl());

        if (this.helpAction != null) {
            helpCopy.setHelpAction((Action)this.helpAction.copy());
        }

        helpCopy.setHelpDefinition(this.getHelpDefinition());
        helpCopy.setTooltipHelpContent(this.tooltipHelpContent);
    }

}
