package org.kuali.ole.ingest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.deliver.constant.OLEPatronConstant;
import org.kuali.ole.ingest.controller.OlePatronRecordController;
import org.kuali.ole.ingest.form.OlePatronRecordForm;
import org.kuali.ole.ingest.pojo.OlePatronGroup;
import org.kuali.ole.service.OlePatronConverterService;
import org.kuali.rice.core.api.resourceloader.GlobalResourceLoader;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: pvsubrah
 * Date: 5/31/12
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */

public class OlePatronRecordController_IT extends KFSTestCaseBase{

    @Mock
    private OlePatronRecordForm mockUifFormBase;
    @Mock
    private BindingResult mockBindingResult;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private ModelAndView mockModelView;

    public  MockOlePatronRecordController mockOlePatronRecordController;
    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mockOlePatronRecordController = new MockOlePatronRecordController();

    }
    public  String patronRecordContent ;

    @Test
    @Transactional
    public void testUpload() throws Exception {
        OlePatronConverterService olePatronRecordService = GlobalResourceLoader.getService(OLEConstants.PATRON_CONVERTER_SERVICE);
        OlePatronRecordHandler olePatronRecordHandler = new OlePatronRecordHandler();
        URL resource = getClass().getResource(OLEPatronConstant.PATRON_FILE_NAME);
        File file = new File(resource.toURI());
        patronRecordContent = new FileUtil().readFile(file);
        OlePatronGroup olePatron = olePatronRecordHandler.buildPatronFromFileContent(patronRecordContent);
        MockMultiPartFile mockMultiPartFile = new MockMultiPartFile();
        Mockito.when(mockUifFormBase.getPatronFile()).thenReturn((mockMultiPartFile));
        Mockito.when(mockUifFormBase.isAddUnmatchedPatron()).thenReturn(true);
        MockOlePatronRecordController mockOlePatronRecordController = new MockOlePatronRecordController();
        ModelAndView modelAndView =
                mockOlePatronRecordController.upload(mockUifFormBase, mockBindingResult, mockRequest, mockResponse);

        assertNotNull(modelAndView);
    }

    @Test
    @Transactional
    public void testValidateFile() throws Exception {
        assertTrue(mockOlePatronRecordController.validateFile(OLEPatronConstant.PATRON_FILE_NAME));
    }

    private class MockOlePatronRecordController extends OlePatronRecordController {

    }

    public class MockMultiPartFile implements MultipartFile {

        @Override
        public String getName() {
            return OLEPatronConstant.PATRON_MULTIPART_FIELD_NAME;
        }

        @Override
        public String getOriginalFilename() {
            return OLEPatronConstant.PATRON_FILE_NAME;
        }

        @Override
        public String getContentType() {
            return OLEPatronConstant.PATRON_MULTIPART_CONTENT_TYPE;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public long getSize() {
            return 0;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return patronRecordContent.getBytes();
        }

        @Override
        public InputStream getInputStream() throws IOException {

            return new ByteArrayInputStream(patronRecordContent.getBytes());
        }

        @Override
        public void transferTo(File file) throws IOException, IllegalStateException {


        }
    }

}
