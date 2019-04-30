package com.example.demo.utils;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.bean.CallResult;

@RestControllerAdvice
public class DefaultExceptionHandler {

	@ExceptionHandler(value = Exception.class)
	public CallResult handlerException(Exception ex, HttpServletResponse res) {
		CallResult result = new CallResult();
		ex.printStackTrace();
		if (ex instanceof AuthorizationException) {
			result.fail("没有权限访问");
		} else {
			result.fail("发生异常");
		}
		return result;
	}
}
