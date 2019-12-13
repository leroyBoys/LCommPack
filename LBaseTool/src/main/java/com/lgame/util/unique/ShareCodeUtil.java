/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.unique;

import java.util.Random;

/**
 * 邀请码生成器，算法原理：<br/>
 * 1) 获取id: 1127738 <br/>
 * 2) 使用自定义进制转为：gpm6 <br/>
 * 3) 转为字符串，并在后面加'o'字符：gpm6o <br/>
 * 4）在后面随机产生若干个随机数字字符：gpm6o7 <br/>
 * 转为自定义进制后就不会出现o这个字符，然后在后面加个'o'，这样就能确定唯一性。最后在后面产生一些随机字符进行补全。<br/>
 *
 * @author jiayu.qiu
 */
public class ShareCodeUtil {

    /**
     * 自定义进制(0,1没有加入,容易与o,l混淆)
     */
    private static final char[] r = new char[]{'q', 'w', 'e', '8', 'a', 's', '2', 'd', 'z', 'x', '9', 'c', '7', 'p', '5', 'i', 'k', '3', 'm', 'j', 'u', 'f', 'r', '4', 'v', 'y', 'l', 't', 'n', '6', 'b', 'g', 'h', 'A', 'B', 'C', 'D', 'E', 'F', 'J', 'H', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'W', 'Y', 'Z', 'G', 'V', '&', '@', 'X'};

    /**
     * (不能与自定义进制有重复)
     */
    private static final char b = 'o';

    /**
     * 进制长度
     */
    private static final int binLen = r.length;

    /**
     * 序列最小长度
     */
    private static final int s = 8;

    static {
        System.out.println("======进制：" + r.length);
    }

    /**
     * 根据ID生成8位随机码
     *
     * @param id ID
     * @return 随机码
     */
    public static String toSerialCode(long id) {
        int system = binLen;
        long num = 0;
        if (id < 0) {
            num = ((long) 2 * 0x7fffffff) + id + 2;
        } else {
            num = id;
        }
        char[] buf = new char[32];
        int charPos = 32;
        while ((num / system) > 0) {
            buf[--charPos] = r[(int) (num % system)];
            num /= system;
        }
        buf[--charPos] = r[(int) (num % system)];
        String ret = new String(buf, charPos, (32 - charPos));
        if (ret.length() < s) {
            StringBuilder sb = new StringBuilder();
            Random rnd = new Random();
            for (int i = 1; i < s - ret.length(); i++) {
                sb.append(r[rnd.nextInt(binLen)]);
            }
            sb.append(b);
            ret = sb.toString() + ret;
        }

        return ret;
    }

    public static long codeToId(String s) {
        int system = binLen;
        char[] buf = new char[s.length()];
        s.getChars(0, s.length(), buf, 0);
        long num = 0;
        for (int i = 0; i < buf.length; i++) {
            if (buf[i] == b) {
                num = 0;
                continue;
            }
            for (int j = 0; j < r.length; j++) {
                if (r[j] == buf[i]) {
                    num += j * Math.pow(system, buf.length - i - 1);
                    break;
                }
            }
        }
        return num;
    }

    public static void main(String[] ar){
        String abc = "abcdef";
        System.out.println(abc.substring(0,2));
        System.out.println(abc.substring(2,4));
        System.out.println(ShareCodeUtil.toSerialCode(Long.MAX_VALUE));
    }
}
