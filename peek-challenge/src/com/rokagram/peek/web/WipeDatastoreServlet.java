package com.rokagram.peek.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rokagram.peek.dao.DAO;
import com.rokagram.peek.entity.BoatEntity;
import com.rokagram.peek.entity.BookingEntity;
import com.rokagram.peek.entity.TimeslotEntity;

public class WipeDatastoreServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		DAO.ofy().delete().keys(DAO.ofy().load().type(TimeslotEntity.class).keys());

		DAO.ofy().delete().keys(DAO.ofy().load().type(BookingEntity.class).keys());

		DAO.ofy().delete().keys(DAO.ofy().load().type(BoatEntity.class).keys());

		System.out.println("****** WIPED ENTIRE DATA STORE *****");
	}

}
