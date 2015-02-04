package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/2/12
 * Time: 2:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemDescription {
    private String text;
    private String itemCharacteristicCode;
    private String data;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getItemCharacteristicCode() {
        return itemCharacteristicCode;
    }

    public void setItemCharacteristicCode(String itemCharacteristicCode) {
        this.itemCharacteristicCode = itemCharacteristicCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
