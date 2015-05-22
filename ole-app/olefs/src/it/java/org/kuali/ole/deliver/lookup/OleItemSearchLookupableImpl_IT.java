package org.kuali.ole.deliver.lookup;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.docstore.common.document.Bib;
import org.kuali.ole.docstore.common.service.DocstoreService;
import org.kuali.ole.docstore.service.BeanLocator;
import org.kuali.rice.krad.web.form.LookupForm;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Palanivelrajanb on 5/12/2015.
 */
public class OleItemSearchLookupableImpl_IT extends OLETestCaseBase{

    DocstoreService docstoreService = null;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        docstoreService = BeanLocator.getDocstoreService();
    }

    @Test
    public void testCreateBib() {
        String xml = getXmlAsString("/org/kuali/ole/BibMarc.xml");
        Bib bib = new Bib();
        bib = (Bib) bib.deserialize(xml);
        bib.setId(null);
        docstoreService.createBib(bib);
        Assert.assertNotNull(bib.getId());
    }

    public String getXmlAsString(String filePath) {
        String input = "";
        File file = null;
        try {
            file = new File(getClass().getResource(filePath).toURI());
            input = FileUtils.readFileToString(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    @Test
    public void searchPublishierTest(){
        OleItemSearchLookupableImpl oleItemSearchLookupable = new OleItemSearchLookupableImpl();
        testCreateBib();
        Map<String,String> searchCriteria = new HashMap<String, String>();
        searchCriteria.put("publisher","printed for T. Trye");
        searchCriteria.put("pageSize","1");
        LookupForm form = new LookupForm();
        Map map = new HashedMap();
        map.put("author","");
        map.put("title","");
        //map.put("callNumber","0");
        map.put("itemType","");
        //map.put("itemBarcode","");
        map.put("publisher","printed for T. Trye");
        map.put("pageSize","1");
        form.setLookupCriteria(map);
        form.setDataObjectClassName("org.kuali.ole.deliver.bo.OleItemSearch");

        List result = (List)oleItemSearchLookupable.performSearch(form,searchCriteria,false);
        Assert.assertNotNull(result);
    }
}
