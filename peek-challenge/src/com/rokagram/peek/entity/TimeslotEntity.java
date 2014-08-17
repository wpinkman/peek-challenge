package com.rokagram.peek.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Stringify;
import com.googlecode.objectify.stringifier.Stringifier;

class LongStringifier implements Stringifier<Long> {
	@Override
	public String toString(Long obj) {
		return obj.toString();
	}

	@Override
	public Long fromString(String str) {
		return Long.parseLong(str);
	}
}

@Entity
@Cache
public class TimeslotEntity {
	@Id
	private Long id;

	@Index
	private long start_time;
	private int duration;

	@JsonIgnore
	@Stringify(LongStringifier.class)
	private Map<Long, AssignedBoat> assignedBoats = new HashMap<Long, AssignedBoat>();

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

	// this is for JSON serialization (to meep the spec)
	public Set<Long> getBoats() {
		return getAssignedBoats().keySet();
	}

	public int getCustomer_count() {
		int ret = 0;

		for (AssignedBoat boat : getAssignedBoats().values()) {
			ret += boat.getCustomer_count();
		}

		return ret;
	}

	public int getAvailability() {
		int ret = 0;

		for (AssignedBoat boat : getAssignedBoats().values()) {
			if (boat.isAvailable()) {
				int openSeats = boat.getCapacity() - boat.getCustomer_count();
				if (openSeats > ret) {
					ret = openSeats;
				}
			}
		}

		return ret;
	}

	public Map<Long, AssignedBoat> getAssignedBoats() {
		return assignedBoats;
	}

	public boolean overlapsWith(TimeslotEntity other) {
		boolean ret = false;

		if (this.id != null && other.id != null && other.duration > 0 && this.duration > 0) {

			// can't overlap with youself
			if (!other.id.equals(this.id)) {

				// debug
				Date myStartDate = new Date(this.start_time * 1000);
				long myEndTime = this.start_time + this.duration * 60;
				Date myEndDate = new Date(myEndTime * 1000);

				Date otherStartDate = new Date(other.start_time * 1000);
				long otherEndTime = other.start_time + other.duration * 60;
				Date otherEndDate = new Date(otherEndTime * 1000);

				System.out.println("this :" + myStartDate + " = " + myEndDate);
				System.out.println("that :" + otherStartDate + " = " + otherEndDate);

				if (other.start_time < this.start_time) {
					if (otherEndTime > this.start_time) {
						ret = true;
					}
				} else {
					if (other.start_time < myEndTime) {
						ret = true;
					}
				}
			} else {
				System.out.println("this and that are the same object");
			}
		}

		System.out.println("returning:" + ret);
		return ret;
	}

	@Override
	public String toString() {
		Date startDate = new Date(start_time * 1000L);

		return "TimeslotEntity [id=" + id + ", start_time=" + start_time + " (" + startDate + "), duration=" + duration
				+ ", customer_count=" + getCustomer_count() + ", availability=" + getAvailability()
				+ ", allocatedBoats=" + assignedBoats.toString() + "]";
	}
}
