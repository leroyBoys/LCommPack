package com.lsocket.util;

import com.logger.log.SystemLogger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

/**
 * 默认传输包协议为：1byte或者2byte,3byte,5byte
 * \-PackageType(1byte)-\-数据长度(1-2byte)-\-module(1byte不能大于256)-\-cmd(1byte不能大于256)-\
 * PackageType(1byte)解析如下
 * 1位：是否是心跳；2位：加密方式（0-4）;
 * 1位：是否包含cmd和module;1位：是否是大数据（如果是大数据则长度展2字节，否则1字节）；3位：class类型（0-8）
 * 数据长度最大值为256*?+?=max=65792( 最大包支持64K)
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class DefaultSocketPackage {
    private final static int compresseMinLength = 200;

    /**
     * 反解析
     * @param buffer
     * @return
     */
    public static void detransMsg(IoBuffer buffer) {
        try {
            System.out.println("====================================>>>>>>>>>>>>");
            byte header = buffer.get();
            if((header&1) == 1){
                System.out.println("===>是心跳");
                return;
            }

            int encryType = header>>PackageType.encryType&3;
            System.out.println("compreType:"+encryType);
            int isContainCmdModule = header>>PackageType.cmdModuleContain&1;
            System.out.println("isContainCmdModule:"+isContainCmdModule);
            int isBiggerData = header>>PackageType.isBiggerData&1;
            System.out.println("isBiggerData:"+isBiggerData);
            int classType = header>>PackageType.classType&0xf;
            System.out.println("classType:"+classType);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static IoBuffer transformHeartMsg() {
        try {
            IoBuffer buffer = IoBuffer.allocate(1);
            buffer.put((byte) 1);
            buffer.flip();
            buffer.free();
            return buffer;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static IoBuffer transformErrorMsg(byte[] bytes,int encryType) {
        try {
           final int messageLength = bytes.length;
            IoBuffer buffer;
            if(messageLength>'\uffff'){
                byte packageType = (byte) (0|(encryType<<PackageType.encryType)|0|1<<PackageType.isBiggerData|(1<<PackageType.classType));
                buffer = IoBuffer.allocate(messageLength + 3);
                buffer.put(packageType);
                buffer.putShort((short) messageLength);
            }else {
                byte packageType = (byte) (0|(encryType<<PackageType.encryType)|0|0|(1<<PackageType.classType));
                buffer = IoBuffer.allocate(messageLength + 2);
                buffer.put(packageType);
                buffer.put((byte) messageLength);
            }
            buffer.put(bytes);
            buffer.flip();
            buffer.free();
            return buffer;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static IoBuffer transformByteArray(byte[] bytes,int encryType,byte module,byte cmd) {
        try {
            final int messageLength = bytes.length;

            IoBuffer buffer;
            if(messageLength>'\uffff'){
                byte packageType = (byte) (0|(encryType<<PackageType.encryType)|1<<PackageType.cmdModuleContain|1<<PackageType.isBiggerData|0);
                buffer = IoBuffer.allocate(messageLength + 5);
                buffer.put(packageType);
                buffer.putShort((short) messageLength);
            }else {
                byte packageType = (byte) (0|(encryType<<PackageType.encryType)|1<<PackageType.cmdModuleContain|0|0);
                buffer = IoBuffer.allocate(messageLength + 4);
                buffer.put(packageType);
                buffer.put((byte) messageLength);
            }

            buffer.put(module);
            buffer.put(cmd);
            buffer.put(bytes);
            buffer.flip();
            buffer.free();
            return buffer;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * @param input
     * @param session
     * @return
     */
    public static ReceiveData getContent(IoBuffer input, IoSession session) {
        int remainSize = input.remaining();
        ReceiveData receiveData = new ReceiveData();
        receiveData.setNextType(ReceiveData.NextType.Next);
        if (remainSize > 0) {
            input.mark();// 标记当前位置，以便reset
            byte header = input.get();
            if((header&1) == 1){
                if (input.hasRemaining()) {
                    //继续循环
                    receiveData.setNextType(ReceiveData.NextType.Continue);
                }
                receiveData.setHeart(true);
                return receiveData;
            }

            if (input.remaining() <= 4) {
                input.reset();//进入下一次接收
                return receiveData;
            }

            int compreType = header>>PackageType.encryType&3;
            if(compreType > 3 || compreType < 0){
                receiveData.close();
                SystemLogger.error(DefaultSocketPackage.class,"compreType:"+compreType+" not right"+ session.toString());
                return receiveData;
            }

            receiveData.setCompreType(compreType);
            final int isContainCmdModule = header>>PackageType.cmdModuleContain&1;
            //System.out.println("isContainCmdModule:"+isContainCmdModule);
            final int isBiggerData = header>>PackageType.isBiggerData&1;
            //System.out.println("isBiggerData:"+isBiggerData);
            final int classType = header>>PackageType.classType&0xf;
            if(classType > 8 || classType < 0){
                receiveData.close();
                SystemLogger.error(DefaultSocketPackage.class,"classType:"+classType+" not right"+ session.toString());
                return receiveData;
            }
            //System.out.println("classType:"+classType);
            receiveData.setClassType(classType);

            short size;
            if(isBiggerData == 1){
                size  = input.getShort();
            }else {
                size = input.get();
            }

            if(size <= 0){
                receiveData.close();
                SystemLogger.error(DefaultSocketPackage.class,"length:"+size+" not right"+ session.toString());
                return receiveData;
            }

            if(isContainCmdModule-1 == 0){
                receiveData.setModule(input.get());
                receiveData.setCmd(input.get());
            }

            if (size > input.remaining()) {// 如果消息内容不够，则重置，相当于不读取size
                input.reset();
            } else {
                byte[] b = new byte[size];
                input.get(b);
                receiveData.setData(b);
                if (input.hasRemaining()) {
                    //继续循环
                    receiveData.setNextType(ReceiveData.NextType.Continue);
                }
            }

        }
        return receiveData;
    }





    final class PackageType{

        private static final int heart = 0;
        /** 加密方式encry */
        private static final int encryType = 1+heart;
        private static final int cmdModuleContain = 2+encryType;
        private static final int isBiggerData = 1+cmdModuleContain;
        private static final int classType = 1+isBiggerData;
    }

    final class ClassType{
        private static final int normal = 0;
        private static final int error = 0;

    }
}
