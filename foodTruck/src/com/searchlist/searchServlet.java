package com.searchlist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.member.MemberDAO;
import com.member.MemberDTO;
import com.util.MyServlet;



@WebServlet("/searchlist/*")
public class searchServlet extends MyServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();
		
		
		if(uri.indexOf("searchum.do")!=-1) {
			searchCategory(req,resp);
		}

	}
	
	protected void searchCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		searchDAO dao = new searchDAO();
		
		
		List<foodtruck_cDTO> list = new ArrayList<foodtruck_cDTO>();
		
		
		String userCode = req.getParameter("usercode");
		
		
		list = dao.searchMenuCategory("서울특별시 마포구 서교동 447-5",userCode);
	
		int size = list.size();
		
		req.setAttribute("size", size);
		
		req.setAttribute("list", list);
		
		
		forward(req, resp, "/WEB-INF/views/foodtruck/searchList.jsp");
	}
	
	
}
