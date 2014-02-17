package com.ljh.main;

import com.jacob.com.*;
import com.jacob.activeX.*;

public class OcrCom
{
	//main方法只是用于测试
	public static void main(String[] args)
	{
		OcrCom ocrCom = new OcrCom();
		//图片地址
		String url = "E:\\image\\test\\1.tif";
		String result = ocrCom.ocr(url);
		System.out.println("\n" + result);
	}

	public String ocr(String url)
	{	
		String str = "";
		try
		{
			ActiveXComponent dotnetCom = null;
			dotnetCom = new ActiveXComponent("TestOcrCom.OcrMain");//("0F82D5C2-1815-49E9-8491-7B601D05F0D4"); // 需要调用的C#代码中的命名空间名和类名。
			Variant var = Dispatch.call(dotnetCom, "Ocr", url); // 需要调用的方法名和参数值
			str = var.toString(); // 返回需要的字符串
			System.out.println(str); // 输出得到的字符串。检查结果是否正确。
					
		} catch (Exception ex)
		{
			System.out.println("something wrong in ocr");
			ex.printStackTrace();
		}	
		
		return str;
	}
}
