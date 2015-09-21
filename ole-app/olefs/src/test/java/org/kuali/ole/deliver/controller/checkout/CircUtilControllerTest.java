package org.kuali.ole.deliver.controller.checkout;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sheiksalahudeenm on 8/24/15.
 */
public class CircUtilControllerTest {

    @Test
    public void testInValidateTime() throws Exception {
        boolean validateTime = new CircUtilController().validateTime("a:b");
        assertFalse(validateTime);
    }

    @Test
    public void testValidateTime() throws Exception {
        boolean validateTime = new CircUtilController().validateTime("11:20");
        assertTrue(validateTime);
    }
}