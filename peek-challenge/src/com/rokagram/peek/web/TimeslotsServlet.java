package com.rokagram.peek.web;

import java.io.IOException;

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

		ServletUtils.writeResponseJson(req, resp, DAO.ofy().load().type(TimeslotEntity.class).list());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			TimeslotEntity timeslot = new TimeslotEntity();
			timeslot.setStart_time(Long.parseLong(req.getParameter("start_time")));
			timeslot.setDuration(Integer.parseInt(req.getParameter("duration")));

			DAO.ofy().save().entity(timeslot).now();

			resp.setStatus(HttpServletResponse.SC_CREATED);
			ServletUtils.writeResponseJson(req, resp, timeslot);

		} catch (NumberFormatException nfe) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
}
