package org.kuali.ole.deliver.util.printSlip;

import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.deliver.bo.OleCirculationDesk;
import org.kuali.ole.deliver.bo.OleDeliverRequestBo;
import org.kuali.ole.deliver.bo.OleItemSearch;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.form.CheckinForm;
import org.kuali.ole.deliver.util.ItemInfoUtil;
import org.kuali.ole.deliver.util.OleItemRecordForCirc;
import org.kuali.ole.deliver.util.OleRegularPrintSlipUtil;
import org.kuali.ole.docstore.engine.service.storage.rdbms.pojo.ItemRecord;
import org.kuali.ole.util.DocstoreUtil;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by sheiksalahudeenm on 7/30/15.
 */
public class OlePrintSlipUtilTest {

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private OutputStream mockOutputStream;

    @Mock
    private ItemInfoUtil mockItemInfoUtil;

    @Mock
    private ItemRecord mockItemRecord;

    @Mock
    private OleCirculationDesk mockOleCirculationDesk;

    @Mock
    private OleDeliverRequestBo mockOleDeliverRequestBo;

    @Mock
    private OleItemRecordForCirc mockOleItemRecordForCirc;

    @Mock
    private CheckinForm mockCheckinForm;

    @Mock
    private DocstoreUtil mockDocstoreUtil;

    @Mock
    private OleItemSearch mockItemSearch;

    @Mock
    private OlePatronDocument mockOlePatron;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void printBillForInTransitTest() throws IOException {
        printSlilp(OLEConstants.ITEM_STATUS_IN_TRANSIT);
    }

    @Test
    public void printBillForInTransitForHoldTest() throws IOException {
        printSlilp(OLEConstants.ITEM_STATUS_IN_TRANSIT_HOLD);
    }

    @Test
    public void printBillForInTransitForStaffTest() throws IOException {
        printSlilp(OLEConstants.ITEM_STATUS_IN_TRANSIT_STAFF);
    }

    @Test
    public void printBillForInTransitForLoanTest() throws IOException {
        printSlilp(OLEConstants.ITEM_STATUS_IN_TRANSIT_LOAN);
    }

    @Test
    public void printBillForHoldTest() throws IOException {
        printSlilp(OLEConstants.ITEM_STATUS_ON_HOLD);
    }

