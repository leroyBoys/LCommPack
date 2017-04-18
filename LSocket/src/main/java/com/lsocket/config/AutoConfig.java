package com.lsocket.config;

import com.lgame.util.load.PropertiesHelper;

/**
 * Created by Administrator on 2017/4/15.
 */
public class AutoConfig {
    protected AutoConfig(){
        PropertiesHelper.processProperties(this);
    }
}
