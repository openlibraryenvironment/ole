package org.kuali.ole;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 11/22/2015.
 */
public class CopyRunOncePropertiesFileBeanTest {

    @Test
    public void testCopyRunOncePropertiesFile() throws Exception {
        CopyRunOncePropertiesFileBean copyRunOncePropertiesFileBean = new MockCopyRunOncePropertiesFileBean();
        copyRunOncePropertiesFileBean.copyRunOncePropertiesFile(true);
    }

    class MockCopyRunOncePropertiesFileBean extends CopyRunOncePropertiesFileBean {
        @Override
        public void createOrUpdateParameter() {

        }

        @Override
        public String getProjectHome() {
            String tempDir = System.getProperty("java.io.tmpdir");
            System.out.println("Temp Dir : " + tempDir);
            return tempDir;
        }
    }
}