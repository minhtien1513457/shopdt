package com.demo.serviceImpl;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.entity.LoaiSp;
import com.demo.repository.LoaiSpRepository;
import com.demo.service.LoaiSpService;

@Service
public class LoaiSpServiceImpl implements LoaiSpService{

	@Autowired
    private LoaiSpRepository loaiSpRepository;

	@Override
	public Iterable<LoaiSp> findAll() {
		return loaiSpRepository.findAll();
	}

	@Override
	public List<LoaiSp> search(String q) {
		return loaiSpRepository.findBytenContaining(q);
	}

	@Override
	public LoaiSp findOne(int id) {
	return loaiSpRepository.findById(id).orElse(null);
	}

	@Override
	public void save(LoaiSp loaiSp) {
		loaiSpRepository.save(loaiSp);
	}

	@Override
	public void delete(int id) {
		loaiSpRepository.deleteById(id);
	}

}
