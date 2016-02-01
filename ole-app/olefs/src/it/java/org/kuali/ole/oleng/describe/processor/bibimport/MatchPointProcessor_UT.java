package org.kuali.ole.oleng.describe.processor.bibimport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.kuali.ole.converter.MarcXMLConverter;
import org.kuali.ole.loaders.common.FileUtils;
import org.kuali.ole.oleng.batch.profile.model.BatchProfileMatchPoint;
import org.marc4j.marc.Record;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by SheikS on 12/20/2015.
 */
public class MatchPointProcessor_UT {

    @Test
    public void testPrepareSolrQueryMapForMatchPoint() throws Exception {
        MatchPointProcessor matchPointProcessor = new MatchPointProcessor();

        String rawMarcContent = FileUtils.readFileContent("org/kuali/ole/spring/batch/processor/InvYBP_Test_1207_2rec.mrc");
        List<Record> records = new MarcXMLConverter().convertRawMarchToMarc(rawMarcContent);
        assertTrue(CollectionUtils.isNotEmpty(records));
        Map queryMap = new HashedMap();
        ArrayList<BatchProfileMatchPoint> batchProfileMatchPoints = new ArrayList<>();

        BatchProfileMatchPoint batchProfileMatchPoint1 = new BatchProfileMatchPoint();
        batchProfileMatchPoint1.setDataType("bibliographic");
        batchProfileMatchPoint1.setDataField("980");
        batchProfileMatchPoint1.setSubField("a");

        BatchProfileMatchPoint batchProfileMatchPoint2 = new BatchProfileMatchPoint();
        batchProfileMatchPoint2.setDataType("bibliographic");
        batchProfileMatchPoint2.setDataField("035");
        batchProfileMatchPoint2.setSubField("a");

        BatchProfileMatchPoint batchProfileMatchPoint3 = new BatchProfileMatchPoint();
        batchProfileMatchPoint3.setDataType("bibliographic");
        batchProfileMatchPoint3.setDataField("001");


        BatchProfileMatchPoint batchProfileMatchPoint4 = new BatchProfileMatchPoint();
        batchProfileMatchPoint4.setDataType("bibliographic");
        batchProfileMatchPoint4.setDataField("980");
        batchProfileMatchPoint4.setSubField("a");
        batchProfileMatchPoint4.setDestDataField("050");
        batchProfileMatchPoint4.setDestSubField("c");

        BatchProfileMatchPoint batchProfileMatchPoint5 = new BatchProfileMatchPoint();
        batchProfileMatchPoint5.setDataType("bibliographic");
        batchProfileMatchPoint5.setDataField("035");
        batchProfileMatchPoint5.setSubField("a");
        batchProfileMatchPoint5.setDestDataField("003");

        BatchProfileMatchPoint batchProfileMatchPoint6 = new BatchProfileMatchPoint();
        batchProfileMatchPoint6.setDataType("bibliographic");
        batchProfileMatchPoint6.setDataField("001");
        batchProfileMatchPoint6.setDestDataField("045");
        batchProfileMatchPoint6.setDestSubField("d");

        BatchProfileMatchPoint batchProfileMatchPoint7 = new BatchProfileMatchPoint();
        batchProfileMatchPoint7.setDataType("bibliographic");
        batchProfileMatchPoint7.setDataField("001");
        batchProfileMatchPoint7.setDestDataField("005");

        batchProfileMatchPoints.add(batchProfileMatchPoint1);
        batchProfileMatchPoints.add(batchProfileMatchPoint2);
        batchProfileMatchPoints.add(batchProfileMatchPoint3);
        batchProfileMatchPoints.add(batchProfileMatchPoint4);
        batchProfileMatchPoints.add(batchProfileMatchPoint5);
        batchProfileMatchPoints.add(batchProfileMatchPoint6);
        batchProfileMatchPoints.add(batchProfileMatchPoint7);

        List<String> queryList = new ArrayList<>();

        for (Iterator<Record> iterator = records.iterator(); iterator.hasNext(); ) {
            Record record = iterator.next();
            String query = matchPointProcessor.prepareSolrQueryMapForMatchPoint(record, batchProfileMatchPoints);
            queryList.add(query);
            System.out.println(query);
            System.out.println("************************");
        }
        assertTrue(queryList.size() == 2);
    }


}