package com.siuao.shopdt.paging;

import com.siuao.shopdt.response.BaseResult;

import java.util.List;

public class Paging<T> extends BaseResult {
	private List<T> lstData;
	private Integer page;
	private Integer pageSize;
	private Integer totalPage;
	private Long totalItem;
	
	public List<T> getLstData() {
		return lstData;
	}
	public void setLstData(List<T> lstData) {
		this.lstData = lstData;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public Long getTotalItem() {
		return totalItem;
	}
	public void setTotalItem(Long totalItem) {
		this.totalItem = totalItem;
	}

	
}
