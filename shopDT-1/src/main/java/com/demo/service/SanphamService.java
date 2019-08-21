package com.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.demo.entity.Sanpham;

public interface SanphamService {
	Iterable<Sanpham> findAll();

	List<Sanpham> search(String q);

	Sanpham findOne(int id);

	void save(Sanpham sanpham);

	void delete(int id);
}
