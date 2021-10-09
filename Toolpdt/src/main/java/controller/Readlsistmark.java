package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
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

import Dao.Daokht;
import Dao.Daokithi;
import Model.DsThi;
import Model.KiHoc;
import Model.SinhVien;
import Services.ReadFileQuiz;
import Services.Readfilediemdanh;
import Services.Readfileonline;
import Services.Uploaddsthi;

@MultipartConfig()
@WebServlet("/Readlsistmark")
public class Readlsistmark extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Readfilediemdanh diemdanh;
	private Readfileonline online;
	private ArrayList<SinhVien> lstsvthi;
	private ArrayList<SinhVien> lstsvcamthi;
	private ReadFileQuiz quiz;
	private Daokht daokht;
	private Daokithi daokithi;
	private Uploaddsthi uploaddsthi;
	private List<DsThi> lstkht;
	private KiHoc k;
	int index;
	FileInputStream fileInputStream = null;
    OutputStream responseOutputStream = null;

	public Readlsistmark() {
		this.diemdanh = new Readfilediemdanh();
		this.online = new Readfileonline();
		this.lstsvcamthi = new ArrayList<SinhVien>();
		this.lstsvthi = new ArrayList<SinhVien>();
		this.quiz = new ReadFileQuiz();
		this.daokht=new Daokht();
		this.daokithi=new Daokithi();
		this.uploaddsthi=new Uploaddsthi();
		this.lstkht=new ArrayList<DsThi>();
		this.k=new KiHoc();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		index=Integer.parseInt(request.getParameter("id"));
		request.setAttribute("idkihoc", index);
		request.getRequestDispatcher("/views/formUpLoadDiem.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("utf-8");
		String loaimon = request.getParameter("Category");
		String loaifile=request.getParameter("downloadFile");
			String file=readurlfile(request, response);
			if(file==null) {
				System.out.print("xin mời chọn file ");
			}else if(!file.contains("xlsx")){
				System.out.print("file bạn chọn không phải là định dạng file excel ");
				response.sendRedirect("http://localhost:8080/Toolpdt/Readlsistmark?id="+index);
			}else {
			if (loaimon.equalsIgnoreCase("option2")) {
				if(this.online.kiemTra(file)>0) {
				this.lstsvcamthi = this.online.xuatsvcamthi();
				this.lstsvthi = this.online.xuatsvthi();
				}else {
					System.out.print("không phải định dạng của danh sách điểm online ");
					response.sendRedirect("http://localhost:8080/Toolpdt/Readlsistmark?id="+index);
				}
			} else if (loaimon.equalsIgnoreCase("option3")) {
				if(this.diemdanh.kiemTra(file)>0) {
				this.lstsvcamthi = this.diemdanh.xuatsvcamthi();
				this.lstsvthi = this.diemdanh.xuatsvthi();
				}else {
					System.out.print("không phải định dạng của danh sách điểm danh ");
					response.sendRedirect("http://localhost:8080/Toolpdt/Readlsistmark?id="+index);
				}
			} else {
				if(this.quiz.kiemTraQuiz(file)>0) {
				this.lstsvcamthi = this.quiz.getListSinhVienCamThi();
				this.lstsvthi = this.quiz.getListSinhVienDiThi();
				}else {
					System.out.print("không phải định dạng của danh sách điểm Quiz online ");
					response.sendRedirect("http://localhost:8080/Toolpdt/Readlsistmark?id="+index);
				}
			}
			}
		this.k=this.daokithi.findid(index);
		if(this.lstsvthi.size()!=0) {
        try {
        	this.lstkht=this.daokht.findkht(this.lstsvthi.get(0).getMamon(), this.lstsvthi.get(0).getLop(), k);
        	if(this.lstkht.size()==0) {
        		request.setAttribute("error", "Lớp và Môn học không tồn tại trong kế hoạch thi kì này");
        		response.sendRedirect("http://localhost:8080/Toolpdt/Readlsistmark?id="+index);
        		
        	}else {
        		if(loaifile.equalsIgnoreCase("fileExcel")) {
        		uploaddsthi.xuatlichthi(this.lstkht, this.lstsvthi, this.lstsvcamthi,response);
        		}else {
        			uploaddsthi.xuatlichthifileword(lstsvthi, response);
        		}
        	
			
        	}
		  } catch (Exception e) {
				e.printStackTrace();
		  }
		}else {
			System.out.print("file không hợp lệ2");
		}
        	this.lstsvcamthi.removeAll(lstsvcamthi);
        	this.lstsvthi.removeAll(lstsvthi);
        	this.lstkht.removeAll(lstkht);
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
			if(namefile.length()==0) {
				System.out.print("xin mời chọn file3 ");
				response.sendRedirect("http://localhost:8080/Toolpdt/Readlsistmark?id="+index);
			}else {
			part.write(realpath + System.getProperty("file.separator") + namefile);
			filename = realpath + System.getProperty("file.separator") + namefile;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("xin mời chọn file1 ");
			response.sendRedirect("http://localhost:8080/Toolpdt/Readlsistmark?id="+index);
		}
		return filename;
	}


}
