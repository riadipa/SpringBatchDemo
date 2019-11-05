package edu.ria.springbatch.controller;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoadController {

    @Autowired
    JobLauncher jobLauncher;

    //autowiring by name qualifier(beanName="csvJob")
    @Autowired
    Job csvJob;

    //autowiring by name qualifier(beanName="mongoDbJob")
    @Autowired
    Job mongoDbJob;

    @GetMapping(path = "/loadfilereader")
    public BatchStatus loadFile() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(csvJob, parameters);
        System.out.println("Reading data from CSV file reader");
        System.out.println("Job Execution: " + jobExecution.getStatus());
        System.out.println("Batch is running...");

        while (jobExecution.isRunning()) {
            System.out.println("....");
        }
        return jobExecution.getStatus();
    }

    @GetMapping(path = "/loadmongodb")
    public BatchStatus loadMongoDb() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(mongoDbJob, parameters);
        System.out.println("Reading data from Mongo database");
        System.out.println("Job Execution: " + jobExecution.getStatus());
        System.out.println("Batch is running...");

        while (jobExecution.isRunning()) {
            System.out.println("....");
        }
        return jobExecution.getStatus();
    }
}
