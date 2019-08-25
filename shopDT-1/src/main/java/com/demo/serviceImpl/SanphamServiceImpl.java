package com.demo.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.entity.Sanpham;
import com.demo.repository.SanphamRepository;
import com.demo.service.SanphamService;

@Service
public class SanphamServiceImpl implements SanphamService {

	@Autowired
	private SanphamRepository sanphamRepository;
	
	@Override
	public Iterable<Sanpham> findAll() {
		return sanphamRepository.findAll();
	}

	@Override
	public List<Sanpham> search(String q) {
		return sanphamRepository.findBytenContaining(q);
	}

	@Override
	public Sanpham findOne(int id) {
		return sanphamRepository.findById(id).orElse(null);
	}

	@Override
	public void save(Sanpham sanpham) {
		sanphamRepository.save(sanpham);
		Sanpham x =new Sanpham();
	}

	@Override
	public void delete(int id) {
		sanphamRepository.deleteById(id);
	}

}
