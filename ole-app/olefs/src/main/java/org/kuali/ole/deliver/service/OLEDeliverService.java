package org.kuali.ole.deliver.service;

import org.apache.commons.collections.CollectionUtils;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;

import java.util.List;

/**
 * Created by sheiksalahudeenm on 17/4/15.
 */
public class OLEDeliverService {

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(OLEDeliverService.class);

    public static OlePatronDocument populatePatronName(OlePatronDocument olePatronDocument){
        if(olePatronDocument != null){
            try {
                EntityBo entityBo =olePatronDocument.getEntity();
                List<EntityNameBo> entityNameBoList = entityBo.getNames();
                if(CollectionUtils.isNotEmpty(entityNameBoList)){
                    for(EntityNameBo entityNameBo:entityNameBoList){
                        if(entityNameBo.getDefaultValue()){
                            olePatronDocument.setFirstName(entityNameBo.getFirstName());
                            olePatronDocument.setMiddleName(entityNameBo.getMiddleName());
                            olePatronDocument.setLastName(entityNameBo.getLastName());
                            olePatronDocument.setPatronName(entityNameBo.getLastName() + ", " + entityNameBo.getFirstName());
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Error occurred while populating patron names (patron Id -"+olePatronDocument.getOlePatronId()+"):"+e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return olePatronDocument;
    }

    public static OlePatronDocument populatePatronEmailAndPhone(OlePatronDocument olePatronDocument){
        if(olePatronDocument != null){
            try {
                EntityBo entityBo =olePatronDocument.getEntity();
                if(CollectionUtils.isNotEmpty(entityBo.getEntityTypeContactInfos())){
                    List<EntityTypeContactInfoBo> entityTypeContactInfoBoList = entityBo.getEntityTypeContactInfos();
                    for(EntityTypeContactInfoBo entityTypeContactInfoBo : entityTypeContactInfoBoList){
                        List<EntityEmailBo> entityEmailList = entityTypeContactInfoBo.getEmailAddresses();
                        if(CollectionUtils.isNotEmpty(entityEmailList)){
                            for(EntityEmailBo entityEmailBo: entityEmailList){
                                if(entityEmailBo.getDefaultValue()){
                                    olePatronDocument.setEmailAddress(entityEmailBo.getEmailAddress());
                                }
                            }
                        }
                        List<EntityPhoneBo> entityPhoneBos = entityTypeContactInfoBo.getPhoneNumbers();
                        if(CollectionUtils.isNotEmpty(entityPhoneBos)){
                            for(EntityPhoneBo entityPhoneBo: entityPhoneBos){
                                if(entityPhoneBo.getDefaultValue()){
                                    olePatronDocument.setPhoneNumber(entityPhoneBo.getPhoneNumber());
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                LOG.error("Error occurred while populating patron names (patron Id -"+olePatronDocument.getOlePatronId()+"):"+e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return olePatronDocument;
    }
}
