package com.siuao.shopdt.dao;

import com.siuao.shopdt.constant.ERole;
import com.siuao.shopdt.dao.common.IAbstractDao;
import com.siuao.shopdt.entity.Role;
import com.siuao.shopdt.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleDao extends IAbstractDao<Role, Long> {
    Optional<Role> findByName(ERole name);

}
