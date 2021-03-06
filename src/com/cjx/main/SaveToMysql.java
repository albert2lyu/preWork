package com.cjx.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import com.cjx.model.PatentDao;
import com.cjx.utils.Constants;
import com.cjx.utils.DatabaseHelper;

/**
 * 将保存下来的专利网页的专利信息保存到数据库
 * 
 * @author cjx
 */
public class SaveToMysql {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SaveToMysql save = new SaveToMysql();
		save.save("11");
		save.save("22");

	}

	public void save(String type) {
		File file = new File(Constants.WEBSITE_PATH + type);
		File[] fileList = file.listFiles();

		// 遍历专利页面文件夹下所有专利页面，并将页面信息保存到数据库
		int len = fileList.length;
		File tempFile;
		for (int i = 0; i < len; i++) {
			tempFile = fileList[i];

			try {
				saveToDatabase(tempFile, type);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void saveToDatabase(File file, String type) throws IOException {
		FileInputStream content = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(content,
				"utf-8"));
		String tempbf;
		StringBuffer html = new StringBuffer(100);
		while ((tempbf = br.readLine()) != null) {
			html.append(tempbf + "\n");
		}

		PatentDao pttDao = new PatentDao();
		try {
			// 从html中读取数据，以PattentDao保存
			pttDao = parseHtmlToPatentDao(html.toString(), type);
			// 保存patents数据表
			DatabaseHelper.writeToDb(pttDao);

		} catch (ParserException e) {
			e.printStackTrace();
		}
	}

	private PatentDao parseHtmlToPatentDao(String html, String type)
			throws ParserException {
		PatentDao pttDao = new PatentDao();
		pttDao.setPttType(type);
		Parser parser = new Parser(html.toString());

		NodeList nodeList1 = parser
				.extractAllNodesThatMatch(new HasAttributeFilter("style",
						"table-layout:fixed;word-break:break-all;word-wrap:break-word"));
		TableTag tableTag = (TableTag) nodeList1.elementAt(0);
		TableRow row;
		TableColumn[] colAry;

		row = tableTag.getRow(0);
		colAry = row.getColumns();
		pttDao.setApplyNum(colAry[1].getStringText()
				.replace("&nbsp;<span class=\"zi_10\">", "")
				.replace("</span>", ""));
		pttDao.setApplyDate(Date.valueOf(colAry[3].getStringText()
				.replace("&nbsp;", "").replace(".", "-")));

		row = tableTag.getRow(1);
		colAry = row.getColumns();
		pttDao.setPttName(colAry[1].getStringText().replace("&nbsp;", ""));

		row = tableTag.getRow(2);
		colAry = row.getColumns();
		pttDao.setPttNum(colAry[1].getStringText().replace("&nbsp;", ""));
		pttDao.setPttDate(Date.valueOf(colAry[3].getStringText()
				.replaceAll("&nbsp;|\\s", "").replace(".", "-")));

		row = tableTag.getRow(3);
		colAry = row.getColumns();
		pttDao.setPttMainClassNum(colAry[1].getStringText().replaceAll(
				"&nbsp;|\\s", ""));

		row = tableTag.getRow(4);
		colAry = row.getColumns();
		pttDao.setPttClassNum(colAry[1].getStringText().replaceAll(
				"&nbsp;|\\s", ""));

		row = tableTag.getRow(6);
		colAry = row.getColumns();
		pttDao.setProposer(colAry[1].getStringText().replaceAll("&nbsp;|\\s",
				""));

		row = tableTag.getRow(7);
		colAry = row.getColumns();
		pttDao.setProposerAddress(colAry[1].getStringText().replaceAll(
				"&nbsp;|\\s", ""));

		row = tableTag.getRow(8);
		colAry = row.getColumns();
		pttDao.setInventor(colAry[1].getStringText().replaceAll("&nbsp;|\\s",
				""));
		pttDao.setInternationalApply(colAry[3].getStringText().replaceAll(
				"&nbsp;|\\s", ""));

		row = tableTag.getRow(9);
		colAry = row.getColumns();
		pttDao.setInternationalPublication(colAry[1].getStringText()
				.replaceAll("&nbsp;|\\s", ""));
		if (!colAry[3].getStringText().replaceAll("&nbsp;|\\s|;", "")
				.replace(".", "-").equals("")) {
			pttDao.setIntoDate(Date.valueOf(colAry[3].getStringText()
					.replaceAll("&nbsp;|\\s|;", "").replace(".", "-")));
		} else {
			pttDao.setIntoDate(new Date(0));
		}

		row = tableTag.getRow(10);
		colAry = row.getColumns();
		pttDao.setPttAgencyOrg(colAry[1].getStringText().replaceAll(
				"&nbsp;|\\s", ""));
		pttDao.setPttAgencyPerson(colAry[3].getStringText().replaceAll(
				"&nbsp;|\\s", ""));

		parser = new Parser(html.toString());
		NodeList nodeList2 = parser
				.extractAllNodesThatMatch(new HasAttributeFilter("class",
						"zi_zw"));
		TableColumn tableCol = (TableColumn) nodeList2.elementAt(0);
		pttDao.setPttAbstract(tableCol.getStringText().replaceAll("&nbsp;|\\s",
				""));

		return pttDao;
	}

}
