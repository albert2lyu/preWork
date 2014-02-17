package com.ljh.helper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class createDateTable
{
	public static void main(String[] args)
	{
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/patentdb","root","1234567890");
			Statement sta = con.createStatement();
			
			String sql = "SELECT DISTINCT PTT_DATE FROM patents";
			ResultSet reset = sta.executeQuery(sql);
			
			while(reset.next())
			{
				Date date = reset.getDate(1);
				String year = date.toString().substring(0, 4);
				String month = date.toString().substring(5, 7);
				String day = date.toString().substring(8, 10);
				
				Statement sta2 = con.createStatement();
				sta2.execute("insert into t_date (TIME_KEY,YEAR,MONTH,DATE) VALUES ('"+date+"','"+year+"','"+month+"','"+day+"')");
				sta2.close();
				
				System.out.println(reset.getDate(1).toString());
			}
			
			System.out.println("insert ok!!!");
			reset.close();
			sta.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
