package com.lgame.util.poi.interfac;

import com.lgame.util.poi.module.DefaultRowListener;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/20.
 */
public abstract class PoiReader {
    public void read(String fileName) {
        read(fileName,new DefaultRowListener(),0);
    }

    public void read(String fileName, RowListener listener) {
        read(fileName,listener,0);
    }

    public boolean read(String fileName, RowListener listener, int endLineNum) {
        return read(fileName,null,listener,endLineNum,0);
    }

    public boolean read(String fileName, String sheetName, RowListener listener, int endLineNum) {
        return read(fileName,sheetName,listener,endLineNum,0);
    }

    /**
     *
     * @param fileName
     * @param listener
     * @param endLineNum 最大读取行号，如果0则表示自动
     * @param maxColumNum 最大读取列数，如果0则表示自动
     */
    public abstract boolean read(String fileName, String sheetName, RowListener listener, int endLineNum, int maxColumNum);

    public abstract boolean read(String fileName, int sheetIdex, RowListener listener, int endLineNum, int maxColumNum);
}
