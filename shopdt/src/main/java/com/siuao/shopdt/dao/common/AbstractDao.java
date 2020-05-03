package com.siuao.shopdt.dao.common;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class AbstractDao<T, ID extends Serializable> extends SimpleJpaRepository<T, ID>  implements IAbstractDao<T, ID> {

	public AbstractDao(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		// TODO Auto-generated constructor stub
	}
}

