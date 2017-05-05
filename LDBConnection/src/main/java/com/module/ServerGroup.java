package com.module;

import com.mysql.impl.DbFactory;

import java.sql.ResultSet;

/**
 * Created by Administrator on 2017/4/26.
 */
public class ServerGroup implements DbFactory {
    public final static GameServer instance = new GameServer();
    private int group;
    private String sqlUrl;
    private String redisUrl;
    private String sqlUserName;
    private String sqlPwd;
    private String redisUserName;
    private String redisPwd;

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public String getSqlUrl() {
        return sqlUrl;
    }

    public void setSqlUrl(String sqlUrl) {
        this.sqlUrl = sqlUrl;
    }

    public String getRedisUrl() {
        return redisUrl;
    }

    public void setRedisUrl(String redisUrl) {
        this.redisUrl = redisUrl;
    }

    public String getSqlUserName() {
        return sqlUserName;
    }

    public void setSqlUserName(String sqlUserName) {
        this.sqlUserName = sqlUserName;
    }

    public String getSqlPwd() {
        return sqlPwd;
    }

    public void setSqlPwd(String sqlPwd) {
        this.sqlPwd = sqlPwd;
    }

    public String getRedisUserName() {
        return redisUserName;
    }

    public void setRedisUserName(String redisUserName) {
        this.redisUserName = redisUserName;
    }

    public String getRedisPwd() {
        return redisPwd;
    }

    public void setRedisPwd(String redisPwd) {
        this.redisPwd = redisPwd;
    }

    @Override
    public ServerGroup create(ResultSet rs) throws Exception {
        ServerGroup serverGroup = new ServerGroup();
        serverGroup.setGroup(rs.getInt("group"));
        serverGroup.setSqlUrl(rs.getString("sqlurl"));
        serverGroup.setSqlPwd(rs.getString("sql_pwd"));
        serverGroup.setSqlUserName(rs.getString("sql_user_name"));
        serverGroup.setRedisUrl(rs.getString("redisurl"));
        serverGroup.setRedisUserName(rs.getString("redis_user_name"));
        serverGroup.setRedisPwd(rs.getString("redis_pwd"));
        return serverGroup;
    }
}
