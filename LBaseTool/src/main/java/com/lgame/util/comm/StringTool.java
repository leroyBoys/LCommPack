/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.comm;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 *
 * @author leroy_boy
 */
public class StringTool {

    public final static String SIGN1 = "_";
    /**
     * ；
     */
    public final static String SIGN2 = ";";
    /**
     * ：
     */
    public final static String SIGN3 = ":";
    public final static String SIGN4 = ",";
    public final static String SIGN5 = "@";
    public final static String SIGN6 = "-";
    public final static String SIGN7 = "/";
    public final static String SIGN8 = "\\|";
    public final static String SIGN9 = "\\.";

    /**
     * 验证str是否为null或者""
     *
     * @param str
     * @return
     */
    public static boolean isNotNull(String str) {
        if (str != null && !"".equals(str) && !str.equals("null")) {
            return true;
        }
        return false;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 二进制转字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    /**
     * 字符串转二进制
     *
     * @param str
     * @return
     */
    public static byte[] hex2byte(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        int len = str.length();
        if (len == 0 || len % 2 == 1) {
            return null;
        }

        byte[] b = new byte[len / 2];
        try {
            for (int i = 0; i < str.length(); i += 2) {
                b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
            }
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 截取特定字符第preNum个之前的字符串
     *
     * @param str 源字符串
     * @param tag 特定字符
     * @param preNum 第几个
     * @return 返回截取以后的字符串
     */
    public static String subPreString(String str, String tag, int preNum) {
        if (str == null) {
            return "";
        }
        int i = 0;
        int s = 0;
        while (i++ < preNum) {
            s = str.indexOf(tag, s + 1);
            if (s == -1) {
                return str;
            }
        }
        return str.substring(0, s);
    }

    public static String Format(String s, Object[] p) {
        int n = (p == null) ? 0 : p.length;
        int i = 0;
        String t = "{" + i + "}";
        int j = s.indexOf(t);
        while (j >= 0) {
            if (i < n) {
                s = s.replace(t, p[i].toString());
            }
            ++i;
            t = "{" + i + "}";
            j = s.indexOf(t);
        }
        return s;
    }

    public static String trimToNull(Object attribute) {
        if (attribute == null) {
            return null;
        }
        String str = attribute.toString();
        if (isEmpty(str)) {
            return null;
        }
        return str;
    }

    public static String getStringFromCollection(Collection<String> strings) {
        StringBuilder sb = new StringBuilder();
        for(String str:strings){
            sb.append("'").append(str).append("'");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
