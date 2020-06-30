package com.siuao.shopdt.request;

public class SimpleRequestIdGenerator implements RequestIdGenerator {

	private String servicePrefix;
	
	private String serviceId;
	
	public SimpleRequestIdGenerator(String servicePrefix, String serviceId) {
		super();
		this.servicePrefix = servicePrefix;
		this.serviceId = serviceId;
	}

	@Override
	public String generate(){
		String rid = String.format("%s-%s-%s", servicePrefix, serviceId, System.currentTimeMillis());
//		return Base64.getEncoder().encodeToString(rid.getBytes());
		return rid;
	}

	public String getServicePrefix() {
		return servicePrefix;
	}

	public void setServicePrefix(String servicePrefix) {
		this.servicePrefix = servicePrefix;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

}
