package com.siuao.shopdt.request;

public class RequestHelper {

	public static final RequestHelper INSTANCE = new RequestHelper();
	
	private RequestIdGenerator generator;
	
	private RequestHelper() {
		super();
	}

	public String genRequestId() throws RuntimeException{
		if(generator == null){
			throw new RuntimeException("Please initialize request generator");
		}
		return generator.generate();
	}
	
	public void useGenerator(RequestIdGenerator gen){
		this.generator = gen;
	}
	
}
