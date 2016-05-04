import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/video/*")
public class VideoFileServlet extends HttpServlet{
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
		System.out.println("VideoFileServlet: GET request");
        String filename = URLDecoder.decode(request.getPathInfo().substring(1), "UTF-8");
        File file = new File(Constants.VIDEO_SAVE_DIRECTORY, filename);
        String fileSize = String.valueOf(file.length());
        response.setHeader("Content-Type", getServletContext().getMimeType(filename));
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("Content-Length", String.valueOf(file.length()));
        response.setHeader("Cache-Control", "max-age=21600");
        
//        response.setHeader("Content-Range", "bytes " + "0-" + fileSize + "/" + fileSize);
//        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
//        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT );
        Files.copy(file.toPath(), response.getOutputStream());
    }
}
