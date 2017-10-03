package com.br.mastercard;

import java.util.HashMap;
import java.util.Map;

public class Config {

	private static Map<String, Object> info = new HashMap<String, Object>();
	
	public static void set(String s, Object o)
	{
		info.put(s,  o);
	}
	
	public static Object get(String s)
	{
		return info.get(s);
	}
	
	public static Map<String, Object> getMap()
	{
		return info;
	}
	
}
