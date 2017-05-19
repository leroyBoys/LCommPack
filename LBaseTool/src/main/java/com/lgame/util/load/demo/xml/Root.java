package com.lgame.util.load.demo.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/28.
 */
@XmlRootElement(name = "root")
public class Root {
    private List<Root> cmds;
    private int cmd;
    private int type;
    private boolean isNeedLogin;
    /** 该消息处理所在服务器serverType */
    private int serverType;

    @XmlAttribute(name = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @XmlAttribute(name = "value")
    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    @XmlAttribute(name = "needLogin")
    public boolean isNeedLogin() {
        return isNeedLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        isNeedLogin = needLogin;
    }

    @XmlAttribute(name = "serverType")
    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }
    @XmlElement(name = "cmd")
    public List<Root> getCmds() {
        return cmds;
    }

    public void setCmds(List<Root> cmds) {
        this.cmds = cmds;
    }

    public void addCmd(Root c) {
        if(cmds == null){
            cmds = new ArrayList<>();
        }
        cmds.add(c);
    }

    public enum Type{
        Admin(0),
        Test(1);
        private final int val;
        Type(int val){
            this.val = val;
        }

        public int getVal() {
            return val;
        }

        public static Type valuOf(int val){
            for(Type type:Type.values()){
                if(type.getVal() == val){
                    return type;
                }
            }
            return null;
        }
    }
}
