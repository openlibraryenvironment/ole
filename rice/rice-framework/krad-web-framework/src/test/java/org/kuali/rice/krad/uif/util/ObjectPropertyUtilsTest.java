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
package org.kuali.rice.krad.uif.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ObjectPropertyUtilsTest extends ProcessLoggingUnitTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
        String afoo();
    }

    public class TestBean implements Serializable {

        private static final long serialVersionUID = 1L;

        public TestBean() {}

        private String rwProp;

        public String getRwProp() {
            return this.rwProp;
        }

        public void setRwProp(String r) {
            this.rwProp = r;
        }

        private String woProp;

        public void setWoProp(String w) {
            this.woProp = w;
        }

        private String roProp;

        @TestAnnotation(afoo = "abar")
        public String getRoProp() {
            return this.roProp;
        }

        private Boolean bitProp;

        public boolean isBitProp() {
            return bitProp != null && bitProp;
        }

        public Boolean getBitProp() {
            return bitProp;
        }

        public void setBitProp(Boolean bitProp) {
            this.bitProp = bitProp;
        }

        private boolean booleanProp;

        public boolean isBooleanProp() {
            return booleanProp;
        }

        public void setBooleanProp(boolean booleanProp) {
            this.booleanProp = booleanProp;
        }

        private Timestamp timestampProp;

        public Timestamp getTimestampProp() {
            return timestampProp;
        }

        public void setTimestampProp(Timestamp timestampProp) {
            this.timestampProp = timestampProp;
        }

        private Date dateProp;

        public Date getDateProp() {
            return dateProp;
        }

        public void setDateProp(Date dateProp) {
            this.dateProp = dateProp;
        }

        private int intProp;

        public int getIntProp() {
            return intProp;
        }

        private BigDecimal bigDecimalProp;

        public BigDecimal getBigDecimalProp() {
            return bigDecimalProp;
        }

        public void setBigDecimalProp(BigDecimal bigDecimalProp) {
            this.bigDecimalProp = bigDecimalProp;
        }

        public void setIntProp(int intProp) {
            this.intProp = intProp;
        }

        private Integer integerProp;

        public Integer getIntegerProp() {
            return integerProp;
        }

        public void setIntegerProp(Integer integerProp) {
            this.integerProp = integerProp;
        }

        private TestBean next;

        public TestBean getNext() {
            return next;
        }

        public void setNext(TestBean next) {
            this.next = next;
        }

        private List<String> stuffs;

        public List<String> getStuffs() {
            return stuffs;
        }

        public void setStuffs(List<String> stuffs) {
            this.stuffs = stuffs;
        }

        private Object[] arrayProp;

        public Object[] getArrayProp() {
            return arrayProp;
        }

        public void setArrayProp(Object[] arrayProp) {
            this.arrayProp = arrayProp;
        }

        private Map<String, Object> mapProp;

        public Map<String, Object> getMapProp() {
            return this.mapProp;
        }

        public void setMapProp(Map<String, Object> mapProp) {
            this.mapProp = mapProp;
        }

    }

    @Test
    public void testSetBoolean() {
        TestBean tb = new TestBean();
        ObjectPropertyUtils.setPropertyValue(tb, "booleanProp", "true");
        assertTrue(tb.isBooleanProp());
    }

    @Test
    public void testGetPropertyDescriptor() {
        TestBean tb = new TestBean();
        PropertyDescriptor[] pds = ObjectPropertyUtils.getPropertyDescriptors(tb);
        boolean rwProp = false;
        boolean roProp = false;
        boolean woProp = false;
        boolean foobar = false;
        for (PropertyDescriptor pd : pds) {
            if (pd.getName().equals("rwProp")) {
                rwProp = true;
            }
            if (pd.getName().equals("roProp")) {
                roProp = true;
            }
            if (pd.getName().equals("woProp")) {
                woProp = true;
            }
            if (pd.getName().equals("foobar")) {
                foobar = true;
            }
        }
        assertTrue(rwProp);
        assertTrue(roProp);
        assertTrue(woProp);
        assertFalse(foobar);
    }

    @Test
    public void testGet() {
        TestBean tb = new TestBean();
        tb.setRwProp("foobar");
        assertEquals("foobar", ObjectPropertyUtils.getPropertyValue(tb, "rwProp"));

        tb.roProp = "barbaz";
        assertEquals("barbaz", ObjectPropertyUtils.getPropertyValue(tb, "roProp"));

        try {
            ObjectPropertyUtils.getPropertyValue(tb, "woProp");
            fail("expected exception");
        } catch (RuntimeException E) {
            // OK!
        }
    }

    @Test
    public void testLookup() {
        TestBean tb = new TestBean();
        tb.roProp = "barbaz";
        assertEquals("barbaz", ObjectPropertyUtils.getPropertyValue(tb, "roProp"));

        Map<String, Object> tm = new java.util.HashMap<String, Object>();
        tb.setMapProp(tm);
        tm.put("barbaz", "hooray!");
        assertEquals("hooray!", ObjectPropertyUtils.getPropertyValue(tb, "mapProp['barbaz']"));
        assertEquals("hooray!", ObjectPropertyUtils.getPropertyValue(tb, "mapProp[\"barbaz\"]"));

        TestBean tb2 = new TestBean();
        tb2.setRwProp("foodbar");
        tb.setNext(tb2);
        tm.put("blah", new Object[]{"next", "rwProp"});
        tm.put("baz", tb2);
        assertTrue(ObjectPropertyUtils.isReadableProperty(tb, "mapProp[\"baz\"].rwProp"));
        assertEquals("barbaz", ObjectPropertyUtils.getPropertyValue(tb, "roProp"));
        assertEquals("foodbar", ObjectPropertyUtils.getPropertyValue(tb, "next.rwProp"));

        tb.setStuffs(Arrays.asList(new String[]{"foo", "bar", "baz",}));
        assertEquals("bar", ObjectPropertyUtils.getPropertyValue(tb, "stuffs[1]"));

        TestBean rb = new TestBean();
        TestBean nb = new TestBean();
        TestBean lb = new TestBean();
        rb.setNext(nb);
        nb.setNext(lb);
        assertEquals(String.class, ObjectPropertyUtils.getPropertyType(rb, "next.next.rwProp"));
        rb.setRwProp("baz");
        nb.setRwProp("bar");
        lb.setRwProp("foo");
        assertEquals("foo", ObjectPropertyUtils.getPropertyValue(rb, "next.next.rwProp"));
    }

    @Test
    public void testSet() {
        TestBean tb = new TestBean();
        ObjectPropertyUtils.setPropertyValue(tb, "rwProp", "foobar");
        assertEquals("foobar", tb.getRwProp());

        ObjectPropertyUtils.setPropertyValue(tb, "woProp", "barbaz");
        assertEquals("barbaz", tb.woProp);

        try {
            ObjectPropertyUtils.setPropertyValue(tb, "roProp", "bazfoo");
            fail("expected exception");
        } catch (Exception E) {
            // OK!
        }

        long now = System.currentTimeMillis();
        ObjectPropertyUtils.setPropertyValue(tb, "dateProp", new java.sql.Date(now));
        assertEquals(now, tb.getDateProp().getTime());
    }

    @Test
    public void testPathSet() {
        TestBean tb = new TestBean();
        ObjectPropertyUtils.setPropertyValue(tb, "rwProp", "bar");
        assertEquals("bar", tb.getRwProp());
        ObjectPropertyUtils.setPropertyValue(tb, "next", new TestBean());
        ObjectPropertyUtils.setPropertyValue(tb, "next.next", new TestBean());
        ObjectPropertyUtils.setPropertyValue(tb, "next.next.woProp", "baz");
        assertEquals("baz", tb.getNext().getNext().woProp);
    }

    @Test
    public void testBulk() {
        Map<String, String> pd = new java.util.HashMap<String, String>();
        pd.put("rwProp", "foobar");
        pd.put("intProp", "3");
        pd.put("booleanProp", "true");
        pd.put("stuffs", "foo,bar,baz");
        for (int i = 0; i < 10000; i++) {
            TestBean tb = new TestBean();
            ObjectPropertyUtils.copyPropertiesToObject(pd, tb);
            assertEquals("foobar", tb.getRwProp());
            assertEquals(3, tb.getIntProp());
            assertEquals(true, tb.isBooleanProp());
            assertEquals(3, tb.getStuffs().size());
            assertEquals("foo", tb.getStuffs().get(0));
            assertEquals("bar", tb.getStuffs().get(1));
            assertEquals("baz", tb.getStuffs().get(2));
        }
    }

}
