package org.kuali.ole.utility;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Stub that returns some Strings.
 */
public class OleHttpRestGetStringImpl implements OleHttpRestGet {
    private List<String> restResult;

    public OleHttpRestGetStringImpl() {
        restResult = Collections.EMPTY_LIST;
    }

    /**
     * Initializes with some Strings.
     * @param restResult    Strings that rest(restPath) will return.
     */
    public OleHttpRestGetStringImpl(String ... restResult) {
        this.restResult = new LinkedList<String>();
        for (String s : restResult) {
            this.restResult.add(s);
        }
    }

    /**
     * Returns the Strings set in the constructor, and then null.
     * @param restPath  the path starting with "rest/" of the URL where to do the GET
     * @return the Strings or null.
     */
    @Override
    public String rest(String restPath) {
        if (restResult.isEmpty()) {
            return null;
        }

        return restResult.remove(0);
    }
}
