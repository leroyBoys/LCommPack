package com.lgame.util.excel;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */
public class RetExcelData {
    private boolean isRepeate;//是否需要重新导入；如果失败行数超过50行则直接需要重新导入
    private int faileCount;
    private List data;

    public boolean isRepeate() {
        return isRepeate;
    }

    public void setRepeate(boolean repeate) {
        isRepeate = repeate;
    }

    public int getFaileCount() {
        return faileCount;
    }

    public void setFaileCount(int faileCount) {
        this.faileCount = faileCount;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }
}
