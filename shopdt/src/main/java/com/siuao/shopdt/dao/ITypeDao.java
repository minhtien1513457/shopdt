package com.siuao.shopdt.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.siuao.shopdt.dao.common.IAbstractDao;
import com.siuao.shopdt.entity.TypeEntity;

@Repository
public interface ITypeDao extends IAbstractDao<TypeEntity, Long> {
    Optional<TypeEntity> findByName(String name);
}
