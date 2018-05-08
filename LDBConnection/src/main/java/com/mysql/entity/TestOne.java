package com.mysql.entity;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/4.
 */
@DBDesc(name = "testone")
public class TestOne {
    @DbColum
    private int id;
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
