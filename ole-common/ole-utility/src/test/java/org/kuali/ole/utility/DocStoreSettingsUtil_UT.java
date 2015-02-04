package org.kuali.ole.utility;

import org.junit.Test;
import org.kuali.ole.docstore.utility.DocStoreSettingsUtil;
import org.kuali.ole.docstore.xstream.BaseTestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: ?
 * Time: ?
 * To change this template use File | Settings | File Templates.
 */
public class DocStoreSettingsUtil_UT extends BaseTestCase {
    private static final Logger LOG = LoggerFactory.getLogger(DocStoreSettingsUtil_UT.class);

    @Test
    public void testDocStoreSettingsUtil() throws Exception {
        DocStoreSettingsUtil docStoreSettingsUtil = DocStoreSettingsUtil.getInstance();
        docStoreSettingsUtil.copyResources();
    }

}
