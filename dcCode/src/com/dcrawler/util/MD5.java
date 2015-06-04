package com.dcrawler.util;

import java.security.MessageDigest;

public class MD5 {
	public static String getMD5(byte[] source){
		String s = null;
		// 用来将字节转换成16进制表示的字符
		char hexDigits[] = {'0','1','2',
				'3','4','5',
				'6','7','8',
				'9','a','b',
				'c','d','e',
				'f'
				};
		try {
			// MD5的计算结果是一个128bit的长整数
			// 用字节表示就是16个字节
			MessageDigest md = 
					MessageDigest.getInstance("MD5");
			md.update(source);
			byte[] tmp = md.digest();
			// 每个字节用16进制表示的话，使用两个字符
			// 所以表示成16进制需要32个字符
			char str[]= new char[16*2];
			// 表示转换结果中对应的字符位置
			int k = 0;
			for (int i = 0; i < 16; i++) {
				// 从第一个字节开始，将MD5的每一个字节转换成16进制字符
				// 取第i个字节
				byte byte0 = tmp[i];
				// 取字节中高4位的数字转换，
				// >>> 为逻辑右移，将符号为一起右移
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				// 取字节中低4位的数字转换
				str[k++]= hexDigits[byte0 & 0xf];
				
			}
			// 将转换后的结果转换为字符串
			s = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return s;
	}
	
	public static void main(String[] args) {
		String xubin = MD5.getMD5("xubin".getBytes());
		System.out.println(xubin);
	}
}
