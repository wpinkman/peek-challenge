package com.rokagram.peek.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.googlecode.objectify.cmd.LoadType;
import com.rokagram.peek.dao.DAO;
import com.rokagram.peek.entity.BoatEntity;
import com.rokagram.peek.entity.BookingEntity;
import com.rokagram.peek.entity.TimeslotEntity;

public class HomeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		DAO.ofy().delete().keys(DAO.ofy().load().type(TimeslotEntity.class).keys());

		DAO.ofy().delete().keys(DAO.ofy().load().type(BookingEntity.class).keys());

		DAO.ofy().delete().keys(DAO.ofy().load().type(BoatEntity.class).keys());

		System.out.println("****** WIPED ENTIRE DATA STORE *****");
		resp.sendRedirect("/");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html");

		PrintWriter writer = resp.getWriter();

		writer.println("<form  method=\"post\"><input value=\"DELETE ALL DATA\" type=\"submit\"/></form>");

		writeH4(writer, "Boats");
		LoadType<BoatEntity> boats = DAO.ofy().load().type(BoatEntity.class);
		for (BoatEntity boat : boats) {
			writer.println(boat + "<br>");
		}
		writer.println();
		writeJson(writer, boats);

		writeH4(writer, "Timeslots");
		LoadType<TimeslotEntity> timeslots = DAO.ofy().load().type(TimeslotEntity.class);
		for (TimeslotEntity ts : timeslots) {
			writer.println(ts + "<br>");
		}

		writer.println();
		writeJson(writer, timeslots);
		writeH4(writer, "Bookings");
		LoadType<BookingEntity> bookings = DAO.ofy().load().type(BookingEntity.class);
		for (BookingEntity booking : bookings) {
			writer.println(booking + "<br>");
		}
		writeJson(writer, bookings);
	}

	private void writeJson(PrintWriter writer, Object object) throws JsonProcessingException {

		writer.println("<pre>" + ServletUtils.getIndentedJSON(object) + "</pre>");
	}

	private void writeH4(PrintWriter writer, String heading) {
		writer.println("<h4>" + heading + "</h4>");
	}
}
