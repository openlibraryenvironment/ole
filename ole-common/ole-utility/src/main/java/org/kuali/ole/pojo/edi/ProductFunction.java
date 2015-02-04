package org.kuali.ole.pojo.edi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/6/12
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductFunction {
    private String productId;
    private List<ProductArticleNumber> productArticleNumber = new ArrayList<ProductArticleNumber>();
    private List<SupplierArticleNumber> supplierArticleNumber = new ArrayList<>();

    public void addProductArticleNumber(ProductArticleNumber productArticleNumber) {
        if (!this.productArticleNumber.contains(productArticleNumber)) {
            this.productArticleNumber.add(productArticleNumber);
        }
    }

    public void addSupplierArticleNumber(SupplierArticleNumber supplierArticleNumber) {
        if (!this.supplierArticleNumber.contains(supplierArticleNumber)) {
            this.supplierArticleNumber.add(supplierArticleNumber);
        }
    }


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public List<ProductArticleNumber> getProductArticleNumber() {
        return productArticleNumber;
    }

    public void setProductArticleNumber(List<ProductArticleNumber> productArticleNumber) {
        this.productArticleNumber = productArticleNumber;
    }

    public List<SupplierArticleNumber> getSupplierArticleNumber() {
        return supplierArticleNumber;
    }

    public void setSupplierArticleNumber(List<SupplierArticleNumber> supplierArticleNumber) {
        this.supplierArticleNumber = supplierArticleNumber;
    }
}
