package com.siuao.shopdt.dao.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface IAbstractDao<T, ID extends Serializable> extends JpaRepository<T, ID>{
}
