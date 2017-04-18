package com.gate.action.dao.mysql;

import com.gate.action.dao.mysql.template.ServerTemplate;
import com.gate.manager.ServerConnection;
import com.mysql.SqlDataSource;
import com.mysql.impl.SqlPool;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ServerService {
    private SqlPool sqlPool;

    public ServerService(SqlPool sqlPool){
        this.sqlPool = sqlPool;
    }

    public List<ServerConnection> getServerByGroup(int group){
        LinkedList<Map<String, Object>> ret = this.sqlPool.ExecuteQuery(ServerTemplate.GET_SERVER_BY_GROUP,group);
        if(ret == null){
            return null;
        }

        List<ServerConnection> servers = new LinkedList<>();
        for(Map<String, Object> serverMap:ret){
            servers.add(new ServerConnection((Integer) serverMap.get("id"),serverMap.get("name").toString(),
                    serverMap.get("ip").toString(),(Integer) serverMap.get("port")));
        }

        return servers;
    }
}
