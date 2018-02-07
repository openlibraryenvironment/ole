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

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.core.api.uif.RemotableAttributeField;
import org.kuali.rice.krad.datadictionary.parse.BeanTag;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.uif.component.BindingInfo;
import org.kuali.rice.krad.uif.component.ComponentBase;
import org.kuali.rice.krad.uif.component.DataBinding;
import org.kuali.rice.krad.uif.component.MethodInvokerConfig;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.util.CloneUtils;
import org.kuali.rice.krad.uif.util.ComponentFactory;
import org.kuali.rice.krad.uif.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder in the configuration for a <code>Container</code> list of items that will be invoked to
 * retrieve a list of {@link RemotableAttributeField} instances which will then be inserted into the containers
 * list at the position of the holder
 *
 * <p>
 * Since remotable fields are dynamic by nature, the individual fields cannot be configured initially with the
 * container. Further more the properties for the field are constructed with code. This gives the ability to specify
 * where that list of fields should be placed, along with configured on how to retrieve the remote fields.
 * </p>
 *
 * <p>
 * The fetching properties are used to configure what method to invoke that will return the list of remotable fields.
 * Specifying the {@link #getFetchingMethodToCall()} only assumes the method is on the view helper service for the
 * contained view. For invoking other classes, such as services or static classes, use {@link
 * #getFetchingMethodInvoker()}
 * </p>
 *
 * <p>
 * The list of remotable fields should bind to a Map property on the model. The {@link #getPropertyName()} and
 * {@link #getBindingInfo()} properties specify the path to this property. The property names configured on the
 * returned fields are assumed to be keys in that above configured map, with the corresponding map value giving the
 * actual model value for the remote field.
 * </p>
 *
 * <p>
 * e.g. configuration
 * {@code
 * <property name="items">
 * <list>
 * <bean parent="RemoteFieldsHolder" p:propertyName="remoteFieldValuesMap"
 * p:fetchingMethodToCall="retrieveRemoteFields"/>
 * ...
 * }
 *
 * This example will invoke a method named 'retrieveRemoteFields' on the view helper service, which should return
 * a list of {@link RemotableAttributeField} instances. The view, model instance, and parent container will be sent
 * to the method as arguments.
 *
 * The returned fields will be translated to {@link InputField} instances that bind to a map property named
 * 'remoteFieldValuesMap' on the model.
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
@BeanTag(name = "remotableFieldsPlaceholderConfig-bean", parent = "Uif-RemotableFieldsPlaceholderConfig")
public class RemoteFieldsHolder extends ComponentBase implements DataBinding {
    private static final long serialVersionUID = -8493923312021633727L;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(RemoteFieldsHolder.class);

    private String propertyName;
    private BindingInfo bindingInfo;

    private String fetchingMethodToCall;
    private MethodInvokerConfig fetchingMethodInvoker;

    public RemoteFieldsHolder() {
        super();
    }

    /**
     * Invokes the configured fetching method to retrieve a list of remotable fields, then invoked the
     * {@code ComponentFactory} to translate the fields, and finally sets up the binding for the attribute fields
     *
     * @param view view instance the container belongs to, sent to the fetching method
     * @param model object containing the view data, sent to the fetching method
     * @param parent container instance that holder is configured for, sent to the fetching method
     * @return list of attribute fields that should be placed into container, if no remotable
     *         fields were returned from the fetching method the list will be empty
     */
    public List<InputField> fetchAndTranslateRemoteFields(View view, Object model, Container parent) {
        if (StringUtils.isBlank(fetchingMethodToCall) && (fetchingMethodInvoker == null)) {
            throw new RuntimeException("");
        }

        if (fetchingMethodInvoker == null) {
            fetchingMethodInvoker = new MethodInvokerConfig();
        }

        // if method not set on invoker, use fetchingMethodToCall, note staticMethod could be set(don't know since
        // there is not a getter), if so it will override the target method in prepare
        if (StringUtils.isBlank(fetchingMethodInvoker.getTargetMethod())) {
            fetchingMethodInvoker.setTargetMethod(fetchingMethodToCall);
        }

        // if target class or object not set, use view helper service
        if ((fetchingMethodInvoker.getTargetClass() == null) && (fetchingMethodInvoker.getTargetObject() == null)) {
            fetchingMethodInvoker.setTargetObject(view.getViewHelperService());
        }

        Object[] arguments = new Object[3];
        arguments[0] = view;
        arguments[1] = model;
        arguments[2] = parent;
        fetchingMethodInvoker.setArguments(arguments);

        // invoke method
        List<RemotableAttributeField> remotableFields = null;
        try {
            LOG.debug("Invoking fetching method: " + fetchingMethodInvoker.getTargetMethod());
            fetchingMethodInvoker.prepare();

            remotableFields = (List<RemotableAttributeField>) fetchingMethodInvoker.invoke();

        } catch (Exception e) {
            LOG.error("Error invoking fetching method", e);
            throw new RuntimeException("Error invoking fetching method", e);
        }

        // do translation
        List<InputField> attributeFields = new ArrayList<InputField>();
        if ((remotableFields != null) && !remotableFields.isEmpty()) {
            attributeFields = ComponentFactory.translateRemotableFields(remotableFields);
        }

        // set binding info on the translated fields
        if (bindingInfo == null) {
            bindingInfo = new BindingInfo();
        }

        // property name should point to a Map that holds attribute name/value pairs
        bindingInfo.addToBindByNamePrefix(propertyName);
        bindingInfo.setBindToMap(true);

        for (InputField field : attributeFields) {
            BindingInfo fieldBindingInfo = CloneUtils.deepClone(bindingInfo);
            fieldBindingInfo.setDefaults(view, field.getPropertyName());
            field.setBindingInfo(fieldBindingInfo);

            view.assignComponentIds(field);
        }

        return attributeFields;
    }

    @Override
    public String getComponentTypeName() {
        return "RemoteFieldsHolder";
    }

    /**
     * Path to the Map property that the translated fields bind to
     *
     * <p>
     * It is assumed this property points to a Map where the property names on the returned remotable fields
     * are keys in that map, with the corresponding map value giving the model value for the field
     * </p>
     *
     * @return path to property on model
     */
    @BeanTagAttribute(name = "propertyName")
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * Setter for the property name that points to the binding Map
     *
     * @param propertyName
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * Can be used to for more complex binding paths
     *
     * <p>
     * Generally not necessary to set on a field level, any default object path or binding prefixes set
     * on the view or container will be inherited
     * </p>
     *
     * @return BindingInfo instance containing binding information for the Map property
     */
    @BeanTagAttribute(name = "bindingInfo", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public BindingInfo getBindingInfo() {
        return bindingInfo;
    }

    /**
     * Setter for the Map property binding info instance
     *
     * @param bindingInfo
     */
    public void setBindingInfo(BindingInfo bindingInfo) {
        this.bindingInfo = bindingInfo;
    }

    /**
     * Name of the method to invoke for retrieving the list of remotable fields
     *
     * <p>
     * When only the fetching method to call is configured it is assumed to be a valid method on the view
     * helper service for the containing view. The method name must accept the view, model object, and parent
     * container as arguments, and return a list of {@link RemotableAttributeField} instances.
     * </p>
     *
     * <p>
     * For invoking the method on classes other than the view helper service, see {@link #getFetchingMethodInvoker()}
     * </p>
     *
     * @return name of method to invoke for fetching remote fields
     */
    @BeanTagAttribute(name = "fetchingMethodToCall")
    public String getFetchingMethodToCall() {
        return fetchingMethodToCall;
    }

    /**
     * Setter for the fetching method to call
     *
     * @param fetchingMethodToCall
     */
    public void setFetchingMethodToCall(String fetchingMethodToCall) {
        this.fetchingMethodToCall = fetchingMethodToCall;
    }

    /**
     * Configuration for the method to invoke for retrieving the list of remotable fields
     *
     * <p>
     * Through the method invoker config, a service or static class can be configured along with the
     * method name that will be invoked. The method name must accept the view, model object, and parent
     * container as arguments, and return a list of {@link RemotableAttributeField} instances.
     * </p>
     *
     * <p>
     * Note the {@link org.kuali.rice.krad.uif.component.MethodInvokerConfig#getTargetMethod()} property can
     * be configured, or the {@link #getFetchingMethodToCall()}. In the case of both configurations, the target
     * method on the method invoker config will be used
     * </p>
     *
     * @return MethodInvokerConfig instance containing method configuration
     */
    @BeanTagAttribute(name = "fetchingMethodInvoker", type = BeanTagAttribute.AttributeType.SINGLEBEAN)
    public MethodInvokerConfig getFetchingMethodInvoker() {
        return fetchingMethodInvoker;
    }

    /**
     * Setter for the fetching method to invoke configuration
     *
     * @param fetchingMethodInvoker
     */
    public void setFetchingMethodInvoker(MethodInvokerConfig fetchingMethodInvoker) {
        this.fetchingMethodInvoker = fetchingMethodInvoker;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);
        RemoteFieldsHolder remoteFieldsHolderCopy = (RemoteFieldsHolder) component;
        remoteFieldsHolderCopy.setPropertyName(this.propertyName);

        if(this.bindingInfo != null) {
            remoteFieldsHolderCopy.setBindingInfo((BindingInfo)this.bindingInfo.copy());
        }

        remoteFieldsHolderCopy.setFetchingMethodToCall(this.fetchingMethodToCall);

        if(this.fetchingMethodInvoker != null) {
            this.setFetchingMethodInvoker(CloneUtils.deepClone(this.fetchingMethodInvoker));
        }
    }

}
