package com.rokagram.peek.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class TimeslotEntity {
	@Id
	private Long id;

	@Index
	private long start_time;
	private int duration;

	@Ignore
	private int customer_count;
	@Ignore
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

	@Override
	public String toString() {
		Date startDate = new Date(start_time * 1000L);
		return "TimeslotEntity [id=" + id + ", start_time=" + start_time + " (" + startDate + "), duration=" + duration
				+ ", customer_count=" + customer_count + ", availability=" + availability + ", boats=" + boats + "]";
	}
}
