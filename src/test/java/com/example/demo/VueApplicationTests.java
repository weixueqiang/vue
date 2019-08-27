package com.example.demo;

import java.util.List;
import java.util.function.Consumer;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.bean.User;
import com.example.demo.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VueApplicationTests {

	@Resource
	UserMapper userMapper;
	
	
	public static <T> Consumer<T> getOut(){
		return System.out::println;
	}
	
	@Test
	public void start() {
		System.out.println(userMapper+"..........");
	}
	
	@Test
	public void invokeProcedure() {
		User user=new User();
		user.setAge(3);
		user.setUsername("yjl");
		userMapper.procedureTest(user);
		System.out.println("over.......");
	}
	
	@Test
	public void invokeProcedureSelect() {
		User user=new User();
		user.setAge(100);
		user.setUsername("yjl");
		List<User> datas = userMapper.procedureSel(user);
		System.out.println("over.......");
		datas.forEach(getOut());
	}
	
	@Test
	public void many() {
		List<User> datas = userMapper.manyPrint();
		System.out.println("over.......");
		datas.forEach(getOut());
	}
	
	@Test
	public void contextLoads() {
		System.out.println(userMapper);
		System.out.println(userMapper.selectAll());
		Example example = new Example(User.class);
		example.createCriteria().andEqualTo("id", "1234");
		List<User> selectByExample = userMapper.selectByExample(example);
		System.out.println(selectByExample);
		User user = userMapper.selectByPrimaryKey("1234");
		System.out.println(user);
	}

	@Test
	public void insert() {
		User user = new User();
		user.setUsername("lisi");
		user.setPassword("123456");
		user.setSex("man");
		userMapper.insert(user);
	}

	@Test
	public void pageTest() {
		PageHelper.startPage(1, 2);
		List<User> selectAll = userMapper.selectAll();
		PageInfo<User> page = new PageInfo<>(selectAll);
		System.out.println(page);
	}

}
