package com.siuao.shopdt.service.impl;

import com.siuao.shopdt.response.BaseResult;
import com.siuao.shopdt.service.ErrorMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ErrorMessageServiceImpl implements ErrorMessageService {

	private static Logger logger = LoggerFactory.getLogger(ErrorMessageServiceImpl.class);

	@Override
	public <T extends BaseResult> ResponseEntity<T> processException(Exception exp, T response) {
		try {
			// Logs exception.
			logger.error("*********** Shopdt-api ERROR: ", exp);
			exp.printStackTrace();
			response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			response.setMessage(exp.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} catch (Throwable e) {
			logger.error("!!! Error occurred in handling for exception.", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
