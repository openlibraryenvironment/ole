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
package org.kuali.rice.krad.util;

import org.kuali.rice.krad.util.GlobalVariables;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * A proxy invocationhandler that implements around advice which pushes a new GlobalVariables frame before
 * invocation and pops it after invocation.
 * @see GlobalVariables#doInNewGlobalVariables
 */
public class GlobalVariablesContextInvocationHandler implements InvocationHandler {
    private Object proxied;
    public GlobalVariablesContextInvocationHandler(Object proxied) {
        this.proxied = proxied;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        return GlobalVariables.doInNewGlobalVariables(new Callable() {
            @Override
            public Object call() throws Exception {
                return method.invoke(proxied, args);
            }
        });
    }
}