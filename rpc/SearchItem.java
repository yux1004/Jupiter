package rpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
import external.TicketMasterAPI;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public SearchItem() {
	// TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    /*
     * protected void doGet(HttpServletRequest request, HttpServletResponse
     * response) throws ServletException, IOException { double lat =
     * Double.parseDouble(request.getParameter("lat")); double lon =
     * Double.parseDouble(request.getParameter("lon"));
     * 
     * String term = request.getParameter("term"); // TicketMasterAPI tmAPI = new
     * TicketMasterAPI(); // List<Item> items = tmAPI.search(lat, lon, term);
     * 
     * DBConnection connenction = DBConnectionFactory.getDBConnection(); List<Item>
     * items = connenction.searchItems(lat, lon, term);
     * 
     * JSONArray array = new JSONArray(); for (Item item : items) { JSONObject obj =
     * item.toJSONObject(); array.put(obj); } RpcHelper.writeJsonArray(response,
     * array); // response.setContentType("application/json"); //
     * response.addHeader("Access-Control-Allow-Origin", "*"); // PrintWriter out =
     * response.getWriter(); // // try { // array.put(new
     * JSONObject().put("username", "abcd")); // array.put(new
     * JSONObject().put("username", "1234")); // } catch (JSONException e) { //
     * e.printStackTrace(); // } // out.print(array); // out.close();
     * 
     * }
     */

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	// allow access only if session exists
	HttpSession session = request.getSession(false);
	if (session == null) {
	    response.setStatus(403);
	    return;
	}

	// optional
	String userId = session.getAttribute("user_id").toString();

	// String userId = request.getParameter("user_id");
	double lat = Double.parseDouble(request.getParameter("lat"));
	double lon = Double.parseDouble(request.getParameter("lon"));
	// Term can be empty or null.
	String term = request.getParameter("term");

	DBConnection conn = DBConnectionFactory.getDBConnection();
	List<Item> items = conn.searchItems(lat, lon, term);

	Set<String> favorite = conn.getFavoriteItemIds(userId);
	List<JSONObject> list = new ArrayList<>();
	try {
	    for (Item item : items) {
		// Add a thin version of restaurant object
		JSONObject obj = item.toJSONObject();
		// Check if this is a favorite one.
		// This field is required by frontend to correctly display favorite items.
		obj.put("favorite", favorite.contains(item.getItemId()));

		list.add(obj);
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	JSONArray array = new JSONArray(list);
	RpcHelper.writeJsonArray(response, array);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	// TODO Auto-generated method stub
	doGet(request, response);
    }

}
