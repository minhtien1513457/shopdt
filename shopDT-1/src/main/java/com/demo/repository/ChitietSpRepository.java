package com.demo.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import com.demo.entity.ChitietSp;

public interface ChitietSpRepository extends CrudRepository<ChitietSp, Integer>{
    List<ChitietSp> findBymotaContaining(String q);
}