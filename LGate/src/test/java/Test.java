import com.gate.config.ServerSetting;
import com.lgame.util.json.JsonTool;
import com.lsocket.util.DefaultSocketPackage;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * Created by leroy:656515489@qq.com
 * 2017/4/6.
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {

        ServerSetting serverSetting = new ServerSetting();
        System.out.println(JsonTool.getJsonFromBean(serverSetting));

        IoBuffer ioBuffer = null;
        ioBuffer =  DefaultSocketPackage.transformHeartMsg();
        DefaultSocketPackage.detransMsg(ioBuffer);

        byte[] bytes = new byte[10];
        ioBuffer =  DefaultSocketPackage.transformErrorMsg(bytes,2);
        DefaultSocketPackage.detransMsg(ioBuffer);

        ioBuffer = DefaultSocketPackage.transformByteArray(bytes,3,(byte)111,(byte)111);
        DefaultSocketPackage.detransMsg(ioBuffer);

    }
}
