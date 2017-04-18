package com;

import com.lgame.util.time.DateTimeTool;

import static com.lgame.util.time.DateTimeTool.C_TIME_PATTON_DEFAULT;

/**
 * Created by Administrator on 2017/4/3.
 */
public class PrintOut {
    public static void log(String str){
        System.out.println(DateTimeTool.getCurrentDateStr(C_TIME_PATTON_DEFAULT)+":"+str);
    }
}
