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
import org.springframework.util.CollectionUtils;

import com.siuao.shopdt.constant.ERole;
import com.siuao.shopdt.dao.IOsDao;
import com.siuao.shopdt.dao.IUserDao;
import com.siuao.shopdt.entity.OsEntity;
import com.siuao.shopdt.entity.User;
import com.siuao.shopdt.paging.Paging;
import com.siuao.shopdt.request.UpdateOsRequest;
import com.siuao.shopdt.service.OsService;
import com.siuao.shopdt.vo.OsVO;

@Service
public class OsServiceImpl implements OsService{
	@Autowired
	private IOsDao osDao;
	
	@Autowired
	private IUserDao userDao;
	
	private <T extends Object, R extends Object> void setPagingResult(Paging<R> res, Page<T> data, Integer page,
			Integer size) {
		res.setPage(page);
		res.setPageSize(size);
		res.setTotalItem(data.getTotalElements());
		res.setTotalPage(data.getTotalPages());
	}
	
	protected OsVO populateOs(OsEntity entity) {
		OsVO vo = new OsVO();
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
	public Paging<OsVO> getAllOS(Integer page, Integer size) {
		Paging<OsVO> res = null;
		Pageable pageable = null;
		if (page != null && size != null) {
			pageable = PageRequest.of(page - 1, size, Sort.by("name").ascending());
		}
		Page<OsEntity> oss = osDao.findAll(pageable);
		if (oss != null && !CollectionUtils.isEmpty(oss.getContent())) {
			res = new Paging<OsVO>();
			List<OsVO> osVOs = new LinkedList<OsVO>();
			for (OsEntity o : oss.getContent()) {
				osVOs.add(populateOs(o));
			}
			res.setLstData(osVOs);
			setPagingResult(res, oss, page, size);
		}
		return res;
	}

	@Override
	public OsVO getOsById(Long id) {
		OsVO res = null;
		OsEntity os = osDao.findById(id).orElse(null);
		if (os != null) {
			res = populateOs(os);
		}
		return res;
	}

	@Override
	public boolean deleteOs(String userActionName, Long osId) {
		User userAction = userDao.findByUsername(userActionName).orElse(null);
		OsEntity os = osDao.findById(osId).orElse(null);
		if (userAction != null) {
			if (userAction.getRole().getName().equals(ERole.ROLE_ADMIN)) {
				osDao.delete(os);
				return true;
			} else
				return false;
		}
		return false;
	}

	@Override
	public Boolean updateOs(String userActionName, UpdateOsRequest req) throws Exception {
		User userAction = userDao.findByUsername(userActionName).orElse(null);
		Optional<OsEntity> os = osDao.findById(req.getId());
		if (userAction != null && userAction.getRole().getName().equals(ERole.ROLE_ADMIN)) {
			if (os.isPresent()) {
				os.get().setName(req.getName());
				os.get().setDescription(req.getDescription());
				os.get().setUpdatedUser(userActionName);
				osDao.save(os.get());
				return true;
			} else {
				return false;
			}
		} else
			return false;
	}

}
