package org.kuali.ole.describe.controller;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kuali.ole.KFSTestCaseBase;
import org.kuali.ole.SpringBaseTestCase;
import org.kuali.ole.describe.form.WorkbenchForm;
import org.kuali.ole.docstore.discovery.model.SearchCondition;
import org.kuali.ole.docstore.discovery.model.SearchParams;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: PP7788
 * Date: 11/28/12
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkbenchController_IT extends KFSTestCaseBase {

    @Mock
    private BindingResult mockResult;
    @Mock
    private HttpServletRequest mockRequest;
    @Mock
    private HttpServletResponse mockResponse;
    @Mock
    private ModelAndView mockModelView;
    @Mock
    private WorkbenchForm mockForm;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
    }
    // To-do, Need to be modified this IT based on current Workbench Controller functionality
    @Test
    public void testSearch() throws Exception {
      /* System.setProperty("app.environment", "local");
        SearchParams searchParams = new SearchParams();
        searchParams.setDocCategory("work");
        searchParams.setDocType("bibliographic");
        searchParams.setDocFormat("marc");
        List<SearchCondition> searchConditionList = new ArrayList<SearchCondition>();
        SearchCondition searchCondition = new SearchCondition();
        searchCondition.setSearchText("Carl");
        searchCondition.setOperator("NOT");
        searchCondition.setDocField("Author_search");
        searchCondition.setSearchScope("AND");
        searchConditionList.add(searchCondition);
        searchCondition = new SearchCondition();
        searchCondition.setSearchText("Sandburg");
        searchCondition.setOperator("NOT");
        searchCondition.setDocField("Author_search");
        searchCondition.setSearchScope("OR");
        searchConditionList.add(searchCondition);
        searchParams.setSearchFieldsList(searchConditionList);
        Mockito.when(mockForm.getSearchParams()).thenReturn(searchParams);
        MockWorkbenchController workbenchController = new MockWorkbenchController();
        ModelAndView modelAndView = workbenchController.search(mockForm,mockResult,mockRequest,mockResponse); */
    }

    private class MockWorkbenchController extends WorkbenchController {
        @Override
        protected ModelAndView callSuper(BindingResult result, HttpServletRequest request, HttpServletResponse response, WorkbenchForm workbenchForm) {
            return mockModelView;
        }
    }
}
