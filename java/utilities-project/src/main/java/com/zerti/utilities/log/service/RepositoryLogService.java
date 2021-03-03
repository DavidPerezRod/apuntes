package com.zerti.utilities.log.service;

import com.zerti.utilities.log.repository.LogRepository;
import com.zerti.utilities.log.vo.ExecutedMethodLogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryLogService {


	private final LogRepository logRepository;

	@Autowired
	public RepositoryLogService(final LogRepository logRepository){
		this.logRepository=logRepository;
	}

	public void register(ExecutedMethodLogInfo logInfo){
		logRepository.insert(LogToJSonObjectMapper.logToRepoInfo(logInfo));
	}
}
