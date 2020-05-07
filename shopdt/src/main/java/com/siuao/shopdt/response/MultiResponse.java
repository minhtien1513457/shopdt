package com.siuao.shopdt.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class MultiResponse<I> extends BaseResult {

	private List<I> data;

	public MultiResponse() {
		super();
	}

	public MultiResponse(boolean success) {
		super(success);
	}

}