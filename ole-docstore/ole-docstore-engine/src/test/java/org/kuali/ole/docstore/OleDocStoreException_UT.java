package org.kuali.ole.docstore;

import org.junit.Test;
import org.kuali.ole.BaseTestCase;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/21/12
 * Time: 2:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDocStoreException_UT extends BaseTestCase {

    @Test
    public void testOleDocStoreException() {

        try {
            throw new OleDocStoreException();
        } catch (OleDocStoreException e) {
            e.getCause();
        }
        try {
            throw new OleDocStoreException("Exception");
        } catch (OleDocStoreException e) {
            e.getMessage();

        }
        try {
            throw new OleDocStoreException("Exception", new Throwable());
        } catch (OleDocStoreException e) {
            e.getStackTrace();
        }
        try {
            throw new OleDocStoreException(new Throwable());
        } catch (OleDocStoreException e) {
            e.getCause();
            e.getMessage();
            e.getStackTrace();
        }
    }
}
