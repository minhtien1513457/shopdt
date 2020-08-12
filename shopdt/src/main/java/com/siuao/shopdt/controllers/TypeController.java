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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siuao.shopdt.log.entity.RootEntity;
import com.siuao.shopdt.paging.Paging;
import com.siuao.shopdt.request.CreateTypeRequest;
import com.siuao.shopdt.request.RequestInfo;
import com.siuao.shopdt.request.UpdateTypeRequest;
import com.siuao.shopdt.response.ResponseResult;
import com.siuao.shopdt.service.ErrorMessageService;
import com.siuao.shopdt.service.LocalizedService;
import com.siuao.shopdt.service.TypeService;
import com.siuao.shopdt.utils.LogUtil;
import com.siuao.shopdt.utils.ThreadLocalHelper;
import com.siuao.shopdt.vo.TypeVO;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/type")
public class TypeController {
	protected TypeService typeService;

	@Autowired
	protected ErrorMessageService errorMessageService;

	@Autowired
	protected LocalizedService localizedService;

	@PostMapping
	public ResponseEntity<ResponseResult<TypeVO>> createGroup(@Context HttpServletRequest request,
			@RequestBody CreateTypeRequest req) {
		ResponseResult<TypeVO> response = new ResponseResult<TypeVO>();
		RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();
		RootEntity log = RootEntity.create();
		if (StringUtils.isEmpty(req.getName())) {
			log.fail();
			response.setSuccess(false);
			response.setMessage("Type name is empty");
			return ResponseEntity.badRequest().body(response);
		}
		try {
			TypeVO t = typeService.getTypeByName(req.getName());
			if (t != null) {
				log.fail();
				response.setSuccess(false);
				response.setMessage("Type has existed.");
				return ResponseEntity.badRequest().body(response);
			}
			TypeVO typeNew = typeService.createType(reqInfo.getUsername(), req);
			response.setData(typeNew);
			log.success();
			return ResponseEntity.ok(response);
		} catch (Throwable ex) {
			log.fail().exp(ex);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} finally {
			log.elapsed();
			LogUtil.jsonLog.info(append("metric", log), log.toString());
		}
	}

	@GetMapping
	public ResponseEntity<ResponseResult<TypeVO>> getTypeId(@Context HttpServletRequest request,
			@RequestParam(value = "id", required = false) Long id) throws Exception {
		ResponseResult<TypeVO> response = new ResponseResult<TypeVO>();
		RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();
		RootEntity log = RootEntity.create();
		try {
			log.reqUri(request.getRequestURI()).type("TypeController.getTypeId").reqUser(reqInfo.getUsername());
			response.setData(typeService.getTypeById(id));
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
	public ResponseEntity<Paging<TypeVO>> getAllType(@Context HttpServletRequest request,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "size", required = false) Integer size) throws Exception {
		Paging<TypeVO> response = new Paging<TypeVO>();
		RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();
		RootEntity log = RootEntity.create();
		if ((page != null && page <= 0) || (size != null && size < 0)) {// page start from 1
			response.setMessage(
					localizedService.getMessageKey("response.message.invalid.page.size", reqInfo.getLanguage()));
			return ResponseEntity.ok(response);
		}
		try {
			log.reqUri(request.getRequestURI()).type("TypeController.getAllType").reqUser(reqInfo.getUsername());
			response = typeService.getAllType(page, size);
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

	@PutMapping("/{id}")
	public ResponseEntity editType(@Context HttpServletRequest request, @PathVariable(name = "id") String id,
			@RequestBody UpdateTypeRequest req) {
		RootEntity log = RootEntity.create();
		RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();

		log.reqUri(request.getRequestURI()).type("TypeController.editType").reqUser(reqInfo.getUsername());
		if (StringUtils.isEmpty(id)) {
			log.fail();
			return ResponseEntity.badRequest().body("Os id is empty");
		}
		log.reqProp("TypeId", id);
		req.setId(Long.valueOf(id));
		try {
			boolean success = typeService.updateType(reqInfo.getUsername(), req);
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

	@DeleteMapping("/{id}")
	public ResponseEntity deleteTypes(@Context HttpServletRequest request, @PathVariable(name = "id") String id) {
		RootEntity log = RootEntity.create();
		RequestInfo reqInfo = ThreadLocalHelper.getRequestinfo();

		log.reqUri(request.getRequestURI()).type("TypeController.deleteTypes").reqUser(reqInfo.getUsername());

		if (StringUtils.isEmpty(id)) {
			log.fail();
			return ResponseEntity.badRequest().body("Type id is empty");
		} else {
			List<String> ids = Arrays.asList(id.split(",", -1));
			log.reqProp("TypeId", id);
			try {
				ids.forEach(u -> {
					typeService.deleteType(reqInfo.getUsername(), Long.valueOf(u).longValue());
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
