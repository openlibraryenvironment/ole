package org.kuali.ole.web;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.base.BaseTestCase;
import org.kuali.ole.docstore.model.enums.DocCategory;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.kuali.ole.docstore.process.LinkingInstanceNBibHandler;
import org.kuali.ole.logger.DocStoreLogger;

import static org.junit.Assert.fail;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 12/17/12
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */
@Ignore
@Deprecated
public class LinkingBibNInstanceAfterBulkIngest_UT extends BaseTestCase {

    DocStoreLogger docStoreLogger = new DocStoreLogger(getClass().getName());
    @Override
       @Before
       public void setUp() throws Exception {
           super.setUp();    //To change body of overridden methods use File | Settings | File Templates.
       }

       @After
       public void tearDown() throws Exception {
       }


    @Test
   public void testRebuildIndexesForMarc() {
       try {
           LinkingInstanceNBibHandler link = LinkingInstanceNBibHandler
                   .getInstance(DocCategory.WORK.getCode(), DocType.BIB.getDescription(),
                                DocFormat.MARC.getDescription());
           link.run();
       } catch (Exception e) {
           docStoreLogger.log(e.getMessage(), e);
           fail();
       }

   }

    @Test
   public void testRebuildIndexesForInstance() {
       try {
           LinkingInstanceNBibHandler link = LinkingInstanceNBibHandler
                   .getInstance(DocCategory.WORK.getCode(), DocType.INSTANCE.getDescription(),
                                DocFormat.OLEML.getCode());
           link.run();
       } catch (Exception e) {
           docStoreLogger.log(e.getMessage(), e);
           fail();
       }

   }
}
