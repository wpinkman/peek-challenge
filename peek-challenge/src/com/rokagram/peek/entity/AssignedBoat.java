package com.rokagram.peek.entity;

/**
 * Keeps track of boat assignments for each time slot. Could/should also have a
 * list of references to the exact bookings allocated.
 * 
 * @author awaddell
 * 
 */
public class AssignedBoat {

	private int capacity;
	private boolean available = true;
	private int customer_count;

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getCustomer_count() {
		return customer_count;
	}

	public void setCustomer_count(int customer_count) {
		this.customer_count = customer_count;
	}

	@Override
	public String toString() {
		return "AssignedBoat [capacity=" + capacity + ", available=" + available + ", customer_count=" + customer_count
				+ "]";
	}

}
