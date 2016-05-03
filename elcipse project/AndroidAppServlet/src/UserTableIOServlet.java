import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.User;
import util.DatabaseIO;

/**
 * Servlet to serve the list of users in server database to the client.
 * 
 * @author chaovictorshin-deh
 *
 */
@WebServlet("/UserTableIOServlet")
public class UserTableIOServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserTableIOServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getOutputStream()
                .println("Hurray !! UserTableIOServlet Works");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        JSONParser parser = new JSONParser();
        String curr_user_fb_id = "";
        StringBuffer jb = new StringBuffer();
        ArrayList<User> users;
        String line = null;
        JSONArray jsonArray = null;
        DatabaseIO dbio = new DatabaseIO();
        if (!dbio.isConnected()) {
            System.out.println("Cannot Connect to Server");
        }

        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            /* report an error */ }

        try {
            // Getting info of current user
            Object obj = parser.parse(jb.toString());
            JSONObject obj2 = (JSONObject) obj;

            // client has a request to send back the users info of server
            if (obj2.get("request_for_user_table") != null) {
                curr_user_fb_id = obj2.get("request_for_user_table").toString();
                users = dbio.getUsers(curr_user_fb_id);
                jsonArray = new JSONArray();
                for (int i = 0; i < users.size(); i++) {
                    jsonArray.add(users.get(i).getJSONObject());
                }

                // write response back to client
                try {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    PrintWriter out = response.getWriter();
                    JSONObject responseObj = new JSONObject();
                    responseObj.put("user_table_server", jsonArray);
                    System.out.println(
                            "responding from server in UserTableServlet");
                    out.print(responseObj);
                    System.out.println(jsonArray.toString());

                } catch (IOException e) {

                    try {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().print(e.getMessage());
                        response.getWriter().close();
                    } catch (IOException ioe) {
                        /* ignore */
                    }

                }

            }

        } catch (ParseException pe) {
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }

    }

}
