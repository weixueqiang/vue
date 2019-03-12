package com.example.demo.utils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class PropertySet {

	private Set<ProPertyStrategyMapper> propertys = new HashSet<ProPertyStrategyMapper>();

	private Class<?> entity;

	@SuppressWarnings("unused")
	private PropertySet() {

	}

	public PropertySet(Class<?> entity) {
		this.entity = entity;
		this.build();
	}

	public Set<ProPertyStrategyMapper> getPropertys() {
		return propertys;
	}

	public void setPropertys(Set<ProPertyStrategyMapper> propertys) {
		this.propertys = propertys;
	}

	public PropertySet build() {
		Field[] fields = entity.getDeclaredFields();
		for (Field field : fields) {
			if ("serialVersionUID".equals(field.getName())) {
				continue;
			}
			if (field.isAnnotationPresent(Id.class)) {
				Id id = field.getAnnotation(Id.class);
				if (id.strategy() == null) {
					continue;
				}
				Class<?> generator = id.strategy();
				Object object = null;
				try {
					object = generator.newInstance();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!(object instanceof IdGenerator)) {
					continue;
				}
				IdGenerator idGenerator = (IdGenerator) object;
				ProPertyStrategyMapper proPertyStrategyMapper = new ProPertyStrategyMapper(field.getName(),
						idGenerator);
				propertys.add(proPertyStrategyMapper);
				break;
			}

		}
		return this;
	}
}
