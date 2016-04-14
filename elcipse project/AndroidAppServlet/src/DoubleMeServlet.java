import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import model.User;
import util.DatabaseIO;

/**
 * Servlet implementation class DoubleMeServlet
 */
@WebServlet("/DoubleMeServlet")
public class DoubleMeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DoubleMeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getOutputStream().println("Hurray !! This Servlet Works");
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        System.out.println("Received POST request");

        JSONParser parser = new JSONParser();

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            /* report an error */ }

        try {
            Object obj = parser.parse(jb.toString());
            JSONObject obj2 = (JSONObject) obj;
            // System.out.println(obj2.get("facebook_id"));
            // System.out.println(obj2.get("first_name"));
            // System.out.println(obj2.get("last_name"));
            User user = new User(obj2.get("facebook_id").toString(),
                    obj2.get("first_name").toString(),
                    obj2.get("last_name").toString());
            DatabaseIO dbio = new DatabaseIO();
            if (!dbio.isConnected()) {
                System.out.println("Cannot Connect to Server");
            }
            dbio.insertUserDb(user);
            // show all users in current database
            dbio.displayAllUsers();

        } catch (ParseException pe) {
            System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
        }
        // try {
        // int length = request.getContentLength();
        // byte[] input = new byte[length];
        // ServletInputStream sin = request.getInputStream();
        // int c, count = 0;
        // while ((c = sin.read(input, count, input.length - count)) != -1) {
        // count += c;
        // }
        // sin.close();
        //
        // String recievedString = new String(input);
        // response.setStatus(HttpServletResponse.SC_OK);
        // OutputStreamWriter writer = new OutputStreamWriter(
        // response.getOutputStream());
        //
        // Integer doubledValue = Integer.parseInt(recievedString) * 2;
        //
        // writer.write(doubledValue.toString());
        // writer.flush();
        // writer.close();
        //
        // } catch (IOException e) {
        //
        // try {
        // response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        // response.getWriter().print(e.getMessage());
        // response.getWriter().close();
        // } catch (IOException ioe) {
        // }
        // }
    }

}
