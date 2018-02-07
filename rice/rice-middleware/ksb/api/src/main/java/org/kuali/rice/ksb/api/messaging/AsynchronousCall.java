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
package org.kuali.rice.ksb.api.messaging;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.ksb.api.bus.ServiceConfiguration;

import java.io.Serializable;

/**
 * Encapsulates an asynchronous call to a service.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AsynchronousCall implements Serializable {

    private static final long serialVersionUID = -1036656564567726747L;

    private Object[] arguments;

    private Class<?>[] paramTypes;

    private ServiceConfiguration serviceConfiguration;

    private Serializable context;

    private String methodName;

    private AsynchronousCallback callback;

    private boolean ignoreStoreAndForward;

    public AsynchronousCall(Class<?>[] paramTypes, Object[] arguments, ServiceConfiguration serviceConfiguration, String methodName, AsynchronousCallback callback, Serializable context) {
        this.arguments = arguments;
        this.paramTypes = paramTypes;
        this.serviceConfiguration = serviceConfiguration;
        this.methodName = methodName;
        this.callback = callback;
        this.context = context;
    }

    public Object[] getArguments() {
        return this.arguments;
    }

    public Class<?>[] getParamTypes() {
        return this.paramTypes;
    }

    public ServiceConfiguration getServiceConfiguration() {
        return this.serviceConfiguration;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public AsynchronousCallback getCallback() {
        return this.callback;
    }

    public String toString() {
        return "[AsynchronousCall: " + "serviceInfo=" + this.serviceConfiguration + ", methodName=" + this.methodName + ", paramTypes=" + getStringifiedArray(this.paramTypes) + ", arguments=" + getStringifiedArray(this.arguments) + "]";
    }

    /**
     * Takes an Object[] and returns a human-readable String of the contents
     * Candidate for relocation to a utility class
     *
     * @param array the Object[]
     * @return a human-readable String of the contents
     */
    private static final String getStringifiedArray(Object[] array) {
        if (array == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer(array.getClass().toString());
        sb.append("[");
        StringUtils.join(array, ", ");
        sb.append("]");
        return sb.toString();
    }

    public boolean isIgnoreStoreAndForward() {
        return this.ignoreStoreAndForward;
    }

    public void setIgnoreStoreAndForward(boolean ignoreStoreAndForward) {
        this.ignoreStoreAndForward = ignoreStoreAndForward;
    }

    public Serializable getContext() {
        return this.context;
    }

    public void setContext(Serializable context) {
        this.context = context;
    }


}
