package com.siuao.shopdt.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siuao.shopdt.utils.StringUtil;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseEntity{

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Map<String, Object> options = new HashMap<String, Object>();;

	public int size() {
		return options.size();
	}

	public Object get(Object key) {
		return options.get(key);
	}

	public Object put(String key, Object value) {
		return options.put(key, StringUtil.toString(value));
	}
	
	public Object put(String key, String value) {
		return options.put(key, value);
	}

	public Object remove(Object key) {
		return options.remove(key);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		for (String key : m.keySet()) {
			Object value = m.get(key);
			options.put(key, StringUtil.toString(value));
		}
	}
	
	public Collection<Object> values() {
		return options.values();
	}
	
	public Map<String, Object> getOptions() {
		return options;
	}
	
	@Override
	public String toString() {
		try {
			return new ObjectMapper().writeValueAsString(this);
		} catch (Exception e) {
			return "to_json_error:" + e.getMessage();
		}
	}
	
}