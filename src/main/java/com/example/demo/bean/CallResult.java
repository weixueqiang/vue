package com.example.demo.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CallResult implements Serializable {
	boolean succee = true;
	int code;
	String msg = "操作成功";
	Object data;
	Map<String, Object> datas;

	public static CallResult ok() {
		CallResult callResult = new CallResult();
		return callResult;
	}

	public static CallResult ok(Integer code) {
		CallResult callResult = new CallResult();
		callResult.setCode(code);
		return callResult;
	}

	public static CallResult ok(String msg) {
		CallResult callResult = new CallResult();
		callResult.setMsg(msg);
		return callResult;
	}

	public static CallResult ok(Object data) {
		CallResult callResult = new CallResult();
		callResult.setData(data);
		return callResult;
	}

	public static CallResult ok(String msg, Object data) {
		CallResult callResult = new CallResult();
		callResult.setData(data);
		callResult.setMsg(msg);
		return callResult;
	}

	public static CallResult ok(String msg, Map<String, Object> datas) {
		CallResult callResult = new CallResult();
		callResult.setDatas(datas);
		callResult.setMsg(msg);
		return callResult;
	}

	public static CallResult ok(Map<String, Object> datas) {
		CallResult callResult = new CallResult();
		callResult.setDatas(datas);
		return callResult;
	}

	public static CallResult ok(Object data, Map<String, Object> datas) {
		CallResult callResult = new CallResult();
		callResult.setData(data);
		callResult.setDatas(datas);
		return callResult;
	}

	public static CallResult ok(Integer code, Object data, Map<String, Object> datas) {
		CallResult callResult = new CallResult();
		callResult.setCode(code);
		callResult.setData(data);
		callResult.setDatas(datas);
		return callResult;
	}

	public static CallResult ok(String msg, Object data, Map<String, Object> datas) {
		CallResult callResult = new CallResult();
		callResult.setMsg(msg);
		callResult.setData(data);
		callResult.setDatas(datas);
		return callResult;
	}

	public static CallResult ok(Integer code, String msg, Object data, Map<String, Object> datas) {
		CallResult callResult = new CallResult();
		callResult.setMsg(msg);
		callResult.setCode(code);
		callResult.setData(data);
		callResult.setDatas(datas);
		return callResult;
	}

	public static CallResult ok(Integer code, String msg) {
		CallResult callResult = new CallResult();
		callResult.setCode(code);
		callResult.setMsg(msg);
		return callResult;
	}

	public static CallResult err() {
		CallResult callResult = new CallResult();
		callResult.setCode(-1);
		callResult.setMsg("操作失败");
		callResult.setSuccee(false);
		return callResult;
	}

	public static CallResult err(Integer code) {
		CallResult callResult = err();
		callResult.setCode(code);
		return callResult;
	}

	public static CallResult err(String msg) {
		CallResult callResult = err();
		callResult.setMsg(msg);
		return callResult;
	}

	public static CallResult err(Object data) {
		CallResult callResult = err();
		callResult.setData(data);
		return callResult;
	}

	public static CallResult err(Map<String, Object> datas) {
		CallResult callResult = err();
		callResult.setDatas(datas);
		return callResult;
	}

	public static CallResult err(String msg, Object data) {
		CallResult callResult = err();
		callResult.setData(data);
		callResult.setMsg(msg);
		return callResult;
	}

	public static CallResult err(String msg, Map<String, Object> datas) {
		CallResult callResult = err();
		callResult.setDatas(datas);
		callResult.setMsg(msg);
		return callResult;
	}

	public static CallResult err(Object data, Map<String, Object> datas) {
		CallResult callResult = err();
		callResult.setData(data);
		callResult.setDatas(datas);
		return callResult;
	}

	public static CallResult err(Integer code, Object data, Map<String, Object> datas) {
		CallResult callResult = err();
		callResult.setCode(code);
		callResult.setData(data);
		callResult.setDatas(datas);
		return callResult;
	}

	public static CallResult err(String msg, Object data, Map<String, Object> datas) {
		CallResult callResult = err();
		callResult.setMsg(msg);
		callResult.setData(data);
		callResult.setDatas(datas);
		return callResult;
	}

	public static CallResult err(Integer code, String msg, Object data, Map<String, Object> datas) {
		CallResult callResult = err();
		callResult.setCode(code);
		callResult.setMsg(msg);
		callResult.setData(data);
		callResult.setDatas(datas);
		return callResult;
	}

	public static CallResult err(Integer code, String msg) {
		CallResult callResult = err();
		callResult.setCode(code);
		callResult.setMsg(msg);
		return callResult;
	}

	public Map<String, Object> getDatas() {
		return datas;
	}

	public void setDatas(Map<String, Object> datas) {
		this.datas = datas;
	}

	public void setData(Object data) {
		this.succee = true;
		this.data = data;
	}

	public CallResult() {

	}

	public CallResult(boolean succee, String msg) {
		this.msg = msg;
		this.succee = succee;

	}

	public boolean isSuccee() {
		return succee;
	}

	public void setSuccee(boolean succee) {
		this.succee = succee;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void fail(int code, String msg) {
		this.succee = false;
		this.msg = msg;
		this.code = code;
	}

	public void fail(String msg) {
		this.succee = false;
		this.msg = msg;
	}

	public void fail(int code) {
		this.succee = false;
		this.code = code;
	}

	public void succee(String msg) {
		this.succee = true;
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void putData(String key, Object value) {
		if (datas == null) {
			datas = new HashMap<String, Object>();
		}
		datas.put(key, value);
	}

	public Object getData(String key) {
		if (datas != null) {
			return datas.get(key);
		}
		return null;
	}

}
