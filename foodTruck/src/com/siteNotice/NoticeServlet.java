package com.siteNotice;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.member.SessionInfo;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.util.FileManager;
import com.util.MyServlet;
import com.util.MyUtil;

@WebServlet("/siteNotice/*")
public class NoticeServlet extends MyServlet {
	private static final long serialVersionUID = 1L;
	
	private String pathname;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		String uri=req.getRequestURI();
		
		HttpSession session=req.getSession();
		
		// ������ ������ ���(pathname)
		String root=session.getServletContext().getRealPath("/");
		pathname=root+File.separator+"uploads"+File.separator+"notice";
		File f=new File(pathname);
		if(! f.exists()) {
			f.mkdirs();
		}
		
		if(uri.indexOf("list.do")!=-1) {
			list(req, resp);
		} else if(uri.indexOf("created.do")!=-1) {
			createdForm(req, resp);
		} else if(uri.indexOf("created_ok.do")!=-1) {
			createdSubmit(req, resp);
		} else if(uri.indexOf("article.do")!=-1) {
			article(req,resp);
		} else if(uri.indexOf("update.do")!=-1) {
			updateForm(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req,resp);
		} else if(uri.indexOf("delete.do")!=-1) {
			delete(req,resp);
		} else if(uri.indexOf("deleteFile.do")!=-1) {
			deleteFile(req,resp);
		} else if(uri.indexOf("download.do")!=-1) {
			download(req,resp);
		}
		
	}
	
	private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �Խù� ����Ʈ
		NoticeDAO dao=new NoticeDAO();
		MyUtil util=new MyUtil();		
		String cp=req.getContextPath();
		
		String page=req.getParameter("page");
		int current_page=1;
		if(page!=null)
			current_page=Integer.parseInt(page);
		
		String searchKey=req.getParameter("searchKey");
		String searchValue=req.getParameter("searchValue");
		if(searchKey==null) {
			searchKey="subject";
			searchValue="";
		}
		if(req.getMethod().equalsIgnoreCase("GET")) {
			searchValue=URLDecoder.decode(searchValue,"utf-8");
		}
		
		int rows=5;
		int dataCount, total_page;
		
		if(searchValue.length()!=0)
			dataCount= dao.dataCount(searchKey, searchValue);
		else
			dataCount= dao.dataCount();
		total_page=util.pageCount(rows, dataCount);
		
		if(current_page>total_page)
			current_page=total_page;
		
		int start=(current_page-1)*rows+1;
		int end=current_page*rows;
		
		List<NoticeDTO> list;
		if(searchValue.length()!=0)
			list= dao.listNotice(start, end, searchKey, searchValue);
		else
			list= dao.listNotice(start, end);
		
		// ������
		List<NoticeDTO> listNotice=null;
		listNotice = dao.listNotice();
		Iterator<NoticeDTO> itNotice=listNotice.iterator();
		while (itNotice.hasNext()) {
			NoticeDTO dto=itNotice.next();
			dto.setCreated(dto.getCreated().substring(0, 10));
		}
		
        Date endDate = new Date();
        long gap;
		// ����Ʈ �۹�ȣ �����
		int listNum, n=0;
		Iterator<NoticeDTO> it=list.iterator();
		while (it.hasNext()) {
			NoticeDTO dto=it.next();
			listNum=dataCount-(start+n-1);
			dto.setListNum(listNum);
			
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date beginDate = formatter.parse(dto.getCreated());
            
            	// ��¥����(��)
            	gap=(endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60* 1000);
            	dto.setGap(gap);

				// ��¥����(�ð�)
				gap=(endDate.getTime() - beginDate.getTime()) / (60*60* 1000);
				dto.setGap(gap);
			}catch(Exception e) {
			}
            
            dto.setCreated(dto.getCreated().substring(0, 10));
			
			n++;
		}
		
		String query="";
		String listUrl;
		String articleUrl;
		
		if(searchValue.length()==0) {
			listUrl=cp+"/siteNotice/list.do";
			articleUrl=cp+"/siteNotice/article.do?page="+current_page;
		} else {
			query="searchKey="+searchKey;
			query+="&searchValue="+URLEncoder.encode(searchValue,"utf-8");
			
			listUrl=cp+"/siteNotice/list.do?"+query;
			articleUrl=cp+"/siteNotice/article.do?page="+current_page+"&"+query;
		}
		
		String paging=util.paging(current_page, total_page, listUrl);
		
		// ������ jsp�� �ѱ� ������
		req.setAttribute("list", list);
		req.setAttribute("listNotice", listNotice);
		req.setAttribute("articleUrl", articleUrl);
		req.setAttribute("dataCount", dataCount);
		req.setAttribute("page", current_page);
		req.setAttribute("total_page", total_page);
		req.setAttribute("paging", paging);
		
		// JSP�� ������
		forward(req, resp, "/WEB-INF/views/siteNotice/list.jsp");
	}
	
	private void createdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �۾��� ��
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		String cp=req.getContextPath();
		
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		// admin�� ���� ���
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/siteNotice/list.do");
			return;
		}
		
		req.setAttribute("mode", "created");
		
		forward(req, resp, "/WEB-INF/views/siteNotice/created.jsp");
		
	}
	
	private void createdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �� ����
		NoticeDAO dao=new NoticeDAO();
		String cp=req.getContextPath();
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		// admin�� ���� ���
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/siteNotice/list.do");
			return;
		}
		
		String encType="utf-8";
		int maxFilesize=10*1024*1024;
		
	    MultipartRequest mreq=new MultipartRequest(
	    		req, pathname, maxFilesize, encType,
	    		new DefaultFileRenamePolicy()
	    		);
		
	    NoticeDTO dto=new NoticeDTO();
	    if(mreq.getParameter("notice")!=null)
	    	dto.setNotice(Integer.parseInt(mreq.getParameter("notice")));
	    dto.setName(mreq.getParameter("name"));
	    dto.setSubject(mreq.getParameter("subject"));
	    dto.setContent(mreq.getParameter("content"));
	    
	    if(mreq.getFile("upload")!=null) {
	    	dto.setSaveFilename(mreq.getFilesystemName("upload"));
	    	dto.setOriginalFilename(mreq.getOriginalFileName("upload"));
		    dto.setFileSize(mreq.getFile("upload").length());
		}
	    dao.insertNotice(dto);
		resp.sendRedirect(cp+"/siteNotice/list.do");
		
		
	}
	
	private void article(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
//		HttpSession session=req.getSession();
//		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		NoticeDAO dao=new NoticeDAO();
		String cp=req.getContextPath();
		
//		if(info==null) {
//			resp.sendRedirect(cp+"/member/login.do");
//			return;
//		}
		
		int noticeNum=Integer.parseInt(req.getParameter("noticeNum"));
		String page=req.getParameter("page");
		
		String searchKey=req.getParameter("searchKey");
		String searchValue=req.getParameter("searchValue");
		if(searchKey==null) {
			searchKey="subject";
			searchValue="";
		}
		searchValue=URLDecoder.decode(searchValue, "utf-8");
		
		dao.updateHitCount(noticeNum);
		
		NoticeDTO dto=dao.readNotice(noticeNum);
		if(dto==null) {
			resp.sendRedirect(cp+"/siteNotice/list.do?page="+page);
			return;
		}
		
		dto.setContent(dto.getContent().replaceAll("\n", "<br>"));
		
		// ������/������
		NoticeDTO preReadDto = dao.preReadNotice(dto.getNoticeNum(), searchKey, searchValue);
		NoticeDTO nextReadDto = dao.nextReadNotice(dto.getNoticeNum(), searchKey, searchValue);
		
		String query="page="+page;
		if(searchValue.length()!=0) {
			query+="&searchKey="+searchKey;
			query+="&searchValue="+URLEncoder.encode(searchValue, "utf-8");
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("preReadDto", preReadDto);
		req.setAttribute("nextReadDto", nextReadDto);
		req.setAttribute("query", query);
		req.setAttribute("page", page);
		
		forward(req, resp, "/WEB-INF/views/siteNotice/article.jsp");
	}
	
	private void updateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		NoticeDAO dao = new NoticeDAO();
		String cp = req.getContextPath();
		
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		String page=req.getParameter("page");
		int noticeNum=Integer.parseInt(req.getParameter("noticeNum"));
		
		NoticeDTO dto=dao.readNotice(noticeNum);
		if(dto==null) {
			resp.sendRedirect(cp+"/siteNotice/list.do?page="+page);
			return;
		}
		
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/notice/list.do?page="+page);
			return;
		}
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		
		req.setAttribute("mode", "update");
		
		forward(req, resp, "/WEB-INF/views/siteNotice/created.jsp");
		
	}
	
	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// ���� �Ϸ�
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		
		NoticeDAO dao=new NoticeDAO();
		String cp=req.getContextPath();
		
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		
		String encType="utf-8";
		int maxFilesize=10*2048*2048;
		
		MultipartRequest mreq=new MultipartRequest(req, pathname, maxFilesize, encType, new DefaultFileRenamePolicy());
		
		NoticeDTO dto=new NoticeDTO();
		
		int noticeNum=Integer.parseInt(mreq.getParameter("noticeNum"));
		String page=mreq.getParameter("page");
		
		dto.setNoticeNum(noticeNum);
		if(mreq.getParameter("notice")!=null)
			dto.setNotice(Integer.parseInt(mreq.getParameter("notice")));
		dto.setName(mreq.getParameter("name"));
		dto.setSubject(mreq.getParameter("subject"));
		dto.setContent(mreq.getParameter("content"));
		dto.setSaveFilename(mreq.getParameter("saveFilename"));
		dto.setOriginalFilename(mreq.getParameter("originalFilename"));
		dto.setFileSize(Long.parseLong(mreq.getParameter("fileSize")));
		
		if(mreq.getFile("upload")!=null) {
			FileManager.doFiledelete(pathname, mreq.getParameter("saveFilename"));
			
			dto.setSaveFilename(mreq.getFilesystemName("upload"));
			dto.setOriginalFilename(mreq.getOriginalFileName("upload"));
			dto.setFileSize(mreq.getFile("upload").length());
		}
		
		dao.updateNotice(dto);
		
		resp.sendRedirect(cp+"/siteNotice/list.do?page="+page);
	}
	
	private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// ���� ����
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		
		NoticeDAO dao = new NoticeDAO();
		String cp=req.getContextPath();
		
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		int noticeNum=Integer.parseInt(req.getParameter("noticeNum"));
		String page=req.getParameter("page");
		
		NoticeDTO dto=dao.readNotice(noticeNum);
		if(dto==null) {
			resp.sendRedirect(cp+"/siteNotice/list.do?page="+page);
			return;
		}
		
		//admin �� ���� ����
		if(! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/notice/list.do?page="+page);
			return;
		}
		
		if(dto.getSaveFilename()!=null && dto.getSaveFilename().length()!=0)
			   FileManager.doFiledelete(pathname, dto.getSaveFilename());
		
		
		dao.deleteNotice(noticeNum);
		
		resp.sendRedirect(cp+"/siteNotice/list.do?page="+page);
	}
	
	private void deleteFile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		NoticeDAO dao=new NoticeDAO();
		String cp=req.getContextPath();
	
		int noticeNum=Integer.parseInt(req.getParameter("noticeNum"));
		String page=req.getParameter("page");
		
		NoticeDTO dto=dao.readNotice(noticeNum);
		if(dto==null) {
			resp.sendRedirect(cp+"/siteNotice/list.do?page="+page);
			return;
		}
		
		if(info==null || ! info.getUserId().equals("admin")) {
			resp.sendRedirect(cp+"/notice/list.do?page="+page);
			return;
		}
		
		FileManager.doFiledelete(pathname, dto.getSaveFilename());
		dto.setOriginalFilename("");
		dto.setSaveFilename("");
		dto.setFileSize(0);
		
		dao.updateNotice(dto);
		
		req.setAttribute("dto", dto);
		req.setAttribute("page", page);
		
		req.setAttribute("mode", "update");

		forward(req, resp, "/WEB-INF/views/siteNotice/created.jsp");			
	}
	
	private void download(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// ���� �ٿ�ε�
		HttpSession session=req.getSession();
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		
		NoticeDAO dao=new NoticeDAO();
		String cp=req.getContextPath();
		
		if(info==null) {
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		int noticeNum=Integer.parseInt(req.getParameter("noticeNum"));
		String page=req.getParameter("page");
		
		NoticeDTO dto=dao.readNotice(noticeNum);
		if(dto==null) {
			resp.sendRedirect(cp+"/siteNotice/list.do"+page);
			return;
		}
		
		boolean b = FileManager.doFiledownload(dto.getSaveFilename(), dto.getOriginalFilename(), pathname, resp);
		
		if(! b) {
			resp.setContentType("text/html;charset=utf-8");
			PrintWriter pw=resp.getWriter();
			pw.print("<script>alert('���ϴٿ�ε� ����~!');history.back();</script>");
			return;
		}
		
				
	}
	
}
