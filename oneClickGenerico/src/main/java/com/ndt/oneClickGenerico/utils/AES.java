package com.ndt.oneClickGenerico.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter; 

import com.sun.jersey.core.util.Base64;

public class AES {
	
	private static final String key = "BoToNes_NdT_2018";
	private static final String initVector = "BoToNes_NdT_2018";
 
	public static String encrypt(String value) {
		try {
			int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
			System.out.println("MaxAllowedKeyLength=[" + maxKeyLen + "].");
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			return DatatypeConverter.printBase64Binary(encrypted);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String decrypt(String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.decode(encrypted));

			return new String(original);
		} catch (Exception ex) {
			System.out.println("Error en AES");
			ex.printStackTrace();
		}

		return null;
	}
}