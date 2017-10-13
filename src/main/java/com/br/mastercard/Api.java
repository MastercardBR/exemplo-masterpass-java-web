package com.br.mastercard;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.mastercard.merchant.checkout.PaymentDataApi;
import com.mastercard.merchant.checkout.PostbackApi;
import com.mastercard.merchant.checkout.model.Card;
import com.mastercard.merchant.checkout.model.PaymentData;
import com.mastercard.merchant.checkout.model.Postback;
import com.mastercard.sdk.core.MasterCardApiConfig;
import com.mastercard.sdk.core.util.QueryParams;

public class Api 
{
	private String consumerKey;
	private String p12Path;
	private String password;
	private String checkoutId;
	private String cartId;
	
	public Api() throws Exception
	{
		
		this.consumerKey = Config.get("consumerKey").toString();
		this.p12Path = Config.get("projectdir").toString() + Config.get("path").toString();
		this.password = Config.get("password").toString();
		this.cartId = Config.get("cartId").toString();
		this.checkoutId = Config.get("checkoutId").toString();
		
		MasterCardApiConfig.setSandBox(true);     
    	MasterCardApiConfig.setConsumerKey(this.consumerKey);
    	MasterCardApiConfig.setPrivateKey(getPrivateKey());
		
	}
	
	private PrivateKey getPrivateKey() throws Exception
	{
		
		FileInputStream fis = new FileInputStream(this.p12Path);
		PrivateKey k = null;
		
		if(fis!=null)
		{
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(fis, this.password.toCharArray());
			
			k = (PrivateKey) ks.getKey(Config.get("alias").toString(), password.toCharArray());
			
		}
		
		fis.close();
		
		return k;
	}
	
    public Card getCard(String transactionId) throws Exception
    {
    	
    	QueryParams queryParams = new QueryParams()
                .add("checkoutId", this.checkoutId)
                .add("cartId", this.cartId);
   
    	
    	PaymentData payment = PaymentDataApi.show(transactionId, queryParams);
    
        return payment.getCard();
    }
    
    private Postback createPostback( String transactionId, String paymentCode, Boolean status )
    {
    	ZonedDateTime zdt = LocalDateTime.now().atZone(ZoneId.systemDefault());
    	java.util.Date date = java.util.Date.from(zdt.toInstant());
    	
    	return new Postback()
                .transactionId(transactionId)
                .currency(Config.get("currency").toString())
                .paymentCode(paymentCode)
                .paymentSuccessful(status)
                .amount( Double.valueOf(Config.get("amount").toString()))
                .paymentDate(date);
    	
    	
    }
    
    public Boolean performPostback(String transactionId, String paymentCode, Boolean status) throws Exception
    {
    	PostbackApi.create(this.createPostback(transactionId, paymentCode, status));
    	return true;
    }
    
    public Boolean performPostback(String precheckoutTransactionId, String transactionId, String paymentCode, Boolean status) throws Exception
    {
    	Postback p = this.createPostback(transactionId, paymentCode, status);
    	p.preCheckoutTransactionId(precheckoutTransactionId);
    	
    	PostbackApi.create(p);
    	return true;
    }
	
}
