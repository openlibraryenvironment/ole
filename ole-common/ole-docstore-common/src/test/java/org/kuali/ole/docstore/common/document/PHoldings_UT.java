package org.kuali.ole.docstore.common.document;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.docstore.common.document.content.enums.DocCategory;
import org.kuali.ole.docstore.common.document.content.enums.DocFormat;
import org.kuali.ole.docstore.common.document.content.enums.DocType;
import org.kuali.ole.docstore.common.document.content.instance.CallNumber;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;
import org.kuali.ole.docstore.common.document.content.instance.ShelvingScheme;
import org.kuali.ole.docstore.common.document.content.instance.xstream.HoldingOlemlRecordProcessor;
import org.kuali.ole.docstore.common.document.content.instance.xstream.ItemOlemlRecordProcessor;


/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 1/7/14
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class PHoldings_UT {


    @Test
    public void  serializeAndDedeserialize() {
        Holdings holdings = getHoldingsRecord();
        String serializeXml = holdings.serialize(holdings);
        Assert.assertNotNull(serializeXml);

        Holdings holdingsObj = new PHoldings();
        holdingsObj=(Holdings) holdings.deserialize(serializeXml);
        Assert.assertSame(holdingsObj, holdingsObj);
    }



    private Item getItemRecord() {
        Item item = new ItemOleml();
        item.setCategory("work");
        item.setType("item");
        item.setFormat("oleml");
        org.kuali.ole.docstore.common.document.content.instance.Item item1 = new org.kuali.ole.docstore.common.document.content.instance.Item();
        item1.setVolumeNumber("123");
        item1.setChronology("12344");
        item1.setEnumeration("en");
        item1.setBarcodeARSL("bararsl");
        item.setContent(new ItemOlemlRecordProcessor().toXML(item1));
        return item;
    }

    private Holdings getHoldingsRecord() {
        Holdings holdings = new PHoldings();
        holdings.setCategory(DocCategory.WORK.getCode());
        holdings.setType(DocType.HOLDINGS.getCode());
        holdings.setFormat(DocFormat.OLEML.getCode());

        OleHoldings oleHoldings = new OleHoldings();
        ShelvingScheme shelvingScheme = new ShelvingScheme();
        shelvingScheme.setCodeValue("LCC");
        CallNumber callNumber = new CallNumber();
        callNumber.setNumber("1234");
        callNumber.setShelvingScheme(shelvingScheme);
        oleHoldings.setCallNumber(callNumber);
        holdings.setContent(new HoldingOlemlRecordProcessor().toXML(oleHoldings));
        holdings.setBib(getBibRecord());
        return holdings;
    }

    private Bib getBibRecord() {
        Bib bib = new Bib();
        bib.setCategory(DocCategory.WORK.getCode());
        bib.setType(DocType.BIB.getCode());
        bib.setFormat(DocFormat.MARC.getCode());
        bib.setId("wbm-10000001");
        return bib;
    }

}
