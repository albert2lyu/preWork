package com.cjx.utils;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cjx.model.PatentDao;

public class DatabaseHelper {

	public static Connection getConnection() throws Exception
	{
		Class.forName("com.mysql.jdbc.Driver");
		return DriverManager.getConnection("jdbc:mysql://localhost/patentdb","root","123");
	}

	public static Boolean writeToDb(PatentDao pttDao)
	{
		try {
			
			Connection con = getConnection();
			Statement sta = con.createStatement();
			
			String sql = "INSERT INTO patents (PTT_NUM,APPLY_NUM,APPLY_DATE,PTT_NAME,PTT_DATE,PTT_MAIN_CLASS_NUM,PTT_CLASS_NUM,PROPOSER," +
					"PROPOSER_ADDRESS,INVENTOR,PTT_AGENCY_ORG,PTT_AGENCY_PERSON,PTT_ABSTRACT,CLASS_NUM_G06Q,INTERNATIONAL_APPLY,INTERNATIONAL_PUBLICATION,INTO_DATE,PTT_TYPE) VALUES ('" +
					pttDao.getPttNum() + "','" +
					pttDao.getApplyNum() + "','" +
					pttDao.getApplyDate() + "','" +
					pttDao.getPttName() + "','" +
					pttDao.getPttDate() + "','" +
					pttDao.getPttMainClassNum() + "','" +
					pttDao.getPttClassNum() + "','" +
					pttDao.getProposer() + "','" +
					pttDao.getProposerAddress() + "','" +
					pttDao.getInventor() + "','" +
					pttDao.getPttAgencyOrg() + "','" +
					pttDao.getPttAgencyPerson() + "','" +
					pttDao.getPttAbstract() + "','" +
					pttDao.getClassNumG06Q() + "','" +
					pttDao.getInternationalApply() + "','" +
					pttDao.getInternationalPublication() + "','" +
					pttDao.getIntoDate() + "','" +
					pttDao.getPttType() + "')";
			
			System.out.println(sql);
			sta.execute(sql);
			
			
			sta.close();
			con.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(pttDao.getPttNum());
			return false;
		}
		
	}
	

	public static void generateTDate()
	{
		Connection con;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = getConnection();
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
			
			reset.close();
			sta.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * 得到resultSet的行数
	 * 
	 * @param rs
	 * @return
	 */
	public static int getSize(ResultSet rs)
	{
		
		try {
			rs.last();
			return rs.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * 返回数据表的长度
	 * 
	 * @param dbName
	 * @return
	 */
	public static int getSize(String dbName)
	{
		int size;
		
		Connection con;
		try {
			con = getConnection();
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery("select count(*) from " + dbName);
			
			rs.first();
			size = rs.getInt(1);
			
			rs.close();
			sta.close();
			con.close();
			
			return size;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}
