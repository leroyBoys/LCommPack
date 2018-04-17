package com.lgame.util.excel;

import java.util.Arrays;

/**
 * Created by Administrator on 2018/4/17.
 */
public class DefaultRowListener implements RowListener{

    @Override
    public void read(String[] row,int rowNum) {
        System.out.println("rowNum:"+rowNum+"   :"+Arrays.toString(row));
    }
}
