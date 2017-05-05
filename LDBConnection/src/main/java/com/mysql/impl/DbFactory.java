package com.mysql.impl;

import java.sql.ResultSet;

/**
 * Created by Administrator on 2017/4/20.
 */
public abstract class DbFactory {

    public abstract <T extends DbFactory>  T create(ResultSet rs) throws Exception;

    protected abstract  <T extends DbFactory> T createNew();
}
