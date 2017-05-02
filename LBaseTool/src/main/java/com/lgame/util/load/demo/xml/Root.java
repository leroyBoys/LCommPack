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

    @XmlElement(name = "cmd")
    public List<Cmd> getCmds() {
        return cmds;
    }

    public void setCmds(List<Cmd> cmds) {
        this.cmds = cmds;
    }
    private List<Cmd> cmds;
    public void addCmd(Cmd c) {
        if(cmds == null){
            cmds = new ArrayList<>();
        }
        cmds.add(c);
    }

    public static class Cmd{
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

        private int cmd;
        private boolean isNeedLogin;
        /** 该消息处理所在服务器serverType */
        private int serverType;
    }
}
