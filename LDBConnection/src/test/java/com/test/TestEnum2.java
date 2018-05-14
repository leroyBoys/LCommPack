package com.test;

import com.mysql.entity.DBEnum;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/14.
 */
public enum TestEnum2 implements DBEnum<Integer>{
    Hell(1),More(2);
    private int i;
    TestEnum2(int i ){
        this.i = i;
    }

    public int getI() {
        return i;
    }

    @Override
    public Integer getDBValue() {
        return i;
    }
}
