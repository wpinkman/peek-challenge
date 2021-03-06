package com.rokagram.peek.entity;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class BoatEntity {
	@Id
	private Long id;
	private int capacity;
	private String name;

	public Long getId() {
		return id;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "BoatEntity [id=" + id + ", capacity=" + capacity + ", name=" + name + "]";
	}

}
