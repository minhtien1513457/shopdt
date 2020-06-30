package com.siuao.shopdt.entity;

import com.siuao.shopdt.utils.StringUtil;

public class LogEntity extends BaseEntity {

	private ResultEntity result;

	public LogEntity resultId(String id){
		rs(true).setId(id);
		return this;
	}

	public LogEntity resultStatus(String status){
		rs(true).setStatus(status);
		return this;
	}

	public LogEntity resultEntity(Object entity){
		/*
		// String or primary type should be wrapped as object so that it can be parse at ELK
		if(entity instanceof String || (entity != null && WrapperType.isWrapperType(entity.getClass()))){
			entity = new WrapperEntity(entity);
		}
		rs(true).setEntity(entity);
		return this;
		*/
		return resultEntity(StringUtil.toString(entity));
	}

	public LogEntity resultEntity(String entity){
		rs(true).setEntity(entity);
		return this;
	}

	public LogEntity resultOpts(String key, Object value){
		/*
		rs(true).put(key, value);
		return this;
		*/
		return resultOpts(key, StringUtil.toString(value));
	}

	public LogEntity resultOpts(String key, String value){
		rs(true).put(key, value);
		return this;
	}

	public LogEntity result(ResultEntity result){
		this.result = result;
		return this;
	}

	public ResultEntity getResult() {
		return result;
	}

	public ResultEntity getResult(boolean force) {
		return rs(force);
	}

	private ResultEntity rs(boolean force){
		if(result == null && force){
			result = new ResultEntity();
		}
		return result;
	}
	
}
