package com.br.mastercard.nosdk.request;

import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;


public class Util 
{
	
	public static String base64EncodeSpecial(String m)
	{
		return base64Encode( m.getBytes())
				 .replace("+", "%20").replace("*", "%2A")
                 .replace("%7E", "~");
	}
	
	public static String urlEncode(String url) throws UnsupportedEncodingException
	{
		return URLEncoder.encode(url, "UTF-8");
	}
	
	public static String base64Encode(byte []m)
	{
		return Base64.getEncoder().encodeToString(m);
	}
	
	public static String base64Encode(String m) throws UnsupportedEncodingException
	{
		return base64Encode( m.getBytes("UTF-8"));
	}
	
	public static byte[] sha256(String txt) throws Exception
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.update(txt.getBytes("UTF-8"));
		
		//return new String(digest.digest(), "UTF-8");
		return digest.digest();
		
	}
	
	public static byte[] sha1(String txt) throws Exception
	{
		
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.update(txt.getBytes("UTF-8"));
		
		//return new String(digest.digest(), "UTF-8");
		return digest.digest();
		
	}
	
	public static HashMap<String, String> splitQuery(URL url) throws UnsupportedEncodingException 
	{
		  final HashMap<String, String> query_pairs = new HashMap<String, String>();
		  
		  if( url.getQuery() != null )
		  {
			  final String[] pairs = url.getQuery().split("&");
			  
			  for (String pair : pairs) {
			    final int idx = pair.indexOf("=");
			    final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
			    
			    final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
			    query_pairs.put(key, value);
			  }
		  }
		  
		  return query_pairs;
	}

}
