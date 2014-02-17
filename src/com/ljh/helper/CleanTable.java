package com.ljh.helper;

import java.sql.Connection;
import java.sql.Statement;

import com.cjx.utils.DatabaseHelper;

public class CleanTable
{
	public static Connection con;
	
	public static void main(String[] args)
	{
		try
		{
			con = DatabaseHelper.getConnection();
			
			Statement statement = con.createStatement();
			
			String sql1 = "delete from patent_cluster;";
			String sql2 = "delete from patent_feature_word;";
			String sql3 = "delete from patent_word_tf_df;";
			String sql4 = "delete from patents_after_word_divide;";
			String sql5	="delete from t_word_info;";
			
			statement.executeUpdate(sql1);
			System.out.println("Delete patent_cluster OK!!!");
			
			statement.executeUpdate(sql2);
			System.out.println("Delete patent_feature_word OK!!!");
			
			statement.executeUpdate(sql3);
			System.out.println("Delete patent_word_tf_df OK!!!");
			
//			statement.executeUpdate(sql4);
//			System.out.println("Delete patents_after_word_divide OK!!!");
			
			statement.executeUpdate(sql5);		
			System.out.println("Delete t_word_info OK!!!");
			
			statement.close();
			con.close();
			
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
