import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONObject;

/***
 * Servlet implementation
 * 
 * class SetAlarmRequestServlet
 */
@WebServlet("/VideoUploadServlet")
public class VideoUploadServlet extends HttpServlet{
	
	@Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getOutputStream()
                .println("Hurray !! VideoUploadServlet Works");
    }
	
	@Override
	protected void doPost(HttpServletRequest res, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("VideoUploadServlet: Received POST request");
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(res);
	    if( isMultipart ){
	    	System.out.println("hurray! is Multipart");
	    }
	    else {
	    	System.out.println("Booboo, not Multipart");
	    }
	 
		// Commons file upload classes are specifically instantiated
		FileItemFactory factory = new DiskFileItemFactory();
	 
		ServletFileUpload upload = new ServletFileUpload(factory);
		ServletOutputStream out = null;
	 
		try {
			System.out.println("Parsing request...");
			// Parse the incoming HTTP request
			// Commons takes over incoming request at this point
			// Get an iterator for all the data that was sent
			List items = upload.parseRequest(res);
			System.out.println("Request parsed");
			Iterator iter = items.iterator();
	 
			System.out.println("list has" + items.size() + items);
			// Set a response content type
//			response.setContentType("text/html");
	 
			// Setup the output stream for the return XML data
//			out = response.getOutputStream();
	 
			// Iterate through the incoming request data
			while (iter.hasNext()) {
				System.out.println("VideoUploadServlet: Iterate through the incoming request data");
				// Get the current item in the iteration
				FileItem item = (FileItem) iter.next();
	 
				// If the current item is an HTML form field
				if (item.isFormField()) {
					// Return an XML node with the field name and value
					System.out.println("this is a form data " + item.getFieldName());
	 
					// If the current item is file data
				} else {
					// Specify where on disk to write the file
					// Using a servlet init param to specify location on disk
					// Write the file data to disk
					// TODO: Place restrictions on upload data
					File disk = new File("/Users/kellycheng/Movies/"+item.getName());
					item.write(disk);
	 
					// Return an XML node with the file name and size (in bytes)
					//out.println(getServletContext().getRealPath("/WEB_INF"));
					System.out.println("this is a file with name: " + item.getName());
				}
			}
	 
			System.out.println("======End of VideoUploadServlet try block=======");
			// Close off the response XML data and stream
	 
//			out.close();
			// Rudimentary handling of any exceptions
			// TODO: Something useful if an error occurs
		} catch (FileUploadException fue) {
			System.out.println("======VideoUploadServlet Exception=====");
			fue.printStackTrace();
		} catch (IOException ioe) {
			System.out.println("======VideoUploadServlet Exception=====");
			ioe.printStackTrace();
		} catch (Exception e) {
			System.out.println("======VideoUploadServlet Exception=====");
			e.printStackTrace();
		}
		
		// Sending Response from server
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            PrintWriter responseOut = response.getWriter();
            JSONObject responseObj = new JSONObject();
            responseObj.put("status", "hello from server");
            responseOut.print(responseObj);

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
