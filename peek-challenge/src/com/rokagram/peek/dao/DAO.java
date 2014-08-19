package com.rokagram.peek.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.rokagram.peek.entity.BoatEntity;
import com.rokagram.peek.entity.BookingEntity;
import com.rokagram.peek.entity.DayEntity;
import com.rokagram.peek.entity.TimeslotEntity;

public class DAO {
	static {
		ObjectifyService.register(TimeslotEntity.class);
		ObjectifyService.register(BoatEntity.class);
		ObjectifyService.register(BookingEntity.class);
		ObjectifyService.register(DayEntity.class);
	}

	public static Objectify ofy() {
		return ObjectifyService.ofy();
	}

	public static List<TimeslotEntity> getTimeslotsForDay(String dateString) {
		List<TimeslotEntity> ret = new ArrayList<TimeslotEntity>();
		if (dateString != null) {
			DayEntity day = ofy().load().type(DayEntity.class).id(dateString).now();
			if (day != null) {
				Map<Key<TimeslotEntity>, TimeslotEntity> map = ofy().load().keys(day.getTimeslots());

				for (TimeslotEntity ts : map.values()) {
					ret.add(ts);
				}
			}
		}
		return ret;
	}
}
