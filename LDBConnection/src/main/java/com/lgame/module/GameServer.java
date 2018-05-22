package com.lgame.module;

import com.lgame.mysql.entity.LQDBTable;
import com.lgame.mysql.entity.LQField;
import com.lgame.mysql.impl.DbFactory;

import java.sql.ResultSet;

/**
 * Created by Administrator on 2017/4/26.
 */
@LQDBTable
public class GameServer extends DbFactory {
    public final static GameServer instance = new GameServer();

    @LQField(isPrimaryKey = true)
    private int id;
    private int g_v_id;
    private String zoneName;
    private String zoneDes;
    private String zoneIcon;
    private ServerType serverType;
    private int groupNum;
    private String ip;
    private int udpPort;
    private int port;
    private int maxCount;
    private int severStatus;
    private String key;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getG_v_id() {
        return g_v_id;
    }

    public void setG_v_id(int g_v_id) {
        this.g_v_id = g_v_id;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getZoneDes() {
        return zoneDes;
    }

    public String getZoneIcon() {
        return zoneIcon;
    }

    public void setZoneIcon(String zoneIcon) {
        this.zoneIcon = zoneIcon;
    }

    public void setZoneDes(String zoneDes) {
        this.zoneDes = zoneDes;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public void setServerType(ServerType serverType) {
        this.serverType = serverType;
    }

    public int getGroupNum() {
        return groupNum;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setGroupNum(int groupNum) {
        this.groupNum = groupNum;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getSeverStatus() {
        return severStatus;
    }

    public void setSeverStatus(int severStatus) {
        this.severStatus = severStatus;
    }

    @Override
    public GameServer create(ResultSet rs) throws Exception {
        GameServer gameServer = createNew();
        gameServer.setId(rs.getInt("id"));
        gameServer.setG_v_id(rs.getInt("g_v_id"));
        gameServer.setGroupNum(rs.getInt("zone_num"));
        gameServer.setIp(rs.getString("ip"));
        gameServer.setMaxCount(rs.getInt("max_count"));
        gameServer.setPort(rs.getInt("port"));
        gameServer.setServerType(ServerType.valueOf(rs.getString("server_type")));
        gameServer.setSeverStatus(rs.getInt("server_status"));
        gameServer.setUdpPort(rs.getInt("udp_port"));
        gameServer.setZoneDes(rs.getString("zone_desc"));
        gameServer.setZoneName(rs.getString("zone_name"));
        gameServer.setZoneIcon(rs.getString("zone_icon"));
        gameServer.setKey(rs.getString("key"));
        return gameServer;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override

    protected GameServer createNew() {
        return new GameServer();
    }

    public enum ServerType{
        server,gate
    }
}
