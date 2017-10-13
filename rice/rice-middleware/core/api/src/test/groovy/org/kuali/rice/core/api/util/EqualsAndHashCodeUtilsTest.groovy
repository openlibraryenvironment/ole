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
package org.kuali.rice.core.api.util;


import org.apache.commons.lang.builder.HashCodeBuilder
import org.junit.Assert
import org.junit.Test

public class EqualsAndHashCodeUtilsTest {
    private final shouldFail = new GroovyTestCase().&shouldFail

    public final static class Foo {
        String canCompare1;
        String canCompare2;
        List<String> cannotCompare;
    }

    public final static class Bar {
        Calendar cal
        Calendar cal2
        String name

        int hashCode() {
            return 31 * HashCodeBuilder.reflectionHashCode(this,["cal","cal2"]) +
                    EqualsAndHashCodeUtils.hashCodeForCalendars(cal)
        }

    }

    static createCalendar() {
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
        c.set(Calendar.HOUR_OF_DAY, 5);
        c.set(Calendar.MINUTE, 30);
        c.set(Calendar.SECOND, 15);
        c.set(Calendar.MILLISECOND, 500);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DATE, 15);
        c.set(Calendar.YEAR, 2011);
        return c;
    }

    @Test
    public void test_equalsUsingCompareToOnFields_shouldBeEqual() {
        Foo f1 = new Foo(canCompare1: "a", canCompare2: "b", cannotCompare: Collections.emptyList());
        Foo f2 = new Foo(canCompare1: "a", canCompare2: "b", cannotCompare: Collections.emptyList());
        Assert.assertTrue(
                EqualsAndHashCodeUtils.equalsUsingCompareToOnFields(f1, f2, "canCompare1", "canCompare2")
        );
    }

    @Test
    public void test_equalsUsingCompareToOnFields_bothReferencesNull() {
        Assert.assertTrue(
                EqualsAndHashCodeUtils.equalsUsingCompareToOnFields(null, null, "canCompare1", "canCompare2")
        );
    }

    @Test
    public void test_equalsUsingCompareToOnFields_oneReferenceNull() {
        Foo f1 = new Foo(canCompare1: "a", canCompare2: "b", cannotCompare: Collections.emptyList());
        Assert.assertFalse(
                EqualsAndHashCodeUtils.equalsUsingCompareToOnFields(f1, null, "canCompare1", "canCompare2")
        );
    }

    @Test
    public void test_equalsUsingCompareToOnFields_bothFieldsNull() {
        Foo f1 = new Foo(canCompare1: "a", canCompare2: null, cannotCompare: Collections.emptyList());
        Foo f2 = new Foo(canCompare1: "a", canCompare2: null, cannotCompare: Collections.emptyList());
        Assert.assertTrue(
                EqualsAndHashCodeUtils.equalsUsingCompareToOnFields(f1, f2, "canCompare1", "canCompare2")
        );
    }

    @Test
    public void test_equalsUsingCompareToOnFields_oneFieldNull() {
        Foo f1 = new Foo(canCompare1: "a", canCompare2: null, cannotCompare: Collections.emptyList());
        Foo f2 = new Foo(canCompare1: "a", canCompare2: "b", cannotCompare: Collections.emptyList());
        Assert.assertFalse(
                EqualsAndHashCodeUtils.equalsUsingCompareToOnFields(f1, f2, "canCompare1", "canCompare2")
        );
    }

    @Test
    public void test_equalsUsingCompareToOnFields_shouldBeUnEqual() {
        Foo f1 = new Foo(canCompare1: "a", canCompare2: "b", cannotCompare: Collections.emptyList());
        Foo f2 = new Foo(canCompare1: "a", canCompare2: "c", cannotCompare: Collections.emptyList());
        Assert.assertFalse(
                EqualsAndHashCodeUtils.equalsUsingCompareToOnFields(f1, f2, "canCompare1", "canCompare2")
        );
    }

    @Test
    public void test_equalsUsingCompareToOnFields_sameInstance() {
        Foo f1 = new Foo(canCompare1: "a", canCompare2: "b", cannotCompare: Collections.emptyList());

        Assert.assertTrue(
                EqualsAndHashCodeUtils.equalsUsingCompareToOnFields(f1, f1, "canCompare1", "canCompare2")
        );
    }

    @Test
    public void test_equalsUsingCompareToOnFields_nonComparableField() {
        Foo f1 = new Foo(canCompare1: "a", canCompare2: "b", cannotCompare: Collections.emptyList());
        Foo f2 = new Foo(canCompare1: "a", canCompare2: "c", cannotCompare: Collections.emptyList());
        Assert.assertFalse(
                EqualsAndHashCodeUtils.equalsUsingCompareToOnFields(f1, f2, "cannotCompare", "canCompare1")
        );
    }

    @Test
    public void test_equalsUsingCompareToOnFields_nonExistentField() {
        Foo f1 = new Foo(canCompare1: "a", canCompare2: "b", cannotCompare: Collections.emptyList());
        Foo f2 = new Foo(canCompare1: "a", canCompare2: "c", cannotCompare: Collections.emptyList());
        shouldFail(RuntimeException) {
            EqualsAndHashCodeUtils.equalsUsingCompareToOnFields(f1, f2, "scoobydoo")
        }
    }

    @Test
    public void test_hashCodeForCalendars() {
        Calendar c1 = createCalendar();
        Calendar c2 = createCalendar();
        c1.lenient = false
        c2.lenient = true
        Assert.assertFalse((c1.equals(c2)))
        Assert.assertFalse(c1.hashCode() == c2.hashCode())
        Assert.assertTrue(c1.compareTo(c2) == 0);

        Bar b1 = new Bar(name: "a", cal: c1)
        Bar b2 = new Bar(name: "a", cal: c2)

        Assert.assertEquals(b1.hashCode(), b2.hashCode())
    }

    @Test
    public void test_hashCodeForCalendars_shouldBeUnequal() {
        Calendar c1 = createCalendar();
        Calendar c2 = createCalendar();
        c1.set(Calendar.DATE, 16);

        Bar b1 = new Bar(name: "a", cal: c1)
        Bar b2 = new Bar(name: "a", cal: c2)
        Assert.assertFalse((c1.equals(c2)))
        Assert.assertFalse(c1.hashCode() == c2.hashCode())
        Assert.assertTrue(c1.compareTo(c2) != 0);
        Assert.assertFalse(b1.hashCode() == b2.hashCode())

    }
}
