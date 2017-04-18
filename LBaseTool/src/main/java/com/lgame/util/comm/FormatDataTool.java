package com.lgame.util.comm;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author leroy_boy
 */
public class FormatDataTool {

    public static int toInt(Object num) {
        if (num == null) {
            return 0;
        }
        try {
            Integer n = (Integer) num;
            return n;
        } catch (Exception ex) {
            System.out.println("FormatDataTool:exception:toInt" + num);
        }
        return 0;
    }
    
    public static byte[] getByteJoin(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }
}
