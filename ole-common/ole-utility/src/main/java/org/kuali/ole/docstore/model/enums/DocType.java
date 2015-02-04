package org.kuali.ole.docstore.model.enums;

/**
 * Enumerations for document types.
 * User: tirumalesh.b
 * Date: 4/12/11 Time: 9:29 PM
 */
public enum DocType {
    BIB("bibliographic", "bibliographic"),
    INSTANCE("instance", "instance"),
    PATRON("patron", "patron"),
    ITEM("item", "item"),
    HOLDINGS("holdings", "holdings"),
    SOURCEHOLDINGS("sourceHoldings", "sourceHoldings"),
    LICENSE("license", "license"),
    EINSTANCE("eInstance", "eInstance"),
    EHOLDINGS("eHoldings", "eHoldings");


    private final String code;
    private final String description;

    private DocType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEqualTo(String input) {
        boolean result = false;
        if (null == input) {
            result = false;
        } else if (input.equalsIgnoreCase(getCode())) {
            result = true;
        } else if (input.equalsIgnoreCase(getDescription())) {
            result = true;
        }
        return result;
    }

}
