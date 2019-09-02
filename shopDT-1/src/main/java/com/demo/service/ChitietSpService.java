package com.demo.service;

import java.util.List;

import com.demo.entity.ChitietSp;

public interface ChitietSpService {
	Iterable<ChitietSp> findAll();

	List<ChitietSp> search(String q);

	ChitietSp findOne(int id);

    void save(ChitietSp chitietSp);

	void delete(int id);
}
