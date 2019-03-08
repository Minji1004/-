package com.member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.util.DBConn;

public class MemberDAO {
	private Connection conn=DBConn.getConnection();
	
	public MemberDTO readMember(String userId) {
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "select userNum, id, pwd, tel, blackList, userCode from tb_user where id = ?";
	
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				dto=new MemberDTO();
				dto.setUserNum(rs.getString("userNum"));
				dto.setUserId(rs.getString("id"));
				dto.setUserPwd(rs.getString("Pwd"));
				dto.setUserCode(rs.getString("userCode"));
				dto.setTel(rs.getString("tel"));
				if(dto.getTel()!=null) {
					String[] ss=dto.getTel().split("-");
					if(ss.length==3) {
						dto.setTel1(ss[0]);
						dto.setTel2(ss[1]);
						dto.setTel3(ss[2]);
					}
				}
			}
			
		}
		 catch (Exception e) {
			System.out.println(e.toString());
			
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
				
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return dto;
	}
	
	public int insertMember(MemberDTO dto) {
		int result = 0; 
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO TB_USER(usernum, id, pwd, tel, blacklist, usercode) VALUES(user_seq.nextval,?, ?, ?, ? ,?)";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getUserPwd());
			pstmt.setString(3, dto.getTel());
			pstmt.setString(4, "N");
			pstmt.setString(5, dto.getUserCode());
			
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return result;
	}
	
	public int updateMember(MemberDTO dto) {
		
		
		int result=0;
		PreparedStatement pstmt=null;
		StringBuffer sb=new StringBuffer();
		
		try { 
			sb.append("UPDATE TB_USER SET pwd=?,tel=? WHERE id=?");
			pstmt=conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getUserPwd());
			pstmt.setString(2, dto.getTel());
			pstmt.setString(3, dto.getUserId());
		
			
			result=pstmt.executeUpdate();
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return result;
	}
	
	public int deleteMember(String userId) {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="UPDATE member1 SET enabled=0 WHERE userId=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			result=pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
			
			sql="DELETE FROM  member2 WHERE userId=?";
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			result=pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println();
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
		}
		
		return result;
	}
}
