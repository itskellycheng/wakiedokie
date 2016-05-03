import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import util.DatabaseIO;

/**
 * Servlet to serve response to users who turned off the alarm.
 * 
 * @author chaovictorshin-deh
 *
 */
@WebServlet("/WakeUpStatusServlet")
public class WakeUpStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public WakeUpStatusServlet() {
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
                .println("Hurray !! WakeUpStatusServlet Works");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        System.out.println("WakeUpStatusServlet: Received POST request");
        String owner_fb_id = "";
        String user2_fb_id = "";
        boolean imOwner = false;
        JSONParser parser = new JSONParser();
        StringBuffer jb = new StringBuffer();
        String line = null;

        boolean bothAwake = false;

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

        // Receiving Request from client.
        try {
            Object obj = parser.parse(jb.toString());
            JSONObject obj2 = (JSONObject) obj;
            owner_fb_id = obj2.get("waking_up_owner_fb_id").toString();
            user2_fb_id = obj2.get("waking_up_user2_fb_id").toString();
            String myRole = obj2.get("waking_up_my_role").toString();

            if (myRole.equals("owner")) {
                imOwner = true;
            } else {
                imOwner = false;
            }
            // update wake up status of alarm in db
            dbio.updateWakeUpStatus(owner_fb_id, user2_fb_id, imOwner);
            bothAwake = dbio.bothAreAwake(owner_fb_id, user2_fb_id);
        } catch (ParseException pe) {
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }

        // Sending Response from server
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            JSONObject responseObj = new JSONObject();

            if (bothAwake) {
                responseObj.put("bothAwake", "true");
            } else {
                responseObj.put("bothAwake", "false");
            }
            out.print(responseObj);

        } catch (IOException e) {

            try {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
                response.getWriter().close();
            } catch (IOException ioe) {
            }

        }
    }

}
