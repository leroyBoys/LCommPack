package com.lgame.util.file;

import java.io.File;

/**
 * Created by Administrator on 2017/5/17.
 */
public class ReadUpdateFile {
    private File file;
    private long lastTimeFileSize;

    public ReadUpdateFile(String filePath){
        file = new File(filePath);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public long getLastTimeFileSize() {
        return lastTimeFileSize;
    }

    public void setLastTimeFileSize(long lastTimeFileSize) {
        this.lastTimeFileSize = lastTimeFileSize;
    }
}
