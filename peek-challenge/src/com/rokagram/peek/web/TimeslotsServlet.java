package com.rokagram.peek.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rokagram.peek.dao.DAO;
import com.rokagram.peek.entity.BoatEntity;
import com.rokagram.peek.entity.BookingEntity;
import com.rokagram.peek.entity.TimeslotEntity;

public class TimeslotsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(TimeslotsServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			String dateParam = req.getParameter("date");

			if (dateParam != null) {

				List<TimeslotEntity> timeslots = null;
				if (dateParam.equals("*")) {
					timeslots = DAO.ofy().load().type(TimeslotEntity.class).list();

				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date = sdf.parse(dateParam);

					long time = date.getTime() / 1000;
					timeslots = DAO.ofy().load().type(TimeslotEntity.class).filter("start_time >=", time)
							.filter("start_time <", time + 86400L).order("start_time").list();

					List<Long> boatIds = new ArrayList<Long>();
					List<Long> timeslotIds = new ArrayList<Long>();

					for (TimeslotEntity timeslot : timeslots) {
						boatIds.addAll(timeslot.getBoats());
						timeslotIds.add(timeslot.getId());
					}

					// Map<Long, BoatEntity> boatMap =
					// DAO.ofy().load().type(BoatEntity.class).ids(boatIds);
					// List<BookingEntity> bookings =
					// DAO.ofy().load().type(BookingEntity.class)
					// .filter("timeslot_id in", timeslotIds).list();
					//
					// computeCapacityAndAvailability(timeslots, boatMap,
					// bookings);

					System.out.println("timeslots for " + dateParam);
					for (TimeslotEntity timeslot : timeslots) {
						System.out.println(timeslot.toString());
					}
					log.info(date + ": " + timeslots.toString());

				}
				ServletUtils.writeResponseJson(req, resp, timeslots);
			} else {
				resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			}
		} catch (ParseException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	private void computeCapacityAndAvailability(List<TimeslotEntity> timeslots, Map<Long, BoatEntity> boatMap,
			List<BookingEntity> bookings) {

		for (TimeslotEntity timeslot : timeslots) {

		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			TimeslotEntity timeslot = new TimeslotEntity();

			String startTimeParam = req.getParameter("timeslot[start_time]");
			String durationParam = req.getParameter("timeslot[duration]");

			long startTime = Long.parseLong(startTimeParam);
			Date temp = new Date(startTime * 1000);
			System.out.println(temp);
			timeslot.setStart_time(startTime);
			timeslot.setDuration(Integer.parseInt(durationParam));

			DAO.ofy().save().entity(timeslot).now();

			log.info("created " + timeslot);

			resp.setStatus(HttpServletResponse.SC_CREATED);
			ServletUtils.writeResponseJson(req, resp, timeslot);

		} catch (NumberFormatException nfe) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
