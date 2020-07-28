package com.siuao.shopdt.log.entity;

import com.siuao.shopdt.utils.StringUtil;

public class ResultEntity extends BaseEntity{

	private String id;
	
	private String status = "failed";
	
	private Object entity;
	
	private long elapsed;
	
	public ResultEntity() {
		super();
	}

	public ResultEntity(Object entity) {
		super();
		this.entity = entity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = StringUtil.toString(entity);
	}
	
	public void setEntity(String entity) {
		this.entity = entity;
	}

	public long getElapsed() {
		return elapsed;
	}

	public void setElapsed(long elapsed) {
		this.elapsed = elapsed;
	}

	public static ResultEntity create(){
		ResultEntity m = new ResultEntity();
		return m;
	}
	
	public static ResultEntity create(Object entity){
		ResultEntity m = new ResultEntity(entity);
		return m;
	}
	
}
