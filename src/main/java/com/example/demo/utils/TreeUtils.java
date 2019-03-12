package com.example.demo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface TreeUtils {

	static <T> List<T> buildTree(List<T> datas, Function<T, Object> id, Function<T, Object> parentId, Object rootId,
			BiConsumer<T, List<T>> setChild) {
		List<T> list = new ArrayList<T>();
		for (T t : datas) {
			if (parentId.apply(t).equals(rootId)) {
				setChild.accept(t, buildTree(datas, id, parentId, id.apply(t), setChild));
				list.add(t);
			}
		}
		return list;
	}

}
