package org.kuali.ole.select.fixture;


import org.kuali.ole.DocumentTestUtils;
import org.kuali.ole.select.bo.OLEContentTypes;
import org.kuali.ole.select.bo.OLEFormatTypeList;
import org.kuali.ole.select.bo.OLEMaterialTypeList;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.keyvalue.OLEFormatTypeKeyValues;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kew.api.exception.WorkflowException;
import org.kuali.rice.krad.service.DocumentService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: meenau
 * Date: 5/26/14
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This fixture provides necessary values to create an EResource document.
 */

public enum OLEEResourceRecordDocumentFixture {

    ERESOURCE_ONLY_REQUIRED_FIELDS(
    "description", //title
     "1", //oleMaterialTypeId
     "1", //oleFormatTypeId
     "1" //oleContentTypeId
    ),
    ;

    public final String title;
    public final String oleMaterialTypeId;
    public final String oleFormatypeId;
    public final String oleContentTypeId;

    private OLEEResourceRecordDocumentFixture (String title, String oleMaterialTypeId, String oleFormatypeId, String oleContentTypeId ) {
        this.title = title;
        this.oleMaterialTypeId = oleMaterialTypeId;
        this.oleFormatypeId = oleFormatypeId;
        this.oleContentTypeId = oleContentTypeId;
    }

    public OLEEResourceRecordDocument createOLEEResourceDocument() {
        OLEEResourceRecordDocument oleeResourceRecordDocument = null;
        try {
            oleeResourceRecordDocument = DocumentTestUtils.createDocument(SpringContext.getBean(DocumentService.class), OLEEResourceRecordDocument.class);
        }
        catch (WorkflowException e) {
            throw new RuntimeException("Document creation failed.");
        }
        oleeResourceRecordDocument.setTitle(title);
        OLEMaterialTypeList oleMaterialType = new OLEMaterialTypeList();
        oleMaterialType.setOleMaterialTypeId(oleMaterialTypeId);
        List<OLEMaterialTypeList> oleMaterialTypeList = new ArrayList<>();
        oleMaterialTypeList.add(oleMaterialType);
        oleeResourceRecordDocument.setOleMaterialTypes(oleMaterialTypeList);
        OLEFormatTypeList oleFormatType = new OLEFormatTypeList();
        oleFormatType.setFormatTypeId(oleFormatypeId);
        List<OLEFormatTypeList> oleFormatTypeList = new ArrayList<>();
        oleFormatTypeList.add(oleFormatType);
        oleeResourceRecordDocument.setOleFormatTypes(oleFormatTypeList);
        OLEContentTypes oleContentTypes = new OLEContentTypes();
        oleContentTypes.setOleContentTypeId(oleContentTypeId);
        List<OLEContentTypes> oleContentTypesList = new ArrayList<>();
        oleContentTypesList.add(oleContentTypes);
        oleeResourceRecordDocument.setOleContentTypes(oleContentTypesList);
        return oleeResourceRecordDocument;
    }
}
