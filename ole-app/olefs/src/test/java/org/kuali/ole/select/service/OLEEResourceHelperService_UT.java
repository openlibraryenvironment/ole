package org.kuali.ole.select.service;

import org.junit.Assert;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.select.bo.OLEGOKbPackage;
import org.kuali.ole.select.document.OLEEResourceRecordDocument;
import org.kuali.ole.select.form.OLEEResourceRecordForm;
import org.kuali.ole.select.gokb.OleGokbPackage;
import org.kuali.ole.select.gokb.OleGokbTipp;
import org.kuali.ole.service.OLEEResourceHelperService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maheswarang on 5/14/15.
 */
public class OLEEResourceHelperService_UT extends OLETestCaseBase {

    @Test
    public void searchGokbForPackagesTest(){
        OleGokbTipp oleGokbTipp = new OleGokbTipp();
        oleGokbTipp.setGokbTippId(1);
        oleGokbTipp.setGokbPackageId(1);
        oleGokbTipp.setGokbTitleId(1);
        oleGokbTipp.setGokbPlatformId(1);
        OleGokbPackage oleGokbPackage = new OleGokbPackage();
        oleGokbPackage.setGokbPackageId(1);
        oleGokbPackage.setStatus("Complete");
        oleGokbTipp.setOleGokbPackage(oleGokbPackage);

        OleGokbTipp oleGokbTipp2 = new OleGokbTipp();
        oleGokbTipp2.setGokbTippId(2);
        oleGokbTipp2.setGokbPackageId(2);
        oleGokbTipp2.setGokbTitleId(2);
        oleGokbTipp2.setGokbPlatformId(1);
        OleGokbPackage oleGokbPackage2 = new OleGokbPackage();
        oleGokbPackage2.setGokbPackageId(2);
        oleGokbPackage2.setStatus("Complete");
        oleGokbTipp2.setOleGokbPackage(oleGokbPackage2);


        OleGokbTipp oleGokbTipp3 = new OleGokbTipp();
        oleGokbTipp3.setGokbTippId(3);
        oleGokbTipp3.setGokbPlatformId(2);
        oleGokbTipp3.setGokbPackageId(2);
        oleGokbTipp3.setGokbTitleId(3);
        oleGokbTipp3.setOleGokbPackage(oleGokbPackage2);

        List<OleGokbTipp> oleGokbTipps = new ArrayList<OleGokbTipp>();
        oleGokbTipps.add(oleGokbTipp);
        oleGokbTipps.add(oleGokbTipp2);
        oleGokbTipps.add(oleGokbTipp3);

        OLEEResourceHelperService oleeResourceHelperService = new OLEEResourceHelperService();
        OLEEResourceRecordForm oleeResourceRecordForm = new OLEEResourceRecordForm();
        OLEEResourceRecordDocument oleeResourceRecordDocument = new OLEEResourceRecordDocument();
        oleeResourceRecordForm.setDocument(oleeResourceRecordDocument);
        List<OLEGOKbPackage> olegoKbPackages = oleeResourceHelperService.searchGokbForPackagess(oleGokbTipps, oleeResourceRecordForm);
        System.out.println("No of Packages : " + olegoKbPackages.size());
        Assert.assertTrue("Should Return two packages",olegoKbPackages.size()==2);


    }
}
