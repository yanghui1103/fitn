package com.bw.fit.log.service;

import org.aspectj.lang.ProceedingJoinPoint;

import com.bw.fit.common.model.CommonModel;

public interface LogService {

	public void log(ProceedingJoinPoint pj);
}
