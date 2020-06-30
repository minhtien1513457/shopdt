package com.siuao.shopdt.utils;

import java.util.HashSet;
import java.util.Set;

public class WrapperType {

	private static final Set<Class<?>> WRAPPER_TYPES = getWrapperTypes();

	private static Set<Class<?>> getWrapperTypes() {
		Set<Class<?>> ret = new HashSet<Class<?>>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Void.class);
		return ret;
	}
	
	public static boolean isWrapperType(Class<?> clazz) {
		return WRAPPER_TYPES.contains(clazz);
	}
	
	public static void main(String[] args) {
		System.out.println(isWrapperType(String.class));
		System.out.println(isWrapperType(Integer.class));
	}
	
}