package com.siuao.shopdt.service;

import com.siuao.shopdt.paging.Paging;
import com.siuao.shopdt.request.CreateTypeRequest;
import com.siuao.shopdt.request.UpdateTypeRequest;
import com.siuao.shopdt.vo.TypeVO;

public interface TypeService {
	/**
	 * @param page
	 * @param size
	 * @return
	 */
	Paging<TypeVO> getAllType(Integer page, Integer size);

	/**
	 * @param id
	 * @param id
	 * @return
	 */
	TypeVO getTypeById(Long id);

	/**
	 * 
	 * @param name
	 * @return
	 */
	TypeVO getTypeByName(String name);

	/**
	 *
	 * @param userActionId
	 * @param userId
	 * @return
	 */
	boolean deleteType(String userActionName, Long id);

	/**
	 *
	 * @param req
	 * @return
	 */
	Boolean updateType(String userActionName, UpdateTypeRequest req) throws Exception;
	
	/**
	 *
	 * @param req
	 * @return
	 */
	TypeVO createType(String userActionName, CreateTypeRequest req) throws Exception;
}
