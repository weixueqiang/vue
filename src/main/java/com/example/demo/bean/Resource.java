package com.example.demo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Resource implements Serializable {

	private Integer id;
	private String name;
	private String parentName;
	private Integer parentId;
	private List<Resource> children;

	public Resource(Integer id, String name, Integer parentId) {
		super();
		this.id = id;
		this.name = name;
		this.parentId = parentId;
	}

	public Resource() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static List<Resource> resources = new ArrayList<Resource>();
	static {
		resources.add(new Resource(1, "资源栏目", 0));
		resources.add(new Resource(11, "总务处", 1));
		resources.add(new Resource(111, "工作计划", 11));
		resources.add(new Resource(12, "教务处", 1));
	}

	public static Resource get(Integer id) {
		for (Resource resource : resources) {
			if (resource.getId() == id) {
				return resource;
			}
		}
		return null;
	}

	public static List<Resource> getTree() {
		return buidTree(resources, 0);
	}

	public static List<Resource> buidTree(List<Resource> datas, Integer parentId) {
		List<Resource> result = new ArrayList<>();
		for (Resource resource : datas) {
			if (resource.getParentId().equals(parentId)) {
				resource.setChildren(buidTree(datas, resource.getId()));
				result.add(resource);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		getTree();
		System.out.println(resources);
		System.out.println(get(1));

	}

}
