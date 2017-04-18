package com.lgame.util.encry;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 将一个字符串按照zip方式压缩和解压缩
 *
 * @author Administrator
 */
public class ZipTool {

    /**
     * 压缩
     *
     * @param str 压缩数据
     * @return 压缩后的二进制
     * @throws Exception
     */
    public static byte[] compress(String str) throws Exception {
        if (str == null || str.length() == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toByteArray();
    }

    public static byte[] compressBytes(byte[] b) throws IOException  {
        if (b == null || b.length == 0) {
            return new byte[]{};
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(b);
        gzip.close();
        return out.toByteArray();
    }

    /**
     * 解压缩
     *
     * @param str 原二进制压缩数据
     * @return 解压后的字符串
     * @throws Exception
     */
    public static String uncompress(byte[] str) throws Exception {
        if (str == null || str.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str);
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
        return out.toString();
    }

    public static byte[] uncompressBytes(byte[] str) throws IOException  {
        if (str == null || str.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(str);
        GZIPInputStream gunzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = gunzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }
        // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
        return out.toByteArray();
    }
    // 测试方法
    public static void main(String[] args) throws Exception {

        String d = "http://localhost:8080/test/test?b={%22commandName%22:%22LoginI%22,%22username%22:%22test%22";
        System.out.println("com->"+ ZipTool.compress(d).length);
        System.out.println("uncom->"+ ZipTool.uncompress(ZipTool.compress(d)));

        System.out.println("===>");
        byte[] bytes = new byte[1024];
        for(int i =0;i<bytes.length;i++){
            bytes[i] = (byte) (i%120);
            System.out.print(" "+bytes[i]);
        }
        bytes = compressBytes(bytes);
        System.out.println("=");
        System.out.println("=======>"+bytes.length);
        bytes = uncompressBytes(bytes);
        System.out.println("=======2>"+bytes.length);

        for(int i =0;i<bytes.length;i++){
            System.out.print(" "+bytes[i]);
        }

    }
}
