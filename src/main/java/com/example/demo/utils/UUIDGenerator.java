package com.example.demo.utils;

import java.io.Serializable;

public class UUIDGenerator implements IdGenerator {

	@Override
	public Serializable generator() {
		return new ObjectId().toString();
	}

}
