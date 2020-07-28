package com.siuao.shopdt.log.entity;

import java.util.Date;

public class RequestEntity extends BaseEntity {

	private String id;
	
	private String sid;
	
	private String user;
	
	private String service_name;
	
	private String uri;
	
	private String time = new Date().toString();
	
	public RequestEntity() {
		super();
	}

	public RequestEntity(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getService_name() {
		return service_name;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
