package com.example.demo.shiro;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.example.demo.bean.User;
import com.example.demo.mapper.UserMapper;

public class MyShiroRealm extends AuthorizingRealm {

	@Resource
	private UserMapper userMapper;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		System.out.println("ko.....");
		Object principal = principalCollection.getPrimaryPrincipal();
		User u = (User) principal;
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Set<String> permissions = new HashSet<>();
		permissions.add("user:info");
		permissions.add("user:list");
		info.setStringPermissions(permissions);
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
		String username = token.getUsername();
		User record = new User();

		record.setUsername(username);
		User user = userMapper.selectOne(record);
		if (user == null) {
			throw new AuthenticationException("用户不存在");
		}
		return new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
	}
}