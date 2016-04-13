import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
 
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
 
@WebServlet("/DoubleMeServlet")
public class DoubleMeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
 
    public DoubleMeServlet() {
        super();
 
    }
 
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
    	System.out.println("Received GET request");
    	response.getOutputStream().println("Hurray !! This Servlet Works");
 
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 
    	System.out.println("Received POST request");
    	
    	JSONParser parser = new JSONParser();
    	
    	StringBuffer jb = new StringBuffer();
    	String line = null;
    	try {
    		BufferedReader reader = request.getReader();
    		while ((line = reader.readLine()) != null)
    			jb.append(line);
    	} catch (Exception e) { /*report an error*/ }

    	try{
            Object obj = parser.parse(jb.toString());
            JSONObject obj2 = (JSONObject)obj;
            System.out.println(obj2.get("username"));
            System.out.println(obj2.get("password")); 
    	} catch(ParseException pe) {
    		System.out.println("position: " + pe.getPosition());
            System.out.println(pe);
    	}
//    	try {
//    		JSONObject jsonObject = JSONObject.fromObject(jb.toString());
//    	} catch (ParseException e) {
//    		// crash and burn
//    		throw new IOException("Error parsing JSON request string");
//    	}
//        try {
//            int length = request.getContentLength();
//            byte[] input = new byte[length];
//            ServletInputStream sin = request.getInputStream();
//            int c, count = 0 ;
//            while ((c = sin.read(input, count, input.length-count)) != -1) {
//                count +=c;
//            }
//            sin.close();
// 
//            String recievedString = new String(input);
//            response.setStatus(HttpServletResponse.SC_OK);
//            OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
// 
//            Integer doubledValue = Integer.parseInt(recievedString) * 2;
// 
//            writer.write(doubledValue.toString());
//            writer.flush();
//            writer.close();
// 
// 
// 
//        } catch (IOException e) {
// 
// 
//            try{
//                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//                response.getWriter().print(e.getMessage());
//                response.getWriter().close();
//            } catch (IOException ioe) {
//            }
//        }   
       }
 
}