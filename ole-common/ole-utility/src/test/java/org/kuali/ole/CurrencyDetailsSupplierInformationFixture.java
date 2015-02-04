package org.kuali.ole;

/**
 * Created by IntelliJ IDEA.
 * User: palanivel
 * Date: 3/8/12
 * Time: 7:31 PM
 * To change this template use File | Settings | File Templates.
 */

import org.kuali.ole.pojo.edi.CurrencyDetailsSupplierInformation;

public enum CurrencyDetailsSupplierInformationFixture {
    CurrencyDetailsSupplierInformation("2",
            "USD",
            "9"
    ),;

    private String defaultCurrency;
    private String currencyType;
    private String orderCurrency;

    private CurrencyDetailsSupplierInformationFixture(String defaultCurrency, String currencyType,
                                                      String orderCurrency) {
        this.defaultCurrency = defaultCurrency;
        this.currencyType = currencyType;
        this.orderCurrency = orderCurrency;
    }

    public CurrencyDetailsSupplierInformation createCurrencyDetailsSupplierInformation(Class clazz) {
        CurrencyDetailsSupplierInformation currencyDetailsSupplierInformation = null;
        try {
            currencyDetailsSupplierInformation = (CurrencyDetailsSupplierInformation) clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("CurrencyDetailsSupplierInformation creation failed. class = " + clazz);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("CurrencyDetailsSupplierInformation creation failed. class = " + clazz);
        }
        currencyDetailsSupplierInformation.setDefaultCurrency(defaultCurrency);
        currencyDetailsSupplierInformation.setCurrencyType(currencyType);
        currencyDetailsSupplierInformation.setOrderCurrency(orderCurrency);
        return currencyDetailsSupplierInformation;
    }

}
