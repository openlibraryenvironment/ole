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
package org.apache.commons.beanutils.bugs;

import junit.framework.TestCase;
import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;


/*
 * jdk's beans class do not work with covariant returns & generics.
 * <p>
 * See https://issues.apache.org/jira/browse/BEANUTILS-340
 * See http://bugs.sun.com/view_bug.do?bug_id=6528714
 * </p>
 */
public class Jira340TestCase extends TestCase {

    public void testStub() {}

    /* these tests require java 5+ language level to compile and execute */

    public void testCovariantReturnPropertyUtils() throws Throwable {
        assertEquals(InnerBean.class, PropertyUtils.getPropertyType(new Bean(), "innerBean"));
        final PropertyDescriptor d = PropertyUtils.getPropertyDescriptor(new Bean(), "innerBean");
        assertEquals(InnerBean.class, d.getPropertyType());
        assertEquals(InnerBean.class, d.getReadMethod().getReturnType());
    }

    //this method will fail due to a java Introspector api bug
    public void failing_testCovariantReturnIntrospector() throws Throwable {
        java.beans.BeanInfo bi = java.beans.Introspector.getBeanInfo(Bean.class);
        final PropertyDescriptor[] descriptors = bi.getPropertyDescriptors();
        for (int i = 0; i < descriptors.length; i ++) {
            if ("innerBean".equals(descriptors[i].getName())) {
                assertEquals(InnerBean.class, descriptors[i].getPropertyType());
                assertEquals(InnerBean.class, descriptors[i].getReadMethod().getReturnType());
            }
        }
    }

    public void testGenericReadWritePropertyUtils() throws Throwable {
        assertEquals(String.class, PropertyUtils.getPropertyType(new ReadWriteNamedBean(), "name"));

        final PropertyDescriptor d = PropertyUtils.getPropertyDescriptor(new ReadWriteNamedBean(), "name");
        assertEquals(String.class, d.getPropertyType());
        assertEquals(String.class, d.getReadMethod().getReturnType());
        assertEquals(String.class, d.getWriteMethod().getParameterTypes()[0]);
    }

    //this method will fail due to a java Introspector api bug
    public void failing_testGenericReadWriteIntrospector() throws Throwable {
        java.beans.BeanInfo bi = java.beans.Introspector.getBeanInfo(ReadWriteNamedBean.class);
        final PropertyDescriptor[] descriptors = bi.getPropertyDescriptors();
        for (int i = 0; i < descriptors.length; i ++) {
            if ("name".equals(descriptors[i].getName())) {
                assertEquals(String.class, descriptors[i].getPropertyType());
                assertEquals(String.class, descriptors[i].getReadMethod().getReturnType());
                assertEquals(String.class, descriptors[i].getWriteMethod().getParameterTypes()[0]);
            }
        }
    }

    public void testGenericWritePropertyUtils() throws Throwable {
        assertEquals(String.class, PropertyUtils.getPropertyType(new WriteNamedBean(), "name"));

        final PropertyDescriptor d = PropertyUtils.getPropertyDescriptor(new WriteNamedBean(), "name");
        assertEquals(String.class, d.getPropertyType());
        assertEquals(String.class, d.getWriteMethod().getParameterTypes()[0]);
    }

    //this method will fail due to a java Introspector api bug
    public void failing_testGenericWriteIntrospector() throws Throwable {
        java.beans.BeanInfo bi = java.beans.Introspector.getBeanInfo(WriteNamedBean.class);
        final PropertyDescriptor[] descriptors = bi.getPropertyDescriptors();
        for (int i = 0; i < descriptors.length; i ++) {
            if ("name".equals(descriptors[i].getName())) {
                assertEquals(String.class, descriptors[i].getPropertyType());
                assertEquals(String.class, descriptors[i].getWriteMethod().getParameterTypes()[0]);
            }
        }
    }

    public void testGenericReadPropertyUtils() throws Throwable {
        assertEquals(String.class, PropertyUtils.getPropertyType(new ReadNamedBean(), "name"));

        final PropertyDescriptor d = PropertyUtils.getPropertyDescriptor(new ReadNamedBean(), "name");
        assertEquals(String.class, d.getPropertyType());
        assertEquals(String.class, d.getReadMethod().getReturnType());
    }

    //this method will fail due to a java Introspector api bug
    public void failing_testGenericReadIntrospector() throws Throwable {
        java.beans.BeanInfo bi = java.beans.Introspector.getBeanInfo(WriteNamedBean.class);
        final PropertyDescriptor[] descriptors = bi.getPropertyDescriptors();
        for (int i = 0; i < descriptors.length; i ++) {
            if ("name".equals(descriptors[i].getName())) {
                assertEquals(String.class, descriptors[i].getPropertyType());
                assertEquals(String.class, descriptors[i].getReadMethod().getReturnType());
            }
        }
    }

