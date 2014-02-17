package org.ictclas4j.utility;

import java.io.UnsupportedEncodingException;

import java.util.HashMap;

import java.util.LinkedHashMap;


/**
 * 和字符串相关的常用操作
 * 
 */
public class StringTools {
	private static final HashMap<String, String> map_hex2bin = new HashMap<String, String>(16);
	
	{
		map_hex2bin.put("0", "0000");
		map_hex2bin.put("1", "0001");
		map_hex2bin.put("2", "0010");
		map_hex2bin.put("3", "0011");
		map_hex2bin.put("4", "0100");
		map_hex2bin.put("5", "0101");
		map_hex2bin.put("6", "0110");
		map_hex2bin.put("7", "0111");
		map_hex2bin.put("8", "1000");
		map_hex2bin.put("9", "1001");
		map_hex2bin.put("A", "1010");
		map_hex2bin.put("B", "1011");
		map_hex2bin.put("C", "1100");
		map_hex2bin.put("D", "1101");
		map_hex2bin.put("E", "1110");
		map_hex2bin.put("F", "1111");		
	}
	
	public final static HashMap<String, String> map_bin2hex = new HashMap<String, String>(16);
	
	{
		map_bin2hex.put("0000", "0");
		map_bin2hex.put("0001", "1");
		map_bin2hex.put("0010", "2");
		map_bin2hex.put("0011", "3");
		map_bin2hex.put("0100", "4");
		map_bin2hex.put("0101", "5");
		map_bin2hex.put("0110", "6");
		map_bin2hex.put("0111", "7");
		map_bin2hex.put("1000", "8");
		map_bin2hex.put("1001", "9");
		map_bin2hex.put("1010", "A");
		map_bin2hex.put("1011", "B");
		map_bin2hex.put("1100", "C");
		map_bin2hex.put("1101", "D");
		map_bin2hex.put("1110", "E");
		map_bin2hex.put("1111", "F");
	}

