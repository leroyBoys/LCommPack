package com.gate.action.dao.mysql;

import com.gate.action.dao.mysql.template.ServerTemplate;
import com.gate.manager.ServerConnection;
import com.lgame.module.GameServer;
import com.lgame.module.ServerGroup;
import com.lgame.mysql.impl.JdbcTemplate;

import java.util.List;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ServerService {
    private JdbcTemplate sqlPool;

    public ServerService(JdbcTemplate sqlPool){
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
