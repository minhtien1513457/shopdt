package com.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.demo.entity.LoaiSp;

public interface LoaiSpRepository extends CrudRepository<LoaiSp, Integer> {

    List<LoaiSp> findBytenContaining(String q);

}
