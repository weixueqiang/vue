package com.xrj.mine;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

public class DispacherSessionImmpl implements HttpSession {

	private HttpSession session;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Map<String, Object> sessionMap = null;
	// private SessionStore sessionStore = new SessionStore();
	private String sid;

	public DispacherSessionImmpl() {
	}

	public DispacherSessionImmpl(HttpSession session) {
		this.session = session;
	}

	public DispacherSessionImmpl(HttpServletRequest request, HttpServletResponse response, String id) {
		this.sid = id;
		this.request = request;
		this.response = response;

	}

	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		if (sessionMap == null) {
			sessionMap = SessionStore.getSession(this.getId());
		}
		return sessionMap.get(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub
		if (sessionMap == null) {
			sessionMap = SessionStore.getSession(this.getId());
		}
		this.sessionMap.put(name, value);
		SessionStore.saveSession(this.getId(), sessionMap);
	}

	@Override
	public void removeAttribute(String name) {
		// TODO Auto-generated method stub
		if (sessionMap == null) {
			sessionMap = SessionStore.getSession(this.getId());
		}
		sessionMap.remove(name);
		SessionStore.saveSession(sid, sessionMap);

	}

	@Override
	public long getCreationTime() {

		return 0;
	}

	@Override
	public String getId() {
		return this.sid;
	}

	@Override
	public long getLastAccessedTime() {
		System.out.println("getLastAccessedTime..");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		System.out.println("getServletContext..");
		return null;
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		// TODO Auto-generated method stub
		System.out.println("setMaxInactiveInterval..");

	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		System.out.println("getMaxInactiveInterval..");
		return 0;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		System.out.println("getSessionContext..");
		return null;
	}

	@Override
	public Object getValue(String name) {
		// TODO Auto-generated method stub
		System.out.println("getValue..");
		return null;
	}

	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		System.out.println("getAttributeNames..");
		return null;
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		System.out.println("getValueNames..");
		return null;
	}

	@Override
	public void putValue(String name, Object value) {
		// TODO Auto-generated method stub
		System.out.println("putValue..");

	}

	@Override
	public void removeValue(String name) {
		// TODO Auto-generated method stub

		System.out.println("removeValue..");
	}

	@Override
	public void invalidate() {
		SessionStore.removeSession(getId());
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		System.out.println("isNew");
		return false;
	}

}