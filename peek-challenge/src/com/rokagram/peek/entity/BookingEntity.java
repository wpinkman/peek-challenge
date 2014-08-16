package com.rokagram.peek.entity;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class BookingEntity {
	@Id
	private Long id;

	@Index
	private long timeslot_id;
	private int size;

	public Long getId() {
		return id;
	}

	public long getTimeslot_id() {
		return timeslot_id;
	}

	public void setTimeslot_id(long timeslot_id) {
		this.timeslot_id = timeslot_id;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "BookingEntity [id=" + id + ", timeslot_id=" + timeslot_id + ", size=" + size + "]";
	}

}
