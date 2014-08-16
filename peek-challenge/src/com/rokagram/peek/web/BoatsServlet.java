package com.rokagram.peek.web;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rokagram.peek.dao.DAO;
import com.rokagram.peek.entity.BoatEntity;

public class BoatsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger log = Logger.getLogger(BoatsServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		List<BoatEntity> boats = DAO.ofy().load().type(BoatEntity.class).list();
		log.info(boats.toString());
		ServletUtils.writeResponseJson(req, resp, boats);
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

			log.info("added " + boat);

			resp.setStatus(HttpServletResponse.SC_CREATED);
			ServletUtils.writeResponseJson(req, resp, boat);

		} catch (NumberFormatException nfe) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

}
