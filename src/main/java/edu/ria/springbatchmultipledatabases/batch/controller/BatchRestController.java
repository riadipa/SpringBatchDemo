package edu.ria.springbatchmultipledatabases.batch.controller;

import org.springframework.batch.core.*;
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
public class BatchRestController {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job csvJob;

    @Autowired
    Job sourceDbJob;

    @Autowired
    Job mongoDbJob;

    @GetMapping(path = "/loadsourcedb")
    public BatchStatus loadSourceDb() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(csvJob, parameters);
        System.out.println("Job Execution for reading data from CSV file to Source database: " + jobExecution.getStatus());
        System.out.println("Batch is running...");

        while (jobExecution.isRunning()) {
            System.out.println("....");
        }
        return jobExecution.getStatus();
    }

    @GetMapping(path = "/loadtargetdb")
    public BatchStatus loadTargetDb() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        Map<String, JobParameter> maps = new HashMap<>();
        maps.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters parameters = new JobParameters(maps);
        JobExecution jobExecution = jobLauncher.run(sourceDbJob, parameters);
        System.out.println("Job Execution for reading data from Source db to Target db: " + jobExecution.getStatus());
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
