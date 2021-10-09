package controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.Daokithi;
import Model.KiHoc;

@WebServlet({ "/Home", "/Home/insert" })
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private KiHoc k;
	private Daokithi kithi;
	private List<KiHoc> lstkh;

	public Home() {
		super();
		this.k = new KiHoc();
		this.kithi = new Daokithi();
		this.lstkh = new ArrayList<KiHoc>();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		/* this.kithi.updatekihoc(); */
		this.lstkh = this.kithi.getkihoc();
		if (lstkh != null) {
			request.setAttribute("lstkythi", lstkh);
			request.getRequestDispatcher("/views/HomeForm.jsp").forward(request, response);
		} else {
			request.getRequestDispatcher("/views/HomeForm.jsp").forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		String url = request.getRequestURL().toString();
		if (url.contains("insert")) {
			insert(request, response);
			KiHoc k1 = this.kithi.getkihoc().get(0);
			response.sendRedirect("http://localhost:8080/Toolpdt/Uploadkht?id=" + k1.getIdhk());
		}

	}

	private void insert(HttpServletRequest request, HttpServletResponse response) {
		LocalDate datetime = LocalDate.now();
		java.sql.Date datetimesql = java.sql.Date.valueOf(datetime);
		k.setBlockid(request.getParameter("BLOCKID"));
		k.setNgayTao(datetimesql);
		k.setTrangThai("Đang Hoạt động");
		k.setKyHoc(request.getParameter("kyhoc"));
		k.setNamHoc(request.getParameter("nam_hoc"));
		this.kithi.insert(k);

	}
}
