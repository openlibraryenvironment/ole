package org.kuali.ole.deliver.defaultload;

import org.drools.compiler.compiler.io.File;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 11/23/2015.
 */
public class CopyDefaultRuleFilesBeanTest {

    @Test
    public void testCopyDefaultRuleFiles() throws Exception {
        CopyDefaultRuleFilesBean copyDefaultRuleFilesBean = new MockCopyDefaultRuleFilesBean();
        copyDefaultRuleFilesBean.copyDefaultRuleFiles();
    }

    class MockCopyDefaultRuleFilesBean extends CopyDefaultRuleFilesBean {
        @Override
        public String getRuleDestinationPath() {
            String tempDir = System.getProperty("java.io.tmpdir") + java.io.File.separator + "rules";
            System.out.println("Temp Dir : " + tempDir);
            return tempDir;
        }

        @Override
        public String getRuleSourcePath() {
            return "org/kuali/ole/deliver/rules/demo";
        }
    }

}