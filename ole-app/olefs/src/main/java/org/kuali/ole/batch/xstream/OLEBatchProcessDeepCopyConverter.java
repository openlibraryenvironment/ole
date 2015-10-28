package org.kuali.ole.batch.xstream;

import com.thoughtworks.xstream.XStream;
import org.kuali.ole.batch.bo.*;

import java.util.ArrayList;

/**
 * Created by sheiksalahudeenm on 10/23/15.
 */
public class OLEBatchProcessDeepCopyConverter {

    private XStream xStream;

    public XStream getxStream() {
        if(null == xStream){
            xStream = new XStream();
            xStream.alias("OLEBatchProcessProfileBo", OLEBatchProcessProfileBo.class);

            xStream.alias("oleBatchProcessProfileMappingOptionsBo", OLEBatchProcessProfileFilterCriteriaBo.class);
            xStream.omitField(OLEBatchProcessProfileFilterCriteriaBo.class, "filterId");
            xStream.omitField(OLEBatchProcessProfileFilterCriteriaBo.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileFilterCriteriaList", OLEBatchProcessProfileFilterCriteriaBo.class);

            xStream.alias("oleBatchProcessProfileMatchPoint", OLEBatchProcessProfileMatchPoint.class);
            xStream.omitField(OLEBatchProcessProfileMatchPoint.class, "matchPointId");
            xStream.omitField(OLEBatchProcessProfileMatchPoint.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileBibliographicMatchPointList", OLEBatchProcessProfileMatchPoint.class);
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileHoldingMatchPointList", OLEBatchProcessProfileMatchPoint.class);
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileItemMatchPointList", OLEBatchProcessProfileMatchPoint.class);
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileEholdingMatchPointList", OLEBatchProcessProfileMatchPoint.class);

            xStream.alias("oleBatchProcessProfileBibStatus", OLEBatchProcessProfileBibStatus.class);
            xStream.omitField(OLEBatchProcessProfileBibStatus.class, "batchProcessBibStatusId");
            xStream.omitField(OLEBatchProcessProfileBibStatus.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileBibStatusList", OLEBatchProcessProfileBibStatus.class);

            xStream.alias("oleBatchProcessProfileBibMatchPoint", OLEBatchProcessProfileBibMatchPoint.class);
            xStream.omitField(OLEBatchProcessProfileBibMatchPoint.class, "oleBibMatchPointId");
            xStream.omitField(OLEBatchProcessProfileBibMatchPoint.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileBibMatchPointList", OLEBatchProcessProfileBibMatchPoint.class);

            xStream.alias("oleBatchProcessProfileInstanceMatchPoint", OLEBatchProcessProfileInstanceMatchPoint.class);
            xStream.omitField(OLEBatchProcessProfileInstanceMatchPoint.class, "oleInstanceMatchPointId");
            xStream.omitField(OLEBatchProcessProfileInstanceMatchPoint.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileInstanceMatchPointList", OLEBatchProcessProfileInstanceMatchPoint.class);

            xStream.alias("oleBatchProcessProfileConstantsBo", OLEBatchProcessProfileConstantsBo.class);
            xStream.omitField(OLEBatchProcessProfileConstantsBo.class, "oleBatchProcessProfileConstantsId");
            xStream.omitField(OLEBatchProcessProfileConstantsBo.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileConstantsList", OLEBatchProcessProfileConstantsBo.class);

            xStream.alias("oleBatchProcessProfileMappingOptionsBo", OLEBatchProcessProfileMappingOptionsBo.class);
            xStream.omitField(OLEBatchProcessProfileMappingOptionsBo.class, "oleBatchProcessDataMapId");
            xStream.omitField(OLEBatchProcessProfileMappingOptionsBo.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileMappingOptionsList", OLEBatchProcessProfileMappingOptionsBo.class);
            xStream.alias("oleBatchProcessProfileDataMappingOptionsBo", OLEBatchProcessProfileDataMappingOptionsBo.class);
            xStream.omitField(OLEBatchProcessProfileDataMappingOptionsBo.class, "oleBatchProcessProfileDataMappingOptionId");
            xStream.omitField(OLEBatchProcessProfileDataMappingOptionsBo.class, "oleBatchProcessDataMapId");
            xStream.addImplicitCollection(OLEBatchProcessProfileMappingOptionsBo.class, "oleBatchProcessProfileDataMappingOptionsBoList", OLEBatchProcessProfileDataMappingOptionsBo.class);

            xStream.alias("oleBatchGloballyProtectedField", OLEBatchGloballyProtectedField.class);
            xStream.omitField(OLEBatchGloballyProtectedField.class, "id");
            xStream.omitField(OLEBatchGloballyProtectedField.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchGloballyProtectedFieldList", OLEBatchGloballyProtectedField.class);

            xStream.alias("oleBatchProcessProfileProtectedField", OLEBatchProcessProfileProtectedField.class);
            xStream.omitField(OLEBatchProcessProfileProtectedField.class, "oleProfileProtectedFieldId");
            xStream.omitField(OLEBatchProcessProfileProtectedField.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileProtectedFieldList", OLEBatchProcessProfileProtectedField.class);

            xStream.alias("oleBatchProcessProfileDeleteField", OLEBatchProcessProfileDeleteField.class);
            xStream.omitField(OLEBatchProcessProfileDeleteField.class, "id");
            xStream.omitField(OLEBatchProcessProfileDeleteField.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileDeleteFieldsList", OLEBatchProcessProfileDeleteField.class);

            xStream.alias("oleBatchProcessProfileRenameField", OLEBatchProcessProfileRenameField.class);
            xStream.omitField(OLEBatchProcessProfileRenameField.class, "id");
            xStream.omitField(OLEBatchProcessProfileRenameField.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileRenameFieldsList", OLEBatchProcessProfileRenameField.class);

            xStream.alias("oleBatchProcessProfileFilterCriteriaBo", OLEBatchProcessProfileFilterCriteriaBo.class);
            xStream.omitField(OLEBatchProcessProfileFilterCriteriaBo.class, "filterId");
            xStream.omitField(OLEBatchProcessProfileFilterCriteriaBo.class, "batchProcessProfileId");
            xStream.addImplicitCollection(OLEBatchProcessProfileBo.class, "oleBatchProcessProfileFilterCriteriaList", OLEBatchProcessProfileFilterCriteriaBo.class);

        }
        return xStream;
    }

    private OLEBatchProcessProfileBo fromXml(String xml){
        Object object = getxStream().fromXML(xml);
        return (OLEBatchProcessProfileBo) object;
    }

    private String toXml(OLEBatchProcessProfileBo oleBatchProcessProfileBo){
        return getxStream().toXML(oleBatchProcessProfileBo);
    }

    public void setxStream(XStream xStream) {
        this.xStream = xStream;
    }

    public OLEBatchProcessProfileBo deepCopyOfOleBatchProcess(OLEBatchProcessProfileBo oleBatchProcessProfileBo){
        String xmlContent = toXml(oleBatchProcessProfileBo);
        OLEBatchProcessProfileBo deepCopyObject = fromXml(xmlContent);
        initializeCollectionIfNull(deepCopyObject);
        return deepCopyObject;
    }

    private void initializeCollectionIfNull(OLEBatchProcessProfileBo deepCopyObject) {
        if(null == deepCopyObject.getOleBatchProcessProfileFilterCriteriaList()){
            deepCopyObject.setOleBatchProcessProfileFilterCriteriaList(new ArrayList<OLEBatchProcessProfileFilterCriteriaBo>());
        }
        if(null == deepCopyObject.getOleBatchProcessProfileMappingOptionsList()){
            deepCopyObject.setOleBatchProcessProfileMappingOptionsList(new ArrayList<OLEBatchProcessProfileMappingOptionsBo>());
        }
        if(null == deepCopyObject.getOleBatchProcessProfileDataMappingOptionsBoList()){
            deepCopyObject.setOleBatchProcessProfileDataMappingOptionsBoList(new ArrayList<OLEBatchProcessProfileDataMappingOptionsBo>());
        }
        if(null == deepCopyObject.getOleBatchGloballyProtectedFieldList()){
            deepCopyObject.setOleBatchGloballyProtectedFieldList(new ArrayList<OLEBatchGloballyProtectedField>());
        }
        if(null == deepCopyObject.getOleBatchProcessProfileProtectedFieldList()){
            deepCopyObject.setOleBatchProcessProfileProtectedFieldList(new ArrayList<OLEBatchProcessProfileProtectedField>());

        }
        if(null == deepCopyObject.getOleBatchProcessProfileConstantsList()){
            deepCopyObject.setOleBatchProcessProfileConstantsList(new ArrayList<OLEBatchProcessProfileConstantsBo>());
        }
        if(null == deepCopyObject.getOleBatchProcessProfileInstanceMatchPointList()){
            deepCopyObject.setOleBatchProcessProfileInstanceMatchPointList(new ArrayList<OLEBatchProcessProfileInstanceMatchPoint>());
        }
        if(null == deepCopyObject.getOleBatchProcessProfileBibMatchPointList()){
            deepCopyObject.setOleBatchProcessProfileBibMatchPointList(new ArrayList<OLEBatchProcessProfileBibMatchPoint>());
        }
        if(null == deepCopyObject.getOleBatchProcessProfileMatchPointList()){
            deepCopyObject.setOleBatchProcessProfileMatchPointList(new ArrayList<OLEBatchProcessProfileMatchPoint>());
        }
        if(null == deepCopyObject.getOleBatchProcessProfileBibliographicMatchPointList()){
            deepCopyObject.setOleBatchProcessProfileBibliographicMatchPointList(new ArrayList<OLEBatchProcessProfileMatchPoint>());
        }
        if(null == deepCopyObject.getOleBatchProcessProfileHoldingMatchPointList()){
            deepCopyObject.setOleBatchProcessProfileHoldingMatchPointList(new ArrayList<OLEBatchProcessProfileMatchPoint>());
        }
        if(null == deepCopyObject.getOleBatchProcessProfileItemMatchPointList()){
            deepCopyObject.setOleBatchProcessProfileItemMatchPointList(new ArrayList<OLEBatchProcessProfileMatchPoint>());
        }
        if(null == deepCopyObject.getOleBatchProcessProfileEholdingMatchPointList()){
            deepCopyObject.setOleBatchProcessProfileEholdingMatchPointList(new ArrayList<OLEBatchProcessProfileMatchPoint>());
        }
    }
}
