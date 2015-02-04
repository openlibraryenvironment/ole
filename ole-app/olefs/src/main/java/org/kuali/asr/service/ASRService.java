package org.kuali.asr.service;

import org.kuali.asr.bo.*;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 12/24/13
 * Time: 5:49 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ASRService {

    public Object lookupNewASRItems(String operatorId);

    public ASRResponseBo removeASRItem(String barcode);

    public Object updateASRItemStatusToAvailable(UpdateASRItemRequestBo updateASRItemRequestBo);

    public ASRResponseBo placeRequestOnASRItem(PlaceASRItemRequestBo placeRequestASRItemBo);

    public ASRResponseBo cancelASRRequest(String holdId, String operatorId);

    public Object lookupASRTypeRequest(String operatorId, String asrLocation);

    public Object updateASRRequestStatus(UpdateASRRequestStatusBo updateASRRequestStatusBo);

    public Object updateASRItemStatusToBeingRetrieved(UpdateASRItemStatusBo updateASRItemStatusBo);

    public Object updateASRItemStatusToMissing(UpdateASRItemStatusBo updateASRItemStatusBo);

    public ASRResponseBo addNewASRItem(ASRItem asrItem);

    public ASRResponseBo sendASRRequest(ASRRequestBo asrRequestBo);

    public Object lookupAsrRequest(String operatorId, String itemBarcode);

    public ASRResponseBo receiveASRItemTransit(ReceiveTransitRequestBo receiveTransitRequestBo);

    public ASRResponseBo checkInASRItem(ASRCheckInBo asrCheckInBo);












}
