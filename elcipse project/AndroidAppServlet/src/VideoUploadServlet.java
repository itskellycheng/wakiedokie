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
 * VideoUploadServlet - WakieDokie uploads video file to this servlet 
 * using multi-part form in POST request
 * 
 * What you need to change for this servlet to work:
 * Go to Constants class and set VIDEO_FILE_DIRECTORY to file path where you 
 * want videos to be saved.
 * Max file size that can be uploaded is set to 10MB.
 * 
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
	
	private boolean isMultipart;
	private String filePath;
	private int maxFileSize = 10 * 1024 * 1024;
	private int maxMemSize = 4 * 1024;
	private File file ;

	@Override
	public void init( ){
		// Get the file location where it would be stored.
		filePath = Constants.VIDEO_FILE_DIRECTORY;
	}
	
	@Override
	public void doPost(HttpServletRequest request, 
			HttpServletResponse response)
					throws ServletException, java.io.IOException {
		
		System.out.println("VideoUploadServlet: Received POST request");
		
		// Check that we have a file upload request
		isMultipart = ServletFileUpload.isMultipartContent(request);
		response.setContentType("text/html");
		java.io.PrintWriter out = response.getWriter( );
		if( !isMultipart ){
			System.out.println("Booboo, not Multipart");
			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");  
			out.println("</head>");
			out.println("<body>");
			out.println("<p>No file uploaded</p>"); 
			out.println("</body>");
			out.println("</html>");
			return;
		}
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File(filePath));

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax( maxFileSize );

		try{ 
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);

			// Process the uploaded file items
			Iterator i = fileItems.iterator();

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Servlet upload</title>");  
			out.println("</head>");
			out.println("<body>");
			while ( i.hasNext () ) 
			{
				FileItem fi = (FileItem)i.next();
				if ( !fi.isFormField () )	
				{
					// Get the uploaded file parameters
					String fieldName = fi.getFieldName();
					String fileName = fi.getName();
					String contentType = fi.getContentType();
					boolean isInMemory = fi.isInMemory();
					long sizeInBytes = fi.getSize();
					// Write the file
					System.out.println("Writing file...");
//					file = new File("/Users/kellycheng/Movies/"+ fileName);
					if( fileName.lastIndexOf("/") >= 0 ){
						file = new File( filePath + 
								fileName.substring( fileName.lastIndexOf("/"))) ;
					}else{
						file = new File( filePath + 
								fileName.substring(fileName.lastIndexOf("/")+1)) ;
					}
					fi.write( file ) ;
					out.println("Uploaded Filename: " + fileName + "<br>");
				}
			}
			out.println("</body>");
			out.println("</html>");
		}catch(Exception ex) {
			System.out.println(ex);
		}
	}
	

}
