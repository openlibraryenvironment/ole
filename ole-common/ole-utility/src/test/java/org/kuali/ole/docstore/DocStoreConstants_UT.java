package org.kuali.ole.docstore;

import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: SM8451
 * Date: 12/15/12
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocStoreConstants_UT {

    @Test
    public void testConstants() {
        DocStoreConstants docStoreConstants = new DocStoreConstants();
        try {
            boolean isVersioningEnabled = DocStoreConstants.isVersioningEnabled;
            throw new OleException("To use in test cases");
        } catch (OleException e) {
            e.getMessage();
        }

        try {
            boolean isVersioningEnabled = DocStoreConstants.isVersioningEnabled;
            throw new OleException();
        } catch (OleException e) {
            e.getMessage();
        }


    }
}
