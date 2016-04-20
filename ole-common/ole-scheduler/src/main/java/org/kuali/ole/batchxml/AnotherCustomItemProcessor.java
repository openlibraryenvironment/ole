package org.kuali.ole.batchxml;

import org.springframework.batch.item.ItemProcessor;

public class AnotherCustomItemProcessor implements ItemProcessor<Item, Item> {

    @Override
    public Item process(Item item) throws Exception {
        item.setProperty(item.getProperty().substring(2, item.getProperty().length()) + item.getProperty().substring(0, 2));
        return item;
    }
}