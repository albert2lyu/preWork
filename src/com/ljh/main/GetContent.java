package com.ljh.main;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cjx.model.PatentDao;
import com.cjx.utils.DatabaseHelper;

public class GetContent
{
//	List<PatentDao> patent = new ArrayList<PatentDao>();
	private String applyNum;
	static String ocrStr;
	static Pattern strPattern = Pattern.compile("说明书");
	static String urlHead = "E:/image";

	public static void main(String[] args)
	{
		GetContent getContent = new GetContent();
		
		//存放图片的头目录，结构式头目录/专利申请号/图片名称
		
		File applyNumPath = new File(urlHead);
		//获取专利申请号的数据
		String[] applyNumArray = applyNumPath.list();
		//专利申请号循环
		for (String applyNum : applyNumArray)
		{
			ocrStr = new String();
			System.out.println(applyNum);
			getContent.setApplyNum(applyNum);
					
			File tifPath = new File(urlHead + "/" + applyNum);
			//获取某个专利申请号下的所有图片名称
			String[] pageNumArray = tifPath.list();		
			
			//某个专利申请号文件夹下的图片名称循环
			for (String pageNum : pageNumArray)
			{
				System.out.println(pageNum);
				
				//不识别第一张图片
				if(pageNum.equals("000001.tif"))
				{
					continue;
				}
				
				//要识别的图片的地址
				String url = urlHead + "/" + applyNum + "/" + pageNum;
				//进行光学识别
				OcrCom ocr = new OcrCom();
				String ocrTemp = ocr.ocr(url);
				//只取权利要求书，如果之后要求说明书也可以继续添加
				Matcher	m = strPattern.matcher(ocrTemp);
				if(m.find())
				{
					break;
				}
				ocrStr = (ocrStr + ocrTemp).replaceAll("'", ",");
			}
			
			System.out.println("=================" + "\n" + ocrStr + "\n" + "---------------------------");
			getContent.saveToDB();
		}		
	}

	public void saveToDB()
	{
		try
		{
			Connection conn = DatabaseHelper.getConnection();
			Statement stt = conn.createStatement();
			String sql = "update patents set ptt_content = '" + ocrStr + "' where apply_num = '" + getApplyNum() + "';";
			stt.executeUpdate(sql);
			
			System.out.println(getApplyNum() + "\t" + "save to DB OK!" + "\n");
			stt.close();
			conn.close();
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			System.out.println("something wrong in saving to DB!!");
			e.printStackTrace();
		}
	}
	
	public String getApplyNum()
	{
		return applyNum;
	}

	public void setApplyNum(String applyNum)
	{
		this.applyNum = applyNum;
	}
}