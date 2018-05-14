import com.lsocket.handler.ModuleHandler;

import java.util.List;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {

        List<Class> lsd =  ClassUtils.getAllImplClassesByInterface(ModuleHandler.class);
        if(lsd == null|| lsd.isEmpty()){
            System.out.println("=====is empty");
           return;
        }
        for(Class c:lsd){
            System.out.println(c.getName());
        }
      /*  ServerSetting serverSetting = new ServerSetting();
        System.out.println(JsonTool.getJsonFromBean(serverSetting));

        IoBuffer ioBuffer = null;
        ioBuffer =  DefaultSocketPackage.transformHeartMsg();
        DefaultSocketPackage.detransMsg(ioBuffer);

        byte[] bytes = new byte[10];
        ioBuffer =  DefaultSocketPackage.transformErrorMsg(bytes,2);
        DefaultSocketPackage.detransMsg(ioBuffer);

        ioBuffer = DefaultSocketPackage.transformByteArray(bytes,3,(byte)111,(byte)111);
        DefaultSocketPackage.detransMsg(ioBuffer);*/

    }
}
