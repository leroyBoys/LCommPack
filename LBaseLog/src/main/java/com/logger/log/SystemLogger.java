package com.logger.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/4/4.
 */
public class SystemLogger {
    public static void error(Class clas,String logs){
        Logger log = LoggerFactory.getLogger(clas);
        log.error(logs);
    }

    public static void error(Class clas,Exception exception){
        Logger log = LoggerFactory.getLogger(clas);
        log.error("Exception",exception);
    }

    public static void info(Class clas,String logs){
        Logger log = LoggerFactory.getLogger(clas);
        log.info(logs);
    }

    public static void warn(Class clas,String logs){
        Logger log = LoggerFactory.getLogger(clas);
        log.warn(logs);
    }

}
