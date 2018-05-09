package com.mysql.entity;

import java.util.Date;
import java.util.List;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/2.
 */
@DBDesc(name="test_data")
public class TestData {
    @DbColum(name = "id",isPrimaryKey = true)
    private int id;
    @DbColum()
    private String num;
    @DbColum()
    private String date;
    @DbColum()
    private String name;
    @DbColum()
    private boolean isGood;

    @DBRelations(relation = DBRelations.Reltaion.OneToMany,
            map={
                @DBRelation(colum = "id",targetColum = "id"),
                @DBRelation(colum = "tname",targetColum = "name")
            })
    private List<Test1> test1;

    @DBRelations(relation = DBRelations.Reltaion.OneToOne,
            map={
                    @DBRelation(colum = "id",targetColum = "id"),
                    @DBRelation(colum = "toname",targetColum = "name")
            })
    private TestOne testOne;

    @DbColum()
    private Boolean visid;
    @DbColum()
    private Double mydouble;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isGood() {
        return isGood;
    }

    public void setGood(boolean good) {
        isGood = good;
    }

    public Boolean getVisid() {
        return visid;
    }

    public List<Test1> getTest1() {
        return test1;
    }

    public void setTest1(List<Test1> test1) {
        this.test1 = test1;
    }

    public TestOne getTestOne() {
        return testOne;
    }

    public void setTestOne(TestOne testOne) {
        this.testOne = testOne;
    }

    public void setVisid(Boolean visid) {
        this.visid = visid;
    }

    public Double getMydouble() {
        return mydouble;
    }

    public void setMydouble(Double mydouble) {
        this.mydouble = mydouble;
    }
}
