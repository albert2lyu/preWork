package com.cjx.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;

import com.cjx.utils.DatabaseHelper;

public class LuceneHelper {

	public String indexFilePath = "E:/Project/CJXProject/Code/patentOLAM/luceneIndex";
	
	public static void main(String[] args) {
		
		LuceneHelper lh = new LuceneHelper();
		lh.createIndex();
		
	}
	
	public void createIndex()
	{
		try
		{
			Connection con = DatabaseHelper.getConnection();
			Statement sta = con.createStatement();
			String sql = "SELECT " +
					"patents.PTT_NUM," +
					"patents.PTT_DATE," +
					"patents.PTT_TYPE," +
					"patents.CLASS_NUM_G06Q," +
					"patents.INVENTOR," +
					"patents.PTT_NAME," +
					"patents.PTT_ABSTRACT," +
					"patent_cluster.CLUSTER " +
					"FROM patents,patent_cluster " +
					"WHERE patent_cluster.PTT_NUM=patents.PTT_NUM";
			ResultSet rs = sta.executeQuery(sql);
			
			StandardAnalyzer a = new StandardAnalyzer();
			IndexWriter indexwriter = new IndexWriter(indexFilePath,a,true );
			while(rs.next())
			{
				Document document = new Document();
				document.add(Field.Keyword("PTT_NUM", rs.getString("PTT_NUM")));
				document.add(Field.UnIndexed("PTT_DATE", rs.getDate("PTT_DATE").toString()));
				document.add(Field.UnIndexed("PTT_TYPE", rs.getString("PTT_TYPE")));
				document.add(Field.Text("CLASS_NUM_G06Q", rs.getString("CLASS_NUM_G06Q")));
				document.add(Field.Text("INVENTOR", rs.getString("INVENTOR")));
				document.add(Field.Text("PTT_NAME", rs.getString("PTT_NAME")));
				document.add(Field.Text("PTT_ABSTRACT", rs.getString("PTT_ABSTRACT")));
				String cluster = String.valueOf(rs.getInt("CLUSTER"));
				document.add(Field.UnIndexed("CLUSTER", cluster));
				indexwriter.addDocument(document);
				
				System.out.println(rs.getRow());
			}
			indexwriter.optimize();
			indexwriter.close();
			
			rs.close();
			sta.close();
			con.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
}
