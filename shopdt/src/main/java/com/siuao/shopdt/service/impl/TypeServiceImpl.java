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
import com.siuao.shopdt.dao.ITypeDao;
import com.siuao.shopdt.dao.IUserDao;
import com.siuao.shopdt.entity.TypeEntity;
import com.siuao.shopdt.entity.User;
import com.siuao.shopdt.paging.Paging;
import com.siuao.shopdt.request.CreateTypeRequest;
import com.siuao.shopdt.request.UpdateTypeRequest;
import com.siuao.shopdt.service.TypeService;
import com.siuao.shopdt.vo.TypeVO;

@Service
public class TypeServiceImpl implements TypeService{
	@Autowired
	private ITypeDao typeDao;
	
	@Autowired
	private IUserDao userDao;
	
	private <T extends Object, R extends Object> void setPagingResult(Paging<R> res, Page<T> data, Integer page,
			Integer size) {
		res.setPage(page);
		res.setPageSize(size);
		res.setTotalItem(data.getTotalElements());
		res.setTotalPage(data.getTotalPages());
	}
	
	protected TypeVO populateType(TypeEntity entity) {
		TypeVO vo = new TypeVO();
		vo.setId(entity.getId());
		vo.setName(entity.getName());
		vo.setDescription(entity.getDescription());
		vo.setProducts(entity.getProducts());
		vo.setCreatedDate(entity.getCreatedDate());
		vo.setUpdatedDate(entity.getUpdatedDate());
		vo.setCreatedUser(entity.getCreatedUser());
		vo.setUpdatedUser(entity.getUpdatedUser());
		return vo;
	}
	
	@Override
	public Paging<TypeVO> getAllType(Integer page, Integer size) {
		Paging<TypeVO> res = null;
		Pageable pageable = null;
		if (page != null && size != null) {
			pageable = PageRequest.of(page - 1, size, Sort.by("name").ascending());
		}
		Page<TypeEntity> types = typeDao.findAll(pageable);
		if (types != null && !CollectionUtils.isEmpty(types.getContent())) {
			res = new Paging<TypeVO>();
			List<TypeVO> typeVOs = new LinkedList<TypeVO>();
			for (TypeEntity o : types.getContent()) {
				typeVOs.add(populateType(o));
			}
			res.setLstData(typeVOs);
			setPagingResult(res, types, page, size);
		}
		return res;
	}

	@Override
	public TypeVO getTypeById(Long id) {
		TypeVO res = null;
		TypeEntity type = typeDao.findById(id).orElse(null);
		if (type != null) {
			res = populateType(type);
		}
		return res;
	}

	@Override
	public boolean deleteType(String userActionName, Long id) {
		User userAction = userDao.findByUsername(userActionName).orElse(null);
		TypeEntity type = typeDao.findById(id).orElse(null);
		if (userAction != null) {
			if (userAction.getRole().getName().equals(ERole.ROLE_ADMIN)) {
				typeDao.delete(type);
				return true;
			} else
				return false;
		}
		return false;
	}

	@Override
    @Transactional(rollbackFor={Exception.class})
	public Boolean updateType(String userActionName, UpdateTypeRequest req) throws Exception {
		User userAction = userDao.findByUsername(userActionName).orElse(null);
		Optional<TypeEntity> type = typeDao.findById(req.getId());
		if (userAction != null && userAction.getRole().getName().equals(ERole.ROLE_ADMIN)) {
			if (type.isPresent()) {
				type.get().setName(req.getName());
				type.get().setDescription(req.getDescription());
				type.get().setUpdatedUser(userActionName);
				typeDao.save(type.get());
				return true;
			} else {
				return false;
			}
		} else
			return false;
	}

	@Override
	public TypeVO getTypeByName(String name) {
		TypeVO res = null;
		TypeEntity type = typeDao.findByName(name).orElse(null);
		if (type != null) {
			res = populateType(type);
		}
		return res;
	}

	@Override
    @Transactional(rollbackFor={Exception.class})
	public TypeVO createType(String userActionName, CreateTypeRequest req) throws Exception {
		TypeVO res = null;
		User userAction = userDao.findByUsername(userActionName).orElse(null);
		if (userAction != null && userAction.getRole().getName().equals(ERole.ROLE_ADMIN)) {
			TypeEntity type = new TypeEntity();
			type.setName(req.getName());
			type.setDescription(req.getDescription());
			type.setCreatedUser(userActionName);
			typeDao.save(type);
			res = this.populateType(type);
		}
		return res;
	}

}
