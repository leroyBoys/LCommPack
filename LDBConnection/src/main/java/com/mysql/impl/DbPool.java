package com.mysql.impl;

import com.lgame.util.PrintTool;
import com.mysql.DbCallBack;
import com.mysql.SqlDataSource;
import com.mysql.compiler.ColumInit;
import com.mysql.compiler.ScanEntitysTool;
import com.mysql.entity.*;

import java.sql.*;
import java.util.*;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/13.
 */
public class DbPool implements SqlDataSource {
    private final static  Map<String,JdbcColumsArray> cmd_jdbcColumsArrayCache = new HashMap<>();
    private SqlDataSource pool;
    private DataSourceType sourceType = DataSourceType.Druid;

    public DbPool(Properties properties){
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

    public DbPool(DataSourceType sourceType){
        this.sourceType = sourceType;
        initPool(null);
    }

    public DbPool(DataSourceType sourceType, Properties properties){
        this.sourceType = sourceType;
        initPool(properties);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return pool.getConnection();
    }

    public void Execute(String cmd, Object... p) {
        PrintTool.log("cmd:" + cmd);
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(cmd);
            SetParameter(ps, p);
            ps.execute();
        } catch (Exception e) {
            PrintTool.error(this.getClass(),e);
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
            PrintTool.error(this.getClass(),e);
        } finally {
            this.close(ps, cn);
        }
        return false;
    }

    /**
     *
     * @param tableName
     * @param datas
     * @param columNames 要插入数据的列名集合(与columValues 顺序对应)
     * @param columValues 要插入数据的列名对应值（或者函数或者固定值）集合(与columValues 顺序对应)
     * @param commitLimitCount 最大提交数量（根据mysql.cnf中 max_allowed_packet调整）如果小于等于0则为默认5000
     * @param
     * @return
     */
    public  boolean InsertBatch(String tableName,List<Map<String,String>> datas,String[] columNames,String[] columValues,int commitLimitCount) {
        if(commitLimitCount <= 0){
            commitLimitCount = this.getDefaultLimitCount();
        }

        StringBuilder sb = new StringBuilder("INSERT INTO  ");
        sb.append(tableName).append(" (");
        for(int i = 0;i<columNames.length;i++){
            if(i != 0){
                sb.append(",");
            }
            sb.append("`").append(columNames[i]).append("`");
        }
        sb.append(") values ");
        String sql = sb.toString();

        String str;
        List<String> sqls = new LinkedList<>();
        int i = 0;
        for(Map<String,String> map:datas){
            if(i++ != 0){
                sb.append(",");
            }

            sb.append("(");
            for(int j = 0;j<columNames.length;j++){
                if(j != 0){
                    sb.append(",");
                }

                str = columValues[j];
                if(str.endsWith("()")){
                    sb.append(str);
                    continue;
                }

                sb.append("'");
                str = map.get(str);
                if(str == null){
                    str = columValues[j];
                }
                sb.append(str);
                sb.append("'");
            }
            sb.append(")");

            if(i > commitLimitCount){
                sqls.add(sb.toString());
                sb=new StringBuilder(sql);
                i=0;
            }
        }

        if(i > 0){
            sqls.add(sb.toString());
        }

        return ExecuteUpdates(sqls);
    }

    public boolean ExecuteUpdates(List<String> cmds) {
        Connection cn = null;
        Statement ps = null;
        try {
            cn = getConnection();
            cn.setAutoCommit(false);
            ps = cn.createStatement();
            for (String cmd : cmds) {
                ps.addBatch(cmd);
            }
            ps.executeBatch();
            cn.commit();
            return true;
        } catch (Exception e) {
            PrintTool.error(this.getClass(),e);
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
            PrintTool.error(this.getClass(),e);
        } finally {
            close(ps, cn, rs);
        }
        return -1;
    }

