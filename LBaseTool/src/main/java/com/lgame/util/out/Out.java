/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.out;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * JDK 1.4(JDK 1.5和JDK 1.6也都兼容这种方法)
 * @author leroy
 */
public class Out {

    public static void main(String[] args) {
        String str = out();
        while (!"q".equals(str)) {
            str = out();
        }
    }

    private static String out() {
        String str = readDataFromConsole("Please input string：");
        System.out.println("The information from console：" + str);
        return str;
    }

    /**
     * Use InputStreamReader and System.in to read data from console
     *
     * @param prompt
     *
     * @return input string
     */
    private static String readDataFromConsole(String prompt) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        try {
            System.out.print(prompt);
            str = br.readLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}
