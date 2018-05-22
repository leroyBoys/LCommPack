package com;


import com.lgame.mysql.compiler.ScanEntitysTool;
import com.lgame.mysql.impl.LqJdbcPool;
import com.lgame.util.LqUtil;
import com.test.TestData;

import java.util.List;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/16.
 */
public class Test {
    @org.junit.Test
    public void test() throws Exception {
        ScanEntitysTool.instance("com");

        LqJdbcPool jdbcPool = new LqJdbcPool(LqJdbcPool.DataSourceType.Hikari, LqUtil.loadProperty("hikari_db.properties"));
        String sql = "SELECT test_data.* ,test1.`id` AS tid,test1.`name` AS tname FROM `test_data` RIGHT JOIN test1 ON test_data.`id` = test1.`id`";


        List<TestData> testDataList = jdbcPool.ExecuteQueryList(TestData.class,sql);

        System.out.println(testDataList.size());

        TestData testData = testDataList.get(1);
        testData.setName("tomess");
        jdbcPool.ExecuteEntity(testData);

        System.out.println(testData.getName());


        testData.setId(0);
        testData.setName("新增");
        jdbcPool.ExecuteEntity(testData);

        System.out.println(testData.getName()+"   "+testData.getId());
    }
}
