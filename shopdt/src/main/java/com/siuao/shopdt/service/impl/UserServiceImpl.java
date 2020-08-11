package com.siuao.shopdt.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.siuao.shopdt.constant.ERole;
import com.siuao.shopdt.dao.IRoleDao;
import com.siuao.shopdt.dao.IUserDao;
import com.siuao.shopdt.entity.Role;
import com.siuao.shopdt.entity.User;
import com.siuao.shopdt.paging.Paging;
import com.siuao.shopdt.request.UpdateUserRequest;
import com.siuao.shopdt.service.UserService;
import com.siuao.shopdt.vo.UserVO;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	IUserDao userDao;

	@Autowired
	IRoleDao roleDao;

	private <T extends Object, R extends Object> void setPagingResult(Paging<R> res, Page<T> data, Integer page,
			Integer size) {
		res.setPage(page);
		res.setPageSize(size);
		res.setTotalItem(data.getTotalElements());
		res.setTotalPage(data.getTotalPages());
	}

	protected UserVO populateUser(User entity) {
		UserVO vo = new UserVO();
		vo.setId(entity.getId());
		vo.setEmail(entity.getEmail());
		vo.setRole(entity.getRole());
		vo.setStatus(entity.getStatus());
		vo.setUsername(entity.getUsername());
		vo.setCreatedDate(entity.getCreatedDate());
		vo.setUpdatedDate(entity.getUpdatedDate());
		vo.setCreatedUser(entity.getCreatedUser());
		vo.setUpdatedUser(entity.getUpdatedUser());
		return vo;
	}

	@Override
	public Paging<UserVO> getAllUser(Integer page, Integer size) {
		Paging<UserVO> res = null;
		Pageable pageable = null;
		if (page != null && size != null) {
			pageable = PageRequest.of(page - 1, size, Sort.by("username").ascending());
		}
		Page<User> users = userDao.findAll(pageable);
		if (users != null && !CollectionUtils.isEmpty(users.getContent())) {
			res = new Paging<UserVO>();
			List<UserVO> userVOs = new LinkedList<UserVO>();
			for (User u : users.getContent()) {
				userVOs.add(populateUser(u));
			}
			res.setLstData(userVOs);
			setPagingResult(res, users, page, size);
		}
		return res;
	}

	@Override
	public UserVO getUserById(Long id) {
		UserVO res = null;
		User user = userDao.findById(id).orElse(null);
		if (user != null) {
			res = populateUser(user);
		}
		return res;
	}

	@Override
	@Transactional(rollbackFor = { Exception.class })
	public boolean deleteUser(String userActionName, Long userId) {
		User userAction = userDao.findByUsername(userActionName).orElse(null);
		User user = userDao.findById(userId).orElse(null);
		if (userAction != null) {
			if (userAction.getRole().getName().equals(ERole.ROLE_ADMIN)) {
				userDao.delete(user);
				return true;
			} else
				return false;
		}
		return false;
	}

	@Override
	public Boolean updateUser(String userActionName, UpdateUserRequest req) throws Exception {
		User userAction = userDao.findByUsername(userActionName).orElse(null);
		Optional<User> user = userDao.findById(req.getId());
		if (userAction != null && userAction.getRole().getName().equals(ERole.ROLE_ADMIN)) {
			if (user.isPresent()) {
				user.get().setEmail(req.getEmail());
				if (req.getRole() != null) {
					Role role = new Role();
					if (req.getRole().equals("admin")) {
						Role adminRole = roleDao.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						role = adminRole;
					} else {
						Role userRole = roleDao.findByName(ERole.ROLE_USER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						role = userRole;
					}
					user.get().setRole(role);
				}
				user.get().setStatus(req.getStatus());
				user.get().setUsername(req.getUsername());
				user.get().setUpdatedUser(userActionName);
				userDao.save(user.get());
				return true;
			} else {
				return false;
			}
		} else
			return false;
	}
}
