package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/2/12
 * Time: 2:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class LineItem {

    private String sequenceNumber;
    private String itemNumberId;
    private List<LineItemArticleNumber> lineItemArticleNumber = new ArrayList<LineItemArticleNumber>();

    public void addArticleNumber(LineItemArticleNumber articleNumber) {
        if (!this.lineItemArticleNumber.contains(articleNumber)) {
            this.lineItemArticleNumber.add(articleNumber);
        }
    }

    public List<LineItemArticleNumber> getLineItemArticleNumber() {
        return lineItemArticleNumber;
    }

    public void setLineItemArticleNumber(List<LineItemArticleNumber> lineItemArticleNumber) {
        this.lineItemArticleNumber = lineItemArticleNumber;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getItemNumberId() {
        return itemNumberId;
    }

    public void setItemNumberId(String itemNumberId) {
        this.itemNumberId = itemNumberId;
    }


}

