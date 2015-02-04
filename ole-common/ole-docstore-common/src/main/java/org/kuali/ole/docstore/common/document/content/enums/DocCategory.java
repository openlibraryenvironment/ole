package org.kuali.ole.docstore.common.document.content.enums;

/**
 * Enumerations for document categories.
 * User: tirumalesh.b
 * Date: 4/12/11
 */
public enum DocCategory {
    WORK("work", "work"),
    AUTHORITY("auth", "authority"),
    SECURITY("security", "security");

    private final String code;
    private final String description;

    private DocCategory(String code, String description) {
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
