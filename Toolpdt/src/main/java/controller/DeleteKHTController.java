package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.Daokht;

/**
 * Servlet implementation class DeleteKHTController
 */
@WebServlet("/deletekht")
public class DeleteKHTController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Daokht dao;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteKHTController() {
        super();
        // TODO Auto-generated constructor stub
        this.dao = new Daokht();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id = request.getParameter("khtID");
		int idKHT = Integer.parseInt(id);
		
		try {
			dao.delete(idKHT);
			response.sendRedirect("Updatekihoc");
			request.setAttribute("suc", "DELETE SUCCESSFUL!");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			request.setAttribute("error", "DELETE FAIL, TRY AGAIN!");
			request.getRequestDispatcher("views/ErrorForm.jsp").forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
