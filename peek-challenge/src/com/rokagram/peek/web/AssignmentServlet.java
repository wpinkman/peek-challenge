package com.rokagram.peek.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.VoidWork;
import com.rokagram.peek.dao.DAO;
import com.rokagram.peek.entity.AssignedBoat;
import com.rokagram.peek.entity.BoatEntity;
import com.rokagram.peek.entity.TimeslotEntity;

public class AssignmentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(AssignmentServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		final long timeslotId = Long.parseLong(req.getParameter("assignment[timeslot_id]"));
		final long boatId = Long.parseLong(req.getParameter("assignment[boat_id]"));

		DAO.ofy().transact(new VoidWork() {

			@Override
			public void vrun() {

				// load from datastore in parallel
				LoadResult<TimeslotEntity> timeslotLoad = DAO.ofy().load().type(TimeslotEntity.class).id(timeslotId);
				LoadResult<BoatEntity> boatLoad = DAO.ofy().load().type(BoatEntity.class).id(boatId);

				// terminate both load RPC's
				TimeslotEntity timeslot = timeslotLoad.now();
				BoatEntity boat = boatLoad.now();

				// yes.. like the one the proctologist had on Seinfeld
				AssignedBoat assBoat = new AssignedBoat();
				assBoat.setCapacity(boat.getCapacity());
				timeslot.getAssignedBoats().put(boat.getId(), assBoat);

				// check if boat is already allocated to an overlapping timeslot

				Date startDate = new Date(timeslot.getStart_time() * 1000);
				String newString = new SimpleDateFormat(TimeslotsServlet.DATE_FORMAT).format(startDate);

				List<TimeslotEntity> timeslotsForDay = DAO.getTimeslotsForDay(newString);

				for (TimeslotEntity ts : timeslotsForDay) {

					if (timeslot.overlapsWith(ts) && ts.getAssignedBoats().containsKey(boatId)) {
						AssignedBoat assignedBoat = ts.getAssignedBoats().get(boatId);
						if (assignedBoat.getCustomer_count() > 0) {
							assBoat.setAvailable(false);
						}
					}
				}

				DAO.ofy().save().entity(timeslot);
				log.info("boat " + boatId + " assinged to timeslot " + timeslot);
			}
		});

		ServletUtils.addAccessControlAllowEverythingLocalhost(resp);
	}

}
