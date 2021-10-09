package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import Dao.Daokht;
import Dao.Daokithi;
import Model.DsThi;
import Model.KiHoc;

@WebServlet("/updatekht")
public class UpdateKHTController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Daokht dao;
	private DsThi kht;
	private Daokithi daokithi;
	private int index;
	private KiHoc k;
	private List<DsThi> listDsThi;

	public UpdateKHTController() {
		super();
		this.dao = new Daokht();
		this.kht = new DsThi();
		this.daokithi = new Daokithi();
		this.k = new KiHoc();
		this.listDsThi = new ArrayList<DsThi>();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		index = Integer.parseInt(request.getParameter("id"));
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("index");
		int idkh = Integer.parseInt(id);
		DsThi kht = this.dao.getKHTById(idkh);
		if (kht != null) {
			request.setAttribute("detail", kht);
			request.setAttribute("suc", "GET Kế Hoạch Thi SUCCESSFUL!");
			request.getRequestDispatcher("views/loadToUpdate.jsp").forward(request, response);
		} else {
			request.setAttribute("error", "GET FAIL, PLEASE TRY AGAIN!");

			request.getRequestDispatcher("http://localhost:8080/Toolpdt/Updatekihoc?id=" + index);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try {
			BeanUtils.populate(this.kht, request.getParameterMap());
			this.kht.setId(Integer.parseInt(request.getParameter("idkht")));
			KiHoc k = this.daokithi.findkihoc(index);
			this.kht.setKiHoc(k);
			this.kht.setNgayThi(request.getParameter("ngay"));

			this.dao.update(kht);
			
			this.k = this.daokithi.findid(index);
			this.listDsThi = this.dao.findbykihoc(k);
			
			request.setAttribute("suc", "UPDATE SUCCESSFUL!");
			request.setAttribute("lst", this.listDsThi);
			//response.sendRedirect("http://localhost:8080/Toolpdt/Updatekihoc?id=" + index);
			request.getRequestDispatcher("/views/formKHT.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "UPDATE FAIL, TRY AGAIN!");
			response.sendRedirect("http://localhost:8080/Toolpdt/Updatekihoc?id=" + index);
		}
	}
}
