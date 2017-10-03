package com.br.mastercard.controller;

import java.util.HashMap;
import java.util.Map;

import com.br.mastercard.Config;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;

public class ExpressCheckoutController {
	
	// pagina principal
    public static Route serveStandardPage = (Request req, Response res) -> {
    	
    	Config.set("callback", "http://wallet.labfintech.com.br:4567/callback-express");
    	
    	Map<String, Object> model = new HashMap<>();
    	model.put("config", Config.getMap());
		 
		 	return new VelocityTemplateEngine().render(
		        new ModelAndView(model, "templates/express.vm")
		 	);
    	
    };
    
    public static Route handleCallback = (Request req, Response res) -> {
    	return "";
    };

}
