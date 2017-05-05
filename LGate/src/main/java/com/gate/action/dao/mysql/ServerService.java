package com.gate.action.dao.mysql;

import com.gate.action.dao.mysql.template.ServerTemplate;
import com.gate.manager.ServerConnection;
import com.module.GameServer;
import com.module.ServerGroup;
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
        List<ServerConnection> ret = this.sqlPool.ExecuteQuery(new ServerConnection(),ServerTemplate.GET_SERVER_BY_GROUP,group);
        return ret;
    }

    public GameServer getServerById(int gameId){
        GameServer server = this.sqlPool.ExecuteQueryOne(GameServer.instance,ServerTemplate.GET_SERVER_BY_ID,gameId);
        if(server == null){
            return null;
        }

        return server;
    }

    public ServerGroup getServerGroup(int groupNum){
        ServerGroup serverGroup = this.sqlPool.ExecuteQueryOne(ServerGroup.instance,ServerTemplate.GET_SERVERGROUP,groupNum);
        if(serverGroup == null){
            return null;
        }
        return serverGroup;
    }
}
