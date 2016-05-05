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

@WebServlet("/GetAlarmTypeServlet")
public class GetAlarmTypeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAlarmTypeServlet() {
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
                .println("Hurray !! GetAlarmTypeServlet Works");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        System.out.println("GetAlarmTypeServlet: Received POST request");

        JSONParser parser = new JSONParser();
        StringBuffer jb = new StringBuffer();
        String line = null;
        String type = "";
        String alarm_id = "";
        String ownership = "";
        DatabaseIO dbio = null;
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
            alarm_id = obj2.get("server_alarm_id").toString();
            ownership = obj2.get("ownership").toString();

            dbio = new DatabaseIO();
            if (!dbio.isConnected()) {
                System.out.println("Cannot Connect to Server");
            }
            // show all users in current database
            dbio.displayAllAlarms();

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

            type = dbio.getAlarmType(alarm_id, ownership);
            responseObj.put("type", type);
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
