package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.beanutils.BeanUtils;

import Dao.Daokht;
import Dao.Daokithi;
//import Dao.Dao;
//import Dao.Daokht;
import Model.DsThi;
import Model.KiHoc;
import Services.Readfilekht;

@MultipartConfig()
@WebServlet("/Uploadkht")
public class Uploadkht extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String namefile,tenfile;
	private Readfilekht read;
	private ArrayList<DsThi> lst;
	private KiHoc kihoc;
	private Daokithi dao;
	private Daokht daokht;
	int indexx;
    public Uploadkht() {
    	this.read=new Readfilekht();
    	this.lst=new ArrayList<DsThi>();
    	this.kihoc=new KiHoc();
    	this.dao=new Daokithi();
        this.daokht=new Daokht();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		request.getRequestDispatcher("/views/formDoiLichThi.jsp").forward(request, response);
		indexx=Integer.parseInt(request.getParameter("id"));
		System.out.print(indexx+"abc");
		request.setAttribute("id", indexx);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		request.setAttribute("id", indexx);
		try {
			readfilekht(request, response,indexx);
		} catch (NullPointerException ex) {
			request.setAttribute("error", "Xin mời chọn file");
			ex.printStackTrace();
			
		} catch (IOException er) {
			er.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		response.sendRedirect("/Toolpdt/Readlsistmark?id="+indexx);
	}

	private void readfilekht(HttpServletRequest request, HttpServletResponse response, int index)throws NullPointerException, IOException, ServletException {
		String name=readurlfile(request, response);
		kihoc=this.dao.findid(index);
		lst=this.read.read(name,kihoc);
		for(DsThi x:lst) {
			this.daokht.insert(x);
		}
		
	}
	private String readurlfile(HttpServletRequest request, HttpServletResponse response)throws NullPointerException, IOException, ServletException{
		String filename = null;
			Part part = request.getPart("namefile");
			String realpath = request.getServletContext().getRealPath(System.getProperty("file.separator") +"filemarks");
			String namefile = Path.of(part.getSubmittedFileName()).getFileName().toString();
			if (!Files.exists(Path.of(realpath))) {
				Files.createDirectory(Path.of(realpath));
			}
			part.write(realpath + System.getProperty("file.separator") + namefile);
			filename = realpath + System.getProperty("file.separator") + namefile;
		return filename;
	}
}
