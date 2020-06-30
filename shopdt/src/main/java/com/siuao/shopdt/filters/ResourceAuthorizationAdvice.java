package com.siuao.shopdt.filters;


import com.siuao.shopdt.request.RequestInfo;
import com.siuao.shopdt.response.BaseResult;
import com.siuao.shopdt.utils.ThreadLocalHelper;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Aspect
@Component
public class ResourceAuthorizationAdvice {


	private static Logger logger = LoggerFactory.getLogger(ResourceAuthorizationAdvice.class);

	@Around(value = "@within(org.springframework.web.bind.annotation.RestController)")
	public Object checkAuthorizeWithRestController(ProceedingJoinPoint joinPoint) throws Throwable {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = sra.getRequest();
		BaseResult unAuthorization = new BaseResult(false, 401, "Unauthorized");
		String language = "en";
		if (StringUtils.isNotEmpty(request.getHeader("language"))) {
			language = request.getHeader("language");
		}
		String userName = request.getHeader("username");
		String token = (String) request.getHeader("authorization");

		if( !request.getRequestURI().toLowerCase().contains("/auth/signin") && !request.getRequestURI().toLowerCase().contains("/auth/signup")) {
			if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(token)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(unAuthorization);
			}
		}

		RequestInfo requqestInfo = new RequestInfo(userName, language, token);
		ThreadLocalHelper.setRequestInfo(requqestInfo);

		Object rs = joinPoint.proceed();
		return rs;
	}
}

