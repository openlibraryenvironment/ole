package org.kuali.ole.batch;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.item.support.ListItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Resource
    private List<Dummy> dummyList;

    @Bean
    public List<Dummy> dummyList() {
        List<Dummy> dummyList = new ArrayList<Dummy>();
        dummyList.add(new Dummy(2));
        dummyList.add(new Dummy(4));
        dummyList.add(new Dummy(8));
        return dummyList;
    }

    @Bean
    public ItemReader<Dummy> reader() {
        ListItemReader<Dummy> reader = new ListItemReader<Dummy>(dummyList);
        return reader;
    }

    @Bean
    public ItemProcessor<Dummy, Dummy> processor() {
        return new DummyProcessor();
    }

    @Bean
    public ItemWriter<Dummy> writer() {
        ListItemWriter<Dummy> writer = new ListItemWriter<Dummy>();
        return writer;
    }

    @Bean
    public Job dummyJob(JobBuilderFactory jobs, Step s1) {
        return jobs.get("dummy")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Dummy> reader, ItemWriter<Dummy> writer,
            ItemProcessor<Dummy, Dummy> processor) {
        return stepBuilderFactory.get("step1")
                .<Dummy, Dummy> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}