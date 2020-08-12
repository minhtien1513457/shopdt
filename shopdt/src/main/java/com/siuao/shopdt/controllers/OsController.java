package com.siuao.shopdt.controllers;

import static net.logstash.logback.marker.Markers.append;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siuao.shopdt.log.entity.RootEntity;
import com.siuao.shopdt.paging.Paging;
import com.siuao.shopdt.request.RequestInfo;
import com.siuao.shopdt.request.UpdateOsRequest;
import com.siuao.shopdt.response.ResponseResult;
import com.siuao.shopdt.service.ErrorMessageService;
import com.siuao.shopdt.service.LocalizedService;
import com.siuao.shopdt.service.OsService;
import com.siuao.shopdt.utils.LogUtil;
import com.siuao.shopdt.utils.ThreadLocalHelper;
import com.siuao.shopdt.vo.OsVO;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/os")
public class OsController {
    @Autowired
    protected OsService osService;

    @Autowired
    protected ErrorMessageService errorMessageService;

    @Autowired
    protected LocalizedService localizedService;
    
    @GetMapping(value = "")
    public ResponseEntity<ResponseResult<OsVO>> getOsId(@Context HttpServletRequest request,
                                                           @RequestParam(value = "id", required = false) Long id) throws Exception {
        ResponseResult<OsVO> response = new ResponseResult<OsVO>();
        RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();
        RootEntity log = RootEntity.create();
        try {
            log.reqUri(request.getRequestURI()).type("OsController.getOsId").reqUser(reqInfo.getUsername());
            response.setData(osService.getOsById(id));
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
    public ResponseEntity<Paging<OsVO>> getAllOs(@Context HttpServletRequest request,
                                                   @RequestParam(value = "page", required = false) Integer page,
                                                   @RequestParam(value = "size", required = false) Integer size) throws Exception {
        Paging<OsVO> response = new Paging<OsVO>();
        RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();
        RootEntity log = RootEntity.create();
        if ((page != null && page <= 0) || (size != null && size < 0)) {//page start from 1
            response.setMessage(localizedService.getMessageKey("response.message.invalid.page.size", reqInfo.getLanguage()));
            return ResponseEntity.ok(response);
        }
        try {
            log.reqUri(request.getRequestURI()).type("OsController.getAllOs").reqUser(reqInfo.getUsername());
            response = osService.getAllOS( page, size);
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

    @PutMapping("/{osid}")
    public ResponseEntity editOs(@Context HttpServletRequest request, @PathVariable(name = "osid") String osid, @RequestBody UpdateOsRequest req) {
        RootEntity log = RootEntity.create();
        RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();

        log.reqUri(request.getRequestURI()).type("OsController.editOs").reqUser(reqInfo.getUsername());
        if (StringUtils.isEmpty(osid)) {
            log.fail();
            return ResponseEntity.badRequest().body("Os id is empty");
        }
        log.reqProp("OsId", osid);
        req.setId(Long.valueOf(osid));
        try {
            boolean success = osService.updateOs(reqInfo.getUsername(), req);
            if (success) {
                log.success();
                return ResponseEntity.ok().build();
            } else {
                log.fail();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Throwable ex) {
            log.fail().exp(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            log.elapsed();
            LogUtil.jsonLog.info(append("metric", log), log.toString());
        }
    }
    
    @DeleteMapping("/{osid}")
    public ResponseEntity deleteOSs(@Context HttpServletRequest request, @PathVariable(name = "osid") String osid) {
        RootEntity log = RootEntity.create();
        RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();

        log.reqUri(request.getRequestURI()).type("OsController.deleteOSs").reqUser(reqInfo.getUsername());

        if (StringUtils.isEmpty(osid)) {
            log.fail();
            return ResponseEntity.badRequest().body("Os id is empty");
        }else {
        	List<String> userIds = Arrays.asList(osid.split(",", -1));
        	 log.reqProp("OsId", osid);
             try {
            	 userIds.forEach(u -> {
            		 osService.deleteOs(reqInfo.getUsername(), Long.valueOf(u).longValue());
            	 });
                 log.success();
                 return ResponseEntity.ok().build();
             } catch (Throwable ex) {
                 log.fail();
                 return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
             } finally {
             	   log.elapsed();
                    LogUtil.jsonLog.info(append("metric", log), log.toString());
             }
        }
       
    }
}
