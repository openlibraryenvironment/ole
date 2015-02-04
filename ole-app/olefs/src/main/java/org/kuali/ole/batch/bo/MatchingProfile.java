package org.kuali.ole.batch.bo;

import org.kuali.rice.krad.bo.PersistableBusinessObjectBase;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 6/17/14
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class MatchingProfile extends PersistableBusinessObjectBase {

    private boolean matchBibs;
    private boolean noMatchBibs_addBibs;
    private String bibsNotMatchAddIncomingBibs;

    private boolean bibNotMatched_discardBib;
    private boolean bibNotMatched_addBib;

    private String matchIncomingExistBib;

    private String bibNotMatchedDiscardOrAdd;

    private boolean bibMatched_addBib;
    private boolean bibMatched_discardBib;
    private boolean bibMatched_updateBib;
    private String bibMatchedDiscardOrAddOrUpdate;
    private String bibMatchedDiscardOrUpdate;


    private boolean matchHoldings;
    private boolean noMatchHoldings_discardHoldingsItems;
    private boolean noMatchHoldings_deleteAddHoldingsItems;
    private boolean noMatchHoldings_retainAddHoldingsItems;
    private boolean holdingsNotMatched_discardHoldings;
    private boolean holdingsNotMatched_addHoldings;
    private boolean holdingsNotMatched_addItems;
    private boolean holdingsMatched_addHoldings;
    private boolean holdingsMatched_addItems;
    private boolean holdingsMatched_discardHoldings;
    private boolean holdingsMatched_updateHoldings;

    private String matchIncomingHoldings;
    private String discardDeleteKeepHoldingItem;
    private String incomingHoldingNotMatched;
    private String incomingHoldingMatched;
    private String incomingHoldingMatchedProcessHolding;
    private String incomingHoldingMatchedProcessItem;
    private String discardDeleteKeepItem;
    private String incomingItemMatchedProcessItem;


    private boolean matchItems;
    private boolean noMatchItem_discardItems;
    private boolean noMatchItem_deleteAddItems;
    private boolean noMatchItem_retainAddItems;
    private boolean itemNotMatched_discardItem;
    private boolean itemNotMatched_addItem;
    private boolean itemMatched_addItem;
    private boolean itemMatched_updateItem;

    private String matchIncomingItems;

    private String incomingItemNotMatchedProcessItem;

    private String incomingBibNotMatchWithExistBib;
    private String incomingBibMatchWithExistBib;
    private String processBib;

    private static String MATCH_BIBS = "matchBibs";
    private static String NO_MATCH_BIBS_ADD_BIBS = "noMatchBibs_addBibs";
    private static String BIB_NOT_MATCHED_DISCARD_BIB = "bibNotMatched_discardBib";
    private static String BIB_NOT_MATCHED_ADD_BIB = "bibNotMatched_addBib";
    private static String BIB_MATCHED_ADD_BIB = "bibMatched_addBib";
    private static String BIB_MATCHED_DISCARD_BIB = "bibMatched_discardBib";
    private static String BIB_MATCHED_UPDATE_BIB = "bibMatched_updateBib";

    private static String MATCH_HOLDINGS = "matchHoldings";
    private static String NO_MATCH_HOLDINGS_DISCARD_HOLDINGS_ITEMS = "noMatchHoldings_discardHoldingsItems";
    private static String NO_MATCH_HOLDINGS_DELETE_ADD_HOLDINGS_ITEMS = "noMatchHoldings_deleteAddHoldingsItems";
    private static String NO_MATCH_HOLDINGS_RETAIN_ADD_HOLDINGS_ITEMS = "noMatchHoldings_retainAddHoldingsItems";
    private static String HOLDINGS_NOT_MATCHED_DISCARD_HOLDINGS = "holdingsNotMatched_discardHoldings";
    private static String HOLDINGS_NOT_MATCHED_ADD_HOLDINGS = "holdingsNotMatched_addHoldings";
    private static String HOLDINGS_NOT_MATCHED_ADD_ITEMS = "holdingsNotMatched_addItems";
    private static String HOLDINGS_MATCHED_ADD_HOLDINGS = "holdingsMatched_addHoldings";
    private static String HOLDINGS_MATCHED_ADD_ITEMS = "holdingsMatched_addItems";
    private static String HOLDINGS_MATCHED_DISCARD_HOLDINGS = "holdingsMatched_discardHoldings";
    private static String HOLDINGS_MATCHED_UPDATE_HOLDINGS = "holdingsMatched_updateHoldings";

    private static String MATCH_ITEMS = "matchItems";
    private static String NO_MATCH_ITEM_DISCARD_ITEMS = "noMatchItem_discardItems";
    private static String NO_MATCH_ITEM_DELETE_ADD_ITEMS = "noMatchItem_deleteAddItems";
    private static String NO_MATCH_ITEM_RETAIN_ADD_ITEMS = "noMatchItem_retainAddItems";
    private static String ITEM_NOT_MATCHED_DISCARD_ITEM = "itemNotMatched_discardItem";
    private static String ITEM_NOT_MATCHED_ADD_ITEM = "itemNotMatched_addItem";
    private static String ITEM_MATCHED_ADD_ITEM = "itemMatched_addItem";
    private static String ITEM_MATCHED_UPDATE_ITEM = "itemMatched_updateItem";
    private static String COMMA = ",";
    private static String EQUALS = "=";

    private static String PERFORM_MATCHING = "performMatching";
    private static String NOT_PERFORM_MATCHING = "notPerformMatching";
    private static String PERFORM_HOLDING_MATCH = "performHoldingMatch";
    private static String NOT_PERFORM_HOLDING_MATCH = "notPerformHodingMatch";
    private static String PERFORM_MATCHING_OF_ITEM = "performMatchingOfItem";
    private static String NOT_PERFORM_MATCHING_OF_ITEM = "notPerformMatchingOfItem";
    private static String PROCESS_BIB_HOLDINGS_ITEMS = "processBibHoldingsItems";
    private static String PROCESS_HOLDING_AND_ITEM = "processHoldingAndItem";




    @Override
    public String toString() {
        if (!matchIncomingExistBib.isEmpty() && matchIncomingExistBib.equals(PERFORM_MATCHING)) {
            matchBibs = true;
        } else {
            matchBibs = false;
        }
        if (!bibsNotMatchAddIncomingBibs.isEmpty() && bibsNotMatchAddIncomingBibs.equals(NO_MATCH_BIBS_ADD_BIBS)) {
            noMatchBibs_addBibs = true;
        } else {
            noMatchBibs_addBibs = false;
        }

        if (!bibNotMatchedDiscardOrAdd.isEmpty() && bibNotMatchedDiscardOrAdd.equals(BIB_NOT_MATCHED_ADD_BIB)) {

            bibNotMatched_addBib = true;
            bibNotMatched_discardBib = false;
        } else {
            bibNotMatched_discardBib = true;
            bibNotMatched_addBib = false;
        }

        if (!bibMatchedDiscardOrAddOrUpdate.isEmpty() && bibMatchedDiscardOrAddOrUpdate.equals(PROCESS_BIB_HOLDINGS_ITEMS)) {
            bibMatched_addBib = false;
        } else {
            bibMatched_addBib = true;
        }

        if (!bibMatchedDiscardOrUpdate.isEmpty() && bibMatchedDiscardOrUpdate.equals(BIB_MATCHED_UPDATE_BIB)) {
            bibMatched_updateBib = true;
            bibMatched_discardBib = false;
        } else {
            bibMatched_discardBib = true;
            bibMatched_updateBib = false;
        }

        if (!matchIncomingHoldings.isEmpty() && matchIncomingHoldings.equals(PERFORM_HOLDING_MATCH)) {
            matchHoldings = true;
        } else {
            matchHoldings = false;
        }

        if (!discardDeleteKeepHoldingItem.isEmpty() && discardDeleteKeepHoldingItem.equals(NO_MATCH_HOLDINGS_DISCARD_HOLDINGS_ITEMS)) {
            noMatchHoldings_discardHoldingsItems = true;
            noMatchHoldings_deleteAddHoldingsItems = false;
            noMatchHoldings_retainAddHoldingsItems = false;

        } else if (!discardDeleteKeepHoldingItem.isEmpty() && discardDeleteKeepHoldingItem.equals(NO_MATCH_HOLDINGS_DELETE_ADD_HOLDINGS_ITEMS)) {
            noMatchHoldings_discardHoldingsItems = false;
            noMatchHoldings_deleteAddHoldingsItems = true;
            noMatchHoldings_retainAddHoldingsItems = false;
        } else {
            noMatchHoldings_discardHoldingsItems = false;
            noMatchHoldings_deleteAddHoldingsItems = false;
            noMatchHoldings_retainAddHoldingsItems = true;
        }

        if (!incomingHoldingNotMatched.isEmpty() && incomingHoldingNotMatched.equals(HOLDINGS_NOT_MATCHED_ADD_HOLDINGS)) {
            holdingsNotMatched_addHoldings = true;
//            holdingsNotMatched_addItems = true;
            holdingsNotMatched_discardHoldings = false;
        } else {
            holdingsNotMatched_addHoldings = false;
//            holdingsNotMatched_addItems = false;
            holdingsNotMatched_discardHoldings = true;
        }
        if (!incomingHoldingMatched.isEmpty() && incomingHoldingMatched.equals(HOLDINGS_MATCHED_ADD_HOLDINGS)) {
            holdingsMatched_addHoldings = true;
//            holdingsMatched_addItems = true;
        } else {
            holdingsMatched_addHoldings = false;
//            holdingsMatched_addItems = false;
        }


        if (!incomingHoldingMatchedProcessHolding.isEmpty() && incomingHoldingMatchedProcessHolding.equals(HOLDINGS_MATCHED_UPDATE_HOLDINGS)) {
            holdingsMatched_updateHoldings = true;
            holdingsMatched_discardHoldings = false;
        } else {
            holdingsMatched_updateHoldings = false;
            holdingsMatched_discardHoldings = true;
        }


        if (!matchIncomingItems.isEmpty() && matchIncomingItems.equals(PERFORM_MATCHING_OF_ITEM)) {
            matchItems = true;

        } else {
            matchItems = false;

        }
        if (!discardDeleteKeepItem.isEmpty() && discardDeleteKeepItem.equals(NO_MATCH_ITEM_DISCARD_ITEMS)) {
            noMatchItem_discardItems = true;
            noMatchItem_deleteAddItems = false;
            noMatchItem_retainAddItems = false;
        } else if (!discardDeleteKeepItem.isEmpty() && discardDeleteKeepItem.equals(NO_MATCH_ITEM_DELETE_ADD_ITEMS)) {
            noMatchItem_discardItems = false;
            noMatchItem_deleteAddItems = true;
            noMatchItem_retainAddItems = false;
        } else {
            noMatchItem_discardItems = false;
            noMatchItem_deleteAddItems = false;
            noMatchItem_retainAddItems = true;

        }


        if (!incomingItemNotMatchedProcessItem.isEmpty() && incomingItemNotMatchedProcessItem.equals(ITEM_NOT_MATCHED_ADD_ITEM)) {
            itemNotMatched_addItem = true;
            itemNotMatched_discardItem = false;
        } else {
            itemNotMatched_addItem = false;
            itemNotMatched_discardItem = true;
        }
        if (!incomingItemMatchedProcessItem.isEmpty() && incomingItemMatchedProcessItem.equals(ITEM_MATCHED_UPDATE_ITEM)) {
            itemMatched_updateItem = true;
            itemMatched_addItem = false;
        } else {
            itemMatched_updateItem = false;
            itemMatched_addItem = true;
        }


        return MATCH_BIBS + EQUALS + matchBibs +
                COMMA + NO_MATCH_BIBS_ADD_BIBS + EQUALS + noMatchBibs_addBibs +
                COMMA + BIB_NOT_MATCHED_DISCARD_BIB + EQUALS + bibNotMatched_discardBib +
                COMMA + BIB_NOT_MATCHED_ADD_BIB + EQUALS + bibNotMatched_addBib +
                COMMA + BIB_MATCHED_ADD_BIB + EQUALS + bibMatched_addBib +
                COMMA + BIB_MATCHED_DISCARD_BIB + EQUALS + bibMatched_discardBib +
                COMMA + BIB_MATCHED_UPDATE_BIB + EQUALS + bibMatched_updateBib +
                COMMA + MATCH_HOLDINGS + EQUALS + matchHoldings +
                COMMA + NO_MATCH_HOLDINGS_DISCARD_HOLDINGS_ITEMS + EQUALS + noMatchHoldings_discardHoldingsItems +
                COMMA + NO_MATCH_HOLDINGS_DELETE_ADD_HOLDINGS_ITEMS + EQUALS + noMatchHoldings_deleteAddHoldingsItems +
                COMMA + NO_MATCH_HOLDINGS_RETAIN_ADD_HOLDINGS_ITEMS + EQUALS + noMatchHoldings_retainAddHoldingsItems +
                COMMA + HOLDINGS_NOT_MATCHED_DISCARD_HOLDINGS + EQUALS + holdingsNotMatched_discardHoldings +
                COMMA + HOLDINGS_NOT_MATCHED_ADD_HOLDINGS + EQUALS + holdingsNotMatched_addHoldings +
                COMMA + HOLDINGS_NOT_MATCHED_ADD_ITEMS + EQUALS + holdingsNotMatched_addItems +
                COMMA + HOLDINGS_MATCHED_ADD_HOLDINGS + EQUALS + holdingsMatched_addHoldings +
                COMMA + HOLDINGS_MATCHED_ADD_ITEMS + EQUALS + holdingsMatched_addItems +
                COMMA + HOLDINGS_MATCHED_DISCARD_HOLDINGS + EQUALS + holdingsMatched_discardHoldings +
                COMMA + HOLDINGS_MATCHED_UPDATE_HOLDINGS + EQUALS + holdingsMatched_updateHoldings +
                COMMA + MATCH_ITEMS + EQUALS + matchItems +
                COMMA + NO_MATCH_ITEM_DISCARD_ITEMS + EQUALS + noMatchItem_discardItems +
                COMMA + NO_MATCH_ITEM_DELETE_ADD_ITEMS + EQUALS + noMatchItem_deleteAddItems +
                COMMA + NO_MATCH_ITEM_RETAIN_ADD_ITEMS + EQUALS + noMatchItem_retainAddItems +
                COMMA + ITEM_NOT_MATCHED_DISCARD_ITEM + EQUALS + itemNotMatched_discardItem +
                COMMA + ITEM_NOT_MATCHED_ADD_ITEM + EQUALS + itemNotMatched_addItem +
                COMMA + ITEM_MATCHED_ADD_ITEM + EQUALS + itemMatched_addItem +
                COMMA + ITEM_MATCHED_UPDATE_ITEM + EQUALS + itemMatched_updateItem;
    }

    public boolean isMatchBibs() {
        return matchBibs;
    }

    public void setMatchBibs(boolean matchBibs) {
        this.matchBibs = matchBibs;
    }

    public boolean isNoMatchBibs_addBibs() {
        return noMatchBibs_addBibs;
    }

    public void setNoMatchBibs_addBibs(boolean noMatchBibs_addBibs) {
        this.noMatchBibs_addBibs = noMatchBibs_addBibs;
    }

    public boolean isBibNotMatched_discardBib() {
        return bibNotMatched_discardBib;
    }

    public void setBibNotMatched_discardBib(boolean bibNotMatched_discardBib) {
        this.bibNotMatched_discardBib = bibNotMatched_discardBib;
    }

    public boolean isBibNotMatched_addBib() {
        return bibNotMatched_addBib;
    }

    public void setBibNotMatched_addBib(boolean bibNotMatched_addBib) {
        this.bibNotMatched_addBib = bibNotMatched_addBib;
    }

    public boolean isBibMatched_addBib() {
        return bibMatched_addBib;
    }

    public void setBibMatched_addBib(boolean bibMatched_addBib) {
        this.bibMatched_addBib = bibMatched_addBib;
    }

    public boolean isBibMatched_discardBib() {
        return bibMatched_discardBib;
    }

    public void setBibMatched_discardBib(boolean bibMatched_discardBib) {
        this.bibMatched_discardBib = bibMatched_discardBib;
    }

    public boolean isBibMatched_updateBib() {
        return bibMatched_updateBib;
    }

    public void setBibMatched_updateBib(boolean bibMatched_updateBib) {
        this.bibMatched_updateBib = bibMatched_updateBib;
    }

    public boolean isMatchHoldings() {
        return matchHoldings;
    }

    public void setMatchHoldings(boolean matchHoldings) {
        this.matchHoldings = matchHoldings;
    }

    public boolean isNoMatchHoldings_discardHoldingsItems() {
        return noMatchHoldings_discardHoldingsItems;
    }

    public void setNoMatchHoldings_discardHoldingsItems(boolean noMatchHoldings_discardHoldingsItems) {
        this.noMatchHoldings_discardHoldingsItems = noMatchHoldings_discardHoldingsItems;
    }

    public boolean isNoMatchHoldings_deleteAddHoldingsItems() {
        return noMatchHoldings_deleteAddHoldingsItems;
    }

    public void setNoMatchHoldings_deleteAddHoldingsItems(boolean noMatchHoldings_deleteAddHoldingsItems) {
        this.noMatchHoldings_deleteAddHoldingsItems = noMatchHoldings_deleteAddHoldingsItems;
    }

    public boolean isNoMatchHoldings_retainAddHoldingsItems() {
        return noMatchHoldings_retainAddHoldingsItems;
    }

    public void setNoMatchHoldings_retainAddHoldingsItems(boolean noMatchHoldings_retainAddHoldingsItems) {
        this.noMatchHoldings_retainAddHoldingsItems = noMatchHoldings_retainAddHoldingsItems;
    }

    public boolean isHoldingsNotMatched_discardHoldings() {
        return holdingsNotMatched_discardHoldings;
    }

    public void setHoldingsNotMatched_discardHoldings(boolean holdingsNotMatched_discardHoldings) {
        this.holdingsNotMatched_discardHoldings = holdingsNotMatched_discardHoldings;
    }

    public boolean isHoldingsNotMatched_addHoldings() {
        return holdingsNotMatched_addHoldings;
    }

    public void setHoldingsNotMatched_addHoldings(boolean holdingsNotMatched_addHoldings) {
        this.holdingsNotMatched_addHoldings = holdingsNotMatched_addHoldings;
    }

    public boolean isHoldingsNotMatched_addItems() {
        return holdingsNotMatched_addItems;
    }

    public void setHoldingsNotMatched_addItems(boolean holdingsNotMatched_addItems) {
        this.holdingsNotMatched_addItems = holdingsNotMatched_addItems;
    }

    public boolean isHoldingsMatched_addHoldings() {
        return holdingsMatched_addHoldings;
    }

    public void setHoldingsMatched_addHoldings(boolean holdingsMatched_addHoldings) {
        this.holdingsMatched_addHoldings = holdingsMatched_addHoldings;
    }

    public boolean isHoldingsMatched_addItems() {
        return holdingsMatched_addItems;
    }

    public void setHoldingsMatched_addItems(boolean holdingsMatched_addItems) {
        this.holdingsMatched_addItems = holdingsMatched_addItems;
    }

    public boolean isHoldingsMatched_discardHoldings() {
        return holdingsMatched_discardHoldings;
    }

    public void setHoldingsMatched_discardHoldings(boolean holdingsMatched_discardHoldings) {
        this.holdingsMatched_discardHoldings = holdingsMatched_discardHoldings;
    }

    public boolean isHoldingsMatched_updateHoldings() {
        return holdingsMatched_updateHoldings;
    }

    public void setHoldingsMatched_updateHoldings(boolean holdingsMatched_updateHoldings) {
        this.holdingsMatched_updateHoldings = holdingsMatched_updateHoldings;
    }

    public boolean isMatchItems() {
        return matchItems;
    }

    public void setMatchItems(boolean matchItems) {
        this.matchItems = matchItems;
    }

    public boolean isNoMatchItem_discardItems() {
        return noMatchItem_discardItems;
    }

    public void setNoMatchItem_discardItems(boolean noMatchItem_discardItems) {
        this.noMatchItem_discardItems = noMatchItem_discardItems;
    }

    public boolean isNoMatchItem_deleteAddItems() {
        return noMatchItem_deleteAddItems;
    }

    public void setNoMatchItem_deleteAddItems(boolean noMatchItem_deleteAddItems) {
        this.noMatchItem_deleteAddItems = noMatchItem_deleteAddItems;
    }

    public boolean isNoMatchItem_retainAddItems() {
        return noMatchItem_retainAddItems;
    }

    public void setNoMatchItem_retainAddItems(boolean noMatchItem_retainAddItems) {
        this.noMatchItem_retainAddItems = noMatchItem_retainAddItems;
    }

    public boolean isItemNotMatched_discardItem() {
        return itemNotMatched_discardItem;
    }

    public void setItemNotMatched_discardItem(boolean itemNotMatched_discardItem) {
        this.itemNotMatched_discardItem = itemNotMatched_discardItem;
    }

    public boolean isItemNotMatched_addItem() {
        return itemNotMatched_addItem;
    }

    public void setItemNotMatched_addItem(boolean itemNotMatched_addItem) {
        this.itemNotMatched_addItem = itemNotMatched_addItem;
    }

    public boolean isItemMatched_addItem() {
        return itemMatched_addItem;
    }

    public void setItemMatched_addItem(boolean itemMatched_addItem) {
        this.itemMatched_addItem = itemMatched_addItem;
    }

    public boolean isItemMatched_updateItem() {
        return itemMatched_updateItem;
    }

    public void setItemMatched_updateItem(boolean itemMatched_updateItem) {
        this.itemMatched_updateItem = itemMatched_updateItem;
    }


    public String getBibNotMatchedDiscardOrAdd() {
        return bibNotMatchedDiscardOrAdd;
    }

    public void setBibNotMatchedDiscardOrAdd(String bibNotMatchedDiscardOrAdd) {
        this.bibNotMatchedDiscardOrAdd = bibNotMatchedDiscardOrAdd;
    }

    public String getBibMatchedDiscardOrAddOrUpdate() {
        return bibMatchedDiscardOrAddOrUpdate;
    }

    public void setBibMatchedDiscardOrAddOrUpdate(String bibMatchedDiscardOrAddOrUpdate) {
        this.bibMatchedDiscardOrAddOrUpdate = bibMatchedDiscardOrAddOrUpdate;
    }

    public String getBibMatchedDiscardOrUpdate() {
        return bibMatchedDiscardOrUpdate;
    }

    public void setBibMatchedDiscardOrUpdate(String bibMatchedDiscardOrUpdate) {
        this.bibMatchedDiscardOrUpdate = bibMatchedDiscardOrUpdate;
    }

    public String getIncomingBibNotMatchWithExistBib() {
        return incomingBibNotMatchWithExistBib;
    }

    public void setIncomingBibNotMatchWithExistBib(String incomingBibNotMatchWithExistBib) {
        this.incomingBibNotMatchWithExistBib = incomingBibNotMatchWithExistBib;
    }

    public String getIncomingBibMatchWithExistBib() {
        return incomingBibMatchWithExistBib;
    }

    public void setIncomingBibMatchWithExistBib(String incomingBibMatchWithExistBib) {
        this.incomingBibMatchWithExistBib = incomingBibMatchWithExistBib;
    }

    public String getProcessBib() {
        return processBib;
    }

    public void setProcessBib(String processBib) {
        this.processBib = processBib;
    }

    public String getDiscardDeleteKeepHoldingItem() {
        return discardDeleteKeepHoldingItem;
    }

    public void setDiscardDeleteKeepHoldingItem(String discardDeleteKeepHoldingItem) {
        this.discardDeleteKeepHoldingItem = discardDeleteKeepHoldingItem;
    }

    public String getIncomingHoldingNotMatched() {
        return incomingHoldingNotMatched;
    }

    public void setIncomingHoldingNotMatched(String incomingHoldingNotMatched) {
        this.incomingHoldingNotMatched = incomingHoldingNotMatched;
    }

    public String getIncomingHoldingMatched() {
        return incomingHoldingMatched;
    }

    public void setIncomingHoldingMatched(String incomingHoldingMatched) {
        this.incomingHoldingMatched = incomingHoldingMatched;
    }

    public String getIncomingHoldingMatchedProcessHolding() {
        return incomingHoldingMatchedProcessHolding;
    }

    public void setIncomingHoldingMatchedProcessHolding(String incomingHoldingMatchedProcessHolding) {
        this.incomingHoldingMatchedProcessHolding = incomingHoldingMatchedProcessHolding;
    }

    public String getIncomingHoldingMatchedProcessItem() {
        return incomingHoldingMatchedProcessItem;
    }

    public void setIncomingHoldingMatchedProcessItem(String incomingHoldingMatchedProcessItem) {
        this.incomingHoldingMatchedProcessItem = incomingHoldingMatchedProcessItem;
    }

    public String getDiscardDeleteKeepItem() {
        return discardDeleteKeepItem;
    }

    public void setDiscardDeleteKeepItem(String discardDeleteKeepItem) {
        this.discardDeleteKeepItem = discardDeleteKeepItem;
    }

    public String getIncomingItemNotMatchedProcessItem() {
        return incomingItemNotMatchedProcessItem;
    }

    public void setIncomingItemNotMatchedProcessItem(String incomingItemNotMatchedProcessItem) {
        this.incomingItemNotMatchedProcessItem = incomingItemNotMatchedProcessItem;
    }

    public String getIncomingItemMatchedProcessItem() {
        return incomingItemMatchedProcessItem;
    }

    public void setIncomingItemMatchedProcessItem(String incomingItemMatchedProcessItem) {
        this.incomingItemMatchedProcessItem = incomingItemMatchedProcessItem;
    }

    public String getMatchIncomingExistBib() {
        return matchIncomingExistBib;
    }

    public void setMatchIncomingExistBib(String matchIncomingExistBib) {
        this.matchIncomingExistBib = matchIncomingExistBib;
    }

    public String getMatchIncomingHoldings() {
        return matchIncomingHoldings;
    }

    public void setMatchIncomingHoldings(String matchIncomingHoldings) {
        this.matchIncomingHoldings = matchIncomingHoldings;
    }

    public String getMatchIncomingItems() {
        return matchIncomingItems;
    }

    public void setMatchIncomingItems(String matchIncomingItems) {
        this.matchIncomingItems = matchIncomingItems;
    }

    public String getBibsNotMatchAddIncomingBibs() {
        return bibsNotMatchAddIncomingBibs;
    }

    public void setBibsNotMatchAddIncomingBibs(String bibsNotMatchAddIncomingBibs) {
        this.bibsNotMatchAddIncomingBibs = bibsNotMatchAddIncomingBibs;
    }

    public static MatchingProfile buildMatchProfileObj(String matchProfileString) throws IOException {
        MatchingProfile matchingProfile = new MatchingProfile();
        String keyValuePairs[] = matchProfileString.split(COMMA);

        for (String keyValuePair : keyValuePairs) {
            String keyValue[] = keyValuePair.split(EQUALS);
            String key = keyValue[0];
            String value = keyValue[1];
            if (key.equalsIgnoreCase(MATCH_BIBS)) {
                matchingProfile.setMatchBibs(Boolean.parseBoolean(value));
                if (matchingProfile.isMatchBibs()) {
                    matchingProfile.setMatchIncomingExistBib(PERFORM_MATCHING);
                } else {
                    matchingProfile.setMatchIncomingExistBib(NOT_PERFORM_MATCHING);
                }

            } else if (key.equalsIgnoreCase(NO_MATCH_BIBS_ADD_BIBS)) {
                matchingProfile.setNoMatchBibs_addBibs(Boolean.parseBoolean(value));
                if (matchingProfile.isNoMatchBibs_addBibs()) {
                    matchingProfile.setBibsNotMatchAddIncomingBibs(NO_MATCH_BIBS_ADD_BIBS);
                }
            } else if (key.equalsIgnoreCase(BIB_NOT_MATCHED_DISCARD_BIB)) {
                matchingProfile.setBibNotMatched_discardBib(Boolean.parseBoolean(value));
                if (matchingProfile.isBibNotMatched_discardBib()) {
                    matchingProfile.setBibNotMatchedDiscardOrAdd(BIB_NOT_MATCHED_DISCARD_BIB);
                }
            } else if (key.equalsIgnoreCase(BIB_NOT_MATCHED_ADD_BIB)) {
                matchingProfile.setBibNotMatched_addBib(Boolean.parseBoolean(value));
                if (matchingProfile.isBibNotMatched_addBib()) {
                    matchingProfile.setBibNotMatchedDiscardOrAdd(BIB_NOT_MATCHED_ADD_BIB);
                }
            } else if (key.equalsIgnoreCase(BIB_MATCHED_ADD_BIB)) {
                matchingProfile.setBibMatched_addBib(Boolean.parseBoolean(value));
                if (matchingProfile.isBibMatched_addBib()) {
                    matchingProfile.setBibMatchedDiscardOrAddOrUpdate(BIB_MATCHED_ADD_BIB);
                } else {
                    matchingProfile.setBibMatchedDiscardOrAddOrUpdate(PROCESS_BIB_HOLDINGS_ITEMS);
                }
            } else if (key.equalsIgnoreCase(BIB_MATCHED_DISCARD_BIB)) {
                matchingProfile.setBibMatched_discardBib(Boolean.parseBoolean(value));
                if (matchingProfile.isBibMatched_discardBib()) {
                    matchingProfile.setBibMatchedDiscardOrUpdate(BIB_MATCHED_DISCARD_BIB);
                }
            } else if (key.equalsIgnoreCase(BIB_MATCHED_UPDATE_BIB)) {
                matchingProfile.setBibMatched_updateBib(Boolean.parseBoolean(value));
                if (matchingProfile.isBibMatched_updateBib()) {
                    matchingProfile.setBibMatchedDiscardOrUpdate(BIB_MATCHED_UPDATE_BIB);
                }
            } else if (key.equalsIgnoreCase(MATCH_HOLDINGS)) {
                matchingProfile.setMatchHoldings(Boolean.parseBoolean(value));
                if (matchingProfile.isMatchHoldings()) {
                    matchingProfile.setMatchIncomingHoldings(PERFORM_HOLDING_MATCH);
                } else {
                    matchingProfile.setMatchIncomingHoldings(NOT_PERFORM_HOLDING_MATCH);
                }
            } else if (key.equalsIgnoreCase(NO_MATCH_HOLDINGS_DISCARD_HOLDINGS_ITEMS)) {
                matchingProfile.setNoMatchHoldings_discardHoldingsItems(Boolean.parseBoolean(value));
                if (matchingProfile.isNoMatchHoldings_discardHoldingsItems()) {
                    matchingProfile.setDiscardDeleteKeepHoldingItem(NO_MATCH_HOLDINGS_DISCARD_HOLDINGS_ITEMS);
                }
            } else if (key.equalsIgnoreCase(NO_MATCH_HOLDINGS_DELETE_ADD_HOLDINGS_ITEMS)) {
                matchingProfile.setNoMatchHoldings_deleteAddHoldingsItems(Boolean.parseBoolean(value));
                if (matchingProfile.isNoMatchHoldings_deleteAddHoldingsItems()) {
                    matchingProfile.setDiscardDeleteKeepHoldingItem(NO_MATCH_HOLDINGS_DELETE_ADD_HOLDINGS_ITEMS);
                }
            } else if (key.equalsIgnoreCase(NO_MATCH_HOLDINGS_RETAIN_ADD_HOLDINGS_ITEMS)) {
                matchingProfile.setNoMatchHoldings_retainAddHoldingsItems(Boolean.parseBoolean(value));
                if (matchingProfile.isNoMatchHoldings_retainAddHoldingsItems()) {
                    matchingProfile.setDiscardDeleteKeepHoldingItem(NO_MATCH_HOLDINGS_RETAIN_ADD_HOLDINGS_ITEMS);
                }
            } else if (key.equalsIgnoreCase(HOLDINGS_NOT_MATCHED_DISCARD_HOLDINGS)) {
                matchingProfile.setHoldingsNotMatched_discardHoldings(Boolean.parseBoolean(value));
                if (matchingProfile.isHoldingsNotMatched_discardHoldings()) {
                    matchingProfile.setIncomingHoldingNotMatched(HOLDINGS_NOT_MATCHED_DISCARD_HOLDINGS);
                }

            } else if (key.equalsIgnoreCase(HOLDINGS_NOT_MATCHED_ADD_HOLDINGS)) {
                matchingProfile.setHoldingsNotMatched_addHoldings(Boolean.parseBoolean(value));
                if (matchingProfile.isHoldingsNotMatched_addHoldings()) {
                    matchingProfile.setIncomingHoldingNotMatched(HOLDINGS_NOT_MATCHED_ADD_HOLDINGS);
                }
            } else if (key.equalsIgnoreCase(HOLDINGS_NOT_MATCHED_ADD_ITEMS)) {
                matchingProfile.setHoldingsNotMatched_addItems(Boolean.parseBoolean(value));

            } else if (key.equalsIgnoreCase(HOLDINGS_MATCHED_ADD_HOLDINGS)) {
                matchingProfile.setHoldingsMatched_addHoldings(Boolean.parseBoolean(value));
                if (matchingProfile.isHoldingsMatched_addHoldings()) {
                    matchingProfile.setIncomingHoldingMatched(HOLDINGS_MATCHED_ADD_HOLDINGS);
                } else {
                    matchingProfile.setIncomingHoldingMatched(PROCESS_HOLDING_AND_ITEM);
                }
            } else if (key.equalsIgnoreCase(HOLDINGS_MATCHED_ADD_ITEMS)) {
                matchingProfile.setHoldingsMatched_addItems(Boolean.parseBoolean(value));

            } else if (key.equalsIgnoreCase(HOLDINGS_MATCHED_DISCARD_HOLDINGS)) {
                matchingProfile.setHoldingsMatched_discardHoldings(Boolean.parseBoolean(value));
                if (matchingProfile.isHoldingsMatched_discardHoldings()) {
                    matchingProfile.setIncomingHoldingMatchedProcessHolding(HOLDINGS_MATCHED_DISCARD_HOLDINGS);
                }
            } else if (key.equalsIgnoreCase(HOLDINGS_MATCHED_UPDATE_HOLDINGS)) {

                matchingProfile.setHoldingsMatched_updateHoldings(Boolean.parseBoolean(value));
                if (matchingProfile.isHoldingsMatched_updateHoldings()) {
                    matchingProfile.setIncomingHoldingMatchedProcessHolding(HOLDINGS_MATCHED_UPDATE_HOLDINGS);
                }
            } else if (key.equalsIgnoreCase(MATCH_ITEMS)) {
                matchingProfile.setMatchItems(Boolean.parseBoolean(value));
                if (matchingProfile.isMatchItems()) {
                    matchingProfile.setMatchIncomingItems(PERFORM_MATCHING_OF_ITEM);
                } else {
                    matchingProfile.setMatchIncomingItems(NOT_PERFORM_MATCHING_OF_ITEM);
                }
            } else if (key.equalsIgnoreCase(NO_MATCH_ITEM_DISCARD_ITEMS)) {
                matchingProfile.setNoMatchItem_discardItems(Boolean.parseBoolean(value));
                if (matchingProfile.isNoMatchItem_discardItems()) {
                    matchingProfile.setDiscardDeleteKeepItem(NO_MATCH_ITEM_DISCARD_ITEMS);
                }
            } else if (key.equalsIgnoreCase(NO_MATCH_ITEM_DELETE_ADD_ITEMS)) {
                matchingProfile.setNoMatchItem_deleteAddItems(Boolean.parseBoolean(value));
                if (matchingProfile.isNoMatchItem_deleteAddItems()) {
                    matchingProfile.setDiscardDeleteKeepItem(NO_MATCH_ITEM_DELETE_ADD_ITEMS);
                }
            } else if (key.equalsIgnoreCase(NO_MATCH_ITEM_RETAIN_ADD_ITEMS)) {
                matchingProfile.setNoMatchItem_retainAddItems(Boolean.parseBoolean(value));
                if (matchingProfile.isNoMatchItem_retainAddItems()) {
                    matchingProfile.setDiscardDeleteKeepItem(NO_MATCH_ITEM_RETAIN_ADD_ITEMS);
                }

            } else if (key.equalsIgnoreCase(ITEM_NOT_MATCHED_DISCARD_ITEM)) {
                matchingProfile.setItemNotMatched_discardItem(Boolean.parseBoolean(value));
                if (matchingProfile.isItemNotMatched_discardItem()) {
                    matchingProfile.setIncomingItemNotMatchedProcessItem(ITEM_NOT_MATCHED_DISCARD_ITEM);
                }
            } else if (key.equalsIgnoreCase(ITEM_NOT_MATCHED_ADD_ITEM)) {
                matchingProfile.setItemNotMatched_addItem(Boolean.parseBoolean(value));
                if (matchingProfile.isItemNotMatched_addItem()) {
                    matchingProfile.setIncomingItemNotMatchedProcessItem(ITEM_NOT_MATCHED_ADD_ITEM);
                }
            } else if (key.equalsIgnoreCase(ITEM_MATCHED_ADD_ITEM)) {
                matchingProfile.setItemMatched_addItem(Boolean.parseBoolean(value));
                if (matchingProfile.isItemMatched_addItem()) {
                    matchingProfile.setIncomingItemMatchedProcessItem(ITEM_MATCHED_ADD_ITEM);
                }
            } else if (key.equalsIgnoreCase(ITEM_MATCHED_UPDATE_ITEM)) {
                matchingProfile.setItemMatched_updateItem(Boolean.parseBoolean(value));
                if (matchingProfile.isItemMatched_updateItem()) {
                    matchingProfile.setIncomingItemMatchedProcessItem(ITEM_MATCHED_UPDATE_ITEM);
                }
            }
        }

        return matchingProfile;
    }


}
