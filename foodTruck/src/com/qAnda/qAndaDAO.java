package com.qAnda;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class qAndaDAO {
	private Connection conn=DBConn.getConnection();
	
	public int insertqAnda(qAndaDTO dto, String mode) {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		int seq = 0;
		
		try {
			sql="SELECT qAnda_seq.NEXTVAL FROM dual";
			pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			seq=0;
			if(rs.next())
				seq=rs.getInt(1);
			rs.close();
			pstmt.close();
			rs=null;
			pstmt=null;
			
			dto.setBoardNum(seq);
			if(mode.equals("created")) {
				dto.setGroupNum(seq);
				dto.setOrderNo(0);
				dto.setDepth(0);
				dto.setParent(0);
			}else if(mode.equals("reply")) {
				updateOrderNo(dto.getGroupNum(), dto.getOrderNo());
				
				dto.setDepth(dto.getDepth()+1);
				dto.setOrderNo(dto.getOrderNo()+1);
			}
			
			sql="INSERT INTO tb_qAnda(boardNum, userNum, subject, content,groupNum, depth, orderNo, parent) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getBoardNum());
			pstmt.setString(2, dto.getUserNum());
			pstmt.setString(3, dto.getSubject());
			pstmt.setString(4, dto.getContent());
			pstmt.setInt(5, dto.getGroupNum());
			pstmt.setInt(6, dto.getDepth());
			pstmt.setInt(7, dto.getOrderNo());
			pstmt.setInt(8, dto.getParent());
			
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
	
	
	public int updateOrderNo(int groupNum, int orderNo) {
		int result = 0;
		PreparedStatement pstmt=null;
		String sql;
		
		sql = "UPDATE tb_qAnda SET orderNo=orderNo+1 WHERE groupNum = ? AND orderNo > ?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, groupNum);
			pstmt.setInt(2, orderNo);
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
	
	public List<qAndaDTO> listqAnda(int start, int end){
		List<qAndaDTO> list=new ArrayList<qAndaDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("         SELECT boardNum, q.userNum,id, ");
			sb.append("               subject, groupNum, orderNo, depth, hitCount,");
			sb.append("               TO_CHAR(created, 'YYYY-MM-DD') created");
			sb.append("               FROM tb_qAnda q");
			sb.append("               JOIN tb_user m ON q.userNum=m.userNum");
			sb.append("               ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ?");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				qAndaDTO dto=new qAndaDTO();
				
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserNum(rs.getString("userNum"));
				dto.setId(rs.getString("id"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setDepth(rs.getInt("depth"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
			}
		} catch (Exception e) {
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
		return list;
	}
	
	public List<qAndaDTO> listqAnda(int start, int end, String searchKey, String searchValue){
		List<qAndaDTO> list=new ArrayList<qAndaDTO>();
		
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("         SELECT boardNum, q.userNum, id,");
			sb.append("               subject, groupNum, orderNo, depth, hitCount,");
			sb.append("               TO_CHAR(created, 'YYYY-MM-DD') created ");
			sb.append("               FROM tb_qAnda q");
			sb.append("               JOIN tb_user m ON q.userNum=m.userNum");
			if(searchKey.equals("created")) {
				searchValue=searchValue.replaceAll("-", "");
				sb.append("           WHERE TO_CHAR(created, 'YYYYMMDD') = ? ");
			} else if(searchKey.equals("id")) {
				sb.append("           WHERE INSTR(id, ?) = 1 ");
			} else {
				sb.append("           WHERE INSTR(" + searchKey + ", ?) >= 1 ");
			}
			sb.append("               ORDER BY groupNum DESC, orderNo ASC ");
			sb.append("    ) tb WHERE ROWNUM <= ?");
			sb.append(") WHERE rnum >= ?");
			
			pstmt= conn.prepareStatement(sb.toString());
			
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				qAndaDTO dto=new qAndaDTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserNum(rs.getString("userNum"));
				dto.setId(rs.getString("id"));
				dto.setSubject(rs.getString("subject"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setDepth(rs.getInt("depth"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setCreated(rs.getString("created"));
				
				list.add(dto);
			}
		} catch (Exception e) {
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
		
		return list;
	}
	
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql; 
		
		try {
			sql="SELECT NVL(COUNT(*),0) FROM tb_qAnda";
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
		
		return result;
	}
	
	public int dataCount(String searchKey, String searchValue) {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			if(searchKey.equals("created")) {
				searchValue=searchValue.replace("-", "");
				sql="SELECT NVL(COUNT(*), 0) FROM tb_qAnda q join tb_user m ON q.userNum=m.userNum WHERE TO_CHAR(created, 'YYYYMMDD') = ? ";
        	} else if(searchKey.equals("userName")) {
        		sql="SELECT NVL(COUNT(*), 0) FROM tb_qAnda q JOIN tb_user m ON b.userNum=m.userNum WHERE INSTR(id, ?) = 1 ";
        	} else {
        		sql="SELECT NVL(COUNT(*), 0) FROM tb_qAnda q JOIN tb_user m ON b.userNum=m.userNum WHERE INSTR(" + searchKey + ", ?) >= 1 ";
        	}
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, searchValue);
			
			rs=pstmt.executeQuery();
			
			if(rs.next())
                result=rs.getInt(1);
        } catch (Exception e) {
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
        
        return result;
    }

	public int hitCount(int boardNum) {
		int result=0;
		PreparedStatement pstmt=null;
		String sql;
		
		sql = "UPDATE tb_qAnda SET hitCount=hitCount+1 WHERE boardNum=?";
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, boardNum);
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
	
	public qAndaDTO readqAnda(int boardNum) {
		qAndaDTO dto=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("select boardNum, q.userNum, id, subject, content, created, hitCount, groupNum, depth, orderNo, parent FROM tb_qAnda q");
			sb.append("     join tb_user m on q.userNum=m.userNum");
			sb.append("    where boardNum=?");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, boardNum);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				dto=new qAndaDTO();
				dto.setBoardNum(rs.getInt("boardNum"));
				dto.setUserNum(rs.getString("userNum"));
				dto.setId(rs.getString("id"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));
				dto.setGroupNum(rs.getInt("groupNum"));
				dto.setDepth(rs.getInt("depth"));
				dto.setOrderNo(rs.getInt("orderNo"));
				dto.setParent(rs.getInt("parent"));
				
			}
		} catch (Exception e) {
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
}
