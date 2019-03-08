package com.siteNotice;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.DBConn;

public class NoticeDAO {
	private Connection conn=DBConn.getConnection();
	
	public int insertNotice(NoticeDTO dto) {
		int result=0;
		PreparedStatement pstmt=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("INSERT INTO TB_SITENOTICE(");
			sb.append("noticeNum, notice,subject,content, saveFilename, originalFilename, filesize");
			sb.append(") VALUES (sitenotice_seq.NEXTVAL,?,?,?,?,?,?)");
			
			pstmt=conn.prepareStatement(sb.toString());
			pstmt.setInt(1, dto.getNotice());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getSaveFilename());
			pstmt.setString(5, dto.getOriginalFilename());
			pstmt.setLong(6, dto.getFileSize());
			
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
	
	public int dataCount() {
		int result=0;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		String sql;
		
		try {
			sql="SELECT NVL(COUNT(*), 0) FROM TB_SITENOTICE";
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
	
	// 검색 에서 전체의 개수
    public int dataCount(String searchKey, String searchValue) {
        int result=0;
        PreparedStatement pstmt=null;
        ResultSet rs=null;
        String sql;

        try {
        	if(searchKey.equalsIgnoreCase("created")) {
        		searchValue=searchValue.replaceAll("-", "");
        		sql="SELECT NVL(COUNT(*), 0) FROM TB_SITENOTICE WHERE TO_CHAR(created, 'YYYYMMDD') = ?  ";
        	} else {
        		sql="SELECT NVL(COUNT(*), 0) FROM TB_SITENOTICE WHERE  INSTR(" + searchKey + ", ?) >= 1 ";
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
    // 공지글
	public List<NoticeDTO> listNotice() {
		List<NoticeDTO> list=new ArrayList<NoticeDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT noticeNum");
			sb.append("            ,subject,  saveFilename, hitCount, created");
			sb.append("         FROM TB_SITENOTICE");
			sb.append("         WHERE notice=1 ");
			sb.append("	       ORDER BY noticeNum DESC");

			pstmt=conn.prepareStatement(sb.toString());
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto=new NoticeDTO();
				
				dto.setNoticeNum(rs.getInt("noticeNum"));
				dto.setSubject(rs.getString("subject"));
				dto.setSaveFilename(rs.getString("saveFilename"));
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
	
	
    // 게시물 리스트
	public List<NoticeDTO> listNotice(int start, int end) {
		List<NoticeDTO> list=new ArrayList<NoticeDTO>();
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		StringBuffer sb=new StringBuffer();
		
		try {
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("        SELECT noticeNum");
			sb.append("            ,subject,  saveFilename, hitCount, created");
			sb.append("         FROM TB_SITENOTICE");
			sb.append("	       ORDER BY noticeNum DESC");
			sb.append("    ) tb WHERE ROWNUM <= ? ");
			sb.append(") WHERE rnum >= ? ");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, end);
			pstmt.setInt(2, start);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				NoticeDTO dto=new NoticeDTO();
				
				dto.setNoticeNum(rs.getInt("noticeNum"));
				dto.setSubject(rs.getString("subject"));
				dto.setSaveFilename(rs.getString("saveFilename"));
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
	
	
	
	// 검색에서 리스트
    public List<NoticeDTO> listNotice(int start, int end, String searchKey, String searchValue) {
        List<NoticeDTO> list=new ArrayList<NoticeDTO>();

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
			sb.append("SELECT * FROM (");
			sb.append("    SELECT ROWNUM rnum, tb.* FROM (");
			sb.append("        SELECT noticeNum");
			sb.append("            ,subject,  saveFilename, hitCount, created");
			sb.append("         FROM TB_SITENOTICE");
			if(searchKey.equalsIgnoreCase("created")) {
				searchValue=searchValue.replaceAll("-", "");
				sb.append("           WHERE TO_CHAR(created, 'YYYYMMDD') = ? ");
			} else {
				sb.append("           WHERE INSTR(" + searchKey + ", ?) >= 1 ");
			}
			sb.append("	       ORDER BY noticeNum DESC");
			sb.append("    ) tb WHERE ROWNUM <= ?");
			sb.append(") WHERE rnum >= ?");
            
			pstmt=conn.prepareStatement(sb.toString());
            
			pstmt.setString(1, searchValue);
			pstmt.setInt(2, end);
			pstmt.setInt(3, start);
            
            rs=pstmt.executeQuery();
            
            while(rs.next()) {
				NoticeDTO dto=new NoticeDTO();
				
				dto.setNoticeNum(rs.getInt("noticeNum"));
				dto.setSubject(rs.getString("subject"));
				dto.setSaveFilename(rs.getString("saveFilename"));
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
    
    public int updateHitCount(int noticeNum) {
    	int result=0;
    	PreparedStatement pstmt=null;
    	String sql;
    	
    	try {
			sql = "UPDATE tb_siteNotice SET hitCount=hitCount+1 WHERE noticeNum=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, noticeNum);
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
	
	public int updateNotice(NoticeDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql="UPDATE tb_siteNotice SET notice=?, subject=?, content=?, saveFilename=?, originalFilename=?, filesize=? WHERE noticeNum=?";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, dto.getNotice());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getSaveFilename());
			pstmt.setString(5, dto.getOriginalFilename());
			pstmt.setLong(6, dto.getFileSize());
			pstmt.setInt(7, dto.getNoticeNum());
			
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
	
	public NoticeDTO readNotice(int noticeNum) {
		NoticeDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		sql = "SELECT noticeNum, notice, subject, content, saveFilename,originalFilename, filesize, hitCount, created ";
		sql+= "  FROM TB_SITENOTICE WHERE noticeNum=?";
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, noticeNum);

			
			rs = pstmt.executeQuery();
			
			if( rs.next()) {
				dto = new NoticeDTO();
				
				dto.setNoticeNum(rs.getInt("noticeNum"));
				dto.setNotice(rs.getInt("notice"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setSaveFilename(rs.getString("saveFilename"));
				dto.setOriginalFilename(rs.getString("originalFilename"));
				dto.setFileSize(rs.getLong("filesize"));
				dto.setCreated(rs.getString("created"));
				dto.setHitCount(rs.getInt("hitCount"));

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
	
	
	

    // 이전글
    public NoticeDTO preReadNotice(int noticeNum, String searchKey, String searchValue) {
    	NoticeDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(searchValue.length() != 0) {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT noticeNum, subject FROM TB_SITENOTICE ");
                if(searchKey.equalsIgnoreCase("created")) {
                	searchValue=searchValue.replaceAll("-", "");
                	sb.append("     WHERE (TO_CHAR(created, 'YYYYMMDD') = ?)  ");
                } else {
                	sb.append("     WHERE (INSTR(" + searchKey + ", ?) >= 1)  ");
                }
                sb.append("         AND (noticeNum > ? ) ");
                sb.append("         ORDER BY noticeNum ASC ");
                sb.append("      ) tb WHERE ROWNUM=1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setString(1, searchValue);
                pstmt.setInt(2, noticeNum);
			} else {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT noticeNum, subject FROM TB_SITENOTICE ");                
                sb.append("     WHERE noticeNum > ? ");
                sb.append("         ORDER BY noticeNum ASC ");
                sb.append("      ) tb WHERE ROWNUM=1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, noticeNum);
			}

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new NoticeDTO();
                dto.setNoticeNum(rs.getInt("noticeNum"));
                dto.setSubject(rs.getString("subject"));
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

    // 다음글
    public NoticeDTO nextReadNotice(int noticeNum, String searchKey, String searchValue) {
    	NoticeDTO dto=null;

        PreparedStatement pstmt=null;
        ResultSet rs=null;
        StringBuffer sb = new StringBuffer();

        try {
            if(searchValue.length() != 0) {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT noticeNum, subject FROM TB_SITENOTICE ");
                if(searchKey.equalsIgnoreCase("created")) {
                	searchValue=searchValue.replaceAll("-", "");
                	sb.append("     WHERE (TO_CHAR(created, 'YYYYMMDD') = ?)  ");
                } else {
                	sb.append("     WHERE (INSTR(" + searchKey + ", ?) >= 1)  ");
                }
                sb.append("         AND (noticeNum < ? ) ");
                sb.append("         ORDER BY noticeNum DESC ");
                sb.append("      ) tb WHERE ROWNUM=1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setString(1, searchValue);
                pstmt.setInt(2, noticeNum);
			} else {
                sb.append("SELECT ROWNUM, tb.* FROM ( ");
                sb.append("     SELECT noticeNum, subject FROM TB_SITENOTICE ");
                sb.append("     WHERE noticeNum < ? ");
                sb.append("         ORDER BY noticeNum DESC ");
                sb.append("      ) tb WHERE ROWNUM=1 ");

                pstmt=conn.prepareStatement(sb.toString());
                pstmt.setInt(1, noticeNum);
			}

            rs=pstmt.executeQuery();

            if(rs.next()) {
                dto=new NoticeDTO();
                dto.setNoticeNum(rs.getInt("noticeNum"));
                dto.setSubject(rs.getString("subject"));
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
    
    public int deleteNotice(int noticeNum) {
    	int result=0;
    	PreparedStatement pstmt=null;
    	String sql;
    	
    	try {
			sql="DELETE FROM TB_SITENOTICE WHERE noticeNum = ? ";
			
			pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, noticeNum);
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
    
	
}
