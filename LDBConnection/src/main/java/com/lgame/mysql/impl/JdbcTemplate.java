package com.lgame.mysql.impl;

import com.lgame.mysql.DbFactoryCache;
import com.lgame.util.LqLogUtil;
import com.lgame.mysql.DbCallBack;
import com.lgame.mysql.SqlDataSource;

import java.sql.*;
import java.util.*;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/13.
 */
@Deprecated
public class JdbcTemplate implements SqlDataSource {
    private SqlDataSource pool;
    private DataSourceType sourceType = DataSourceType.Druid;

    public JdbcTemplate(Properties properties){
        initPool(properties);
    }

    private void initPool(Properties properties){
        switch (sourceType){
            case Druid:
                pool = new DruidDataSourceImpl(properties);
                break;
            case Hikari:
                pool = new HikariDataSourceImpl(properties);
                break;
            default:
                pool = new DruidDataSourceImpl(properties);
                break;
        }
    }

    public JdbcTemplate(DataSourceType sourceType){
        this.sourceType = sourceType;
        initPool(null);
    }

    public JdbcTemplate(DataSourceType sourceType, Properties properties){
        this.sourceType = sourceType;
        initPool(properties);
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
            LqLogUtil.error(this.getClass(),e);
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
            LqLogUtil.error(this.getClass(),e);
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
            LqLogUtil.error(this.getClass(),e);
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
            LqLogUtil.error(this.getClass(),e);
        } finally {
            close(ps, cn, rs);
        }
        return -1;
    }

    public LinkedList<Map<String, Object>> ExecuteQuery(String cmd, Object... p) {
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();

            ps = cn.prepareStatement(cmd);
            SetParameter(ps, p);

            rs =  ps.executeQuery();
            ResultSetMetaData rsMeta = rs.getMetaData();

            String[] columsArray = null;
            LinkedList<Map<String, Object>> rows = new LinkedList<Map<String, Object>>();
            while (rs.next()){

                if(columsArray == null){
                    columsArray  = initColumsArray(rsMeta);
                }
                HashMap<String, Object> row = new HashMap<>(columsArray.length);
                for (int i = 0, size = columsArray.length; i < size; ++i) {
                    Object value = rs.getObject(i + 1);
                    row.put(columsArray[i], value);
                }

                rows.add(row);
            }
            return rows;
        } catch (Exception e) {
            LqLogUtil.error(this.getClass(),e);
        } finally {
            this.close(ps, cn, rs);
        }
        return null;
    }

    private String[] initColumsArray(ResultSetMetaData rsMeta) throws SQLException {
        String[] array = new String[rsMeta.getColumnCount()];
        for (int i = 0, size = array.length; i < size; ++i) {
            array[i] = rsMeta.getColumnLabel(i + 1);
        }
        return array;
    }

    /**
     * 只返回一个值
     * @param cmd
     * @param p
     * @return
     */
    public Object ExecuteQueryOnlyValue(String cmd, Object... p) {
        System.out.println("cmd:" + cmd);
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();

            ps = cn.prepareStatement(cmd);
            SetParameter(ps, p);

            rs =  ps.executeQuery();
            while (rs.next()){
                return rs.getObject(1);
            }
            return null;
        } catch (Exception e) {
            LqLogUtil.error(this.getClass(),e);
        } finally {
            this.close(ps, cn, rs);
        }
        return null;
    }

    public <T> T ExecuteQuery(String sql, DbCallBack<T> callBack) {
        System.out.println("cmd:" + sql);
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();

            ps = cn.prepareStatement(sql);

            rs =  ps.executeQuery();
           return callBack.doInPreparedStatement(rs);
        } catch (Exception e) {
            LqLogUtil.error(this.getClass(),e);
        } finally {
            this.close(ps, cn, rs);
        }
        return null;
    }

    public <T extends DbFactory> List<T> ExecuteQuery(T dbFactory,String cmd, Object... p) {
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
                T t = dbFactory.create(rs);
                rows.add(t);
            }
            return rows;
        } catch (Exception e) {
            LqLogUtil.error(this.getClass(),e);
        } finally {
            this.close(ps, cn, rs);
        }
        return null;
    }

    public <T extends DbFactory> List<T> ExecuteQuery(Class<T> dbClass,String cmd, Object... p) {
        return ExecuteQuery(DbFactoryCache.getInstance().getDbFactory(dbClass),cmd,p);
    }

    /**
     * 只返回一个对象
     * @param dbFactory
     * @param cmd
     * @param p
     * @param <T>
     * @return
     */
    public <T extends DbFactory> T ExecuteQueryOne(T dbFactory,String cmd, Object... p) {
        System.out.println("cm2d:" + cmd);
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();

            ps = cn.prepareStatement(cmd);
            SetParameter(ps, p);

            rs =  ps.executeQuery();
            if (!rs.next()){
                return null;
            }
            return dbFactory.create(rs);
        } catch (Exception e) {
            LqLogUtil.error(this.getClass(),e);
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
            LqLogUtil.error(this.getClass(),e);
        }
        try {
            if (cn != null) {
                cn.close();
            }
        } catch (Exception e) {
            LqLogUtil.error(this.getClass(),e);
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
            LqLogUtil.error(this.getClass(),e);
        }
        try {
            if (cn != null) {
                cn.close();
            }
        } catch (Exception e) {
            LqLogUtil.error(this.getClass(),e);
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