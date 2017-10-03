package com.br.mastercard.controller;

import static spark.Spark.get;

import java.util.HashMap;
import java.util.Map;

import com.br.mastercard.Config;
import com.br.mastercard.Api;
import com.mastercard.merchant.checkout.model.Card;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;

public class StandardCheckoutController {
	
	
	// pagina principal
    public static Route serveStandardPage = (Request req, Response res) -> {
    	
    	Config.set("callback", "http://wallet.labfintech.com.br:4567/callback-standard");
    	
    	Map<String, Object> model = new HashMap<>();
    	model.put("config", Config.getMap());
		 
		 	return new VelocityTemplateEngine().render(
		        new ModelAndView(model, "templates/index.vm")
		 	);
    	
    };
    
    
    public static Route handleCallback = (Request req, Response res) -> {
    	
    	Api t = new Api();
 
		 Card c =  t.getCard(req.queryParams("oauth_verifier"));
		 
		 
		 /*
		  * Ao chegar aqui, deve executar alguns passos
		  * 1- Ir no gateway, adquirente e executar o fluxo normal da transacao, 
		  * ja que possui acesso ao getAccountNumber() (pan), getExpiryMonth (vcto) e getExpiryYear(), além de endereço, nome e outros dados pessoais
		  * 
		  * 2- Executar o postback, que faz com que os servidores da masterpass registrem o resultado da transacao
		  */
		 
		 
		 t.performPostback(req.queryParams("oauth_verifier"), "12A345", true);
		 
		 
		 // ao finalizar, se positivo ou negativo, deve executar o postback.
		 // analisar o método e editar conforme os resultados
		 //t.performPostback(req.queryParams("oauth_verifier"));
		 
		 Map<String, Object> model = new HashMap<>();
		 model.put("data", c);
		 
		 
		 	return new VelocityTemplateEngine().render(
		        new ModelAndView(model, "templates/card.vm")
		 );
		 
    };

}
