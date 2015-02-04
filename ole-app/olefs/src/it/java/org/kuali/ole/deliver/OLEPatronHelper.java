package org.kuali.ole.deliver;

import org.kuali.ole.deliver.bo.*;
import org.kuali.rice.kim.impl.identity.address.EntityAddressBo;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 9/19/13
 * Time: 10:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEPatronHelper {
    private EntityBo entityBo =new EntityBo();
    private EntityNameBo entityNameBo=new EntityNameBo();
    private EntityAddressBo entityAddressBo=new EntityAddressBo();
    private EntityTypeContactInfoBo entityTypeContactInfoBo=new EntityTypeContactInfoBo();
    private EntityEmailBo entityEmailBo=new EntityEmailBo();
    private EntityPhoneBo entityPhoneBo=new EntityPhoneBo();
    public static int idValue=2001;

    public OlePatronDocument changeId(OlePatronDocument olePatronDocument){
       /* private EntityBo entityBo =new EntityBo();
        private EntityNameBo entityNameBo=new EntityNameBo();
        private EntityAddressBo entityAddressBo=new EntityAddressBo();
        private EntityTypeContactInfoBo entityTypeContactInfoBo=new EntityTypeContactInfoBo();
        private EntityEmailBo entityEmailBo=new EntityEmailBo();
        private EntityPhoneBo entityPhoneBo=new EntityPhoneBo();*/
        idValue=idValue+1;
        int id=idValue;
        List<EntityAddressBo> addressList=new ArrayList<>();
        List<EntityEmailBo> emailList=new ArrayList<>();
        List<EntityPhoneBo> phoneList=new ArrayList<>();
        List<EntityPhoneBo> contactPhoneList=new ArrayList<>();
        List<EntityTypeContactInfoBo> contactList=new ArrayList<>();
        olePatronDocument.setOlePatronId(String.valueOf(id));
        olePatronDocument.getEntity().setId(String.valueOf(id));
        for(EntityTypeContactInfoBo entityTypeContactInfoBo1:olePatronDocument.getEntity().getEntityTypeContactInfos()){
            entityTypeContactInfoBo=entityTypeContactInfoBo1;
            entityTypeContactInfoBo.setEntityId(String.valueOf(id));
            contactList.add(entityTypeContactInfoBo);
        }
        olePatronDocument.getEntity().setEntityTypeContactInfos(contactList);
        for(EntityAddressBo entityAddressBo1:olePatronDocument.getAddresses()){
            entityAddressBo=entityAddressBo1;
            entityAddressBo.setId(String.valueOf(id));
            entityAddressBo.setEntityId(String.valueOf(id));
            addressList.add(entityAddressBo);
        }
        for(EntityEmailBo entityEmailBo1:olePatronDocument.getEmails()){
            entityEmailBo=entityEmailBo1;
            entityEmailBo.setEntityId(String.valueOf(id));
            entityEmailBo.setId(String.valueOf(id));
            emailList.add(entityEmailBo);
        }
        olePatronDocument.getName().setId(String.valueOf(id));
        for(EntityPhoneBo entityPhoneBo1:olePatronDocument.getPhones()){
            entityPhoneBo=entityPhoneBo1;
            entityPhoneBo.setId(String.valueOf(id));
            entityPhoneBo.setEntityId(String.valueOf(id));
            phoneList.add(entityPhoneBo);
        }

        List<OlePatronNotes> olePatronNotesList=new ArrayList<>();
        for(OlePatronNotes OlePatronNotes:olePatronDocument.getNotes()){
            OlePatronNotes.setPatronNoteId(String.valueOf(id));
            OlePatronNotes.setOlePatronId(String.valueOf(id));
            olePatronNotesList.add(OlePatronNotes);
        }
        List<OlePatronLostBarcode> olePatronLostBarcodeList=new ArrayList<>();
        for(OlePatronLostBarcode olePatronLostBarcode:olePatronDocument.getLostBarcodes()){
            olePatronLostBarcode.setOlePatronLostBarcodeId(String.valueOf(id));
            olePatronLostBarcode.setOlePatronId(String.valueOf(id));
            olePatronLostBarcodeList.add(olePatronLostBarcode);
        }

        olePatronDocument.getOleBorrowerType().setBorrowerTypeId(String.valueOf(id));

        List<OlePatronAffiliation> olePatronAffiliationList=new ArrayList<>();
        for(OlePatronAffiliation olePatronAffiliation:olePatronDocument.getPatronAffiliations()){
            olePatronAffiliation.setEntityAffiliationId(String.valueOf(id));
            olePatronAffiliation.setEntityId(String.valueOf(id));
            olePatronAffiliationList.add(olePatronAffiliation);
        }
        List<OleProxyPatronDocument> oleProxyPatronDocumentList=new ArrayList<>();
        for(OleProxyPatronDocument oleProxyPatronDocument:olePatronDocument.getOleProxyPatronDocumentList()){
            oleProxyPatronDocument.setProxyPatronId(String.valueOf(id));
            oleProxyPatronDocument.setOleProxyPatronDocumentId(String.valueOf(id));
            oleProxyPatronDocument.setOlePatronId(String.valueOf(id));
            oleProxyPatronDocumentList.add(oleProxyPatronDocument);
        }
        List<OlePatronLocalIdentificationBo> olePatronLocalIdentificationBoList=new ArrayList<>();
        for(OlePatronLocalIdentificationBo olePatronLocalIdentificationBo:olePatronDocument.getOlePatronLocalIds()){
            olePatronLocalIdentificationBo.setPatronLocalSeqId(String.valueOf(id));
            olePatronLocalIdentificationBo.setLocalId(String.valueOf(id));
            olePatronLocalIdentificationBo.setOlePatronId(String.valueOf(id));
            olePatronLocalIdentificationBoList.add(olePatronLocalIdentificationBo);
        }
        List<OleAddressBo> oleAddressBoList=new ArrayList<>();
        for(OleAddressBo oleAddressBo:olePatronDocument.getOleAddresses()){
            oleAddressBo.setOleAddressId(String.valueOf(id));
            oleAddressBo.setOlePatronId(String.valueOf(id));
            oleAddressBoList.add(oleAddressBo);
        }

        /*private List<OlePatronLostBarcode> lostBarcodes = new ArrayList<OlePatronLostBarcode>();
        private OleBorrowerType oleBorrowerType;
        private EntityBo entity = new EntityBo();
        private OleSourceBo sourceBo;
        private OleStatisticalCategoryBo statisticalCategoryBo;
        private List<OlePatronAffiliation> patronAffiliations = new ArrayList<OlePatronAffiliation>();
        private List<EntityEmploymentBo> employments = new ArrayList<EntityEmploymentBo>();
        private List<OleDeliverRequestBo> oleDeliverRequestBos = new ArrayList<OleDeliverRequestBo>();
        private List<OleProxyPatronDocument> oleProxyPatronDocuments = new ArrayList<OleProxyPatronDocument>();
        private List<OleTemporaryCirculationHistory> oleTemporaryCirculationHistoryRecords = new ArrayList<OleTemporaryCirculationHistory>();
        private List<OlePatronLocalIdentificationBo> olePatronLocalIds = new ArrayList<OlePatronLocalIdentificationBo>();
        private List<OleProxyPatronDocument> oleProxyPatronDocumentList = new ArrayList<OleProxyPatronDocument>();*/


        olePatronDocument.setAddresses(addressList);
        olePatronDocument.setEmails(emailList);
        olePatronDocument.setPhones(phoneList);
        olePatronDocument.setNotes(olePatronNotesList);
        olePatronDocument.setLostBarcodes(olePatronLostBarcodeList);
        olePatronDocument.setPatronAffiliations(olePatronAffiliationList);
        olePatronDocument.setOleProxyPatronDocuments(oleProxyPatronDocumentList);
        olePatronDocument.setOlePatronLocalIds(olePatronLocalIdentificationBoList);
        olePatronDocument.setOleAddresses(oleAddressBoList);
        return olePatronDocument;
    }
}
