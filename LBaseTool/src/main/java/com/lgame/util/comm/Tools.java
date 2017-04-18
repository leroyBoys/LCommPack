/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.comm;

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

    public static void outBytes(byte[] aa) {
        if (aa == null || aa.length == 0) {
            System.out.println("==空");
            return;
        }
        System.out.println("==字节流开始");
        for (byte a : aa) {
            System.out.println(a);
        }
         System.out.println("==字节流结束");
    }
}
