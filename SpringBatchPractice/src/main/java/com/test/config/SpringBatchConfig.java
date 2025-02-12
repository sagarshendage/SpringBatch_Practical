package com.test.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.test.entity.Person;
import com.test.repository.PersonRepository;

@Configuration
public class SpringBatchConfig {
	
	@Autowired
	private PersonRepository personRepository;
	
	@Bean
	public FlatFileItemReader<Person> reader() {
		final FlatFileItemReader<Person> reader = new FlatFileItemReader<Person>();
		reader.setName("personItemReader");
		reader.setResource(new ClassPathResource("people-1000.csv"));
		reader.setLinesToSkip(1);
		reader.setStrict(false);
		reader.setLineMapper(lineMapper());
		return reader;
	}
	
	private LineMapper<Person> lineMapper() {
		final DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<Person>();
		final DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("id","userId","firstName","lastName","gender","email","phone","dob","jobTitle");
		
		BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Person.class);
		
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}
	
	@Bean
	PersonProcessor processor() {
		return new PersonProcessor();
	}
	
	public RepositoryItemWriter<Person> writer(){
		final RepositoryItemWriter<Person> writer = new RepositoryItemWriter<Person>();
		writer.setRepository(personRepository);
		writer.setMethodName("save");
		return writer;
	}
	
	@Bean
	public Job job(JobRepository jobRepository, Step step) {
		return new JobBuilder("importPerson", jobRepository)
				.start(step)
				.build();
	}
	
	@Bean
	public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("csv-import-step", jobRepository)
				.<Person, Person>chunk(10, transactionManager)
				.reader(reader())
				.writer(writer())
				.build();
	}

}
