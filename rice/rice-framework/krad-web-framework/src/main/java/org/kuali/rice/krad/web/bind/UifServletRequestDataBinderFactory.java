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
package org.kuali.rice.krad.web.bind;

import org.kuali.rice.krad.web.form.UifFormBase;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.InvocableHandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.List;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class UifServletRequestDataBinderFactory extends ServletRequestDataBinderFactory {

    /**
     * Create a new instance.
     *
     * @param binderMethods one or more {@code @InitBinder} methods
     * @param initializer provides global data binder initialization
     */
    public UifServletRequestDataBinderFactory(List<InvocableHandlerMethod> binderMethods,
            WebBindingInitializer initializer) {
        super(binderMethods, initializer);
    }

    @Override
    protected ServletRequestDataBinder createBinderInstance(Object target, String objectName,
            NativeWebRequest request) {
        if (target != null) {
            // only override for UifFormBase models so that non KRAD spring MVC
            // can be used in same dispatcher servlet.
            if (target instanceof UifFormBase) {
                return new UifServletRequestDataBinder(target, objectName);
            }
        }

        return super.createBinderInstance(target, objectName, request);
    }
}
