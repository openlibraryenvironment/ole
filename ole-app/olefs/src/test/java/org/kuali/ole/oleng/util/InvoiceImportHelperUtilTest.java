package org.kuali.ole.oleng.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.common.i18n.Exception;
import org.junit.Before;
import org.junit.Test;
import org.kuali.ole.pojo.OleInvoiceRecord;
import org.kuali.ole.pojo.edi.INVOrder;
import org.kuali.ole.pojo.edi.LineItemOrder;
import org.kuali.rice.krad.service.BusinessObjectService;
import org.kuali.rice.krad.service.LookupService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by SheikS on 2/26/2016.
 */
public class InvoiceImportHelperUtilTest {

    @Mock
    private LookupService mockLookupService;

    @Mock
    private BusinessObjectService mockBusinessObjectService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testReadEDIContent() throws java.lang.Exception {
        String ediFilePath = org.kuali.ole.loaders.common.FileUtils.getFilePath("org/kuali/ole/oleng/invoice/invoice.edi");
        File ediFile = new File(ediFilePath);
        String rawContent = FileUtils.readFileToString(ediFile);
        InvoiceImportHelperUtil invoiceImportHelperUtil = new InvoiceImportHelperUtil();
        List<INVOrder> invOrders = invoiceImportHelperUtil.readEDIContent(rawContent);
        assertTrue(CollectionUtils.isNotEmpty(invOrders));
        System.out.println(invOrders);
        OleNGInvoiceRecordBuilderUtil oleNGInvoiceRecordBuilderUtil = OleNGInvoiceRecordBuilderUtil.getInstance();
        oleNGInvoiceRecordBuilderUtil.setLookupService(mockLookupService);
        oleNGInvoiceRecordBuilderUtil.setBusinessObjectService(mockBusinessObjectService);
        List<OleInvoiceRecord> oleInvoiceRecords = new ArrayList<>();
        for (Iterator<INVOrder> iterator = invOrders.iterator(); iterator.hasNext(); ) {
            INVOrder invOrder = iterator.next();
            if(CollectionUtils.isNotEmpty(invOrder.getLineItemOrder())) {
                for (Iterator<LineItemOrder> invOrderIterator = invOrder.getLineItemOrder().iterator(); invOrderIterator.hasNext(); ) {
                    LineItemOrder lineItemOrder = invOrderIterator.next();
                    OleInvoiceRecord oleInvoiceRecord = oleNGInvoiceRecordBuilderUtil.build(lineItemOrder, invOrder);
                    oleInvoiceRecords.add(oleInvoiceRecord);
                }
            }
        }
        assertTrue(CollectionUtils.isNotEmpty(oleInvoiceRecords));
    }

    @Test
    public void splitContentByLineItem() throws java.lang.Exception {
        String ediFilePath = org.kuali.ole.loaders.common.FileUtils.getFilePath("org/kuali/ole/oleng/invoice/swets030416.inv");
        File ediFile = new File(ediFilePath);
        String rawContent = FileUtils.readFileToString(ediFile);
        String[] lineItems = rawContent.split("'LIN");
        StringBuilder contentBuilder = new StringBuilder();
        for(String lineItem : lineItems) {
            if(StringUtils.isNotBlank(contentBuilder.toString())) {
                contentBuilder.append("'LIN");
            }
            contentBuilder.append(lineItem).append("\n");

        }
        assertTrue(StringUtils.isNotBlank(contentBuilder.toString()));
        System.out.println(contentBuilder.toString());
    }
}