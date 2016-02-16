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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@EnableBatchProcessing
@EnableScheduling
public class BatchConfiguration {
    
    @Bean 
    public DummyTask dummyTask() {
        return new DummyTask();
    }
    
    @Bean
    public ConcurrentTaskScheduler springScheduler() {
        return new ConcurrentTaskScheduler();        
    }
    
    @Bean
    public SchedulerFactoryBean quartzScheduler() {
        return new SchedulerFactoryBean();
    }

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
    public DummyScheduler scheduler() {
        return new DummyScheduler();
    }

    @Bean
    public ItemReader<Dummy> reader() throws Exception {
        FlatFileItemReader<Dummy> reader = new FlatFileItemReader<Dummy>();
        reader.setResource(new ClassPathResource("dummies.csv", getClass()));
        reader.setLineMapper(new DefaultLineMapper<Dummy>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(new String[] {"property"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Dummy>() {{
                setTargetType(Dummy.class);
            }});
        }});        
        return reader;
    }

    @Bean
    public ItemProcessor<Dummy, Dummy> processor() {
        return new DummyProcessor();
    }

    @Bean
    public ItemWriter<Dummy> writer() {
        DummyWriter writer = new DummyWriter();
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
                .allowStartIfComplete(true)
                .<Dummy, Dummy> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}