	private final static LinkedHashMap<String, Integer> bopoMap = new LinkedHashMap<String, Integer>();
	{
		bopoMap.put("a", 1);	
		bopoMap.put("a", -20319);
		bopoMap.put("ai", -20317);
		bopoMap.put("an", -20304);
		bopoMap.put("ang", -20295);
		bopoMap.put("ao", -20292);
		bopoMap.put("ba", -20283);
		bopoMap.put("bai", -20265);
		bopoMap.put("ban", -20257);
		bopoMap.put("bang", -20242);
		bopoMap.put("bao", -20230);
		bopoMap.put("bei", -20051);
		bopoMap.put("ben", -20036);
		bopoMap.put("beng", -20032);
		bopoMap.put("bi", -20026);
		bopoMap.put("bian", -20002);
		bopoMap.put("biao", -19990);
		bopoMap.put("bie", -19986);
		bopoMap.put("bin", -19982);
		bopoMap.put("bing", -19976);
		bopoMap.put("bo", -19805);
		bopoMap.put("bu", -19784);
		bopoMap.put("ca", -19775);
		bopoMap.put("cai", -19774);
		bopoMap.put("can", -19763);
		bopoMap.put("cang", -19756);
		bopoMap.put("cao", -19751);
		bopoMap.put("ce", -19746);
		bopoMap.put("ceng", -19741);
		bopoMap.put("cha", -19739);
		bopoMap.put("chai", -19728);
		bopoMap.put("chan", -19725);
		bopoMap.put("chang", -19715);
		bopoMap.put("chao", -19540);
		bopoMap.put("che", -19531);
		bopoMap.put("chen", -19525);
		bopoMap.put("cheng", -19515);
		bopoMap.put("chi", -19500);
		bopoMap.put("chong", -19484);
		bopoMap.put("chou", -19479);
		bopoMap.put("chu", -19467);
		bopoMap.put("chuai", -19289);
		bopoMap.put("chuan", -19288);
		bopoMap.put("chuang", -19281);
		bopoMap.put("chui", -19275);
		bopoMap.put("chun", -19270);
		bopoMap.put("chuo", -19263);
		bopoMap.put("ci", -19261);
		bopoMap.put("cong", -19249);
		bopoMap.put("cou", -19243);
		bopoMap.put("cu", -19242);
		bopoMap.put("cuan", -19238);
		bopoMap.put("cui", -19235);
		bopoMap.put("cun", -19227);
		bopoMap.put("cuo", -19224);
		bopoMap.put("da", -19218);
		bopoMap.put("dai", -19212);
		bopoMap.put("dan", -19038);
		bopoMap.put("dang", -19023);
		bopoMap.put("dao", -19018);
		bopoMap.put("de", -19006);
		bopoMap.put("deng", -19003);
		bopoMap.put("di", -18996);
		bopoMap.put("dian", -18977);
		bopoMap.put("diao", -18961);
		bopoMap.put("die", -18952);
		bopoMap.put("ding", -18783);
		bopoMap.put("diu", -18774);
		bopoMap.put("dong", -18773);
		bopoMap.put("dou", -18763);
		bopoMap.put("du", -18756);
		bopoMap.put("duan", -18741);
		bopoMap.put("dui", -18735);
		bopoMap.put("dun", -18731);
		bopoMap.put("duo", -18722);
		bopoMap.put("e", -18710);
		bopoMap.put("en", -18697);
		bopoMap.put("er", -18696);
		bopoMap.put("fa", -18526);
		bopoMap.put("fan", -18518);
		bopoMap.put("fang", -18501);
		bopoMap.put("fei", -18490);
		bopoMap.put("fen", -18478);
		bopoMap.put("feng", -18463);
		bopoMap.put("fo", -18448);
		bopoMap.put("fou", -18447);
		bopoMap.put("fu", -18446);
		bopoMap.put("ga", -18239);
		bopoMap.put("gai", -18237);
		bopoMap.put("gan", -18231);
		bopoMap.put("gang", -18220);
		bopoMap.put("gao", -18211);
		bopoMap.put("ge", -18201);
		bopoMap.put("gei", -18184);
		bopoMap.put("gen", -18183);
		bopoMap.put("geng", -18181);
		bopoMap.put("gong", -18012);
		bopoMap.put("gou", -17997);
		bopoMap.put("gu", -17988);
		bopoMap.put("gua", -17970);
		bopoMap.put("guai", -17964);
		bopoMap.put("guan", -17961);
		bopoMap.put("guang", -17950);
		bopoMap.put("gui", -17947);
		bopoMap.put("gun", -17931);
		bopoMap.put("guo", -17928);
		bopoMap.put("ha", -17922);
		bopoMap.put("hai", -17759);
		bopoMap.put("han", -17752);
		bopoMap.put("hang", -17733);
		bopoMap.put("hao", -17730);
		bopoMap.put("he", -17721);
		bopoMap.put("hei", -17703);
		bopoMap.put("hen", -17701);
		bopoMap.put("heng", -17697);
		bopoMap.put("hong", -17692);
		bopoMap.put("hou", -17683);
		bopoMap.put("hu", -17676);
		bopoMap.put("hua", -17496);
		bopoMap.put("huai", -17487);
		bopoMap.put("huan", -17482);
		bopoMap.put("huang", -17468);
		bopoMap.put("hui", -17454);
		bopoMap.put("hun", -17433);
		bopoMap.put("huo", -17427);
		bopoMap.put("ji", -17417);
		bopoMap.put("jia", -17202);
		bopoMap.put("jian", -17185);
		bopoMap.put("jiang", -16983);
		bopoMap.put("jiao", -16970);
		bopoMap.put("jie", -16942);
		bopoMap.put("jin", -16915);
		bopoMap.put("jing", -16733);
		bopoMap.put("jiong", -16708);
		bopoMap.put("jiu", -16706);
		bopoMap.put("ju", -16689);
		bopoMap.put("juan", -16664);
		bopoMap.put("jue", -16657);
		bopoMap.put("jun", -16647);
		bopoMap.put("ka", -16474);
		bopoMap.put("kai", -16470);
		bopoMap.put("kan", -16465);
		bopoMap.put("kang", -16459);
		bopoMap.put("kao", -16452);
		bopoMap.put("ke", -16448);
		bopoMap.put("ken", -16433);
		bopoMap.put("keng", -16429);
		bopoMap.put("kong", -16427);
		bopoMap.put("kou", -16423);
		bopoMap.put("ku", -16419);
		bopoMap.put("kua", -16412);
		bopoMap.put("kuai", -16407);
		bopoMap.put("kuan", -16403);
		bopoMap.put("kuang", -16401);
		bopoMap.put("kui", -16393);
		bopoMap.put("kun", -16220);
		bopoMap.put("kuo", -16216);
		bopoMap.put("la", -16212);
		bopoMap.put("lai", -16205);
		bopoMap.put("lan", -16202);
		bopoMap.put("lang", -16187);
		bopoMap.put("lao", -16180);
		bopoMap.put("le", -16171);
		bopoMap.put("lei", -16169);
		bopoMap.put("leng", -16158);
		bopoMap.put("li", -16155);
		bopoMap.put("lia", -15959);
		bopoMap.put("lian", -15958);
		bopoMap.put("liang", -15944);
		bopoMap.put("liao", -15933);
		bopoMap.put("lie", -15920);
		bopoMap.put("lin", -15915);
		bopoMap.put("ling", -15903);
		bopoMap.put("liu", -15889);
		bopoMap.put("long", -15878);
		bopoMap.put("lou", -15707);
		bopoMap.put("lu", -15701);
		bopoMap.put("lv", -15681);
		bopoMap.put("luan", -15667);
		bopoMap.put("lue", -15661);
		bopoMap.put("lun", -15659);
		bopoMap.put("luo", -15652);
		bopoMap.put("ma", -15640);
		bopoMap.put("mai", -15631);
		bopoMap.put("man", -15625);
		bopoMap.put("mang", -15454);
		bopoMap.put("mao", -15448);
		bopoMap.put("me", -15436);
		bopoMap.put("mei", -15435);
		bopoMap.put("men", -15419);
		bopoMap.put("meng", -15416);
		bopoMap.put("mi", -15408);
		bopoMap.put("mian", -15394);
		bopoMap.put("miao", -15385);
		bopoMap.put("mie", -15377);
		bopoMap.put("min", -15375);
		bopoMap.put("ming", -15369);
		bopoMap.put("miu", -15363);
		bopoMap.put("mo", -15362);
		bopoMap.put("mou", -15183);
		bopoMap.put("mu", -15180);
		bopoMap.put("na", -15165);
		bopoMap.put("nai", -15158);
		bopoMap.put("nan", -15153);
		bopoMap.put("nang", -15150);
		bopoMap.put("nao", -15149);
		bopoMap.put("ne", -15144);
		bopoMap.put("nei", -15143);
		bopoMap.put("nen", -15141);
		bopoMap.put("neng", -15140);
		bopoMap.put("ni", -15139);
		bopoMap.put("nian", -15128);
		bopoMap.put("niang", -15121);
		bopoMap.put("niao", -15119);
		bopoMap.put("nie", -15117);
		bopoMap.put("nin", -15110);
		bopoMap.put("ning", -15109);
		bopoMap.put("niu", -14941);
		bopoMap.put("nong", -14937);
		bopoMap.put("nu", -14933);
		bopoMap.put("nv", -14930);
		bopoMap.put("nuan", -14929);
		bopoMap.put("nue", -14928);
		bopoMap.put("nuo", -14926);
		bopoMap.put("o", -14922);
		bopoMap.put("ou", -14921);
		bopoMap.put("pa", -14914);
		bopoMap.put("pai", -14908);
		bopoMap.put("pan", -14902);
		bopoMap.put("pang", -14894);
		bopoMap.put("pao", -14889);
		bopoMap.put("pei", -14882);
		bopoMap.put("pen", -14873);
		bopoMap.put("peng", -14871);
		bopoMap.put("pi", -14857);
		bopoMap.put("pian", -14678);
		bopoMap.put("piao", -14674);
		bopoMap.put("pie", -14670);
		bopoMap.put("pin", -14668);
		bopoMap.put("ping", -14663);
		bopoMap.put("po", -14654);
		bopoMap.put("pu", -14645);
		bopoMap.put("qi", -14630);
		bopoMap.put("qia", -14594);
		bopoMap.put("qian", -14429);
		bopoMap.put("qiang", -14407);
		bopoMap.put("qiao", -14399);
		bopoMap.put("qie", -14384);
		bopoMap.put("qin", -14379);
		bopoMap.put("qing", -14368);
		bopoMap.put("qiong", -14355);
		bopoMap.put("qiu", -14353);
		bopoMap.put("qu", -14345);
		bopoMap.put("quan", -14170);
		bopoMap.put("que", -14159);
		bopoMap.put("qun", -14151);
		bopoMap.put("ran", -14149);
		bopoMap.put("rang", -14145);
		bopoMap.put("rao", -14140);
		bopoMap.put("re", -14137);
		bopoMap.put("ren", -14135);
		bopoMap.put("reng", -14125);
		bopoMap.put("ri", -14123);
		bopoMap.put("rong", -14122);
		bopoMap.put("rou", -14112);
		bopoMap.put("ru", -14109);
		bopoMap.put("ruan", -14099);
		bopoMap.put("rui", -14097);
		bopoMap.put("run", -14094);
		bopoMap.put("ruo", -14092);
		bopoMap.put("sa", -14090);
		bopoMap.put("sai", -14087);
		bopoMap.put("san", -14083);
		bopoMap.put("sang", -13917);
		bopoMap.put("sao", -13914);
		bopoMap.put("se", -13910);
		bopoMap.put("sen", -13907);
		bopoMap.put("seng", -13906);
		bopoMap.put("sha", -13905);
		bopoMap.put("shai", -13896);
		bopoMap.put("shan", -13894);
		bopoMap.put("shang", -13878);
		bopoMap.put("shao", -13870);
		bopoMap.put("she", -13859);
		bopoMap.put("shen", -13847);
		bopoMap.put("sheng", -13831);
		bopoMap.put("shi", -13658);
		bopoMap.put("shou", -13611);
		bopoMap.put("shu", -13601);
		bopoMap.put("shua", -13406);
		bopoMap.put("shuai", -13404);
		bopoMap.put("shuan", -13400);
		bopoMap.put("shuang", -13398);
		bopoMap.put("shui", -13395);
		bopoMap.put("shun", -13391);
		bopoMap.put("shuo", -13387);
		bopoMap.put("si", -13383);
		bopoMap.put("song", -13367);
		bopoMap.put("sou", -13359);
		bopoMap.put("su", -13356);
		bopoMap.put("suan", -13343);
		bopoMap.put("sui", -13340);
		bopoMap.put("sun", -13329);
		bopoMap.put("suo", -13326);
		bopoMap.put("ta", -13318);
		bopoMap.put("tai", -13147);
		bopoMap.put("tan", -13138);
		bopoMap.put("tang", -13120);
		bopoMap.put("tao", -13107);
		bopoMap.put("te", -13096);
		bopoMap.put("teng", -13095);
		bopoMap.put("ti", -13091);
		bopoMap.put("tian", -13076);
		bopoMap.put("tiao", -13068);
		bopoMap.put("tie", -13063);
		bopoMap.put("ting", -13060);
		bopoMap.put("tong", -12888);
		bopoMap.put("tou", -12875);
		bopoMap.put("tu", -12871);
		bopoMap.put("tuan", -12860);
		bopoMap.put("tui", -12858);
		bopoMap.put("tun", -12852);
		bopoMap.put("tuo", -12849);
		bopoMap.put("wa", -12838);
		bopoMap.put("wai", -12831);
		bopoMap.put("wan", -12829);
		bopoMap.put("wang", -12812);
		bopoMap.put("wei", -12802);
		bopoMap.put("wen", -12607);
		bopoMap.put("weng", -12597);
		bopoMap.put("wo", -12594);
		bopoMap.put("wu", -12585);
		bopoMap.put("xi", -12556);
		bopoMap.put("xia", -12359);
		bopoMap.put("xian", -12346);
		bopoMap.put("xiang", -12320);
		bopoMap.put("xiao", -12300);
		bopoMap.put("xie", -12120);
		bopoMap.put("xin", -12099);
		bopoMap.put("xing", -12089);
		bopoMap.put("xiong", -12074);
		bopoMap.put("xiu", -12067);
		bopoMap.put("xu", -12058);
		bopoMap.put("xuan", -12039);
		bopoMap.put("xue", -11867);
		bopoMap.put("xun", -11861);
		bopoMap.put("ya", -11847);
		bopoMap.put("yan", -11831);
		bopoMap.put("yang", -11798);
		bopoMap.put("yao", -11781);
		bopoMap.put("ye", -11604);
		bopoMap.put("yi", -11589);
		bopoMap.put("yin", -11536);
		bopoMap.put("ying", -11358);
		bopoMap.put("yo", -11340);
		bopoMap.put("yong", -11339);
		bopoMap.put("you", -11324);
		bopoMap.put("yu", -11303);
		bopoMap.put("yuan", -11097);
		bopoMap.put("yue", -11077);
		bopoMap.put("yun", -11067);
		bopoMap.put("za", -11055);
		bopoMap.put("zai", -11052);
		bopoMap.put("zan", -11045);
		bopoMap.put("zang", -11041);
		bopoMap.put("zao", -11038);
		bopoMap.put("ze", -11024);
		bopoMap.put("zei", -11020);
		bopoMap.put("zen", -11019);
		bopoMap.put("zeng", -11018);
		bopoMap.put("zha", -11014);
		bopoMap.put("zhai", -10838);
		bopoMap.put("zhan", -10832);
		bopoMap.put("zhang", -10815);
		bopoMap.put("zhao", -10800);
		bopoMap.put("zhe", -10790);
		bopoMap.put("zhen", -10780);
		bopoMap.put("zheng", -10764);
		bopoMap.put("zhi", -10587);
		bopoMap.put("zhong", -10544);
		bopoMap.put("zhou", -10533);
		bopoMap.put("zhu", -10519);
		bopoMap.put("zhua", -10331);
		bopoMap.put("zhuai", -10329);
		bopoMap.put("zhuan", -10328);
		bopoMap.put("zhuang", -10322);
		bopoMap.put("zhui", -10315);
		bopoMap.put("zhun", -10309);
		bopoMap.put("zhuo", -10307);
		bopoMap.put("zi", -10296);
		bopoMap.put("zong", -10281);
		bopoMap.put("zou", -10274);
		bopoMap.put("zu", -10270);
		bopoMap.put("zuan", -10262);
		bopoMap.put("zui", -10260);
		bopoMap.put("zun", -10256);
		bopoMap.put("zuo", -10254);
		bopoMap.put("", -10246);
	}
	
