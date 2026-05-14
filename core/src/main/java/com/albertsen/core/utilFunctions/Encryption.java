package com.albertsen.core.utilFunctions;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.GCMParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.InvalidKeyException;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.KeySpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
  NOTE: to be able to decrypt an encrypted string you must use the same
  initialization vector (IV) Therefore it must be sent along with other
  metadata. It can be reconstructed using the bytes that it uses along
  with the tag length both of which can be extracted like this:
  GCMParameterSpec gcm = EncryptionInstance.getGCM();
  byte[] byteArray = gcm.getIV();
  int tlen= gcm.getTLen();
*/

public class Encryption
{
    private GCMParameterSpec gcmParameterSpec;

    public Encryption(GCMParameterSpec iv)	{this.gcmParameterSpec = iv;}
    public Encryption()				{this.gcmParameterSpec = generateIv();}
    public GCMParameterSpec getGCM()		{return gcmParameterSpec;}

    
    public static void main(String[] args) throws Exception
    {
	Encryption en = new Encryption();
	String initial_text = "some text to be encrypted";
	String algorithm = "AES/GCM/NoPadding";	    

	KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
	keyGenerator.init(128);
	SecretKey key = keyGenerator.generateKey();

	String encrypted = en.encryptString(initial_text, key);
	System.out.println(encrypted);

	GCMParameterSpec gcm = en.getGCM();
	byte[] byteArray = gcm.getIV();
	int tlen= gcm.getTLen();
	System.out.println(tlen);
	for (byte b : byteArray) {
	    System.out.printf("%02x ", b);
	}
	System.out.printf("\n");

	GCMParameterSpec gcm2 = new GCMParameterSpec(tlen, byteArray); /* reconstruct gcm from byte array and tag length*/
	Encryption de = new Encryption(gcm2);
	String decrypted = de.decryptString(encrypted, key);
	System.out.println("decryption success: " + (initial_text.equals(decrypted)));
    }
    
    public  GCMParameterSpec generateIv()
    {
	byte[] iv = new byte[12];
	new SecureRandom().nextBytes(iv);
	return new GCMParameterSpec(128, iv);
    }

    
    public  String encryptString(String message, SecretKey key)
    {
	try{
	    String algorithm = "AES/GCM/NoPadding";	    
	    Cipher cipher = Cipher.getInstance(algorithm);
	    cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
	    byte[] cipherText = cipher.doFinal(message.getBytes());
	    return Base64.getEncoder().encodeToString(cipherText);
	} catch (NoSuchAlgorithmException e){
	    System.out.println(e);
	} catch (NoSuchPaddingException e){
	    System.out.println(e);
	} catch (IllegalBlockSizeException e){
	    System.out.println(e);
	}catch(InvalidKeyException e){
	    System.out.println(e);
	}catch(InvalidAlgorithmParameterException e){
	    System.out.println(e);
	}catch(BadPaddingException e){
	    System.out.println(e);
	}
	return "";
    }
    
    public  String decryptString(String message, SecretKey key)
    {
	try{
	    String algorithm = "AES/GCM/NoPadding";	    
	    Cipher cipher = Cipher.getInstance(algorithm);
	    
	  
	    cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
	    byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(message));
	    return new String(plainText);
	} catch (NoSuchAlgorithmException e){
	    System.out.println(e);
	} catch (NoSuchPaddingException e){
	    System.out.println(e);
	} catch (IllegalBlockSizeException e){
	    System.out.println(e);
	}catch(InvalidKeyException e){
	    System.out.println(e);
	}catch(InvalidAlgorithmParameterException e){
	    System.out.println(e);
	}catch(BadPaddingException e){
	    System.out.println(e);
	}
	return "";
    }

    public  String decryptFile(String message, SecretKey key, File inputFile, File outputFile)
    {
	try{
	    String algorithm = "AES/GCM/NoPadding";	    
	    Cipher cipher = Cipher.getInstance(algorithm);
	    
	    cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
	    FileInputStream inputStream = new FileInputStream(inputFile);
	    FileOutputStream outputStream = new FileOutputStream(outputFile);
	    byte[] buffer = new byte[64];
	    int bytesRead;
	    while ((bytesRead = inputStream.read(buffer)) != -1) {
		byte[] output = cipher.update(buffer, 0, bytesRead);
		if (output != null) {
		    outputStream.write(output);
		}
	    }
	    byte[] outputBytes = cipher.doFinal();
	    if (outputBytes != null) {
		outputStream.write(outputBytes);
	    }
	    inputStream.close();
	    outputStream.close();

	} catch (NoSuchAlgorithmException e){
	    System.out.println(e);
	} catch (NoSuchPaddingException e){
	    System.out.println(e);
	} catch (IllegalBlockSizeException e){
	    System.out.println(e);
	}catch(InvalidKeyException e){
	    System.out.println(e);
	}catch(InvalidAlgorithmParameterException e){
	    System.out.println(e);
	}catch(BadPaddingException e){
	    System.out.println(e);
	}catch(FileNotFoundException e){
	    System.out.println(e);
	}catch(IOException e){
	    System.out.println(e);
	}
	return "";
    }
    public  String encryptFile(String message, SecretKey key, File inputFile, File outputFile)
    {
	try{
	    String algorithm = "AES/GCM/NoPadding";	    
	    Cipher cipher = Cipher.getInstance(algorithm);
	    
	    cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
	    FileInputStream inputStream = new FileInputStream(inputFile);
	    FileOutputStream outputStream = new FileOutputStream(outputFile);
	    byte[] buffer = new byte[64];
	    int bytesRead;
	    while ((bytesRead = inputStream.read(buffer)) != -1) {
		byte[] output = cipher.update(buffer, 0, bytesRead);
		if (output != null) {
		    outputStream.write(output);
		}
	    }
	    byte[] outputBytes = cipher.doFinal();
	    if (outputBytes != null) {
		outputStream.write(outputBytes);
	    }
	    inputStream.close();
	    outputStream.close();

	} catch (NoSuchAlgorithmException e){
	    System.out.println(e);
	} catch (NoSuchPaddingException e){
	    System.out.println(e);
	} catch (IllegalBlockSizeException e){
	    System.out.println(e);
	}catch(InvalidKeyException e){
	    System.out.println(e);
	}catch(InvalidAlgorithmParameterException e){
	    System.out.println(e);
	}catch(BadPaddingException e){
	    System.out.println(e);
	}catch(FileNotFoundException e){
	    System.out.println(e);
	}catch(IOException e){
	    System.out.println(e);
	}
	return "";
    }
    
}
