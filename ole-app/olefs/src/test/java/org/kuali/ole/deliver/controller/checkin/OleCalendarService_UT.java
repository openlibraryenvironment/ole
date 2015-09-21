package org.kuali.ole.deliver.controller.checkin;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.deliver.calendar.bo.OleCalendar;
import org.kuali.ole.deliver.calendar.bo.OleCalendarGroup;
import org.kuali.ole.deliver.calendar.service.impl.OleCalendarServiceImpl;
import org.kuali.ole.deliver.service.ParameterValueResolver;
import org.kuali.ole.deliver.util.ItemFineRate;
import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.util.DocstoreUtil;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * Created by pvsubrah on 7/27/15.
 */
public class OleCalendarService_UT {

    @Mock
    private ParameterValueResolver mockParameterValueResolver;

    @Mock
    private OleCalendarGroup mockOleCalendarGroup;

    @Mock
    private OleCalendar mockOleCalendar;

    @Mock
    private BusinessObjectService mockBusinessObjectService;

    @Mock
    private ItemInfoUtil mockItemInfoUtil;

    @Mock
    private ItemRecord mockItemRecord;

    @Mock
    private DocstoreUtil mockDocstoreUtil;

    @Mock
    private OleItemSearch mockItemSearch;

    @Mock
    private OleItemRecordForCirc mockOleItemRecordForCirc;


    @Before
    public void setUp() throws  Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void generateOverdueFineForHoursWithIncludingHours() throws Exception {
        Mockito.when(mockParameterValueResolver.getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.FINE_FLAG)).thenReturn("true");
        OleCalendarServiceImpl oleCalendarService = new OleCalendarServiceImpl();
        oleCalendarService.setParameterValueResolver(mockParameterValueResolver);
        String groupId = "100";

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        Timestamp dueDate = new Timestamp(calendar.getTimeInMillis());

        Timestamp checkInDate =new Timestamp(System.currentTimeMillis());

        ItemFineRate itemFineRate = new ItemFineRate();
        itemFineRate.setInterval("H");
        itemFineRate.setFineRate(10.0);

        Double fine = oleCalendarService.calculateOverdueFine(groupId, dueDate, checkInDate, itemFineRate);
        assertNotNull(fine);
        System.out.println("Fine Rate : " + fine);
    }

    @Test
    public void generateOverdueFineForHoursWithOutIncludingHours() throws Exception {
        Mockito.when(mockParameterValueResolver.getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT, OLEConstants.FINE_FLAG)).thenReturn("false");

        OleCalendarServiceImpl oleCalendarService = new MockMockOleCalendarServiceImpl();
        oleCalendarService.setParameterValueResolver(mockParameterValueResolver);

        String groupId = "100";

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        Timestamp dueDate = new Timestamp(calendar.getTimeInMillis());

        Timestamp checkInDate =new Timestamp(System.currentTimeMillis());

        ItemFineRate itemFineRate = new ItemFineRate();
        itemFineRate.setInterval("H");
        itemFineRate.setFineRate(10.0);


        Double fine = oleCalendarService.calculateOverdueFine(groupId, dueDate, checkInDate, itemFineRate);
        assertNotNull(fine);
        System.out.println("Fine Rate : " + fine);
    }

    @Test
    public void generateOverdueFineForDays() throws Exception {
        Mockito.when(mockParameterValueResolver.getParameter(OLEConstants
                .APPL_ID_OLE, OLEConstants.DLVR_NMSPC, OLEConstants.DLVR_CMPNT,OLEConstants.FINE_FLAG)).thenReturn("true");
        OleCalendarServiceImpl oleCalendarService = new MockMockOleCalendarServiceImpl();
        oleCalendarService.setParameterValueResolver(mockParameterValueResolver);
        String groupId = "100";

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -5);

        Timestamp dueDate = new Timestamp(calendar.getTimeInMillis());

        Timestamp checkInDate =new Timestamp(System.currentTimeMillis());


        ItemFineRate itemFineRate = new ItemFineRate();
        itemFineRate.setInterval("D");
        itemFineRate.setFineRate(10.0);

        Double fine = oleCalendarService.calculateOverdueFine(groupId, dueDate, checkInDate, itemFineRate);
        assertNotNull(fine);
        System.out.println("Fine Rate : " + fine);
    }


    class MockMockOleCalendarServiceImpl extends OleCalendarServiceImpl {
        @Override
        public boolean isHoliday(String deskId, Timestamp currentDate) {
            return true;
        }

        @Override
        public Integer workingHours(String deskId, Timestamp dueDateTime, Timestamp currentDate) {
            return 12;
        }
    }

}