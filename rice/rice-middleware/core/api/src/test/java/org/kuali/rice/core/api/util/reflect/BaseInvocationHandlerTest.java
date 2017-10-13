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
package org.kuali.rice.core.api.util.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Tests the abstract BaseInvocationHandler class.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BaseInvocationHandlerTest {


    @Test
    public void testInvokeInternal() {
        MyClass myClass = new MyClass();
        Assert.assertEquals("myMethod", myClass.myMethod());
        MyInterface proxy = (MyInterface)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { MyInterface.class }, new TestInvocationHandler());
        Assert.assertEquals("This is only a test", proxy.myMethod());
    }

    @Test
    public void testEquals() {

        // create two different proxies to the same invocation handler
        TestInvocationHandler invocationHandler = new TestInvocationHandler();
        MyInterface proxy1 = (MyInterface)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { MyInterface.class }, invocationHandler);
        MyInterface proxy2 = (MyInterface)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { MyInterface.class }, invocationHandler);

        Assert.assertNotSame(proxy1, proxy2);
        Assert.assertFalse(proxy1.equals(proxy2));
        Assert.assertTrue(proxy1.equals(proxy1));
    }

    @Test
    public void testHashCode() {

        // create two different proxies to the same invocation handler
        TestInvocationHandler invocationHandler = new TestInvocationHandler();
        MyInterface proxy1 = (MyInterface)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { MyInterface.class }, invocationHandler);
        MyInterface proxy2 = (MyInterface)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { MyInterface.class }, invocationHandler);

        Assert.assertFalse(proxy1.hashCode() == proxy2.hashCode());
        Assert.assertEquals(proxy1.hashCode(), proxy1.hashCode());
    }


    @Test
    public void testToString() {
        MyInterface proxy = (MyInterface)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { MyInterface.class }, new TestInvocationHandler());
        String toStringValue = proxy.toString();
        // the default toString() prints out the toString of the invocation handler
        Assert.assertEquals("My RAD toString()!", toStringValue);
    }

    public static class TestInvocationHandler extends BaseInvocationHandler {
        @Override
        protected Object invokeInternal(Object proxy, Method method, Object[] arguments) throws Throwable {
            return "This is only a test";
        }

        public String toString() {
            return "My RAD toString()!";
        }
    }

    public static interface MyInterface {
        String myMethod();
    }

    public static class MyClass implements MyInterface {
        public String myMethod() {
            return "myMethod";
        }
    }

}
