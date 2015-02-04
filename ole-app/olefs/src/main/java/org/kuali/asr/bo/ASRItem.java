package org.kuali.asr.bo;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: maheswarang
 * Date: 12/24/13
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "asrItem")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonAutoDetect(JsonMethod.FIELD)
public class ASRItem {
    @XmlElement(name = "itemNumber")
    @JsonProperty("itemNumber")
    private String itemBarcode;
    @XmlElement(name = "title")
    @JsonProperty("title")
    private String title;
    @XmlElement(name = "author")
    @JsonProperty("author")
    private String author;
    @XmlElement(name = "callNumber")
    @JsonProperty("callNumber")
    private String callNumber;

    public String getItemBarcode() {
        return itemBarcode;
    }

    public void setItemBarcode(String itemBarcode) {
        this.itemBarcode = itemBarcode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }
}
