package com.br.mastercard.controller;

import java.util.HashMap;
import java.util.Map;

import com.br.mastercard.Api;
import com.br.mastercard.Config;
import com.mastercard.merchant.checkout.model.Pairing;
import com.mastercard.merchant.checkout.model.PaymentData;
import com.mastercard.merchant.checkout.model.PreCheckoutData;
import com.mastercard.sdk.core.util.QueryParams;
import com.mastercard.merchant.checkout.model.Card;
import com.mastercard.merchant.checkout.model.ExpressCheckoutRequest;
import com.mastercard.merchant.checkout.ExpressCheckoutApi;
import com.mastercard.merchant.checkout.PairingIdApi;
import com.mastercard.merchant.checkout.PreCheckoutApi;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.velocity.VelocityTemplateEngine;

public class ExpressCheckoutController {
	
	// pagina principal
    public static Route serveStandardPage = (Request req, Response res) -> {
    	
    	Config.set("callback", "http://wallet.labfintech.com.br:4567/callback-express");
    	Config.set("userId", "1234567890");
    	
    	Map<String, Object> model = new HashMap<>();
    	model.put("config", Config.getMap());
		 
		 	return new VelocityTemplateEngine().render(
		        new ModelAndView(model, "templates/express.vm")
		 	);
    	
    };
    
    public static Route handleCallback = (Request req, Response res) -> {
    	
    	Api t = new Api();
    	
    	QueryParams queryParams = new QueryParams()
                .add("pairingTransactionId", req.queryParams("pairing_verifier"))
                .add("userId", Config.get("userId").toString() );
 
    	// recebe o pairing token. Esse token é válido por chamada, e a cada nova, um novo token é gerado.
    	// deve ser guardado em storage seguro
    	Pairing pairingToken = PairingIdApi.show(queryParams);
    	
    	// precheckout trás os dados mascarados. Normalmente usado na tela para confirmar o pagamento, 
    	//e selecionar qual cartão deverá ser usado para o pagamento
    	PreCheckoutData pcd = PreCheckoutApi.show(pairingToken.getPairingId());
    	
    	
    	// expresscheckout trás os dados abertos, uma vez selecionado o cartao no passo anterior
    	// uma vez selecionado o "cardId", o serviço trás os dados que deverão ser passados ao gateway/adquirente
    	ExpressCheckoutRequest ecr = new ExpressCheckoutRequest()
    										.amount( Double.parseDouble(Config.get("amount").toString()))
    										.cardId( pcd.getCards().get(0).getCardId() )
    										.checkoutId( Config.get("checkoutId").toString() )
    										.digitalGoods(true)
    										.currency(Config.get("currency").toString())
    										.pairingId(pcd.getPairingId())
    										.preCheckoutTransactionId( pcd.getPreCheckoutTransactionId() );
    	
    	PaymentData paymentData = ExpressCheckoutApi.create(ecr);
    	
    	// salvar esse token para uso futuro.
    	String pairingTokenId = paymentData.getPairingId();
    	Card c = paymentData.getCard();
    	
    	
    	// executa o postback
    	//t.performPostback(req.queryParams("oauth_verifier"), "12A345", true);
    	t.performPostback(pcd.getPreCheckoutTransactionId(), pcd.getPreCheckoutTransactionId(), "12A345", true);
    	
    	
    	 Map<String, Object> model = new HashMap<>();
		 model.put("card", c);
		 model.put("pairingId", pairingTokenId);
		 
		 return new VelocityTemplateEngine().render(
		        new ModelAndView(model, "templates/card-express.vm")
		 			);
  
    	
    	
    };
    
    public static Route handleNewTransaction = (Request req, Response res) -> {
    	
    	Api t = new Api();
    	
    	// precheckout trás os dados mascarados. Normalmente usado na tela para confirmar o pagamento, 
    	//e selecionar qual cartão deverá ser usado para o pagamento
    	PreCheckoutData pcd = PreCheckoutApi.show(req.queryParams("pairingId"));
    	
    	
    	// expresscheckout trás os dados abertos, uma vez selecionado o cartao no passo anterior
    	// uma vez selecionado o "cardId", o serviço trás os dados que deverão ser passados ao gateway/adquirente
    	ExpressCheckoutRequest ecr = new ExpressCheckoutRequest()
    										.amount( Double.parseDouble(Config.get("amount").toString()))
    										.cardId( pcd.getCards().get(0).getCardId() )
    										.checkoutId( Config.get("checkoutId").toString() )
    										.digitalGoods(true)
    										.currency(Config.get("currency").toString())
    										.pairingId(pcd.getPairingId())
    										.preCheckoutTransactionId( pcd.getPreCheckoutTransactionId() );
    	
    	PaymentData paymentData = ExpressCheckoutApi.create(ecr);
    	
    	// salvar esse token para uso futuro.
    	String pairingTokenId = paymentData.getPairingId();
    	Card c = paymentData.getCard();
    	
    	
    	// executa o postback
    	t.performPostback("abc123", pcd.getPreCheckoutTransactionId(), "12A345", true);
    	 
    	
    	
    	 Map<String, Object> model = new HashMap<>();
		 model.put("card", c);
		 model.put("pairingId", pairingTokenId);
		 
		 return new VelocityTemplateEngine().render(
		        new ModelAndView(model, "templates/card-express.vm")
		 			);
    	
    	
    };
    

}
