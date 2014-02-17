package com.ljh.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cjx.model.PatentDao;
import com.cjx.utils.DatabaseHelper;

public class DownloadTif
{	
	String urlHead = "E:/image/";//想要修改图片的保存地址修改此处
	private String applyNum;
	private String fileName;
	private boolean overRange = false;	
	static int index = 0;
	static PatentDao patentTemp = new PatentDao();
		
	public static void main(String[] args)
	{
				
		DownloadTif download = new DownloadTif();
		List<PatentDao> patentList = download.getPatentList();
			
		for (PatentDao patent : patentList)
		{
			download.setApplyNum(patent.getApplyNum());
			patentTemp = patent;
			//得到没有图片编号的下载地址，此地址未完整
			String urlWithoutPage = download.getUrl(patent);
		
			for(int i = 1 ; i <= 500 && urlWithoutPage.length()!= 0 ; i++)
			{
				index = i;
				if(download.isOverRange() == true )
				{
					download.setOverRange(false);					
					break;
				}
				
				String pageNum = "";
				if(i < 10)
				{
					pageNum = "00000" + i;
				}
				else if(i < 100)
				{
					pageNum = "0000" + i; 
				}
				else
				{
					pageNum = "000" + i;
				}
				//完整的下载地址
				String url = urlWithoutPage + pageNum + ".tif";
				download.setFileName(pageNum + ".tif");
				
				download.download(url);							
				System.out.println(url + "\t" + "download ok!!");
			}
			System.out.println("循环结束");
		}
		
		
	}
	
