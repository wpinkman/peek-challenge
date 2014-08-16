package com.rokagram.peek.web;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rokagram.peek.dao.DAO;
import com.rokagram.peek.entity.TimeslotEntity;

public class TimeslotsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			String dateParam = req.getParameter("date");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(dateParam);

			List<TimeslotEntity> timeslots = DAO.ofy().load().type(TimeslotEntity.class)
					.filter("start_time >=", date.getTime()).filter("start_time <", date.getTime() + 86400000L).list();

			ServletUtils.writeResponseJson(req, resp, timeslots);
		} catch (ParseException e) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			TimeslotEntity timeslot = new TimeslotEntity();
			timeslot.setStart_time(Long.parseLong(req.getParameter("timeslot[start_time]")));
			timeslot.setDuration(Integer.parseInt(req.getParameter("timeslot[duration]")));

			DAO.ofy().save().entity(timeslot).now();

			resp.setStatus(HttpServletResponse.SC_CREATED);
			ServletUtils.writeResponseJson(req, resp, timeslot);

		} catch (NumberFormatException nfe) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
