/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.out;

import java.util.Scanner;

/**
 *JDK 1.5（利用Scanner进行读取）
 * @author leroy
 */
public class Out2 {

    public static void main(String[] args) {
        String str = out();
        while (!"q".equals(str)) {
            str = out();
        }

    }

    private static String out() {
        String str = readDataFromConsole("Please input string:");
        System.out.println("The information from console:" + str);
        return str;
    }

    /**
     * Use java.util.Scanner to read data from console
     *
     * @param prompt
     *
     * @return input string
     */
    private static String readDataFromConsole(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
