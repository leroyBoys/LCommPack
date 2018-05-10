package com.test;

import com.lgame.util.PrintTool;
import com.lgame.util.file.PropertiesTool;
import com.lgame.util.json.JsonTool;
import com.mysql.compiler.ScanEntitysTool;
import com.mysql.impl.JdbcTemplate;

import java.util.List;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/10.
 */
public class Test {
    public static  void main(String[] args) throws Exception {
        //扫描包，可以多个包
        ScanEntitysTool.scan("com.test");
        JdbcTemplate db = new JdbcTemplate(JdbcTemplate.DataSourceType.Druid, PropertiesTool.loadProperty("druid_db.properties"));


        String sql = "SELECT * FROM `test_data`";
        PrintTool.outTime("1","===>");///开始计时

        List<TestData> testDatas =  db.ExecuteQuery(TestData.class,sql);
        System.out.println(JsonTool.getJsonFromBean(testDatas));


        PrintTool.outTime("1","===>");///结束计时
    }
}
