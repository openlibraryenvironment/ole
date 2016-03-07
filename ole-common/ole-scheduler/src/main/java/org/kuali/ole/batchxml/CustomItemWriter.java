package org.kuali.ole.batchxml;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CustomItemWriter implements ItemWriter<Item> {

    @Override
    public void write(List<? extends Item> items) throws Exception {
        for (Item item : items) {
            System.out.println(item);
        }
    }
}