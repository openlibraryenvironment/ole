package org.kuali.ole.spring.batch;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

/**
 * Created by SheikS on 3/16/2016.
 */
public class BatchUtilTest {

    @Test
    public void testGetDetailedMessage() throws Exception {
        try {
            String text = null;
            text.length();
        } catch (Exception e) {
            e.printStackTrace();
            String detailedMessage = new BatchUtil().getDetailedMessage(e);
            assertNotNull(detailedMessage);
            System.out.println(detailedMessage);
        }
    }

    @Test
    public void testGetDetailedMessage1() throws Exception {
        try {
            ArrayList<String> strings = new ArrayList<>();
            strings.addAll(null);
        } catch (Exception e) {
            e.printStackTrace();
            String detailedMessage = new BatchUtil().getDetailedMessage(e);
            assertNotNull(detailedMessage);
            System.out.println(detailedMessage);
        }
    }

    @Test
    public void testGetDetailedMessageIndexOutOfBound() throws Exception {
        try {
            ArrayList<String> strings = new ArrayList<>();
            strings.get(5);
        } catch (Exception e) {
            e.printStackTrace();
            String detailedMessage = new BatchUtil().getDetailedMessage(e);
            assertNotNull(detailedMessage);
            System.out.println(detailedMessage);
        }
    }
}