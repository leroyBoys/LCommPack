package com.lsocket.util;

import com.logger.log.SystemLogger;
import org.apache.mina.core.session.IoSession;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/7.
 */
public class ReceiveData {
    private int type;
    private byte[] data;//接收的数据
    private NextType nextType;
    private boolean isHeart = false;
    private boolean isClose = false;//
    private byte cmd;
    private byte module;
    private int classType;
    private int compreType;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setData(byte data) {
        this.data = new byte[1];
        this.data[0] = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public NextType getNextType() {
        return nextType;
    }

    public void setNextType(NextType nextType) {
        this.nextType = nextType;
    }

    public boolean isHeart() {
        return isHeart;
    }

    public void setHeart(boolean heart) {
        isHeart = heart;
    }

    public boolean isClose() {
        return isClose;
    }

    public void close() {
        isClose = true;
    }

    public byte getCmd() {
        return cmd;
    }

    public void setCmd(byte cmd) {
        this.cmd = cmd;
    }

    public byte getModule() {
        return module;
    }

    public void setModule(byte module) {
        this.module = module;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    public int getCompreType() {
        return compreType;
    }

    public void setCompreType(int compreType) {
        this.compreType = compreType;
    }

    public static enum NextType {
        /**
         * 继续遍历(继续重读)
         */
        Continue,
        /**
         * 接收下一个包
         */
        Next;

    }
}
