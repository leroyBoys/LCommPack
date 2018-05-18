package com.pro;

import com.mysql.entity.LQField;
import com.redis.entity.RedisCache;

import java.util.Date;
import java.util.List;

/**
 * 商品类
 */
@RedisCache(keyMethodName = "getS1")
public class Products {
    @LQField(name = "names1")
    private String s1;
    @LQField
    private String s2;
    @LQField
    private String s3;
    @LQField
    private String s4;
    @LQField
    private String s5;
    @LQField
    private String s6;
    @LQField
    private String s7;
    @LQField
    private String s8;
    @LQField
    private String s9;
    @LQField(name = "ndate")
    private Date date;
    private Products products;

    @LQField
    private int i1;
    @LQField
    private int i2;
    @LQField
    private int i3;
    @LQField
    private int i4;
    @LQField
    private int i5;
    @LQField
    private int i6;
    @LQField
    private int i7;
    @LQField
    private int i8;
    @LQField
    private int i9;

    @LQField
    private boolean  b1;
    @LQField
    private boolean  b2;
    @LQField
    private boolean  b3;
    @LQField
    private boolean  b4;
    @LQField
    private boolean  b5;
    @LQField
    private boolean  b6;
    @LQField
    private boolean  b7;
    @LQField
    private boolean  b8;
    @LQField
    private boolean  b9;

    private List<String> list;
    @LQField
    private long l1;
    @LQField
    private long l2;
    @LQField
    private long l3;
    @LQField
    private long l4;
    @LQField
    private long l5;
    @LQField
    private long l6;
    @LQField
    private long l7;
    @LQField
    private long l8;
    private long l9;
    public long getL9() {
        return l9;
    }

    public void setL9(long l9) {
        this.l9 = l9;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String getS3() {
        return s3;
    }

    public void setS3(String s3) {
        this.s3 = s3;
    }

    public String getS4() {
        return s4;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Products getProducts() {
        return products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }

    public void setS4(String s4) {
        this.s4 = s4;
    }

    public String getS5() {
        return s5;
    }

    public void setS5(String s5) {
        this.s5 = s5;
    }

    public long getL1() {
        return l1;
    }

    public void setL1(long l1) {
        this.l1 = l1;
    }

    public long getL2() {
        return l2;
    }

    public void setL2(long l2) {
        this.l2 = l2;
    }

    public long getL3() {
        return l3;
    }

    public void setL3(long l3) {
        this.l3 = l3;
    }

    public long getL4() {
        return l4;
    }

    public void setL4(long l4) {
        this.l4 = l4;
    }

    public long getL5() {
        return l5;
    }

    public void setL5(long l5) {
        this.l5 = l5;
    }

    public long getL6() {
        return l6;
    }

    public void setL6(long l6) {
        this.l6 = l6;
    }

    public long getL7() {
        return l7;
    }

    public void setL7(long l7) {
        this.l7 = l7;
    }

    public long getL8() {
        return l8;
    }

    public void setL8(long l8) {
        this.l8 = l8;
    }

    public String getS6() {
        return s6;
    }

    public void setS6(String s6) {
        this.s6 = s6;
    }

    public String getS7() {
        return s7;
    }

    public void setS7(String s7) {
        this.s7 = s7;
    }

    public String getS8() {
        return s8;
    }

    public void setS8(String s8) {
        this.s8 = s8;
    }

    public String getS9() {
        return s9;
    }

    public void setS9(String s9) {
        this.s9 = s9;
    }

    public int getI1() {
        return i1;
    }

    public void setI1(int i1) {
        this.i1 = i1;
    }

    public int getI2() {
        return i2;
    }

    public void setI2(int i2) {
        this.i2 = i2;
    }

    public int getI3() {
        return i3;
    }

    public void setI3(int i3) {
        this.i3 = i3;
    }

    public int getI4() {
        return i4;
    }

    public void setI4(int i4) {
        this.i4 = i4;
    }

    public int getI5() {
        return i5;
    }

    public void setI5(int i5) {
        this.i5 = i5;
    }

    public int getI6() {
        return i6;
    }

    public void setI6(int i6) {
        this.i6 = i6;
    }

    public int getI7() {
        return i7;
    }

    public void setI7(int i7) {
        this.i7 = i7;
    }

    public int getI8() {
        return i8;
    }

    public void setI8(int i8) {
        this.i8 = i8;
    }

    public int getI9() {
        return i9;
    }

    public void setI9(int i9) {
        this.i9 = i9;
    }

    public boolean isB1() {
        return b1;
    }

    public void setB1(boolean b1) {
        this.b1 = b1;
    }

    public boolean isB2() {
        return b2;
    }

    public void setB2(boolean b2) {
        this.b2 = b2;
    }

    public boolean isB3() {
        return b3;
    }

    public void setB3(boolean b3) {
        this.b3 = b3;
    }

    public boolean isB4() {
        return b4;
    }

    public void setB4(boolean b4) {
        this.b4 = b4;
    }

    public boolean isB5() {
        return b5;
    }

    public void setB5(boolean b5) {
        this.b5 = b5;
    }

    public boolean isB6() {
        return b6;
    }

    public void setB6(boolean b6) {
        this.b6 = b6;
    }

    public boolean isB7() {
        return b7;
    }

    public void setB7(boolean b7) {
        this.b7 = b7;
    }

    public boolean isB8() {
        return b8;
    }

    public void setB8(boolean b8) {
        this.b8 = b8;
    }

    public boolean isB9() {
        return b9;
    }

    public void setB9(boolean b9) {
        this.b9 = b9;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Products(){

    }

    public Products(String s1, String s2, String s3, String s4, String s5,
                    String s6, String s7, String s8, String s9, int i1, int i2, int i3,
                    int i4, int i5, int i6, int i7, int i8, int i9, boolean b1,
                    boolean b2, boolean b3, boolean b4, boolean b5, boolean b6,
                    boolean b7, boolean b8, boolean b9, List<String> list) {
        super();
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.s4 = s4;
        this.s5 = s5;
        this.s6 = s6;
        this.s7 = s7;
        this.s8 = s8;
        this.s9 = s9;
        this.i1 = i1;
        this.i2 = i2;
        this.i3 = i3;
        this.i4 = i4;
        this.i5 = i5;
        this.i6 = i6;
        this.i7 = i7;
        this.i8 = i8;
        this.i9 = i9;
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
        this.b4 = b4;
        this.b5 = b5;
        this.b6 = b6;
        this.b7 = b7;
        this.b8 = b8;
        this.b9 = b9;
        this.list = list;
    }

    @Override
    public String toString() {
        return "Products [s1=" + s1 + ", s2=" + s2 + ", s3=" + s3 + ", s4="
                + s4 + ", s5=" + s5 + ", s6=" + s6 + ", s7=" + s7 + ", s8="
                + s8 + ", s9=" + s9 + ", i1=" + i1 + ", i2=" + i2 + ", i3="
                + i3 + ", i4=" + i4 + ", i5=" + i5 + ", i6=" + i6 + ", i7="
                + i7 + ", i8=" + i8 + ", i9=" + i9 + ", b1=" + b1 + ", b2="
                + b2 + ", b3=" + b3 + ", b4=" + b4 + ", b5=" + b5 + ", b6="
                + b6 + ", b7=" + b7 + ", b8=" + b8 + ", b9=" + b9 + ", list="
                + list + "]";
    }
}