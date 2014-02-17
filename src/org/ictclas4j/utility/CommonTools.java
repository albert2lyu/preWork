package org.ictclas4j.utility;

public class CommonTools {

	public static int bytesCopy(byte d[], byte s[], int from, int maxlen) {
		int end = from;

		if (s != null && d != null) {
			if (from >= 0 && maxlen > 0) {
				if (s.length < maxlen) {
					for (int i = 0; i < s.length && i + from < d.length; i++)
						d[i + from] = s[i];
					end = from + maxlen - 1;

				} else {
					for (int i = 0; i < maxlen && i + from < d.length; i++) {
						d[i + from] = s[i];
						end = i + from;
					}
				}
			} else if (from < 0 && maxlen > 0) {
				for (int i = d.length + from, j = 0; i > 0
						&& j < (s.length > maxlen ? maxlen : s.length); i--, j++) {
					d[i] = s[j];
					end = i;
				}
			}
		}
		return end;
	}


	public static byte[] bytesCopy(byte[] src, int from, int len) {
		byte[] result = null;
		int totalLen = 0;

		if (src != null && src.length > 0 && len > 0) {
			if (from >= 0) {
				totalLen = src.length > from + len ? len : src.length - from;
				result = new byte[Math.abs(len)];

				for (int i = from, j = 0; i < from + totalLen; i++, j++)
					result[j] = src[i];

			} else {
				int i0 = src.length + from;// 正向实际位置
				if (i0 - len < 0)
					totalLen = i0 + 1;
				else
					totalLen = len;

				result = new byte[totalLen];
				for (int i = i0, j = 0; i >= 0 && j < totalLen; i--, j++)
					result[j] = src[i];
			}

		}

		return result;
	}

	public static byte[] int2bytes(int a, boolean isHighFirst) {
		byte[] result = new byte[4];

		if (isHighFirst) { 
			//高位优先
			result[0] = (byte) (a >> 24 & 0xff);
			result[1] = (byte) (a >> 16 & 0xff);
			result[2] = (byte) (a >> 8 & 0xff);
			result[3] = (byte) (a & 0xff);
		} else {
			//地位优先
			result[3] = (byte) (a >> 24 & 0xff);
			result[2] = (byte) (a >> 16 & 0xff);
			result[1] = (byte) (a >> 8 & 0xff);
			result[0] = (byte) (a & 0xff);
		}
		return result;	
	}

	public static byte[] int2bytes(int a) {
		return int2bytes(a, true);
	}


	public static String getClassName(Object obj) {
		String name = null;
		if (obj != null) {
			int index = 0;
			String temp = obj.getClass().toString();

			index = temp.lastIndexOf(".");
			if (index > 0 && index < temp.length())
				name = temp.substring(index + 1);

		}
		return name;
	}

	public static int bytes2int(byte[] b) {

		return (int) bytes2long(b);
	}

	public static int bytes2int(byte[] b, boolean isHighFirst) {

		return (int) bytes2long(b, isHighFirst);
	}


	public static long bytes2long(byte[] b) {

		return bytes2long(b, true);
	}


	public static long bytes2long(byte[] b, boolean isHighFirst) {
		long result = 0;

		if (b != null && b.length <= 8) {
			long value;

			if (isHighFirst) {
				for (int i = b.length - 1, j = 0; i >= 0; i--, j++) {
					value = (long) (b[i] & 0xFF);
					result += value << (j <<3);
				}
			} else {
				for (int i = 0, j = 0; i < b.length - 1; i++, j++) {
					value = (long) (b[i] & 0xFF);
					result += value << (j <<3);
				}
			}
		}

		return result;
	}

 

	public static int getUnsigned(byte b) {
		if (b > 0)
			return (int) b;
		else
			return (b & 0x7F+128);
	}

}