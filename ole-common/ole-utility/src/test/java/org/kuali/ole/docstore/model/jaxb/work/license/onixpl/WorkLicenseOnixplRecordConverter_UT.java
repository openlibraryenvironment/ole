package org.kuali.ole.docstore.model.jaxb.work.license.onixpl;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.kuali.ole.docstore.model.xmlpojo.work.license.onixpl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Pranitha
 * Date: 5/30/12
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLicenseOnixplRecordConverter_UT {

    public static final Logger LOG = LoggerFactory.getLogger(WorkLicenseOnixplRecordConverter_UT.class);

    @Test
    public void unmarshal() {
        WorkLicenseOnixplRecordConverter jaxbUnmarshal = new WorkLicenseOnixplRecordConverter();
        URL resource = getClass().getResource("/org/kuali/ole/license.onixsample.xml");
        try {
            File file = new File(resource.toURI());
            String OnixXML = FileUtils.readFileToString(file);
            LOG.info(OnixXML);
            ONIXPublicationsLicenseMessage onixElement = jaxbUnmarshal.unmarshal(OnixXML);
            List<org.kuali.ole.docstore.model.xmlpojo.work.license.onixpl.PublicationsLicenseExpression.LicenseDocumentText>
                    licenseDocumentText = onixElement.getPublicationsLicenseExpression().get(0).getLicenseDocumentText();
            LOG.info("DocumentLabel..." + licenseDocumentText.get(0).getDocumentLabel().getValue());
            String DocumentLable = licenseDocumentText.get(0).getDocumentLabel().getValue();
            assertEquals(DocumentLable, "DocumentLabel");
            org.kuali.ole.docstore.model.xmlpojo.work.license.onixpl.PublicationsLicenseExpression.LicenseDetail
                    licenseDetail = onixElement.getPublicationsLicenseExpression().get(0).getLicenseDetail();
            LOG.info("licenseIdentifier......." + licenseDetail.getLicenseIdentifier());
            List<org.kuali.ole.docstore.model.xmlpojo.work.license.onixpl.PublicationsLicenseExpression.
                    LicenseDetail.LicenseIdentifier> licenseIdentifierList
                    = licenseDetail.getLicenseIdentifier();
            LOG.info("License Identifier Value......" + licenseIdentifierList.get(0).getIDValue().getValue());
            String licenseIdentifierValue = licenseIdentifierList.get(0).getIDValue().getValue();
            assertEquals(licenseIdentifierValue, "LicenseIdentifierIDValue");

            PartyName addresseeName = (PartyName) onixElement.getHeader().getAddressee().get(0).getAddresseeName();
            LOG.info("addressee namr is...." + addresseeName.getValue());
            String addresseeNameValue = addresseeName.getValue();
            assertEquals(addresseeNameValue, "AddresseeName2");

            PartyName senderName = (PartyName) onixElement.getHeader().getSender().getSenderName();
            LOG.info("Sender name is...." + senderName.getValue());
            String senderNameValue = senderName.getValue();
            assertEquals(senderNameValue, "SenderName2");
            LOG.info(onixElement.getDatestamp());
            LOG.info(onixElement.getVersion());
            ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.AddresseeIDType
                    addresseeIDType = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getAddresseeIDType();
            ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDTypeName
                    idTypeName = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDTypeName();
            ONIXPublicationsLicenseMessage.Header.Addressee.AddresseeIdentifier.IDValue
                    idValue = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDValue();
            String addresseeIdentifierDateStamp = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getDatestamp();
            String addresseeIdentifierSourceName = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getSourcename();
            String addresseeIdentifierSourceType = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getSourcetype();
            String addresseContactValue = onixElement.getHeader().getAddressee().get(0).getAddresseeContact().getValue();
            String addresseContactDateStamp = onixElement.getHeader().getAddressee().get(0).getAddresseeContact().getDatestamp();
            String addresseContactSourceName = onixElement.getHeader().getAddressee().get(0).getAddresseeContact().getSourcename();
            String addresseContactSourceType = onixElement.getHeader().getAddressee().get(0).getAddresseeContact().getSourcetype();
            String addresseDateStamp = onixElement.getHeader().getAddressee().get(0).getDatestamp();
            String addresseSourceType = onixElement.getHeader().getAddressee().get(0).getSourcename();
            String addresseSourceName = onixElement.getHeader().getAddressee().get(0).getSourcetype();
            String addresseEmailValue = onixElement.getHeader().getAddressee().get(0).getAddresseeEmail().getValue();
            String addresseEmailDateStamp = onixElement.getHeader().getAddressee().get(0).getAddresseeEmail().getDatestamp();
            String addresseEmailSourceName = onixElement.getHeader().getAddressee().get(0).getAddresseeEmail().getSourcename();
            String addresseEmailSourceType = onixElement.getHeader().getAddressee().get(0).getAddresseeEmail().getSourcetype();
            String messageNumberDateStamp = onixElement.getHeader().getMessageNumber().getDatestamp();
            String messageNumberSourceName = onixElement.getHeader().getMessageNumber().getSourcename();
            String messageNumberSourceType = onixElement.getHeader().getMessageNumber().getSourcetype();
            String messageNumberValue = onixElement.getHeader().getMessageNumber().getValue();
            String messageRepeatDateStamp = onixElement.getHeader().getMessageRepeat().getDatestamp();
            String messageRepeatSourceName = onixElement.getHeader().getMessageRepeat().getSourcename();
            String messageRepeatSourceType = onixElement.getHeader().getMessageRepeat().getSourcetype();
            int messageRepeatValue = onixElement.getHeader().getMessageRepeat().getValue();
            String sentDateTimeDateStamp = onixElement.getHeader().getSentDateTime().getDatestamp();
            String sentDateTimeSourceName = onixElement.getHeader().getSentDateTime().getSourcename();
            String sentDateTimeSourceType = onixElement.getHeader().getSentDateTime().getSourcetype();
            String sentDateTimeValue = onixElement.getHeader().getSentDateTime().getValue();
            String messageNoteDateStamp = onixElement.getHeader().getMessageNote().getDatestamp();
            String messageNoteSourceName = onixElement.getHeader().getMessageNote().getSourcename();
            String messageNoteSourceType = onixElement.getHeader().getMessageNote().getSourcetype();
            String messageNoteValue = onixElement.getHeader().getMessageNote().getValue();
            String headerDateStamp = onixElement.getHeader().getDatestamp();
            String headerSourceName = onixElement.getHeader().getSourcename();
            String headerSourceType = onixElement.getHeader().getSourcetype();
            String publicationLisenceMessageDateStamp = onixElement.getDatestamp();
            String publicationLisenceMessageSourceName = onixElement.getSourcename();
            String publicationLisenceMessageSourceType = onixElement.getSourcetype();
            String expressionDetailDateStamp = onixElement.getPublicationsLicenseExpression().get(0).getExpressionDetail().getDatestamp();
            String expressionDetailSourceName = onixElement.getPublicationsLicenseExpression().get(0).getExpressionDetail().getSourcename();
            String expressionDetailSourceType = onixElement.getPublicationsLicenseExpression().get(0).getExpressionDetail().getSourcetype();
            String definitionDateStamp = onixElement.getPublicationsLicenseExpression().get(0).getDefinitions().getDatestamp();
            String definitionSourceName = onixElement.getPublicationsLicenseExpression().get(0).getDefinitions().getSourcename();
            String definitionSourceType = onixElement.getPublicationsLicenseExpression().get(0).getDefinitions().getSourcetype();
            String lisenceGrantDateStamp = onixElement.getPublicationsLicenseExpression().get(0).getLicenseGrant().getDatestamp();
            String lisenceGrantSourceName = onixElement.getPublicationsLicenseExpression().get(0).getLicenseGrant().getSourcename();
            String lisenceGrantSourceType = onixElement.getPublicationsLicenseExpression().get(0).getLicenseGrant().getSourcetype();
            String usageTermsDateStamp = onixElement.getPublicationsLicenseExpression().get(0).getUsageTerms().getDatestamp();
            String usageTermsSourceName = onixElement.getPublicationsLicenseExpression().get(0).getUsageTerms().getSourcename();
            String usageTermsSourceType = onixElement.getPublicationsLicenseExpression().get(0).getUsageTerms().getSourcetype();
            String supplyTermsDateStamp = onixElement.getPublicationsLicenseExpression().get(0).getSupplyTerms().getDatestamp();
            String supplyTermsSourceName = onixElement.getPublicationsLicenseExpression().get(0).getSupplyTerms().getSourcename();
            String supplyTermsSourceType = onixElement.getPublicationsLicenseExpression().get(0).getSupplyTerms().getSourcetype();
            String continuigAccessTermsDateStamp = onixElement.getPublicationsLicenseExpression().get(0).getContinuingAccessTerms().getDatestamp();
            String continuigAccessTermsSourceName = onixElement.getPublicationsLicenseExpression().get(0).getContinuingAccessTerms().getSourcename();
            String continuigAccessTermsSourceType = onixElement.getPublicationsLicenseExpression().get(0).getContinuingAccessTerms().getSourcetype();
            String paymentTermsDateStamp = onixElement.getPublicationsLicenseExpression().get(0).getPaymentTerms().getDatestamp();
            String paymentTermsSourceName = onixElement.getPublicationsLicenseExpression().get(0).getPaymentTerms().getSourcename();
            String paymentTermsSourceType = onixElement.getPublicationsLicenseExpression().get(0).getPaymentTerms().getSourcetype();
            String generalTermsDateStamp = onixElement.getPublicationsLicenseExpression().get(0).getGeneralTerms().getDatestamp();
            String generalTermsSourceName = onixElement.getPublicationsLicenseExpression().get(0).getGeneralTerms().getSourcename();
            String generalTermsSourceType = onixElement.getPublicationsLicenseExpression().get(0).getGeneralTerms().getSourcetype();
            String publicationsLicenseExpressionDateStamp = onixElement.getPublicationsLicenseExpression().get(0).getDatestamp();
            String publicationsLicenseExpressionSourceName = onixElement.getPublicationsLicenseExpression().get(0).getSourcename();
            String publicationsLicenseExpressionSourceType = onixElement.getPublicationsLicenseExpression().get(0).getSourcetype();
            String publicationsLicenseExpressionVersion = onixElement.getPublicationsLicenseExpression().get(0).getVersion();
            String idValueDateStamp = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDValue().getDatestamp();
            String idValueSourceName = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDValue().getSourcename();
            String idValueSourceType = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDValue().getSourcetype();
            String idValueValue = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDValue().getValue();
            String idTypeNameDateStamp = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDTypeName().getDatestamp();
            String idTypeNameSourceName = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDTypeName().getSourcename();
            String idTypeNameSourceType = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDTypeName().getSourcetype();
            String idTypeNameValue = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDTypeName().getValue();
            String addressIdTypeDateStamp = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getAddresseeIDType().getDatestamp();
            String addressIdTypeSourceName = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getAddresseeIDType().getSourcename();
            String addressIdTypeSourceType = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getAddresseeIDType().getSourcetype();
            String addressIdTypeValue = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getAddresseeIDType().getValue();
            String senderIdentifierDateStamp = onixElement.getHeader().getSender().getSenderIdentifier().getDatestamp();
            String senderIdentifierSourceName = onixElement.getHeader().getSender().getSenderIdentifier().getSourcename();
            String senderIdentifierSourceType = onixElement.getHeader().getSender().getSenderIdentifier().getSourcetype();
            ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDTypeName senderIdentifierIdTypeName = onixElement.getHeader().getSender().getSenderIdentifier().getIDTypeName();
            ONIXPublicationsLicenseMessage.Header.Sender.SenderIdentifier.IDValue senderIdentifierIdValue = onixElement.getHeader().getSender().getSenderIdentifier().getIDValue();
            String senderContactDateStamp = onixElement.getHeader().getSender().getSenderContact().getDatestamp();
            String senderContactSourceName = onixElement.getHeader().getSender().getSenderContact().getSourcename();
            String senderContactSourceType = onixElement.getHeader().getSender().getSenderContact().getSourcetype();
            String senderContactValue = onixElement.getHeader().getSender().getSenderContact().getValue();
            String senederIdTypeDatestamp = onixElement.getHeader().getSender().getSenderIdentifier().getSenderIDType().getDatestamp();
            String senederIdTypeSourceName = onixElement.getHeader().getSender().getSenderIdentifier().getSenderIDType().getSourcename();
            String senederIdTypeSourceType = onixElement.getHeader().getSender().getSenderIdentifier().getSenderIDType().getSourcetype();
            String senederIdTypeValue = onixElement.getHeader().getSender().getSenderIdentifier().getSenderIDType().getValue();
            String senederEmailDateStamp = onixElement.getHeader().getSender().getSenderEmail().getDatestamp();
            String senederEmailSourceName = onixElement.getHeader().getSender().getSenderEmail().getSourcename();
            String senederEmailSourceType = onixElement.getHeader().getSender().getSenderEmail().getSourcetype();
            String senederEmailValue = onixElement.getHeader().getSender().getSenderEmail().getValue();
            String senderDateStamp = onixElement.getHeader().getSender().getDatestamp();
            String senderEmailDateStamp = onixElement.getHeader().getSender().getSenderEmail().getDatestamp();
            String senderEmailSourceANme = onixElement.getHeader().getSender().getSenderEmail().getSourcename();
            String senderEmailSourcetype = onixElement.getHeader().getSender().getSenderEmail().getSourcetype();
            String senderSourceType = onixElement.getHeader().getSender().getSourcetype();
            String addresseIdTypeDateStamp = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getAddresseeIDType().getDatestamp();
            String addresseIdTypeSourceName = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getAddresseeIDType().getSourcename();
            String addresseIdTypeSourceType = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getAddresseeIDType().getSourcetype();
            String addresseIdTypeValue = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getAddresseeIDType().getValue();
            String senderSourceName = onixElement.getHeader().getSender().getSourcename();
            String AddresseIdentifierIdTypeNameDateStamp = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDTypeName().getDatestamp();
            String AddresseIdentifierIdTypeNameSourceName = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDTypeName().getSourcename();
            String AddresseIdentifierIdTypeNameSourceType = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDTypeName().getSourcename();
            String AddresseIdentifierIdTypeNameValue = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDTypeName().getValue();
            String AddresseIdentifierIdValueDateStamp = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDValue().getDatestamp();
            String AddresseIdentifierIdValueSourceName = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDValue().getSourcename();
            String AddresseIdentifierIdValueSourceType = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDValue().getSourcetype();
            String AddresseIdentifierIdValueValue = onixElement.getHeader().getAddressee().get(0).getAddresseeIdentifier().getIDValue().getValue();
            String senderIdentifierIdTypeNameDateStamp = onixElement.getHeader().getSender().getSenderIdentifier().getIDTypeName().getDatestamp();
            String senderIdentifierIdTypeNameSourceName = onixElement.getHeader().getSender().getSenderIdentifier().getIDTypeName().getSourcename();
            String senderIdentifierIdTypeNameSourceType = onixElement.getHeader().getSender().getSenderIdentifier().getIDTypeName().getSourcetype();
            String senderIdentifierIdTypeNameValue = onixElement.getHeader().getSender().getSenderIdentifier().getIDTypeName().getValue();
            String senderIdentifierIdValueDateStamp = onixElement.getHeader().getSender().getSenderIdentifier().getIDValue().getDatestamp();
            String senderIdentifierIdValueSourceName = onixElement.getHeader().getSender().getSenderIdentifier().getIDValue().getSourcename();
            String senderIdentifierIdValueSourceType = onixElement.getHeader().getSender().getSenderIdentifier().getIDValue().getSourcetype();
            String senderIdentifierIdValueValue = onixElement.getHeader().getSender().getSenderIdentifier().getIDValue().getValue();

            Annotation.Authority authority = new Annotation.Authority();
            authority.setDatestamp("12/12/2012");
            authority.setSourcename("sourcename");
            authority.setSourcetype("sourcetype");
            authority.setValue("value");
            assertNotNull(authority.getDatestamp());
            assertNotNull(authority.getSourcename());
            assertNotNull(authority.getSourcetype());
            assertNotNull(authority.getValue());
            Annotation.Authority authority1 = new Annotation.Authority();
            authority1.setSourcetype(null);
            assertNotNull(authority1.getSourcetype());

            Annotation.AnnotationType annotationType = new Annotation.AnnotationType();
            annotationType.setDatestamp("12/12/2012");
            annotationType.setSourcename("sourcename");
            annotationType.setValue(AnnotationTypeCode.ONIX_PL_ACKNOWLEDGEMENT_WORDING);
            annotationType.setSourcetype("sourceType");
            assertNotNull(annotationType.getDatestamp());
            assertNotNull(annotationType.getSourcename());
            assertNotNull(annotationType.getSourcetype());
            assertNotNull(annotationType.getValue());
            Annotation.AnnotationType annotationType2 = new Annotation.AnnotationType();
            annotationType2.setSourcetype(null);
            assertNotNull(annotationType2.getSourcetype());

            Annotation.AnnotationText annotationText = new Annotation.AnnotationText();
            annotationText.setDatestamp("12/12/2012");
            annotationText.setSourcename("sourcename");
            annotationText.setSourcetype("sourceType");
            annotationText.setValue("value");
            annotationText.setLanguage("english");
            assertNotNull(annotationText.getDatestamp());
            assertNotNull(annotationText.getSourcename());
            assertNotNull(annotationText.getSourcetype());
            assertNotNull(annotationText.getValue());
            assertNotNull(annotationText.getLanguage());

            Annotation.AnnotationText annotationText1 = new Annotation.AnnotationText();
            annotationText1.setSourcetype(null);
            assertNotNull(annotationText1.getSourcetype());

            Annotation annotation = new Annotation();
            annotation.setAnnotationText(annotationText);
            annotation.setAnnotationType(annotationType);
            annotation.setAuthority(authority);
            annotation.setDatestamp("12/12/2012");
            annotation.setSourcename("sourcename");
            annotation.setSourcetype("sourceType");
            assertNotNull(annotation.getAnnotationText());
            assertNotNull(annotation.getAnnotationType());
            assertNotNull(annotation.getAuthority());
            assertNotNull(annotation.getDatestamp());
            assertNotNull(annotation.getSourcename());
            assertNotNull(annotation.getSourcetype());

            Annotation annotation1 = new Annotation();
            annotation1.setSourcetype(null);
            assertNotNull(annotation1.getSourcetype());

            LicenseDocumentReference.SectionDesignation sectionDesignation = new LicenseDocumentReference.SectionDesignation();
            sectionDesignation.setDatestamp("12/12/2012");
            sectionDesignation.setSourcename("sourcename");
            sectionDesignation.setSourcetype("sourceType");
            sectionDesignation.setValue("value");
            assertNotNull(sectionDesignation.getDatestamp());
            assertNotNull(sectionDesignation.getSourcename());
            assertNotNull(sectionDesignation.getSourcetype());
            assertNotNull(sectionDesignation.getValue());
            LicenseDocumentReference.SectionDesignation sectionDesignation1 = new LicenseDocumentReference.SectionDesignation();
            sectionDesignation1.setSourcetype(null);
            assertNotNull(sectionDesignation1.getSourcetype());

            SectionIdentifier.SectionIDType sectionIDType = new SectionIdentifier.SectionIDType();
            sectionIDType.setValue(SectionIDTypeCode.ONIX_PL_DOI);
            sectionIDType.setDatestamp("12/12/2012");
            sectionIDType.setSourcename("sourcename");
            sectionIDType.setSourcetype("sourceType");
            assertNotNull(sectionIDType.getDatestamp());
            assertNotNull(sectionIDType.getSourcename());
            assertNotNull(sectionIDType.getSourcetype());
            assertNotNull(sectionIDType.getValue());

            SectionIdentifier.SectionIDType sectionIDType1 = new SectionIdentifier.SectionIDType();
            sectionIDType1.setSourcetype(null);
            assertNotNull(sectionIDType1.getSourcetype());

            SectionIdentifier.IDValue idValue1 = new SectionIdentifier.IDValue();
            idValue1.setDatestamp("12/12/2012");
            idValue1.setSourcename("sourcename");
            idValue1.setSourcetype("sourceType");
            idValue1.setValue("value");
            assertNotNull(idValue1.getDatestamp());
            assertNotNull(idValue1.getSourcename());
            assertNotNull(idValue1.getSourcetype());
            assertNotNull(idValue1.getValue());

            SectionIdentifier.IDValue idValue2 = new SectionIdentifier.IDValue();
            idValue2.setSourcetype(null);
            assertNotNull(idValue2.getSourcetype());

            SectionIdentifier sectionIdentifier = new SectionIdentifier();
            sectionIdentifier.setIDValue(idValue1);
            sectionIdentifier.setSectionIDType(sectionIDType);
            sectionIdentifier.setDatestamp("12/12/2012");
            sectionIdentifier.setSourcename("sourcename");
            sectionIdentifier.setSourcetype("sourceType");
            assertNotNull(sectionIdentifier.getSectionIDType());
            assertNotNull(sectionIdentifier.getIDValue());
            assertNotNull(sectionIdentifier.getDatestamp());
            assertNotNull(sectionIdentifier.getSourcename());
            assertNotNull(sectionIdentifier.getSourcetype());

            SectionIdentifier sectionIdentifier1 = new SectionIdentifier();
            sectionIdentifier1.setSourcetype(null);
            assertNotNull(sectionIdentifier1.getSourcetype());


            LicenseDocumentReference.DocumentLabel documentLabel = new LicenseDocumentReference.DocumentLabel();
            documentLabel.setDatestamp("12/12/2012");
            documentLabel.setSourcename("sourcename");
            documentLabel.setSourcetype("sourceType");
            documentLabel.setValue("value");
            assertNotNull(documentLabel.getDatestamp());
            assertNotNull(documentLabel.getSourcename());
            assertNotNull(documentLabel.getSourcetype());
            assertNotNull(documentLabel.getValue());
            LicenseDocumentReference.DocumentLabel documentLabel1 = new LicenseDocumentReference.DocumentLabel();
            documentLabel1.setSourcetype(null);
            assertNotNull(documentLabel1.getSourcetype());

            LicenseDocumentReference licenseDocumentReference = new LicenseDocumentReference();
            licenseDocumentReference.setDocumentLabel(documentLabel);
            licenseDocumentReference.setSectionDesignation(sectionDesignation);
            licenseDocumentReference.setSectionIdentifier(sectionIdentifier);
            licenseDocumentReference.setDatestamp("12/12/2012");
            licenseDocumentReference.setSourcename("sourcename");
            licenseDocumentReference.setSourcetype("sourceType");
            assertNotNull(licenseDocumentReference.getDocumentLabel());
            assertNotNull(licenseDocumentReference.getSectionDesignation());
            assertNotNull(licenseDocumentReference.getSectionIdentifier());
            assertNotNull(licenseDocumentReference.getDatestamp());
            assertNotNull(licenseDocumentReference.getSourcename());
            assertNotNull(licenseDocumentReference.getSourcetype());

            LicenseDocumentReference licenseDocumentReference1 = new LicenseDocumentReference();
            licenseDocumentReference1.setSourcetype(null);
            assertNotNull(licenseDocumentReference1.getSourcetype());


            WorkLicenseOnixplRecordConverter workLicenseOnixplRecordConverter = new WorkLicenseOnixplRecordConverter();
            workLicenseOnixplRecordConverter.unmarshal(null);

        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }

    }


    @Test
    public void marshal() {
        WorkLicenseOnixplRecordConverter workLicenseOnixplRecordConverter = new WorkLicenseOnixplRecordConverter();
        ONIXPublicationsLicenseMessage onixPublicationsLicenseMessage = new ONIXPublicationsLicenseMessage();
        workLicenseOnixplRecordConverter.marshal(onixPublicationsLicenseMessage);
    }

    @Test
    public void testObjectFactory() {
        ObjectFactory objectFactory = new ObjectFactory();
        assertNotNull(objectFactory.createAnnotation());
        assertNotNull(objectFactory.createAnnotationAnnotationText());
        assertNotNull(objectFactory.createAnnotationAnnotationType());
        assertNotNull(objectFactory.createAnnotationAuthority());
        assertNotNull(objectFactory.createLicenseDescription());
        assertNotNull(objectFactory.createLicenseDocumentReference());
        assertNotNull(objectFactory.createLicenseDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createLicenseDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createLicenseTextLink());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessage());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeader());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderAddressee());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderAddresseeAddresseeContact());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderAddresseeAddresseeEmail());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderAddresseeAddresseeIdentifier());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderAddresseeAddresseeIdentifierAddresseeIDType());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderMessageNote());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderMessageNumber());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderMessageRepeat());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderSender());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderSenderSenderContact());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderSenderSenderEmail());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderSenderSenderIdentifier());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderSenderSenderIdentifierIDTypeName());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderSenderSenderIdentifierIDValue());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderSenderSenderIdentifierSenderIDType());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderSentDateTime());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessagePublicationsLicenseExpression());
        assertNotNull(objectFactory.createPartyName());
        assertNotNull(objectFactory.createPublicationsLicenseExpression());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTerms());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTerm());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermQuantity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermQuantityContinuingAccessTermQuantityType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermQuantityQuantityDetail());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermQuantityQuantityDetailProximity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermQuantityQuantityDetailQuantityUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermQuantityQuantityDetailReferenceUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermQuantityQuantityDetailValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedAgentContinuingAccessTermAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermOtherDocumentReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermOtherDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermOtherDocumentReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermOtherDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitions());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionDescription());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionDescription());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointQuantity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTerms());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceIdentifierResourceIDType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedPlaceSupplyTermPlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceQuantityQuantityDetailReferenceUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionDescription());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermQuantityQuantityDetailReferenceUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionRelatedTimePointTimePointIdentifier());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionOtherDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionOtherDocumentReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermQuantityQuantityDetailReferenceUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionRelatedTimePointTimePointIdentifier());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionOtherDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermQuantityQuantityDetailValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermQuantityQuantityDetailReferenceUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderAddresseeAddresseeIdentifierIDValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedTimePointPaymentTermTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedTimePointAgentTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionOtherDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceQuantityQuantityDetailQuantityUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentNameAgentNamePartAgentNamePartType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermQuantityQuantityDetail());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageOtherDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageQuantity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedAgentLicenseGrantAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDocumentTextTextElementTextPreceding());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionIdentifierIDTypeName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionOtherDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTerms());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointIdentifierTimePointIDType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermOtherDocumentReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTerms());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedPlaceLicensePlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedTimePointResourceTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinition());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedTimePointSupplyTermTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTerm());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantOtherDocumentReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentIdentifier());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedAgentPaymentTermAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedAgentPlaceAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageStatus());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionReferenceExpressionVersion());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermOtherDocumentReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantOtherDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermQuantityQuantityDetailProximity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceNamePlaceNamePartNamePart());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedTimePointLicenseTimePointRelator());
        assertNotNull(objectFactory.createSectionIdentifier());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageCondition());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDocumentTextTextElement());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermQuantityQuantityDetail());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinition());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentNameAgentNamePart());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantCondition());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionOtherDocumentReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsCurrencyCode());
        assertNotNull(objectFactory.createSectionIdentifierSectionIDType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantCondition());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermOtherDocumentReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantPurpose());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedTimePointLicenseGrantTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointQuantityQuantityDetailQuantityUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermQuantityQuantityDetail());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageException());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentQuantityQuantityDetailReferenceUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedResourceLicenseResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionOtherDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailDescription());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermOtherDocumentReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsagePurpose());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinition());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseDocumentLicenseDocumentType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentNameAgentNameType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantOtherDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageOtherDocumentReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermOtherDocumentReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermQuantity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermOtherDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantOtherDocumentReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionOtherDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedTimePointGeneralTermTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermQuantityQuantityDetailReferenceUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionReferenceExpressionIdentifierIDTypeName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointQuantityQuantityDetailValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedResourceAgentResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionDescription());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedAgentSupplyTermAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedPlaceContinuingAccessTermPlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceQuantityQuantityDetail());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseIdentifier());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDescription());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentIdentifierIDTypeName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedPlaceDocumentPlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseIdentifierIDValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentQuantityAgentQuantityType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceQuantityQuantityDetailReferenceUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedPlaceUsagePlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedPlacePaymentTermPlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermQuantitySupplyTermQuantityType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedTimePointDocumentTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageMethod());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentQuantity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedPlaceTimePointPlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseIdentifierLicenseIDType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceQuantity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionOtherDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermOtherDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceQuantityQuantityDetail());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceNamePlaceNamePartPlaceNamePartType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentQuantityQuantityDetailQuantityUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedAgentGeneralTermAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionIdentifierExpressionIDType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceIdentifierPlaceIDType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionOtherDocumentReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedResourcePlaceResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermQuantity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionIdentifierIDValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermQuantityQuantityDetailProximity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermQuantityQuantityDetailQuantityUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermQuantityQuantityDetailValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermQuantityQuantityDetailQuantityUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTerm());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionOtherDocumentReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDocumentTextDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedResourceTimePointResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedAgentLicenseAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetail());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailOtherDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedResourceUsageResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionOtherDocumentReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointQuantityQuantityDetailProximity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceQuantityResourceQuantityType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedTimePointContinuingAccessTermTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceQuantityQuantityDetailValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRenewalType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointQuantityQuantityDetail());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentIdentifier());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermOtherDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceIdentifierIDValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedTimePointTimePointTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsage());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionVersion());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTerm());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermOtherDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermQuantity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDocumentText());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceIdentifierIDTypeName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedResourceGeneralTermResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedResourceLicenseGrantResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailOtherDocumentReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionReferenceExpressionIdentifierIDValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceQuantityPlaceQuantityType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedAgentDocumentAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceIdentifier());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailOtherDocumentReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionOtherDocumentReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTerms());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageQuantityQuantityDetailQuantityUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionOtherDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermOtherDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedPlaceLicenseGrantPlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseIdentifierIDTypeName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermQuantityQuantityDetailValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermQuantityGeneralTermQuantityType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedPlaceGeneralTermPlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermQuantityQuantityDetailProximity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedTimePointUsageTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinition());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedResourceContinuingAccessTermResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionRelatedTimePointTimePointIdentifierTimePointIDType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceQuantity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentIdentifierAgentIDType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedTimePointPlaceTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointIdentifierIDValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrant());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceNameName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedAgentAgentAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageOtherDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionReferenceExpressionIdentifier());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermQuantityQuantityDetailQuantityUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinition());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentIdentifierIDValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionIdentifier());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedPlaceAgentPlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createSectionIdentifierIDValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUser());
        assertNotNull(objectFactory.createONIXPublicationsLicenseMessageHeaderAddresseeAddresseeIdentifierIDTypeName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDocumentTextTextElementText());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionRelatedAgentExpressionAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageQuantityQuantityDetailValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceQuantityQuantityDetailValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionContinuingAccessTermsContinuingAccessTermContinuingAccessTermRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageQuantityUsageQuantityType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceIdentifier());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedAgentUsageAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageQuantityQuantityDetailReferenceUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentQuantityQuantityDetail());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentIdentifierIDValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetail());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionStatus());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionReferenceExpressionIdentifierExpressionIDType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionRelatedTimePointExpressionTimePointRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionRelatedTimePointTimePointIdentifierIDValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceIdentifierIDTypeName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedResourceDocumentResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceRelatedPlacePlacePlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionRelatedAgentName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionOtherDocumentReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceQuantityQuantityDetailQuantityUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentNameName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentIdentifierIDTypeName());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentQuantityQuantityDetailValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermQuantityQuantityDetailReferenceUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceQuantityQuantityDetailProximity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceNamePlaceNameType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermQuantityPaymentTermQuantityType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermOtherDocumentReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageQuantityQuantityDetailProximity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedResource());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedResourceResourceResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceIdentifierIDValue());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseDocumentDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionGeneralTermsGeneralTermGeneralTermRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointIdentifier());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentQuantityQuantityDetailProximity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDocumentTextTextElementDisplayNumber());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedAgentTimePointAgentRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedAgentRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionPaymentTermsPaymentTermPaymentTermRelatedResourcePaymentTermResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageOtherDocumentReferenceReferenceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantAuthority());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionExpressionDetailExpressionReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermOtherDocumentReferenceSectionDesignation());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointQuantityQuantityDetailReferenceUnit());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedAgent());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseStatus());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionOtherDocumentReference());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageRelatedPlaceRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionOtherDocumentReferenceDocumentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceNamePlaceNamePart());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsPlaceDefinitionPlaceQuantityQuantityDetailProximity());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageQuantityQuantityDetail());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedResourceSupplyTermResourceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDocumentTextTextElementSortNumber());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentNameAgentNamePartNamePart());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseGrantLicenseGrantRelatedPlace());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionLicenseDetailLicenseDocument());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionSupplyTermsSupplyTermSupplyTermRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentRelatedTimePointRelatedTimePoint());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsDocumentDefinitionDocumentIdentifierDocumentIDType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsAgentDefinitionAgentLabel());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionUsageTermsUsageUsageExceptionUsageExceptionType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsTimePointDefinitionTimePointQuantityTimePointQuantityType());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedPlaceResourcePlaceRelator());
        assertNotNull(objectFactory.createPublicationsLicenseExpressionDefinitionsResourceDefinitionResourceRelatedAgentResourceAgentRelator());

        assertNotNull(GeneralTermTypeCode.fromValue("onixPL:UserFeedback"));
        assertNotNull(GeneralTermTypeCode.ONIX_PL_ACTION_AGAINST_MISUSE.value());
        try {
            GeneralTermTypeCode.fromValue("Abc");
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(LicenseAgentRelatorCode.fromValue("onixPL:LicensorLicenseAdministrator"));
        assertNotNull(LicenseAgentRelatorCode.ONIX_PL_LICENSEE_REPRESENTATIVE.value());
        try {
            LicenseAgentRelatorCode.fromValue("jdhfj");
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(UsagePurposeCode.ONIX_PL_PERSONAL_USE.value());
        assertNotNull(UsagePurposeCode.fromValue("onixPL:CommercialUse"));
        try {
            assertNotNull(UsagePurposeCode.fromValue("hgdsf"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        assertNotNull(UsageTypeCode.ONIX_PL_COPY.value());
        assertNotNull(UsageTypeCode.fromValue("onixPL:UseForDataMining"));
        try {
            assertNotNull(UsageTypeCode.fromValue("ABS"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        assertNotNull(SupplyTermTypeCode.ONIX_PL_CHANGES_TO_LICENSED_CONTENT.value());
        assertNotNull(SupplyTermTypeCode.fromValue("onixPL:UserSupport"));
        try {
            assertNotNull(SupplyTermTypeCode.fromValue("abc"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }
        assertNotNull(AnnotationTypeCode.ONIX_PL_ACKNOWLEDGEMENT_WORDING.value());
        assertNotNull(AnnotationTypeCode.fromValue("onixPL:ERMI:WalkInUserTermNote"));
        try {
            assertNotNull(AnnotationTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(TermStatusCode.ONIX_PL_EXPLICIT_YES.value());
        assertNotNull(TermStatusCode.fromValue("onixPL:Uncertain"));
        try {
            assertNotNull(TermStatusCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(QuantityUnitCode.ONIX_PL_DAYS.value());
        assertNotNull(QuantityUnitCode.fromValue("onixPL:Copies"));
        try {
            assertNotNull(QuantityUnitCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(LicensePlaceRelatorCode.ONIX_PL_ADDRESS_FOR_NOTICES_TO_LICENSEE_CONSORTIUM.value());
        assertNotNull(LicensePlaceRelatorCode.fromValue("onixPL:PlaceOfCopyrightLaw"));
        try {
            assertNotNull(LicensePlaceRelatorCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(UsageConditionCode.ONIX_PL_RECORD_KEEPING_NOT_REQUIRED.value());
        assertNotNull(UsageConditionCode.fromValue("onixPL:DestructionOfUsedResourceAfterUse"));
        try {
            assertNotNull(UsageConditionCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(UsageStatusCode.ONIX_PL_NOT_APPLICABLE.value());
        assertNotNull(UsageStatusCode.fromValue("onixPL:InterpretedAsProhibited"));
        try {
            assertNotNull(UsageStatusCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(UsageMethodCode.ONIX_PL_FAX.value());
        assertNotNull(UsageMethodCode.fromValue("onixPL:Fax"));
        try {
            assertNotNull(UsageMethodCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(LicenseStatusCode.ONIX_PL_MODEL.value());
        assertNotNull(LicenseStatusCode.fromValue("onixPL:ActiveLicense"));
        try {
            assertNotNull(LicenseStatusCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(ExpressionTypeCode.ONIX_PL_ERMI_LICENSE_ENCODING.value());
        assertNotNull(ExpressionTypeCode.fromValue("onixPL:LicenseExpression"));
        try {
            assertNotNull(ExpressionTypeCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(DocumentIDTypeCode.ONIX_PL_LICENSEE_DOCUMENT_REFERENCE.value());
        assertNotNull(DocumentIDTypeCode.fromValue("onixPL:Proprietary"));
        try {
            assertNotNull(DocumentIDTypeCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(ResourceResourceRelatorCode.ONIX_PL_IS_ANY_OF.value());
        assertNotNull(ResourceResourceRelatorCode.fromValue("onixPL:IsPartOf"));
        try {
            assertNotNull(ResourceResourceRelatorCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(ReferenceRelatorCode.ONIX_PL_BASED_ON_TERMS_OF.value());
        assertNotNull(ReferenceRelatorCode.fromValue("onixPL:DerivedFromModel"));
        try {
            assertNotNull(ReferenceRelatorCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(ProximityCode.ONIX_PL_LESS_THAN.value());
        assertNotNull(ProximityCode.fromValue("onixPL:MoreThan"));
        try {
            assertNotNull(ProximityCode.fromValue("abs"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(ContinuingAccessTermTypeCode.ONIX_PL_CONTINUING_ACCESS.value());
        assertNotNull(ContinuingAccessTermTypeCode.fromValue("onixPL:NotificationOfDarkArchive"));
        try {
            assertNotNull(ContinuingAccessTermTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(AgentTypeCode.ONIX_PL_PERSON.value());
        assertNotNull(AgentTypeCode.fromValue("onixPL:Unspecified"));
        try {
            assertNotNull(AgentTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(AgentPlaceRelatorCode.ONIX_PL_PLACE_OF_CORPORATE_REGISTRATION.value());
        assertNotNull(AgentPlaceRelatorCode.fromValue("onixPL:RegisteredAddress"));
        try {
            assertNotNull(AgentPlaceRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(GeneralTermQuantityTypeCode.ONIX_PL_NOTICE_PERIOD_FOR_TERMINATION.value());
        assertNotNull(GeneralTermQuantityTypeCode.fromValue("onixPL:NoticePeriodForNonRenewal"));
        try {
            assertNotNull(GeneralTermQuantityTypeCode.fromValue("abc"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(UsageQuantityTypeCode.ONIX_PL_NUMBER_OF_COPIES_PERMITTED.value());
        assertNotNull(UsageQuantityTypeCode.fromValue("onixPL:NumberOfConcurrentUsers"));
        try {
            assertNotNull(UsageQuantityTypeCode.fromValue("absd"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(SectionIDTypeCode.ONIX_PL_PROPRIETARY.value());
        assertNotNull(SectionIDTypeCode.fromValue("onixPL:URI"));
        try {
            assertNotNull(SectionIDTypeCode.fromValue("absh"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(ResourceIDTypeCode.ONIX_PL_ISBN_13.value());
        assertNotNull(ResourceIDTypeCode.fromValue("onixPL:DOI"));
        try {
            assertNotNull(ResourceIDTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(PlaceIDTypeCode.ONIX_PL_LOCATION_CODE.value());
        assertNotNull(PlaceIDTypeCode.fromValue("onixPL:LocationCode"));
        try {
            assertNotNull(PlaceIDTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(LicenseTimePointRelatorCode.ONIX_PL_LICENSE_END_DATE.value());
        assertNotNull(LicenseTimePointRelatorCode.fromValue("onixPL:LicenseEndDate"));
        try {
            assertNotNull(LicenseTimePointRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(LicenseRenewalTypeCode.ONIX_PL_EXPLICIT.value());
        assertNotNull(LicenseRenewalTypeCode.fromValue("onixPL:Explicit"));
        try {
            assertNotNull(LicenseRenewalTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(LicenseIDTypeCode.ONIX_PL_LICENSEE_CONTRACT_NUMBER.value());
        assertNotNull(LicenseIDTypeCode.fromValue("onixPL:LicenseeContractNumber"));
        try {
            assertNotNull(LicenseIDTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(LicenseGrantTypeCode.ONIX_PL_NON_EXCLUSIVE.value());
        assertNotNull(LicenseGrantTypeCode.fromValue("onixPL:Exclusive"));
        try {
            assertNotNull(LicenseGrantTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(LicenseDocumentTypeCode.ONIX_PL_LICENSE.value());
        assertNotNull(LicenseDocumentTypeCode.fromValue("onixPL:LicenseMainTerms"));
        try {
            assertNotNull(LicenseDocumentTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(GeneralTermPlaceRelatorCode.ONIX_PL_ADDRESS_FOR_NOTICES_TO_LICENSOR.value());
        assertNotNull(GeneralTermPlaceRelatorCode.fromValue("onixPL:AddressForNoticesToLicensor"));
        try {
            assertNotNull(GeneralTermPlaceRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(ExpressionStatusCode.ONIX_PL_DRAFT.value());
        assertNotNull(ExpressionStatusCode.fromValue("onixPL:Approved"));
        try {
            assertNotNull(ExpressionStatusCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(ExpressionIDTypeCode.ONIX_PL_URL.value());
        assertNotNull(ExpressionIDTypeCode.fromValue("onixPL:URI"));
        try {
            assertNotNull(ExpressionIDTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(AgentNameTypeCode.ONIX_PL_POSITION_IN_ORGANIZATION.value());
        assertNotNull(AgentNameTypeCode.fromValue("onixPL:RegisteredName"));
        try {
            assertNotNull(AgentNameTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(AgentAgentRelatorCode.ONIX_PL_IS_ANY_OF.value());
        assertNotNull(AgentAgentRelatorCode.fromValue("onixPL:IsAuthorizedRepresentativeOf"));
        try {
            assertNotNull(AgentAgentRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(UsagePlaceRelatorCode.ONIX_PL_PLACE_OF_RECEIVING_AGENT.value());
        assertNotNull(UsagePlaceRelatorCode.fromValue("onixPL:PlaceOfDeposit"));
        try {
            assertNotNull(UsagePlaceRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(UsageExceptionTypeCode.ONIX_PL_EXCEPT_AS_PERMITTED.value());
        assertNotNull(UsageExceptionTypeCode.fromValue("onixPL:ExceptAsPermittedByStatute"));
        try {
            assertNotNull(UsageExceptionTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(GeneralTermTimePointRelatorCode.ONIX_PL_EFFECTIVE_DATE_FOR_VARIATION.value());
        assertNotNull(GeneralTermTimePointRelatorCode.fromValue("onixPL:ExpiryDateForNotice"));
        try {
            assertNotNull(GeneralTermTimePointRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(RelatedAgentCode.ONIX_PL_EXTERNAL_LIBRARIAN.value());
        assertNotNull(RelatedAgentCode.fromValue("onixPL:ExternalLibrarian"));
        try {
            assertNotNull(RelatedAgentCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(RelatedPlaceCode.ONIX_PL_LICENSEE_PREMISES.value());
        assertNotNull(RelatedPlaceCode.fromValue("onixPL:LicenseePremises"));
        try {
            assertNotNull(RelatedPlaceCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(RelatedResourceCode.ONIX_PL_ACKNOWLEDGMENT_OF_SOURCE.value());
        assertNotNull(RelatedResourceCode.fromValue("onixPL:AcknowledgmentOfSource"));
        try {
            assertNotNull(RelatedResourceCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }


        assertNotNull(RelatedTimePointCode.ONIX_PL_LICENSE_START_DATE.value());
        assertNotNull(RelatedTimePointCode.fromValue("onixPL:LicenseEndDate"));
        try {
            assertNotNull(RelatedTimePointCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(UsedResourceCode.ONIX_PL_AUTHORED_CONTENT.value());
        assertNotNull(UsedResourceCode.fromValue("onixPL:AuthoredContent"));
        try {
            assertNotNull(UsedResourceCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(UserCode.ONIX_PL_EXTERNAL_STUDENT.value());
        assertNotNull(UserCode.fromValue("onixPL:ExternalStudent"));
        try {
            assertNotNull(UserCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(PlaceNameTypeCode.ONIX_PL_ADDRESS_AS_SEPARATE_LINES.value());
        assertNotNull(PlaceNameTypeCode.fromValue("onixPL:AddressAsSeparateLines"));
        try {
            assertNotNull(PlaceNameTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(UsageResourceRelatorCode.ONIX_PL_TARGET_RESOURCE.value());
        assertNotNull(UsageResourceRelatorCode.fromValue("onixPL:TargetResource"));
        try {
            assertNotNull(UsageResourceRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(LicenseGrantPurposeCode.ONIX_PL_NON_COMMERCIAL_USE.value());
        assertNotNull(LicenseGrantPurposeCode.fromValue("onixPL:NonCommercialUse"));
        try {
            assertNotNull(LicenseGrantPurposeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(GeneralTermAgentRelatorCode.ONIX_PL_ADDRESSEE_FOR_NOTICES_TO_LICENSEE.value());
        assertNotNull(GeneralTermAgentRelatorCode.fromValue("onixPL:AddresseeForNoticesToLicensee"));
        try {
            assertNotNull(GeneralTermAgentRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(ExpressionAgentRelatorCode.ONIX_PL_APPROVED_BY.value());
        assertNotNull(ExpressionAgentRelatorCode.fromValue("onixPL:ApprovedBy"));
        try {
            assertNotNull(ExpressionAgentRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(DocumentTypeCode.ONIX_PL_DOCUMENT.value());
        assertNotNull(DocumentTypeCode.fromValue("onixPL:Document"));
        try {
            assertNotNull(DocumentTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(AgentIDTypeCode.ONIX_PL_COMPANY_REGISTRATION_NUMBER.value());
        assertNotNull(AgentIDTypeCode.fromValue("onixPL:CompanyRegistrationNumber"));
        try {
            assertNotNull(AgentIDTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(UsageAgentRelatorCode.ONIX_PL_RECEIVING_AGENT.value());
        assertNotNull(UsageAgentRelatorCode.fromValue("onixPL:ReceivingAgent"));
        try {
            assertNotNull(UsageAgentRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(TimePointIDTypeCode.ONIX_PL_YYYYMMDD.value());
        assertNotNull(TimePointIDTypeCode.fromValue("onixPL:YYYYMMDD"));
        try {
            assertNotNull(TimePointIDTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(SupplyTermTimePointRelatorCode.ONIX_PL_SUPPLY_START_DATE.value());
        assertNotNull(SupplyTermTimePointRelatorCode.fromValue("onixPL:SupplyStartDate"));
        try {
            assertNotNull(SupplyTermTimePointRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(SupplyTermQuantityTypeCode.ONIX_PL_GUARANTEED_UPTIME.value());
        assertNotNull(SupplyTermQuantityTypeCode.fromValue("onixPL:GuaranteedUptime"));
        try {
            assertNotNull(SupplyTermQuantityTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(PlacePlaceRelatorCode.ONIX_PL_IS_ANY_OF.value());
        assertNotNull(PlacePlaceRelatorCode.fromValue("onixPL:IsAnyOf"));
        try {
            assertNotNull(PlacePlaceRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(PlaceNamePartTypeCode.ONIX_PL_ADDRESS_LINE.value());
        assertNotNull(PlaceNamePartTypeCode.fromValue("onixPL:AddressLine"));
        try {
            assertNotNull(PlaceNamePartTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(PaymentTermTypeCode.ONIX_PL_PAYMENT_CONDITIONS.value());
        assertNotNull(PaymentTermTypeCode.fromValue("onixPL:PaymentConditions"));
        try {
            assertNotNull(PaymentTermTypeCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(LicenseResourceRelatorCode.ONIX_PL_LICENSED_CONTENT.value());
        assertNotNull(LicenseResourceRelatorCode.fromValue("onixPL:LicensedContent"));
        try {
            assertNotNull(LicenseResourceRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(ExpressionTimePointRelatorCode.ONIX_PL_PREPARED_ON.value());
        assertNotNull(ExpressionTimePointRelatorCode.fromValue("onixPL:PreparedOn"));
        try {
            assertNotNull(ExpressionTimePointRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        assertNotNull(DocumentTimePointRelatorCode.ONIX_PL_LATEST_REVISION_DATE.value());
        assertNotNull(DocumentTimePointRelatorCode.fromValue("onixPL:LatestRevisionDate"));
        try {
            assertNotNull(DocumentTimePointRelatorCode.fromValue("onix"));
        } catch (Exception e) {
            LOG.info(e.getMessage());
        }

        WorkLicenseOnixplRecord workLicenseOnixplRecord = new WorkLicenseOnixplRecord();
        workLicenseOnixplRecord.setTitle("title");
        workLicenseOnixplRecord.setContractNumber("123");
        workLicenseOnixplRecord.setLicensee("admin");
        workLicenseOnixplRecord.setLicensor("licensor");
        assertNotNull(workLicenseOnixplRecord.getContractNumber());
        assertNotNull(workLicenseOnixplRecord.getLicensee());
        assertNotNull(workLicenseOnixplRecord.getLicensor());
        assertNotNull(workLicenseOnixplRecord.getTitle());

        LicenseDescription licenseDescription = new LicenseDescription();
        licenseDescription.setValue("value");
        licenseDescription.setLanguage("language");
        licenseDescription.setSourcename("sourcename");
        licenseDescription.setSourcetype("type");
        licenseDescription.setDatestamp("12/12/2013");
        assertNotNull(licenseDescription.getDatestamp());
        assertNotNull(licenseDescription.getLanguage());
        assertNotNull(licenseDescription.getSourcename());
        assertNotNull(licenseDescription.getSourcetype());
        assertNotNull(licenseDescription.getValue());
        LicenseDescription licenseDescription1 = new LicenseDescription();
        licenseDescription1.setSourcetype(null);
        assertNotNull(licenseDescription1.getSourcetype());

        LicenseTextLink licenseTextLink = new LicenseTextLink();
        licenseTextLink.setDatestamp("12/12/2013");
        licenseTextLink.setSourcename("sourcename");
        licenseTextLink.setSourcetype("sourcetype");
        licenseTextLink.setHref("href");
        assertNotNull(licenseTextLink.getDatestamp());
        assertNotNull(licenseTextLink.getHref());
        assertNotNull(licenseTextLink.getSourcename());
        assertNotNull(licenseTextLink.getSourcetype());
        LicenseTextLink licenseTextLink1 = new LicenseTextLink();
        assertNotNull(licenseTextLink1.getSourcetype());


        PartyName partyName = new PartyName();
        partyName.setDatestamp("12/12/2013");
        partyName.setSourcename("sourcename");
        partyName.setSourcetype("sourcetype");
        partyName.setValue("value");
        assertNotNull(partyName.getDatestamp());
        assertNotNull(partyName.getSourcename());
        assertNotNull(partyName.getSourcetype());
        assertNotNull(partyName.getValue());
        PartyName partyName1 = new PartyName();
        partyName1.setSourcetype(null);
        assertNotNull(partyName1.getSourcetype());


    }
}


