package com.rokagram.peek.dao;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.rokagram.peek.entity.BoatEntity;
import com.rokagram.peek.entity.TimeslotEntity;

public class DAO {
	static {
		ObjectifyService.register(TimeslotEntity.class);
		ObjectifyService.register(BoatEntity.class);
	}

	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}
}