	/**
	 * 判断一个字符串是否是数字
	 */
	public static boolean isNumeric(String str) {
		if (str != null) {

			try {
				str = str.trim();
				double d = Double.parseDouble(str);
				d = d + 1;
				return true;
			} catch (NumberFormatException e) {

			}
		}
		return false;
	}

	/**
	 * 判断字符串是否全是汉字
	 */
	public static boolean isAllChinese(String str) {
		if (str != null) {
			str = quan2ban(str);
			if (str != null) {
				if (str.length() * 2 == str.getBytes().length)
					return true;
			}
		}

		return false;
	}


	/**
	 * 把表示数字含义的字符串转你成整形
	 */
	public static int cint(String str) {
		if (str != null)
			try {
				int i = new Integer(str).intValue();
				return i;
			} catch (NumberFormatException e) {

			}

		return -1;
	}



	/**
	 * 用UTF-16BE的编码方式把含有全角编码的字符串转成半角编码的字符串
	 */
	public static String quan2ban(String str) {
		String result = null;

		if (str != null) {
			try {
				byte[] uniBytes = str.getBytes("utf-16be");
				byte[] b = new byte[uniBytes.length];
				for (int i = 0; i < b.length; i++) {
					if (uniBytes[i] == -1) {
						b[i] = 0;
						if (i + 1 < uniBytes.length)
							b[++i] = (byte) (uniBytes[i] + 0x20);

					} else
						b[i] = uniBytes[i];
				}

				result = new String(b, "utf-16be");
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 用UTF-16BE的编码方式把含有半角的字符串转成全角字符串
	 */
	public static String ban2quan(String str) {
		String result = null;

		if (str != null) {
			try {
				byte[] uniBytes = str.getBytes("utf-16be");
				byte[] b = new byte[uniBytes.length];
				for (int i = 0; i < b.length; i++) {
					if (uniBytes[i] == 0) {
						b[i] = -1;
						if (i + 1 < uniBytes.length)
							b[++i] = (byte) (uniBytes[i] - 0x20);

					} else
						b[i] = uniBytes[i];
				}
				result = new String(b, "utf-16be");
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 用GBK编码进行全角转半角
	 */
	public static String quan2banGBK(String str) {
		String result = null;

		if (str != null) {
			try {
				int j = 0;
				byte[] uniBytes = str.getBytes("GBK");
				byte[] b = new byte[uniBytes.length];
				for (int i = 0; i < b.length; i++) {
					if (uniBytes[i] == (byte) 0xA3) {
						if (i + 1 < uniBytes.length)
							b[j] = (byte) (uniBytes[++i] - 0x80);
					} else {
						b[j] = uniBytes[i];
						if (uniBytes[i] < 0 && i + 1 < b.length)
							b[++j] = uniBytes[++i];

					}
					j++;
				}
				result = new String(b, 0, j, "GBK");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 用GBK编码进行半角转全角
	 */
	public static String ban2quanGBK(String str) {
		String result = null;

		if (str != null) {
			try {
				int j = 0;
				byte[] uniBytes = str.getBytes("GBK");
				byte[] b = new byte[uniBytes.length * 2];
				for (int i = 0; i < uniBytes.length; i++) {
					if (uniBytes[i] >= 0) {
						b[j] = (byte) 0xA3;
						if (j + 1 < b.length)
							b[++j] = (byte) (uniBytes[i] + 0x80);

					} else {
						b[j] = uniBytes[i];
						if (i + 1 < uniBytes.length && j + 1 < b.length)
							b[++j] = uniBytes[++i];
					}

					j++;
				}
				result = new String(b, 0, j, "GBK");
			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 去掉字符串中的空白符
	 */
	public static String removeSpace(String s) {
		String rs = null;
		String s1 = null;

		if (s != null) {
			s += " ";
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < s.length(); i++) {
				s1 = s.substring(i, i + 1);
				if (!s1.equals(" "))
					sb.append(s1);
			}

			rs = sb.toString();
		}
		return rs;
	}

	/**
	 * 对字符串进行原子分隔
	 */
	public static String[] atomSplit(String str) {		
		if (str==null) {return null;}
		
		String[] result = null;
		int nLen=str.length();
		result = new String[nLen];			
		for (int i = 0; i < nLen; i++) {
			result[i] = str.substring(i, i + 1);
		}
		return result;
	}
	

	/**
	 * 按字典顺序对两个字符串进行比较
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	///////////////////////////////
	public static int compareTo(String s1, String s2) {
		if (s1 == null && s2 == null)
			return 0;
		else if (s1 != null && s2 == null)
			return 1;
		else if (s1 == null && s2 != null)
			return -1;
		else {
			int len = Math.min(s1.length(), s2.length());
			s1 += " ";
			s2 += " ";
			for (int i = 0; i < len; i++) {
				String id1 = s1.substring(i, i + 1);
				String id2 = s2.substring(i, i + 1);
				int rs = getID(id1) - getID(id2);

				if (rs != 0)
					return rs;
			}

			if (s1.length() > s2.length())
				return 1;
			else if (s1.length() < s2.length())
				return -1;
			else
				return 0;
		}

	}

	/**
	 * 根据ID号得到对应的GB汉字
	 * 
	 * @param id
	 *            0--6767
	 * @return
	 */
	public static int getID(String s) {
		int result = -1;

		if (s != null && s.length() == 1) {
			byte[] b = s.getBytes();
			if (b.length == 2) {
				int high = b[0] + 256;
				int low = b[1] + 256;

				return high * 256 + low;
			} else
				return b[0];
		}
		return result;
	}

	
}
