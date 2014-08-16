package com.rokagram.peek.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.VoidWork;
import com.rokagram.peek.dao.DAO;
import com.rokagram.peek.entity.BoatEntity;
import com.rokagram.peek.entity.TimeslotEntity;

public class AssignmentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String timeslotIdParam = req.getParameter("assignment[timeslot_id]");
		String boatIdParam = req.getParameter("assignment[boat_id]");
		try {
			final long timeslotId = Long.parseLong(timeslotIdParam);
			final long boatId = Long.parseLong(boatIdParam);

			DAO.ofy().transact(new VoidWork() {

				@Override
				public void vrun() {
					// start both asynchronously
					LoadResult<TimeslotEntity> tsload = DAO.ofy().load().type(TimeslotEntity.class).id(timeslotId);
					LoadResult<BoatEntity> boatload = DAO.ofy().load().type(BoatEntity.class).id(boatId);

					TimeslotEntity timeslot = tsload.now();
					BoatEntity boat = boatload.now();

					timeslot.getBoats().add(boatId);
					timeslot.setAvailability(timeslot.getAvailability() + boat.getCapacity());

					DAO.ofy().save().entity(timeslot);
				}
			});

		} catch (NumberFormatException nfe) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

}
