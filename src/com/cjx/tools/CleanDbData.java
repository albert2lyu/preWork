package com.cjx.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.cjx.utils.DatabaseHelper;

public class CleanDbData {

	public static void main(String[] args) {
		
		CleanDbData cdd = new CleanDbData();
		cdd.cleanClassData();
		
	}
	
	/**
	 * 将patents表下的CLASS_NUM_G06Q字段格式化 “G06Q10/00”
	 * @return
	 */
	public Boolean cleanClassData()
	{
		try {
			Connection con = DatabaseHelper.getConnection();
			Statement sta = con.createStatement();
			
			String sql = "SELECT * FROM patents";
			ResultSet rs = sta.executeQuery(sql);
			
			while(rs.next())
			{
				String classNum = rs.getString("CLASS_NUM_G06Q");
				String pttNum = rs.getString("PTT_NUM");
				
				//格式化
				classNum = classNum.substring(0, 9);
				
				Statement sta2 = con.createStatement();
				sta2.execute("UPDATE patents SET CLASS_NUM_G06Q='" + classNum + "' WHERE PTT_NUM = '" + pttNum + "'");
				
				sta2.close();
			}
			
			rs.close();
			sta.close();
			con.close();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
