package com.cjx.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cjx.model.PatentFeatureWordModel;
import com.cjx.model.PatentMatrix;
import com.cjx.model.PatentWordTFIDFModel;
import com.cjx.model.PatentsAfterWordDivideModel;
import com.cjx.model.WordInfoModel;
import com.cjx.tools.IctclasHelper;
import com.cjx.utils.DatabaseHelper;
import com.sun.crypto.provider.RSACipher;

public class PatentsPreprocess {

	public static Connection con;
	
	public Map<String, PatentWordTFIDFModel> titleWordDic;
	public Map<String, PatentWordTFIDFModel> abstractWordDic;
	public Map<String, PatentWordTFIDFModel> contentWordDic;
	int count = 0;
	
	public static void main(String[] args)
	{
		PatentsPreprocess pp = new PatentsPreprocess();
		//将名称和摘要分词并存入patent_word_after_divide
		pp.divideWordToDb();
		
		//计算TF，存入Map中
		pp.countTF();
		//将Map中的数据(即TF)存入到patent_word_tf_df
		pp.saveWordDicToDatabase();
		//更新patent_word_tf_df中的DF值
		pp.countDF();
		
		//保存（word,maxTF,DF）值到t_word_info
		pp.extractFeatureWord();
		pp.countAndSaveToDb(20);
		pp.countStandardTFIDF();
		pp.cluster(20);
		
	}
	
