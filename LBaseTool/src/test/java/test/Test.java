/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import com.lgame.util.comm.MemDisk;
import com.lgame.util.comm.StringTool;
import com.lgame.util.http.HttpTool;

import java.io.File;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author leroy_boy
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        while (new File("D:\\info.txt").exists()){
            MemDisk.getMemLastInfo();

            Thread.sleep(300);
        }
    }
}
