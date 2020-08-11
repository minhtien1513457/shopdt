package com.siuao.shopdt.dao;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.siuao.shopdt.dao.common.IAbstractDao;
import com.siuao.shopdt.entity.OsEntity;

@Repository
public interface IOsDao extends IAbstractDao<OsEntity, Long> {
    Optional<OsEntity> findByName(String name);
}