	public PatentsPreprocess()
	{
		try {
			con = DatabaseHelper.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将专利源数据库表patents的标题、摘要信息分词并过滤，存入新的数据库表patents_after_word_divide中
	 * 
	 */
	public void divideWordToDb()
	{
		try {
			
			Statement sta = con.createStatement();
			
			String sql = "SELECT PTT_NUM,PTT_NAME,PTT_DATE,CLASS_NUM_G06Q,PTT_ABSTRACT,PTT_CONTENT FROM patents";
			ResultSet rs = sta.executeQuery(sql);
			
			PatentsAfterWordDivideModel pttAWDM;
			while(rs.next())
			{				
				pttAWDM = new PatentsAfterWordDivideModel();
				pttAWDM.setPtt_num(rs.getString("PTT_NUM"));
				pttAWDM.setPtt_date(rs.getDate("PTT_DATE"));
				pttAWDM.setClass_num_g06q(rs.getString("CLASS_NUM_G06Q"));
				
				//将专利名称和摘要分词
				pttAWDM.setPtt_name(IctclasHelper.getInstance().splitToString(rs.getString("PTT_NAME")));
				pttAWDM.setPtt_abstract(IctclasHelper.getInstance().splitToString(rs.getString("PTT_ABSTRACT")));
				if(rs.getString("PTT_CONTENT") != null)
				{
					pttAWDM.setPtt_content(IctclasHelper.getInstance().splitToString(rs.getString("PTT_CONTENT")));
				}
							
				//存入到patents_after_word_divide数据表
				Statement sta2 = con.createStatement();
				String sql2 = "INSERT INTO patents_after_word_divide (PTT_NUM,PTT_NAME_DIVIDED,PTT_DATE,CLASS_NUM_G06Q,PTT_ABSTRACT_DIVIDED,PTT_CONTENT_DIVIDED) VALUES ('"
					+ pttAWDM.getPtt_num() + "','" 
					+ pttAWDM.getPtt_name() + "','" 
					+ pttAWDM.getPtt_date() + "','"
					+ pttAWDM.getClass_num_g06q() + "','" 
					+ pttAWDM.getPtt_abstract() + "','"
					+ pttAWDM.getPtt_content()
					+ "');";
				sta2.execute(sql2);
				//8624
				System.out.println("Number : " + rs.getRow()
 						+ "\n" + sql2);
				sta2.close();
			}
			
			rs.close();
			sta.close();
			con.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * 计算各专利文档中词汇的词频
	 * 
	 */
	public void countTF()
	{
		try {
			Statement sta = con.createStatement();
			String sql = "SELECT * FROM patents_after_word_divide";
			
			ResultSet rs = sta.executeQuery(sql);
			
			PatentsAfterWordDivideModel pawd;
			String[] titleArr;
			String[] abstractArr;
			String[] contentArr;
			titleWordDic = new HashMap<String, PatentWordTFIDFModel>();
			abstractWordDic = new HashMap<String, PatentWordTFIDFModel>();
			contentWordDic = new HashMap<String, PatentWordTFIDFModel>();
			
			//数据集循环
			while(rs.next())
			{
				pawd = new PatentsAfterWordDivideModel();
				pawd.read(rs);
				
				String tempStr;
				
				//以空格切开专利名词（折扣/n 卡/n 系统/n ）
				titleArr = pawd.getPtt_name().split(" ");
				for(int i=0; i<titleArr.length; i++)
				{
					tempStr = titleArr[i];
					if(tempStr.lastIndexOf("/") != -1)
						tempStr = tempStr.substring(0,tempStr.lastIndexOf("/"));
					PatentWordTFIDFModel p;
					//以word_专利名称为格式判断是否唯一，如果是唯一的tf为1
					if(titleWordDic.get(tempStr+"_"+pawd.getPtt_num()) == null)
					{
						p = new PatentWordTFIDFModel();
						p.setFlag(1); //1为标题
						p.setWord(tempStr);
						p.setPttNum(pawd.getPtt_num());
						titleWordDic.put(tempStr+"_"+pawd.getPtt_num(), p);
					}
					//如果不唯一，tf+1
					else
					{
						p = titleWordDic.get(tempStr+"_"+pawd.getPtt_num());
						p.setTf(p.getTf()+1);
					}
				}
				
				//以空格切开专利摘要
				abstractArr = pawd.getPtt_abstract().split(" ");
				for(int j=0; j<abstractArr.length; j++)
				{
					tempStr = abstractArr[j];
					if(tempStr.lastIndexOf("/") != -1)
						tempStr = tempStr.substring(0,tempStr.lastIndexOf("/"));
					PatentWordTFIDFModel p;
					if(abstractWordDic.get(tempStr+"_"+pawd.getPtt_num()) == null)
					{
						p = new PatentWordTFIDFModel();
						p.setFlag(0); //0为摘要
						p.setWord(tempStr);
						p.setPttNum(pawd.getPtt_num());
						abstractWordDic.put(tempStr+"_"+pawd.getPtt_num(), p);
					}
					else
					{
						p = abstractWordDic.get(tempStr+"_"+pawd.getPtt_num());
						p.setTf(p.getTf()+1);
					}
				}
				
				//以空格切开专利说明书
				contentArr = pawd.getPtt_content().split(" ");
				for(int k = 0 ; k < contentArr.length ; k++ )
				{
					tempStr = contentArr[k];
					if(tempStr.lastIndexOf("/") != -1)
					{
						tempStr = tempStr.substring(0 , tempStr.lastIndexOf("/"));
					}
					PatentWordTFIDFModel p;
					if(contentWordDic.get(tempStr + "_" + pawd.getPtt_num()) == null)
					{
						p = new PatentWordTFIDFModel();
						p.setFlag(2); //设定2为专利说明书的内容
						p.setWord(tempStr);
						p.setPttNum(pawd.getPtt_num());
						contentWordDic.put(tempStr + "_" + pawd.getPtt_num() , p);
					}
					else
					{
						p = contentWordDic.get(tempStr + "_" + pawd.getPtt_num());
						p.setTf(p.getTf() + 1);
					}
				}
				
				//判断哈希表长度，防止内存溢出。
				if(abstractWordDic.size() > 60000 || titleWordDic.size() > 60000 || contentWordDic.size() > 60000)
				{
					saveWordDicToDatabase();
					abstractWordDic = new HashMap<String, PatentWordTFIDFModel>();
					titleWordDic = new HashMap<String, PatentWordTFIDFModel>();
					contentWordDic = new HashMap<String, PatentWordTFIDFModel>();
					
					System.out.println("长度过长，保存数据，清空哈希表，继续统计！");
				}
				
				System.out.println(rs.getRow() + "\t" + "ok!\t"+pawd.getPtt_num()+"\t"+pawd.getPtt_name());
			}
			
			rs.close();
			sta.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将两个字典存入数据库
	 * 
	 */
	public void saveWordDicToDatabase()
	{
		Iterator<Map.Entry<String, PatentWordTFIDFModel>> iterator1 = titleWordDic.entrySet().iterator();
		Iterator<Map.Entry<String, PatentWordTFIDFModel>> iterator2 = abstractWordDic.entrySet().iterator();
		Iterator<Map.Entry<String, PatentWordTFIDFModel>> iterator3 = contentWordDic.entrySet().iterator();
		
		Map.Entry<String, PatentWordTFIDFModel> entry;
		while(iterator1.hasNext())
		{
			entry = iterator1.next();
			entry.getValue().write(con);
		}
		System.out.println("Save titleWordDic OK!");
		while(iterator2.hasNext())
		{
			entry = iterator2.next();
			entry.getValue().write(con);
		}
		System.out.println("Save abstractWordDic OK!");
		while(iterator3.hasNext())
		{
			entry = iterator3.next();
			entry.getValue().write(con);
		}
		System.out.println("Save contentWordDic OK!");
	}
	
	/**
	 * 计算所有词的文档频数
	 * 
	 */
	public void countDF()
	{
		try {
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery("SELECT DISTINCT WORD FROM patent_word_tf_df");
			
			Statement tempSta1,tempSta2;
			ResultSet tempRs1,tempRs2;
//			int count = 0;
			while(rs.next())
			{
				System.out.println(rs.getRow() + "\t" + rs.getString("WORD"));
				
				tempSta1 = con.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
				tempRs1 = tempSta1.executeQuery("SELECT * FROM patent_word_tf_df WHERE WORD='" + rs.getString(1) + "'");
				
				//统计某词出现的文档频率
				tempSta2 = con.createStatement();
				tempRs2 = tempSta2.executeQuery("SELECT DISTINCT PTT_NUM FROM patent_word_tf_df WHERE WORD='" +
						rs.getString(1) + "'");
				int f = DatabaseHelper.getSize(tempRs2);
				
				System.out.println("DF : " + f);
				while(tempRs1.next())
				{
					tempRs1.updateInt(5, f);
					tempRs1.updateRow();
				}
				
				tempRs2.close();
				tempSta2.close();
				
				tempRs1.close();
				tempSta1.close();
				
//				System.out.println(count++);
				
			}
			
			rs.close();
			sta.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算所有词权重，按权重提取特证词，并存入数据库表 PATENT_FEATURE_WORD
	 * 
	 */
	public void extractFeatureWord()
	{
		Map<String, Number> wordMaxTFmap = new HashMap<String, Number>();
		try 
		{
			int count = 0;
			
			Connection connection = DatabaseHelper.getConnection();
			Statement statement = connection.createStatement();
			
			ResultSet resultSet = statement.executeQuery("SELECT DISTINCT WORD FROM patent_word_tf_df");
			
			int tempNum;
			String word;
			WordInfoModel tempWim;
			while(resultSet.next())
			{
				tempNum = 0;
				Statement wordSta = connection.createStatement();
				word = resultSet.getString(1);
				ResultSet wordRs = wordSta.executeQuery("select * from patent_word_tf_df where WORD='" + word + "'");
				
				int df = 1;
				
				while(wordRs.next())
				{
					df = wordRs.getInt(5);
					tempNum = Math.max(tempNum, wordRs.getInt(4));
				}
				wordMaxTFmap.put(word, tempNum);
				
				//保存进数据库
				tempWim = new WordInfoModel();
				tempWim.setWord(word);
				tempWim.setMaxTf(tempNum);
				tempWim.setDf(df);
				tempWim.write();
				
				wordRs.close();
				wordSta.close();
				
				count++;
				System.out.println("map count:" + count + "\tkey:" + word + "\tvalue:" + tempNum);
			}
			
			System.out.println("map保存成功！");
			
			resultSet.close();
			statement.close();
			connection.close();
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 计算TF-IDF值并提取前size位存入数据库
	 * 
	 */
	public void countAndSaveToDb(int size)
	{
		try
		{
			int n = DatabaseHelper.getSize("patents_after_word_divide");
			int count = 0;
			Map<String, Number> wordMaxTFmap = new HashMap<String, Number>();
			
			Connection connection = DatabaseHelper.getConnection();
			
			Statement mapSta = connection.createStatement();
			ResultSet mapRs = mapSta.executeQuery("select * from t_word_info");
			while(mapRs.next())
			{
				wordMaxTFmap.put(mapRs.getString("WORD"), mapRs.getInt("MAX_TF"));
			}
			mapRs.close();
			mapSta.close();
			
			System.out.println(wordMaxTFmap.get("web"));
			System.out.println(wordMaxTFmap.size());
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT DISTINCT PTT_NUM FROM patent_word_tf_df");
			String pttnum;
			ArrayList<PatentFeatureWordModel> tempArr;
			while(resultSet.next())
			{
				tempArr = new ArrayList<PatentFeatureWordModel>();
				Statement pttnumSta = connection.createStatement();
				pttnum = resultSet.getString(1);
				ResultSet pttnumRs = pttnumSta.executeQuery("select * from patent_word_tf_df where PTT_NUM='" + pttnum + "'");
				
				while(pttnumRs.next())
				{
					int tf = pttnumRs.getInt(4);
					
					int maxTf = 0;
					if(wordMaxTFmap.get(pttnumRs.getString(3)) == null)
					{
						System.out.println(pttnumRs.getString(3) + "\tnullpointerexception!");
						continue;
					}
					else
					{
						maxTf = wordMaxTFmap.get(pttnumRs.getString(3)).intValue();
					}
					
					int df = pttnumRs.getInt(5);
					Number tempValue = (0.5 + 0.5*tf/maxTf)*(Math.log(n/df));
					PatentFeatureWordModel pfwm = new PatentFeatureWordModel(pttnum,pttnumRs.getString(3),tempValue.doubleValue());
					tempArr.add(pfwm);
				}
				
				for(int i=0; i<tempArr.size()-1; i++)
				{
					for(int j=i+1; j<tempArr.size(); j++)
					{
						if(tempArr.get(i).getTfidfValue() < tempArr.get(j).getTfidfValue())
						{
							PatentFeatureWordModel tempPfwm = tempArr.get(i);
							tempArr.set(i, tempArr.get(j));
							tempArr.set(j, tempPfwm);
						}
					}
				}
				
				int len = Math.min(size, tempArr.size());
				for(int m=0; m<len; m++)
				{
					//写入数据库
					tempArr.get(m).write();
				}
				
				pttnumRs.close();
				pttnumSta.close();
				
				count++;
				System.out.println("------------" + pttnum + "\t" + count + "\tlen:" + len);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 规范化TF-IDF值，使同一篇文档中所有特征权重的平方和等于1
	 * 
	 */
	public void countStandardTFIDF()
	{
		try {
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery("select PTT_NUM from patents");
			
			while(rs.next())
			{
				String pttNum = rs.getString(1);
				Statement sta2 = con.createStatement();
				ResultSet rs2 = sta2.executeQuery("select * from patent_feature_word where PTT_NUM='" + pttNum + "'");
				
				List<PatentFeatureWordModel> pfwnList = new ArrayList<PatentFeatureWordModel>();
				while(rs2.next())
				{
					PatentFeatureWordModel pfwm = new PatentFeatureWordModel();
					pfwm.read(rs2);
					pfwnList.add(pfwm);
				}
				
				double sum = 0;
				for(int i=0; i<pfwnList.size(); i++)
				{
					sum += Math.pow(pfwnList.get(i).getTfidfValue(),2);
				}
				
				sum = Math.sqrt(sum);
				
				for(int j=0; j<pfwnList.size(); j++)
				{
					PatentFeatureWordModel p = pfwnList.get(j);
					p.setTfidfValueStandard(p.getTfidfValue()/sum);
					p.updateTfidfValueStandard();
				}
				
				rs2.close();
				sta2.close();
				
				System.out.println(pttNum);
			}
			
			rs.close();
			sta.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 根据patent_feature_word表对专利进行分类,k-Means聚类
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void cluster(int k)
	{
		List<PatentMatrix> pttMatrix = new ArrayList<PatentMatrix>();
		int n=0;
		try {
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery("select PTT_NUM from patents");
			while(rs.next())
			{
				String pttNum = rs.getString(1);
				
				PatentMatrix pm = new PatentMatrix();
				pm.pttNum = pttNum;
				
				Statement sta2 = con.createStatement();
				ResultSet rs2 = sta2.executeQuery("select * from patent_feature_word where PTT_NUM='" + pttNum + "'");
				int index = 0;
				double sum = 0;
				while(rs2.next())
				{
					PatentFeatureWordModel pfwm = new PatentFeatureWordModel();
					pfwm.read(rs2);
					
					pm.value[index] = pfwm.getTfidfValueStandard();
					sum += pm.value[index] * pm.value[index];
					
					index += 1;
				}
				for(int t=index; t<20; t++)
				{
					pm.value[t] = 0;
				}
				rs2.close();
				sta2.close();
				
				pttMatrix.add(pm);
				
				System.out.println(":" + n);
				System.out.println("" + sum);
//				if(n == 100)
//				{
//					break;
//				}
				
				n++;
			}
			
			rs.close();
			sta.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//变量定义
		ArrayList<PatentMatrix>[] clusters = new ArrayList[k]; //k个聚类
		List<PatentMatrix> clustersCenter = new ArrayList<PatentMatrix>();	//k个聚类的中心点
		for(int z=0; z<k; z++)
		{
			clusters[z] = new ArrayList<PatentMatrix>();
			clusters[z].add(pttMatrix.get(z));
			clustersCenter.add(pttMatrix.get(z));
		}
		
		int[] clusterAssignments = new int[n];	//各个数据属于那个聚类
		int[] nearestCluster = new int[n];	//各个数据离那个聚类最近
		double[][] distance = new double[n][k]; //各个数据与各个聚类的距离
		
		int count = 1;
		while(true)
		{
			System.out.println(":"+count);
			//1.重新计算每个聚类的中心点
			System.out.println("clustersCenter:");
			for(int i=0; i<20; i++)
			{
				clustersCenter.set(i, getClusterCenter(clusters[i]));
			}
			
			//2.计算每个数据和每个聚类中心的距离
			System.out.println("distance:");
			for(int m=0; m<k; m++)
			{
				for(int j=0; j<n; j++)
				{
					distance[j][m] = getDistance(pttMatrix.get(j),clustersCenter.get(m));
					System.out.print(" " + distance[j][m]);
				}
				System.out.print("\n");
			}
			
			//3.计算每个数据离哪个聚类最近
			System.out.print("nearestCluster:");
			for(int f=0; f<n; f++)
			{
				nearestCluster[f] = getNearestCluster(distance[f]);
				System.out.print(" " + nearestCluster[f]);
			}
			System.out.print("\n");
			
			//4.比较每个数据最近的聚类是否就是它所属的聚类
	        //如果全相等表示所有的点已经是最佳距离了，直接返回；
			System.out.print("clusterAssignments:");
			int r = 0;
	        for(int w=0; w<n; w++)
	        {
	        	System.out.print(" " + clusterAssignments[w]);
	            if(nearestCluster[w] == clusterAssignments[w])
	            {
	            	r++;
	            }
	            else
	            {
	            	clusterAssignments[w] = nearestCluster[w];
	            }
	        }
	        System.out.print("\n");
	        if(r == n)
	            break;
	        System.out.println("第四步完成");
	        
	        //5.否则需要重新调整资料点和群聚类的关系，调整完毕后再重新开始循环；
	        //需要修改每个聚类的成员和表示某个数据属于哪个聚类的变量
	        for(int q=0; q<k; q++)
	        {
	        	clusters[q].clear();
	        }
	        for(int p=0; p<n; p++)
	        {
	        	clusters[clusterAssignments[p]].add(pttMatrix.get(p));
	        }
	        System.out.println("第五步完成");
	        count ++;
		}
		
		for(int ii=0; ii<n; ii++)
		{
			Statement statement;
			try {
				statement = con.createStatement();
				statement.execute("INSERT INTO patent_cluster (PTT_NUM,CLUSTER) VALUES ('" + pttMatrix.get(ii).pttNum + "'," + clusterAssignments[ii] + ")");
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("聚类成功！");
	}
	
	private PatentMatrix getClusterCenter(ArrayList<PatentMatrix> cluster)
	{
		int len = cluster.size();
		double[][] distance = new double[len][len];	//存储各数据间相互距离的二维数组
		for(int j=0; j<len; j++)
		{
			for(int i=j; i<len; i++)
			{
				if(i == j)
				{
					distance[i][j] = 0;
				}
				else
				{
					distance[i][j] = distance[j][i] = getDistance(cluster.get(i), cluster.get(j));
				}
			}
		}
		
		double[] standardDiviation = new double[len];
		for(int n=0; n<len; n++)
		{
			standardDiviation[n] = countStandardDiviation(distance[n]);
		}
		
		double min = standardDiviation[0];
		int index = 0;
		for(int m=0; m<len; m++)
		{
			if(standardDiviation[m] < min)
			{
				min = standardDiviation[m];
				index = m;
			}
		}
		
		System.out.println("getClusterCenter-" + index);
		return cluster.get(index);
	}
	private double getDistance(PatentMatrix pm,PatentMatrix center)
	{
		double value = 0;
		
		//规范化后的距离 |dx|*|dy|=1  distance=dx*dy/|dx|*|dy|=dx*dy
		for(int i=0; i<20; i++)
		{
			value += center.value[i]*pm.value[i];
		}
		
		return Math.abs(1-value);
	}
	private int getNearestCluster(double[] distances)
	{
		int i = 0;
		
		double min = distances[0];
		for(int j=1; j<distances.length; j++)
		{
			if(distances[j] < min)
			{
				min = distances[j];
				i = j;
			}
		}
		
		return i;
	}
	
	private double countStandardDiviation(double[] distance)
	{
		int len = distance.length;
		
		double sum = 0;
		for(int i=0; i<len; i++)
		{
			sum += distance[i];
		}
		double average = sum/len;
		
		double s2 = 0;
		for(int j=0; j<len; j++)
		{
			s2 += Math.pow(distance[j]-average,2);
		}
		
		return Math.sqrt(s2/len);
	}
	
}
