package com.lsocket.manager;

import com.lsocket.handler.CmdModule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/7.
 */
public class CMDManager {
    private final static CMDManager cmdManager = new CMDManager();
    private final Map<Integer,CmdModule> cmdMaps = new HashMap<>(10);

    private CMDManager(){}

    public static CMDManager getIntance(){
        return cmdManager;
    }

    public static int getCmd_M(int module, int cmd){
        return module<<8|cmd;
    }

    public void put(int module, int cmd, CmdModule cmdModule){
        synchronized (cmdMaps){
            if(cmdMaps.put(getCmd_M(module,cmd),cmdModule) != null){
                throw new RuntimeException("module:"+module+" cmd:"+cmd+" is repeate!");
            }
        }
    }

    public CmdModule getCmdModule(int cmd_m){
        return cmdMaps.get(cmd_m);
    }

    public CmdModule getCmdModule(int module, int cmd){
        return cmdMaps.get(getCmd_M(module,cmd));
    }

    public static int getCmd(int cmd_m){
        return cmd_m&0xFF;
    }

    public static int getModule(int cmd_m){
        return cmd_m>>8&0xFFFFF;
    }
}
