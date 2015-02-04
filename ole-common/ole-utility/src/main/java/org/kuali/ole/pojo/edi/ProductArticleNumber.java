package org.kuali.ole.pojo.edi;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductArticleNumber {
    private String productIsbn;
    private String productItemNumberType;
    private String unknown1;
    private String unknown2;
    private String unknown3;

    public String getProductIsbn() {
        return productIsbn;
    }

    public void setProductIsbn(String productIsbn) {
        this.productIsbn = productIsbn;
    }

    public String getProductItemNumberType() {
        return productItemNumberType;
    }

    public void setProductItemNumberType(String productItemNumberType) {
        this.productItemNumberType = productItemNumberType;
    }

    public String getUnknown1() {
        return unknown1;
    }

    public void setUnknown1(String unknown1) {
        this.unknown1 = unknown1;
    }

    public String getUnknown2() {
        return unknown2;
    }

    public void setUnknown2(String unknown2) {
        this.unknown2 = unknown2;
    }

    public String getUnknown3() {
        return unknown3;
    }

    public void setUnknown3(String unknown3) {
        this.unknown3 = unknown3;
    }
}
