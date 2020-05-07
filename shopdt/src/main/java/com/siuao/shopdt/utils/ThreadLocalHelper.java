package com.siuao.shopdt.utils;


import com.siuao.shopdt.request.RequestInfo;

public class ThreadLocalHelper {

	private static ThreadLocal<RequestInfo> loginInfoThreads = new ThreadLocal<RequestInfo>();
	
	
	public static void setRequestInfo(final RequestInfo loginInfo) {
		loginInfoThreads.set(loginInfo);
	}
	
	public static RequestInfo getRequestinfo() {
		return loginInfoThreads.get();
	}

	public static void removeRequesInfo() {
		loginInfoThreads.remove();
	}
	
}
