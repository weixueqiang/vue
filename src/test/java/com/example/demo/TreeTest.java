package com.example.demo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.util.CollectionUtils;
import org.junit.Test;

import com.example.demo.utils.TreeUtils;

public class TreeTest {

	public static <T> List<T> build(List<T> list, Object rootId) {
		if (CollectionUtils.isEmpty(list) || rootId == null) {
			return list;
		}
		try {
			Class<? extends Object> clazz = list.get(0).getClass();
			Method getId = clazz.getMethod("getId", null);
			Method getPId = clazz.getMethod("getParentId", null);
			Method setChildren = clazz.getMethod("setChildren", List.class);
			if (getId == null || getPId == null || setChildren == null) {
				throw new RuntimeException("实体方法错误");
			}
			return buildTrees(list, getId, getPId, setChildren, rootId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	private static <T> List<T> buildTrees(List<T> datas, Method getId, Method getPId, Method setChildren, Object rootId)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<T> list = new ArrayList<T>();
		for (T t : datas) {

			if (getPId.invoke(t, null).equals(rootId)) {
				setChildren.invoke(t, buildTrees(datas, getId, getPId, setChildren, getId.invoke(t, null)));
				list.add(t);
			}
		}
		return list;
	}

	@Test
	public void buildTree() {
		List<Tree> tree = TreeUtils.buildTree(getList(), Tree::getId, Tree::getParentId, 1, Tree::setChildren);
		System.out.println(tree);
	}

	@Test
	public void bui2() {
		List<Tree> build = build(getList(), 1);
		System.out.println(build);
	}

	public List<Tree> getList() {
		List<Tree> list = new ArrayList<>();
		Tree tree1 = new Tree(11, 1, "一级");
		Tree tree2 = new Tree(12, 1, "一级");
		Tree tree11 = new Tree(111, 11, "二级");
		Tree tree111 = new Tree(1111, 111, "三级");
		list.add(tree2);
		list.add(tree1);
		list.add(tree11);
		list.add(tree111);
		return list;
	}

}

class Tree {

	private Integer id;
	private Integer parentId;
	private String name;
	private List<Tree> children;

	public Tree(Integer id, Integer parentId, String name) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Tree> getChildren() {
		return children;
	}

	public void setChildren(List<Tree> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "Tree [id=" + id + ", parentId=" + parentId + ", name=" + name + ", chidren=" + children + "]";
	}

}