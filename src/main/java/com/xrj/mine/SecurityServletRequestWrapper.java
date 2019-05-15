package com.xrj.mine;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

public class SecurityServletRequestWrapper extends HttpServletRequestWrapper {
	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public SecurityServletRequestWrapper(HttpServletRequest request, HttpServletResponse response) {
		super(request);
		this.request = request;
		this.response = response;
	}

	// 重写获取session的方法
	public HttpSession getSession() {
		return this.getSession(true);
	}

	public HttpSession getSession(boolean create) {
		if (create) {
			String id = CookieUtil.getCookieValue(request, "pcxSessionId");
			if (StringUtils.isEmpty(id)) {
				id = UUID.randomUUID().toString();
				CookieUtil.setCookie(request, response, "pcxSessionId", id, 60 * 60);
			}
			this.session = new DispacherSessionImmpl(this.request, this.response, id);

			return this.session;
		} else {
			return null;
		}
	}
}