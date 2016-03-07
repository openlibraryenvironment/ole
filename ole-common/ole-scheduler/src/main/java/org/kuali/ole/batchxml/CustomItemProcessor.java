package org.kuali.ole.batchxml;

import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Item, Item> {

    @Override
    public Item process(Item item) throws Exception {
        item.setProperty("I am a " + item.getProperty());
        return item;
    }
}