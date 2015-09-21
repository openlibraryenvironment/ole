package org.kuali.ole.deliver.util;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 8/17/15.
 */
public class OleItemRecordForCircTest {

    @Test
    public void getFullPathLocation() {
        OleItemRecordForCirc oleItemRecordForCirc = new OleItemRecordForCirc();
        oleItemRecordForCirc.setItemLocation("shelving");
        oleItemRecordForCirc.setCollectionLocation("collection");
        oleItemRecordForCirc.setItemLibraryLocation("library");
        String itemFullPathLocation = oleItemRecordForCirc.getItemFullPathLocation();
        assertNotNull(itemFullPathLocation);
        System.out.println(itemFullPathLocation);
    }

}