package com.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.MyServlet;

@WebServlet("/member/*")
public class MemberServlet extends MyServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void process(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		
		String uri=req.getRequestURI();
		
		// uri�� ���� �۾� ����
		if(uri.indexOf("login.do")!=-1) {
			loginForm(req, resp);
		} else if(uri.indexOf("login_ok.do")!=-1) {
			loginSubmit(req, resp);
		} else if(uri.indexOf("logout.do")!=-1) {
			logout(req, resp);
		} else if(uri.indexOf("member.do")!=-1) {
			memberForm(req, resp);
		} else if(uri.indexOf("member_ok.do")!=-1) {
			memberSubmit(req, resp);
		} else if(uri.indexOf("pwd.do")!=-1) {
			pwdForm(req, resp);
		} else if(uri.indexOf("pwd_ok.do")!=-1) {
			pwdSubmit(req, resp);
		} else if(uri.indexOf("update_ok.do")!=-1) {
			updateSubmit(req, resp);
		}
	}

	private void loginForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �α��� ��
		String path="/WEB-INF/views/member/login.jsp";
		forward(req, resp, path);
	}

	private void loginSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �α��� ó��
		// ���ǰ�ü. ���� ������ ������ ����(�α�������, ���ѵ�������)
		HttpSession session=req.getSession();
		
		MemberDAO dao=new MemberDAO();
		String cp=req.getContextPath();
		
		String userId=req.getParameter("id");
		String userPwd=req.getParameter("password");
		
		MemberDTO dto=dao.readMember(userId);
		
		if(dto!=null) {
			if(userPwd.equals(dto.getUserPwd()) ) {
				// �α��� ���� : �α��������� ������ ����
				// ������ �����ð��� 20�м���(�⺻ 30��)
				session.setMaxInactiveInterval(20*60);
				
				// ���ǿ� ������ ����
				SessionInfo info=new SessionInfo();
				info.setUserId(dto.getUserId());
				info.setUserName(dto.getUserName());
				info.setUserRoll(dto.getUserCode());
				info.setUserNum(dto.getUserNum());
				
				
				System.out.println(info.getUserRoll());
				
				// ���ǿ� member�̶�� �̸����� ����
				session.setAttribute("member", info);
				
				// ����ȭ������ �����̷�Ʈ
				resp.sendRedirect(cp);
				
				return;
			} 
		}
		
		// �α��� ������ ���(�ٽ� �α��� ������)
		String msg="���̵� �Ǵ� �н����尡 ��ġ���� �ʽ��ϴ�.";
		req.setAttribute("message", msg);
		
		forward(req, resp, "/WEB-INF/views/member/login.jsp");
	}
	
	private void logout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �α׾ƿ�
		HttpSession session=req.getSession();
		String cp=req.getContextPath();

		// ���ǿ� ����� ������ �����.
		session.removeAttribute("member");
		
		// ���ǿ� ����� ��� ������ ����� ������ �ʱ�ȭ �Ѵ�.
		session.invalidate();
		
		// ��Ʈ�� �����̷�Ʈ
		resp.sendRedirect(cp);
	}
	
	private void memberForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ��������
		req.setAttribute("title", "ȸ�� ����");
		req.setAttribute("mode", "created");
		
		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}

	private void memberSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ������ ó��
		MemberDAO dao=new MemberDAO();
		MemberDTO dto = new MemberDTO();
		dto.setUserId(req.getParameter("userId"));
		dto.setUserPwd(req.getParameter("userPwd"));
		dto.setUserName(req.getParameter("userName"));
		String tel1 = req.getParameter("tel1");
		String tel2 = req.getParameter("tel2");
		String tel3 = req.getParameter("tel3");
		if (tel1 != null && tel1.length() != 0 && tel2 != null
				&& tel2.length() != 0 && tel3 != null && tel3.length() != 0) {
			dto.setTel(tel1 + "-" + tel2 + "-" + tel3);
		}
		dto.setUserCode(req.getParameter("usercode"));

		int result = dao.insertMember(dto);
		if (result == 0) {
			String message = "ȸ�� ������ ���� �߽��ϴ�.";

			req.setAttribute("title", "ȸ�� ����");
			req.setAttribute("mode", "created");
			req.setAttribute("message", message);
			forward(req, resp, "/WEB-INF/views/member/member.jsp");
			return;
		}

		StringBuffer sb=new StringBuffer();
		sb.append("<b>"+dto.getUserName()+ "</b>�� ȸ�������� �Ǿ����ϴ�.<br>");
		sb.append("����ȭ������ �̵��Ͽ� �α��� �Ͻñ� �ٶ��ϴ�.<br>");
		
		req.setAttribute("title", "ȸ�� ����");
		req.setAttribute("message", sb.toString());
		
		forward(req, resp, "/WEB-INF/views/main/main.jsp");
	}
	
	private void pwdForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �н����� Ȯ�� ��
		HttpSession session=req.getSession();
		String cp=req.getContextPath();
		
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) {
			// �α׾ƿ������̸�
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		String mode=req.getParameter("mode");
		if(mode.equals("update"))
			req.setAttribute("title", "ȸ�� ���� ����");
		else
			req.setAttribute("title", "ȸ�� Ż��");
		
		req.setAttribute("mode", mode);
		
		forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
	}

	private void pwdSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// �н����� Ȯ��
		HttpSession session=req.getSession();
		String cp=req.getContextPath();
		MemberDAO dao=new MemberDAO();
		
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) { //�α׾ƿ� �� ���
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		
		// DB���� �ش� ȸ�� ���� ��������
		MemberDTO dto=dao.readMember(info.getUserId());
	
		if(dto==null) {
			session.invalidate();
			resp.sendRedirect(cp);
			return;
		}
		
		String userPwd=req.getParameter("password");
		String mode=req.getParameter("mode");
		if(! dto.getUserPwd().equals(userPwd)) {
			if(mode.equals("update"))
				req.setAttribute("title", "ȸ�� ���� ����");
			else
				req.setAttribute("title", "ȸ�� Ż��");
	
			req.setAttribute("mode", mode);
			req.setAttribute("message", "<span style='color:red;'>�н����尡 ��ġ���� �ʽ��ϴ�.</span>");
			forward(req, resp, "/WEB-INF/views/member/pwd.jsp");
			return;
		}
		
		if(mode.equals("delete")) {
			// ȸ��Ż��
			dao.deleteMember(info.getUserId());
			
			session.removeAttribute("member");
			session.invalidate();
			
			StringBuffer sb=new StringBuffer();
			sb.append("<b>"+dto.getUserName()+ "</b>�� ȸ��Ż�� ����ó���Ǿ����ϴ�.<br>");
			sb.append("�׵��� �̿��� �ּż� �����մϴ�.<br>");
			
			req.setAttribute("title", "ȸ�� Ż��");
			req.setAttribute("message", sb.toString());
			
			forward(req, resp, "/WEB-INF/views/main/main.jsp");
			
			return;
		}
		
		// ȸ����������
		
		if(dto.getTel()!=null) {
			String []s=dto.getTel().split("-");
			if(s.length==3) {
				dto.setTel1(s[0]);
				dto.setTel2(s[1]);
				dto.setTel3(s[2]);
			}
		}
		
		// ȸ������������ �̵�
		req.setAttribute("title", "ȸ�� ���� ����");
		req.setAttribute("dto", dto);
		req.setAttribute("mode", "update");
		forward(req, resp, "/WEB-INF/views/member/member.jsp");
	}

	private void updateSubmit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// ȸ������ ���� �Ϸ�
		HttpSession session=req.getSession();
		String cp=req.getContextPath();
		MemberDAO dao=new MemberDAO();
		
		SessionInfo info=(SessionInfo)session.getAttribute("member");
		if(info==null) { //�α׾ƿ� �� ���
			resp.sendRedirect(cp+"/member/login.do");
			return;
		}
		
		
		//MemberDTO dto2 = new MemberDTO();
		MemberDTO dto = new MemberDTO();

		//dto2= dao.readMember(req.getParameter("userId"));
		
		dto.setUserId(req.getParameter("userId"));
		dto.setUserPwd(req.getParameter("userPwd"));
	
		
		//dto.setUserCode(dto2.getUserCode());

		
		String tel1 = req.getParameter("tel1");
		String tel2 = req.getParameter("tel2");
		String tel3 = req.getParameter("tel3");
		if (tel1 != null && tel1.length() != 0 && tel2 != null
				&& tel2.length() != 0 && tel3 != null && tel3.length() != 0) {
			dto.setTel(tel1 + "-" + tel2 + "-" + tel3);
		}
		
		dao.updateMember(dto);
		
		StringBuffer sb=new StringBuffer();
		sb.append("<b>"+dto.getUserName()+ "</b>�� ȸ�� ������ ���� �Ǿ����ϴ�.<br>");
		sb.append("����ȭ������ �̵� �Ͻñ� �ٶ��ϴ�.<br>");
		
		req.setAttribute("title", "ȸ�� ���� ����");
		req.setAttribute("message", sb.toString());
		
		forward(req, resp, "/WEB-INF/views/main/main.jsp");
	}
	
	
}