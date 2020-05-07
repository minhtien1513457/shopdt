package com.siuao.shopdt.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @GetMapping(value = "/")
    public ResponseEntity<ResponseResult<AppVO>> getAppId(@Context HttpServletRequest request,
                                                          @RequestParam(value = "id", required = false) Long id) throws Exception {
        ResponseResult<AppVO> response = new ResponseResult<AppVO>();
        RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();
        RootEntity log = RootEntity.create();
        try {
            log.reqUri(request.getRequestURI()).type("AppController.getAppId").reqUser(reqInfo.getUsername());
            response.setData(appService.getAppById(id));
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
    public ResponseEntity<Paging<AppVO>> getAllApp(@Context HttpServletRequest request,
                                                   @RequestParam(value = "page", required = false) Integer page,
                                                   @RequestParam(value = "size", required = false) Integer size)
}
