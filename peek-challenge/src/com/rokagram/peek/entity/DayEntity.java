package com.rokagram.peek.entity;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class DayEntity {
	@Id
	private String date;
	private List<Key<TimeslotEntity>> timeslots = new ArrayList<Key<TimeslotEntity>>();

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<Key<TimeslotEntity>> getTimeslots() {
		return timeslots;
	}

	public void setTimeslots(List<Key<TimeslotEntity>> timeslots) {
		this.timeslots = timeslots;
	}

	@Override
	public String toString() {
		return "DayEntity [date=" + date + ", timeslots=" + timeslots + "]";
	}
}