	/**
	 * 下载tif文件到本地
	 * @param urlStr
	 */
	public void download(String urlStr)
	{
		// new一个URL对象
		URL url;
		try
		{
			url = new URL(urlStr);
			
			// 打开链接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置请求方式为"GET"
			conn.setRequestMethod("GET");
			// 超时响应时间为5秒
			conn.setConnectTimeout(5 * 1000);
			// 通过输入流获取图片数据
			InputStream inStream = conn.getInputStream();
			// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
			byte[] data = readInputStream(inStream);

			// new一个文件对象用来保存图片，默认保存当前工程根目录
			File filePath = new File(urlHead + getApplyNum());
			if(!filePath.exists())
			{
				filePath.mkdir();
			}
			File imageFile = new File(filePath + "/" + getFileName());
			// 创建输出流
			FileOutputStream outStream = new FileOutputStream(imageFile);
			// 写入数据
			outStream.write(data);
			// 关闭输出流
			outStream.close();
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			if(index == 1)
			{
				index++;
				//得到没有图片编号的下载地址，此地址未完整
				String urlWithoutPage = getUrl(patentTemp).replaceFirst("fm", "xx");
			
				for(int i = 1 ; i <= 500 && urlWithoutPage.length()!= 0 ; i++)
				{
					
					if(isOverRange() == true )
					{
						setOverRange(false);					
						break;
					}
					
					String pageNum = "";
					if(i < 10)
					{
						pageNum = "00000" + i;
					}
					else if(i < 100)
					{
						pageNum = "0000" + i; 
					}
					else
					{
						pageNum = "000" + i;
					}
					String urlTemp = urlWithoutPage + pageNum + ".tif";
					setFileName(pageNum + ".tif");
					
					download(urlTemp);							
					System.out.println(urlTemp + "\t" + "download ok!!");
				}
				System.out.println("循环结束");
				//xxpatentList.add(patentTemp);
			}
			else
			{
				setOverRange(true);	
				System.out.println("is over range");
			}
			
			e.printStackTrace();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static byte[] readInputStream(InputStream inStream) throws Exception
	{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1)
		{
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}

	/**
	 * 
	 * @return 得到专利列表，列表每一个元素是一个PatentDao，内含（申请号，公开号）
	 */
	public List<PatentDao> getPatentList()
	{
		List<PatentDao> patentList = new ArrayList<PatentDao>();
		PatentDao patent;

		try
		{
			Connection conn = DatabaseHelper.getConnection();
			Statement stt = conn.createStatement();
			String sql = "select apply_num,ptt_date from patents order by ptt_date;";
			//String sql = "select apply_num,ptt_date from patents where ptt_date > '2007-04-18' order by ptt_date;";
			//String sql = "select apply_num,ptt_date from patents where apply_num = '200620089058.9' order by ptt_date;";
			ResultSet resultSet = stt.executeQuery(sql);

			while (resultSet.next())
			{
				patent = new PatentDao();
				patent.setApplyNum(resultSet.getString(1));
				patent.setPttDate(resultSet.getDate(2));

				patentList.add(patent);
			}

		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			System.out.println("something wrong in getting patentList!!!");
			e.printStackTrace();
		}

		return patentList;
	}

	/**
	 * 
	 * @param patent
	 * @return 返回根据专利列表得到的tif图片的下载地址
	 */
	public String getUrl(PatentDao patent)
	{
		String url = "";

		// 2006.1.1 to 2008.4.31
		if (patent.getPttDate().after(new Date(106, 0, 1))
				&& patent.getPttDate().before(new Date(108, 4, 1)))// 106,11,31
		{
			String beginStr = "http://211.157.104.86/books";
			String unknownStr = "fm";
			int year = patent.getPttDate().getYear() + 1900;
			long num = 0;

			switch (year)
			{
			case 2006:
				long interval1 = (patent.getPttDate().getTime() - new Date(106,
						0, 4).getTime())
						/ (7 * 24 * 60 * 60 * 1000);
				num = 2201 + interval1;
				break;
			case 2007:
				long interval2 = (patent.getPttDate().getTime() - new Date(107,
						0, 3).getTime())
						/ (7 * 24 * 60 * 60 * 1000);
				num = 2301 + interval2;
				break;
			case 2008:
				long interval3 = (patent.getPttDate().getTime() - new Date(108,
						0, 2).getTime())
						/ (7 * 24 * 60 * 60 * 1000);
				num = 2401 + interval3;
				break;
			}

			String[] patentNumArray = patent.getApplyNum().toString().split(
					"\\.");
			String patentNum = patentNumArray[0];

			url = beginStr + "/" + unknownStr + "/" + year + "/" + num
					+ "/" + patentNum + "/";
			//System.out.println(url);
		}

		// --------------------------------------------------------------------
		// 2008.5.1 to 2008.11.1
		else if (patent.getPttDate().after(new Date(108, 4, 2))
				&& patent.getPttDate().before(new Date(108, 10, 1)))
		{
			String beginStr = "http://211.157.104.86/books";
			String unknownStr = "fm";
			int year = patent.getPttDate().getYear() + 1900;
			String pttDate = getPttDate(patent.getPttDate());

			String[] patentNumArray = patent.getApplyNum().toString().split(
					"\\.");
			String patentNum = patentNumArray[0];

			url = beginStr + "/" + unknownStr + "/" + year + "/"
					+ pttDate + "/" + patentNum + "/";
			//System.out.println(url);
		}

		// 2008.11.5 to 2010.9.25
		else if (patent.getPttDate().after(new Date(108, 10, 1))
				&& patent.getPttDate().before(new Date(110, 8, 25)))
		{
			String beginStr = "http://211.157.104.86/books";
			String unknownStr = "fm";
			int year = patent.getPttDate().getYear() + 1900;
			String pttDate = getPttDate(patent.getPttDate());
			String patentNum = patent.getApplyNum();

			url = beginStr + "/" + unknownStr + "/" + year + "/"
					+ pttDate + "/" + patentNum + "/";
			//System.out.println(url);
		}
		
		return url;
	}

	/**
	 * 
	 * @param date
	 * @return 返回一个日期的字符串，格式是20060101
	 */
	public String getPttDate(Date date)
	{
		int year = date.getYear() + 1900;
		int month = date.getMonth() + 1;
		int day = date.getDate();

		String yearStr = Integer.toString(year);
		String monthStr = "";
		String dateStr = "";
		if (month < 10)
		{
			monthStr = "0" + month;
		} else if (month >= 10 && month <= 12)
		{
			monthStr = Integer.toString(month);
		} else
		{
			System.out.println("something wrong in the month");
		}

		if (day < 10)
		{
			dateStr = "0" + day;
		} else if (day >= 10 && day <= 31)
		{
			dateStr = Integer.toString(day);
		} else
		{
			System.out.println("something wrong in the date");
		}

		String pttDate = yearStr + monthStr + dateStr;

		return pttDate;
	}
	
	//----------------------------------------------------------------
	public void setApplyNum(String applyNum)
	{
		this.applyNum = applyNum;
	}

	public String getApplyNum()
	{
		return applyNum;
	}

	public void setFileName(String name)
	{
		this.fileName = name;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setOverRange(boolean overRange)
	{
		this.overRange = overRange;
	}

	public boolean isOverRange()
	{
		return overRange;
	}


}
