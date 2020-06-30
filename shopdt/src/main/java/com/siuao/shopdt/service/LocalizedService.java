package com.siuao.shopdt.service;

import org.springframework.lang.Nullable;

public interface LocalizedService {

	/**
	 * 
	 * @param code
	 * @param lang
	 * @param arg
	 * @return
	 * @throws Exception
	 */
	 String getMessage(String code, String lang, @Nullable Object[] arg) throws Exception;
	 
	 /**
	  * 
	  * @param key
	  * @param lang
	  * @param args
	  * @return
	  * @throws Exception
	  */
	 String getMessageKey(String key, String lang, Object... args) throws Exception;
}
