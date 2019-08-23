/**
 * MD5.java
 * payFront
 * Oct 7, 20114:11:13 PM
 */
package com.yufan.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public  class MD5 {
	
	public static String enCodeStandard(String password ) {
		StringBuffer buf = new StringBuffer("");
		try { 
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes("UTF-8")); 
			byte b[] = md.digest(); 
			int i; 
			
			for (int offset = 0; offset < b.length; offset++) { 
				i = b[offset]; 
				if(i<0) i+= 256; 
				if(i<16) buf.append("0"); 
				buf.append(Integer.toHexString(i));
			} 
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); 
			return null;
		}catch(Exception e){
			e.printStackTrace(); 
			return null;
		} 
		return buf.toString();
	} 
	
	public static byte[] sha512(String text) {
		try { 
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
			messageDigest.update(text.getBytes("UTF-8"));
			byte[] bytes = messageDigest.digest();
			return bytes;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); 
		}catch(Exception e){
			e.printStackTrace(); 
		} 
		return null;
	}

    public static String enCodeStandard2(String password ) {
        StringBuilder buf = new StringBuilder("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes("UTF-8"));
            byte b[] = md.digest();
            int i;

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if(i<0) i+= 256;
                if(i<16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return buf.toString();
    }

    public static String sha1(String text) {
        StringBuilder buf = new StringBuilder("");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes("UTF-8"));
            byte b[] = md.digest();
            int i;

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if(i<0) i+= 256;
                if(i<16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return buf.toString();
    }

    public static void main(String[] args) {
        System.out.println(MD5.enCodeStandard("1"));
    }

}
