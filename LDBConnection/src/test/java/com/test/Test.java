package com.test;

import com.lgame.util.PrintTool;
import com.lgame.util.file.PropertiesTool;
import com.lgame.util.poi.ExcelHelper;
import com.lgame.util.poi.interfac.DbService;
import com.lgame.util.poi.module.DefaultDbEntity;
import com.lgame.util.poi.module.ExcelConfig;
import com.mysql.DbCallBack;
import com.mysql.compiler.ScanEntitysTool;
import com.mysql.impl.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * Created by leroy:656515489@qq.com
 * 2018/5/10.
 */
public class Test {
    public static  void main(String[] args) throws Exception {
        //扫描包，可以多个包
        ScanEntitysTool.scan("com.test");
        JdbcTemplate db = new JdbcTemplate(JdbcTemplate.DataSourceType.Hikari, PropertiesTool.loadProperty("hikari_db.properties"));
        /*JdbcTemplate db = new JdbcTemplate(JdbcTemplate.DataSourceType.Druid, PropertiesTool.loadProperty("druid_db.properties"));*/
        PrintTool.outTime("1","===>");///开始计时

        Map<String,ExcelConfig.ExcelColumConverter> excelColumConverterMap = new HashMap<>();
        excelColumConverterMap.put("type",new ExcelConfig.ExcelColumConverter(){
            @Override
            public String converter(String value) {
                if("天猫".equals(value)){
                    return "1";
                }else if("淘宝".equals(value)){
                    return "2";
                }
                return "3";
            }
        });

        ExcelHelper.importDBFromExcel(new DbService() {
            @Override
            public List<String> queryExistIds(String sql) {
                List<String> ss = db.ExecuteQuery(sql, new DbCallBack<List<String>>() {
                    @Override
                    public List<String> doInPreparedStatement(ResultSet rs) {
                        List<String> ids = new LinkedList<>();
                        try {
                            while (rs.next()){
                                ids.add(rs.getString(1));
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        return ids;
                    }
                });
                return ss;
            }
            @Override
            public boolean excute(List<String> sqls) {
                return db.ExecuteUpdates(sqls);
            }

            @Override
            public boolean excute(String sql) {
                return db.ExecuteUpdate(sql,null);
            }

            @Override
            public boolean insertBatchs(String tableName, List<Map<String, String>> datas, String[] columNames, String[] columValues, int commitLimitCount) {
                return db.InsertBatch(tableName,datas,columNames,columValues,commitLimitCount);
            }
        },null,new DefaultDbEntity(),"D:/shop.xls","D:/test.xls",excelColumConverterMap);

        PrintTool.outTime("1","===>");///结束计时
    }
}
