package com.cloudtour.twittmap.web;

import java.io.IOException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SnsSubscriber
 */
public class SnsSubscriber extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String savedMessage = null;
	private String savedSnsMessage = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SnsSubscriber() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		response.getWriter().println("</p>this servlet is working!</p>");
		response.getWriter().println("</p>message: "+ savedMessage+ "</p>");
		response.getWriter().println("</p>sns message: "+ savedSnsMessage+ "</p>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String messagetype = request.getHeader("x-amz-sns-message-type");
		if (messagetype == null) {
			return;
		}
//		Scanner scan = new Scanner(request.getInputStream());
//		StringBuilder builder = new StringBuilder();
//		while (scan.hasNextLine()) {
//			builder.append(scan.nextLine());
//		}
//		scan.close();
//		message = "<p>" + builder.toString() + "</p>";
//		return;
		
		if (messagetype.equals("Notification")) {
			Scanner scanner = new Scanner(request.getInputStream());
			StringBuilder message = new StringBuilder();
			while (scanner.hasNextLine()) {
				message.append(scanner.nextLine());
			}
			scanner.close();
			
			SnsParser parser = new SnsParser();
			SnsMessage snsMsg = parser.parse(message.toString());
			savedMessage = message.toString();
			savedSnsMessage = snsMsg.toString();
			SnsCache.getInstance().update(snsMsg);
		} else if (messagetype.equals("SubscriptionConfirmation")) {
			Scanner scan = new Scanner(request.getInputStream());
			StringBuilder builder = new StringBuilder();
			while (scan.hasNextLine()) {
				builder.append(scan.nextLine());
			}
			scan.close();
			savedMessage = "<p>" + builder.toString() + "</p>";
		}
	}

}
