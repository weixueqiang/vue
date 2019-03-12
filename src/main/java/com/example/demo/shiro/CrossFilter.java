package com.example.demo.shiro;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.web.filter.AccessControlFilter;

public class CrossFilter extends AccessControlFilter {

	private static final Logger logger = LogManager.getLogger(CrossFilter.class);
	private List<String> hosts;
	private List<String> urls;

	@SuppressWarnings({ "unchecked" })
	protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String origin = request.getHeader("Origin");
		response.setHeader("Vary", "Accept-Encoding,Origin");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("P3P", "CP='IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");// ie,p3p规范
		if ("OPTIONS".equals(request.getMethod())) {
			Enumeration<String> headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = headerNames.nextElement();
				response.addHeader(key, request.getHeader(key));
			}
			response.addHeader("Access-Control-Allow-Methods", "POST,GET,TRACE,OPTIONS,DELETE");
			response.addHeader("Access-Control-Allow-Headers", "Content-Type,Origin,Accept");
			response.addHeader("Access-Control-Max-Age", "3600");
			response.setStatus(HttpServletResponse.SC_OK);
			response.flushBuffer();
			return false;
		}
		return true;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		return false;
	}

	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}

	private boolean isAllowUrl(String url) {
		if (urls == null) {
			return false;
		}
		for (String _url : urls) {
			if (_url.indexOf(url) != -1 || url.indexOf(_url) != -1) {
				return true;
			}
		}
		return false;
	}

	private boolean isAllowHost(String host) {
		if (host == null) {
			return true;
		}
		// try {
		// URI uri = new URI(host, false);
		// if (hosts.contains(uri.getHost()) || uri.getHost().startsWith("192.168.")) {
		// return true;
		// }
		// } catch (URIException e) {
		// logger.error("在处理跨域时获得异常", e);
		// }
		return false;
	}
}
