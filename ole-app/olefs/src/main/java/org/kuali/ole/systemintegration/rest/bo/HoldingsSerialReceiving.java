package org.kuali.ole.systemintegration.rest.bo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.log4j.Logger;
import org.kuali.ole.docstore.common.document.content.instance.OleHoldings;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: sheiksalahudeenm
 * Date: 3/11/14
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlType(name = "holdingsSerialReceiving", propOrder = {
        "serialReceiving"
})
@XStreamAlias("oleHoldings")
@XmlRootElement(name = "oleHoldings")
public class HoldingsSerialReceiving extends OleHoldings {

    private static final Logger LOG = Logger.getLogger(HoldingsSerialReceiving.class);

    private SerialReceiving serialReceiving;

    public SerialReceiving getSerialReceiving() {
        return serialReceiving;
    }

    public void setSerialReceiving(SerialReceiving serialReceiving) {
        this.serialReceiving = serialReceiving;
    }

    public HoldingsSerialReceiving copyHoldingSerialReceiving(OleHoldings oleHoldings){
        HoldingsSerialReceiving oleHoldingsSerialReceiving = new HoldingsSerialReceiving();
        oleHoldingsSerialReceiving.setHoldingsIdentifier(oleHoldings.getHoldingsIdentifier());
        oleHoldingsSerialReceiving.setBibIdentifier(oleHoldings.getBibIdentifier());
        oleHoldingsSerialReceiving. setUri(oleHoldings.getUri());
        oleHoldingsSerialReceiving.setStaffOnlyFlag(oleHoldings.isStaffOnlyFlag());
        oleHoldingsSerialReceiving.setAccessStatus(oleHoldings.getAccessStatus());
        oleHoldingsSerialReceiving.setCallNumber(oleHoldings.getCallNumber());
        oleHoldingsSerialReceiving.setDonorInfo(oleHoldings.getDonorInfo());
        oleHoldingsSerialReceiving.setCopyNumber(oleHoldings.getCopyNumber());
        oleHoldingsSerialReceiving.setDonorNote(oleHoldings.getDonorNote());
        oleHoldingsSerialReceiving.setDonorPublicDisplay(oleHoldings.getDonorPublicDisplay());
        oleHoldingsSerialReceiving.setEResourceId(oleHoldings.getEResourceId());
        oleHoldingsSerialReceiving.setExtension(oleHoldings.getExtension());
        oleHoldingsSerialReceiving.setExtentOfOwnership(oleHoldings.getExtentOfOwnership());
        oleHoldingsSerialReceiving.setHoldingsType(oleHoldings.getHoldingsType());
        oleHoldingsSerialReceiving.setImprint(oleHoldings.getImprint());
        oleHoldingsSerialReceiving.setInterLibraryLoanAllowed(oleHoldings.isInterLibraryLoanAllowed());
        oleHoldingsSerialReceiving.setHoldingsAccessInformation(oleHoldings.getHoldingsAccessInformation());
        oleHoldingsSerialReceiving.setLink(oleHoldings.getLink());
        oleHoldingsSerialReceiving.setLocalPersistentLink(oleHoldings.getLocalPersistentLink());
        oleHoldingsSerialReceiving.setLocation(oleHoldings.getLocation());
        oleHoldingsSerialReceiving.setNote(oleHoldings.getNote());
        oleHoldingsSerialReceiving.setPlatform(oleHoldings.getPlatform());
        oleHoldingsSerialReceiving.setPrimary(oleHoldings.getPrimary());
        oleHoldingsSerialReceiving.setInterLibraryLoanAllowed(oleHoldings.isInterLibraryLoanAllowed());
        oleHoldingsSerialReceiving.setPublisher(oleHoldings.getPublisher());
        oleHoldingsSerialReceiving.setStatisticalSearchingCode(oleHoldings.getStatisticalSearchingCode());
        oleHoldingsSerialReceiving.setStatusDate(oleHoldings.getStatusDate());
        oleHoldingsSerialReceiving.setSubscriptionStatus(oleHoldings.getSubscriptionStatus());
        return oleHoldingsSerialReceiving;
    }


    public String serialize(Object object) {
        String result = null;
        StringWriter sw = new StringWriter();
        HoldingsSerialReceiving holdingsSerialReceiving = (HoldingsSerialReceiving) object;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(HoldingsSerialReceiving.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(holdingsSerialReceiving, sw);
            result = sw.toString().replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>","");
        } catch (Exception e) {
            LOG.error("Exception :", e);
        }
        return result;
    }
}
