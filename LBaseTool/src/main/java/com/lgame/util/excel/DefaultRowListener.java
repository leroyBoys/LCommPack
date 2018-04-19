package com.lgame.util.excel;

import java.util.Arrays;

/**
 * Created by Administrator on 2018/4/17.
 */
public class DefaultRowListener implements RowListener{

    @Override
    public boolean read(String[] row,int rowNum) {
        System.out.println("rowNum:"+rowNum+"   :"+Arrays.toString(row));
        return true;
    }

    @Override
    public void overDocument(int rowNum) {
        System.out.println("over");
    }
}
