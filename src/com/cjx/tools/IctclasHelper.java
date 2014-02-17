package com.cjx.tools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.ictclas4j.segment.SegTag;

import com.cjx.model.PatentsAfterWordDivideModel;
import com.cjx.utils.DatabaseHelper;

/**
 * 单例模式，分词工具类
 * 
 * @author kitten
 *
 */
public class IctclasHelper {

	private static IctclasHelper _instance = null;
	
	private SegTag segTag;
	
	public Map<String, Number> stopWordMap;
	public Map<String, Number> wordMarkMap;
	
	//创建实例时创建SegTag，加载停用词表、词性标注表，用于词性过滤
	private IctclasHelper()
	{
		setSegTag(new SegTag(1));
		
		stopWordMap = new HashMap<String, Number>();
		wordMarkMap = new HashMap<String, Number>();
		
		try {
			Connection con = DatabaseHelper.getConnection();
			Statement sta = con.createStatement();
			
			ResultSet rs = sta.executeQuery("SELECT * FROM t_stopword");
			String word;
			int flag;
			while(rs.next())
			{
				word = rs.getString("WORD");
				flag = rs.getInt("FLAG");
				
				if(flag == 0) //1:有用 ； 0：过滤
				{
					stopWordMap.put(word, flag);
				}
			}
			rs.close();
			
			rs = sta.executeQuery("SELECT WORD_SMARK,FLAG FROM t_word_smark");
			while(rs.next())
			{
				word = rs.getString("WORD_SMARK");
				flag = rs.getInt("FLAG");
				
				if(flag == 0) //1:有用 ； 0：过滤
				{
					wordMarkMap.put(word, flag);
				}
			}
			rs.close();
			
			sta.close();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static IctclasHelper getInstance()
	{
		if(_instance == null)
		{
			_instance = new IctclasHelper();
		}
		return _instance;
	}
	
	/**
	 * 使用ICTCLAS分词法分词，并过滤无关词组
	 * 
	 * @param str
	 * @return
	 */
	public String splitToString(String str)
	{
		return filterString(getSegTag().split(str));
	}
	
	/**
	 * 过滤已分词字符串
	 * 
	 * @param splitStr 已分词字符串
	 * @return String
	 */
	public String filterString(String splitStr)
	{
		String newStr = "";
		
		String[] tempList = splitStr.split(" ");
		int len = tempList.length;
		for(int i=0; i<len; i++)
		{
			//过滤
			if(checkValid(tempList[i]))
			{
				newStr += tempList[i] + " ";
			}
		}
		
		return newStr;
	}
	
	/**
	 * 检查词是否有意义，过滤无意义词组
	 * 
	 * @param str
	 * @return
	 */
	public Boolean checkValid(String str)
	{
		int index = str.lastIndexOf("/");
		if(index != -1)
		{
			if(wordMarkMap.get(str.substring(index+1)) != null)
			{
				return false;
			}
			else if(stopWordMap.get(str.substring(0, index)) != null)
			{
				return false;
			}
		}
		else
		{
			if(stopWordMap.get(str) != null)
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 若表patents_after_word_divide数据已存在，执行此方法可再次过滤无用词组
	 * 
	 * @return
	 */
	public Boolean filterAfterTable()
	{
		Connection con;
		try {
			con = DatabaseHelper.getConnection();
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery("SELECT * FROM patents_after_word_divide");
			
			PatentsAfterWordDivideModel pawd;
			String tempName,tempAbstract;
			Statement sta2;
			while(rs.next())
			{
				pawd = new PatentsAfterWordDivideModel();
				pawd.read(rs);
				
				tempName = filterString(pawd.getPtt_name());
				tempAbstract = filterString(pawd.getPtt_abstract());
				
				if(!tempName.equals(pawd.getPtt_name()) || !tempAbstract.equals(pawd.getPtt_abstract()))
				{
					sta2 = con.createStatement();
					sta2.execute("UPDATE patents_after_word_divide SET PTT_NAME_DIVIDED='" + tempName +
							"',PTT_ABSTRACT_DIVIDED='" + tempAbstract +
							"' WHERE PTT_NUM='" + pawd.getPtt_num() +
							"'");
					sta2.close();
				}
			}
			
			rs.close();
			sta.close();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public void setSegTag(SegTag segTag) {
		this.segTag = segTag;
	}

	public SegTag getSegTag() {
		return segTag;
	}
	
	public static void main(String[] args)
	{
		IctclasHelper.getInstance().filterAfterTable();
		
		//去空格
//		try {
//			Connection con = DatabaseHelper.getConnection();
//			Statement sta = con.createStatement();
//			ResultSet rs = sta.executeQuery("SELECT PTT_NUM FROM patents_after_word_divide");
//			
//			String num;
//			Statement sta2;
//			while(rs.next())
//			{
//				num = rs.getString("PTT_NUM");
//				
//				sta2 = con.createStatement();
//				sta2.execute("UPDATE patents_after_word_divide SET PTT_NUM='" + num.trim() +
//						"' WHERE PTT_NUM='" + num +
//						"'");
//				sta2.close();
//			}
//			
//			rs.close();
//			sta.close();
//			con.close();
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		
	}
}
