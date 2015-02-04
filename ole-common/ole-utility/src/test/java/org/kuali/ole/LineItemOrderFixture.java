package org.kuali.ole;

import org.kuali.ole.pojo.edi.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/8/12
 * Time: 8:31 PM
 * To change this template use File | Settings | File Templates.
 */
public enum LineItemOrderFixture {
    LINEITEM("1", null, "9783835309449", "EN", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
    PRODUCTFUNCTION(null, null, null, null, "5", "3835309447", "IB", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null),
    ITEMDESCRIPTION(null, null, null, null, null, null, null, "L", "050", ":::Ausnahmezustand der Literatur", null, null, null, null, null, null, null, null, null, null, null, null),
    QUANTITYINFORMATION(null, null, null, null, null, null, null, null, null, null, "21", "1", null, null, null, null, null, null, null, null, null, null),
    PRICEINFORMATION(null, null, null, null, null, null, null, null, null, null, null, null, "AAB", "34.07", null, null, null, null, null, null, null, null),
    REFERENCEINFORMATION(null, null, null, null, null, null, null, null, null, null, null, null, null, null, "LI", "603634", null, null, null, null, null, null),
    SUPPLIERSREFERENCEINFORMATION(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "SLI", "har110148705", null, null, null, null),
    FUNDNUMBERREFERENCE(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "BFN", "PXXUXXXGRXXX631-2012", null, null),
    TRANSPORTSTAGEQUALIFIER(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "20", "53"),;

    private String sequenceNumber;
    private String itemNumberId;
    private String lineItemIsbn;
    private String lineItemNumberType;
    private String productId;
    private String productIsbn;
    private String productItemNumberType;
    private String text;
    private String itemCharacteristicCode;
    private String data;
    private String quantityConstant;
    private String quantity;
    private String grossPrice;
    private String price;
    private String buyersOrderLine;
    private String orderLineNumber;
    private String suppliersOrderLine;
    private String vendorReferenceNumber;
    private String buyersFundNumber;
    private String budgetNumber;
    private String transportStageConstant;
    private String surfaceMail;

    private LineItemOrderFixture(String sequenceNumber, String itemNumberId, String lineItemIsbn,
                                 String lineItemNumberType, String productId, String productIsbn,
                                 String productItemNumberType, String text, String itemCharacteristicCode,
                                 String data, String quantityConstant, String quantity, String grossPrice,
                                 String price, String buyersOrderLine, String orderLineNumber, String suppliersOrderLine,
                                 String vendorReferenceNumber, String buyersFundNumber, String budgetNumber, String transportStageConstant,
                                 String surfaceMail) {
        this.sequenceNumber = sequenceNumber;
        this.itemNumberId = itemNumberId;
        this.lineItemIsbn = lineItemIsbn;
        this.lineItemNumberType = lineItemNumberType;
        this.productId = productId;
        this.productIsbn = productIsbn;
        this.productItemNumberType = productItemNumberType;
        this.text = text;
        this.itemCharacteristicCode = itemCharacteristicCode;
        this.data = data;
        this.quantityConstant = quantityConstant;
        this.quantity = quantity;
        this.grossPrice = grossPrice;
        this.price = price;
        this.buyersOrderLine = buyersOrderLine;
        this.orderLineNumber = orderLineNumber;
        this.suppliersOrderLine = suppliersOrderLine;
        this.vendorReferenceNumber = vendorReferenceNumber;
        this.buyersFundNumber = buyersFundNumber;
        this.budgetNumber = budgetNumber;
        this.transportStageConstant = transportStageConstant;
        this.surfaceMail = surfaceMail;


    }

    public LineItem createLineItemPojo(Class clazz) {
        LineItem lineItem = null;
        try {
            lineItem = (LineItem) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("LineItem creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("LineItem creation failed. class = " + clazz);
        }
        LineItemArticleNumber lineItemArticleNumber = new LineItemArticleNumber();
        lineItemArticleNumber.setLineItemIsbn(lineItemIsbn);
        lineItemArticleNumber.setLineItemNumberType(lineItemNumberType);
        lineItem.setSequenceNumber(sequenceNumber);
        lineItem.setItemNumberId(itemNumberId);
        lineItem.addArticleNumber(lineItemArticleNumber);
        return lineItem;
    }

    public ProductFunction createProductFunctionPojo(Class clazz) {
        ProductFunction productFunction = null;
        try {
            productFunction = (ProductFunction) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Product Function creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Product Function creation failed. class = " + clazz);
        }
        ProductArticleNumber productArticleNumber = new ProductArticleNumber();
        productArticleNumber.setProductIsbn(productIsbn);
        productArticleNumber.setProductItemNumberType(productItemNumberType);
        productFunction.setProductId(productId);
        productFunction.addProductArticleNumber(productArticleNumber);

        return productFunction;
    }

    public ItemDescription createItemDescriptionPojo(Class clazz) {
        ItemDescription itemDescription = null;
        try {
            itemDescription = (ItemDescription) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("ItemDescription creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("ItemDescription creation failed. class = " + clazz);
        }

        itemDescription.setText(text);
        itemDescription.setItemCharacteristicCode(itemCharacteristicCode);
        itemDescription.setData(data);
        return itemDescription;
    }

    public QuantityInformation createQuantityInformationPojo(Class clazz) {
        QuantityInformation quantityInformation = null;
        try {
            quantityInformation = (QuantityInformation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("QuantityInformation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("QuantityInformation creation failed. class = " + clazz);
        }
        Qunatity quantity1 = new Qunatity();
        quantity1.setQuantityConstant(quantityConstant);
        quantity1.setQuantity(quantity);
        quantityInformation.addQuantity(quantity1);

        return quantityInformation;
    }

    public PriceInformation createPriceInformationPojo(Class clazz) {
        PriceInformation priceInformation = null;
        try {
            priceInformation = (PriceInformation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("PriceInformation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("PriceInformation creation failed. class = " + clazz);
        }
        ItemPrice itemPrice = new ItemPrice();
        itemPrice.setGrossPrice(grossPrice);
        itemPrice.setPrice(price);
        priceInformation.addPrice(itemPrice);

        return priceInformation;
    }

    public BuyerReferenceInformation createBuyerReferenceInformationPojo(Class clazz) {
        BuyerReferenceInformation buyerReferenceInformation = null;
        try {
            buyerReferenceInformation = (BuyerReferenceInformation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("BuyerReferenceInformation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("BuyerReferenceInformation creation failed. class = " + clazz);
        }
        BuyerLineItemReference buyerLineItemReference = new BuyerLineItemReference();
        buyerLineItemReference.setBuyersOrderLine(buyersOrderLine);
        buyerLineItemReference.setOrderLineNumber(orderLineNumber);
        buyerReferenceInformation.addBuyerLineItemReference(buyerLineItemReference);

        return buyerReferenceInformation;
    }

    public SupplierReferenceInformation createSupplierReferenceInformationPojo(Class clazz) {
        SupplierReferenceInformation supplierReferenceInformation = null;
        try {
            supplierReferenceInformation = (SupplierReferenceInformation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("SupplierReferenceInformation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SupplierReferenceInformation creation failed. class = " + clazz);
        }
        SupplierLineItemReference supplierLineItemReference = new SupplierLineItemReference();
        supplierLineItemReference.setSuppliersOrderLine(suppliersOrderLine);
        supplierLineItemReference.setVendorReferenceNumber(vendorReferenceNumber);
        supplierReferenceInformation.addSupplierLineItemReference(supplierLineItemReference);

        return supplierReferenceInformation;
    }

    public FundNumberReference createFundNumberReferencePojo(Class clazz) {
        FundNumberReference fundNumberReference = null;
        try {
            fundNumberReference = (FundNumberReference) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("FundNumberReference creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("FundNumberReference creation failed. class = " + clazz);
        }
        BuyersFundNumberReference buyersFundNumberReference = new BuyersFundNumberReference();
        buyersFundNumberReference.setBudgetNumber(budgetNumber);
        buyersFundNumberReference.setBuyersFundNumber(buyersFundNumber);
        fundNumberReference.addBuyersFundNumberReference(buyersFundNumberReference);

        return fundNumberReference;
    }


    public TransportStageQualifier createTransportStageQualifierPojo(Class clazz) {
        TransportStageQualifier transportStageQualifier = null;
        try {
            transportStageQualifier = (TransportStageQualifier) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("TransportStageQualifier creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("TransportStageQualifier creation failed. class = " + clazz);
        }

        transportStageQualifier.setSurfaceMail(surfaceMail);
        transportStageQualifier.setTransportStageConstant(transportStageConstant);

        return transportStageQualifier;
    }
}