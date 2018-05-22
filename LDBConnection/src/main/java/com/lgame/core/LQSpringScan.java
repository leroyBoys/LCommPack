package com.lgame.core;

import com.lgame.mysql.compiler.ScanEntitysTool;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/22.
 */
public class LQSpringScan {
    public LQSpringScan(String packStr) throws Exception {
        ScanEntitysTool.instance(packStr.split(","));
    }
}
