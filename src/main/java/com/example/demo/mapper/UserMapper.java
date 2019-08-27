package com.example.demo.mapper;

import java.util.List;

import com.example.demo.bean.User;

import tk.mybatis.mapper.common.Mapper;

@org.apache.ibatis.annotations.Mapper
public interface UserMapper extends Mapper<User> {

	/**
	 * 执行计划,批量插入数据
	 * @date 2019年8月27日 上午11:30:05
	 * @author weixueqiang
	 */
	void procedureTest(User user);
	
	/**
	 * 执行计划,返回数据
	 * @date 2019年8月27日 上午11:30:27
	 * @author weixueqiang
	 */
	List<User> procedureSel(User user);
	
	/**
	 * 多次输出结果集时,仅返回第一个
	 * @date 2019年8月27日 上午11:36:12
	 * @author weixueqiang
	 */
	List<User> manyPrint();
}