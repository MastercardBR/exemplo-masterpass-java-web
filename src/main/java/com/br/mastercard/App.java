package com.br.mastercard;


import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

import com.br.mastercard.controller.ExpressCheckoutController;
import com.br.mastercard.controller.StandardCheckoutController;


public class App
{
	
	private static void makeConfigMap()
	{
		
		Config.set("consumerKey", "x9CN7cXABdzeOpjVrAOa7v4pHhpkzLNTDcMEIOY60e2a0e46!10e8bc47714141be983e984732a73a4c0000000000000000");
		Config.set("password", "wDWmmgi3AY0L5qwolJXw");
		Config.set("path", "/sandbox.p12");
		Config.set("checkoutId", "d072ca48e19b49c49bb76ea4513b42d1");
		Config.set("cartId", "21345");
		Config.set("currency", "USD");
		Config.set("amount", "100");
		Config.set("supressShipping", false);
		Config.set("supress3d", true);
		Config.set("acceptedCards", "master,amex,diners,discover,jcb,maestro,visa");
		Config.set("projectdir", System.getProperty("user.dir"));
		
		
	}
	
    public static void main( String[] args ) throws Exception
    {
    	
    	// inicia a configuracao
    	makeConfigMap();
    	
        String staticDir = "/src/main/resources/public/html";
        
        staticFiles.externalLocation(Config.get("projectdir").toString() + staticDir);
        
        get("/standard", StandardCheckoutController.serveStandardPage);
        get("/callback-standard", StandardCheckoutController.handleCallback);
        get("/express", ExpressCheckoutController.serveStandardPage);
        get("/callback-express", ExpressCheckoutController.handleCallback);
  
        enableDebugScreen();
    	
    }
}
