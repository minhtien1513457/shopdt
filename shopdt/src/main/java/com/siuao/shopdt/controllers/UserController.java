package com.siuao.shopdt.controllers;

import com.siuao.shopdt.entity.RootEntity;
import com.siuao.shopdt.paging.Paging;
import com.siuao.shopdt.request.RequestInfo;
import com.siuao.shopdt.response.ResponseResult;
import com.siuao.shopdt.service.ErrorMessageService;
import com.siuao.shopdt.service.LocalizedService;
import com.siuao.shopdt.service.UserService;
import com.siuao.shopdt.utils.LogUtil;
import com.siuao.shopdt.utils.ThreadLocalHelper;
import com.siuao.shopdt.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.ws.rs.core.Context;

import javax.servlet.http.HttpServletRequest;
import static net.logstash.logback.marker.Markers.append;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    protected UserService userService;

    @Autowired
    protected ErrorMessageService errorMessageService;

    @Autowired
    protected LocalizedService localizedService;

    @GetMapping(value = "")
    public ResponseEntity<ResponseResult<UserVO>> getUserId(@Context HttpServletRequest request,
                                                           @RequestParam(value = "id", required = false) Long id) throws Exception {
        ResponseResult<UserVO> response = new ResponseResult<UserVO>();
        RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();
        RootEntity log = RootEntity.create();
        try {
            log.reqUri(request.getRequestURI()).type("UserController.getUserId").reqUser(reqInfo.getUsername());
            response.setData(userService.getUserById(id));
            response.setSuccess(true);
            log.success();
        } catch (Exception e) {
            log.exp(e).fail();
            return errorMessageService.processException(e, response);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(append("metric", log), log.toString());
        }
        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/list")
    public ResponseEntity<Paging<UserVO>> getAllUser(@Context HttpServletRequest request,
                                                   @RequestParam(value = "page", required = false) Integer page,
                                                   @RequestParam(value = "size", required = false) Integer size) throws Exception {
        Paging<UserVO> response = new Paging<UserVO>();
        RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();
        RootEntity log = RootEntity.create();
        if ((page != null && page <= 0) || (size != null && size < 0)) {//page start from 1
            response.setMessage(localizedService.getMessageKey("response.message.invalid.page.size", reqInfo.getLanguage()));
            return ResponseEntity.ok(response);
        }
        try {
            log.reqUri(request.getRequestURI()).type("UserController.getAllUser").reqUser(reqInfo.getUsername());
            response = userService.getAllUser( page, size);
            response.setSuccess(true);
            log.success();
        } catch (Exception e) {
            log.exp(e).fail();
            return errorMessageService.processException(e, response);
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(append("metric", log), log.toString());
        }
        return ResponseEntity.ok(response);
    }

}
