package com.lgame.util.encry;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by Administrator on 2017/4/19.
 */
public class DesTool {

    //解密数据
    public static byte[] decrypt(byte[] bytesrc,String key) {
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));

            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

            return cipher.doFinal(bytesrc);
        }catch (Exception e){
            return null;
        }

    }

    public static byte[] encrypt(byte[] byts, String key){
        try {
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

            DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
            IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            return cipher.doFinal(byts);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static byte[] convertHexString(String ss)
    {
        byte digest[] = new byte[ss.length() / 2];
        for(int i = 0; i < digest.length; i++)
        {
            String byteString = ss.substring(2 * i, 2 * i + 2);
            int byteValue = Integer.parseInt(byteString, 16);
            digest[i] = (byte)byteValue;
        }

        return digest;
    }


    public static void main(String[] args) throws Exception {

    }


    public static String toHexString(byte b[]) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String plainText = Integer.toHexString(0xff & b[i]);
            if (plainText.length() < 2)
                plainText = "0" + plainText;
            hexString.append(plainText);
        }

        return hexString.toString();
    }

}
