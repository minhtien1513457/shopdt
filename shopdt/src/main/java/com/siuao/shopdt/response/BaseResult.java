package com.siuao.shopdt.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * @author nhadt
 *
 */

@JsonInclude(Include.NON_EMPTY)
public class BaseResult {

	private String message;

	private Integer code = null;

	private boolean success = false;

	public BaseResult() {
		super();
	}

	public BaseResult(boolean success) {
		super();
		this.success = success;
	}

	public BaseResult(Integer code, String error_msg) {
		super();
		this.code = code;
		this.message = error_msg;
	}
	
	public BaseResult(boolean success,Integer code, String error_msg) {
		super();
		this.success = success;
		this.code = code;
		this.message = error_msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer error_code) {
		this.code = error_code;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String errorMsg) {
		this.message = errorMsg;
	}

}
