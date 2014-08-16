package com.rokagram.peek.entity;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class TimeslotEntity {
	@Id
	private Long id;

	@Index
	private long start_time;
	private int duration;

	private int customer_count;
	private int availability;

	private List<Long> boats = new ArrayList<Long>();

	public Long getId() {
		return id;
	}

	public long getStart_time() {
		return start_time;
	}

	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<Long> getBoats() {
		return boats;
	}

	public int getCustomer_count() {
		return customer_count;
	}

	public void setCustomer_count(int customer_count) {
		this.customer_count = customer_count;
	}

	public int getAvailability() {
		return availability;
	}

	public void setAvailability(int availability) {
		this.availability = availability;
	}

}
