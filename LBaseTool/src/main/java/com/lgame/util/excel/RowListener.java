package com.lgame.util.excel;

/**
 * Created by Administrator on 2018/4/17.
 */
public interface RowListener {
    /**
     *
     * @param row
     * @param rowNum 行号
     */
    void read(String[] row,int rowNum);
}
