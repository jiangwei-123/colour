package com.jw.colour.common;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Date;
import java.util.HashMap;

public class Geohash {
	private static int numbits = 6*5;

	private final static char[] digits = {'0','1','2','3','4','5','6','7','8','9','b','c','d','e','f',
			'g','h','j','k','m','n','p','q','r','s','t','u','v','w','x','y','z'};

	private static final double[] geoHashRadius = {2500000,630000,78000,20000,2400,610,76,19.11,
			4.78,0.5971,0.1492,0.0186};

	private final static HashMap<Character, Integer> lookup = new HashMap<>();
	
	static {
		int i = 0;
		for(char c : digits)
			lookup.put(c, i++);
	}

	/**
	* geo转为经纬度
	* @author jw
	* @date 2021/2/27
	* @param geohash geohash
	* @return: double[]
	**/
	public double[] decode(String geohash) {
		StringBuilder buffer = new StringBuilder();
		for(char c : geohash.toCharArray()) {
			int i = lookup.get(c) + 32;
			buffer.append(Integer.toString(i,2).substring(1));
		}
		BitSet lonset = new BitSet();
		BitSet latset = new BitSet();
		
		//even bits
		int j = 0;
		for (int i = 0; i < numbits * 2; i+=2) {
			boolean isSet = false;
			if(i < buffer.length())
				isSet = buffer.charAt(i) == '1';
			lonset.set(j++,isSet);
		}
		
		//odd bits
		j = 0;
		for (int i = 1; i < numbits * 2; i+=2) {
			boolean isSet = false;
			if(i < buffer.length())
				isSet = buffer.charAt(i) == '1';
			latset.set(j++,isSet);
		}
		double lon = decode(lonset,-180,180);
		double lat = decode(latset,-90,90);
		return new double[] {lat,lon};
	}

	/**
	* 二进制转为经纬度
	* @author jw
	* @date 2021/2/27
	* @param bs bs
	* @param floor floor
	* @param ceiling ceiling
	* @return: double
	**/
	private double decode(BitSet bs, double floor, double ceiling) {
		double mid = 0;
		for (int i = 0; i < bs.length(); i++) {
			mid = (floor+ceiling)/2;
			if(bs.get(i))
				floor = mid;
			else
				ceiling = mid;
		}
		return mid;
	}

	/**
	* 经纬度转为geo格式
	* @author jw
	* @date 2021/2/27
	* @param lon lon
	* @param lat lat
	* @return: java.lang.String
	**/
	public String encode(double lon,double lat) {
		BitSet latbitsLat = getBits(lat,-90,90);
		BitSet lonbitsLon = getBits(lon,-180,180);
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < numbits; i++) {
			buffer.append((lonbitsLon.get(i))?'1':'0');
			buffer.append((latbitsLat.get(i))?'1':'0');
		}
		return base32(Long.parseLong(buffer.toString(),2));
	}

	/**
	* 经纬度转为字节码
	* @author jw
	* @date 2021/2/27
	* @param lat lat
	* @param floor floor
	* @param ceiling ceiling
	* @return: java.util.BitSet
	**/
	private BitSet getBits(double lat, double floor, double ceiling) {
		BitSet buffer = new BitSet(numbits);
		for (int i = 0; i < numbits; i++) {
			double mid = (floor+ceiling)/2;
			if(lat >= mid) {
				buffer.set(i);
				floor = mid;
			}else {
				ceiling = mid;
			}
		}
		return buffer;
	}

	/**
	* base32加密
	* @author jw
	* @date 2021/2/27
	* @param i i
	* @return: java.lang.String
	**/
	public static String base32(long i) {
		char[] buf = new char[65];
		int charPos = 64;
		boolean negative = (i < 0);
		if(!negative)
			i = -i;
		while(i <= -32) {
			buf[charPos--] = digits[(int)(-(i % 32))];
			i /= 32;
		}
		buf[charPos] = digits[(int)(-i)];
		if(negative)
			buf[--charPos] = '-';
		return new String(buf,charPos,(65-charPos));
	}


	
	/**
	* 返回geohash长度
	* @author jw
	* @date 2021/3/1 
	* @param radius radius
	* @return: int
	**/        
	public static int getGeoHashLength(double radius) {
		for (int i = 0; i < geoHashRadius.length; i++) {
			if(radius > geoHashRadius[i]){
				return i;
			}
		}
		return geoHashRadius.length;
	}
	
	//25路公交测试
	public static void main(String[] args) throws Exception{
		System.out.println("------------------------------------------");
		String encode = new Geohash().encode(118.790707, 32.104723);
		System.out.println(encode);
		System.out.println(Arrays.toString(new Geohash().decode(encode)));
		String dateToString = DateUtil.dateToString(new Date(), "HH:00");
		System.out.println(new Date().getTime());
	}
	
}
