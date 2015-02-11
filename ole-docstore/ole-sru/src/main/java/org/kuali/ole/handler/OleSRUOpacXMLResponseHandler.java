package org.kuali.ole.handler;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.kuali.ole.OleSRUConstants;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostic;
import org.kuali.ole.bo.diagnostics.OleSRUDiagnostics;
import org.kuali.ole.bo.serachRetrieve.*;
import org.kuali.ole.converters.OleSRUCirculationDocumentConverter;
import org.kuali.ole.docstore.common.document.content.bib.marc.*;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.ControlFieldConverter;
import org.kuali.ole.docstore.common.document.content.bib.marc.xstream.DataFieldConverter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Srinivasan
 * Date: 7/16/12
 * Time: 7:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OleSRUOpacXMLResponseHandler {

    /**
     * this method converts xml to  OleSRUResponseDocuments object
     *
     * @param fileContent
     * @return OleSRUResponseDocuments object
     * @throws URISyntaxException
     * @throws IOException
     */
    public OleSRUResponseDocuments fromXML(String fileContent) throws URISyntaxException, IOException {

        XStream xStream = new XStream();
        xStream.alias("opacRecords", OleSRUResponseDocuments.class);
        xStream.alias("opacRecord", OleSRUResponseDocument.class);
        xStream.alias("holding", OleSRUInstanceDocument.class);
        xStream.alias("circulation", OleSRUCirculationDocument.class);
        xStream.alias("volume", OleSRUInstanceVolume.class);
        xStream.addImplicitCollection(OleSRUResponseDocuments.class, "opacRecords");
        Object object = xStream.fromXML(fileContent);
        return (OleSRUResponseDocuments) object;
    }

    /**
     * this method converts OleSRUResponseDocuments object to xml string   </>
     *
     * @param oleSRUSearchRetrieveResponse object
     * @return xml as a String
     */
    public String toXML(OleSRUSearchRetrieveResponse oleSRUSearchRetrieveResponse, String recordSchema) {

        List<OleSRUResponseRecord> oleSRUResponseRecords = new ArrayList<>();
        if(oleSRUSearchRetrieveResponse.getOleSRUResponseRecords()!=null && CollectionUtils.isEmpty(oleSRUSearchRetrieveResponse.getOleSRUResponseRecords().getOleSRUResponseRecordList())){
            oleSRUResponseRecords=oleSRUSearchRetrieveResponse.getOleSRUResponseRecords().getOleSRUResponseRecordList();
        }
        if (CollectionUtils.isNotEmpty(oleSRUResponseRecords)) {
            for (OleSRUResponseRecord oleSRUResponseRecord : oleSRUResponseRecords) {
                OleSRUResponseDocument oleSRUResponseDocument = oleSRUResponseRecord.getOleSRUResponseDocument();
                OleSRUResponseRecordData oleSRUResponseRecordData = oleSRUResponseDocument.getOleSRUResponseRecordData();
                List<OleSRUInstanceDocument> oleSRUInstanceDocuments = oleSRUResponseRecordData.getHoldings();
                if (CollectionUtils.isNotEmpty(oleSRUInstanceDocuments)) {
                    for (OleSRUInstanceDocument oleSRUInstanceDocument : oleSRUInstanceDocuments) {
                        convertToEscape(oleSRUInstanceDocument);
                        if (CollectionUtils.isNotEmpty(oleSRUInstanceDocument.getVolumes())) {
                            for (OleSRUInstanceVolume oleSRUInstanceVolume : oleSRUInstanceDocument.getVolumes()) {
                                convertToEscape(oleSRUInstanceVolume);
                            }
                        }
                        if (CollectionUtils.isNotEmpty(oleSRUInstanceDocument.getCirculations())) {
                            for (OleSRUCirculationDocument oleSRUCirculationDocument : oleSRUInstanceDocument.getCirculations()) {
                                convertToEscape(oleSRUCirculationDocument);
                            }
                        }

                    }
                }
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        XStream xStream = new XStream();
        xStream.registerConverter(new OleSRUCirculationDocumentConverter());
        //xStream.registerConverter(new OleSRUResponseRecordDataConverter());
        xStream.alias("zs:searchRetrieveResponse", OleSRUSearchRetrieveResponse.class);
        xStream.aliasField("zs:version", OleSRUSearchRetrieveResponse.class, "version");
        xStream.aliasField("zs:numberOfRecords", OleSRUSearchRetrieveResponse.class, "numberOfRecords");
        xStream.aliasField("zs:records", OleSRUSearchRetrieveResponse.class, "oleSRUResponseRecords");
        xStream.aliasField("zs:diagnostics", OleSRUSearchRetrieveResponse.class, "oleSRUDiagnostics");
        xStream.addImplicitCollection(OleSRUResponseRecords.class, "oleSRUResponseRecordList");
        xStream.addImplicitCollection(OleSRUDiagnostics.class, "oleSRUDiagnosticList");
        xStream.alias("diagnostic", OleSRUDiagnostic.class);
        xStream.alias("zs:record", OleSRUResponseRecord.class);
        xStream.aliasField("zs:recordSchema", OleSRUResponseRecord.class, "recordSchema");
        xStream.aliasField("zs:recordPacking", OleSRUResponseRecord.class, "recordPacking");
        xStream.aliasField("zs:recordPosition", OleSRUResponseRecord.class, "recordPosition");
        xStream.alias("holding", OleSRUInstanceDocument.class);
        xStream.alias("circulation", OleSRUCirculationDocument.class);
        xStream.alias("volume", OleSRUInstanceVolume.class);
        xStream.aliasField("bibliographicRecord",OleSRUResponseRecordData.class,"bibMarcRecords");
        xStream.alias("record", BibMarcRecord.class);
        xStream.alias("controlfield", ControlField.class);
        xStream.alias("datafield", DataField.class);
        xStream.alias("subfield", SubField.class);
        xStream.addImplicitCollection(BibMarcRecord.class, "dataFields", DataField.class);
        xStream.addImplicitCollection(BibMarcRecord.class, "controlFields", ControlField.class);
        xStream.addImplicitCollection(BibMarcRecords.class, "records");
        xStream.registerConverter(new DataFieldConverter());
        xStream.registerConverter(new ControlFieldConverter());
        xStream.omitField(OleSRUResponseRecordData.class,"bibliographicRecord");

        if (recordSchema != null && recordSchema.equalsIgnoreCase("OPAC")) {
            xStream.aliasField("opacRecord", OleSRUResponseDocument.class, "oleSRUResponseRecordData");
            String xml = xStream.toXML(oleSRUSearchRetrieveResponse);
            xml = xml.replace("<zs:searchRetrieveResponse>", "<zs:searchRetrieveResponse xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:zs=\"http://www.loc.gov/zing/srw/\">");
            xml = xml.replace("<diagnostic>", "<diagnostic xmlns=\"http://www.loc.gov/zing/srw/diagnostic/\">");
            xml = xml.replace("<oleSRUResponseDocument>", "<zs:recordData>");
            xml = xml.replace("</oleSRUResponseDocument>", "</zs:recordData>");
            stringBuffer.append(xml);
        } else {
            xStream.alias("zs:searchRetrieveResponse", OleSRUSearchRetrieveResponse.class);
            xStream.aliasField("zs:recordData", OleSRUResponseDocument.class, "oleSRUResponseRecordData");
            String xml = xStream.toXML(oleSRUSearchRetrieveResponse);
            xml = xml.replace("<zs:searchRetrieveResponse>", "<zs:searchRetrieveResponse xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:zs=\"http://www.loc.gov/zing/srw/\">");
            xml = xml.replace("<diagnostic>", "<diagnostic xmlns=\"http://www.loc.gov/zing/srw/diagnostic/\">");
            xml = xml.replace("<oleSRUResponseDocument>", "");
            xml = xml.replace("<bibliographicRecord>", "");
            xml = xml.replace("</oleSRUResponseDocument>", "");
            xml = xml.replace("</bibliographicRecord>", "");
            stringBuffer.append(xml);
        }
        return stringBuffer.toString();
    }

    public void convertToEscape(Object obj) {
        Class clazz=obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            boolean isSetter= Modifier.isPublic(method.getModifiers()) &&
                    method.getReturnType()!=null && StringUtils.isNotEmpty(method.getReturnType().getName()) && method.getReturnType().getName().equalsIgnoreCase("void") &&
                    method.getName().matches("^set[A-Z].*");
            Class<?>[] parameterTypes=method.getParameterTypes();

            if (isSetter) {
                if (parameterTypes.length == 1 && parameterTypes[0].getName().equalsIgnoreCase("java.lang.String")) {
                    try {
                        String value = (String) method.getDefaultValue();
                        if(StringUtils.isNotBlank(value)){
                            value = StringEscapeUtils.unescapeXml(value);
                            method.invoke(obj, value);
                        }

                    } catch (Exception e) {

                    }
                }
            }

        }
    }
}
