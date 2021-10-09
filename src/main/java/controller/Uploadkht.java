package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
	String namefile, tenfile;
	private Readfilekht read;
	private ArrayList<DsThi> lst;
	private List<DsThi> lstkht;
	private KiHoc kihoc;
	private Daokithi dao;
	private Daokht daokht;
	int indexx;

	public Uploadkht() {
		this.read = new Readfilekht();
		this.lst = new ArrayList<DsThi>();
		this.kihoc = new KiHoc();
		this.dao = new Daokithi();
		this.daokht = new Daokht();
		this.lstkht = new ArrayList<DsThi>();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		request.getRequestDispatcher("/views/formDoiLichThi.jsp").forward(request, response);
		indexx = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("id", indexx);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		request.setAttribute("id", indexx);
		try {
			readfilekht(request, response, indexx);
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} catch (IOException er) {
			er.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("/Toolpdt/Readlsistmark?id=" + indexx);
	}

	private void readfilekht(HttpServletRequest request, HttpServletResponse response, int index) throws Exception {
		String name = readurlfile(request, response);
		kihoc = this.dao.findid(index);
		if (name == null) {
			System.out.print("xin mời chọn file ");
		} else if (!name.contains("xlsx")) {
			System.out.print("file bạn chọn không phải là định dạng file excel ");
			response.sendRedirect("http://localhost:8080/Toolpdt/Uploadkht?id=" + index);
		} else {
			lst = this.read.read(name, kihoc);
			if (lst.size() < 0) {
				System.out.print("không phải định dạng kế hoạch thi ");
				response.sendRedirect("http://localhost:8080/Toolpdt/Uploadkht?id=" + index);
			} else
				delete(kihoc, index);
			for (DsThi x : lst) {
				this.daokht.insert(x);
			}
		}

	}

	private String readurlfile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String filename = null;
		try {
			Part part = request.getPart("namefile");
			String realpath = request.getServletContext().getRealPath("/filemarks");
			String namefile = Path.of(part.getSubmittedFileName()).getFileName().toString();
			if (!Files.exists(Path.of(realpath))) {
				Files.createDirectory(Path.of(realpath));
			}
			if (namefile.length() == 0) {
				System.out.print("xin mời chọn file3 ");
				response.sendRedirect("http://localhost:8080/Toolpdt/Readlsistmark?id=" + indexx);
			} else {
				part.write(realpath + System.getProperty("file.separator") + namefile);
				filename = realpath + System.getProperty("file.separator") + namefile;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("xin mời chọn file1 ");
			response.sendRedirect("http://localhost:8080/Toolpdt/Readlsistmark?id=" + indexx);
		}
		return filename;
	}

	private void delete(KiHoc k, int index) throws Exception {
		this.lstkht = this.daokht.findbykihoc(k);
		this.daokht.deletebykihoc(k);

	}
}
