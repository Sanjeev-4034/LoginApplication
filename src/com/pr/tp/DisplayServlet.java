package com.pr.tp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DisplayServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String userid, username;
	Connection connection;
	PreparedStatement statement;
	ResultSet result;
	RequestDispatcher reqDis;
	HttpServletRequest request;
	HttpServletRequest response;
	public DisplayServlet() {
		super();
	}

	public boolean validateUserid_username(String uid, String uname) {

		 Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		 Matcher m = p.matcher(uname);
		 boolean b = m.find();
		 if (b)
		    return false;
		 else
		return true;
	}

	public void establishConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pramathi", "root", "121fa04034");
			statement = connection.prepareStatement("select * from user");
			result = statement.executeQuery();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getSession().setAttribute("errorMessage"," ");
		userid = request.getParameter("userid");
		username = request.getParameter("username");
		boolean validity = validateUserid_username(userid, username);
		if (validity == true) {
			establishConnection();
			try {
				while (result.next()) {
					if (!userid.isEmpty() || !username.isEmpty()) {
						if (userid.equals(result.getString(1)) && username.equals(result.getString(2))) {
							HttpSession session = request.getSession(true);
							session.setAttribute("username", username);
							reqDis = request.getRequestDispatcher("/welcome.jsp");
							reqDis.forward(request, response);
							return;
						} else if (userid != null && username != null) {
							request.getSession().setAttribute("errorMessage", "user id and user name do not match");
							reqDis = request.getRequestDispatcher("/index.jsp");
							reqDis.forward(request, response);
							return;
						}
					} else {
						request.getSession().setAttribute("errorMessage", "please enter the credentials");
						reqDis = request.getRequestDispatcher("/index.jsp");
						reqDis.forward(request, response);
						return;
					}
				}
			} catch (NullPointerException npe) {
				npe.printStackTrace();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		} else {
			request.getSession().setAttribute("errorMessage", "invalid username");
			reqDis = request.getRequestDispatcher("/index.jsp");
			reqDis.forward(request, response);
			return;

		}

	}
}
