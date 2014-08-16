package com.rokagram.peek.web;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.objectify.VoidWork;
import com.rokagram.peek.dao.DAO;
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

				TimeslotEntity timeslot = DAO.ofy().load().type(TimeslotEntity.class).id(timeslotId).now();

				if (!timeslot.getBoats().contains(boatId)) {
					timeslot.getBoats().add(boatId);

					DAO.ofy().save().entity(timeslot);
					log.info("boat " + boatId + " assinged to timeslot " + timeslot);
				}
			}
		});

	}

}
