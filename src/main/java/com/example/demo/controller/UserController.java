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
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@RestController
@RequestMapping("/vue/user")
public class UserController {

	@Resource
	private UserMapper userMapper;

	@RequestMapping("list")
	public CallResult list() {
		return CallResult.ok(userMapper.selectAll());
	}

	@RequestMapping("pageList")
	public PageInfo<User> pageList(PageInfo<User> page, User user) {
		PageHelper.startPage(page.getPageNum(), page.getPageSize());
		Example example = new Example(User.class);
		Criteria criteria = example.createCriteria();
		if (!StringUtils.isEmpty(user.getUsername())) {
			criteria.andLike("username", user.getUsername());
		}
		example.setOrderByClause("create_time desc");
		return new PageInfo<>(userMapper.selectByExample(example));
	}

	@RequestMapping("save")
	public CallResult save(User user) {
		Example example = new Example(User.class);
		Criteria criteria = example.createCriteria().andEqualTo("username", user.getUsername());
		boolean exist = userMapper.selectByExample(example).stream().anyMatch(e -> !e.getId().equals(user.getId()));
		if (exist) {
			return CallResult.err("存在该用户名称的数据了");
		}
		userMapper.selectByExample(example);
		if (StringUtils.isEmpty(user.getId())) {
			user.setPassword("123456");
			user.setCreateTime(new Date());
			userMapper.insert(user);
		} else {
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
		if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getSex()) || user.getAge() == null) {
			return CallResult.err("请正确填写数据");
		}
		return this.save(user);

	}

	@RequestMapping("info")
	public CallResult info() {
		Subject subject = SecurityUtils.getSubject();
		return CallResult.ok(subject.getPrincipal());
	}

	@RequestMapping("/logout")
	public CallResult logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return CallResult.ok();
	}

}
