package com.demo.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.demo.entity.ChitietSp;
import com.demo.repository.ChitietSpRepository;
import com.demo.service.ChitietSpService;

public class ChitietSpServiceImpl implements ChitietSpService{
	@Autowired
	private ChitietSpRepository chitietSpRepository;

	@Override
	public Iterable<ChitietSp> findAll() {
		return chitietSpRepository.findAll();
	}

	@Override
	public List<ChitietSp> search(String q) {
		return chitietSpRepository.findBymotaContaining(q);
	}

	@Override
	public ChitietSp findOne(int id) {
		return chitietSpRepository.findById(id).orElse(null);
	}

	@Override
	public void save(ChitietSp chitietSp) {
		chitietSpRepository.save(chitietSp);
		}

	@Override
	public void delete(int id) {
		chitietSpRepository.deleteById(id);		
	}

}