    /**
     * 只返回一个值
     * @param cmd
     * @param p
     * @return
     */
    public Object ExecuteQueryOnlyValue(String cmd, Object... p) {
 //       PrintTool.log("cmd:" + cmd);
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
            PrintTool.error(this.getClass(),e);
        } finally {
            this.close(ps, cn, rs);
        }
        return null;
    }

    public <T> T ExecuteQuery(String sql, DbCallBack<T> callBack) {
        //PrintTool.log("cmd:" + sql);
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();

            ps = cn.prepareStatement(sql);

            rs =  ps.executeQuery();
           return callBack.doInPreparedStatement(rs);
        } catch (Exception e) {
            PrintTool.error(this.getClass(),e);
        } finally {
            this.close(ps, cn, rs);
        }
        return null;
    }

    private <T> JdbcColumsArray initColumsArray(ResultSetMetaData rsMeta, DBTable dbTable) throws SQLException {
        String[] array = new String[rsMeta.getColumnCount()];

        final Map<String,Map<String,ColumInit>> relationFieldNames = new HashMap<>(2);
        String str;
        RelationData relationData;
        for (int i = 0, size = array.length; i < size; ++i) {
            str = rsMeta.getColumnLabel(i + 1);
            array[i] = str;
            if(dbTable.getColumInit(str) != null){
                continue;
            }
            relationData = dbTable.getRelationMap(str);
            if(relationData == null){
                continue;
            }

            if(!relationFieldNames.containsKey(relationData.getFieldName())){
                relationFieldNames.put(relationData.getFieldName(),new HashMap<>());
            }
        }

        if(relationFieldNames.isEmpty()){
           return new JdbcColumsArray(array);
        }

        Set<String> cols = new HashSet<>(array.length);
        for(int i = 0;i<array.length;i++){
            cols.add(array[i]);
        }

        for(Map.Entry<String,Map<String,ColumInit>> entry:relationFieldNames.entrySet()){
            relationData = dbTable.getRelationByFieldName(entry.getKey());
            for(Map.Entry<String,ColumInit> entry1:relationData.getColums_target_map().entrySet()){
                if(cols.contains(entry1.getKey())){
                    entry.getValue().put(entry1.getKey(),entry1.getValue());
                }
            }
        }

        return new MoreJdbcColumsArray(array,relationFieldNames);
    }

    private <T> JdbcColumsArray getJdbcColumsArray(ResultSet rs,Class<T> cls,String cmd,DBTable dbTable) throws SQLException {
        JdbcColumsArray jdbcColumsArray = cmd_jdbcColumsArrayCache.get(cmd);
        if(jdbcColumsArray != null){
            return jdbcColumsArray;
        }

        synchronized (cls) {
            jdbcColumsArray = cmd_jdbcColumsArrayCache.get(cmd);
            if (jdbcColumsArray != null) {
                return jdbcColumsArray;
            }

            jdbcColumsArray = initColumsArray(rs.getMetaData(),dbTable);
            cmd_jdbcColumsArrayCache.put(cmd, jdbcColumsArray);
            return jdbcColumsArray;
        }
    }

    public <T> List<T> ExecuteQuery(Class<T> cls,String cmd, Object... p) {
        //PrintTool.log("cm2d:" + cmd);
        DBTable dbTable = ScanEntitysTool.getDBTable(cls);
        if(dbTable == null){
            throw new RuntimeException(cls.getSimpleName()+" not config dbentity");
        }

        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();

            ps = cn.prepareStatement(cmd);
            SetParameter(ps, p);

            rs =  ps.executeQuery();
            JdbcColumsArray jdbcColumsArray = getJdbcColumsArray(rs,cls,cmd,dbTable);

            QueryResultData<T> resultData = new QueryResultData<>();
            while (rs.next()){
                jdbcColumsArray.doExute(dbTable,rs,cls,resultData);
            }
            return resultData.getResult();
        } catch (Exception e) {
            PrintTool.error(this.getClass(),e);
        } finally {
            this.close(ps, cn, rs);
        }
        return null;
    }

    /**
     * 只返回一个对象
     * @param cls
     * @param cmd
     * @param p
     * @param <T>
     * @return
     */
    public <T> T ExecuteQueryOne(Class<T> cls,String cmd, Object... p) {
        PrintTool.log("ExecuteQueryOne cmd:" + cmd);

        DBTable dbTable = ScanEntitysTool.getDBTable(cls);
        if(dbTable == null){
            throw new RuntimeException(cls.getSimpleName()+" not config dbentity");
        }

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
            return getJdbcColumsArray(rs,cls,cmd,dbTable).doExuteOnlyOne(dbTable,rs,cls);
        } catch (Exception e) {
            PrintTool.error(this.getClass(),e);
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
            PrintTool.error(this.getClass(),e);
        }
        try {
            if (cn != null) {
                cn.close();
            }
        } catch (Exception e) {
            PrintTool.error(this.getClass(),e);
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
            PrintTool.error(this.getClass(),e);
        }
        try {
            if (cn != null) {
                cn.close();
            }
        } catch (Exception e) {
            PrintTool.error(this.getClass(),e);
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

    public int getDefaultLimitCount() {
        return 5000;
    }

    public enum DataSourceType{
        Druid,Hikari
    }
}