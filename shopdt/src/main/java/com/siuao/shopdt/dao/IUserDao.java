package com.siuao.shopdt.dao;

import com.siuao.shopdt.dao.common.IAbstractDao;
import com.siuao.shopdt.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserDao extends IAbstractDao<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
