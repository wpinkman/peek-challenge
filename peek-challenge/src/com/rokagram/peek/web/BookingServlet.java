package com.rokagram.peek.web;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.Work;
import com.rokagram.peek.dao.DAO;
import com.rokagram.peek.entity.BookingEntity;
import com.rokagram.peek.entity.TimeslotEntity;

public class BookingServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(BookingServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String timeslotIdParam = req.getParameter("booking[timeslot_id]");
		String sizeParam = req.getParameter("booking[size]");
		try {
			final long timeslotId = Long.parseLong(timeslotIdParam);
			final Integer size = Integer.parseInt(sizeParam);

			BookingEntity booking = DAO.ofy().transact(new Work<BookingEntity>() {
				public BookingEntity run() {
					BookingEntity booking = new BookingEntity();
					TimeslotEntity timeslot = DAO.ofy().load().type(TimeslotEntity.class).id(timeslotId).now();
					booking.setSize(size);

					DAO.ofy().save().entities(booking, timeslot);
					return booking;
				}
			});

			log.info("created " + booking);

			resp.setStatus(HttpServletResponse.SC_CREATED);
			ServletUtils.writeResponseJson(req, resp, booking);

		} catch (NumberFormatException nfe) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<BookingEntity> bookings = DAO.ofy().load().type(BookingEntity.class).list();
		log.info(bookings.toString());
		ServletUtils.writeResponseJson(req, resp, bookings);
	}

}
