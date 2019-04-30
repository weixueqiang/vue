package com.example.demo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Resource implements Serializable {

	/**
	 * @date 2019年4月30日 下午5:03:28
	 * @author weixueqiang
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String parentName;
	private Integer parentId;
	private List<Resource> children;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<Resource> getChildren() {
		return children;
	}

	public void setChildren(List<Resource> children) {
		this.children = children;
	}

	public static List<Resource> getResources() {
		return resources;
	}

	public static void setResources(List<Resource> resources) {
		Resource.resources = resources;
	}

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
