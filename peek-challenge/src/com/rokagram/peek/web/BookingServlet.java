package com.rokagram.peek.web;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.Work;
import com.rokagram.peek.dao.DAO;
import com.rokagram.peek.entity.AssignedBoat;
import com.rokagram.peek.entity.BookingEntity;
import com.rokagram.peek.entity.TimeslotEntity;

public class BookingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(BookingServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String timeslotIdParam = req.getParameter("booking[timeslot_id]");
		String sizeParam = req.getParameter("booking[size]");

		final long timeslotId = Long.parseLong(timeslotIdParam);
		final Integer size = Integer.parseInt(sizeParam);

		BookingEntity booking = DAO.ofy().transact(new Work<BookingEntity>() {
			public BookingEntity run() {

				BookingEntity booking = null;
				List<Object> entitiesToSave = new ArrayList<Object>();

				TimeslotEntity timeslot = DAO.ofy().load().type(TimeslotEntity.class).id(timeslotId).now();

				boolean booked = false;

				// first try to accommodate this booking on a boat that is
				// already going to be used for this time slot
				for (AssignedBoat boat : timeslot.getAssignedBoats().values()) {
					if (boat.getCustomer_count() > 0 && boat.isAvailable()) {
						if (boat.getCapacity() - boat.getCustomer_count() >= size) {
							boat.setCustomer_count(boat.getCustomer_count() + size);
							booked = true;
							break;
						}
					}
				}

				// need to allocate a boat from those available (assigned)
				// to the time slot
				Long allocatedBoatId = null;
				if (!booked) {
					for (Entry<Long, AssignedBoat> entry : timeslot.getAssignedBoats().entrySet()) {
						AssignedBoat boat = entry.getValue();
						if (boat.isAvailable()) {
							if (boat.getCapacity() - boat.getCustomer_count() >= size) {
								boat.setCustomer_count(boat.getCustomer_count() + size);
								booked = true;
								allocatedBoatId = entry.getKey();
								break;
							}
						}
					}
				}

				// if boat was allocated, mark is as unavailable for all
				// overlapping time slots
				if (allocatedBoatId != null) {

					Date startDate = new Date(timeslot.getStart_time() * 1000);
					String newString = new SimpleDateFormat(TimeslotsServlet.DATE_FORMAT).format(startDate);

					List<TimeslotEntity> timeslotsForDay = DAO.getTimeslotsForDay(newString);
					for (TimeslotEntity ts : timeslotsForDay) {

						if (timeslot.overlapsWith(ts) && ts.getAssignedBoats().containsKey(allocatedBoatId)) {
							AssignedBoat assignedBoat = ts.getAssignedBoats().get(allocatedBoatId);
							assignedBoat.setAvailable(false);
							entitiesToSave.add(ts);
						}
					}

				}

				if (booked) {
					booking = new BookingEntity();
					booking.setSize(size);
					booking.setTimeslot_id(timeslot.getId());

					entitiesToSave.add(booking);
					entitiesToSave.add(timeslot);

					DAO.ofy().save().entities(entitiesToSave).now();
				}
				return booking;
			}
		});

		if (booking != null) {
			log.info("created " + booking);
			resp.setStatus(HttpServletResponse.SC_CREATED);
			ServletUtils.writeResponseJson(resp, booking);
		} else {
			log.warning("failed to book part of " + size + " to requested timeslot");
			ServletUtils.addAccessControlAllowEverything(resp);
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BookingEntity> bookings = DAO.ofy().load().type(BookingEntity.class).list();
		log.info(bookings.toString());
		ServletUtils.writeResponseJson(resp, bookings);
	}

}
