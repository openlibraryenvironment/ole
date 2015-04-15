package org.kuali.ole.deliver;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.kuali.ole.OLEConstants;
import org.kuali.ole.OLETestCaseBase;
import org.kuali.ole.deliver.bo.OLEPatronEntityViewBo;
import org.kuali.ole.deliver.bo.OlePatronDocument;
import org.kuali.ole.deliver.processor.LoanProcessor;
import org.kuali.ole.sys.context.SpringContext;
import org.kuali.rice.kim.api.identity.email.EntityEmail;
import org.kuali.rice.kim.impl.identity.email.EntityEmailBo;
import org.kuali.rice.kim.impl.identity.entity.EntityBo;
import org.kuali.rice.kim.impl.identity.name.EntityNameBo;
import org.kuali.rice.kim.impl.identity.phone.EntityPhoneBo;
import org.kuali.rice.kim.impl.identity.type.EntityTypeContactInfoBo;
import org.kuali.rice.krad.service.KRADServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sheiksalahudeenm on 15/4/15.
 */
public class OLEPatron_IT extends OLETestCaseBase {
    Logger LOG = Logger.getLogger(OLEPatron_IT.class);

   /* @Autowired
    LoanProcessor loanProcessor;*/

    @Test
    public void testPatronFetchingTime(){
        LOG.info("Inside testPatronFetchingTime");
        LoanProcessor loanProcessor = (LoanProcessor) SpringContext.getService("loanProcessor");

        LOG.info("************************* Starting ************************************");
        Map patronMap = new HashMap();
        patronMap.put("barcode", "2825810");
        Long patronFetchStartingTime = System.currentTimeMillis();
        List<OlePatronDocument> olePatronDocumentList = (List<OlePatronDocument>) KRADServiceLocator.getBusinessObjectService().findMatching(OlePatronDocument.class,patronMap);
        Long patronFetchEndTime = System.currentTimeMillis();
        Long patronFetchTotalTime = patronFetchEndTime-patronFetchStartingTime;
        LOG.info("The Time Taken for patron fetch : "+patronFetchTotalTime);
        if(CollectionUtils.isNotEmpty(olePatronDocumentList)){
            Long entityFetchStartingTime = System.currentTimeMillis();
            OlePatronDocument patronBo = (OlePatronDocument) olePatronDocumentList.get(0);
            EntityBo entityBo =patronBo.getEntity();
            Long entityFetchEndTime = System.currentTimeMillis();
            Long entityFetchTotalTime = entityFetchEndTime-entityFetchStartingTime;
            LOG.info("The Time Taken for Entity fetch : "+entityFetchTotalTime);
            Long nameFetchStartingTime = System.currentTimeMillis();
            List<EntityNameBo> entityNameBoList = entityBo.getNames();
            if(CollectionUtils.isNotEmpty(entityNameBoList)){
                for(EntityNameBo entityNameBo:entityNameBoList){
                    if(entityNameBo.getDefaultValue()){
                        patronBo.setFirstName(entityNameBo.getFirstName());
                        patronBo.setMiddleName(entityNameBo.getMiddleName());
                        patronBo.setLastName(entityNameBo.getLastName());
                    }
                }
            }
            Long nameFetchEndTime = System.currentTimeMillis();
            Long nameFetchTotalTime = nameFetchEndTime-nameFetchStartingTime;
            LOG.info("The Time Taken for Name fetch : "+nameFetchTotalTime);

            if(CollectionUtils.isNotEmpty(entityBo.getEntityTypeContactInfos())){
                Long contactInfoFetchStartingTime = System.currentTimeMillis();
                List<EntityTypeContactInfoBo> entityTypeContactInfoBoList = entityBo.getEntityTypeContactInfos();
                Long contactInfoFetchEndTime = System.currentTimeMillis();
                Long contactInfoFetchTotalTime = contactInfoFetchEndTime-contactInfoFetchStartingTime;
                LOG.info("The Time Taken for Contact Info fetch : "+contactInfoFetchTotalTime);
                for(EntityTypeContactInfoBo entityTypeContactInfoBo : entityTypeContactInfoBoList){
                    Long emailFetchStartingTime = System.currentTimeMillis();
                    List<EntityEmailBo> entityEmailList = entityTypeContactInfoBo.getEmailAddresses();
                    if(CollectionUtils.isNotEmpty(entityEmailList)){
                        for(EntityEmailBo entityEmailBo: entityEmailList){
                            if(entityEmailBo.getDefaultValue()){
                                patronBo.setEmailAddress(entityEmailBo.getEmailAddress());
                            }
                        }
                    }
                    Long emailFetchEndTime = System.currentTimeMillis();
                    Long emailFetchTotalTime = emailFetchEndTime-emailFetchStartingTime;
                    LOG.info("The Time Taken for email fetch : "+emailFetchTotalTime);
                    Long phoneFetchStartingTime = System.currentTimeMillis();
                    List<EntityPhoneBo> entityPhoneBos = entityTypeContactInfoBo.getPhoneNumbers();
                    if(CollectionUtils.isNotEmpty(entityPhoneBos)){
                        for(EntityPhoneBo entityPhoneBo: entityPhoneBos){
                            if(entityPhoneBo.getDefaultValue()){
                                patronBo.setPhoneNumber(entityPhoneBo.getPhoneNumber());
                            }
                        }
                    }
                    Long phoneFetchEndTime = System.currentTimeMillis();
                    Long phoneFetchTotalTime = phoneFetchEndTime-phoneFetchStartingTime;
                    LOG.info("The Time Taken for Phone fetch : "+phoneFetchTotalTime);
                }
            }

        }

        LOG.info("************************* End ************************************");


    }
}
