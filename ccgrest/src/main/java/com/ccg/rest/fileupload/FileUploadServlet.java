package com.ccg.rest.fileupload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.springframework.beans.factory.annotation.Autowired;

import com.ccg.services.data.CCGDBService;

/**
 * Servlet implementation class FileUploadServlet
 */
@WebServlet("/upload")
@MultipartConfig
public class FileUploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	CCGDBService dataservice;

    /**
     * Default constructor. 
     */
    public FileUploadServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		String action = request.getParameter("action");
		ArticleHelper helper = new ArticleHelper();
		System.out.println(action+"<-----");
		if("confirmed".equals(action)){			
			String base64data = request.getParameter("requestData");
			String json = new String(Base64.getDecoder().decode(base64data));
			System.out.println("============>>" + json + "<<=====");
			
			
			RequestData data = JsonHelper.fromJson(json, RequestData.class);
			helper.saveArticle(data);
			response.getWriter().println("Article Saved");
		}else{
		
			String description = request.getParameter("description");

			Part filePart = request.getPart("file");
			String filename = System.currentTimeMillis() + "_" +filePart.getSubmittedFileName();
			InputStream is = filePart.getInputStream();
			
			// save file to repository
			String path = helper.saveTempFile(is, filename);
			//helper.saveTempFile(is, filename);
			
			System.out.println("description: " + description);
			System.out.println("filename: " + filename);
			
			String pattern = request.getParameter("pattern");
			String type = request.getParameter("type");
			String company = request.getParameter("company");
			String acceptStatus = request.getParameter("status");
			
			RequestData data = new RequestData();
			data.setAcceptStatus(acceptStatus);
			data.setArticleType(type);
			data.setCompany(company);
			data.setFilename(filename);
			data.setFilepath(path);
			data.setPattern(pattern);
			

			String categoryString = helper.getCategoryForVerify(data);
			categoryString = "<pre>" + categoryString + "</pre>";//.replaceAll("\n", "<br />");
			
			//request.setAttribute("category", categoryString);
			String json = JsonHelper.toJson(data);
			System.out.println(json);
			String base64data = Base64.getEncoder().encodeToString(json.getBytes());
			//request.setAttribute("requestData", base64data);
			//this.getServletContext().getRequestDispatcher("/verify.jsp").forward(request, response);
			//String responseJson = "{'success':true, 'category':'" + categoryString + "', requestBase64:'" + base64data + "'}";
			ExtjsResponse er = new ExtjsResponse();
			er.success = true;
			er.category = categoryString;
			er.base64Request = base64data;
			String responseJson = JsonHelper.toJson(er);
			System.out.println(responseJson);
			response.setContentType("application/json");
			response.getWriter().println(responseJson);
		}
	
	}

}

class ExtjsResponse{
	boolean success;
	String category;
	String base64Request;
}


