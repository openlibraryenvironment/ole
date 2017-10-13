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
package org.kuali.rice.kcb.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.StaticListableBeanFactory;

import java.lang.reflect.Proxy;


/**
 * Tests BeanFactoryIinvocationHandler class
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class BeanFactoryInvocationHandlerTest {
    private static interface BadInterface {
        public int add(int a, int b);

        public void notAGetter();

        public void get();

        public Object getBean();
    }

    private BadInterface bad;

    @Before
    public void createBadInstance() {
        StaticListableBeanFactory bf = new StaticListableBeanFactory();
        bf.addBean("bean", "This is a bean");
        bad = (BadInterface)
                Proxy.newProxyInstance(this.getClass().getClassLoader(),
                        new Class[]{BadInterface.class},
                        new BeanFactoryInvocationHandler(bf));
    }

    @Test(expected = RuntimeException.class)
    public void testRandomMethod() {
        int result = bad.add(2, 2);
    }

    @Test(expected = RuntimeException.class)
    public void testNotAGetter() {
        bad.notAGetter();
    }

    @Test(expected = RuntimeException.class)
    public void testAnotherBadGetter() {
        bad.get();
    }

    @Test
    public void testGoodGetter() {
        Assert.assertEquals("This is a bean", bad.getBean());
    }
}
