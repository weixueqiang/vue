package com.example.demo.controller;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.bean.CallResult;
import com.example.demo.bean.User;
import com.example.demo.mapper.UserMapper;

@RestController
@RequestMapping("/vue/user")
public class UserController {

	@Resource
	private UserMapper userMapper;

	@RequestMapping("list")
	public CallResult list() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		System.out.println("session--;" + session);
		return CallResult.ok(userMapper.selectAll());
	}

	@RequestMapping("save")
	public CallResult save(User user) {
		if (StringUtils.isEmpty(user.getId())) {
			userMapper.insert(user);
		} else {
			user.setPassword("123456");
			user.setCreateTime(new Date());
			userMapper.updateByPrimaryKeySelective(user);
		}
		return CallResult.ok();
	}

	@RequestMapping("delete")
	public CallResult delete(Integer id) {
		if (id == null) {
			return CallResult.err("id不能为空");
		}
		userMapper.deleteByPrimaryKey(id);
		return CallResult.ok();
	}

	@RequestMapping("get")
	public CallResult get(String id) {
		if (StringUtils.isEmpty(id)) {
			return CallResult.err("请正确填写数据");
		}
		return CallResult.ok(userMapper.selectByPrimaryKey(id));
	}

	@RequestMapping("login")
	public CallResult login(String username, String password) {
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(username, password, true);
		if (!subject.isAuthenticated()) {
			try {
				subject.login(token);
			} catch (Exception e) {
				return CallResult.err("用户名或密码错误");
			}
		}
		Session session = subject.getSession();
		System.out.println("session--;" + session);
		return CallResult.ok();
	}

	@RequestMapping("register")
	public CallResult register(User user) {
		if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
			return CallResult.err("请正确填写数据");
		}

		return CallResult.err("用户名或密码错误");
	}

	@RequestMapping("info")
	public CallResult info() {
		Subject subject = SecurityUtils.getSubject();
		return CallResult.ok(subject.getPrincipal());
	}

}
