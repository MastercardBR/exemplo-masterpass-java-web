package com.br.mastercard;

import com.br.mastercard.nosdk.request.*;

public class ApiNoSdk 
{
	
	public OAuthRequest getPaymentData(String transactionId) throws Exception
	{
		
		String url = String.format("https://sandbox.api.mastercard.com/masterpass/paymentdata/%s?cartId=%s&checkoutId=%s", 
										transactionId, 
										Config.get("cartId").toString(), 
										Config.get("checkoutId").toString());
		
		OAuthRequest req = new OAuthRequest();
		req.setAction(url);
		req.setMethod("GET");
		
		return req;
		
		
	}
	
	public OAuthRequest getPostbackData(String transactionId, String paymentCode, Boolean status) throws Exception
	{
		
		OAuthRequest req = new OAuthRequest();
		req.setAction("https://sandbox.api.mastercard.com/masterpass/postback");
		req.setMethod("POST");
		
		String json = String.format("{\"transactionId\":\"%s\",\"currency\":\"%s\",\"amount\":%f,\"paymentSuccessful\":%b,\"paymentCode\":\"%s\",\"paymentDate\":%d}",
									transactionId, 
									Config.get("currency").toString(),
									Double.valueOf(Config.get("amount").toString()),
									status,
									paymentCode,
									System.currentTimeMillis() / 1000L
									);
		
		req.setPostData(json);
		
		return req;
		
		
	}
	
}
