package com.example.demo.config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

/**
 * 
 * 调试时,热加载mapper.xml文件
 * 
 * @author weixueqiang
 * @date 2019年7月30日
 */
@Component
public class MybatisMapperDynamicLoader implements InitializingBean, ApplicationContextAware {

	static Logger logger = LoggerFactory.getLogger(MybatisMapperDynamicLoader.class);
	private final HashMap<String, String> mappers = new HashMap<String, String>();
	private volatile ConfigurableApplicationContext context = null;
	private volatile Scanner scanner = null;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = (ConfigurableApplicationContext) applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			scanner = new Scanner();
			new Timer(true).schedule(new TimerTask() {
				public void run() {
					try {
						if (scanner.isChanged()) {
							scanner.reloadXML();
							logger.info("========重新加载完成======");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 60 * 1000, 5 * 1000);
			logger.info("==========开启mapper.xml自动更新========");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	class Scanner {
		private static final String XML_RESOURCE_PATTERN = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				+ "**/*Mapper.xml";
		private final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

		/**
		 * 加载mapper.xml 以文件的uri为key以文件长度和最后修改时间为value
		 * 
		 * @date 2019年7月30日 上午11:39:48
		 * @author weixueqiang
		 */
		public Scanner() throws IOException {
			Resource[] resources = findResource();
			if (resources != null) {
				for (Resource resource : resources) {
					String key = resource.getURI().toString();
					String value = getMd(resource);
					mappers.put(key, value);
				}
			}
		}

		/**
		 * 读取mapper
		 * 
		 * @date 2019年7月30日 上午11:45:16
		 * @author weixueqiang
		 */
		public void reloadXML() throws Exception {
			// 通过spring的context 获取工程中的SqlSessionFactory
			SqlSessionFactory factory = context.getBean(SqlSessionFactory.class);
			Configuration configuration = factory.getConfiguration();
			removeConfig(configuration);// 删除所有
			// 重新添加
			for (Resource resource : findResource()) {
				try {
					XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(), configuration,
							resource.toString(), configuration.getSqlFragments());
					xmlMapperBuilder.parse();
				} finally {
					ErrorContext.instance().reset();
				}
			}
		}

		private void removeConfig(Configuration configuration) throws Exception {
			Configuration configuration_ = new Configuration();
			Class<?> classConfig = configuration_.getClass();
			clearMap(classConfig, configuration, "mappedStatements");
			clearMap(classConfig, configuration, "caches");
			clearMap(classConfig, configuration, "resultMaps");
			clearMap(classConfig, configuration, "parameterMaps");
			clearMap(classConfig, configuration, "keyGenerators");
			clearMap(classConfig, configuration, "sqlFragments");
			clearSet(classConfig, configuration, "loadedResources");
		}

		private void clearMap(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
			Field field = classConfig.getDeclaredField(fieldName);
			field.setAccessible(true);
			((Map) field.get(configuration)).clear();
		}

		private void clearSet(Class<?> classConfig, Configuration configuration, String fieldName) throws Exception {
			Field field = classConfig.getDeclaredField(fieldName);
			field.setAccessible(true);
			((Set) field.get(configuration)).clear();
		}

		/**
		 * 判断文件是否变更,并将变更后的文件信息更新至map
		 * 
		 * @date 2019年7月30日 上午11:48:34
		 * @author weixueqiang
		 */
		public boolean isChanged() throws IOException {
			boolean isChanged = false;
			for (Resource resource : findResource()) {
				String key = resource.getURI().toString();
				String value = getMd(resource);
				if (!value.equals(mappers.get(key))) {
					isChanged = true;
					int index = key.lastIndexOf("/");
					if (index != -1) {
						logger.info("重新加载:" + key.substring(index + 1, key.length()));
					}
					mappers.put(key, value);
				}
			}
			return isChanged;
		}

		private Resource[] findResource() throws IOException {
			return resourcePatternResolver.getResources(XML_RESOURCE_PATTERN);
		}

		private String getMd(Resource resource) throws IOException {
			return new StringBuilder().append(resource.contentLength()).append("-").append(resource.lastModified())
					.toString();
		}
	}
}