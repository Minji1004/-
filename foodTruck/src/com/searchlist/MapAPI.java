package com.searchlist;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MapAPI {
	private final static String key = "b9e5c61f5febb775457a9c2f2c9ca712";

	// 좌표의 x값 반환
	public HttpURLConnection connectXML(String userAddr) {
		HttpURLConnection con = null;
		try {
			String addr = URLEncoder.encode(userAddr, "UTF-8");
			String apiurl = "https://dapi.kakao.com/v2/local/search/address.xml?query=" + addr;
			URL url = new URL(apiurl);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", "KakaoAK " + key);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public String returnXValue(String userAddr) {

		String x = null;
		BufferedReader br = null;

		try {
			HttpURLConnection con = connectXML(userAddr);

			int responseCode = con.getResponseCode();

			if (responseCode == 200) {
				// 정상호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				// 에러발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();

			String xType = "<x>";
			String xType2 = "</x>";
			int startx = response.toString().indexOf(xType);
			int endx = response.toString().indexOf(xType2);
			x = response.toString().substring(startx + 3, endx);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return x;
	}

	// 좌표의 y값 반환
	public String returnYValue(String userAddr) {

		String y = null;
		BufferedReader br = null;

		try {
			HttpURLConnection con = connectXML(userAddr);
			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				// 정상호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				// 에러발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();

			String yType = "<y>";
	        String yType2 = "</y>";
			int starty = response.toString().indexOf(yType);
			int endy = response.toString().indexOf(yType2);
	        
	        y = response.toString().substring(starty+3, endy);


			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return y;
	}

	// 찾기
	public List<foodtruck_cDTO> searchTruckApi(List<foodtruck_cDTO> pvl, String myAddr) {
		List<foodtruck_cDTO> pList = new ArrayList<>();

		try {
			for (foodtruck_cDTO vo : pvl) {
				if(vo.getPlace()==null) {
					continue;
				}
				String myX = returnXValue(myAddr);
				String myY = returnYValue(myAddr);

				String truckX = returnXValue(vo.getPlace());
				String truckY = returnYValue(vo.getPlace());

				double dist = distance(Double.parseDouble(myX), Double.parseDouble(myY), Double.parseDouble(truckX),
						Double.parseDouble(truckY));

				if (dist <= 1) {
					pList.add(vo);
				}
			}

		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return pList;

	}

	// 거리 계산
	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;

		dist = dist * 1.609344;

		return dist;
	}

	// decimal-> radian
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	// radian-> decimal
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

}
