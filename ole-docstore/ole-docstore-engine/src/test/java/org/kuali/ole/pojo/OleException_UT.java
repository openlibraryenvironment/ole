package org.kuali.ole.pojo;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 1/21/13
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleException_UT extends BaseTestCase {

    @Test
    public void testOleException() throws OleException, ArithmeticException {

        try {
            throw new OleException();
        } catch (OleException e) {
            e.getCause();
            e.getMessage();
            e.getStackTrace();
        }
        try {
            throw new OleException("exception", new Throwable());
        } catch (OleException e) {
            e.getCause();
            e.getMessage();
            e.getStackTrace();
        }
        try {
            throw new OleException("exception");
        } catch (OleException e) {
            e.getCause();
            e.getMessage();
            e.getStackTrace();
        }
        try {
            throw new OleException(new Throwable());
        } catch (OleException e) {
            e.getCause();
            e.getMessage();
            e.getStackTrace();
        }
    }
}
