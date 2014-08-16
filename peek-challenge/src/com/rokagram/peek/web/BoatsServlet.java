package com.rokagram.peek.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rokagram.peek.dao.DAO;
import com.rokagram.peek.entity.BoatEntity;

public class BoatsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		ServletUtils.writeResponseJson(req, resp, DAO.ofy().load().type(BoatEntity.class).list());
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String capacityParam = req.getParameter("boat[capacity]");
		String nameParam = req.getParameter("boat[name]");
		try {
			BoatEntity boat = new BoatEntity();
			boat.setCapacity(Integer.parseInt(capacityParam));
			boat.setName(nameParam);

			DAO.ofy().save().entity(boat).now();

			resp.setStatus(HttpServletResponse.SC_CREATED);
			ServletUtils.writeResponseJson(req, resp, boat);

		} catch (NumberFormatException nfe) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}
