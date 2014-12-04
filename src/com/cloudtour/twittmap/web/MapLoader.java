package com.cloudtour.twittmap.web;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Timestamp;
import java.util.*;

/**
 * Servlet implementation class MapLoader
 */
public class MapLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MapLoader() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		StringBuffer jb = new StringBuffer();
		String line = null;
		BufferedReader reader = request.getReader();
		while ((line = reader.readLine()) != null)
			jb.append(line);

		String[] ss = jb.toString().split(";");
		String responseText = "";
		System.out.println(ss.length);
		if (ss.length == 2)
			responseText = getResponseFromDB(ss);
		else if (ss.length == 1)
			responseText = getResponseFromCache(ss);
		else
			return;
		
		response.setContentType("text/html");
		response.getWriter().write(responseText);
	}
	
	private String getResponseFromDB(String[] ss) {
		String[] begin = ss[0].split(" ");
		String[] end = ss[1].split(" ");

		DBManager ma = new DBManager();
		ma.getDirver();
		ma.connectAWS();
		
		List<List<String>> result = ma.queryLatLng(begin[0], begin[1], end[0],
				end[1]);
		ma.shutdown();
		System.out.println(result.size());

		// data
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < result.size(); ++i) {
			List<String> pair = result.get(i);
			sb.append(pair.get(0)).append(",");
			sb.append(pair.get(1)).append(";");
		}
		
		return sb.toString();
	}

	private String getResponseFromCache(String[] ss) {
		List<SnsMessage> list = null;
		if (ss[0].equals("positive"))
			list = SnsCache.getInstance().getPositive();
		else if (ss[0].equals("negative")) 
			list = SnsCache.getInstance().getNegative();
		else 
			list = SnsCache.getInstance().getNeutral();
		
		StringBuilder sb = new StringBuilder();
		for (SnsMessage msg : list) {
			sb.append(msg.lat).append(",");
			sb.append(msg.lng).append(";");
		}
		return sb.toString();
	}
}
