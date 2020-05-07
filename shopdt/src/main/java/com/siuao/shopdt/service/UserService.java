package com.siuao.shopdt.service;

import com.siuao.shopdt.paging.Paging;
import com.siuao.shopdt.request.UpdateUserRequest;
import com.siuao.shopdt.vo.UserVO;

public interface UserService {
    /**
     * @param page
     * @param size
     * @return
     */
    Paging<UserVO> getAllUser(Integer page, Integer size);

    /**
     * @param id
     * @param id
     * @return
     */
    UserVO getUserById(Long id);

    /**
     *
     * @param userActionId
     * @param userId
     * @return
     */
    boolean deleteUser(Long userActionId, Long userId);

    /**
     *
     * @param req
     * @return
     */
    Boolean updateUser(UpdateUserRequest req) throws Exception;
    }
