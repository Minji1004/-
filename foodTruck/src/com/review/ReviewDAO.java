package com.review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class ReviewDAO {
	private Connection conn = DBConn.getConnection();
	
	public int insertReview(ReviewDTO dto) { // ∏Æ∫‰ µÓ∑œ
		int result=0;
		PreparedStatement pstmt = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("INSERT INTO TB_REVIEW(reviewnum, usernum, tnum, reviewcontent, reviewscore) ");
			sb.append("VALUES(REVIEW_SEQ.NEXTVAL,?,?,?,?)");
			
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, dto.getUsernum());
			pstmt.setString(2, dto.getTnum());
			pstmt.setString(3, dto.getReviewContent());
			pstmt.setInt(4, dto.getReviewScore());
			
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
	
	public int dataCount() { // ∆‰¿Ã¬°
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*), 0) FROM tb_review";
			
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next())
				result=rs.getInt(1);
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return result;
		
	}
	
	public List<ReviewDTO> listReview() { // ∏Æ∫‰ ∏ÆΩ∫∆Æ
		List<ReviewDTO> list=new ArrayList<ReviewDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		StringBuffer sb = new StringBuffer();
		
		try {
			/*
			sb.append("SELECT * FROM (");
			sb.append("		SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("			SELECT reviewnum, usernum, tnum, reviewcontent, created, reviewscore");
			sb.append("			FROM tb_review r JOIN tb_user u ON r.usernum=u.usernum ");
			sb.append("					JOIN tb_foodtruck f ON r.tnum=f.tnum ");
			sb.append("		OREDER BY reviewnum DESC");
			sb.append("		) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");
			*/
			
			sb.append("SELECT reviewnum, usernum, tnum, reviewcontent, created, reviewscore FROM tb_review ORDER BY reviewnum DESC");
			// sb.append(" JOIN tb_user u ON u.usernum=r.usernum");
			// sb.append(" JOIN tb_foodtruck f ON f.tnum=r.tnum");
			
			pstmt = conn.prepareStatement(sb.toString());
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				ReviewDTO dto = new ReviewDTO();
				dto.setReviewNum(rs.getInt("reviewnum"));
				dto.setUsernum(rs.getString("usernum"));
				dto.setTnum(rs.getString("tnum"));
				dto.setReviewContent(rs.getString("reviewcontent"));
				dto.setCreated(rs.getString("created"));
				dto.setReviewScore(rs.getInt("reviewscore"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (Exception e2) {
				}
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (Exception e2) {
				}
			}
		}
		return list;
		
	}
	
	public int updateReview(ReviewDTO dto) { // ∏Æ∫‰ ºˆ¡§
		int result=0;
		PreparedStatement pstmt=null;
		StringBuffer sb = new StringBuffer();
		
		try {
			sb.append("UPDATE tb_review SET reviewcontent=?, modify_date=SYSDATE, reviewscore=? WHERE usernum=?");
			pstmt=conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, dto.getReviewContent());
			pstmt.setInt(2, dto.getReviewScore());
			pstmt.setString(3, dto.getUsernum());
			
			result=pstmt.executeUpdate();
			pstmt.close();
			pstmt=null;
			
			
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return result;
		
	}
	
	public int deleteReview(int reviewnum) { // ∏Æ∫‰ ªË¡¶
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		try {
			sql="DELETE FROM tb_review WHERE reviewnum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, reviewnum);
			
			result=pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e2) {
				}
			}
		}
		return result;
		
	}
	
}

