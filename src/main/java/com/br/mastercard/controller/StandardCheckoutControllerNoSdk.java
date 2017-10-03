package com.br.mastercard.controller;

import java.util.HashMap;
import java.util.Map;

import com.br.mastercard.Config;
import com.br.mastercard.nosdk.request.OAuthRequest;
import com.br.mastercard.ApiNoSdk;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;

public class StandardCheckoutControllerNoSdk {
	
	
	// pagina principal
    public static Route serveStandardPage = (Request req, Response res) -> {
    	
    	Config.set("callback", "http://wallet.labfintech.com.br:4567/callback-standard-nosdk");
    	
    	Map<String, Object> model = new HashMap<>();
    	model.put("config", Config.getMap());
		 
		return new VelocityTemplateEngine().render(
		        new ModelAndView(model, "templates/index.vm")
		 );
    	
    };
    
    
    public static Route handleCallback = (Request req, Response res) -> {
    	

    	ApiNoSdk a = new ApiNoSdk();
    	OAuthRequest r = a.getPaymentData(req.queryParams("oauth_verifier"));
    	
    	Map<String, Object> model = new HashMap<>();
    	String curlPaymentData = String.format("curl -v -H 'Content-Type: application/json' -H 'Authorization: %s' '%s'", 
    									r.getOAuthRealm(), 
    									r.getAction());
    	
    	
    	OAuthRequest p = a.getPostbackData(req.queryParams("oauth_verifier"),"123456", true);
    	

    	String curlPostback = String.format("curl -v -H 'Content-Type: application/json' -H 'Authorization: %s' -X POST -d '%s' '%s'", 
									p.getOAuthRealm(), 
									p.getPostData(), 
									p.getAction());
    	
    	
    	model.put("payment_data", curlPaymentData );
    	model.put("postback_data", curlPostback );

    	return new VelocityTemplateEngine().render(
		        new ModelAndView(model, "templates/no-sdk.vm")
		 ); 
		 
    };

}
