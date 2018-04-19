package com.lgame.util.excel;

/**
 * Created by Administrator on 2018/4/17.
 */
public interface RowListener {
    /**
     * 返回是否继续解析
     * @param row
     * @param rowNum 行号
     */
    boolean read(String[] row,int rowNum);

    /**
     * 结束
     * @param rowNum
     */
    void overDocument(int rowNum);
}
