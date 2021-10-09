package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.Daokht;
import Dao.Daokithi;
import Model.DsThi;
import Model.KiHoc;

/**
 * Servlet implementation class DeleteKHTController
 */
@WebServlet("/deletekht")
public class DeleteKHTController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Daokht dao;
	private int index;
	private KiHoc k;
	private List<DsThi> listDsThi;
	private Daokithi daokithi;
	private Daokht daokht;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteKHTController() {
		super();
		// TODO Auto-generated constructor stub
		this.dao = new Daokht();
		this.k = new KiHoc();
		this.listDsThi = new ArrayList<>();
		this.daokithi = new Daokithi();
		this.daokht = new Daokht();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		index = Integer.parseInt(request.getParameter("ID"));

		String id = request.getParameter("khtID");
		int idKHT = Integer.parseInt(id);

		try {
			dao.deleteKHT(idKHT);
			this.k = this.daokithi.findid(index);
			this.listDsThi = this.daokht.findbykihoc(k);

			request.setAttribute("suc", "DELETE SUCCESSFUL!");

			request.setAttribute("lst", this.listDsThi);
			request.setAttribute("idkihoc", index);
			request.getRequestDispatcher("/views/formKHT.jsp").forward(request, response);
			System.out.println("Xóa thành công");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "DELETE FAIL, TRY AGAIN!");
			// request.getRequestDispatcher("views/formKHT.jsp").forward(request, response);
			response.sendRedirect("http://localhost:8080/Toolpdt/Updatekihoc?id=" + index);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
