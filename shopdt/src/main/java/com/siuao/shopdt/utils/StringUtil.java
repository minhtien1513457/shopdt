package com.siuao.shopdt.utils;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

public class StringUtil {

	public static String toString(Object obj) {
		if (obj == null) {
			return "NULL";
		} else if (obj instanceof String || obj instanceof Date) {
			return obj.toString();
		} else if (WrapperType.isWrapperType(obj.getClass())) {
			return obj.toString();
		} else {
			// is object type
			return ToStringBuilder.reflectionToString(obj, LogSetting.def_style);
		}
	}

}
