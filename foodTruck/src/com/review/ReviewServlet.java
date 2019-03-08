package com.review;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.util.MyServlet;


@WebServlet("/main/*") // /review/*
public class ReviewServlet extends MyServlet{
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri = req.getRequestURI();
		
		// 
		if(uri.indexOf("main.do")!=-1) {
			review(req, resp);
		} else if(uri.indexOf("main_ok.do")!=-1) {
			reviewSubmit(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
			reviewCreate(req, resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req, resp);
		}
		
	}
	
	private void review(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 리뷰 리스트
		String cp = req.getContextPath();
		
		ReviewDAO dao = new ReviewDAO();
		
		int dataCount = dao.dataCount();
		
		List<ReviewDTO> list = dao.listReview();
		
		Iterator<ReviewDTO> it = list.iterator();
		while(it.hasNext()) {
			ReviewDTO dto = it.next();
			
			dto.setReviewContent(dto.getReviewContent().replaceAll(">", "&gt;"));
			dto.setReviewContent(dto.getReviewContent().replaceAll("<", "&lt;"));
			dto.setReviewContent(dto.getReviewContent().replaceAll("\n", "<br>"));
		}
		
		req.setAttribute("list", list);
		req.setAttribute("dataCount", dataCount);
		
		forward(req, resp, "/WEB-INF/views/main/main.jsp");
	}
	
	private void reviewSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 리뷰 저장
		
		HttpSession session = req.getSession(); 
		SessionInfo info = (SessionInfo)session.getAttribute("member");
		String cp = req.getContextPath();
		
		
		if(info==null) { 
			resp.sendRedirect(cp+"member/login.do"); 
			return; 
		}
		
		String cnum = "7";  // 나중에 세션에서 받아오는 거 처리
		String tnum = "1번"; // request attribute에서 받아오기
		
		ReviewDAO dao = new ReviewDAO();
		ReviewDTO dto = new ReviewDTO();
		
		//dto.setUsernum(info.getUserNum());
		//dto.setTnum(req.getAttribute("tnum"));
		
		dto.setUsernum(cnum);
		dto.setTnum(tnum);
		dto.setReviewContent(req.getParameter("reviewcontent"));
		dto.setReviewScore(Integer.parseInt(req.getParameter("reviewscore")));
		
		dao.insertReview(dto);
		
		resp.sendRedirect(cp+"/main/main.do"); // resp.sendRedirect(cp+"/review/review.do");
	}
	
	private void reviewCreate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 리뷰 수정
		
		
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 리뷰 삭제
		// HttpSession session = req.getSession();
		// SessionInfo info = (SessionInfo)session.getAttribute("member");
		
		String cp = req.getContextPath();
		/*
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		*/
		ReviewDAO dao = new ReviewDAO();
		
		int reviewnum = Integer.parseInt(req.getParameter("reviewnum"));
		
		//dao.deleteReview(reviewnum);
		
		//resp.sendRedirect(cp+"/main/main.do"); // resp.sendRedirect(cp+"/review/review.do");
		
		
	}
}