    public void testGenericWriteOverloadInheritPropertyUtils() throws Throwable {
        //can't really assert a specific type b/c it's non-deterministic - just want to make sure things don't blow up
        assertNotNull(PropertyUtils.getPropertyType(new WriteOverloadNamedBeanInherit(), "name"));

        final PropertyDescriptor d = PropertyUtils.getPropertyDescriptor(new WriteOverloadNamedBeanInherit(), "name");
        assertNotNull(d.getPropertyType());
        assertNotNull(d.getWriteMethod().getParameterTypes()[0]);
    }

    //this method will fail due to a java Introspector api bug
    public void testGenericWriteOverloadInheritIntrospector() throws Throwable {
        //can't really assert a specific type b/c it's non-deterministic - just want to make sure things don't blow up
        java.beans.BeanInfo bi = java.beans.Introspector.getBeanInfo(WriteOverloadNamedBeanInherit.class);
        final PropertyDescriptor[] descriptors = bi.getPropertyDescriptors();
        for (int i = 0; i < descriptors.length; i ++) {
            if ("name".equals(descriptors[i].getName())) {
                assertNotNull(descriptors[i].getPropertyType());
                assertNotNull(descriptors[i].getWriteMethod().getParameterTypes()[0]);
            }
        }
    }

    public void testGenericWriteOverloadNonInheritPropertyUtils() throws Throwable {
        //can't really assert a specific type b/c it's non-deterministic - just want to make sure things don't blow up
        assertNotNull(PropertyUtils.getPropertyType(new WriteOverloadNamedBeanNonInherit(), "name"));

        final PropertyDescriptor d = PropertyUtils.getPropertyDescriptor(new WriteOverloadNamedBeanNonInherit(), "name");
        assertNotNull(d.getPropertyType());
        assertNotNull(d.getWriteMethod().getParameterTypes()[0]);
    }

    //this method will fail due to a java Introspector api bug
    public void testGenericWriteOverloadNonInheritIntrospector() throws Throwable {
        //can't really assert a specific type b/c it's non-deterministic - just want to make sure things don't blow up
        java.beans.BeanInfo bi = java.beans.Introspector.getBeanInfo(WriteOverloadNamedBeanNonInherit.class);
        final PropertyDescriptor[] descriptors = bi.getPropertyDescriptors();
        for (int i = 0; i < descriptors.length; i ++) {
            if ("name".equals(descriptors[i].getName())) {
                assertNotNull(descriptors[i].getPropertyType());
                assertNotNull(descriptors[i].getWriteMethod().getParameterTypes()[0]);
            }
        }
    }

    public void testVisibilityBridge() throws Throwable {
        assertEquals(String.class, PropertyUtils.getPropertyType(new PublicClass(), "foo"));
    }

    //example 1 start
    static class Bean implements Beanable {
        private InnerBean innerBean = new InnerBean();

        public InnerBean getInnerBean() {
            return innerBean;
        }
    }

    static interface Beanable {
        InnerBeanable getInnerBean();
    }

    static class InnerBean implements InnerBeanable {
    }

    static interface InnerBeanable {
    }
    //example 1 end

    //example 2/3/4 start
    static interface ReadNamed<T> {
        T getName();
    }

    static interface WriteNamed<T> {
        void setName(T t);
    }

    static class ReadWriteNamedBean implements ReadNamed<String>, WriteNamed<String> {
        private String myName;

        public String getName() {
            return myName;
        }

        public void setName(String name) {
            myName = name;
        }
    }

    static class WriteNamedBean implements WriteNamed<String> {
        private String myName;
        public void setName(String name) {
            myName = name;
        }
    }

    static class ReadNamedBean implements ReadNamed<String> {
        private final String myName = "foo";
        public String getName() {
            return myName;
        }
    }
    //example 2/3/4 end


    //example 5 start
    //not using covariant return but still generating a bridge method on later java versions
    static class PackagePrivateClass {
        public String getFoo() { return null; }
    }
    public static class PublicClass extends PackagePrivateClass { }
    //example 5 end

    //example 6 start
    static class WriteOverloadNamedBeanInherit implements WriteNamed<String> {
        private String myName;

        public void setName(String s) {
            myName = s;
        }

        public void setName(CharSequence s) {
            myName = s.toString();
        }
    }
    //example 6 end

    //example 7 start
    static class WriteOverloadNamedBeanNonInherit implements WriteNamed<String> {
        private String myName;

        public void setName(String s) {
            myName = s;
        }

        public void setName(Number s) {
            myName = s.toString();
        }
    }
    //example 7 end
}
