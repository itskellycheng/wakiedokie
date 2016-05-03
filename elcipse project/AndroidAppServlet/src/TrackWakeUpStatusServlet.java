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
 * This servlet reponds to periodic requests of a client who disabled the alarm
 * earlier than his buddy.
 * 
 * @author chaovictorshin-deh
 *
 */
@WebServlet("/TrackWakeUpStatusServlet")
public class TrackWakeUpStatusServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public TrackWakeUpStatusServlet() {
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
                .println("Hurray !! TrackWakeUpStatusServlet Works");
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        System.out.println("TrackWakeUpStatusServlet: Received POST request");
        String owner_fb_id = "";
        String user2_fb_id = "";
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
            owner_fb_id = obj2.get("track_wakeup_status_owner_fb_id")
                    .toString();
            user2_fb_id = obj2.get("track_wakeup_status_user2_fb_id")
                    .toString();

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
                responseObj.put("track_wakeup_status_bothAwake", "true");
                dbio.deleteAlarm(owner_fb_id, user2_fb_id);
            } else {
                responseObj.put("track_wakeup_status_bothAwake", "false");
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
