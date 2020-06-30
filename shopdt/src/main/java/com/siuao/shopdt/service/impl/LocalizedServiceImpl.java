package com.siuao.shopdt.service.impl;

import com.siuao.shopdt.service.LocalizedService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Locale;


@Service
public class LocalizedServiceImpl implements LocalizedService {
	@Autowired
    private MessageSource messageSource;

    private MessageSourceAccessor accessor;

	@Override
	public String getMessage(String code, String lang, @Nullable Object[] arg) throws Exception {
		String language = "en";
		if (StringUtils.isNotEmpty(lang)) {
			language = lang;
		}
        accessor = new MessageSourceAccessor(messageSource, new Locale(language));
        return accessor.getMessage(code, arg, new Locale(lang));
	}

	@Override
	public String getMessageKey(String key, String lang, Object... args) throws Exception {
		String language = "en";
		if (StringUtils.isNotEmpty(lang)) {
			language = lang;
		}
		accessor = new MessageSourceAccessor(messageSource, new Locale(language));
		return accessor.getMessage(key, args, new Locale(lang));
	}
	
	

}
