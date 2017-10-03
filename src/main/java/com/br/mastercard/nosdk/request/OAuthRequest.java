package com.br.mastercard.nosdk.request;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.br.mastercard.Config;

import java.util.HashMap;


public class OAuthRequest 
{
	private TreeMap<String, String> map = new TreeMap<>();
	
	private String method;
	private String alias;
	private String password;
	private String postData;
	private String action;
	private String keyPath;
	
	final static Logger logger = Logger.getLogger(OAuthRequest.class);
	
	public OAuthRequest()
	{
		map.put("oauth_nonce",  new BigInteger(130, new SecureRandom()).toString(32) );
		map.put("oauth_signature_method", "RSA-SHA256");
		map.put("oauth_timestamp", String.valueOf( System.currentTimeMillis() / 1000L));
		map.put("oauth_version", "1.0");
		map.put("oauth_consumer_key", Config.get("consumerKey").toString());
		
		
		this.keyPath = Config.get("projectdir").toString() + Config.get("path").toString();
		this.password = Config.get("password").toString();
		this.alias = Config.get("alias").toString();
		
		logger.debug("=== Starting oauth request ===");
		
		
	}
	
	
	public void setMethod(String method)
	{
		this.method = method;
	}
	
	public void setPostData(String data)
	{
		this.postData = data; 
	}
	
	public String getPostData()
	{
		return this.postData;
	}
	
	public String getAction()
	{
		return this.action;
	}
	
	public void setAction(String action)
	{
		logger.debug("Action is: " + action);
		this.action = action;
	}
	
	
	
	/*
	 * Generates the OAuth authentication realm, which should be used to execute requests.
	 * For POST and PUT methods, it's mandatory to add oauth_body_hash, which is a simple step of sha256 (raw) + base64
	 * 
	 * This also generates the oauth_signature, which is based on RSA-SHA256. The keys necessary to perform it are available at the developer console
	 * 
	 */
	public String getOAuthRealm() throws Exception
	{
		
		if(this.method == "POST" || this.method == "PUT")
		{
			
			logger.debug("POSTDATA: "+ postData);
			map.put("oauth_body_hash", Util.base64Encode(Util.sha256( postData  )));
		}
		
		
		// sign the request and add the signature
		SignRequest req = new SignRequest(getBaseString() , keyPath, alias, password);
		map.put("oauth_signature", req.sign() );
		
		
		String realm = "OAuth ";
		
		for(Entry<String, String> entry : map.entrySet()) {
		        String value = entry.getValue();
		        String key = entry.getKey();
		        
		        realm+= Util.urlEncode(key) + "=\"" + Util.urlEncode(value) + "\",";
		        
		}
		
		if(realm.endsWith(","))
			realm = realm.substring(0, realm.length()-1);
		
		logger.debug("OAUTH REALM: "+ realm);
		
		return realm;
		
	}
	
	/*
	 * Parameter String is formed by all oauth keys, except signature
	 * It must include any query string and post data, if the post data is www-form-urlencoded. 
	 * In the case of the mastercard, post is sent to the body, and doesnt need to.
	 * 
	 * It uses a treemap, which by default sorts keys by names, which is necessary for the oauth signature generation
	 */
	private String getParameterString() throws UnsupportedEncodingException, MalformedURLException 
	{

		HashMap<String, String> m = Util.splitQuery( new URL(this.action) );
		TreeMap<String, String> tm = new TreeMap<String, String>();
		
		String parameterString = ""; 
		
		// add query string
		for( Entry<String, String> entry: m.entrySet() )
		{
			tm.put(entry.getKey(), entry.getValue());
		}
		
		// add oauth params
		for(Entry<String, String> entry : map.entrySet()) 
		{
			tm.put(entry.getKey(), entry.getValue());
		}
	
		// get all the parameters ordered by key  
		for(Entry<String, String> entry : tm.entrySet()) 
		{
			parameterString += Util.urlEncode(entry.getKey()) + "=" + Util.urlEncode(entry.getValue()) + "&";
		}
		
		if(parameterString.endsWith("&"))
			parameterString = parameterString.substring(0, parameterString.length()-1);
		
		logger.debug("PARAMETER STRING: "+  parameterString);
		
		return parameterString;
	}
	
	/*
	 * Base string is the piece used to generate oauth signature. It must be set as:
	 * HTTP_METHOD&URL&PARAMETER_STRING
	 * 
	 * Where HTTP_METHOD is GET, POST, PUT
	 * Url is the endpoint url, without any query string
	 * Base String is described in the parameter string method
	 * 
	 */
	private String getBaseString() throws UnsupportedEncodingException, MalformedURLException
	{
		// need to trim the url until we find the extra parameters
		String baseAction = this.action.substring(0, this.action.indexOf("?") == -1 ? this.action.length() : this.action.indexOf("?"));
		String baseString = this.method + "&" + Util.urlEncode(baseAction) + "&"  + Util.urlEncode(getParameterString());
		logger.debug("BASE STRING: "+ baseString);
		return baseString;
	}
		
	
	
}
