package hello;

import java.io.InputStream;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class Confidentiality {
	
	static String passphrase = "passphrase";
	static String salt = "1234";
	static String iv = "5b002fa89bb4dd63cde14badfcb257d5";
	static int size = 128;
	static int iteration = 2;
	
	public static String Decrypt(InputStream i) {
		String formData = "";
		try{
			Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	        KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), Hex.decodeHex(salt.toCharArray()), iteration, size);
	        SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	
	        String cipherText = InputStreamToString.getStringFromInputStream(i);
	        
	        System.out.printf("cipherText: %s\n", cipherText);
	        
	        aesCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Hex.decodeHex(iv.toCharArray())));
	        byte[] decrypted = aesCipher.doFinal(Base64.getDecoder().decode(cipherText));
	        
	        formData = new String(decrypted, "UTF-8");
	        
	        System.out.printf("decrepted: %s\n", formData);
		
		}catch (Exception e){
		    e.printStackTrace();
		}
		return formData;
	}
}