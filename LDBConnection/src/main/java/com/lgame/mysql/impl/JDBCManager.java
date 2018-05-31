package com.lgame.mysql.impl;

import com.lgame.entity.Node;
import com.lgame.entity.NodeManger;

import java.util.Properties;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/24.
 */
public class JDBCManager extends NodeManger<LQDataSource> {

    @Override
    protected Node<LQDataSource> intanceNode(boolean slowSlaveOn) {
        return new JdbcNode(slowSlaveOn);
    }

}
