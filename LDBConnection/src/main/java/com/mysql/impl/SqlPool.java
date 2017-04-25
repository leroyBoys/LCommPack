package com.mysql.impl;

import com.mysql.SqlDataSource;

import java.sql.*;
import java.util.*;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/13.
 */
public class SqlPool implements SqlDataSource {
    private SqlDataSource pool;
    private DataSourceType sourceType = DataSourceType.Druid;

    public SqlPool(){
        initPool(null);
    }

    public SqlPool(String propertieFileName){
        initPool(propertieFileName);
    }

    public SqlPool(DataSourceType sourceType){
        this.sourceType = sourceType;
        initPool(null);
    }

    public SqlPool(DataSourceType sourceType,String propertieFileName){
        this.sourceType = sourceType;
        initPool(propertieFileName);
    }

    private void initPool(String propertieFileName){
        switch (sourceType){
            case Druid:
                pool = new DruidDataSourceImpl(propertieFileName);
                break;
            case Hikari:
                pool = new HikariDataSourceImpl(propertieFileName);
                break;
            default:
                pool = new DruidDataSourceImpl(propertieFileName);
                    break;
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public void Execute(String cmd, Object... p) {
        System.out.println("cmd:" + cmd);
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(cmd);
            SetParameter(ps, p);
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(ps, cn);
        }
    }

    public boolean ExecuteUpdate(String cmd, Object[] p) {
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(cmd);
            SetParameter(ps, p);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(ps, cn);
        }
        return false;
    }

    public boolean ExecuteUpdates(List<String> cmds) {
        Connection cn = null;
        Statement ps = null;
        try {
            cn = getConnection();
            cn.setAutoCommit(false);
            ps = cn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            for (String cmd : cmds) {
                ps.executeUpdate(cmd);
            }
            cn.commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.close(ps, cn);
        }
        return false;
    }

    public long Insert(String sql, Object... p) {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            SetParameter(ps, p);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return (Long) rs.getObject(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(ps, cn, rs);
        }
        return -1;
    }

    public LinkedList<Map<String, Object>> ExecuteQuery(String cmd, Object... p) {
        System.out.println("cmd:" + cmd);
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();

            ps = cn.prepareStatement(cmd);
            SetParameter(ps, p);

            rs =  ps.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();

            LinkedList<Map<String, Object>> rows = new LinkedList<Map<String, Object>>();
            while (rs.next()){
                Map<String, Object> row = new LinkedHashMap<String, Object>();

                for (int i = 0, size = rsMeta.getColumnCount(); i < size; ++i) {
                    String columName = rsMeta.getColumnLabel(i + 1);
                    Object value = rs.getObject(i + 1);
                    row.put(columName, value);
                }

                rows.add(row);
            }
            return rows;
        } catch (Exception e) {
        } finally {
            this.close(ps, cn, rs);
        }
        return null;
    }

    public <T extends DbFactory> List<T> ExecuteQuery(DbFactory<T> dbFactory,String cmd, Object... p) {
        System.out.println("cm2d:" + cmd);
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();

            ps = cn.prepareStatement(cmd);
            SetParameter(ps, p);

            rs =  ps.executeQuery();
            List<T> rows = new ArrayList<>();
            while (rs.next()){
                rows.add(dbFactory.create(rs));
            }
            return rows;
        } catch (Exception e) {
        } finally {
            this.close(ps, cn, rs);
        }
        return null;
    }

    void SetParameter(PreparedStatement stmt, Object[] parameters)
            throws SQLException {
        if (parameters == null) {
            return;
        }
        for (int i = 0, size = parameters.length; i < size; ++i) {
            Object param = parameters[i];
            stmt.setObject(i + 1, param);
        }
    }

    void close(Statement ps, Connection cn) {
        try {
            if (ps != null) {
                ps.close();
            }

        } catch (Exception e) {
        }
        try {
            if (cn != null) {
                cn.close();
            }
        } catch (Exception e) {
        }
        ps = null;
        cn = null;
    }

    private void close(PreparedStatement ps, Connection cn, ResultSet rs) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (Exception e) {
        }
        try {
            if (cn != null) {
                cn.close();
            }
        } catch (Exception e) {
        }

        try {
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
        }
        ps = null;
        cn = null;
        rs = null;
    }

    public enum DataSourceType{
        Druid,Hikari
    }
}
