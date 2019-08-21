package com.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.demo.entity.LoaiSp;
import com.demo.entity.Sanpham;

public interface SanphamRepository extends CrudRepository<Sanpham, Integer>{
    List<Sanpham> findBytenContaining(String q);
}
