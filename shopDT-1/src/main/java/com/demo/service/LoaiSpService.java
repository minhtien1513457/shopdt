package com.demo.service;

import java.util.List;

import com.demo.entity.LoaiSp;

public interface LoaiSpService {
	Iterable<LoaiSp> findAll();

	List<LoaiSp> search(String q);

	LoaiSp findOne(int id);

	void save(LoaiSp loaiSp);

	void delete(int id);
}
