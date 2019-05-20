package com.pactera.batch.config.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

public class MyDecider implements JobExecutionDecider{

	private int count;
	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		// TODO Auto-generated method stub
		count++;
		if(count%2 == 0 ) {
			return new FlowExecutionStatus("even");
		}else {
			return new FlowExecutionStatus("odd");
		}
	}

}
