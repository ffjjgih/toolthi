package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.Daokht;

@WebServlet("/deletekht")
public class DeleteKHTController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Daokht dao;
	
    public DeleteKHTController() {
        super();
        this.dao = new Daokht();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("khtID");
		int idKHT = Integer.parseInt(id);
		
		try {
			dao.delete(idKHT);
			response.sendRedirect("Updatekihoc");
			request.setAttribute("suc", "DELETE SUCCESSFUL!");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "DELETE FAIL, TRY AGAIN!");
			request.getRequestDispatcher("views/ErrorForm.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
