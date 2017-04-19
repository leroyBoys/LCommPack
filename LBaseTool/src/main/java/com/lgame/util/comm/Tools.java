/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.comm;

import com.lgame.util.encry.Endecrypt;
import com.sun.corba.se.impl.legacy.connection.DefaultSocketFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author leroy_boy
 */
public class Tools {

    public static String getCharacterAndNumber(int length) {
        String val = "";

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; // 输出字母还是数字  
            if ("char".equalsIgnoreCase(charOrNum)) // 字符串  
            {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; //取得大写字母还是小写字母  
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) // 数字  
            {
                val += String.valueOf(random.nextInt(10));
            }
        }

        return val;
    }

    public static byte[] getByteJoin(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    /**
     * 过滤特殊字符，只允许中文、字母或数字
     *
     * @param str
     * @return
     */
    public static String trimNoFormalChar(String str) {
        String reg = "[^0-9a-zA-Z\u4e00-\u9fa5]+";
        return str.replaceAll(reg, "");
    }

    private final static String key = "2.：WSJks@12#$)#";

    public static void writeBytes(String path,List<byte[]> byts) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file, true);
        for (int i = 0; i < byts.size(); i++) {
            byte[] oldbytes = Endecrypt.getInstance().get3DESEncrypt(byts.get(i),key);

            int oldLength = oldbytes.length;
            byte[] newCapacity = FormatDataTool.getByteJoin(FormatDataTool.intToByteArray(oldLength),oldbytes);
            out.write(newCapacity);
        }
        out.close();
    }

    public static List<byte[]> readBytes(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }

        List<byte[]> byts = new ArrayList<>(10);
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[1024];
        byte[] oldBuf = null;

        boolean isOver = false;
        while ((fis.read(buf)) != -1) {
            if(oldBuf != null){
                oldBuf = FormatDataTool.getByteJoin(oldBuf,buf);
            }else {
                oldBuf = buf;
            }

         //   System.out.println("===============================================================>");
            while (true){
              //  System.out.println("=======>"+Arrays.toString(oldBuf));
                byte[] temLength = Arrays.copyOf(oldBuf,4);
               // System.out.println("====temLength===>"+Arrays.toString(temLength));
                int length = FormatDataTool.byteArrayToInt(temLength);
             //   System.out.println("====ength===>"+length);
                if(length == 0){
                    isOver = true;
                    break;
                }

                if(oldBuf.length - 4<length){
                    break;
                }

                byte[] data = Arrays.copyOfRange(oldBuf,4,4+length);
                //System.out.println("====data===>"+Arrays.toString(data));
                byts.add(Endecrypt.getInstance().get3DESDecrypt(data,key));

                oldBuf = Arrays.copyOfRange(oldBuf,4+length,oldBuf.length);
              //  System.out.println("====remaim===>"+Arrays.toString(oldBuf));
            }

            if(isOver){
                break;
            }
            buf = new byte[1024];//重新生成，避免和上次读取的数据重复
        }

        return byts;
    }

}