    @Test
    public void printBillForLoan() throws IOException {
        String itemStatus = "INTRANSIT-FOR-LOAN";
        Mockito.when(mockItemSearch.getTitle()).thenReturn("Test");
        Mockito.when(mockItemSearch.getCallNumber()).thenReturn("c-12");
        Mockito.when(mockItemSearch.getCopyNumber()).thenReturn("c-123213");

        Mockito.when(mockOleItemRecordForCirc.getItemUUID()).thenReturn("123");
        Mockito.when(mockOleItemRecordForCirc.getItemStatusToBeUpdatedTo()).thenReturn(itemStatus);
        Mockito.when(mockItemRecord.getEffectiveDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        Mockito.when(mockOleItemRecordForCirc.getItemRecord()).thenReturn(mockItemRecord);

        Mockito.when(mockItemInfoUtil.getOleItemRecordForCirc(mockItemRecord, mockOleCirculationDesk)).thenReturn(mockOleItemRecordForCirc);
        Mockito.when(mockDocstoreUtil.getOleItemSearchList("123")).thenReturn(mockItemSearch);


        Mockito.when(mockCheckinForm.getItemBarcode()).thenReturn("123");
        OleRegularPrintSlipUtil inTransiForLoanPrintSlipUtil = new InTransitForLoanRegularPrintSlipUtil();
        setOutputStream(inTransiForLoanPrintSlipUtil, itemStatus);
        inTransiForLoanPrintSlipUtil.setDocstoreUtil(mockDocstoreUtil);
        inTransiForLoanPrintSlipUtil.createPdfForPrintingSlip(mockOleItemRecordForCirc, mockResponse);
    }

    private void printSlilp(String itemStatus) {
        Mockito.when(mockItemSearch.getTitle()).thenReturn("Test");
        Mockito.when(mockItemSearch.getCallNumber()).thenReturn("c-12");
        Mockito.when(mockItemSearch.getCopyNumber()).thenReturn("c-123213");
        Mockito.when(mockItemSearch.getVolumeNumber()).thenReturn("v");

        Mockito.when(mockOleItemRecordForCirc.getItemUUID()).thenReturn("123");
        Mockito.when(mockOleItemRecordForCirc.getItemStatusToBeUpdatedTo()).thenReturn(itemStatus);
        Mockito.when(mockOleItemRecordForCirc.getItemRecord()).thenReturn(mockItemRecord);
        Mockito.when(mockOleItemRecordForCirc.getOleDeliverRequestBo()).thenReturn(mockOleDeliverRequestBo);
        Mockito.when(mockOleItemRecordForCirc.getCheckinLocation()).thenReturn(mockOleCirculationDesk);

        Mockito.when(mockItemRecord.getEffectiveDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        Mockito.when(mockItemRecord.getBarCode()).thenReturn("222222");

        Mockito.when(mockItemInfoUtil.getOleItemRecordForCirc(mockItemRecord, mockOleCirculationDesk)).thenReturn(mockOleItemRecordForCirc);

        Mockito.when(mockDocstoreUtil.getOleItemSearchList("123")).thenReturn(mockItemSearch);

        Mockito.when(mockOleCirculationDesk.getCirculationDeskPublicName()).thenReturn("BL Education");
        Mockito.when(mockOleCirculationDesk.getOnHoldDays()).thenReturn("1");
        Mockito.when(mockOleCirculationDesk.getHoldFormat()).thenReturn(OLEConstants.RECEIPT_PRINTER);
        //Mockito.when(mockOleCirculationDesk.getHoldFormat()).thenReturn(OLEConstants.NORMAL_PRINTER);

        Mockito.when(mockOlePatron.getPatronName()).thenReturn("Adam,Smith");
        Mockito.when(mockOleDeliverRequestBo.getOlePatron()).thenReturn(mockOlePatron);

        Mockito.when(mockOleDeliverRequestBo.getHoldExpirationDate()).thenReturn(new Date(System.currentTimeMillis()));

        Mockito.when(mockCheckinForm.getItemBarcode()).thenReturn("123");


        if (itemStatus.equals(OLEConstants.ITEM_STATUS_IN_TRANSIT)) {
            OlePrintSlipUtil inTransitOleRegularPrintSlipUtil = new InTransitRegularPrintSlipUtil();
            inTransitOleRegularPrintSlipUtil.setDocstoreUtil(mockDocstoreUtil);
            setOutputStream(inTransitOleRegularPrintSlipUtil, OLEConstants.ITEM_STATUS_IN_TRANSIT);
            inTransitOleRegularPrintSlipUtil.createPdfForPrintingSlip(mockOleItemRecordForCirc, mockResponse);
        } else if (itemStatus.equals(OLEConstants.ITEM_STATUS_IN_TRANSIT_HOLD)) {
            OlePrintSlipUtil inTransitForHoldOleRegularPrintSlipUtil = new InTransitForHoldRegularPrintSlipUtil();
            setOutputStream(inTransitForHoldOleRegularPrintSlipUtil, OLEConstants.ITEM_STATUS_IN_TRANSIT_HOLD);
            inTransitForHoldOleRegularPrintSlipUtil.setDocstoreUtil(mockDocstoreUtil);
            inTransitForHoldOleRegularPrintSlipUtil.createPdfForPrintingSlip(mockOleItemRecordForCirc, mockResponse);
        } else if (itemStatus.equals(OLEConstants.ITEM_STATUS_IN_TRANSIT_STAFF)) {
            OlePrintSlipUtil inTransitForStaffRegularPrintSlipUtil = new InTransitForStaffRegularPrintSlipUtil();
            setOutputStream(inTransitForStaffRegularPrintSlipUtil, OLEConstants.ITEM_STATUS_IN_TRANSIT_STAFF);
            inTransitForStaffRegularPrintSlipUtil.setDocstoreUtil(mockDocstoreUtil);
            inTransitForStaffRegularPrintSlipUtil.createPdfForPrintingSlip(mockOleItemRecordForCirc, mockResponse);
        } else if (itemStatus.equals(OLEConstants.ITEM_STATUS_IN_TRANSIT_LOAN)) {
            OlePrintSlipUtil inTransitForLoanRegularPrintSlipUtil = new InTransitForLoanRegularPrintSlipUtil();
            setOutputStream(inTransitForLoanRegularPrintSlipUtil, OLEConstants.ITEM_STATUS_IN_TRANSIT_LOAN);
            inTransitForLoanRegularPrintSlipUtil.setDocstoreUtil(mockDocstoreUtil);
            inTransitForLoanRegularPrintSlipUtil.createPdfForPrintingSlip(mockOleItemRecordForCirc, mockResponse);
        } else if (itemStatus.equals(OLEConstants.ITEM_STATUS_ON_HOLD)) {
            OlePrintSlipUtil olePrintSlipUtil = null;
            if (mockOleCirculationDesk != null && mockOleCirculationDesk.getHoldFormat() != null && mockOleCirculationDesk.getHoldFormat().equals(OLEConstants.RECEIPT_PRINTER)) {
                olePrintSlipUtil = new OnHoldRecieptPrintSlipUtil();
            } else {
                olePrintSlipUtil = new OnHoldRegularPrintSlipUtil();
            }
            setOutputStream(olePrintSlipUtil, OLEConstants.ITEM_STATUS_ON_HOLD);
            olePrintSlipUtil.setDocstoreUtil(mockDocstoreUtil);
            olePrintSlipUtil.createPdfForPrintingSlip(mockOleItemRecordForCirc, mockResponse);
        }
    }

    public void setOutputStream(OlePrintSlipUtil olePrintSlipUtil, String itemStatusToBeUpdatedTo) {
        String tempLocation = System.getProperty("java.io.tmpdir");
        System.out.println("Temp dir : " + tempLocation);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(tempLocation+ "/"+itemStatusToBeUpdatedTo + ".pdf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        olePrintSlipUtil.setOutputStream(fileOutputStream);
    }

    @Test
    public void createPdfForPrintingSlipTest() throws Exception {
        OleRegularPrintSlipUtil printSlipUtil = new InTransitRegularPrintSlipUtil();
        printSlipUtil.setDocstoreUtil(mockDocstoreUtil);
        Mockito.when(mockOleItemRecordForCirc.getItemUUID()).thenReturn("123");
        Mockito.when(mockDocstoreUtil.getOleItemSearchList("123")).thenReturn(mockItemSearch);
        Mockito.when(mockItemRecord.getEffectiveDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        Mockito.when(mockOleItemRecordForCirc.getItemRecord()).thenReturn(mockItemRecord);

        String tempLocation = System.getProperty("java.io.tmpdir");
        System.out.println("Temp dir : " + tempLocation);
        FileOutputStream fileOutputStream = new FileOutputStream(tempLocation+ "/printSlip.pdf");
        printSlipUtil.setOutputStream(fileOutputStream);
        printSlipUtil.createPdfForPrintingSlip(mockOleItemRecordForCirc, mockResponse);
    }


}