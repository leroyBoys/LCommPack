import com.lgame.util.PrintTool;
import com.lgame.util.comm.StringTool;
import com.lgame.util.file.PropertiesTool;
import com.lgame.util.json.JsonTool;
import com.module.GameServer;
import com.mysql.compiler.ScanEntitysTool;
import com.mysql.impl.JdbcTemplate;
import com.test.TestData;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/2.
 */
public class Test {


    public void go() {
        String url = "redis://@119.254.166.136:6379";
        String[] firstArray = url.split("//");
        String[] secondArray = firstArray[1].split("@");
        int db = 0;
        if(!secondArray[0].trim().isEmpty()){
            db = Integer.valueOf(secondArray[0]);
        }

        String[] threeArray = secondArray[1].split(":");
        String host = threeArray[0];
        int port = Integer.valueOf(threeArray[1].split("/")[0]);

        String password = null;
        if(threeArray[1].split("/").length>1){
            password = StringTool.trimToNull(threeArray[1].split("/")[1]);
        }

        System.out.println("db:"+db+"  ip:"+host+"  port:"+port+"  pwd:"+password);
    }

    @org.junit.Test
    public void thres() throws Exception {
        testJdkReflect();
     //   go();
      //  testReflectAsm();
     //   tttDB22();

      /*  Map<String,String> map = new HashMap<>();
        map.put("id","ss");

        final ClassIntrospector ci = new ClassIntrospector();

        ObjectInfo res;

        res = ci.introspect(map);
        System.out.println( res.getDeepSize() );*/
    }

    public static void testJdkReflect() throws Exception {
        ScanEntitysTool.scan("com.mysql.entity");
        String sql = "SELECT * FROM `test_data`";
        JdbcTemplate db = new JdbcTemplate(JdbcTemplate.DataSourceType.Druid,PropertiesTool.loadProperty("druid_db.properties"));



        PrintTool.outTime("1","===>");

        List<TestData> testDatas =  db.ExecuteQuery(TestData.class,sql);
        System.out.println(JsonTool.getJsonFromBean(testDatas));


        PrintTool.outTime("1","===>");

    }

    public void testReflectAsm() throws Exception  {
        long now;
        long sum = 0;
        Class<?> c222 = GameServer.class;

        now = System.currentTimeMillis();

        for(int i = 0; i<500000000; ++i){
            GameServer t = new GameServer();
            t.setId(i);
            sum += t.getId();
        }
        System.out.println("get-set耗时"+(System.currentTimeMillis() - now) + "ms秒，和是" +sum);
        ttt();
        sum = 0;
        now = System.currentTimeMillis();

        for(int i = 0; i<5000000; ++i){
            Class<?> c = GameServer.class;
            GameServer o = (GameServer) c.newInstance();
            Class<?>[] argsType = new Class[1];
            argsType[0] = int.class;

            Method m = c.getMethod("setId",argsType);
            m.invoke(o, i);
            sum += o.getId();
        }
        System.out.println("标准反射耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);
        ttt2();
        sum = 0;

        Map<String,Object> maps = new HashMap<>();
        maps.put("id",1);
        now = System.currentTimeMillis();

        for(int i = 0; i<500000000; ++i){
            sum +=  (int)maps.get("id");
        }
        System.out.println("map耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);
        sum = 0;

        now = System.currentTimeMillis();

        for(int i = 0; i<500000000; ++i){
            Map<String,Object> map = new HashMap<>(1);
            map.put("id",i);
            sum +=  (int)map.get("id");
        }
        System.out.println("map222耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);
        sum = 0;
    }

    private void ttt2() throws Exception {
        long sum = 0;
        Class<?>[] argsType = new Class[1];
        argsType[0] = int.class;
        Class<?> c = GameServer.class;
        Map<String,Method> methodMap = new HashMap<>();
        methodMap.put("setId",c.getMethod("setId",argsType));
        long now = System.currentTimeMillis();
        for(int i = 0; i<500000000; ++i){
            GameServer o = new GameServer();

            methodMap.get("setId").invoke(o, i);
            sum += o.getId();
        }
        System.out.println("缓存反射耗时2222:::"+(System.currentTimeMillis() - now) + "ms，和是" +sum);

    }
    private void ttt() throws Exception {
        long sum = 0;
        Class<?>[] argsType = new Class[1];
        argsType[0] = int.class;
        Class<?> c = GameServer.class;
        Method m = c.getMethod("setId",argsType);

        long now = System.currentTimeMillis();
        for(int i = 0; i<500000000; ++i){
            GameServer o = (GameServer) c.newInstance();

            m.invoke(o, i);
            sum += o.getId();
        }
        System.out.println("缓存反射耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);

    }
    //redis://db@119.254.166.136:6379/pwd
    private void tttDB22() throws Exception {
       // RedisConnectionImpl redisConnectionManager = new RedisConnectionImpl("redis://0@192.168.11.128:3307");
      /*  RedisConnectionImpl redisConnectionManager = new RedisConnectionImpl("redis://0@127.0.0.1:6379");
        redisConnectionManager.hset("abc","abc",100+"");
        long sum = 0;

        long now = System.currentTimeMillis();
        for(int i = 0; i<50000; ++i){
            String obj = redisConnectionManager.hget("abc","abc");
            GameServer o = new GameServer();
            o.setId(i);
            sum += o.getId();
        }
        System.out.println("===========db耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);


        sum = 0;
        now = System.currentTimeMillis();
        Cache<Integer,GameServer> gameServerCache = EchCacheManager.getCache("abc",Integer.class,GameServer.class);
        for(int i = 0; i<500; ++i){
            GameServer o = new GameServer();
            o.setId(i);
            gameServerCache.put(i,o);
        }
        System.out.println("========set cache时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);
        now = System.currentTimeMillis();

        for(int i = 0; i<500; ++i){
            GameServer o = gameServerCache.get(i);
            if(o == null){
                continue;
            }
            System.out.println(o.getId());
            sum += o.getId();
        }
        System.out.println("========555===db耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);*/

    }

    private void tttDB() throws Exception {
        JdbcTemplate db = new JdbcTemplate(PropertiesTool.loadProperty("druid_db.properties"));

        long sum = 0;

        long now = System.currentTimeMillis();
        for(int i = 0; i<5000; ++i){
            Object obj = db.ExecuteQueryOnlyValue("select id from test1");
            if(i < 2){
                System.out.println(obj);
            }
            GameServer o = new GameServer();
            o.setId(i);
            sum += o.getId();
        }
        System.out.println("===========db耗时"+(System.currentTimeMillis() - now) + "ms，和是" +sum);

    }

}
