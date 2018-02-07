/**
 * Copyright 2005-2013 The Kuali Foundation
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
package org.kuali.rice.core.test;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * Tests that ordering of annotation definitions is preserved 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class AnnotationOrderTest {
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {
        String value();
    }

    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation1 {
        String value();
    }
    
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation2 {
        String value();
    }
    
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotationHolder {
        TestAnnotation[] value();
    }


    @Test
    @TestAnnotation1("11")
    @TestAnnotationHolder({
            @TestAnnotation("1"),
            @TestAnnotation("2"),
            @TestAnnotation("3"),
            @TestAnnotation("4"),
            @TestAnnotation("5"),
            @TestAnnotation("6")
    })
    @TestAnnotation2("22")
    public void testAnnotationOrder() throws Exception {
        Method me = getClass().getMethod("testAnnotationOrder");
        Annotation[] annotations = me.getAnnotations();
        Assert.assertEquals(4, annotations.length);
        Assert.assertEquals(Test.class, annotations[0].annotationType());
        Assert.assertEquals(TestAnnotation1.class, annotations[1].annotationType());
        Assert.assertEquals(TestAnnotationHolder.class, annotations[2].annotationType());
        Assert.assertEquals(TestAnnotation2.class, annotations[3].annotationType());
        
        TestAnnotationHolder holder = me.getAnnotation(TestAnnotationHolder.class);
        TestAnnotation[] children = holder.value();
        Assert.assertEquals(6, children.length);
        Assert.assertEquals("1", children[0].value());
        Assert.assertEquals("2", children[1].value());
        Assert.assertEquals("3", children[2].value());
        Assert.assertEquals("4", children[3].value());
        Assert.assertEquals("5", children[4].value());
        Assert.assertEquals("6", children[5].value());
    }
}
