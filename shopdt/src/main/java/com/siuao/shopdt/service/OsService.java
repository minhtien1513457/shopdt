package com.siuao.shopdt.service;

import com.siuao.shopdt.paging.Paging;
import com.siuao.shopdt.request.CreateOsRequest;
import com.siuao.shopdt.request.UpdateOsRequest;
import com.siuao.shopdt.vo.OsVO;
import com.siuao.shopdt.vo.TypeVO;

public interface OsService {
	/**
	 * @param page
	 * @param size
	 * @return
	 */
	Paging<OsVO> getAllOS(Integer page, Integer size);

	/**
	 * @param id
	 * @param id
	 * @return
	 */
	OsVO getOsById(Long id);

	/**
	 *
	 * @param userActionId
	 * @param userId
	 * @return
	 */
	boolean deleteOs(String userActionName, Long osId);

	/**
	 *
	 * @param req
	 * @return
	 */
	Boolean updateOs(String userActionName, UpdateOsRequest req) throws Exception;
	
	/**
	 * 
	 * @param userActionName
	 * @param req
	 * @return
	 * @throws Exception
	 */
	OsVO createOs(String userActionName, CreateOsRequest req) throws Exception;
}
