package org.kuali.ole.batch.bo;

import org.kuali.ole.docstore.common.document.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jayabharathreddy
 * Date: 7/2/14
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class OLEBatchBibImportBo {


    List<BibTree> bibTreesToCreate = new ArrayList<>();
    List<Bib> bibsToUpdate = new ArrayList<>();
    List<HoldingsTree> holdingsTreesToCreate = new ArrayList<>();
    List<Holdings> holdingsToUpdate = new ArrayList<>();
    List<Item> itemsToCreate = new ArrayList<>();
    List<Item> itemsToUpdate = new ArrayList<>();
    List<String> holdingsIdsToBeDeleted = new ArrayList<>();
    List<String> itemIdsToBeDeleted = new ArrayList<>();

    public List<BibTree> getBibTreesToCreate() {
        return bibTreesToCreate;
    }

    public List<Bib> getBibsToUpdate() {
        return bibsToUpdate;
    }

    public List<HoldingsTree> getHoldingsTreesToCreate() {
        return holdingsTreesToCreate;
    }

    public List<Holdings> getHoldingsToUpdate() {
        return holdingsToUpdate;
    }

    public List<Item> getItemsToCreate() {
        return itemsToCreate;
    }

    public List<Item> getItemsToUpdate() {
        return itemsToUpdate;
    }

    public List<String> getHoldingsIdsToBeDeleted() {
        return holdingsIdsToBeDeleted;
    }

    public List<String> getItemIdsToBeDeleted() {
        return itemIdsToBeDeleted;
    }
}
