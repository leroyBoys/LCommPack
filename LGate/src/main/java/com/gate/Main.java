package com.gate;

import com.gate.socket.Lgate;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class Main {
    public static void main(String[] args){
        try {
            Lgate.getIntance().start(1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
