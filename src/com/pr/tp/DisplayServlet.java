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

/**
 * @author sanjeevn
 *
 */
public class DisplayServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public DisplayServlet() {
	super();
    }
    public boolean validateUsername(String uname) {

	Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
	Matcher m = p.matcher(uname);
	boolean b = m.find();
	if (b)
	    return false;
	else
	    return true;
    }

    /**
     * @return
     */
    public ResultSet establishConnection() {
	ResultSet result = null;
	try {
	    Class.forName("com.mysql.jdbc.Driver");
	    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pramathi", "root",
		    "121fa04034");
	    PreparedStatement statement = connection.prepareStatement("select * from user");
	    result = statement.executeQuery();
	} catch (ClassNotFoundException cnfe) {
	    cnfe.printStackTrace();
	} catch (SQLException se) {
	    se.printStackTrace();
	}
	return result;
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
     *  javax.servlet.http.HttpServletResponse)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	RequestDispatcher reqDis;
	request.getSession().setAttribute("errorMessage", " ");
	String userid = request.getParameter("userid");
	String username = request.getParameter("username");
	boolean validity = validateUsername(username);
	if (validity == true) {
	    ResultSet result = establishConnection();
	    try {
		while (result.next()) {
		    if (!userid.isEmpty() || !username.isEmpty()) {
			if (userid.equals(result.getString(1)) && username.equals(result.getString(2))) {
			    reqDis = request.getRequestDispatcher("/welcome.jsp");
			    reqDis.forward(request, response);
			    return;
			} else {
			    request.setAttribute("errorMessage", "user id and user name do not match");
			    reqDis = request.getRequestDispatcher("/index.jsp");
			    reqDis.forward(request, response);
			    return;
			}
		    } else {
			request.setAttribute("errorMessage", "please enter the credentials");
			reqDis = request.getRequestDispatcher("/index.jsp");
			reqDis.forward(request, response);
			return;
		    }
		}
	    } catch (SQLException se) {
		se.printStackTrace();
	    }
	} else {
	    request.setAttribute("errorMessage", "invalid username");
	    reqDis = request.getRequestDispatcher("/index.jsp");
	    reqDis.forward(request, response);
	    return;

	}

    }
}
