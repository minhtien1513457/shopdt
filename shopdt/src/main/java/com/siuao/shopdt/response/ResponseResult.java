package com.siuao.shopdt.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseResult<T> extends BaseResult {
	private T data;
	
	public ResponseResult() {
		super();
	}
	
	public ResponseResult(T obj) {
		super();
		this.data = obj;
	}
	
	public ResponseResult(boolean success) {
		super(success);
	}

	public ResponseResult(String errorMsg, int errorCode) {
		super(errorCode, errorMsg);
	}
}
