package com.example.demo.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bean.CallResult;
import com.example.demo.bean.Resource;

@RestController
@RequestMapping("/vue/resource")
public class ResourceController {

	@RequestMapping("/list")
	public CallResult list() {
		return CallResult.ok(Resource.getTree());
	}

	@RequestMapping("/get")
	public CallResult get(Integer id) {
		if (id == null) {
			return CallResult.err("标识不能为空");
		}
		Resource resource = Resource.get(id);
		CallResult ok = CallResult.ok();
		ok.setData(resource);
		if (resource == null) {
			return ok;
		}
		Resource parent = Resource.get(resource.getParentId());
		resource.setParentName(parent == null ? "" : parent.getName());
		return ok;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public CallResult save(Resource resource) {
		if (StringUtils.isEmpty(resource.getName()) || resource.getName().trim().isEmpty()) {
			return CallResult.err("名称不能为空");
		}
		if (resource.getId() == null) {
			if (resource.getParentId() == null) {
				return CallResult.err("请选择父级");
			}
			int size = Resource.resources.size() + 1;
			resource.setId(size);
			Resource.resources.add(resource);
		} else {
			Resource resource2 = Resource.get(resource.getId());
			if (resource2 == null) {
				return CallResult.err("数据获取失败");
			}
			resource2.setName(resource.getName());
		}
		return CallResult.ok();
	}

}
