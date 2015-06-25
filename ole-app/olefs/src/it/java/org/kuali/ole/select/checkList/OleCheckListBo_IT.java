package org.kuali.ole.select.checkList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.ingest.FileUtil;
import org.kuali.ole.select.bo.OleCheckListBo;
import org.kuali.ole.select.maintenance.OleCheckListMaintenanceImpl;
import org.kuali.rice.core.api.CoreApiServiceLocator;
import org.kuali.rice.core.api.config.property.ConfigurationService;
import org.kuali.rice.kim.api.identity.Person;
import org.kuali.rice.krad.UserSession;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.kuali.rice.krad.util.GlobalVariables;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.util.UUID;

import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: ?
 * Date: 7/11/12
 * Time: 6:36 PM
 * To change this template use File | Settings | File Templates.
 */

public class OleCheckListBo_IT extends KFSTestCaseBase {
    private String MULTIPART_FILE_NAME = "spellings.txt";
    private String FILE_NAME="spellings.txt";
    private String MULTIPART_CONTENT_TYPE="text/plain";
    private String fileContent = "";
    private BusinessObjectService businessObjectService;
    private File file;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        businessObjectService = KRADServiceLocator.getBusinessObjectService();
    }

    @Test
    @Transactional
    public void testCreateCheckList() throws Exception {
        URL resource = getClass().getResource(MULTIPART_FILE_NAME);
        file = new File(resource.toURI());
        fileContent = new FileUtil().readFile(file);
        OleCheckListBo checkListBo = new OleCheckListBo();
        checkListBo.setFileName(FILE_NAME);
        GlobalVariables.setUserSession(new UserSession("admin"));
        Person kualiUser = GlobalVariables.getUserSession().getPerson();
        checkListBo.setDescription("Mock Description");
        checkListBo.setName("Mock CheckList Name");
        checkListBo.setLastModified((Timestamp) CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp());
        checkListBo.setAuthor(kualiUser.getPrincipalId());
        checkListBo.setMimeType(MULTIPART_CONTENT_TYPE);
        checkListBo.setRemoteObjectIdentifier(UUID.randomUUID().toString());
        checkListBo.setActiveIndicator(true);
        OleCheckListMaintenanceImpl mainImple = new OleCheckListMaintenanceImpl();

        OleCheckListBo savedcheckListBo = businessObjectService.save(checkListBo);
        assertNotNull(savedcheckListBo);
        assertNotNull(savedcheckListBo.getOleCheckListId());
        /*File documentDirectory = new File(getKualiConfigurationService().getPropertyValueAsString(
                KRADConstants.ATTACHMENTS_PENDING_DIRECTORY_KEY)+ File.separator+"checkList"+File.separator+savedcheckListBo.getObjectId()+File.separator+FILE_NAME);
        assertTrue(documentDirectory.exists());
*/
    }

   /* public ConfigurationService getKualiConfigurationService() {
        return KRADServiceLocator.getKualiConfigurationService();
    }*/



    @Test
    @Transactional
    public void testSearchCheckList() throws Exception{
        URL resource = getClass().getResource(MULTIPART_FILE_NAME);
        file = new File(resource.toURI());
        fileContent = new FileUtil().readFile(file);
        OleCheckListBo checkListBo = new OleCheckListBo();
        checkListBo.setFileName(FILE_NAME);
        GlobalVariables.setUserSession(new UserSession("admin"));
        Person kualiUser = GlobalVariables.getUserSession().getPerson();
        checkListBo.setDescription("Mock Description");
        checkListBo.setName("Mock CheckList Name");
        checkListBo.setRemoteObjectIdentifier(UUID.randomUUID().toString());
        checkListBo.setLastModified((Timestamp) CoreApiServiceLocator.getDateTimeService().getCurrentTimestamp());
        checkListBo.setAuthor(kualiUser.getPrincipalId());
        checkListBo.setMimeType(MULTIPART_CONTENT_TYPE);
        checkListBo.setActiveIndicator(true);
        OleCheckListMaintenanceImpl mainImple = new OleCheckListMaintenanceImpl();

        OleCheckListBo savedcheckListBo = businessObjectService.save(checkListBo);
        assertNotNull(savedcheckListBo);
        assertNotNull(savedcheckListBo.getOleCheckListId());


        OleCheckListBo oleCheckListBo = businessObjectService.findBySinglePrimaryKey(OleCheckListBo.class, savedcheckListBo.getOleCheckListId());
        assertNotNull(oleCheckListBo);


    }
}
