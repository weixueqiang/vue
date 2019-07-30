package com.example.demo.config;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.example.demo.shiro.CrossFilter;
import com.example.demo.shiro.MyShiroRealm;

//@Configuration
public class ShiroConfiguration {

	/**
	 * <bean class=
	 * "org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor">
	 * <property name="securityManager" ref="securityManager" /> </bean>
	 * 
	 * @date 2019年3月20日 下午2:23:59
	 * @author weixueqiang
	 */

	// 将自己的验证方式加入容器
	@Bean
	public MyShiroRealm myShiroRealm() {
		MyShiroRealm myShiroRealm = new MyShiroRealm();
		return myShiroRealm;
	}

	// 权限管理，配置主要是Realm的管理认证
	@Bean
	public SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		securityManager.setRealm(myShiroRealm());
		securityManager.setSessionManager(defaultWebSessionManager());
		securityManager.setRememberMeManager(cookieRememberMeManager());
		return securityManager;
	}

	// Filter工厂，设置对应的过滤条件和跳转条件
	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		Map<String, Filter> filters = new LinkedHashMap<>();
		filters.put("cross", new CrossFilter());
		shiroFilterFactoryBean.setFilters(filters);
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		Map<String, String> map = new LinkedHashMap<>();

		// Map<String, String> map = new HashMap<String, String>();

		map.put("/**/user/login", "anon");
		map.put("/**/user/register", "anon");
		map.put("/**/auth/authz", "anon");
		map.put("/**/.js", "anon");
		map.put("/**/.css", "anon");
		map.put("/**/*.html", "anon");
		// 登出
		// map.put("/**/logout", "logout");
		// 对所有用户认证
		map.put("/**", "authc");
		// 登录
		// shiroFilterFactoryBean.setLoginUrl("/index.html");
		shiroFilterFactoryBean.setLoginUrl("/auth/authz");
		// 首页
		// shiroFilterFactoryBean.setSuccessUrl("/index");
		// 错误页面，认证不通过跳转
		shiroFilterFactoryBean.setUnauthorizedUrl("/error");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
		return shiroFilterFactoryBean;
	}

	/**
	 * <bean id="sessionManager" class=
	 * "org.apache.shiro.web.session.mgt.DefaultWebSessionManager"> <!--
	 * 修改sessionId不会出现在地址栏上 -->
	 * <property name="sessionIdUrlRewritingEnabled" value="false" /> <!--
	 * session的失效时长，单位毫秒 --> <property name="globalSessionTimeout" value="600000"/>
	 * <!-- 删除失效的session --> <property name="deleteInvalidSessions" value="true"/>
	 * </bean>
	 * 
	 * @date 2019年2月13日 下午5:48:17
	 * @author weixueqiang
	 */
	@Bean
	public DefaultWebSessionManager defaultWebSessionManager() {
		DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
		defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false);
		defaultWebSessionManager.setGlobalSessionTimeout(600000);
		return defaultWebSessionManager;
	}

	/**
	 * <bean id="rememberMeCookie" class=
	 * "org.apache.shiro.web.servlet.SimpleCookie"> <!-- rememberMe是cookie的名字 -->
	 * <constructor-arg value="rememberMe" /> <!-- 记住我cookie生效时间30天 -->
	 * <property name="maxAge" value="2592000" />
	 * 
	 * <bean id="rememberMeManager" class=
	 * "org.apache.shiro.web.mgt.CookieRememberMeManager">
	 * <property name="cookie" ref="rememberMeCookie" /> </bean> </bean>
	 * 
	 * @date 2019年2月14日 下午2:09:02
	 * @author weixueqiang
	 */

	@Bean
	public SimpleCookie simpleCookie() {
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		simpleCookie.setPath("/");
		simpleCookie.setMaxAge(2592000);
		return simpleCookie;
	}

	@Bean
	public CookieRememberMeManager cookieRememberMeManager() {
		CookieRememberMeManager simpleCookie = new CookieRememberMeManager();
		simpleCookie.setCookie(simpleCookie());
		return simpleCookie;
	}

	// 加入注解的使用，不加入这个注解不生效
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	@Bean
	@ConditionalOnMissingBean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
		defaultAAP.setProxyTargetClass(true);
		return defaultAAP;
	}
}