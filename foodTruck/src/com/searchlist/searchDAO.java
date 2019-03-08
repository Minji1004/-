package com.searchlist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.util.DBConn;

public class searchDAO {
	
	
	private Connection conn = DBConn.getConnection();

	
	
	// only 카테고리로만 검색해서 나오는 리스트.
	public List<foodtruck_cDTO> searchMenuCategory(String userAddr,String userCode) {
				
		foodtruck_cDTO dto = null;
		
		List<foodtruck_cDTO> list = new ArrayList<>();
		List<foodtruck_cDTO> pvlist = new ArrayList<>();
		
		PreparedStatement pstmt = null;
		
		ResultSet rs = null;
		
		String sql = "";
		
		
		
		sql = "select tname,post,avgscore,place from tb_foodtruck where foodcode = ?";
		
		
		try {
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, userCode);
			
			rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				
				dto = new foodtruck_cDTO(); 
				
				dto.setTname(rs.getString("tname"));
				
				dto.setPost(rs.getString("post"));
				
				dto.setAvgScore(rs.getString("avgscore"));
				
				dto.setPlace(rs.getString("place"));
				
				list.add(dto);
				
			}
			
			
			 MapAPI mapi = new MapAPI();
			 pvlist = mapi.searchTruckApi(list, userAddr); 
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}finally {
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			}
			
			if(rs!=null) {
				
				try {
					rs.close();
				} catch (Exception e2) {
			
				}
				
			}
		}

		
		return pvlist;
		
	}
	
	
	// 카테고리 + 거리 검색 메뉴
public List<foodtruck_cDTO> searchCodeNearTruck(String userAddr, String cartegoryCode) {
		
		
		List<foodtruck_cDTO> tlist = new ArrayList<>(); // 전체 푸드트럭 리스트 가져오기
		List<foodtruck_cDTO> pvlist = new ArrayList<>(); // 1km 이내의 푸드트럭 가져오기
		PreparedStatement pstmt = null;
		StringBuilder sb = new StringBuilder();
		ResultSet rs = null;

		try {
			sb.append("select f.tnum,f.tname, f.avgscore, f.place");
			sb.append(" from tb_foodtruck f");
			sb.append(" left outer join (select tnum, place from (select tnum, place, rank() over");
			sb.append("(partition by tnum order by openTime desc) 순위 from tb_analysis)");
			sb.append(" where 순위 = 1) i ON f.tnum = i.tnum WHERE foodcode = ?");

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, cartegoryCode);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				foodtruck_cDTO pv = new foodtruck_cDTO();
				pv.setTnum(rs.getString("tnum"));
				pv.setTname(rs.getString("tname"));
				pv.setAvgScore(rs.getString("avgscore"));


				tlist.add(pv);
			}
			 MapAPI mapi = new MapAPI();
			 pvlist = mapi.searchTruckApi(tlist, userAddr); // 1km 이내의 푸드트럭 가져오기
//서울특별시 마포구 서교동 447-5
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pvlist;
	}
	
	

	
	

	
}
