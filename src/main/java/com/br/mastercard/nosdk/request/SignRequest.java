package com.br.mastercard.nosdk.request;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

public class SignRequest {
	
	private String body;
	private String keyPath;
	private String password;
	private String alias;
	
	public SignRequest(String body, String keyPath, String alias, String password)
	{
		this.body = body;
		this.keyPath = keyPath;
		this.password = password;
		this.alias = alias;
	}
	
	
	private PrivateKey getPrivateKey() throws Exception
	{
		
		FileInputStream fis = new FileInputStream(this.keyPath);
		PrivateKey k = null;
		
		if(fis!=null)
		{
			KeyStore ks = KeyStore.getInstance("PKCS12");
			ks.load(fis, this.password.toCharArray());
			
			k = (PrivateKey) ks.getKey(this.alias, password.toCharArray());
			
		}
		
		fis.close();
		
		return k;
	}
	
    public String sign() throws Exception {
        try 
        {
            Signature signer = Signature.getInstance("SHA256withRSA");
            signer.initSign( getPrivateKey() );

            byte[] text = this.body.getBytes("UTF-8");
            signer.update(text);

            
            //return Util.urlEncode(Util.base64Encode(signer.sign()));
            return Util.base64Encode(signer.sign());
        }
        catch (Exception e) {
            throw e;
        }
        
        
    } 	

	

}
