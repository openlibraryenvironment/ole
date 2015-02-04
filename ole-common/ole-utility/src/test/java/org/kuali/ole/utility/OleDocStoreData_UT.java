package org.kuali.ole.utility;

import org.junit.Test;
import org.kuali.ole.OleDocStoreData;
import org.kuali.ole.docstore.model.enums.DocFormat;
import org.kuali.ole.docstore.model.enums.DocType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: SR8455
 * Date: 12/20/12
 * Time: 3:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleDocStoreData_UT {

    private static final Logger LOG = LoggerFactory.getLogger(OleDocStoreData_UT.class);

    @Test
    public void testCoverage() throws Exception {
        LOG.info("testing");
    }

    @Test
    public void testOleDocStoreData() throws Exception {

        OleDocStoreData oleDocStoreData = new OleDocStoreData();
        oleDocStoreData.setCategory("Fictional");
        if (oleDocStoreData.getCategory() != null) {
            LOG.info("Category" + oleDocStoreData.getCategory());
        }

        List<String> docTypeList = new ArrayList<String>();
        docTypeList.add("BIB");
        docTypeList.add("HOLDINGS");
        docTypeList.add("ITEM");
        oleDocStoreData.setDoctypes(docTypeList);
        if (oleDocStoreData.getDoctypes() != null) {
            LOG.info("DOCTYPES");
            for (String docType : oleDocStoreData.getDoctypes()) {
                LOG.info(docType);
            }
        }
        if (oleDocStoreData.getFormats() != null) {
            LOG.info("FORMATS");
            for (String format : oleDocStoreData.getFormats()) {
                LOG.info(format);
            }
        }
        if (oleDocStoreData.getTypeFormatMap() != null) {
            LOG.info("FORMAT MAP");
            for (Map.Entry<String, List<String>> entry : oleDocStoreData.getTypeFormatMap().entrySet()) {
                LOG.info(entry.getKey());
                for (String stringList : entry.getValue()) {
                    LOG.info(stringList);
                }
            }
        }
        oleDocStoreData.addFormat("BIB", DocFormat.DOC.getCode());
        oleDocStoreData.addDocType("INSTANCE");
        oleDocStoreData.addLevelInfoForFormat(null, null);
        oleDocStoreData.setFormatLevelsMap(null);
        oleDocStoreData.setTypeFormatMapWithNodeCount(null);

        if (oleDocStoreData.getFormatLevelsMap() != null) {
            LOG.info("FORMAT LEVELS");
            for (Map.Entry<String, Integer> entry : oleDocStoreData.getFormatLevelsMap().entrySet()) {
                LOG.info(entry.getKey());
                LOG.info(entry.getValue().toString());
            }
        }

        if (oleDocStoreData.getTypeFormatMapWithNodeCount() != null) {
            LOG.info("TYPE FORMAT MAP WITH NODE COUNT");
            for (Map.Entry<String, Map<String, Long>> entry : oleDocStoreData.getTypeFormatMapWithNodeCount().entrySet()) {
                LOG.info(entry.getKey());
                for (Map.Entry<String, Long> entry2 : entry.getValue().entrySet()) {
                    LOG.info(entry2.getKey());
                    LOG.info(entry2.getValue().toString());
                }
            }
        }


    }

}
