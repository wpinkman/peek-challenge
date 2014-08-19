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

import com.googlecode.objectify.Work;
import com.rokagram.peek.dao.DAO;
import com.rokagram.peek.entity.DayEntity;
import com.rokagram.peek.entity.TimeslotEntity;

public class TimeslotsServlet extends HttpServlet {

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(TimeslotsServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String dateParam = req.getParameter("date");

		if (dateParam != null) {

			List<TimeslotEntity> timeslots = DAO.getTimeslotsForDay(dateParam);

			log.info(dateParam + ": " + timeslots.toString());

			ServletUtils.writeResponseJson(req, resp, timeslots);

		} else {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	// public static Query<TimeslotEntity> createTimeslotQuery(String dateParam,
	// boolean transactionless)
	// throws ParseException {
	// SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
	// Date date = sdf.parse(dateParam);
	//
	// long time = date.getTime() / 1000;
	//
	// Query<TimeslotEntity> ret = null;
	//
	// if (transactionless) {
	// ret = DAO.ofy().transactionless().load().type(TimeslotEntity.class);
	// } else {
	// ret = DAO.ofy().load().type(TimeslotEntity.class);
	// }
	// ret = ret.filter("start_time >=", time).filter("start_time <", time +
	// 86400L).order("start_time");
	//
	// return ret;
	//
	// }

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String startTimeParam = req.getParameter("timeslot[start_time]");
		final String durationParam = req.getParameter("timeslot[duration]");

		final long startTime = Long.parseLong(startTimeParam);

		Date startDate = new Date(startTime * 1000);
		final String startDateString = new SimpleDateFormat(TimeslotsServlet.DATE_FORMAT).format(startDate);

		TimeslotEntity timeslot = DAO.ofy().transact(new Work<TimeslotEntity>() {

			@Override
			public TimeslotEntity run() {

				TimeslotEntity timeslot = new TimeslotEntity();
				timeslot.setStart_time(startTime);
				timeslot.setDuration(Integer.parseInt(durationParam));

				DayEntity day = DAO.ofy().load().type(DayEntity.class).id(startDateString).now();
				if (day == null) {
					day = new DayEntity();
					day.setDate(startDateString);
					System.out.println("Creating new day:" + startDateString);
				} else {
					System.out.println("Found day:" + startDateString);
				}

				day.getTimeslots().add(DAO.ofy().save().entity(timeslot).now());
				DAO.ofy().save().entity(day).now();

				return timeslot;
			}

		});

		log.info("created " + timeslot);

		resp.setStatus(HttpServletResponse.SC_CREATED);
		ServletUtils.writeResponseJson(req, resp, timeslot);

	}
}
