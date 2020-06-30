package com.siuao.shopdt.service;

import com.siuao.shopdt.response.BaseResult;
import org.springframework.http.ResponseEntity;


public interface ErrorMessageService {
	<T extends BaseResult> ResponseEntity<T> processException(Exception exp, T response);
}
