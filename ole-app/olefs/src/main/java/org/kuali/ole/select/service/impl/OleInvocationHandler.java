/*
 * Copyright 2012 The Kuali Foundation.
 * 
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.opensource.org/licenses/ecl1.php
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.ole.select.service.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class OleInvocationHandler implements InvocationHandler {
    private final Object ojb;


    public OleInvocationHandler(Object ojb) {
        super();
        this.ojb = ojb;

    }

    public Object invoke(Object proxy, Method meth, Object[] args) throws Throwable {
        try {
            Object result = meth.invoke(ojb, args);
            return result;
        } catch (final InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }


}
