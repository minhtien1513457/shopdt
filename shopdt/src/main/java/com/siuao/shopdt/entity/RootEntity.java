package com.siuao.shopdt.entity;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.siuao.shopdt.request.RequestHelper;

public class RootEntity extends BaseEntity {
	
	public static String app_id_def;
	
	public static String app_name_def;

	private String app_id;
	
	private String app_name;
	
	private String event_type;

	private RequestEntity request;

	private LogEntity detail;

	private long start;

	private String exp_stacks;

	@JsonIgnore
	public Throwable exp;

	public RootEntity() {
		super();
		start = System.currentTimeMillis();
	}

	public RootEntity appId(String app_id) {
		this.app_id = app_id;
		return this;
	}

	public RootEntity type(String eventType) {
		this.event_type = eventType;
		return this;
	}

	public RootEntity reqId(String id) {
		req(true).setId(id);
		return this;
	}

	public RootEntity reqSid(String sid) {
		req(true).setSid(sid);
		return this;
	}

	public RootEntity reqProp(String key, Object value) {
		req(true).put(key, value);
		return this;
	}

	public RootEntity reqUser(String user) {
		req(true).setUser(user);
		return this;
	}

	public RootEntity reqServiceName(String service_name) {
		req(true).setService_name(service_name);
		return this;
	}

	public RootEntity reqUri(String uri) {
		req(true).setUri(uri);
		return this;
	}

	public RootEntity detailResultId(String rsId) {
		detail(true).resultId(rsId);
		return this;
	}

	public RootEntity detailResultStatus(String status) {
		detail(true).resultStatus(status);
		return this;
	}

	public RootEntity success() {
		detail(true).resultStatus("success");
		return this;
	}

	public RootEntity success(Object entity) {
		detail(true).resultStatus("success").resultEntity(entity);
		return this;
	}

	public RootEntity success(String entity) {
		detail(true).resultStatus("success").resultEntity(entity);
		return this;
	}

	public RootEntity fail() {
		detail(true).resultStatus("failed");
		return this;
	}

	public RootEntity fail(Object entity) {
		detail(true).resultStatus("failed").resultEntity(entity);
		return this;
	}

	public RootEntity fail(String entity) {
		detail(true).resultStatus("failed").resultEntity(entity);
		return this;
	}

	public RootEntity detailResultSucess() {
		detail(true).resultStatus("success");
		return this;
	}

	public RootEntity detailResultFail() {
		detail(true).resultStatus("failed");
		return this;
	}

	public RootEntity detailResultEntity(Object entity) {
		detail(true).resultEntity(entity);
		return this;
	}

	public RootEntity detailResultEntity(String entity) {
		detail(true).resultEntity(entity);
		return this;
	}

	public RootEntity detailResult(ResultEntity result) {
		detail(true).result(result);
		return this;
	}

	public RootEntity detailResultOpts(String key, Object value) {
		detail(true).resultOpts(key, value);
		return this;
	}

	public RootEntity detailResultOpts(String key, String value) {
		detail(true).resultOpts(key, value);
		return this;
	}

	public RootEntity request(RequestEntity request) {
		this.request = request;
		return this;
	}

	public RootEntity detail(LogEntity detail) {
		this.detail = detail;
		return this;
	}

	public RootEntity exp(Throwable exp) {
		this.exp = exp;
		exp_stacks += ExceptionUtils.getStackTrace(exp) + "\n";
		return detailResultStatus("error:" + exp.getMessage());
	}

	public String getApp_id() {
		return app_id == null? app_id_def : app_id;
	}

	public String getApp_name() {
		return app_name == null? app_name_def: app_name;
	}

	public String getEvent_type() {
		return event_type;
	}

	@JsonProperty("req")
	public RequestEntity getRequest() {
		return request;
	}

	@JsonProperty("rsp")
	public LogEntity getDetail() {
		return detail;
	}

	public String getExp_stacks() {
		return exp_stacks;
	}

	public void elapsed(){
		// in seconds
		long elapsed = (System.currentTimeMillis() - start);
		detail(true).getResult(true).setElapsed(elapsed);
	}

	public static RootEntity create(){
		RootEntity m = new RootEntity();
		return m;
	}

	public static RootEntity createWithReqId(){
		RootEntity m = new RootEntity();
		m.req(true).setId(RequestHelper.INSTANCE.genRequestId());
		return m;
	}

	private RequestEntity req(boolean force) {
		if(request == null && force){
			request = new RequestEntity();
		}
		return request;
	}

	private LogEntity detail(boolean force){
		if(detail == null && force){
			detail = new LogEntity();
		}
		return detail;
	}
	
}
