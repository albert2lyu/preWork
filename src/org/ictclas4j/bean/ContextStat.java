package org.ictclas4j.bean;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

//import org.apache.log4j.Logger;
import org.ictclas4j.utility.CommonTools;
import org.ictclas4j.utility.Utility;


public class ContextStat {
	private int tableLen;

	private int[] symbolTable;

	private ArrayList<TagContext> tcList;

	public ContextStat() { 
		tcList = new ArrayList<TagContext>();
	}

	public boolean load(String fileName) {
		return load(fileName, false);
	}

	public boolean load(String fileName, boolean isReset) {
		File file = new File(fileName);
		if (!file.canRead())
			return false;// fail while opening the file

		try {

			byte[] b = null;
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			tableLen = CommonTools.bytes2int(Utility.readBytes(in, 4), false);

			symbolTable = new int[tableLen];
			for (int i = 0; i < tableLen; i++) {
				b = Utility.readBytes(in, 4);
				symbolTable[i] = CommonTools.bytes2int(b, false);
			}

			long fileLen = file.length();
			long curLen = 4 + tableLen * 4; //File OffSet
			while (curLen < fileLen) {

				TagContext tc = new TagContext();

				b = Utility.readBytes(in, 4);
				int key = CommonTools.bytes2int(b);
				curLen += 4;

				b = Utility.readBytes(in, 4);
				curLen += 4;
				int totalFreq = CommonTools.bytes2int(b, false);

				int[] tagFreq = new int[tableLen];
				for (int i = 0; i < tableLen; i++) {
					b = Utility.readBytes(in, 4);
					curLen += 4;
					tagFreq[i] = CommonTools.bytes2int(b, false);
				}


				int[][] contextArray = new int[tableLen][tableLen];
				for (int i = 0; i < tableLen; i++) {
					String pr = "";
					for (int j = 0; j < tableLen; j++) {
						b = Utility.readBytes(in, 4);
						curLen += 4;
						contextArray[i][j] = CommonTools.bytes2int(b, false);
						pr += " " + contextArray[i][j];
					}

				}

				tc.setTotalFreq(totalFreq);
				tc.setKey(key);
				tc.setTagFreq(tagFreq);
				tc.setContextArray(contextArray);
				tcList.add(tc);
			}
			in.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
		return true;
	}

	public int getFreq(int key, int symbol) {
		TagContext tc = getItem(key);
		if (tc == null)
			return 0;

		int index = Utility.binarySearch(symbol, symbolTable);
		if (index == -1)// error finding the symbol
			return 0;

		// Add the frequency
		int frequency = 0;
		if (tc.getTagFreq() != null)
			frequency = tc.getTagFreq()[index];
		return frequency;

	}

	public double getPossibility(int key, int prev, int cur) {
		double result = 0;

		int curIndex = Utility.binarySearch(cur, symbolTable);
		int prevIndex = Utility.binarySearch(prev, symbolTable);

		TagContext tc = getItem(key);

		// return a lower value, not 0 to prevent data sparse
		if (tc == null || curIndex == -1 || prevIndex == -1
				|| tc.getContextArray()[prevIndex][curIndex] == 0
				|| tc.getTagFreq()[prevIndex] == 0)
			return 0.000001;
		
		int prevCurConFreq = tc.getContextArray()[prevIndex][curIndex];
		int prevFreq = tc.getTagFreq()[prevIndex];

		// 0.9 and 0.1 is a value based experience
		result = 0.9 * (double) prevCurConFreq;
		result /= (double) prevFreq;
		result += 0.1 * (double) prevFreq / (double) tc.getTotalFreq();

		return result;
	}

	public TagContext getItem(int key) {
		TagContext result = null; 
		
		if(tcList==null||tcList.size()==0)
			return null;
		if (key == 0  )
			result = tcList.get(0);
		else   {
			int i=0;
			for ( ; i < tcList.size() && tcList.get(i).getKey()<key; i++);
			if(i<tcList.size() && tcList.get(i).getKey()==key)
				result=tcList.get(i);
			else if(i-1<tcList.size())
				result=tcList.get(i-1);
		}
		
		return result;
	}

}
