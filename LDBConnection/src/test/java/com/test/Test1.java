package com.test;

import com.mysql.entity.DBDesc;
import com.mysql.entity.DbColum;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/4.
 */
@DBDesc(name = "test1")
public class Test1 {
    @DbColum
    private int id;
    private int TestData;
    @DbColum
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}